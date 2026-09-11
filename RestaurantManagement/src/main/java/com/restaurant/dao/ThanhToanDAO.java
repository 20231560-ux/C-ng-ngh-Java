package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.ThanhToan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDAO {

    public boolean thucHienThanhToan(
            int maDonHang,
            int maBan,
            String phuongThuc,
            double soTien,
            String maGiaoDich,
            Integer maKhachHang
    ) {
        String insertPaySql = "INSERT INTO thanh_toan (ma_don_hang, phuong_thuc, so_tien, trang_thai, ma_giao_dich) "
                            + "VALUES (?, ?, ?, 'DA_THANH_TOAN', ?)";
        String updateOrderSql = "UPDATE don_hang SET trang_thai = 'HOAN_THANH' WHERE ma_don_hang = ?";
        String updateTableSql = "UPDATE ban_an SET trang_thai = 'TRONG' WHERE ma_ban = ?";
        String updateCustomerSql = "UPDATE khach_hang SET tong_chi_tieu = COALESCE(tong_chi_tieu, 0) + ?, "
                                 + "                    diem_tich_luy = COALESCE(diem_tich_luy, 0) + ? "
                                 + "WHERE ma_khach_hang = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Ghi nhận thanh toán
                try (PreparedStatement psPay = conn.prepareStatement(insertPaySql, Statement.RETURN_GENERATED_KEYS)) {
                    psPay.setInt(1, maDonHang);
                    psPay.setString(2, phuongThuc != null ? phuongThuc : "TIEN_MAT");
                    psPay.setDouble(3, soTien);
                    psPay.setString(4, maGiaoDich);
                    psPay.executeUpdate();
                }

                // 2. Cập nhật trạng thái đơn hàng
                try (PreparedStatement psOrder = conn.prepareStatement(updateOrderSql)) {
                    psOrder.setInt(1, maDonHang);
                    psOrder.executeUpdate();
                }

                // 3. Đổi trạng thái bàn về TRONG
                if (maBan > 0) {
                    try (PreparedStatement psTable = conn.prepareStatement(updateTableSql)) {
                        psTable.setInt(1, maBan);
                        psTable.executeUpdate();
                    }
                }

                // 4. Cộng tích lũy khách hàng nếu có
                if (maKhachHang != null && maKhachHang > 0) {
                    try (PreparedStatement psCust = conn.prepareStatement(updateCustomerSql)) {
                        psCust.setDouble(1, soTien);
                        int diemCong = (int) (soTien / 10000); // 10k = 1 điểm
                        psCust.setInt(2, diemCong);
                        psCust.setInt(3, maKhachHang);
                        psCust.executeUpdate();
                    }
                }

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

    public List<ThanhToan> layLichSuThanhToan(int limit) {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM thanh_toan ORDER BY ma_thanh_toan DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 100);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThanhToan tt = new ThanhToan();
                    tt.setMaThanhToan(rs.getInt("ma_thanh_toan"));
                    tt.setMaDonHang(rs.getInt("ma_don_hang"));
                    tt.setPhuongThuc(rs.getString("phuong_thuc"));
                    tt.setSoTien(rs.getDouble("so_tien"));
                    tt.setTrangThai(rs.getString("trang_thai"));
                    tt.setMaGiaoDich(rs.getString("ma_giao_dich"));
                    tt.setThoiGianThanhToan(rs.getTimestamp("thoi_gian_thanh_toan"));
                    list.add(tt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
