import React from 'react';
import KhachHangPage from './pages/KhachHangPage';

export default function App() {
  return (
    <div style={{minHeight: '100vh', background: '#f8f9fa'}}>
      {/* Navbar */}
      <nav className="navbar navbar-dark bg-primary shadow-sm">
        <div className="container-fluid px-4">
          <span className="navbar-brand fw-bold">
            <i className="bi bi-shop me-2"></i>
            Thuê Trang Phục – Quản Lý
          </span>
          <span className="text-white-50 small">
            <i className="bi bi-circle-fill text-success me-1" style={{fontSize: 8}}></i>
            Hệ thống đang hoạt động
          </span>
        </div>
      </nav>

      {/* Sidebar + Content */}
      <div className="d-flex" style={{minHeight: 'calc(100vh - 56px)'}}>
        {/* Sidebar */}
        <div className="bg-white border-end shadow-sm" style={{width: 220, minHeight: '100%', flexShrink: 0}}>
          <div className="p-3">
            <p className="text-muted small fw-semibold text-uppercase mb-2 px-2">Quản lý</p>
            <nav className="nav flex-column gap-1">
              <a className="nav-link active rounded d-flex align-items-center gap-2 px-3 py-2"
                style={{background: '#e7f1ff', color: '#0d6efd'}} href="#khachhang">
                <i className="bi bi-people-fill"></i>
                <span>Khách hàng</span>
              </a>
            </nav>
          </div>
        </div>

        {/* Main */}
        <div className="flex-grow-1 p-4" style={{maxWidth: 'calc(100% - 220px)'}}>
          <KhachHangPage />
        </div>
      </div>
    </div>
  );
}
