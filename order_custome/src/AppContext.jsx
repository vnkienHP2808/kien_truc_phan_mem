import React, { createContext, useContext, useState } from 'react';

const AppContext = createContext(null);

export function AppProvider({ children }) {
  const [khachHangId, setKhachHangId] = useState(null);
  const [trangHienTai, setTrangHienTai] = useState('trang-phuc'); // 'trang-phuc' | 'gio-hang' | 'phieu-thue'

  return (
    <AppContext.Provider value={{ khachHangId, setKhachHangId, trangHienTai, setTrangHienTai }}>
      {children}
    </AppContext.Provider>
  );
}

export const useApp = () => useContext(AppContext);
