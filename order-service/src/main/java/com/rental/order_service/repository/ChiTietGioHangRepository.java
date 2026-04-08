package com.rental.order_service.repository;

import com.rental.order_service.entity.ChiTietGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Long> {
    List<ChiTietGioHang> findByGioHangId(Long gioHangId);
    Optional<ChiTietGioHang> findByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
    boolean existsByGioHangIdAndTrangPhucId(Long gioHangId, Long trangPhucId);
}