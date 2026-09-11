package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.MonAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    public MonAnDAO() {
    }

    public List<MonAn> findAll() {
        return layTatCa();
    }

    public List<MonAn> layTatCa() {
        List<MonAn> danhSach = new ArrayList<>();
        String sql = "SELECT m.ma_mon, m.ma_mon_an, m.ma_danh_muc, m.ten_mon, m.mo_ta, "
                   + "       m.don_vi_tinh, m.gia_ban, m.gia_von, m.duong_dan_anh, m.luot_ban, "
                   + "       m.trang_thai, d.ten_danh_muc "
                   + "FROM mon_an m "
                   + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                   + "ORDER BY m.ma_mon ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonAn mon = docMonAn(rs);
                danhSach.add(mon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<MonAn> layMonDangBan() {
        List<MonAn> danhSach = new ArrayList<>();
        String sql = "SELECT m.ma_mon, m.ma_mon_an, m.ma_danh_muc, m.ten_mon, m.mo_ta, "
                   + "       m.don_vi_tinh, m.gia_ban, m.gia_von, m.duong_dan_anh, m.luot_ban, "
                   + "       m.trang_thai, d.ten_danh_muc "
                   + "FROM mon_an m "
                   + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                   + "WHERE m.trang_thai = 1 "
                   + "ORDER BY m.ma_danh_muc, m.ma_mon ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonAn mon = docMonAn(rs);
                danhSach.add(mon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public MonAn timTheoMa(int maMon) {
        String sql = "SELECT m.ma_mon, m.ma_mon_an, m.ma_danh_muc, m.ten_mon, m.mo_ta, "
                   + "       m.don_vi_tinh, m.gia_ban, m.gia_von, m.duong_dan_anh, m.luot_ban, "
                   + "       m.trang_thai, d.ten_danh_muc "
                   + "FROM mon_an m "
                   + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                   + "WHERE m.ma_mon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maMon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return docMonAn(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MonAn> timTheoTen(String tuKhoa) {
        List<MonAn> danhSach = new ArrayList<>();
        String sql = "SELECT m.ma_mon, m.ma_mon_an, m.ma_danh_muc, m.ten_mon, m.mo_ta, "
                   + "       m.don_vi_tinh, m.gia_ban, m.gia_von, m.duong_dan_anh, m.luot_ban, "
                   + "       m.trang_thai, d.ten_danh_muc "
                   + "FROM mon_an m "
                   + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                   + "WHERE LOWER(m.ten_mon) LIKE ? "
                   + "ORDER BY m.ma_mon ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa.toLowerCase().trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSach.add(docMonAn(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean insert(MonAn mon) {
        String sql = "INSERT INTO mon_an (ma_mon_an, ma_danh_muc, ten_mon, mo_ta, don_vi_tinh, "
                   + "                   gia_ban, gia_von, duong_dan_anh, luot_ban, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String code = mon.getMaMonAn();
            if (code == null || code.trim().isEmpty()) {
                code = "MON" + (System.currentTimeMillis() % 10000);
            }
            ps.setString(1, code);
            ps.setInt(2, mon.getMaDanhMuc() > 0 ? mon.getMaDanhMuc() : 1);
            ps.setString(3, mon.getTenMon());
            ps.setString(4, mon.getMoTa());
            ps.setString(5, mon.getDonVi() != null ? mon.getDonVi() : "Phần");
            ps.setDouble(6, mon.getGia());
            ps.setDouble(7, mon.getGiaVon() > 0 ? mon.getGiaVon() : mon.getGia() * 0.5);
            ps.setString(8, mon.getDuongDanAnh());
            ps.setInt(9, mon.getLuotBan());
            ps.setBoolean(10, mon.isDangBan());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        mon.setMaMon(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(MonAn mon) {
        String sql = "UPDATE mon_an SET ma_danh_muc = ?, ten_mon = ?, mo_ta = ?, "
                   + "                don_vi_tinh = ?, gia_ban = ?, gia_von = ?, "
                   + "                duong_dan_anh = ?, trang_thai = ? "
                   + "WHERE ma_mon = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mon.getMaDanhMuc() > 0 ? mon.getMaDanhMuc() : 1);
            ps.setString(2, mon.getTenMon());
            ps.setString(3, mon.getMoTa());
            ps.setString(4, mon.getDonVi());
            ps.setDouble(5, mon.getGia());
            ps.setDouble(6, mon.getGiaVon());
            ps.setString(7, mon.getDuongDanAnh());
            ps.setBoolean(8, mon.isDangBan());
            ps.setInt(9, mon.getMaMon());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maMon) {
        // Kiểm tra xem món có trong đơn hàng đang phục vụ không
        String checkSql = "SELECT COUNT(*) FROM chi_tiet_don_hang c "
                        + "JOIN don_hang d ON d.ma_don_hang = c.ma_don_hang "
                        + "WHERE c.ma_mon = ? AND d.trang_thai = 'DANG_PHUC_VU'";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setInt(1, maMon);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Đang có trong đơn phục vụ
                    }
                }
            }

            // Nếu món đã từng có trong lịch sử đơn hàng cũ, ẩn món (trang_thai = 0) để giữ toàn vẹn dữ liệu
            String countOrderSql = "SELECT COUNT(*) FROM chi_tiet_don_hang WHERE ma_mon = ?";
            try (PreparedStatement psCount = conn.prepareStatement(countOrderSql)) {
                psCount.setInt(1, maMon);
                try (ResultSet rs = psCount.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return capNhatTrangThai(maMon, false);
                    }
                }
            }

            String deleteSql = "DELETE FROM mon_an WHERE ma_mon = ?";
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setInt(1, maMon);
                return psDel.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTrangThai(int maMon, boolean dangBan) {
        String sql = "UPDATE mon_an SET trang_thai = ? WHERE ma_mon = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, dangBan);
            ps.setInt(2, maMon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> layDanhSachDanhMuc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT ten_danh_muc FROM danh_muc_mon ORDER BY thu_tu ASC, ma_danh_muc ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private MonAn docMonAn(ResultSet rs) throws Exception {
        MonAn mon = new MonAn();
        mon.setMaMon(rs.getInt("ma_mon"));
        mon.setMaMonAn(rs.getString("ma_mon_an"));
        mon.setMaDanhMuc(rs.getInt("ma_danh_muc"));
        mon.setTenMon(rs.getString("ten_mon"));
        mon.setMoTa(rs.getString("mo_ta"));
        mon.setDonVi(rs.getString("don_vi_tinh"));
        mon.setGia(rs.getDouble("gia_ban"));
        mon.setGiaVon(rs.getDouble("gia_von"));
        mon.setDuongDanAnh(rs.getString("duong_dan_anh"));
        mon.setLuotBan(rs.getInt("luot_ban"));
        mon.setDangBan(rs.getBoolean("trang_thai"));

        String nhom = rs.getString("ten_danh_muc");
        mon.setDanhMuc(nhom != null ? nhom : "Khác");
        return mon;
    }
}