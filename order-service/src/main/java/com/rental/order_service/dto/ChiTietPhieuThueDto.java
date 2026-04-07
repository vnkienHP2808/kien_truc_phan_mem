package com.rental.order_service.dto;

public class ChiTietPhieuThueDto {

    private Long id;
    private Long trangPhucId;
    private String tenTrangPhuc;
    private int soLuong;
    private Double donGia;

    public ChiTietPhieuThueDto() {}

    public ChiTietPhieuThueDto(Long id, Long trangPhucId, String tenTrangPhuc,
                                int soLuong, Double donGia) {
        this.id = id;
        this.trangPhucId = trangPhucId;
        this.tenTrangPhuc = tenTrangPhuc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTrangPhucId() { return trangPhucId; }
    public void setTrangPhucId(Long trangPhucId) { this.trangPhucId = trangPhucId; }
    public String getTenTrangPhuc() { return tenTrangPhuc; }
    public void setTenTrangPhuc(String tenTrangPhuc) { this.tenTrangPhuc = tenTrangPhuc; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }
}
