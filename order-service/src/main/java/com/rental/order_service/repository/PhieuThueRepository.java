package com.rental.order_service.repository;

import com.rental.order_service.entity.PhieuThue;
import com.rental.order_service.enums.PhieuThueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Refactor: thay FakePhieuThueRepository (ArrayList in-memory) bằng JpaRepository.
 */
@Repository
public interface PhieuThueRepository extends JpaRepository<PhieuThue, Long> {

    Optional<PhieuThue> findByMaPhieu(String maPhieu);

    List<PhieuThue> findByKhachHangId(Long khachHangId);

    List<PhieuThue> findByTrangThai(PhieuThueStatus trangThai);
}
