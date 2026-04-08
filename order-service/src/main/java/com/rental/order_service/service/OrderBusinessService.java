package com.rental.order_service.service;

import com.rental.order_service.dto.*;
import com.rental.order_service.entity.ChiTietPhieuThue;
import com.rental.order_service.entity.ChiTietGioHang;
import com.rental.order_service.entity.GioHang;
import com.rental.order_service.entity.PhieuThue;
import com.rental.order_service.enums.HinhThucThue;
import com.rental.order_service.enums.PhieuThueStatus;
import com.rental.order_service.enums.TrangThaiDatCoc;
import com.rental.order_service.enums.TrangPhucStatus; // <-- ĐÃ THÊM IMPORT ENUM NÀY
import com.rental.order_service.repository.ChiTietGioHangRepository;
import com.rental.order_service.repository.GioHangRepository;
import com.rental.order_service.repository.PhieuThueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(OrderBusinessService.class);

    private final PhieuThueRepository repo;
    private final GioHangRepository gioHangRepo;
    private final ChiTietGioHangRepository chiTietGioHangRepo;
    private final RestTemplate restTemplate;

    @Value("${service.customer-url}")
    private String customerUrl;

    @Value("${service.costume-url}")
    private String costumeUrl;

    public OrderBusinessService(PhieuThueRepository repo, GioHangRepository gioHangRepo, 
                                ChiTietGioHangRepository chiTietGioHangRepo, RestTemplate restTemplate) {
        this.repo = repo;
        this.gioHangRepo = gioHangRepo;
        this.chiTietGioHangRepo = chiTietGioHangRepo;
        this.restTemplate = restTemplate;
    }

    private GioHang getOrCreateCart(Long khachHangId) {
        return gioHangRepo.findByKhachHangId(khachHangId).orElseGet(() -> {
            GioHang newCart = new GioHang(null, "GH-" + khachHangId, khachHangId, LocalDate.now(), LocalDate.now());
            return gioHangRepo.save(newCart);
        });
    }

    @Transactional
    public void addToCart(Long khachHangId, Long trangPhucId) {
        GioHang cart = getOrCreateCart(khachHangId);

        if (chiTietGioHangRepo.existsByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId)) {
            throw new RuntimeException("Trang phục này đã có trong giỏ hàng!");
        }

        TrangPhucDto tp = restTemplate.getForObject(costumeUrl + "/" + trangPhucId, TrangPhucDto.class);
        
        if (tp == null || tp.getTrangThai() != TrangPhucStatus.AVAILABLE) {
            throw new RuntimeException("Trang phục không tồn tại hoặc đã được người khác thuê!");
        }

        chiTietGioHangRepo.save(new ChiTietGioHang(null, cart.getId(), trangPhucId, 1));
        cart.setNgayCapNhat(LocalDate.now());
        gioHangRepo.save(cart);
        logger.info("Đã thêm trang phục {} vào giỏ hàng của khách {}", trangPhucId, khachHangId);
    }

    public List<TrangPhucDto> getCartItems(Long khachHangId) {
        GioHang cart = getOrCreateCart(khachHangId);
        List<ChiTietGioHang> items = chiTietGioHangRepo.findByGioHangId(cart.getId());
        List<TrangPhucDto> result = new ArrayList<>();
        
        for (ChiTietGioHang item : items) {
            try {
                TrangPhucDto tp = restTemplate.getForObject(costumeUrl + "/" + item.getTrangPhucId(), TrangPhucDto.class);
                if (tp != null) result.add(tp);
            } catch (Exception ignored) {}
        }
        return result;
    }

    @Transactional
    public void removeFromCart(Long khachHangId, Long trangPhucId) {
        GioHang cart = getOrCreateCart(khachHangId);
        ChiTietGioHang item = chiTietGioHangRepo.findByGioHangIdAndTrangPhucId(cart.getId(), trangPhucId)
                .orElseThrow(() -> new RuntimeException("Trang phục không có trong giỏ hàng"));
        
        chiTietGioHangRepo.delete(item);
        cart.setNgayCapNhat(LocalDate.now());
        gioHangRepo.save(cart);
    }

    // ─────────────────────────── Tạo phiếu thuê ──────────────────────────────

    @Transactional
    public PhieuThueDto createOrder(Long khachHangId, CreateOrderRequest req) {
        logger.info("Tạo phiếu thuê từ giỏ hàng cho khách hàng id={}", khachHangId);

        if (!req.getNgayHenTra().isAfter(req.getNgayHenLay())) {
            throw new RuntimeException("Ngày hẹn trả phải sau ngày hẹn lấy");
        }

        GioHang cart = getOrCreateCart(khachHangId);
        List<ChiTietGioHang> cartItems = chiTietGioHangRepo.findByGioHangId(cart.getId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống! Vui lòng chọn trang phục trước khi thuê.");
        }

        PhieuThue pt = new PhieuThue(
                null, "PT-" + khachHangId + "-" + System.currentTimeMillis(),
                khachHangId, LocalDate.now(), req.getNgayHenLay(), req.getNgayHenTra(),
                HinhThucThue.ONLINE, PhieuThueStatus.CHO_XU_LY, 0.0, TrangThaiDatCoc.CHUA_THANH_TOAN
        );

        // Khởi tạo list rỗng để đảm bảo không bị NullPointerException khi add chi tiết
        if (pt.getChiTiet() == null) {
            pt.setChiTiet(new ArrayList<>());
        }

        int rentedCount = 0;

        for (ChiTietGioHang item : cartItems) {
            TrangPhucDto tp = null;
            try {
                tp = restTemplate.getForObject(costumeUrl + "/" + item.getTrangPhucId(), TrangPhucDto.class);
            } catch (Exception e) {
                logger.warn("Không thể kiểm tra đồ id={}", item.getTrangPhucId());
            }

            if (tp != null && tp.getTrangThai() == TrangPhucStatus.AVAILABLE) {
                ChiTietPhieuThue ct = new ChiTietPhieuThue(null, null, tp.getId(), 1, tp.getGiaThue());
                pt.getChiTiet().add(ct);

                // Khóa đồ
                restTemplate.patchForObject(costumeUrl + "/" + tp.getId() + "/trang-thai?trangThai=RENTED", null, Void.class);
                rentedCount++;
            }
        }

        if (rentedCount == 0) {
            throw new RuntimeException("Rất tiếc, các trang phục trong giỏ hàng của bạn đều vừa bị người khác thuê mất!");
        }

        // Xóa sạch giỏ hàng sau khi chốt đơn thành công
        chiTietGioHangRepo.deleteAll(cartItems);
        cart.setNgayCapNhat(LocalDate.now());
        gioHangRepo.save(cart);

        double tienCoc = pt.getChiTiet().stream()
                .mapToDouble(ct -> ct.getDonGia() != null ? ct.getDonGia() : 0)
                .sum() * 0.3;
        pt.setTienDatCoc(tienCoc);

        PhieuThue saved = repo.save(pt);
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