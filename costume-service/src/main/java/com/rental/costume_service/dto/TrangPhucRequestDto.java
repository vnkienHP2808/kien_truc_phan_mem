package com.rental.costume_service.dto;

import com.rental.costume_service.enums.TrangPhucStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO để tạo / cập nhật trang phục.
 */
public class TrangPhucRequestDto {

    @NotBlank(message = "Mã trang phục không được để trống")
    private String maTrangPhuc;

    @NotBlank(message = "Tên trang phục không được để trống")
    private String tenTrangPhuc;

    @NotNull(message = "Loại trang phục không được để trống")
    private Long loaiTrangPhucId;

    @NotBlank(message = "Kích thước không được để trống")
    private String kichThuoc;

    @NotBlank(message = "Màu sắc không được để trống")
    private String mauSac;

    @Min(value = 0, message = "Giá thuê phải >= 0")
    private Double giaThue;

    @Min(value = 0, message = "Giá gốc phải >= 0")
    private Double giaGoc;

    private TrangPhucStatus trangThai = TrangPhucStatus.AVAILABLE;

    public TrangPhucRequestDto() {}

    public String getMaTrangPhuc() { return maTrangPhuc; }
    public void setMaTrangPhuc(String maTrangPhuc) { this.maTrangPhuc = maTrangPhuc; }

    public String getTenTrangPhuc() { return tenTrangPhuc; }
    public void setTenTrangPhuc(String tenTrangPhuc) { this.tenTrangPhuc = tenTrangPhuc; }

    public Long getLoaiTrangPhucId() { return loaiTrangPhucId; }
    public void setLoaiTrangPhucId(Long loaiTrangPhucId) { this.loaiTrangPhucId = loaiTrangPhucId; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public Double getGiaThue() { return giaThue; }
    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }

    public Double getGiaGoc() { return giaGoc; }
    public void setGiaGoc(Double giaGoc) { this.giaGoc = giaGoc; }

    public TrangPhucStatus getTrangThai() { return trangThai; }
    public void setTrangThai(TrangPhucStatus trangThai) { this.trangThai = trangThai; }
}
