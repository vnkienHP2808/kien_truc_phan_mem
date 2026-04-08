import React, { useEffect, useState, useCallback } from 'react';
import API from '../api';
import { useApp } from '../AppContext';

const BADGE_TRANG_THAI = {
  AVAILABLE: { cls: 'bg-success', label: 'Còn trống' },
  RENTED: { cls: 'bg-secondary', label: 'Đang thuê' },
  UNAVAILABLE: { cls: 'bg-danger', label: 'Không có' },
};

export default function TrangPhucPage() {
  const { khachHangId, setTrangHienTai } = useApp();
  const [danhSach, setDanhSach] = useState([]);
  const [loading, setLoading] = useState(false);
  const [thongBao, setThongBao] = useState('');
  const [loi, setLoi] = useState('');
  const [dangThem, setDangThem] = useState(null);
  const [locTrangThai, setLocTrangThai] = useState('AVAILABLE');

  const taiDuLieu = useCallback(async () => {
    setLoading(true);
    setLoi('');
    try {
      const url = locTrangThai === 'AVAILABLE'
        ? '/api/trang-phuc/available'
        : '/api/trang-phuc';
      const res = await API.get(url);
      setDanhSach(res.data);
    } catch {
      setLoi('Không thể tải danh sách trang phục.');
    } finally {
      setLoading(false);
    }
  }, [locTrangThai]);

  useEffect(() => { taiDuLieu(); }, [taiDuLieu]);

  const themVaoGio = async (trangPhucId, tenTrangPhuc) => {
    setDangThem(trangPhucId);
    try {
      await API.post(`/api/phieu-thue/gio-hang/${khachHangId}?trangPhucId=${trangPhucId}`);
      setThongBao(`Đã thêm "${tenTrangPhuc}" vào giỏ hàng!`);
      setTimeout(() => setThongBao(''), 3000);
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || '';
      if (typeof msg === 'string' && msg.toLowerCase().includes('đã')) {
        setThongBao(`"${tenTrangPhuc}" đã có trong giỏ hàng rồi!`);
      } else {
        setLoi('Thêm vào giỏ thất bại. Thử lại!');
      }
      setTimeout(() => { setThongBao(''); setLoi(''); }, 3000);
    } finally {
      setDangThem(null);
    }
  };

  const danhSachHienThi = locTrangThai === 'ALL'
    ? danhSach
    : danhSach.filter(tp => tp.trangThai === locTrangThai);

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">
            <i className="bi bi-grid me-2 text-primary"></i>
            Danh Sách Trang Phục
          </h4>
          <small className="text-muted">Chọn trang phục và thêm vào giỏ hàng</small>
        </div>
        <button className="btn btn-outline-primary btn-sm" onClick={() => setTrangHienTai('gio-hang')}>
          <i className="bi bi-cart me-1"></i>Xem giỏ hàng
        </button>
      </div>

      {thongBao && (
        <div className="alert alert-success alert-dismissible d-flex align-items-center py-2">
          <i className="bi bi-check-circle-fill me-2"></i>{thongBao}
          <button className="btn-close" onClick={() => setThongBao('')}></button>
        </div>
      )}
      {loi && (
        <div className="alert alert-danger py-2">
          <i className="bi bi-exclamation-circle me-1"></i>{loi}
        </div>
      )}

      {/* Bộ lọc */}
      <div className="mb-3 d-flex gap-2">
        {[
          { key: 'AVAILABLE', label: 'Còn trống', icon: 'bi-check-circle' },
          { key: 'ALL', label: 'Tất cả', icon: 'bi-grid' },
        ].map(({ key, label, icon }) => (
          <button
            key={key}
            className={`btn btn-sm ${locTrangThai === key ? 'btn-primary' : 'btn-outline-secondary'}`}
            onClick={() => setLocTrangThai(key)}
          >
            <i className={`bi ${icon} me-1`}></i>{label}
          </button>
        ))}
        <button className="btn btn-outline-secondary btn-sm ms-auto" onClick={taiDuLieu}>
          <i className="bi bi-arrow-clockwise me-1"></i>Làm mới
        </button>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary"></div>
          <p className="mt-2 text-muted">Đang tải...</p>
        </div>
      ) : danhSachHienThi.length === 0 ? (
        <div className="text-center py-5 text-muted">
          <i className="bi bi-inbox fs-3 d-block mb-2"></i>
          Không có trang phục nào
        </div>
      ) : (
        <div className="row g-3">
          {danhSachHienThi.map(tp => {
            const badge = BADGE_TRANG_THAI[tp.trangThai] || { cls: 'bg-secondary', label: tp.trangThai };
            const coTheThue = tp.trangThai === 'AVAILABLE';
            return (
              <div key={tp.id} className="col-md-4 col-lg-3">
                <div className={`card h-100 border ${coTheThue ? '' : 'opacity-75'}`}>
                  <div className="card-body">
                    <div className="d-flex justify-content-between align-items-start mb-2">
                      <span className={`badge ${badge.cls}`}>{badge.label}</span>
                      <small className="text-muted">{tp.maTrangPhuc}</small>
                    </div>
                    <h6 className="card-title fw-semibold mb-1">{tp.tenTrangPhuc}</h6>
                    <p className="text-muted small mb-2">{tp.tenLoaiTrangPhuc || 'Chưa phân loại'}</p>
                    <div className="d-flex gap-2 flex-wrap mb-2">
                      {tp.kichThuoc && (
                        <span className="badge bg-light text-dark border">
                          <i className="bi bi-rulers me-1"></i>{tp.kichThuoc}
                        </span>
                      )}
                      {tp.mauSac && (
                        <span className="badge bg-light text-dark border">
                          <i className="bi bi-palette me-1"></i>{tp.mauSac}
                        </span>
                      )}
                    </div>
                    <div className="fw-bold text-primary">
                      {tp.giaThue?.toLocaleString('vi-VN')}đ<small className="text-muted fw-normal">/lần</small>
                    </div>
                  </div>
                  <div className="card-footer bg-transparent border-top pt-2 pb-3 px-3">
                    <button
                      className="btn btn-primary btn-sm w-100"
                      disabled={!coTheThue || dangThem === tp.id}
                      onClick={() => coTheThue && themVaoGio(tp.id, tp.tenTrangPhuc)}
                    >
                      {dangThem === tp.id ? (
                        <span className="spinner-border spinner-border-sm"></span>
                      ) : coTheThue ? (
                        <><i className="bi bi-cart-plus me-1"></i>Thêm vào giỏ</>
                      ) : (
                        <><i className="bi bi-x-circle me-1"></i>Không thể thuê</>
                      )}
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
