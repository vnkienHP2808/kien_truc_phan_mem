package com.rental.order_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_gio_hang")
public class ChiTietGioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gio_hang_id", nullable = false)
    private Long gioHangId;

    @Column(name = "trang_phuc_id", nullable = false)
    private Long trangPhucId;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    public ChiTietGioHang() {}

    public ChiTietGioHang(Long id, Long gioHangId, Long trangPhucId, Integer soLuong) {
        this.id = id;
        this.gioHangId = gioHangId;
        this.trangPhucId = trangPhucId;
        this.soLuong = soLuong;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGioHangId() { return gioHangId; }
    public void setGioHangId(Long gioHangId) { this.gioHangId = gioHangId; }
    public Long getTrangPhucId() { return trangPhucId; }
    public void setTrangPhucId(Long trangPhucId) { this.trangPhucId = trangPhucId; }
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
}