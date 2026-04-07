package com.rental.costume_service.repository;

import com.rental.costume_service.entity.TrangPhuc;
import com.rental.costume_service.enums.TrangPhucStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Refactor: thay FakeTrangPhucRepository (in-memory ArrayList) bằng JpaRepository thực sự.
 * Không cần implement thủ công findAll, save, findById... - Spring Data JPA tự generate.
 */
@Repository
public interface TrangPhucRepository extends JpaRepository<TrangPhuc, Long> {

    Optional<TrangPhuc> findByMaTrangPhuc(String maTrangPhuc);

    List<TrangPhuc> findByTrangThai(TrangPhucStatus trangThai);

    List<TrangPhuc> findByLoaiTrangPhucId(Long loaiTrangPhucId);
}
