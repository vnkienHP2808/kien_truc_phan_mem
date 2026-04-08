package com.rental.order_service.repository;

import com.rental.order_service.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    Optional<GioHang> findByKhachHangId(Long khachHangId);
}