package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.BanAn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BanAnDAO {

    public List<BanAn> layTatCa() { 
        List<BanAn> danhSach = new ArrayList<>();
        String sql = "SELECT ma_ban, ma_ban_so, ten_ban, suc_chua, trang_thai, khu_vuc, ghi_chu "
                   + "FROM ban_an ORDER BY ma_ban ASC";

        try (Connection ketNoi = DatabaseConnection.getConnection();
             PreparedStatement ps = ketNoi.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BanAn ban = new BanAn();
                ban.setMaBan(rs.getInt("ma_ban"));
                ban.setMaBanSo(rs.getString("ma_ban_so"));
                ban.setTenBan(rs.getString("ten_ban"));
                ban.setSoCho(rs.getInt("suc_chua"));
                ban.setTrangThai(rs.getString("trang_thai"));
                ban.setKhuVuc(rs.getString("khu_vuc"));
                ban.setGhiChu(rs.getString("ghi_chu"));
                danhSach.add(ban);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public BanAn timTheoMa(int maBan) {
        String sql = "SELECT ma_ban, ma_ban_so, ten_ban, suc_chua, trang_thai, khu_vuc, ghi_chu "
                   + "FROM ban_an WHERE ma_ban = ?";
        try (Connection ketNoi = DatabaseConnection.getConnection();
             PreparedStatement ps = ketNoi.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BanAn ban = new BanAn();
                    ban.setMaBan(rs.getInt("ma_ban"));
                    ban.setMaBanSo(rs.getString("ma_ban_so"));
                    ban.setTenBan(rs.getString("ten_ban"));
                    ban.setSoCho(rs.getInt("suc_chua"));
                    ban.setTrangThai(rs.getString("trang_thai"));
                    ban.setKhuVuc(rs.getString("khu_vuc"));
                    ban.setGhiChu(rs.getString("ghi_chu"));
                    return ban;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themBan(BanAn ban) {
        String sql = "INSERT INTO ban_an (ma_ban_so, ten_ban, suc_chua, khu_vuc, trang_thai, ghi_chu) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection ketNoi = DatabaseConnection.getConnection();
             PreparedStatement ps = ketNoi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String maSo = ban.getMaBanSo();
            if (maSo == null || maSo.trim().isEmpty()) {
                maSo = "B" + (System.currentTimeMillis() % 10000);
            }
            ps.setString(1, maSo);
            ps.setString(2, ban.getTenBan());
            ps.setInt(3, ban.getSoCho() > 0 ? ban.getSoCho() : 4);
            ps.setString(4, ban.getKhuVuc() != null ? ban.getKhuVuc() : "Tầng 1");
            ps.setString(5, ban.getTrangThai() != null ? ban.getTrangThai() : "TRONG");
            ps.setString(6, ban.getGhiChu());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        ban.setMaBan(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaBan(BanAn ban) {
        String sql = "UPDATE ban_an SET ten_ban = ?, suc_chua = ?, khu_vuc = ?, trang_thai = ?, ghi_chu = ? "
                   + "WHERE ma_ban = ?";
        try (Connection ketNoi = DatabaseConnection.getConnection();
             PreparedStatement ps = ketNoi.prepareStatement(sql)) {
            ps.setString(1, ban.getTenBan());
            ps.setInt(2, ban.getSoCho());
            ps.setString(3, ban.getKhuVuc());
            ps.setString(4, ban.getTrangThai());
            ps.setString(5, ban.getGhiChu());
            ps.setInt(6, ban.getMaBan());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaBan(int maBan) {
        String checkSql = "SELECT COUNT(*) FROM don_hang WHERE ma_ban = ? AND trang_thai = 'DANG_PHUC_VU'";
        try (Connection ketNoi = DatabaseConnection.getConnection()) {
            try (PreparedStatement psCheck = ketNoi.prepareStatement(checkSql)) {
                psCheck.setInt(1, maBan);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            String updateOrders = "UPDATE don_hang SET ma_ban = NULL WHERE ma_ban = ?";
            try (PreparedStatement psUp = ketNoi.prepareStatement(updateOrders)) {
                psUp.setInt(1, maBan);
                psUp.executeUpdate();
            }

            String deleteSql = "DELETE FROM ban_an WHERE ma_ban = ?";
            try (PreparedStatement psDel = ketNoi.prepareStatement(deleteSql)) {
                psDel.setInt(1, maBan);
                return psDel.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTrangThai(int maBan, String trangThai) {
        String sql = "UPDATE ban_an SET trang_thai = ? WHERE ma_ban = ?";
        try (Connection ketNoi = DatabaseConnection.getConnection();
             PreparedStatement ps = ketNoi.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean chuyenBan(int maBanCu, int maBanMoi) {
        String sqlFindOrder = "SELECT ma_don_hang FROM don_hang WHERE ma_ban = ? AND trang_thai = 'DANG_PHUC_VU' ORDER BY ma_don_hang DESC LIMIT 1";
        String sqlUpdateOrder = "UPDATE don_hang SET ma_ban = ? WHERE ma_don_hang = ?";
        String sqlUpdateOldTable = "UPDATE ban_an SET trang_thai = 'TRONG' WHERE ma_ban = ?";
        String sqlUpdateNewTable = "UPDATE ban_an SET trang_thai = 'DANG_PHUC_VU' WHERE ma_ban = ?";

        try (Connection ketNoi = DatabaseConnection.getConnection()) {
            ketNoi.setAutoCommit(false);
            try {
                int maDonHang = -1;
                try (PreparedStatement psFind = ketNoi.prepareStatement(sqlFindOrder)) {
                    psFind.setInt(1, maBanCu);
                    try (ResultSet rs = psFind.executeQuery()) {
                        if (rs.next()) {
                            maDonHang = rs.getInt(1);
                        }
                    }
                }

                if (maDonHang > 0) {
                    try (PreparedStatement psUp = ketNoi.prepareStatement(sqlUpdateOrder)) {
                        psUp.setInt(1, maBanMoi);
                        psUp.setInt(2, maDonHang);
                        psUp.executeUpdate();
                    }
                }

                try (PreparedStatement psOld = ketNoi.prepareStatement(sqlUpdateOldTable)) {
                    psOld.setInt(1, maBanCu);
                    psOld.executeUpdate();
                }

                try (PreparedStatement psNew = ketNoi.prepareStatement(sqlUpdateNewTable)) {
                    psNew.setInt(1, maBanMoi);
                    psNew.executeUpdate();
                }

                ketNoi.commit();
                return true;
            } catch (Exception ex) {
                ketNoi.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                ketNoi.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean gopBan(int maBanNguon, int maBanDich) {
        String sqlFindOrder = "SELECT ma_don_hang, tien_tam_tinh, tong_tien FROM don_hang WHERE ma_ban = ? AND trang_thai = 'DANG_PHUC_VU' ORDER BY ma_don_hang DESC LIMIT 1";

        try (Connection ketNoi = DatabaseConnection.getConnection()) {
            ketNoi.setAutoCommit(false);
            try {
                int maDonNguon = -1;
                try (PreparedStatement ps = ketNoi.prepareStatement(sqlFindOrder)) {
                    ps.setInt(1, maBanNguon);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) maDonNguon = rs.getInt(1);
                    }
                }

                int maDonDich = -1;
                try (PreparedStatement ps = ketNoi.prepareStatement(sqlFindOrder)) {
                    ps.setInt(1, maBanDich);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) maDonDich = rs.getInt(1);
                    }
                }

                if (maDonNguon > 0 && maDonDich > 0 && maDonNguon != maDonDich) {
                    String sqlMoveItems = "UPDATE chi_tiet_don_hang SET ma_don_hang = ? WHERE ma_don_hang = ?";
                    try (PreparedStatement psMove = ketNoi.prepareStatement(sqlMoveItems)) {
                        psMove.setInt(1, maDonDich);
                        psMove.setInt(2, maDonNguon);
                        psMove.executeUpdate();
                    }

                    String sqlHuyDon = "UPDATE don_hang SET trang_thai = 'DA_HUY', ghi_chu = 'Gộp vào bàn đích' WHERE ma_don_hang = ?";
                    try (PreparedStatement psHuy = ketNoi.prepareStatement(sqlHuyDon)) {
                        psHuy.setInt(1, maDonNguon);
                        psHuy.executeUpdate();
                    }

                    String sqlRecalc = "SELECT SUM(so_luong * don_gia) FROM chi_tiet_don_hang WHERE ma_don_hang = ?";
                    double tongMoi = 0;
                    try (PreparedStatement psCalc = ketNoi.prepareStatement(sqlRecalc)) {
                        psCalc.setInt(1, maDonDich);
                        try (ResultSet rs = psCalc.executeQuery()) {
                            if (rs.next()) tongMoi = rs.getDouble(1);
                        }
                    }

                    String sqlUpDich = "UPDATE don_hang SET tien_tam_tinh = ?, tong_tien = ? WHERE ma_don_hang = ?";
                    try (PreparedStatement psUpDich = ketNoi.prepareStatement(sqlUpDich)) {
                        psUpDich.setDouble(1, tongMoi);
                        psUpDich.setDouble(2, tongMoi);
                        psUpDich.setInt(3, maDonDich);
                        psUpDich.executeUpdate();
                    }

                    String sqlFreeTable = "UPDATE ban_an SET trang_thai = 'TRONG' WHERE ma_ban = ?";
                    try (PreparedStatement psFree = ketNoi.prepareStatement(sqlFreeTable)) {
                        psFree.setInt(1, maBanNguon);
                        psFree.executeUpdate();
                    }
                } else if (maDonNguon > 0 && maDonDich <= 0) {
                    return chuyenBan(maBanNguon, maBanDich);
                }

                ketNoi.commit();
                return true;
            } catch (Exception ex) {
                ketNoi.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                ketNoi.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}