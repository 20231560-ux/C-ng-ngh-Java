package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    public List<KhuyenMai> findAll() {
        List<KhuyenMai> danhSach = new ArrayList<>();

        String sql = "SELECT * FROM khuyen_mai ORDER BY ma_khuyen_mai DESC";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();

                km.setMaKhuyenMai(rs.getInt("ma_khuyen_mai"));
                km.setMaCode(rs.getString("ma_code"));
                km.setTenKhuyenMai(rs.getString("ten_khuyen_mai"));
                km.setLoaiGiam(rs.getString("loai_giam"));
                km.setGiaTriGiam(rs.getDouble("gia_tri_giam"));
                km.setDonHangToiThieu(rs.getDouble("don_hang_toi_thieu"));
                km.setNgayBatDau(rs.getDate("ngay_bat_dau"));
                km.setNgayKetThuc(rs.getDate("ngay_ket_thuc"));

                danhSach.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO khuyen_mai " +
                "(ma_code, ten_khuyen_mai, loai_giam, gia_tri_giam, " +
                "don_hang_toi_thieu, ngay_bat_dau, ngay_ket_thuc) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, km.getMaCode());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setString(3, km.getLoaiGiam());
            ps.setDouble(4, km.getGiaTriGiam());
            ps.setDouble(5, km.getDonHangToiThieu());
            ps.setDate(6, km.getNgayBatDau());
            ps.setDate(7, km.getNgayKetThuc());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhuyenMai km) {
        String sql = "UPDATE khuyen_mai SET " +
                "ma_code=?, ten_khuyen_mai=?, loai_giam=?, " +
                "gia_tri_giam=?, don_hang_toi_thieu=?, " +
                "ngay_bat_dau=?, ngay_ket_thuc=? " +
                "WHERE ma_khuyen_mai=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, km.getMaCode());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setString(3, km.getLoaiGiam());
            ps.setDouble(4, km.getGiaTriGiam());
            ps.setDouble(5, km.getDonHangToiThieu());
            ps.setDate(6, km.getNgayBatDau());
            ps.setDate(7, km.getNgayKetThuc());
            ps.setInt(8, km.getMaKhuyenMai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maKhuyenMai) {
        String sql = "DELETE FROM khuyen_mai WHERE ma_khuyen_mai=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maKhuyenMai);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}