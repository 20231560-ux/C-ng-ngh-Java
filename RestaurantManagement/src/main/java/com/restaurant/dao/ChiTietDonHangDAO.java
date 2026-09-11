package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.ChiTietDonHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangDAO {

    public List<ChiTietDonHang> layChiTietTheoDonHang(int maDonHang) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT c.*, m.ten_mon, m.don_vi_tinh, d.ten_danh_muc "
                   + "FROM chi_tiet_don_hang c "
                   + "JOIN mon_an m ON m.ma_mon = c.ma_mon "
                   + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                   + "WHERE c.ma_don_hang = ? "
                   + "ORDER BY c.ma_chi_tiet ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang ct = new ChiTietDonHang();
                    ct.setMaChiTiet(rs.getInt("ma_chi_tiet"));
                    ct.setMaDonHang(rs.getInt("ma_don_hang"));
                    ct.setMaMon(rs.getInt("ma_mon"));
                    ct.setSoLuong(rs.getInt("so_luong"));
                    ct.setDonGia(rs.getDouble("don_gia"));
                    ct.setGhiChu(rs.getString("ghi_chu"));
                    ct.setTrangThaiMon(rs.getString("trang_thai_mon"));
                    ct.setTenMon(rs.getString("ten_mon"));
                    ct.setDonViTinh(rs.getString("don_vi_tinh"));
                    ct.setDanhMuc(rs.getString("ten_danh_muc"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themHoacTangMon(int maDonHang, int maMon, int soLuongThem, double donGia, String ghiChu) {
        String checkSql = "SELECT ma_chi_tiet, so_luong FROM chi_tiet_don_hang WHERE ma_don_hang = ? AND ma_mon = ?";
        String updateSql = "UPDATE chi_tiet_don_hang SET so_luong = so_luong + ?, don_gia = ? WHERE ma_chi_tiet = ?";
        String insertSql = "INSERT INTO chi_tiet_don_hang (ma_don_hang, ma_mon, so_luong, don_gia, ghi_chu, trang_thai_mon) "
                         + "VALUES (?, ?, ?, ?, ?, 'CHO_CUNG_UNG')";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int maChiTietDaCo = -1;
                try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                    psCheck.setInt(1, maDonHang);
                    psCheck.setInt(2, maMon);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            maChiTietDaCo = rs.getInt(1);
                        }
                    }
                }

                if (maChiTietDaCo > 0) {
                    try (PreparedStatement psUp = conn.prepareStatement(updateSql)) {
                        psUp.setInt(1, soLuongThem);
                        psUp.setDouble(2, donGia);
                        psUp.setInt(3, maChiTietDaCo);
                        psUp.executeUpdate();
                    }
                } else {
                    try (PreparedStatement psIn = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        psIn.setInt(1, maDonHang);
                        psIn.setInt(2, maMon);
                        psIn.setInt(3, soLuongThem);
                        psIn.setDouble(4, donGia);
                        psIn.setString(5, ghiChu);
                        psIn.executeUpdate();
                    }
                }

                dongBoTongTienDonHang(conn, maDonHang);

                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoLuong(int maChiTiet, int maDonHang, int soLuongMoi) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (soLuongMoi <= 0) {
                    String delSql = "DELETE FROM chi_tiet_don_hang WHERE ma_chi_tiet = ?";
                    try (PreparedStatement ps = conn.prepareStatement(delSql)) {
                        ps.setInt(1, maChiTiet);
                        ps.executeUpdate();
                    }
                } else {
                    String upSql = "UPDATE chi_tiet_don_hang SET so_luong = ? WHERE ma_chi_tiet = ?";
                    try (PreparedStatement ps = conn.prepareStatement(upSql)) {
                        ps.setInt(1, soLuongMoi);
                        ps.setInt(2, maChiTiet);
                        ps.executeUpdate();
                    }
                }

                dongBoTongTienDonHang(conn, maDonHang);

                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaMonKhoiDon(int maChiTiet, int maDonHang) {
        return capNhatSoLuong(maChiTiet, maDonHang, 0);
    }

    public boolean xoaToanBoMon(int maDonHang) {
        String delSql = "DELETE FROM chi_tiet_don_hang WHERE ma_don_hang = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(delSql)) {
                    ps.setInt(1, maDonHang);
                    ps.executeUpdate();
                }
                dongBoTongTienDonHang(conn, maDonHang);
                conn.commit();
                return true;
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void dongBoTongTienDonHang(Connection conn, int maDonHang) throws Exception {
        String calcSql = "SELECT COALESCE(SUM(so_luong * don_gia), 0) FROM chi_tiet_don_hang WHERE ma_don_hang = ?";
        double tamTinh = 0;
        try (PreparedStatement ps = conn.prepareStatement(calcSql)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tamTinh = rs.getDouble(1);
            }
        }

        // Lấy thông tin tiền giảm hiện tại nếu có
        String getDiscount = "SELECT tien_giam, tien_thue FROM don_hang WHERE ma_don_hang = ?";
        double tienGiam = 0;
        double tienThue = 0;
        try (PreparedStatement ps = conn.prepareStatement(getDiscount)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tienGiam = rs.getDouble(1);
                    tienThue = rs.getDouble(2);
                }
            }
        }

        double tongTien = Math.max(0, tamTinh - tienGiam + tienThue);
        String updateOrder = "UPDATE don_hang SET tien_tam_tinh = ?, tong_tien = ? WHERE ma_don_hang = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateOrder)) {
            ps.setDouble(1, tamTinh);
            ps.setDouble(2, tongTien);
            ps.setInt(3, maDonHang);
            ps.executeUpdate();
        }
    }
}
