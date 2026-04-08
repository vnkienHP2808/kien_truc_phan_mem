package com.rental.order_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gio_hang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_gio_hang", unique = true, nullable = false)
    private String maGioHang;

    @Column(name = "khach_hang_id", unique = true, nullable = false)
    private Long khachHangId;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDate ngayCapNhat;

    public GioHang() {}

    public GioHang(Long id, String maGioHang, Long khachHangId, LocalDate ngayTao, LocalDate ngayCapNhat) {
        this.id = id;
        this.maGioHang = maGioHang;
        this.khachHangId = khachHangId;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaGioHang() { return maGioHang; }
    public void setMaGioHang(String maGioHang) { this.maGioHang = maGioHang; }

    public Long getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Long khachHangId) { this.khachHangId = khachHangId; }

    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }
    
    public LocalDate getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(LocalDate ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}