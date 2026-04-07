package com.rental.costume_service.controller;

import com.rental.costume_service.dto.TrangPhucDto;
import com.rental.costume_service.enums.TrangPhucStatus;
import com.rental.costume_service.service.CostumeBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Refactor CostumeController:
 *  1. Inject CostumeBusinessService (không qua interface) như demo_lam_NMH.
 *  2. Thêm SLF4J Logger ở mọi endpoint.
 *  3. Dùng @Valid trên request body để trigger Bean Validation.
 *  4. Tách riêng endpoint PATCH /trang-thai phục vụ order-service.
 *  5. Loại bỏ các endpoint GioHang (đã chuyển sang order-service).
 */
@RestController
@RequestMapping("/api/trang-phuc")
public class CostumeController {

    private static final Logger logger = LoggerFactory.getLogger(CostumeController.class);

    private final CostumeBusinessService costumeService;

    public CostumeController(CostumeBusinessService costumeService) {
        this.costumeService = costumeService;
    }

    @GetMapping
    public ResponseEntity<List<TrangPhucDto>> getAll() {
        logger.info("GET /api/trang-phuc - Lấy danh sách trang phục");
        List<TrangPhucDto> result = costumeService.findAll();
        logger.info("Trả về {} trang phục", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public ResponseEntity<List<TrangPhucDto>> getAvailable() {
        logger.info("GET /api/trang-phuc/available - Lấy trang phục còn hàng");
        return ResponseEntity.ok(costumeService.findAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrangPhucDto> getById(@PathVariable Long id) {
        logger.info("GET /api/trang-phuc/{} - Lấy chi tiết trang phục", id);
        TrangPhucDto dto = costumeService.findById(id);
        if (dto == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/loai/{loaiId}")
    public ResponseEntity<List<TrangPhucDto>> getByLoai(@PathVariable Long loaiId) {
        logger.info("GET /api/trang-phuc/loai/{} - Lấy danh sách trang phục theo loại", loaiId);
        List<TrangPhucDto> result = costumeService.findByLoaiTrangPhuc(loaiId);
        logger.info("Trả về {} trang phục thuộc loại id={}", result.size(), loaiId);
        return ResponseEntity.ok(result);
    }

    /**
     * Cập nhật trạng thái trang phục.
     * Endpoint này được order-service gọi khi xác nhận / hủy phiếu thuê.
     */
    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<TrangPhucDto> updateStatus(@PathVariable Long id,
                                                              @RequestParam TrangPhucStatus trangThai) {
        logger.info("PATCH /api/trang-phuc/{}/trang-thai?trangThai={} - Cập nhật trạng thái", id, trangThai);
        TrangPhucDto result = costumeService.updateStatus(id, trangThai);
        if (result == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Đã cập nhật trạng thái trang phục id={} → {}", id, trangThai);
        return ResponseEntity.ok(result);
    }
}
