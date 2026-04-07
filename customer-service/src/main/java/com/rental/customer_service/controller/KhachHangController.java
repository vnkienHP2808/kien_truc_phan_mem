package com.rental.customer_service.controller;

import com.rental.customer_service.dto.KhachHangResponseDto;
import com.rental.customer_service.dto.KhachHangRequestDto;
import com.rental.customer_service.service.KhachHangBusinessService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
public class KhachHangController {

    private static final Logger logger = LoggerFactory.getLogger(KhachHangController.class);

    private final KhachHangBusinessService service;

    public KhachHangController(KhachHangBusinessService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<KhachHangResponseDto>> getAll() {
        logger.info("GET /api/khach-hang - Lấy danh sách khách hàng");
        List<KhachHangResponseDto> result = service.getAll();
        logger.info("Trả về {} khách hàng", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhachHangResponseDto> getById(@PathVariable Long id) {
        logger.info("GET /api/khach-hang/{} - Lấy chi tiết khách hàng", id);
        KhachHangResponseDto dto = service.getById(id);
        if (dto == null) {
            logger.warn("Không tìm thấy khách hàng id={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<KhachHangResponseDto> create(@Valid @RequestBody KhachHangRequestDto req) {
        logger.info("POST /api/khach-hang - Yêu cầu tạo mới khách hàng");
        KhachHangResponseDto result = service.createCustomer(req);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KhachHangResponseDto> update(@PathVariable Long id, @Valid @RequestBody KhachHangRequestDto req) {
        logger.info("PUT /api/khach-hang/{} - Yêu cầu cập nhật khách hàng", id);
        KhachHangResponseDto result = service.updateCustomer(id, req);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE /api/khach-hang/{} - Yêu cầu xóa khách hàng", id);
        service.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
