import React, { useEffect, useState } from 'react';
import { AppProvider, useApp } from './AppContext';
import DangNhapPage from './pages/DangNhapPage';
import TrangPhucPage from './pages/TrangPhucPage';
import GioHangPage from './pages/GioHangPage';
import PhieuThuePage from './pages/PhieuThuePage';
import API from './api';

const NAV_ITEMS = [
  { key: 'trang-phuc', label: 'Trang phục', icon: 'bi-grid' },
  { key: 'gio-hang', label: 'Giỏ hàng', icon: 'bi-cart3' },
  { key: 'phieu-thue', label: 'Phiếu thuê', icon: 'bi-file-text' },
];

function MainLayout() {
  const { khachHangId, setKhachHangId, trangHienTai, setTrangHienTai } = useApp();
  const [soGio, setSoGio] = useState(0);
  const [tenKhach, setTenKhach] = useState('');

  useEffect(() => {
    if (!khachHangId) return;
    // Lấy tên khách
    API.get(`/api/khach-hang/${khachHangId}`)
      .then(r => setTenKhach(r.data?.hoTen || `KH #${khachHangId}`))
      .catch(() => setTenKhach(`KH #${khachHangId}`));
  }, [khachHangId]);

  useEffect(() => {
    if (!khachHangId) return;
    // Đếm giỏ hàng
    API.get(`/api/phieu-thue/gio-hang/${khachHangId}`)
      .then(r => setSoGio(Array.isArray(r.data) ? r.data.length : 0))
      .catch(() => setSoGio(0));
  }, [khachHangId, trangHienTai]);

  if (!khachHangId) return <DangNhapPage />;

  return (
    <div style={{minHeight: '100vh', background: '#f8f9fa'}}>
      {/* Navbar */}
      <nav className="navbar navbar-dark bg-primary shadow-sm">
        <div className="container-fluid px-4">
          <span className="navbar-brand fw-bold">
            <i className="bi bi-shop me-2"></i>
            Thuê Trang Phục Online
          </span>
          <div className="d-flex align-items-center gap-3">
            <span className="text-white small">
              <i className="bi bi-person-circle me-1"></i>{tenKhach}
            </span>
            <button
              className="btn btn-outline-light btn-sm"
              onClick={() => setKhachHangId(null)}
            >
              <i className="bi bi-box-arrow-right me-1"></i>Thoát
            </button>
          </div>
        </div>
      </nav>

      {/* Nav tabs */}
      <div className="bg-white border-bottom shadow-sm">
        <div className="container-fluid px-4">
          <ul className="nav nav-tabs border-0">
            {NAV_ITEMS.map(item => (
              <li className="nav-item" key={item.key}>
                <button
                  className={`nav-link border-0 py-3 px-4 ${trangHienTai === item.key ? 'active fw-semibold' : 'text-muted'}`}
                  onClick={() => setTrangHienTai(item.key)}
                  style={trangHienTai === item.key ? {borderBottom: '2px solid #0d6efd', color: '#0d6efd'} : {}}
                >
                  <i className={`bi ${item.icon} me-2`}></i>
                  {item.label}
                  {item.key === 'gio-hang' && soGio > 0 && (
                    <span className="badge bg-danger ms-1" style={{fontSize: 10}}>{soGio}</span>
                  )}
                </button>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Content */}
      <div className="container-fluid px-4 py-4">
        {trangHienTai === 'trang-phuc' && <TrangPhucPage />}
        {trangHienTai === 'gio-hang' && <GioHangPage />}
        {trangHienTai === 'phieu-thue' && <PhieuThuePage />}
      </div>
    </div>
  );
}

export default function App() {
  return (
    <AppProvider>
      <MainLayout />
    </AppProvider>
  );
}
