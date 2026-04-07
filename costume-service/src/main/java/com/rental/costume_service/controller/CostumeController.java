package com.rental.costume_service.controller;

import com.rental.costume_service.dto.TrangPhucRequestDto;
import com.rental.costume_service.dto.TrangPhucResponseDto;
import com.rental.costume_service.enums.TrangPhucStatus;
import com.rental.costume_service.service.CostumeBusinessService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<TrangPhucResponseDto>> getAll() {
        logger.info("GET /api/trang-phuc - Lấy danh sách trang phục");
        List<TrangPhucResponseDto> result = costumeService.findAll();
        logger.info("Trả về {} trang phục", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public ResponseEntity<List<TrangPhucResponseDto>> getAvailable() {
        logger.info("GET /api/trang-phuc/available - Lấy trang phục còn hàng");
        return ResponseEntity.ok(costumeService.findAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrangPhucResponseDto> getById(@PathVariable Long id) {
        logger.info("GET /api/trang-phuc/{} - Lấy chi tiết trang phục", id);
        TrangPhucResponseDto dto = costumeService.findById(id);
        if (dto == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TrangPhucResponseDto> create(@Valid @RequestBody TrangPhucRequestDto dto) {
        logger.info("POST /api/trang-phuc - Tạo trang phục: {}", dto.getMaTrangPhuc());
        TrangPhucResponseDto result = costumeService.save(dto);
        logger.info("Đã tạo trang phục id={}", result.getId());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrangPhucResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody TrangPhucRequestDto dto) {
        logger.info("PUT /api/trang-phuc/{} - Cập nhật trang phục", id);
        TrangPhucResponseDto result = costumeService.update(id, dto);
        if (result == null) {
            logger.warn("Không tìm thấy trang phục để cập nhật id={}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Đã cập nhật trang phục id={}", id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE /api/trang-phuc/{} - Xóa trang phục", id);
        boolean deleted = costumeService.delete(id);
        if (!deleted) {
            logger.warn("Không tìm thấy trang phục để xóa id={}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Đã xóa trang phục id={}", id);
        return ResponseEntity.ok().build();
    }

    /**
     * Cập nhật trạng thái trang phục.
     * Endpoint này được order-service gọi khi xác nhận / hủy phiếu thuê.
     */
    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<TrangPhucResponseDto> updateStatus(@PathVariable Long id,
                                                              @RequestParam TrangPhucStatus trangThai) {
        logger.info("PATCH /api/trang-phuc/{}/trang-thai?trangThai={} - Cập nhật trạng thái", id, trangThai);
        TrangPhucResponseDto result = costumeService.updateStatus(id, trangThai);
        if (result == null) {
            logger.warn("Không tìm thấy trang phục id={}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Đã cập nhật trạng thái trang phục id={} → {}", id, trangThai);
        return ResponseEntity.ok(result);
    }
}
