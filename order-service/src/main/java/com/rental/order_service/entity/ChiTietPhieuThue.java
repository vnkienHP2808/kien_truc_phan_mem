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

    @Column(name = "trang_phuc_id")
    private Long trangPhucId;

    @Column(name = "ten_trang_phuc")
    private String tenTrangPhuc;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "don_gia")
    private Double donGia;

    public ChiTietPhieuThue() {}

    public ChiTietPhieuThue(Long id, Long phieuThueId, Long trangPhucId,
                             String tenTrangPhuc, int soLuong, Double donGia) {
        this.id = id;
        this.phieuThueId = phieuThueId;
        this.trangPhucId = trangPhucId;
        this.tenTrangPhuc = tenTrangPhuc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPhieuThueId() { return phieuThueId; }
    public void setPhieuThueId(Long phieuThueId) { this.phieuThueId = phieuThueId; }
    public Long getTrangPhucId() { return trangPhucId; }
    public void setTrangPhucId(Long trangPhucId) { this.trangPhucId = trangPhucId; }
    public String getTenTrangPhuc() { return tenTrangPhuc; }
    public void setTenTrangPhuc(String tenTrangPhuc) { this.tenTrangPhuc = tenTrangPhuc; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }
}
