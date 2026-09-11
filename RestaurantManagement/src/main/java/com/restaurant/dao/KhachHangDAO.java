package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> findAll() {
        List<KhachHang> danhSach = new ArrayList<>();

        String sql = "SELECT * FROM khach_hang ORDER BY ma_khach_hang DESC";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();

                kh.setMaKhachHang(rs.getInt("ma_khach_hang"));
                kh.setHoTen(rs.getString("ho_ten"));
                kh.setSoDienThoai(rs.getString("so_dien_thoai"));
                kh.setEmail(rs.getString("email"));
                kh.setDiaChi(rs.getString("dia_chi"));
                kh.setNgayTao(rs.getTimestamp("ngay_tao"));
                kh.setGhiChu(rs.getString("ghi_chu"));

                danhSach.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO khach_hang " +
                "(ho_ten, so_dien_thoai, email, dia_chi, ghi_chu) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE khach_hang SET " +
                "ho_ten=?, so_dien_thoai=?, email=?, dia_chi=?, ghi_chu=? " +
                "WHERE ma_khach_hang=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getGhiChu());
            ps.setInt(6, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maKhachHang) {
        String sql = "DELETE FROM khach_hang WHERE ma_khach_hang=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maKhachHang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}