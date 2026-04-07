package com.rental.order_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateOrderRequest {

    @NotNull(message = "Ngày hẹn lấy không được để trống")
    @Future(message = "Ngày hẹn lấy phải là ngày trong tương lai")
    private LocalDate ngayHenLay;

    @NotNull(message = "Ngày hẹn trả không được để trống")
    @Future(message = "Ngày hẹn trả phải là ngày trong tương lai")
    private LocalDate ngayHenTra;

    public CreateOrderRequest() {}

    public CreateOrderRequest(LocalDate ngayHenLay, LocalDate ngayHenTra) {
        this.ngayHenLay = ngayHenLay;
        this.ngayHenTra = ngayHenTra;
    }

    public LocalDate getNgayHenLay() { return ngayHenLay; }
    public void setNgayHenLay(LocalDate ngayHenLay) { this.ngayHenLay = ngayHenLay; }
    public LocalDate getNgayHenTra() { return ngayHenTra; }
    public void setNgayHenTra(LocalDate ngayHenTra) { this.ngayHenTra = ngayHenTra; }
}
