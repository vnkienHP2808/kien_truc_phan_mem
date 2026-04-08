import React, { useEffect, useState, useCallback } from 'react';
import API from '../api';
import { useApp } from '../AppContext';

const TRANG_THAI_PHIEU = {
  CHO_XU_LY: { cls: 'warning', label: 'Chờ xử lý' },
  CHO_XAC_NHAN: { cls: 'info', label: 'Chờ xác nhận' },
  DA_XAC_NHAN: { cls: 'success', label: 'Đã xác nhận' },
  DA_HUY: { cls: 'danger', label: 'Đã hủy' },
};

const TRANG_THAI_COC = {
  CHUA_THANH_TOAN: { cls: 'warning', label: 'Chưa thanh toán', icon: 'bi-clock' },
  DA_THANH_TOAN: { cls: 'success', label: 'Đã thanh toán', icon: 'bi-check-circle' },
  DA_HOAN_TRA: { cls: 'secondary', label: 'Đã hoàn trả', icon: 'bi-arrow-return-left' },
};

export default function PhieuThuePage() {
  const { khachHangId } = useApp();
  const [danhSach, setDanhSach] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loi, setLoi] = useState('');
  const [thongBao, setThongBao] = useState('');
  const [chiTiet, setChiTiet] = useState(null);
  const [loadingChiTiet, setLoadingChiTiet] = useState(false);
  const [dangThanhToan, setDangThanhToan] = useState(null);

  const taiDuLieu = useCallback(async () => {
    setLoading(true);
    setLoi('');
    try {
      const res = await API.get(`/api/phieu-thue/khach-hang/${khachHangId}`);
      setDanhSach(Array.isArray(res.data) ? res.data : []);
    } catch {
      setLoi('Không thể tải lịch sử phiếu thuê.');
    } finally {
      setLoading(false);
    }
  }, [khachHangId]);

  useEffect(() => { taiDuLieu(); }, [taiDuLieu]);

  const xemChiTiet = async (maPhieu) => {
    setLoadingChiTiet(true);
    setChiTiet(null);
    try {
      const res = await API.get(`/api/phieu-thue/${maPhieu}`);
      setChiTiet(res.data);
    } catch {
      setLoi('Không thể tải chi tiết phiếu thuê.');
    } finally {
      setLoadingChiTiet(false);
    }
  };

  const thanhToanCoc = async (maPhieu) => {
    if (!window.confirm('Xác nhận thanh toán đặt cọc cho phiếu này?')) return;
    setDangThanhToan(maPhieu);
    try {
      await API.patch(`/api/phieu-thue/${maPhieu}/dat-coc`);
      setThongBao(`Thanh toán đặt cọc phiếu ${maPhieu} thành công!`);
      taiDuLieu();
      if (chiTiet?.maPhieu === maPhieu) xemChiTiet(maPhieu);
      setTimeout(() => setThongBao(''), 4000);
    } catch {
      setLoi('Thanh toán thất bại. Thử lại!');
    } finally {
      setDangThanhToan(null);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 className="mb-0 fw-bold">
            <i className="bi bi-file-text me-2 text-primary"></i>
            Lịch Sử Phiếu Thuê
          </h4>
          <small className="text-muted">Theo dõi và thanh toán các phiếu thuê của bạn</small>
        </div>
        <button className="btn btn-outline-secondary btn-sm" onClick={taiDuLieu}>
          <i className="bi bi-arrow-clockwise me-1"></i>Làm mới
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
          <p className="mt-2 text-muted">Đang tải...</p>
        </div>
      ) : danhSach.length === 0 ? (
        <div className="text-center py-5 text-muted">
          <i className="bi bi-file-x" style={{fontSize: 48}}></i>
          <p className="mt-3">Bạn chưa có phiếu thuê nào</p>
        </div>
      ) : (
        <div className="row g-4">
          {/* Danh sách phiếu */}
          <div className={chiTiet ? 'col-md-6' : 'col-12'}>
            <div className="card border-0 shadow-sm">
              <div className="table-responsive">
                <table className="table table-hover align-middle mb-0">
                  <thead className="table-light">
                    <tr>
                      <th>Mã phiếu</th>
                      <th>Ngày tạo</th>
                      <th>Ngày lấy</th>
                      <th>Ngày trả</th>
                      <th>Tiền cọc</th>
                      <th>Trạng thái</th>
                      <th>Đặt cọc</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {danhSach.map(pt => {
                      const trangThai = TRANG_THAI_PHIEU[pt.trangThai] || { cls: 'secondary', label: pt.trangThai };
                      const trangThaiCoc = TRANG_THAI_COC[pt.trangThaiDatCoc] || { cls: 'secondary', label: pt.trangThaiDatCoc, icon: 'bi-dash' };
                      const coCoc = pt.trangThaiDatCoc === 'CHUA_THANH_TOAN';
                      return (
                        <tr key={pt.id} className={chiTiet?.maPhieu === pt.maPhieu ? 'table-active' : ''}>
                          <td><code className="text-primary small">{pt.maPhieu}</code></td>
                          <td className="small">{pt.ngayTao}</td>
                          <td className="small">{pt.ngayHenLay}</td>
                          <td className="small">{pt.ngayHenTra}</td>
                          <td className="fw-semibold text-warning small">
                            {pt.tienDatCoc?.toLocaleString('vi-VN')}đ
                          </td>
                          <td>
                            <span className={`badge bg-${trangThai.cls}`}>{trangThai.label}</span>
                          </td>
                          <td>
                            <span className={`badge bg-${trangThaiCoc.cls}`}>
                              <i className={`bi ${trangThaiCoc.icon} me-1`}></i>
                              {trangThaiCoc.label}
                            </span>
                          </td>
                          <td>
                            <div className="d-flex gap-1">
                              <button
                                className="btn btn-outline-primary btn-sm"
                                title="Xem chi tiết"
                                onClick={() => xemChiTiet(pt.maPhieu)}
                                data-bs-toggle={!chiTiet ? undefined : undefined}
                              >
                                <i className="bi bi-eye"></i>
                              </button>
                              {coCoc && (
                                <button
                                  className="btn btn-success btn-sm"
                                  title="Thanh toán cọc"
                                  disabled={dangThanhToan === pt.maPhieu}
                                  onClick={() => thanhToanCoc(pt.maPhieu)}
                                >
                                  {dangThanhToan === pt.maPhieu
                                    ? <span className="spinner-border spinner-border-sm"></span>
                                    : <i className="bi bi-credit-card"></i>}
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Chi tiết phiếu */}
          {(chiTiet || loadingChiTiet) && (
            <div className="col-md-6">
              <div className="card border-0 shadow-sm h-100">
                <div className="card-header d-flex justify-content-between align-items-center">
                  <span className="fw-semibold">
                    <i className="bi bi-file-earmark-text me-2"></i>
                    Chi tiết phiếu
                  </span>
                  <button className="btn-close btn-sm" onClick={() => setChiTiet(null)}></button>
                </div>
                <div className="card-body">
                  {loadingChiTiet ? (
                    <div className="text-center py-4">
                      <div className="spinner-border spinner-border-sm text-primary"></div>
                    </div>
                  ) : chiTiet && (
                    <>
                      <div className="mb-3">
                        <div className="row g-2 text-sm">
                          {[
                            ['Mã phiếu', chiTiet.maPhieu],
                            ['Hình thức', chiTiet.hinhThuc],
                            ['Ngày lấy', chiTiet.ngayHenLay],
                            ['Ngày trả', chiTiet.ngayHenTra],
                            ['Tiền cọc', `${chiTiet.tienDatCoc?.toLocaleString('vi-VN')}đ`],
                          ].map(([k, v]) => (
                            <div key={k} className="col-6">
                              <small className="text-muted d-block">{k}</small>
                              <span className="fw-medium">{v}</span>
                            </div>
                          ))}
                        </div>
                      </div>
                      <hr />
                      <h6 className="fw-semibold mb-2">
                        <i className="bi bi-list-ul me-1"></i>Danh sách trang phục
                      </h6>
                      {chiTiet.chiTietPhieuThues?.length > 0 ? (
                        <div className="list-group list-group-flush">
                          {chiTiet.chiTietPhieuThues.map((ct, idx) => (
                            <div key={idx} className="list-group-item px-0 py-2">
                              <div className="d-flex justify-content-between">
                                <span className="small">{ct.tenTrangPhuc || `Trang phục #${ct.trangPhucId}`}</span>
                                <span className="small fw-semibold text-primary">
                                  {ct.donGia?.toLocaleString('vi-VN')}đ
                                </span>
                              </div>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <p className="text-muted small">Không có dữ liệu chi tiết</p>
                      )}

                      {chiTiet.trangThaiDatCoc === 'CHUA_THANH_TOAN' && (
                        <button
                          className="btn btn-success w-100 mt-3"
                          disabled={dangThanhToan === chiTiet.maPhieu}
                          onClick={() => thanhToanCoc(chiTiet.maPhieu)}
                        >
                          {dangThanhToan === chiTiet.maPhieu
                            ? <><span className="spinner-border spinner-border-sm me-1"></span>Đang xử lý...</>
                            : <><i className="bi bi-credit-card me-2"></i>Thanh toán đặt cọc {chiTiet.tienDatCoc?.toLocaleString('vi-VN')}đ</>}
                        </button>
                      )}
                    </>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
