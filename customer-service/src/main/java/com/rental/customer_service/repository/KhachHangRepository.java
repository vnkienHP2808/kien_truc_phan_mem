package com.rental.customer_service.repository;

import com.rental.customer_service.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    Optional<KhachHang> findByUsername(String username);

}
