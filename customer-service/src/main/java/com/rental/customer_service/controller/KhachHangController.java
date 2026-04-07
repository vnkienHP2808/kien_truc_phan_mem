package com.rental.customer_service.controller;

import com.rental.customer_service.dto.KhachHangDto;
import com.rental.customer_service.service.KhachHangBusinessService;
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
    public ResponseEntity<List<KhachHangDto>> getAll() {
        logger.info("GET /api/khach-hang - Lấy danh sách khách hàng");
        List<KhachHangDto> result = service.getAll();
        logger.info("Trả về {} khách hàng", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhachHangDto> getById(@PathVariable Long id) {
        logger.info("GET /api/khach-hang/{} - Lấy chi tiết khách hàng", id);
        KhachHangDto dto = service.getById(id);
        if (dto == null) {
            logger.warn("Không tìm thấy khách hàng id={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<KhachHangDto> getByUsername(@PathVariable String username) {
        logger.info("GET /api/khach-hang/username/{}", username);
        KhachHangDto dto = service.getByUsername(username);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/ma/{maKhachHang}")
    public ResponseEntity<KhachHangDto> getByMa(@PathVariable String maKhachHang) {
        logger.info("GET /api/khach-hang/ma/{}", maKhachHang);
        KhachHangDto dto = service.getByMaKhachHang(maKhachHang);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
}
