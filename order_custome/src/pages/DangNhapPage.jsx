import React, { useState } from 'react';
import { useApp } from '../AppContext';
import API from '../api';

export default function DangNhapPage() {
  const { setKhachHangId } = useApp();
  const [id, setId] = useState('');
  const [loi, setLoi] = useState('');
  const [loading, setLoading] = useState(false);

  const dangNhap = async () => {
    if (!id.trim() || isNaN(Number(id))) {
      setLoi('Vui lòng nhập mã khách hàng hợp lệ (số nguyên)');
      return;
    }
    setLoading(true);
    setLoi('');
    try {
      await API.get(`/api/khach-hang/${id}`);
      setKhachHangId(Number(id));
    } catch {
      setLoi('Không tìm thấy khách hàng với mã này. Vui lòng kiểm tra lại.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 d-flex align-items-center justify-content-center" style={{background: '#f0f4ff'}}>
      <div className="card border-0 shadow" style={{width: 420}}>
        <div className="card-body p-5">
          <div className="text-center mb-4">
            <div className="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
              style={{width: 72, height: 72}}>
              <i className="bi bi-person-circle text-primary" style={{fontSize: 36}}></i>
            </div>
            <h4 className="fw-bold">Chào mừng!</h4>
            <p className="text-muted small">Nhập mã khách hàng để bắt đầu đặt trang phục</p>
          </div>

          {loi && (
            <div className="alert alert-danger py-2 small">
              <i className="bi bi-exclamation-circle me-1"></i>{loi}
            </div>
          )}

          <div className="mb-3">
            <label className="form-label fw-medium">Mã khách hàng (ID)</label>
            <input
              type="number"
              className="form-control form-control-lg"
              placeholder="Ví dụ: 1, 2, 3..."
              value={id}
              onChange={e => setId(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && dangNhap()}
              autoFocus
            />
            <div className="form-text">
              <i className="bi bi-info-circle me-1"></i>
              Mã này là <code>id</code> của khách hàng trong hệ thống
            </div>
          </div>

          <button
            className="btn btn-primary w-100 btn-lg"
            onClick={dangNhap}
            disabled={loading}
          >
            {loading ? (
              <><span className="spinner-border spinner-border-sm me-2"></span>Đang kiểm tra...</>
            ) : (
              <><i className="bi bi-box-arrow-in-right me-2"></i>Tiếp tục</>
            )}
          </button>
        </div>
      </div>
    </div>
  );
}
