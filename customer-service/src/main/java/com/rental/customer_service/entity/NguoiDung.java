package com.rental.customer_service.entity;

import jakarta.persistence.*;

/**
 * Refactor: thêm @MappedSuperclass để JPA kế thừa các column vào bảng khach_hang.
 */
@MappedSuperclass
public abstract class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "ho_ten")
    protected String hoTen;

    @Column(name = "username", unique = true)
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "email")
    protected String email;

    @Column(name = "so_dien_thoai")
    protected String soDienThoai;

    @Column(name = "dia_chi")
    protected String diaChi;

    @Column(name = "dob")
    protected String dob;

    public NguoiDung() {}

    public NguoiDung(Long id, String hoTen, String username, String password,
                     String email, String soDienThoai, String diaChi, String dob) {
        this.id = id;
        this.hoTen = hoTen;
        this.username = username;
        this.password = password;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.dob = dob;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
}
