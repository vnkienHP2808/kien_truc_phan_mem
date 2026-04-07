package com.rental.customer_service.service;

import com.rental.customer_service.dto.KhachHangDto;
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

        // Seed dữ liệu mẫu nếu DB trống
        if (repo.count() == 0) {
            seedData();
        }
    }

    public List<KhachHangDto> getAll() {
        logger.info("Lấy danh sách tất cả khách hàng");
        List<KhachHangDto> result = repo.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
        logger.info("Trả về {} khách hàng", result.size());
        return result;
    }

    public KhachHangDto getById(Long id) {
        logger.info("Tìm khách hàng id={}", id);
        KhachHang kh = repo.findById(id).orElse(null);
        if (kh == null) {
            logger.warn("Không tìm thấy khách hàng id={}", id);
            return null;
        }
        return toDto(kh);
    }

    public KhachHangDto getByUsername(String username) {
        logger.info("Tìm khách hàng username={}", username);
        KhachHang kh = repo.findByUsername(username).orElse(null);
        if (kh == null) {
            logger.warn("Không tìm thấy username={}", username);
            return null;
        }
        return toDto(kh);
    }

    public KhachHangDto getByMaKhachHang(String maKhachHang) {
        logger.info("Tìm khách hàng mã={}", maKhachHang);
        KhachHang kh = repo.findByMaKhachHang(maKhachHang).orElse(null);
        if (kh == null) {
            logger.warn("Không tìm thấy mã={}", maKhachHang);
            return null;
        }
        return toDto(kh);
    }

    private KhachHangDto toDto(KhachHang kh) {
        return new KhachHangDto(
                kh.getId(), kh.getMaKhachHang(), kh.getHoTen(),
                kh.getUsername(), kh.getEmail(), kh.getSoDienThoai(),
                kh.getDiaChi(), kh.getDob()
        );
    }

    private void seedData() {
        repo.save(new KhachHang(null, "KH001", "Nguyễn Văn An",
                "an.nguyen",  "123456", "an@gmail.com",   "0901111111", "12 Lê Lợi, HN",           "1995-03-10"));
        repo.save(new KhachHang(null, "KH002", "Trần Thị Bích",
                "bich.tran",  "123456", "bich@gmail.com", "0902222222", "34 Trần Hưng Đạo, HN",    "1998-07-22"));
        repo.save(new KhachHang(null, "KH003", "Lê Minh Cường",
                "cuong.le",   "123456", "cuong@gmail.com","0903333333", "56 Nguyễn Trãi, HN",       "1993-11-05"));
        repo.save(new KhachHang(null, "KH004", "Phạm Thị Dung",
                "dung.pham",  "123456", "dung@gmail.com", "0904444444", "78 Hoàng Diệu, HN",        "2000-01-15"));
        repo.save(new KhachHang(null, "KH005", "Hoàng Văn Em",
                "em.hoang",   "123456", "em@gmail.com",   "0905555555", "90 Bạch Mai, HN",          "1997-09-30"));
        logger.info("Đã seed 5 khách hàng mẫu vào database");
    }
}
