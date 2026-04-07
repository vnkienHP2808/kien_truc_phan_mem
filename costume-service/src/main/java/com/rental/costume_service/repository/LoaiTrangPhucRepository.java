package com.rental.costume_service.repository;

import com.rental.costume_service.entity.LoaiTrangPhuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiTrangPhucRepository extends JpaRepository<LoaiTrangPhuc, Long> {

    boolean existsByMaLoaiTrangPhuc(String maLoaiTrangPhuc);
}
