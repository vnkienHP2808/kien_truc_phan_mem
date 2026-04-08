package com.rental.order_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_phieu_thue")
public class ChiTietPhieuThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phieu_thue_id")
    private Long phieuThueId;

    @Column(name = "trang_phuc_id", nullable = false)
    private Long trangPhucId;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_gia", nullable = false)
    private Double donGia;

    public ChiTietPhieuThue() {}

    public ChiTietPhieuThue(Long id, Long phieuThueId, Long trangPhucId, Integer soLuong, Double donGia) {
        this.id = id;
        this.phieuThueId = phieuThueId;
        this.trangPhucId = trangPhucId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPhieuThueId() { return phieuThueId; }
    public void setPhieuThueId(Long phieuThueId) { this.phieuThueId = phieuThueId; }

    public Long getTrangPhucId() { return trangPhucId; }
    public void setTrangPhucId(Long trangPhucId) { this.trangPhucId = trangPhucId; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }
}