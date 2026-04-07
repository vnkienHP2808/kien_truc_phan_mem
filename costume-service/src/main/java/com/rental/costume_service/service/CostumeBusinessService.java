package com.rental.costume_service.service;

import com.rental.costume_service.dto.TrangPhucRequestDto;
import com.rental.costume_service.dto.TrangPhucResponseDto;
import com.rental.costume_service.entity.LoaiTrangPhuc;
import com.rental.costume_service.entity.TrangPhuc;
import com.rental.costume_service.enums.TrangPhucStatus;
import com.rental.costume_service.repository.LoaiTrangPhucRepository;
import com.rental.costume_service.repository.TrangPhucRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Refactor CostumeServiceImpl + ICostumeService → CostumeBusinessService (theo chuẩn demo_lam_NMH).
 *
 * Thay đổi chính:
 *  1. Xóa bỏ ICostumeService interface (không cần thiết khi chỉ có 1 impl).
 *  2. Thêm SLF4J Logger ở mọi method quan trọng.
 *  3. Thay Fake Repositories bằng JpaRepository thực sự.
 *  4. Loại bỏ logic GioHang khỏi costume-service (GioHang là trách nhiệm của order-service).
 *  5. Dùng TrangPhucRequestDto / TrangPhucResponseDto có validation.
 */
@Service
public class CostumeBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(CostumeBusinessService.class);

    private final TrangPhucRepository trangPhucRepo;
    private final LoaiTrangPhucRepository loaiRepo;

    public CostumeBusinessService(TrangPhucRepository trangPhucRepo,
                                   LoaiTrangPhucRepository loaiRepo) {
        this.trangPhucRepo = trangPhucRepo;
        this.loaiRepo = loaiRepo;

        // Seed dữ liệu mẫu nếu DB trống (giữ logic từ DataSeeder)
        if (trangPhucRepo.count() == 0) {
            seedData();
        }
    }

    // ───────────────────────────────── CRUD ──────────────────────────────────

    public List<TrangPhucResponseDto> findAll() {
        logger.info("Lấy danh sách tất cả trang phục");
        List<TrangPhucResponseDto> result = trangPhucRepo.findAll()
                .stream().map(this::toResponseDto).collect(Collectors.toList());
        logger.info("Trả về {} trang phục", result.size());
        return result;
    }

    public TrangPhucResponseDto findById(Long id) {
        logger.info("Tìm trang phục id={}", id);
        TrangPhuc tp = trangPhucRepo.findById(id).orElse(null);
        if (tp == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return null;
        }
        return toResponseDto(tp);
    }

    public List<TrangPhucResponseDto> findAvailable() {
        logger.info("Lấy danh sách trang phục còn hàng");
        return trangPhucRepo.findByTrangThai(TrangPhucStatus.AVAILABLE)
                .stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái trang phục (AVAILABLE / RENTED / UNAVAILABLE).
     * Endpoint này được order-service gọi khi tạo / hủy phiếu thuê.
     */
    public TrangPhucResponseDto updateStatus(Long id, TrangPhucStatus trangThai) {
        TrangPhuc tp = trangPhucRepo.findById(id).orElse(null);
        if (tp == null) {
            logger.error("Không tìm thấy trang phục id={}", id);
            return null;
        }
        logger.info("Cập nhật trạng thái trang phục id={}: {} → {}", id, tp.getTrangThai(), trangThai);
        tp.setTrangThai(trangThai);
        return toResponseDto(trangPhucRepo.save(tp));
    }

    // ─────────────────────────── Mapping helper ───────────────────────────────

    private TrangPhucResponseDto toResponseDto(TrangPhuc tp) {
        String tenLoai = loaiRepo.findById(tp.getLoaiTrangPhucId())
                .map(LoaiTrangPhuc::getTenLoaiTrangPhuc)
                .orElse("");
        return new TrangPhucResponseDto(
                tp.getId(), tp.getMaTrangPhuc(), tp.getTenTrangPhuc(),
                tp.getLoaiTrangPhucId(), tenLoai,
                tp.getKichThuoc(), tp.getMauSac(),
                tp.getGiaThue(), tp.getGiaGoc(), tp.getTrangThai()
        );
    }

    // ──────────────────────────── Seed dữ liệu ───────────────────────────────

    private void seedData() {
        LoaiTrangPhuc ao  = loaiRepo.save(new LoaiTrangPhuc(null, "LTP001", "Áo dài",  "Trang phục truyền thống Việt Nam"));
        LoaiTrangPhuc vest= loaiRepo.save(new LoaiTrangPhuc(null, "LTP002", "Vest",    "Trang phục công sở lịch sự"));
        LoaiTrangPhuc cos = loaiRepo.save(new LoaiTrangPhuc(null, "LTP003", "Cosplay", "Trang phục hóa trang nhân vật"));

        trangPhucRepo.save(new TrangPhuc(null, "TP001", "Áo dài đỏ hoa văn",        ao.getId(),   "M", "Đỏ",        150000.0, 800000.0,  TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP002", "Áo dài xanh ngọc",         ao.getId(),   "S", "Xanh",      150000.0, 750000.0,  TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP003", "Áo dài vàng hoàng kim",    ao.getId(),   "L", "Vàng",      180000.0, 900000.0,  TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP004", "Vest đen 3 mảnh",          vest.getId(), "L", "Đen",       200000.0, 1200000.0, TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP005", "Vest xám sang trọng",      vest.getId(), "M", "Xám",       200000.0, 1100000.0, TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP006", "Vest navy slim fit",        vest.getId(), "S", "Navy",      180000.0, 1000000.0, TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP007", "Cosplay Naruto",            cos.getId(),  "M", "Cam",       120000.0, 500000.0,  TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP008", "Cosplay Hanfu cổ trang",   cos.getId(),  "L", "Trắng",     200000.0, 900000.0,  TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP009", "Cosplay quân đội thế kỷ 19",cos.getId(), "M", "Xanh dương",250000.0, 1100000.0, TrangPhucStatus.AVAILABLE));
        trangPhucRepo.save(new TrangPhuc(null, "TP010", "Áo dài tím pastel",         ao.getId(),  "M", "Tím",       160000.0, 820000.0,  TrangPhucStatus.UNAVAILABLE));

        logger.info("Đã seed 10 trang phục mẫu vào database");
    }
}
