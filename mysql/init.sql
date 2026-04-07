-- Tạo 3 database tương ứng 3 microservice
-- File này được MySQL tự động chạy khi container khởi động lần đầu

CREATE DATABASE IF NOT EXISTS customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS costume_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS order_db    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
