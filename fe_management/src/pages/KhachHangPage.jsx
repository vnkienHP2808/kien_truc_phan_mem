import React, { useEffect, useState, useCallback } from 'react';
import API from '../api';

const emptyForm = {
  hoTen: '', username: '', password: '',
  email: '', soDienThoai: '', diaChi: '', dob: '',
};

export default function KhachHangPage() {
  const [danhSach, setDanhSach] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loi, setLoi] = useState('');
  const [thongBao, setThongBao] = useState('');

  // Modal state
  const [hienModal, setHienModal] = useState(false);
  const [dangSua, setDangSua] = useState(null); // null = thêm mới
  const [form, setForm] = useState(emptyForm);
  const [formLoi, setFormLoi] = useState({});

  // Modal xem chi tiết
  const [chiTiet, setChiTiet] = useState(null);

  // Tìm kiếm
  const [timKiem, setTimKiem] = useState('');

  const taiDuLieu = useCallback(async () => {
    setLoading(true);
    setLoi('');
    try {
      const res = await API.get('/api/khach-hang');
      setDanhSach(res.data);
    } catch {
      setLoi('Không thể tải danh sách khách hàng. Kiểm tra kết nối API Gateway.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { taiDuLieu(); }, [taiDuLieu]);

  const moModalThem = () => {
    setDangSua(null);
    setForm(emptyForm);
    setFormLoi({});
    setHienModal(true);
  };

  const moModalSua = (kh) => {
    setDangSua(kh);
    setForm({
      hoTen: kh.hoTen || '',
      username: kh.username || '',
      password: '',
      email: kh.email || '',
      soDienThoai: kh.soDienThoai || '',
      diaChi: kh.diaChi || '',
      dob: kh.dob || '',
    });
    setFormLoi({});
    setHienModal(true);
  };

  const dongModal = () => { setHienModal(false); setDangSua(null); };

  const kiemTraForm = () => {
    const loi = {};
    if (!form.hoTen.trim()) loi.hoTen = 'Vui lòng nhập họ tên';
    if (!form.username.trim()) loi.username = 'Vui lòng nhập tên đăng nhập';
    if (!dangSua && !form.password.trim()) loi.password = 'Vui lòng nhập mật khẩu';
    if (!form.email.trim()) loi.email = 'Vui lòng nhập email';
    else if (!/\S+@\S+\.\S+/.test(form.email)) loi.email = 'Email không hợp lệ';
    if (!form.soDienThoai.trim()) loi.soDienThoai = 'Vui lòng nhập số điện thoại';
    return loi;
  };

  const luuKhachHang = async () => {
    const loi = kiemTraForm();
    if (Object.keys(loi).length > 0) { setFormLoi(loi); return; }
    try {
      if (dangSua) {
        await API.put(`/api/khach-hang/${dangSua.id}`, form);
        setThongBao('Cập nhật khách hàng thành công!');
      } else {
        await API.post('/api/khach-hang', form);
        setThongBao('Thêm khách hàng thành công!');
      }
      dongModal();
      taiDuLieu();
      setTimeout(() => setThongBao(''), 3000);
    } catch (err) {
      setFormLoi({ chung: err.response?.data?.message || 'Có lỗi xảy ra. Thử lại!' });
    }
  };

  const xoaKhachHang = async (id, ten) => {
    if (!window.confirm(`Bạn có chắc muốn xóa khách hàng "${ten}" không?`)) return;
    try {
      await API.delete(`/api/khach-hang/${id}`);
      setThongBao('Đã xóa khách hàng thành công!');
      taiDuLieu();
      setTimeout(() => setThongBao(''), 3000);
    } catch {
      setLoi('Xóa thất bại. Thử lại!');
    }
  };

  const xemChiTiet = async (id) => {
    try {
      const res = await API.get(`/api/khach-hang/${id}`);
      setChiTiet(res.data);
    } catch {
      setLoi('Không thể tải thông tin chi tiết.');
    }
  };

  const danhSachLocRa = danhSach.filter(kh =>
    kh.hoTen?.toLowerCase().includes(timKiem.toLowerCase()) ||
    kh.username?.toLowerCase().includes(timKiem.toLowerCase()) ||
    kh.email?.toLowerCase().includes(timKiem.toLowerCase()) ||
    kh.soDienThoai?.includes(timKiem)
  );

  return (
    <div>
      {/* Thanh tiêu đề */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">
            <i className="bi bi-people-fill me-2 text-primary"></i>
            Danh Sách Khách Hàng
          </h4>
          <small className="text-muted">Quản lý thông tin toàn bộ khách hàng trong hệ thống</small>
        </div>
        <button className="btn btn-primary" onClick={moModalThem}>
          <i className="bi bi-plus-lg me-1"></i> Thêm khách hàng
        </button>
      </div>

      {/* Thông báo */}
      {thongBao && (
        <div className="alert alert-success alert-dismissible d-flex align-items-center" role="alert">
          <i className="bi bi-check-circle-fill me-2"></i>
          {thongBao}
          <button type="button" className="btn-close" onClick={() => setThongBao('')}></button>
        </div>
      )}
      {loi && (
        <div className="alert alert-danger d-flex align-items-center" role="alert">
          <i className="bi bi-exclamation-triangle-fill me-2"></i>
          {loi}
        </div>
      )}

      {/* Tìm kiếm + thống kê */}
      <div className="row mb-3">
        <div className="col-md-5">
          <div className="input-group">
            <span className="input-group-text bg-white">
              <i className="bi bi-search text-muted"></i>
            </span>
            <input
              type="text"
              className="form-control"
              placeholder="Tìm theo tên, username, email, SĐT..."
              value={timKiem}
              onChange={e => setTimKiem(e.target.value)}
            />
            {timKiem && (
              <button className="btn btn-outline-secondary" onClick={() => setTimKiem('')}>
                <i className="bi bi-x"></i>
              </button>
            )}
          </div>
        </div>
        <div className="col-md-7 d-flex align-items-center justify-content-end">
          <span className="text-muted small">
            Hiển thị <strong>{danhSachLocRa.length}</strong> / {danhSach.length} khách hàng
          </span>
          <button className="btn btn-outline-secondary btn-sm ms-2" onClick={taiDuLieu}>
            <i className="bi bi-arrow-clockwise"></i>
          </button>
        </div>
      </div>

      {/* Bảng danh sách */}
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Đang tải...</span>
          </div>
          <p className="mt-2 text-muted">Đang tải dữ liệu...</p>
        </div>
      ) : (
        <div className="card border-0 shadow-sm">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th style={{width: 50}}>#</th>
                  <th>Họ tên</th>
                  <th>Tên đăng nhập</th>
                  <th>Email</th>
                  <th>Số điện thoại</th>
                  <th>Địa chỉ</th>
                  <th style={{width: 130}} className="text-center">Thao tác</th>
                </tr>
              </thead>
              <tbody>
                {danhSachLocRa.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="text-center py-5 text-muted">
                      <i className="bi bi-inbox fs-3 d-block mb-2"></i>
                      {timKiem ? 'Không tìm thấy kết quả phù hợp' : 'Chưa có khách hàng nào'}
                    </td>
                  </tr>
                ) : (
                  danhSachLocRa.map((kh, idx) => (
                    <tr key={kh.id}>
                      <td className="text-muted">{idx + 1}</td>
                      <td>
                        <div className="d-flex align-items-center gap-2">
                          <div
                            className="rounded-circle d-flex align-items-center justify-content-center text-white fw-bold"
                            style={{
                              width: 36, height: 36, fontSize: 14,
                              background: `hsl(${(kh.hoTen?.charCodeAt(0) || 0) * 15 % 360}, 55%, 50%)`
                            }}
                          >
                            {kh.hoTen?.charAt(0).toUpperCase()}
                          </div>
                          <span className="fw-medium">{kh.hoTen}</span>
                        </div>
                      </td>
                      <td><code className="text-primary">{kh.username}</code></td>
                      <td>{kh.email}</td>
                      <td>{kh.soDienThoai}</td>
                      <td>
                        <span className="text-truncate d-inline-block" style={{maxWidth: 150}}>
                          {kh.diaChi || <span className="text-muted fst-italic">Chưa có</span>}
                        </span>
                      </td>
                      <td className="text-center">
                        <button
                          className="btn btn-outline-info btn-sm me-1"
                          title="Xem chi tiết"
                          onClick={() => xemChiTiet(kh.id)}
                          data-bs-toggle="modal"
                          data-bs-target="#modalChiTiet"
                        >
                          <i className="bi bi-eye"></i>
                        </button>
                        <button
                          className="btn btn-outline-warning btn-sm me-1"
                          title="Chỉnh sửa"
                          onClick={() => moModalSua(kh)}
                        >
                          <i className="bi bi-pencil"></i>
                        </button>
                        <button
                          className="btn btn-outline-danger btn-sm"
                          title="Xóa"
                          onClick={() => xoaKhachHang(kh.id, kh.hoTen)}
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal Thêm / Sửa */}
      {hienModal && (
        <div className="modal show d-block" style={{background: 'rgba(0,0,0,0.5)'}}>
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  <i className={`bi ${dangSua ? 'bi-pencil-square' : 'bi-person-plus'} me-2`}></i>
                  {dangSua ? 'Cập nhật thông tin khách hàng' : 'Thêm khách hàng mới'}
                </h5>
                <button className="btn-close" onClick={dongModal}></button>
              </div>
              <div className="modal-body">
                {formLoi.chung && (
                  <div className="alert alert-danger py-2">
                    <i className="bi bi-exclamation-circle me-1"></i>{formLoi.chung}
                  </div>
                )}
                <div className="row g-3">
                  <div className="col-md-6">
                    <label className="form-label fw-medium">Họ tên <span className="text-danger">*</span></label>
                    <input
                      className={`form-control ${formLoi.hoTen ? 'is-invalid' : ''}`}
                      placeholder="Nguyễn Văn A"
                      value={form.hoTen}
                      onChange={e => setForm({...form, hoTen: e.target.value})}
                    />
                    {formLoi.hoTen && <div className="invalid-feedback">{formLoi.hoTen}</div>}
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-medium">Tên đăng nhập <span className="text-danger">*</span></label>
                    <input
                      className={`form-control ${formLoi.username ? 'is-invalid' : ''}`}
                      placeholder="nguyenvana"
                      value={form.username}
                      onChange={e => setForm({...form, username: e.target.value})}
                      disabled={!!dangSua}
                    />
                    {formLoi.username && <div className="invalid-feedback">{formLoi.username}</div>}
                    {dangSua && <div className="form-text">Tên đăng nhập không thể thay đổi</div>}
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-medium">
                      Mật khẩu {!dangSua && <span className="text-danger">*</span>}
                    </label>
                    <input
                      type="password"
                      className={`form-control ${formLoi.password ? 'is-invalid' : ''}`}
                      placeholder={dangSua ? 'Để trống nếu không đổi' : '••••••••'}
                      value={form.password}
                      onChange={e => setForm({...form, password: e.target.value})}
                    />
                    {formLoi.password && <div className="invalid-feedback">{formLoi.password}</div>}
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-medium">Email <span className="text-danger">*</span></label>
                    <input
                      type="email"
                      className={`form-control ${formLoi.email ? 'is-invalid' : ''}`}
                      placeholder="email@example.com"
                      value={form.email}
                      onChange={e => setForm({...form, email: e.target.value})}
                    />
                    {formLoi.email && <div className="invalid-feedback">{formLoi.email}</div>}
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-medium">Số điện thoại <span className="text-danger">*</span></label>
                    <input
                      className={`form-control ${formLoi.soDienThoai ? 'is-invalid' : ''}`}
                      placeholder="0901234567"
                      value={form.soDienThoai}
                      onChange={e => setForm({...form, soDienThoai: e.target.value})}
                    />
                    {formLoi.soDienThoai && <div className="invalid-feedback">{formLoi.soDienThoai}</div>}
                  </div>
                  <div className="col-md-6">
                    <label className="form-label fw-medium">Ngày sinh</label>
                    <input
                      type="date"
                      className="form-control"
                      value={form.dob}
                      onChange={e => setForm({...form, dob: e.target.value})}
                    />
                  </div>
                  <div className="col-12">
                    <label className="form-label fw-medium">Địa chỉ</label>
                    <input
                      className="form-control"
                      placeholder="Số nhà, đường, quận/huyện, tỉnh/thành phố"
                      value={form.diaChi}
                      onChange={e => setForm({...form, diaChi: e.target.value})}
                    />
                  </div>
                </div>
              </div>
              <div className="modal-footer">
                <button className="btn btn-secondary" onClick={dongModal}>
                  <i className="bi bi-x me-1"></i> Hủy
                </button>
                <button className="btn btn-primary" onClick={luuKhachHang}>
                  <i className={`bi ${dangSua ? 'bi-check-lg' : 'bi-plus-lg'} me-1`}></i>
                  {dangSua ? 'Lưu thay đổi' : 'Thêm mới'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Modal Chi tiết */}
      <div className="modal fade" id="modalChiTiet" tabIndex="-1">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">
                <i className="bi bi-person-badge me-2"></i>Thông tin chi tiết
              </h5>
              <button className="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div className="modal-body">
              {chiTiet && (
                <div>
                  <div className="text-center mb-3">
                    <div
                      className="rounded-circle d-inline-flex align-items-center justify-content-center text-white fw-bold mx-auto"
                      style={{
                        width: 72, height: 72, fontSize: 28,
                        background: `hsl(${(chiTiet.hoTen?.charCodeAt(0) || 0) * 15 % 360}, 55%, 50%)`
                      }}
                    >
                      {chiTiet.hoTen?.charAt(0).toUpperCase()}
                    </div>
                    <h5 className="mt-2 mb-0">{chiTiet.hoTen}</h5>
                    <small className="text-muted">{chiTiet.maKhachHang}</small>
                  </div>
                  <table className="table table-sm">
                    <tbody>
                      {[
                        ['bi-person', 'Tên đăng nhập', chiTiet.username],
                        ['bi-envelope', 'Email', chiTiet.email],
                        ['bi-phone', 'Số điện thoại', chiTiet.soDienThoai],
                        ['bi-calendar', 'Ngày sinh', chiTiet.dob],
                        ['bi-geo-alt', 'Địa chỉ', chiTiet.diaChi],
                      ].map(([icon, label, value]) => (
                        <tr key={label}>
                          <td className="text-muted" style={{width: 130}}>
                            <i className={`bi ${icon} me-1`}></i>{label}
                          </td>
                          <td>{value || <span className="text-muted fst-italic">Chưa cập nhật</span>}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary btn-sm" data-bs-dismiss="modal">Đóng</button>
              <button
                className="btn btn-warning btn-sm"
                data-bs-dismiss="modal"
                onClick={() => chiTiet && moModalSua(chiTiet)}
              >
                <i className="bi bi-pencil me-1"></i>Chỉnh sửa
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
