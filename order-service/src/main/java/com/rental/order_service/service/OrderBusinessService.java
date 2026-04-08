package com.rental.order_service.service;

import com.rental.order_service.dto.*;
import com.rental.order_service.entity.ChiTietPhieuThue;
import com.rental.order_service.entity.PhieuThue;
import com.rental.order_service.enums.HinhThucThue;
import com.rental.order_service.enums.PhieuThueStatus;
import com.rental.order_service.enums.TrangThaiDatCoc;
import com.rental.order_service.repository.PhieuThueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(OrderBusinessService.class);

    private final PhieuThueRepository repo;
    private final RestTemplate restTemplate;

    @Value("${service.customer-url}")
    private String customerUrl;

    @Value("${service.costume-url}")
    private String costumeUrl;

    public OrderBusinessService(PhieuThueRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    // ─────────────────────────── Tạo phiếu thuê ──────────────────────────────

    public PhieuThueDto createOrder(Long khachHangId, CreateOrderRequest req) {
        logger.info("Tạo phiếu thuê cho khách hàng id={}", khachHangId);

        if (!req.getNgayHenTra().isAfter(req.getNgayHenLay())) {
            throw new RuntimeException("Ngày hẹn trả phải sau ngày hẹn lấy");
        }

        logger.info("Gọi costume-service lấy danh sách trang phục còn hàng");
        TrangPhucDto[] available;
        try {
            available = restTemplate.getForObject(costumeUrl + "/available", TrangPhucDto[].class);
        } catch (Exception e) {
            logger.error("Không thể kết nối costume-service: {}", e.getMessage());
            throw new RuntimeException("Không thể kết nối đến costume-service");
        }
        if (available == null || available.length == 0) {
            throw new RuntimeException("Không có trang phục nào còn hàng để tạo phiếu thuê");
        }

        PhieuThue pt = new PhieuThue(
                null,
                "PT-" + khachHangId + "-" + System.currentTimeMillis(),
                khachHangId,
                LocalDate.now(),
                req.getNgayHenLay(),
                req.getNgayHenTra(),
                HinhThucThue.ONLINE,
                PhieuThueStatus.CHO_XU_LY,
                0.0,
                TrangThaiDatCoc.CHUA_THANH_TOAN
        );

        // Thêm tất cả trang phục còn hàng vào phiếu thuê và đổi trạng thái sang RENTED
        for (TrangPhucDto tp : available) {
            ChiTietPhieuThue ct = new ChiTietPhieuThue(
                    null, null,
                    tp.getId(),
                    1, tp.getGiaThue()
            );
            pt.getChiTiet().add(ct);

            try {
                restTemplate.patchForObject(
                        costumeUrl + "/" + tp.getId() + "/trang-thai?trangThai=RENTED",
                        null, Void.class);
                logger.info("Đã cập nhật trạng thái trang phục id={} → RENTED", tp.getId());
            } catch (Exception e) {
                logger.warn("Không thể cập nhật trạng thái trang phục id={}: {}", tp.getId(), e.getMessage());
            }
        }

        double tienCoc = pt.getChiTiet().stream()
                .mapToDouble(ct -> ct.getDonGia() != null ? ct.getDonGia() : 0)
                .sum() * 0.3;
        pt.setTienDatCoc(tienCoc);

        PhieuThue saved = repo.save(pt);
        logger.info("Đã tạo phiếu thuê mã={} với {} trang phục, tiền cọc={}",
                saved.getMaPhieu(), saved.getChiTiet().size(), tienCoc);
        return toDto(saved);
    }

    // ─────────────────────────── Query ───────────────────────────────────────

    public List<PhieuThueDto> getByKhachHang(Long khachHangId) {
        logger.info("Lấy phiếu thuê của khách hàng id={}", khachHangId);
        List<PhieuThueDto> result = repo.findByKhachHangId(khachHangId)
                .stream().map(this::toDto).collect(Collectors.toList());
        logger.info("Trả về {} phiếu thuê", result.size());
        return result;
    }

    public PhieuThueDto getByMaPhieu(String maPhieu) {
        logger.info("Lấy phiếu thuê mã={}", maPhieu);
        PhieuThue pt = repo.findByMaPhieu(maPhieu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu thuê mã=" + maPhieu));
        return toDto(pt);
    }

    // ─────────────────────────── Xác nhận đặt cọc ────────────────────────────

    public PhieuThueDto confirmDatCoc(String maPhieu) {
        logger.info("Xác nhận đặt cọc phiếu thuê mã={}", maPhieu);
        PhieuThue pt = repo.findByMaPhieu(maPhieu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu thuê mã=" + maPhieu));
        pt.setTrangThaiDatCoc(TrangThaiDatCoc.DA_THANH_TOAN);
        pt.setTrangThai(PhieuThueStatus.CHO_XAC_NHAN);
        logger.info("Khách đã thanh toán đặt cọc phiếu thuê mã={}", maPhieu);
        return toDto(repo.save(pt));
    }

    // ─────────────────────────── Mapping helper ──────────────────────────────

    private PhieuThueDto toDto(PhieuThue pt) {
        String tenKhachHang = "";
        try {
            KhachHangResponseDto kh = restTemplate.getForObject(
                    customerUrl + "/" + pt.getKhachHangId(), KhachHangResponseDto.class);
            if (kh != null) tenKhachHang = kh.getHoTen();
        } catch (Exception e) {
            logger.warn("Không thể lấy tên khách hàng id={}: {}", pt.getKhachHangId(), e.getMessage());
        }

        List<ChiTietPhieuThueDto> chiTiet = pt.getChiTiet().stream()
                .map(ct -> new ChiTietPhieuThueDto(
                        ct.getId(), ct.getTrangPhucId(),
                        ct.getSoLuong(), ct.getDonGia()))
                .collect(Collectors.toList());

        return new PhieuThueDto(
                pt.getId(), pt.getMaPhieu(), pt.getKhachHangId(), tenKhachHang,
                pt.getNgayTao(), pt.getNgayHenLay(), pt.getNgayHenTra(),
                pt.getHinhThuc(), pt.getTrangThai(),
                pt.getTienDatCoc(), pt.getTrangThaiDatCoc(), chiTiet
        );
    }
}