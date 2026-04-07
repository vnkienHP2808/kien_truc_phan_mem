package com.rental.order_service.dto;

public class TrangPhucResponseDto {

    private Long id;
    private String maTrangPhuc;
    private String tenTrangPhuc;
    private Long loaiTrangPhucId;
    private String tenLoaiTrangPhuc;
    private String kichThuoc;
    private String mauSac;
    private Double giaThue;
    private Double giaGoc;
    private String trangThai;

    public TrangPhucResponseDto() {}

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
    public Double getGiaGoc() { return giaGoc; }
    public void setGiaGoc(Double giaGoc) { this.giaGoc = giaGoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
