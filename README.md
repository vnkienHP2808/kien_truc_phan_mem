# Costume Rental – Refactored Code

## Tổng quan kiến trúc

```
discovery-server  :8761   ← Eureka Registry
api-gateway       :8080   ← Spring Cloud Gateway MVC
customer-service  :8081   ← Quản lý khách hàng
costume-service   :8082   ← Quản lý trang phục
order-service     :8083   ← Quản lý phiếu thuê
```

## Thứ tự khởi động

1. `discovery-server` (khởi động trước)
2. `customer-service`, `costume-service`, `order-service` (song song)
3. `api-gateway` (khởi động sau cùng)

---

### 1. Fake Repository → JPA Repository (thay đổi lớn nhất)

| Trước | Sau |
|---|---|
| `FakeTrangPhucRepository` (ArrayList in-memory) | `TrangPhucRepository extends JpaRepository<TrangPhuc, Long>` |
| `FakeKhachHangRepository` (ArrayList in-memory) | `KhachHangRepository extends JpaRepository<KhachHang, Long>` |
| `FakePhieuThueRepository` (ArrayList in-memory) | `PhieuThueRepository extends JpaRepository<PhieuThue, Long>` |
| `FakeGioHangRepository`, `FakeLoaiTrangPhucRepository` | `LoaiTrangPhucRepository extends JpaRepository` |

**Lý do:** Data in-memory mất khi restart server, không thể dùng production.

### 2. POJO Entity → JPA Entity

| Trước | Sau |
|---|---|
| `class TrangPhuc {}` – POJO thuần | `@Entity @Table(name="trang_phuc") class TrangPhuc` |
| `class KhachHang extends NguoiDung {}` – không có annotation | `@Entity @Table` + `NguoiDung` dùng `@MappedSuperclass` |
| `class PhieuThue {}` – POJO | `@Entity` + `@OneToMany(cascade=ALL)` với `ChiTietPhieuThue` |

### 3. Interface + Impl → BusinessService (bỏ interface thừa)

| Trước | Sau |
|---|---|
| `ICostumeService` + `CostumeServiceImpl` | `CostumeBusinessService` |
| `IKhachHangService` + `KhachHangServiceImpl` | `KhachHangBusinessService` |
| `IOrderService` + `OrderServiceImpl` | `OrderBusinessService` |

**Lý do:** Khi chỉ có 1 implementation, interface không mang lại giá trị, gây thêm boilerplate.

### 4. Thêm SLF4J Logger

Mọi controller và service đều có:
```java
private static final Logger logger = LoggerFactory.getLogger(XxxClass.class);
logger.info("...");
logger.warn("...");
logger.error("...");
```

### 5. Thêm Bean Validation

- `TrangPhucRequestDto`: `@NotBlank`, `@Min`
- `CreateOrderRequest`: `@NotNull`, `@Future`
- Controller dùng `@Valid @RequestBody`

### 6. Thêm GlobalExceptionHandler

Mỗi service đều có `@RestControllerAdvice` trả về JSON nhất quán:
```json
{ "status": 400, "error": "Validation Failed", "details": {...} }
```

### 7. application.properties → application.yml

Đổi sang YAML với đầy đủ: datasource, JPA, Eureka, logging charset UTF-8.

### 8. Thêm Discovery Server (Eureka)

Module `discovery-server` mới hoàn toàn, chạy tại cổng `8761`.
Tất cả service đều có `spring-cloud-starter-netflix-eureka-client`.

### 9. Thêm CorsConfig cho api-gateway

Cho phép frontend React (`:3000`, `:5173`) gọi qua gateway.

### 10. RestTemplate config tách riêng

`@Bean RestTemplate` chuyển từ main class sang `RestTemplateConfig.java`.
URL service đọc từ `application.yml` qua `@Value` thay vì hard-code trong service.

---

## Cấu hình Database

Mỗi service cần một database riêng trong MySQL:

```sql
CREATE DATABASE customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE costume_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE order_db    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Mặc định kết nối: `root / 123456 @ localhost:3306`
(Thay đổi trong `application.yml` của từng service nếu cần.)

---

## API Endpoints

### customer-service (:8081)
| Method | Path | Mô tả |
|---|---|---|
| GET | `/api/khach-hang` | Lấy danh sách khách hàng |
| GET | `/api/khach-hang/{id}` | Lấy chi tiết theo id |
| GET | `/api/khach-hang/username/{username}` | Tìm theo username |
| GET | `/api/khach-hang/ma/{maKhachHang}` | Tìm theo mã |

### costume-service (:8082)
| Method | Path | Mô tả |
|---|---|---|
| GET | `/api/trang-phuc` | Lấy tất cả trang phục |
| GET | `/api/trang-phuc/available` | Lấy trang phục còn hàng |
| GET | `/api/trang-phuc/{id}` | Lấy chi tiết |
| POST | `/api/trang-phuc` | Tạo mới |
| PUT | `/api/trang-phuc/{id}` | Cập nhật |
| DELETE | `/api/trang-phuc/{id}` | Xóa |
| PATCH | `/api/trang-phuc/{id}/trang-thai?trangThai=RENTED` | Cập nhật trạng thái |

### order-service (:8083)
| Method | Path | Mô tả |
|---|---|---|
| POST | `/api/phieu-thue/khach-hang/{id}` | Tạo phiếu thuê |
| GET | `/api/phieu-thue/khach-hang/{id}` | Lấy phiếu thuê theo khách hàng |
| GET | `/api/phieu-thue/{maPhieu}` | Lấy phiếu thuê theo mã |
| PATCH | `/api/phieu-thue/{maPhieu}/huy` | Hủy phiếu thuê |
| PATCH | `/api/phieu-thue/{maPhieu}/dat-coc` | Xác nhận đặt cọc |
| PATCH | `/api/phieu-thue/{maPhieu}/xac-nhan` | Lễ tân xác nhận |
