package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.customer.CustomerCartFrame.MonGioHang;
import com.restaurant.model.DonHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {

    public int timMaBanTheoSo(String maBanSo) {
        if (maBanSo == null || maBanSo.trim().isEmpty()) return -1;
        String sql = "SELECT ma_ban FROM ban_an WHERE ma_ban_so = ? OR ten_ban = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBanSo.trim());
            ps.setString(2, maBanSo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("ma_ban");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public DonHang layDonHangDangPhucVuCuaBan(int maBan) {
        String sql = "SELECT d.*, b.ten_ban, k.ho_ten AS ten_khach, u.ho_ten AS ten_nv "
                   + "FROM don_hang d "
                   + "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban "
                   + "LEFT JOIN khach_hang k ON k.ma_khach_hang = d.ma_khach_hang "
                   + "LEFT JOIN nguoi_dung u ON u.ma_nguoi_dung = d.ma_nguoi_dung "
                   + "WHERE d.ma_ban = ? AND d.trang_thai = 'DANG_PHUC_VU' "
                   + "ORDER BY d.ma_don_hang DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return docDonHang(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DonHang timTheoMa(int maDonHang) {
        String sql = "SELECT d.*, b.ten_ban, k.ho_ten AS ten_khach, u.ho_ten AS ten_nv "
                   + "FROM don_hang d "
                   + "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban "
                   + "LEFT JOIN khach_hang k ON k.ma_khach_hang = d.ma_khach_hang "
                   + "LEFT JOIN nguoi_dung u ON u.ma_nguoi_dung = d.ma_nguoi_dung "
                   + "WHERE d.ma_don_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return docDonHang(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean taoDonHang(DonHang donHang) {
        String sql = "INSERT INTO don_hang (ma_don, ma_ban, ma_khach_hang, ma_nguoi_dung, ma_khuyen_mai, "
                   + "trang_thai, tien_tam_tinh, tien_giam, tien_thue, tong_tien, ghi_chu) "
                   + "VALUES (?, ?, ?, ?, ?, 'DANG_PHUC_VU', ?, ?, ?, ?, ?)";
        String updateTableSql = "UPDATE ban_an SET trang_thai = 'DANG_PHUC_VU' WHERE ma_ban = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                ganMaDonNeuThieu(donHang);
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    setThongTinDonHang(ps, donHang, "DANG_PHUC_VU");
                    if (ps.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) donHang.setMaDonHang(keys.getInt(1));
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(updateTableSql)) {
                    ps.setInt(1, donHang.getMaBan());
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean taoDonHangVaChiTiet(DonHang donHang, List<MonGioHang> gioHang) {
        if (donHang == null || gioHang == null || gioHang.isEmpty()) return false;
        String sqlDon = "INSERT INTO don_hang (ma_don, ma_ban, ma_khach_hang, ma_nguoi_dung, ma_khuyen_mai, "
                      + "trang_thai, tien_tam_tinh, tien_giam, tien_thue, tong_tien, ghi_chu) "
                      + "VALUES (?, ?, ?, ?, ?, 'CHO_XU_LY', ?, ?, ?, ?, ?)";
        String sqlChiTiet = "INSERT INTO chi_tiet_don_hang (ma_don_hang, ma_mon, so_luong, don_gia, ghi_chu, trang_thai_mon) "
                          + "VALUES (?, ?, ?, ?, ?, 'CHO_CUNG_UNG')";
        String sqlBan = "UPDATE ban_an SET trang_thai = 'DANG_PHUC_VU' WHERE ma_ban = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                ganMaDonNeuThieu(donHang);
                try (PreparedStatement psDon = conn.prepareStatement(sqlDon, Statement.RETURN_GENERATED_KEYS)) {
                    setThongTinDonHang(psDon, donHang, "CHO_XU_LY");
                    if (psDon.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                    try (ResultSet keys = psDon.getGeneratedKeys()) {
                        if (keys.next()) donHang.setMaDonHang(keys.getInt(1));
                    }
                }
                if (donHang.getMaDonHang() <= 0) {
                    conn.rollback();
                    return false;
                }
                try (PreparedStatement psCt = conn.prepareStatement(sqlChiTiet)) {
                    for (MonGioHang mon : gioHang) {
                        psCt.setInt(1, donHang.getMaDonHang());
                        psCt.setInt(2, mon.getMaMon());
                        psCt.setInt(3, mon.getSoLuong());
                        psCt.setDouble(4, mon.getGia());
                        psCt.setString(5, "");
                        psCt.addBatch();
                    }
                    psCt.executeBatch();
                }
                try (PreparedStatement psBan = conn.prepareStatement(sqlBan)) {
                    psBan.setInt(1, donHang.getMaBan());
                    psBan.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DonHang> layDonHangChoXuLy() {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT d.*, b.ten_ban, k.ho_ten AS ten_khach, u.ho_ten AS ten_nv "
                   + "FROM don_hang d "
                   + "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban "
                   + "LEFT JOIN khach_hang k ON k.ma_khach_hang = d.ma_khach_hang "
                   + "LEFT JOIN nguoi_dung u ON u.ma_nguoi_dung = d.ma_nguoi_dung "
                   + "WHERE d.trang_thai = 'CHO_XU_LY' "
                   + "ORDER BY d.ngay_tao ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(docDonHang(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean tiepNhanDonHang(int maDonHang) {
        String sql = "UPDATE don_hang SET trang_thai = 'DANG_PHUC_VU' WHERE ma_don_hang = ? AND trang_thai = 'CHO_XU_LY'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTienDonHang(int maDonHang, double tamTinh, double tienGiam, double tienThue, double tongTien) {
        String sql = "UPDATE don_hang SET tien_tam_tinh = ?, tien_giam = ?, tien_thue = ?, tong_tien = ? WHERE ma_don_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tamTinh);
            ps.setDouble(2, tienGiam);
            ps.setDouble(3, tienThue);
            ps.setDouble(4, tongTien);
            ps.setInt(5, maDonHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean apDungKhuyenMai(int maDonHang, int maKhuyenMai, double tienGiam, double tongTien) {
        String sql = "UPDATE don_hang SET ma_khuyen_mai = ?, tien_giam = ?, tong_tien = ? WHERE ma_don_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (maKhuyenMai > 0) ps.setInt(1, maKhuyenMai); else ps.setNull(1, Types.INTEGER);
            ps.setDouble(2, tienGiam);
            ps.setDouble(3, tongTien);
            ps.setInt(4, maDonHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hoanThanhDonHang(int maDonHang, int maBan) {
        String sqlDon = "UPDATE don_hang SET trang_thai = 'HOAN_THANH' WHERE ma_don_hang = ?";
        String sqlBan = "UPDATE ban_an SET trang_thai = 'TRONG' WHERE ma_ban = ?";
        return capNhatTrangThaiDonVaBan(maDonHang, maBan, sqlDon, sqlBan);
    }

    public boolean huyDonHang(int maDonHang, int maBan) {
        String sqlDon = "UPDATE don_hang SET trang_thai = 'DA_HUY' WHERE ma_don_hang = ?";
        String sqlBan = "UPDATE ban_an SET trang_thai = 'TRONG' WHERE ma_ban = ?";
        return capNhatTrangThaiDonVaBan(maDonHang, maBan, sqlDon, sqlBan);
    }

    public List<DonHang> layDanhSachDonHang(int limit) {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT d.*, b.ten_ban, k.ho_ten AS ten_khach, u.ho_ten AS ten_nv "
                   + "FROM don_hang d "
                   + "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban "
                   + "LEFT JOIN khach_hang k ON k.ma_khach_hang = d.ma_khach_hang "
                   + "LEFT JOIN nguoi_dung u ON u.ma_nguoi_dung = d.ma_nguoi_dung "
                   + "ORDER BY d.ma_don_hang DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 100);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docDonHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private boolean capNhatTrangThaiDonVaBan(int maDonHang, int maBan, String sqlDon, String sqlBan) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement psDon = conn.prepareStatement(sqlDon)) {
                    psDon.setInt(1, maDonHang);
                    psDon.executeUpdate();
                }
                if (maBan > 0) {
                    try (PreparedStatement psBan = conn.prepareStatement(sqlBan)) {
                        psBan.setInt(1, maBan);
                        psBan.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void ganMaDonNeuThieu(DonHang donHang) {
        String maDon = donHang.getMaDon();
        if (maDon == null || maDon.trim().isEmpty()) {
            maDon = "DH" + System.currentTimeMillis();
            donHang.setMaDon(maDon);
        }
    }

    private void setThongTinDonHang(PreparedStatement ps, DonHang donHang, String trangThai) throws Exception {
        ps.setString(1, donHang.getMaDon());
        ps.setInt(2, donHang.getMaBan());
        if (donHang.getMaKhachHang() != null && donHang.getMaKhachHang() > 0) ps.setInt(3, donHang.getMaKhachHang()); else ps.setNull(3, Types.INTEGER);
        if (donHang.getMaNguoiDung() != null && donHang.getMaNguoiDung() > 0) ps.setInt(4, donHang.getMaNguoiDung()); else ps.setNull(4, Types.INTEGER);
        if (donHang.getMaKhuyenMai() != null && donHang.getMaKhuyenMai() > 0) ps.setInt(5, donHang.getMaKhuyenMai()); else ps.setNull(5, Types.INTEGER);
        ps.setDouble(6, donHang.getTienTamTinh());
        ps.setDouble(7, donHang.getTienGiam());
        ps.setDouble(8, donHang.getTienThue());
        ps.setDouble(9, donHang.getTongTien());
        ps.setString(10, donHang.getGhiChu());
    }

    private DonHang docDonHang(ResultSet rs) throws Exception {
        DonHang d = new DonHang();
        d.setMaDonHang(rs.getInt("ma_don_hang"));
        d.setMaDon(rs.getString("ma_don"));
        d.setMaBan(rs.getInt("ma_ban"));
        d.setMaKhachHang((Integer) rs.getObject("ma_khach_hang"));
        d.setMaNguoiDung((Integer) rs.getObject("ma_nguoi_dung"));
        d.setMaKhuyenMai((Integer) rs.getObject("ma_khuyen_mai"));
        d.setTrangThai(rs.getString("trang_thai"));
        d.setTienTamTinh(rs.getDouble("tien_tam_tinh"));
        d.setTienGiam(rs.getDouble("tien_giam"));
        d.setTienThue(rs.getDouble("tien_thue"));
        d.setTongTien(rs.getDouble("tong_tien"));
        d.setGhiChu(rs.getString("ghi_chu"));
        d.setNgayTao(rs.getTimestamp("ngay_tao"));
        try { d.setTenBan(rs.getString("ten_ban")); } catch (Exception ignored) {}
        try { d.setTenKhachHang(rs.getString("ten_khach")); } catch (Exception ignored) {}
        try { d.setTenNhanVien(rs.getString("ten_nv")); } catch (Exception ignored) {}
        return d;
    }
}
