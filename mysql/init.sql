-- =============================================================
--  HỆ THỐNG THUÊ TRANG PHỤC - INIT DATABASE
--  Sinh từ entities: NguoiDung, KhachHang, LoaiTrangPhuc,
--                    TrangPhuc, PhieuThue, ChiTietPhieuThue,
--                    GioHang, ChiTietGioHang
-- =============================================================

-- Mỗi service dùng schema riêng (microservice)
CREATE DATABASE IF NOT EXISTS customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS costume_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS order_db    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- =============================================================
--  1. CUSTOMER SERVICE  (customer_db)
-- =============================================================
USE customer_db;

DROP TABLE IF EXISTS khach_hang;

CREATE TABLE khach_hang (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    ma_khach_hang   VARCHAR(50)     UNIQUE,
    ho_ten          VARCHAR(100),
    username        VARCHAR(100)    UNIQUE,
    password        VARCHAR(255),
    email           VARCHAR(100),
    so_dien_thoai   VARCHAR(20),
    dia_chi         VARCHAR(255),
    dob             VARCHAR(20),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO khach_hang (ma_khach_hang, ho_ten, username, password, email, so_dien_thoai, dia_chi, dob) VALUES
('KH001', 'Nguyễn Văn An',    'nguyenvanan',    '$2a$10$hashedpwd1', 'an.nguyen@email.com',    '0901111111', '12 Lý Thường Kiệt, Hà Nội',      '1995-03-15'),
('KH002', 'Trần Thị Bình',    'tranthibinh',    '$2a$10$hashedpwd2', 'binh.tran@email.com',    '0902222222', '45 Nguyễn Huệ, TP. HCM',         '1998-07-22'),
('KH003', 'Lê Hoàng Cường',   'lehoangcuong',   '$2a$10$hashedpwd3', 'cuong.le@email.com',     '0903333333', '89 Trần Phú, Đà Nẵng',           '1992-11-08'),
('KH004', 'Phạm Thị Dung',    'phamthidung',    '$2a$10$hashedpwd4', 'dung.pham@email.com',    '0904444444', '23 Bà Triệu, Hà Nội',            '2000-01-30'),
('KH005', 'Hoàng Minh Đức',   'hoangminhduc',   '$2a$10$hashedpwd5', 'duc.hoang@email.com',    '0905555555', '67 Lê Lợi, Huế',                 '1997-05-19');


-- =============================================================
--  2. COSTUME SERVICE  (costume_db)
-- =============================================================
USE costume_db;

DROP TABLE IF EXISTS trang_phuc;
DROP TABLE IF EXISTS loai_trang_phuc;

CREATE TABLE loai_trang_phuc (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    ma_loai_trang_phuc  VARCHAR(50)     UNIQUE,
    ten_loai_trang_phuc VARCHAR(150),
    mo_ta               TEXT,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO loai_trang_phuc (ma_loai_trang_phuc, ten_loai_trang_phuc, mo_ta) VALUES
('LTP001', 'Hanbok',             'Trang phục truyền thống Hàn Quốc, thường dùng trong lễ hội và chụp ảnh kỷ niệm.'),
('LTP002', 'Áo dài',             'Trang phục truyền thống Việt Nam, phù hợp lễ cưới, tốt nghiệp và sự kiện trọng đại.'),
('LTP003', 'Kimono',             'Trang phục truyền thống Nhật Bản, phong cách thanh lịch và tinh tế.'),
('LTP004', 'Cosplay',            'Hóa trang nhân vật anime, game, phim – phù hợp sự kiện và chụp ảnh concept.'),
('LTP005', 'Váy dạ hội',         'Đầm dự tiệc và sự kiện sang trọng.');

CREATE TABLE trang_phuc (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    ma_trang_phuc       VARCHAR(50)     UNIQUE,
    ten_trang_phuc      VARCHAR(200),
    loai_trang_phuc_id  BIGINT,
    kich_thuoc          VARCHAR(10),
    mau_sac             VARCHAR(50),
    gia_thue            DOUBLE,
    gia_goc             DOUBLE,
    trang_thai          ENUM('AVAILABLE','RENTED','UNAVAILABLE') DEFAULT 'AVAILABLE',
    PRIMARY KEY (id),
    CONSTRAINT fk_trang_phuc_loai FOREIGN KEY (loai_trang_phuc_id) REFERENCES loai_trang_phuc(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO trang_phuc (ma_trang_phuc, ten_trang_phuc, loai_trang_phuc_id, kich_thuoc, mau_sac, gia_thue, gia_goc, trang_thai) VALUES
-- Hanbok
('TP001', 'Hanbok nữ truyền thống xanh lá',     1, 'M',  'Xanh lá',   250000, 1500000, 'AVAILABLE'),
('TP002', 'Hanbok nữ cô dâu đỏ hồng',           1, 'S',  'Đỏ hồng',   300000, 2000000, 'AVAILABLE'),
('TP003', 'Hanbok nam lễ phục xanh dương',       1, 'L',  'Xanh dương',200000, 1200000, 'RENTED'),
-- Áo dài
('TP004', 'Áo dài trắng truyền thống',           2, 'M',  'Trắng',     150000,  800000, 'AVAILABLE'),
('TP005', 'Áo dài cách tân hoa văn đỏ',         2, 'S',  'Đỏ',        180000,  950000, 'AVAILABLE'),
('TP006', 'Áo dài cô dâu vàng thêu',             2, 'L',  'Vàng gold', 350000, 2500000, 'RENTED'),
-- Kimono
('TP007', 'Kimono furisode họa tiết anh đào',   3, 'M',  'Hồng',      400000, 3000000, 'AVAILABLE'),
('TP008', 'Kimono yukata mùa hè xanh navy',     3, 'S',  'Xanh navy', 200000, 1000000, 'AVAILABLE'),
-- Cosplay
('TP009', 'Cosplay Nezuko (Demon Slayer)',       4, 'S',  'Hồng/đen',  200000,  700000, 'AVAILABLE'),
('TP010', 'Cosplay Naruto Uzumaki',              4, 'M',  'Cam',        180000,  600000, 'UNAVAILABLE'),
-- Váy dạ hội
('TP011', 'Đầm dạ hội xoè đỏ sequin',            5, 'M',  'Đỏ',        500000, 4000000, 'AVAILABLE'),
('TP012', 'Đầm dạ hội đen tối giản',             5, 'L',  'Đen',        450000, 3500000, 'AVAILABLE');


-- =============================================================
--  3. ORDER SERVICE  (order_db)
-- =============================================================
USE order_db;

DROP TABLE IF EXISTS chi_tiet_gio_hang;
DROP TABLE IF EXISTS gio_hang;
DROP TABLE IF EXISTS chi_tiet_phieu_thue;
DROP TABLE IF EXISTS phieu_thue;

-- Bảng Giỏ Hàng
CREATE TABLE gio_hang (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_gio_hang     VARCHAR(50) UNIQUE NOT NULL,
    khach_hang_id   BIGINT UNIQUE NOT NULL,
    ngay_tao        DATE,
    ngay_cap_nhat   DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE chi_tiet_gio_hang (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    gio_hang_id     BIGINT NOT NULL,
    trang_phuc_id   BIGINT NOT NULL,
    so_luong        INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_ctgh_gio FOREIGN KEY (gio_hang_id) REFERENCES gio_hang(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Bảng Phiếu Thuê
CREATE TABLE phieu_thue (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    ma_phieu            VARCHAR(50)     UNIQUE,
    khach_hang_id       BIGINT,
    ngay_tao            DATE,
    ngay_hen_lay        DATE,
    ngay_hen_tra        DATE,
    hinh_thuc           ENUM('ONLINE','TAI_CHO')                                   DEFAULT 'ONLINE',
    trang_thai          ENUM('CHO_XU_LY','CHO_XAC_NHAN','DA_XAC_NHAN','DA_HUY')   DEFAULT 'CHO_XU_LY',
    tien_dat_coc        DOUBLE,
    trang_thai_dat_coc  ENUM('CHUA_THANH_TOAN','DA_THANH_TOAN','DA_HOAN_TRA')      DEFAULT 'CHUA_THANH_TOAN',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE chi_tiet_phieu_thue (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    phieu_thue_id   BIGINT,
    trang_phuc_id   BIGINT,
    so_luong        INT         DEFAULT 1,
    don_gia         DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT fk_ctpt_phieu FOREIGN KEY (phieu_thue_id) REFERENCES phieu_thue(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Phiếu 1: KH001 đặt online, đã xác nhận, đã đặt cọc
INSERT INTO phieu_thue (ma_phieu, khach_hang_id, ngay_tao, ngay_hen_lay, ngay_hen_tra, hinh_thuc, trang_thai, tien_dat_coc, trang_thai_dat_coc) VALUES
('PT20260401001', 1, '2026-04-01', '2026-04-05', '2026-04-07', 'ONLINE',  'DA_XAC_NHAN',  500000, 'DA_THANH_TOAN');
INSERT INTO chi_tiet_phieu_thue (phieu_thue_id, trang_phuc_id, so_luong, don_gia) VALUES
(1, 1, 1, 250000),
(1, 4, 1, 150000);

-- Phiếu 2: KH002 đặt tại chỗ, chờ xác nhận, chưa cọc
INSERT INTO phieu_thue (ma_phieu, khach_hang_id, ngay_tao, ngay_hen_lay, ngay_hen_tra, hinh_thuc, trang_thai, tien_dat_coc, trang_thai_dat_coc) VALUES
('PT20260402001', 2, '2026-04-02', '2026-04-06', '2026-04-08', 'TAI_CHO', 'CHO_XAC_NHAN', 400000, 'CHUA_THANH_TOAN');
INSERT INTO chi_tiet_phieu_thue (phieu_thue_id, trang_phuc_id, so_luong, don_gia) VALUES
(2, 7, 1, 400000);

-- Phiếu 3: KH003 đặt online, đã hủy
INSERT INTO phieu_thue (ma_phieu, khach_hang_id, ngay_tao, ngay_hen_lay, ngay_hen_tra, hinh_thuc, trang_thai, tien_dat_coc, trang_thai_dat_coc) VALUES
('PT20260402002', 3, '2026-04-02', '2026-04-10', '2026-04-12', 'ONLINE',  'DA_HUY',        300000, 'DA_HOAN_TRA');
INSERT INTO chi_tiet_phieu_thue (phieu_thue_id, trang_phuc_id, so_luong, don_gia) VALUES
(3, 11, 1, 500000);

-- Phiếu 4: KH004 đặt tại chỗ, chờ xử lý, chưa cọc
INSERT INTO phieu_thue (ma_phieu, khach_hang_id, ngay_tao, ngay_hen_lay, ngay_hen_tra, hinh_thuc, trang_thai, tien_dat_coc, trang_thai_dat_coc) VALUES
('PT20260403001', 4, '2026-04-03', '2026-04-08', '2026-04-10', 'TAI_CHO', 'CHO_XU_LY',   200000, 'CHUA_THANH_TOAN');
INSERT INTO chi_tiet_phieu_thue (phieu_thue_id, trang_phuc_id, so_luong, don_gia) VALUES
(4, 2, 1, 300000),
(4, 5, 1, 180000);

-- Phiếu 5: KH005 đặt online, đã xác nhận, đã cọc
INSERT INTO phieu_thue (ma_phieu, khach_hang_id, ngay_tao, ngay_hen_lay, ngay_hen_tra, hinh_thuc, trang_thai, tien_dat_coc, trang_thai_dat_coc) VALUES
('PT20260404001', 5, '2026-04-04', '2026-04-09', '2026-04-11', 'ONLINE',  'DA_XAC_NHAN',  900000, 'DA_THANH_TOAN');
INSERT INTO chi_tiet_phieu_thue (phieu_thue_id, trang_phuc_id, so_luong, don_gia) VALUES
(5, 9,  1, 200000),
(5, 12, 1, 450000);

-- Thêm data giả lập Giỏ hàng cho KH001 đang có đồ chưa thanh toán
INSERT INTO gio_hang (ma_gio_hang, khach_hang_id, ngay_tao, ngay_cap_nhat) VALUES
('GH-1', 1, '2026-04-08', '2026-04-08');
INSERT INTO chi_tiet_gio_hang (gio_hang_id, trang_phuc_id, so_luong) VALUES
(1, 8, 1);

