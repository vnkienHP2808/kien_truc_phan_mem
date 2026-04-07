package com.rental.costume_service.entity;

import com.rental.costume_service.enums.TrangPhucStatus;
import jakarta.persistence.*;

/**
 * Entity JPA tương ứng bảng trang_phuc trong MySQL.
 * Refactor từ POJO thuần sang JPA Entity theo chuẩn demo_lam_NMH.
 */
@Entity
@Table(name = "trang_phuc")
public class TrangPhuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_trang_phuc", unique = true)
    private String maTrangPhuc;

    @Column(name = "ten_trang_phuc")
    private String tenTrangPhuc;

    @Column(name = "loai_trang_phuc_id")
    private Long loaiTrangPhucId;

    @Column(name = "kich_thuoc")
    private String kichThuoc;

    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "gia_thue")
    private Double giaThue;

    @Column(name = "gia_goc")
    private Double giaGoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangPhucStatus trangThai;

    public TrangPhuc() {}

    public TrangPhuc(Long id, String maTrangPhuc, String tenTrangPhuc, Long loaiTrangPhucId,
                     String kichThuoc, String mauSac, Double giaThue, Double giaGoc,
                     TrangPhucStatus trangThai) {
        this.id = id;
        this.maTrangPhuc = maTrangPhuc;
        this.tenTrangPhuc = tenTrangPhuc;
        this.loaiTrangPhucId = loaiTrangPhucId;
        this.kichThuoc = kichThuoc;
        this.mauSac = mauSac;
        this.giaThue = giaThue;
        this.giaGoc = giaGoc;
        this.trangThai = trangThai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaTrangPhuc() { return maTrangPhuc; }
    public void setMaTrangPhuc(String maTrangPhuc) { this.maTrangPhuc = maTrangPhuc; }

    public String getTenTrangPhuc() { return tenTrangPhuc; }
    public void setTenTrangPhuc(String tenTrangPhuc) { this.tenTrangPhuc = tenTrangPhuc; }

    public Long getLoaiTrangPhucId() { return loaiTrangPhucId; }
    public void setLoaiTrangPhucId(Long loaiTrangPhucId) { this.loaiTrangPhucId = loaiTrangPhucId; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public Double getGiaThue() { return giaThue; }
    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }
    
    public Double getGiaGoc() { return giaGoc; }
    public void setGiaGoc(Double giaGoc) { this.giaGoc = giaGoc; }
    
    public TrangPhucStatus getTrangThai() { return trangThai; }
    public void setTrangThai(TrangPhucStatus trangThai) { this.trangThai = trangThai; }
}
