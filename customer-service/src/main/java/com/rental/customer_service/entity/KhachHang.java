package com.rental.customer_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "khach_hang")
public class KhachHang extends NguoiDung {

    @Column(name = "ma_khach_hang", unique = true)
    private String maKhachHang;

    public KhachHang() {}

    public KhachHang(Long id, String maKhachHang, String hoTen, String username,
                     String password, String email, String soDienThoai,
                     String diaChi, String dob) {
        super(id, hoTen, username, password, email, soDienThoai, diaChi, dob);
        this.maKhachHang = maKhachHang;
    }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }
}
