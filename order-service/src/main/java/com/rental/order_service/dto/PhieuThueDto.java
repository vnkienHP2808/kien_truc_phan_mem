package com.rental.order_service.dto;

import com.rental.order_service.enums.HinhThucThue;
import com.rental.order_service.enums.PhieuThueStatus;
import com.rental.order_service.enums.TrangThaiDatCoc;

import java.time.LocalDate;
import java.util.List;

public class PhieuThueDto {

    private Long id;
    private String maPhieu;
    private Long khachHangId;
    private String tenKhachHang;
    private LocalDate ngayTao;
    private LocalDate ngayHenLay;
    private LocalDate ngayHenTra;
    private HinhThucThue hinhThuc;
    private PhieuThueStatus trangThai;
    private Double tienDatCoc;
    private TrangThaiDatCoc trangThaiDatCoc;
    private List<ChiTietPhieuThueDto> chiTiet;

    public PhieuThueDto() {}

    public PhieuThueDto(Long id, String maPhieu, Long khachHangId, String tenKhachHang,
                        LocalDate ngayTao, LocalDate ngayHenLay, LocalDate ngayHenTra,
                        HinhThucThue hinhThuc, PhieuThueStatus trangThai,
                        Double tienDatCoc, TrangThaiDatCoc trangThaiDatCoc,
                        List<ChiTietPhieuThueDto> chiTiet) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.khachHangId = khachHangId;
        this.tenKhachHang = tenKhachHang;
        this.ngayTao = ngayTao;
        this.ngayHenLay = ngayHenLay;
        this.ngayHenTra = ngayHenTra;
        this.hinhThuc = hinhThuc;
        this.trangThai = trangThai;
        this.tienDatCoc = tienDatCoc;
        this.trangThaiDatCoc = trangThaiDatCoc;
        this.chiTiet = chiTiet;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public Long getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Long khachHangId) { this.khachHangId = khachHangId; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }
    public LocalDate getNgayHenLay() { return ngayHenLay; }
    public void setNgayHenLay(LocalDate ngayHenLay) { this.ngayHenLay = ngayHenLay; }
    public LocalDate getNgayHenTra() { return ngayHenTra; }
    public void setNgayHenTra(LocalDate ngayHenTra) { this.ngayHenTra = ngayHenTra; }
    public HinhThucThue getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(HinhThucThue hinhThuc) { this.hinhThuc = hinhThuc; }
    public PhieuThueStatus getTrangThai() { return trangThai; }
    public void setTrangThai(PhieuThueStatus trangThai) { this.trangThai = trangThai; }
    public Double getTienDatCoc() { return tienDatCoc; }
    public void setTienDatCoc(Double tienDatCoc) { this.tienDatCoc = tienDatCoc; }
    public TrangThaiDatCoc getTrangThaiDatCoc() { return trangThaiDatCoc; }
    public void setTrangThaiDatCoc(TrangThaiDatCoc trangThaiDatCoc) { this.trangThaiDatCoc = trangThaiDatCoc; }
    public List<ChiTietPhieuThueDto> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietPhieuThueDto> chiTiet) { this.chiTiet = chiTiet; }
}
