package com.rental.costume_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loai_trang_phuc")
public class LoaiTrangPhuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_loai_trang_phuc", unique = true)
    private String maLoaiTrangPhuc;

    @Column(name = "ten_loai_trang_phuc")
    private String tenLoaiTrangPhuc;

    @Column(name = "mo_ta")
    private String moTa;

    public LoaiTrangPhuc() {}

    public LoaiTrangPhuc(Long id, String maLoaiTrangPhuc, String tenLoaiTrangPhuc, String moTa) {
        this.id = id;
        this.maLoaiTrangPhuc = maLoaiTrangPhuc;
        this.tenLoaiTrangPhuc = tenLoaiTrangPhuc;
        this.moTa = moTa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaLoaiTrangPhuc() { return maLoaiTrangPhuc; }
    public void setMaLoaiTrangPhuc(String maLoaiTrangPhuc) { this.maLoaiTrangPhuc = maLoaiTrangPhuc; }
    public String getTenLoaiTrangPhuc() { return tenLoaiTrangPhuc; }
    public void setTenLoaiTrangPhuc(String tenLoaiTrangPhuc) { this.tenLoaiTrangPhuc = tenLoaiTrangPhuc; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
