package com.rental.order_service.controller;

import com.rental.order_service.dto.CreateOrderRequest;
import com.rental.order_service.dto.PhieuThueDto;
import com.rental.order_service.service.OrderBusinessService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-thue")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderBusinessService service;

    public OrderController(OrderBusinessService service) {
        this.service = service;
    }

    @PostMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<PhieuThueDto> createOrder(@PathVariable Long khachHangId,
                                                     @Valid @RequestBody CreateOrderRequest req) {
        logger.info("POST /api/phieu-thue/khach-hang/{} - Tạo phiếu thuê", khachHangId);
        PhieuThueDto result = service.createOrder(khachHangId, req);
        logger.info("Đã tạo phiếu thuê mã={}", result.getMaPhieu());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<List<PhieuThueDto>> getByKhachHang(@PathVariable Long khachHangId) {
        logger.info("GET /api/phieu-thue/khach-hang/{}", khachHangId);
        return ResponseEntity.ok(service.getByKhachHang(khachHangId));
    }

    @GetMapping("/{maPhieu}")
    public ResponseEntity<PhieuThueDto> getByMaPhieu(@PathVariable String maPhieu) {
        logger.info("GET /api/phieu-thue/{}", maPhieu);
        return ResponseEntity.ok(service.getByMaPhieu(maPhieu));
    }

    @PatchMapping("/{maPhieu}/huy")
    public ResponseEntity<PhieuThueDto> cancelOrder(@PathVariable String maPhieu) {
        logger.info("PATCH /api/phieu-thue/{}/huy - Hủy phiếu thuê", maPhieu);
        PhieuThueDto result = service.cancelOrder(maPhieu);
        logger.info("Đã hủy phiếu thuê mã={}", maPhieu);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{maPhieu}/dat-coc")
    public ResponseEntity<PhieuThueDto> confirmDatCoc(@PathVariable String maPhieu) {
        logger.info("PATCH /api/phieu-thue/{}/dat-coc - Xác nhận đặt cọc", maPhieu);
        return ResponseEntity.ok(service.confirmDatCoc(maPhieu));
    }

    @PatchMapping("/{maPhieu}/xac-nhan")
    public ResponseEntity<PhieuThueDto> confirmOrder(@PathVariable String maPhieu) {
        logger.info("PATCH /api/phieu-thue/{}/xac-nhan - Xác nhận phiếu thuê", maPhieu);
        return ResponseEntity.ok(service.confirmOrder(maPhieu));
    }
}
