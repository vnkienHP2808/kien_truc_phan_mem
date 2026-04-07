package com.rental.customer_service.dto;

public class KhachHangDto {

    private Long id;
    private String maKhachHang;
    private String hoTen;
    private String username;
    private String email;
    private String soDienThoai;
    private String diaChi;
    private String dob;

    public KhachHangDto() {}

    public KhachHangDto(Long id, String maKhachHang, String hoTen, String username,
                        String email, String soDienThoai, String diaChi, String dob) {
        this.id = id;
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.username = username;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.dob = dob;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
}
