# ============================================================
# fe_management/Dockerfile
# Module: Quản lý khách hàng  →  chạy tại http://localhost:3000
# ============================================================

# Stage 1: Build React app
FROM node:18-alpine AS build
WORKDIR /app

# Copy package files và install dependencies
COPY package.json ./
RUN npm install --legacy-peer-deps

# Copy toàn bộ source
COPY . .

# Build production bundle
# REACT_APP_API_URL được inject lúc build để FE gọi đúng gateway
ARG REACT_APP_API_URL=http://localhost:8080
ENV REACT_APP_API_URL=$REACT_APP_API_URL

RUN npm run build

# Stage 2: Serve bằng nginx
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Xóa trang mặc định của nginx
RUN rm -rf ./*

# Copy build output từ stage 1
COPY --from=build /app/build .

# Copy nginx config tùy chỉnh (xử lý SPA routing + proxy API)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
