package com.rental.costume_service.dto;

import com.rental.costume_service.enums.TrangPhucStatus;

public class TrangPhucDto {

    private Long id;
    private String maTrangPhuc;
    private String tenTrangPhuc;
    private Long loaiTrangPhucId;
    private String tenLoaiTrangPhuc;
    private String kichThuoc;
    private String mauSac;
    private Double giaThue;
    private TrangPhucStatus trangThai;

    public TrangPhucDto() {}

    public TrangPhucDto(Long id, String maTrangPhuc, String tenTrangPhuc,
                                Long loaiTrangPhucId, String tenLoaiTrangPhuc,
                                String kichThuoc, String mauSac,
                                Double giaThue, TrangPhucStatus trangThai) {
        this.id = id;
        this.maTrangPhuc = maTrangPhuc;
        this.tenTrangPhuc = tenTrangPhuc;
        this.loaiTrangPhucId = loaiTrangPhucId;
        this.tenLoaiTrangPhuc = tenLoaiTrangPhuc;
        this.kichThuoc = kichThuoc;
        this.mauSac = mauSac;
        this.giaThue = giaThue;
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

    public String getTenLoaiTrangPhuc() { return tenLoaiTrangPhuc; }
    public void setTenLoaiTrangPhuc(String tenLoaiTrangPhuc) { this.tenLoaiTrangPhuc = tenLoaiTrangPhuc; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public Double getGiaThue() { return giaThue; }
    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }
    
    public TrangPhucStatus getTrangThai() { return trangThai; }
    public void setTrangThai(TrangPhucStatus trangThai) { this.trangThai = trangThai; }
}
