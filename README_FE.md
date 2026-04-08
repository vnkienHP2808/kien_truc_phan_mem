# 🎭 Giao Diện Hệ Thống Thuê Trang Phục

Hai folder React độc lập giao tiếp với Backend qua API Gateway tại `http://localhost:8080`.

---

## 📁 Cấu trúc

```
quan-ly-khach-hang/       ← Module quản lý (admin)  → chạy port 3000
khach-dat-trang-phuc/     ← Module khách hàng        → chạy port 3001
```

---

## 🚀 Cách chạy

### Yêu cầu
- Node.js 16+
- Backend microservices đang chạy tại `http://localhost:8080`

### Module 1 – Quản lý khách hàng (port 3000)
```bash
cd quan-ly-khach-hang
npm install
npm start
```
Truy cập: http://localhost:3000

### Module 2 – Khách đặt trang phục (port 3001)
```bash
cd khach-dat-trang-phuc
npm install
npm start
```
Truy cập: http://localhost:3001

---

## 📋 Tính năng

### Module 1 – Quản lý khách hàng
| Tính năng | Mô tả |
|---|---|
| Xem danh sách | Hiển thị tất cả khách hàng, có tìm kiếm |
| Thêm khách hàng | Form nhập đầy đủ thông tin |
| Sửa khách hàng | Cập nhật thông tin (username không đổi được) |
| Xóa khách hàng | Xóa có confirm |
| Xem chi tiết | Popup thông tin đầy đủ |

### Module 2 – Khách đặt trang phục
| Tính năng | Mô tả |
|---|---|
| Đăng nhập | Nhập ID khách hàng để vào hệ thống |
| Xem trang phục | Lọc theo trạng thái AVAILABLE / Tất cả |
| Thêm vào giỏ | Thêm trang phục còn trống vào giỏ hàng |
| Giỏ hàng | Xem, xóa đồ; tính tổng tiền + 30% cọc |
| Chốt đơn | Chọn ngày lấy/trả, tự động tạo phiếu thuê |
| Phiếu thuê | Xem lịch sử, xem chi tiết, thanh toán cọc |

---

## ⚙️ API Gateway Endpoints sử dụng

| Service | Base URL |
|---|---|
| Khách hàng | `GET/POST/PUT/DELETE http://localhost:8080/api/khach-hang` |
| Trang phục | `GET http://localhost:8080/api/trang-phuc` |
| Giỏ hàng & Phiếu thuê | `GET/POST/DELETE/PATCH http://localhost:8080/api/phieu-thue` |
