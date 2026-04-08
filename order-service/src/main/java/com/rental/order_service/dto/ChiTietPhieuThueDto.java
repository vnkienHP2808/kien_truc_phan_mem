package com.rental.order_service.dto;

public class ChiTietPhieuThueDto {

    private Long id;
    private Long trangPhucId;
    private Integer soLuong;
    private Double donGia;

    public ChiTietPhieuThueDto() {}

    public ChiTietPhieuThueDto(Long id, Long trangPhucId, Integer soLuong, Double donGia) {
        this.id = id;
        this.trangPhucId = trangPhucId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrangPhucId() { return trangPhucId; }
    public void setTrangPhucId(Long trangPhucId) { this.trangPhucId = trangPhucId; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }
}