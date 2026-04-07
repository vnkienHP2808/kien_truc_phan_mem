package com.rental.customer_service.service;

import com.rental.customer_service.dto.KhachHangResponseDto;
import com.rental.customer_service.dto.KhachHangRequestDto;
import com.rental.customer_service.entity.KhachHang;
import com.rental.customer_service.repository.KhachHangRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Refactor KhachHangServiceImpl + IKhachHangService → KhachHangBusinessService.
 * Thêm Logger, xóa interface riêng, dùng JpaRepository thực sự.
 */
@Service
public class KhachHangBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(KhachHangBusinessService.class);

    private final KhachHangRepository repo;

    public KhachHangBusinessService(KhachHangRepository repo) {
        this.repo = repo;
    }

    public List<KhachHangResponseDto> getAll() {
        logger.info("Lấy danh sách tất cả khách hàng");
        List<KhachHangResponseDto> result = repo.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
        logger.info("Trả về {} khách hàng", result.size());
        return result;
    }

    public KhachHangResponseDto getById(Long id) {
        logger.info("Tìm khách hàng id={}", id);
        KhachHang kh = repo.findById(id).orElse(null);
        if (kh == null) {
            logger.warn("Không tìm thấy khách hàng id={}", id);
            return null;
        }
        return toDto(kh);
    }

    public KhachHangResponseDto createCustomer(KhachHangRequestDto dto) {
        logger.info("Tạo mới khách hàng username={}", dto.getUsername());
        

        if (repo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại trong hệ thống");
        }

        String maKhachHang = "KH" + System.currentTimeMillis();

        KhachHang kh = new KhachHang(
                null, maKhachHang, dto.getHoTen(), dto.getUsername(),
                dto.getPassword(), dto.getEmail(), dto.getSoDienThoai(),
                dto.getDiaChi(), dto.getDob()
        );

        KhachHang saved = repo.save(kh);
        logger.info("Đã tạo khách hàng id={}, mã={}", saved.getId(), saved.getMaKhachHang());
        return toDto(saved);
    }

    public KhachHangResponseDto updateCustomer(Long id, KhachHangRequestDto dto) {
        logger.info("Cập nhật thông tin khách hàng id={}", id);
        
        KhachHang kh = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng id=" + id));

        // Cập nhật các trường thông tin (không cho phép đổi username và maKhachHang)
        kh.setHoTen(dto.getHoTen());
        kh.setPassword(dto.getPassword());
        kh.setEmail(dto.getEmail());
        kh.setSoDienThoai(dto.getSoDienThoai());
        kh.setDiaChi(dto.getDiaChi());
        kh.setDob(dto.getDob());

        KhachHang updated = repo.save(kh);
        logger.info("Đã cập nhật khách hàng id={}", updated.getId());
        return toDto(updated);
    }

    public void deleteCustomer(Long id) {
        logger.info("Xóa khách hàng id={}", id);
        if (!repo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khách hàng id=" + id);
        }
        repo.deleteById(id);
        logger.info("Đã xóa khách hàng id={}", id);
    }

    private KhachHangResponseDto toDto(KhachHang kh) {
        return new KhachHangResponseDto(
                kh.getId(), kh.getMaKhachHang(), kh.getHoTen(),
                kh.getUsername(), kh.getEmail(), kh.getSoDienThoai(),
                kh.getDiaChi(), kh.getDob()
        );
    }

}
