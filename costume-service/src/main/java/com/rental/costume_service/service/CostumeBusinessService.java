package com.rental.costume_service.service;

import com.rental.costume_service.dto.TrangPhucDto;
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
    }

    // ───────────────────────────────── CRUD ──────────────────────────────────

    public List<TrangPhucDto> findAll() {
        logger.info("Lấy danh sách tất cả trang phục");
        List<TrangPhucDto> result = trangPhucRepo.findAll()
                .stream().map(this::toResponseDto).collect(Collectors.toList());
        logger.info("Trả về {} trang phục", result.size());
        return result;
    }

    public TrangPhucDto findById(Long id) {
        logger.info("Tìm trang phục id={}", id);
        TrangPhuc tp = trangPhucRepo.findById(id).orElse(null);
        if (tp == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return null;
        }
        return toResponseDto(tp);
    }

    public List<TrangPhucDto> findAvailable() {
        logger.info("Lấy danh sách trang phục còn hàng");
        return trangPhucRepo.findByTrangThai(TrangPhucStatus.AVAILABLE)
                .stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái trang phục (AVAILABLE / RENTED / UNAVAILABLE).
     * Endpoint này được order-service gọi khi tạo / hủy phiếu thuê.
     */
    public TrangPhucDto updateStatus(Long id, TrangPhucStatus trangThai) {
        TrangPhuc tp = trangPhucRepo.findById(id).orElse(null);
        if (tp == null) {
            logger.error("Không tìm thấy trang phục id={}", id);
            return null;
        }
        logger.info("Cập nhật trạng thái trang phục id={}: {} → {}", id, tp.getTrangThai(), trangThai);
        tp.setTrangThai(trangThai);
        return toResponseDto(trangPhucRepo.save(tp));
    }

    public List<TrangPhucDto> findByLoaiTrangPhuc(Long loaiId) {
        logger.info("Lấy danh sách trang phục theo loại id={}", loaiId);
        
        return trangPhucRepo.findByLoaiTrangPhucId(loaiId)
                .stream()
                // Lọc thêm trang phục đang sẵn sàng
                .filter(tp -> tp.getTrangThai() == TrangPhucStatus.AVAILABLE)
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────── Mapping helper ───────────────────────────────

    private TrangPhucDto toResponseDto(TrangPhuc tp) {
        String tenLoai = loaiRepo.findById(tp.getLoaiTrangPhucId())
                .map(LoaiTrangPhuc::getTenLoaiTrangPhuc)
                .orElse("");
        return new TrangPhucDto(
                tp.getId(), tp.getMaTrangPhuc(), tp.getTenTrangPhuc(),
                tp.getLoaiTrangPhucId(), tenLoai,
                tp.getKichThuoc(), tp.getMauSac(),
                tp.getGiaThue(), tp.getTrangThai()
        );
    }
}
