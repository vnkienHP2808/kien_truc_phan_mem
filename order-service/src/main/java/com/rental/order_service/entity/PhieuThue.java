package com.rental.order_service.entity;

import com.rental.order_service.enums.HinhThucThue;
import com.rental.order_service.enums.PhieuThueStatus;
import com.rental.order_service.enums.TrangThaiDatCoc;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phieu_thue")
public class PhieuThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_phieu", unique = true)
    private String maPhieu;

    @Column(name = "khach_hang_id")
    private Long khachHangId;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_hen_lay")
    private LocalDate ngayHenLay;

    @Column(name = "ngay_hen_tra")
    private LocalDate ngayHenTra;

    @Enumerated(EnumType.STRING)
    @Column(name = "hinh_thuc")
    private HinhThucThue hinhThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private PhieuThueStatus trangThai;

    @Column(name = "tien_dat_coc")
    private Double tienDatCoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_dat_coc")
    private TrangThaiDatCoc trangThaiDatCoc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "phieu_thue_id")
    private List<ChiTietPhieuThue> chiTiet = new ArrayList<>();

    public PhieuThue() {}

    public PhieuThue(Long id, String maPhieu, Long khachHangId,
                     LocalDate ngayTao, LocalDate ngayHenLay, LocalDate ngayHenTra,
                     HinhThucThue hinhThuc, PhieuThueStatus trangThai,
                     Double tienDatCoc, TrangThaiDatCoc trangThaiDatCoc) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.khachHangId = khachHangId;
        this.ngayTao = ngayTao;
        this.ngayHenLay = ngayHenLay;
        this.ngayHenTra = ngayHenTra;
        this.hinhThuc = hinhThuc;
        this.trangThai = trangThai;
        this.tienDatCoc = tienDatCoc;
        this.trangThaiDatCoc = trangThaiDatCoc;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public Long getKhachHangId() { return khachHangId; }
    public void setKhachHangId(Long khachHangId) { this.khachHangId = khachHangId; }
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
    public List<ChiTietPhieuThue> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietPhieuThue> chiTiet) { this.chiTiet = chiTiet; }
}
