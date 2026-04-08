import React, { useEffect, useState, useCallback } from 'react';
import API from '../api';
import { useApp } from '../AppContext';

export default function GioHangPage() {
  const { khachHangId, setTrangHienTai } = useApp();
  const [gioHang, setGioHang] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loi, setLoi] = useState('');
  const [thongBao, setThongBao] = useState('');
  const [dangXoa, setDangXoa] = useState(null);

  // Modal chốt đơn
  const [hienModalChot, setHienModalChot] = useState(false);
  const [ngayHenLay, setNgayHenLay] = useState('');
  const [ngayHenTra, setNgayHenTra] = useState('');
  const [formLoi, setFormLoi] = useState({});
  const [dangTao, setDangTao] = useState(false);

  const taiGioHang = useCallback(async () => {
    setLoading(true);
    setLoi('');
    try {
      const res = await API.get(`/api/phieu-thue/gio-hang/${khachHangId}`);
      setGioHang(Array.isArray(res.data) ? res.data : []);
    } catch {
      setGioHang([]);
      setLoi('Không thể tải giỏ hàng.');
    } finally {
      setLoading(false);
    }
  }, [khachHangId]);

  useEffect(() => { taiGioHang(); }, [taiGioHang]);

  const xoaKhoiGio = async (trangPhucId, tenTrangPhuc) => {
    setDangXoa(trangPhucId);
    try {
      await API.delete(`/api/phieu-thue/gio-hang/${khachHangId}?trangPhucId=${trangPhucId}`);
      setThongBao(`Đã xóa "${tenTrangPhuc}" khỏi giỏ hàng`);
      taiGioHang();
      setTimeout(() => setThongBao(''), 3000);
    } catch {
      setLoi('Xóa thất bại!');
    } finally {
      setDangXoa(null);
    }
  };

  const kiemTraForm = () => {
    const loi = {};
    const today = new Date().toISOString().split('T')[0];
    if (!ngayHenLay) loi.ngayHenLay = 'Vui lòng chọn ngày lấy hàng';
    else if (ngayHenLay < today) loi.ngayHenLay = 'Ngày lấy không được trong quá khứ';
    if (!ngayHenTra) loi.ngayHenTra = 'Vui lòng chọn ngày trả hàng';
    else if (ngayHenTra <= ngayHenLay) loi.ngayHenTra = 'Ngày trả phải sau ngày lấy';
    return loi;
  };

  const chotDon = async () => {
    const loi = kiemTraForm();
    if (Object.keys(loi).length > 0) { setFormLoi(loi); return; }
    setDangTao(true);
    try {
      const res = await API.post(`/api/phieu-thue/khach-hang/${khachHangId}`, {
        ngayHenLay, ngayHenTra,
      });
      setHienModalChot(false);
      setThongBao(`Tạo phiếu thuê thành công! Mã phiếu: ${res.data?.maPhieu || ''}`);
      setTimeout(() => setThongBao(''), 5000);
      setTrangHienTai('phieu-thue');
    } catch (err) {
      setFormLoi({ chung: err.response?.data?.message || 'Tạo phiếu thuê thất bại. Thử lại!' });
    } finally {
      setDangTao(false);
    }
  };

  const tongTien = gioHang.reduce((sum, tp) => sum + (tp.giaThue || 0), 0);
  const tienCoc = tongTien * 0.3;

  const today = new Date().toISOString().split('T')[0];

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">
            <i className="bi bi-cart3 me-2 text-primary"></i>
            Giỏ Hàng Của Bạn
          </h4>
          <small className="text-muted">{gioHang.length} trang phục đang chờ đặt</small>
        </div>
        <button className="btn btn-outline-secondary btn-sm" onClick={() => setTrangHienTai('trang-phuc')}>
          <i className="bi bi-arrow-left me-1"></i>Tiếp tục chọn
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

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary"></div>
          <p className="mt-2 text-muted">Đang tải giỏ hàng...</p>
        </div>
      ) : gioHang.length === 0 ? (
        <div className="text-center py-5">
          <i className="bi bi-cart-x text-muted" style={{fontSize: 48}}></i>
          <p className="mt-3 text-muted">Giỏ hàng trống</p>
          <button className="btn btn-primary" onClick={() => setTrangHienTai('trang-phuc')}>
            <i className="bi bi-grid me-1"></i>Xem trang phục
          </button>
        </div>
      ) : (
        <div className="row g-4">
          {/* Danh sách */}
          <div className="col-md-8">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-0">
                <table className="table table-hover align-middle mb-0">
                  <thead className="table-light">
                    <tr>
                      <th>#</th>
                      <th>Trang phục</th>
                      <th>Kích thước</th>
                      <th className="text-end">Giá thuê</th>
                      <th className="text-center">Xóa</th>
                    </tr>
                  </thead>
                  <tbody>
                    {gioHang.map((tp, idx) => (
                      <tr key={tp.id}>
                        <td className="text-muted">{idx + 1}</td>
                        <td>
                          <div className="fw-medium">{tp.tenTrangPhuc}</div>
                          <small className="text-muted">{tp.maTrangPhuc}</small>
                        </td>
                        <td>
                          <span className="badge bg-light text-dark border">{tp.kichThuoc || '—'}</span>
                        </td>
                        <td className="text-end fw-semibold text-primary">
                          {tp.giaThue?.toLocaleString('vi-VN')}đ
                        </td>
                        <td className="text-center">
                          <button
                            className="btn btn-outline-danger btn-sm"
                            disabled={dangXoa === tp.id}
                            onClick={() => xoaKhoiGio(tp.id, tp.tenTrangPhuc)}
                          >
                            {dangXoa === tp.id
                              ? <span className="spinner-border spinner-border-sm"></span>
                              : <i className="bi bi-trash"></i>}
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Tóm tắt */}
          <div className="col-md-4">
            <div className="card border-0 shadow-sm">
              <div className="card-header bg-primary text-white fw-semibold">
                <i className="bi bi-receipt me-2"></i>Tóm tắt đơn hàng
              </div>
              <div className="card-body">
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Số lượng:</span>
                  <strong>{gioHang.length} trang phục</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Tổng tiền thuê:</span>
                  <strong>{tongTien.toLocaleString('vi-VN')}đ</strong>
                </div>
                <hr />
                <div className="d-flex justify-content-between mb-1">
                  <span className="text-muted">Đặt cọc (30%):</span>
                  <strong className="text-warning">{tienCoc.toLocaleString('vi-VN')}đ</strong>
                </div>
                <div className="alert alert-info py-2 small mt-2">
                  <i className="bi bi-info-circle me-1"></i>
                  Bạn sẽ thanh toán <strong>30% tiền cọc</strong> ngay khi chốt đơn.
                </div>
                <button
                  className="btn btn-success w-100 mt-1"
                  onClick={() => { setFormLoi({}); setHienModalChot(true); }}
                >
                  <i className="bi bi-check2-circle me-2"></i>Chốt đơn thuê
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Modal chốt đơn */}
      {hienModalChot && (
        <div className="modal show d-block" style={{background: 'rgba(0,0,0,0.5)'}}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  <i className="bi bi-calendar-check me-2"></i>Chọn ngày thuê
                </h5>
                <button className="btn-close" onClick={() => setHienModalChot(false)}></button>
              </div>
              <div className="modal-body">
                {formLoi.chung && (
                  <div className="alert alert-danger py-2 small">
                    <i className="bi bi-exclamation-circle me-1"></i>{formLoi.chung}
                  </div>
                )}
                <div className="mb-3">
                  <label className="form-label fw-medium">Ngày lấy hàng <span className="text-danger">*</span></label>
                  <input
                    type="date"
                    className={`form-control ${formLoi.ngayHenLay ? 'is-invalid' : ''}`}
                    min={today}
                    value={ngayHenLay}
                    onChange={e => setNgayHenLay(e.target.value)}
                  />
                  {formLoi.ngayHenLay && <div className="invalid-feedback">{formLoi.ngayHenLay}</div>}
                </div>
                <div className="mb-3">
                  <label className="form-label fw-medium">Ngày trả hàng <span className="text-danger">*</span></label>
                  <input
                    type="date"
                    className={`form-control ${formLoi.ngayHenTra ? 'is-invalid' : ''}`}
                    min={ngayHenLay || today}
                    value={ngayHenTra}
                    onChange={e => setNgayHenTra(e.target.value)}
                  />
                  {formLoi.ngayHenTra && <div className="invalid-feedback">{formLoi.ngayHenTra}</div>}
                </div>
                <div className="alert alert-warning py-2 small">
                  <i className="bi bi-exclamation-triangle me-1"></i>
                  Sau khi chốt, toàn bộ đồ trong giỏ sẽ được đặt và tính tiền cọc <strong>30%</strong>.
                </div>
              </div>
              <div className="modal-footer">
                <button className="btn btn-secondary" onClick={() => setHienModalChot(false)}>Hủy</button>
                <button className="btn btn-success" onClick={chotDon} disabled={dangTao}>
                  {dangTao
                    ? <><span className="spinner-border spinner-border-sm me-1"></span>Đang xử lý...</>
                    : <><i className="bi bi-check2-circle me-1"></i>Xác nhận chốt đơn</>}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
