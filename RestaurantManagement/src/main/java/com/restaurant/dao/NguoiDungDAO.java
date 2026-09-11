package com.restaurant.dao;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NguoiDungDAO {

    public static final String ADMIN = "ADMIN";
    public static final String QUAN_LY = "QUAN_LY";
    public static final String NHAN_VIEN = "NHAN_VIEN";
    public static final String PHUC_VU = "PHUC_VU";
    public static final String THU_NGAN = "THU_NGAN";
    public static final String BEP = "BEP";
    public static final String KHACH_HANG = "KHACH_HANG";

    public String loiKetNoi;

    public static String chuanHoaVaiTro(String vaiTro) {
        if (vaiTro == null) {
            return "";
        }

        String v = vaiTro.trim()
                .toUpperCase()
                .replace(' ', '_')
                .replace("Ả", "A")
                .replace("Ạ", "A")
                .replace("Ả", "A")
                .replace("Â", "A")
                .replace("Ă", "A")
                .replace("Ê", "E")
                .replace("Ơ", "O")
                .replace("Ố", "O")
                .replace("Ô", "O")
                .replace("Ộ", "O")
                .replace("Ư", "U")
                .replace("Ậ", "A")
                .replace("Ị", "I")
                .replace("Ý", "Y")
                .replace("Ỳ", "Y")
                .replace("Ỷ", "Y")
                .replace("Ỹ", "Y")
                .replace("Ỵ", "Y");

        if (v.contains("ADMIN") || v.contains("QUAN_TRI")) {
            return ADMIN;
        }

        if (v.contains("QUAN_LY")) {
            return QUAN_LY;
        }

        if (v.contains("PHUC_VU")) {
            return PHUC_VU;
        }

        if (v.contains("THU_NGAN")) {
            return THU_NGAN;
        }

        if (v.contains("BEP")) {
            return BEP;
        }

        if (v.equals("NHAN_VIEN") || v.contains("STAFF")) {
            return NHAN_VIEN;
        }

        if (v.contains("KHACH") || v.contains("CUSTOMER") || v.equals("USER")) {
            return KHACH_HANG;
        }

        return v;
    }

    public NguoiDung dangNhap(String tenDangNhap, String matKhau) {
        String sql = """
                SELECT ma_nguoi_dung,
                       ten_dang_nhap,
                       mat_khau,
                       ho_ten,
                       vai_tro,
                       trang_thai
                FROM nguoi_dung
                WHERE ten_dang_nhap = ?
                  AND mat_khau = ?
                  AND trang_thai = TRUE
                """;

        try (
                Connection ketNoi = DatabaseConnection.getConnection();
                PreparedStatement ps = ketNoi.prepareStatement(sql)
        ) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return docNguoiDung(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung dangNhap(String tenDangNhap, String matKhau, String vaiTro) {
        String sql = """
                SELECT ma_nguoi_dung,
                       ten_dang_nhap,
                       mat_khau,
                       ho_ten,
                       vai_tro,
                       trang_thai
                FROM nguoi_dung
                WHERE ten_dang_nhap = ?
                  AND mat_khau = ?
                  AND vai_tro = ?
                  AND trang_thai = TRUE
                """;

        try (
                Connection ketNoi = DatabaseConnection.getConnection();
                PreparedStatement ps = ketNoi.prepareStatement(sql)
        ) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            ps.setString(3, vaiTro);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return docNguoiDung(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung timTheoTenDangNhap(String tenDangNhap) {
        String sql = """
                SELECT ma_nguoi_dung,
                       ten_dang_nhap,
                       mat_khau,
                       ho_ten,
                       vai_tro,
                       trang_thai
                FROM nguoi_dung
                WHERE ten_dang_nhap = ?
                """;

        try (
                Connection ketNoi = DatabaseConnection.getConnection();
                PreparedStatement ps = ketNoi.prepareStatement(sql)
        ) {
            ps.setString(1, tenDangNhap);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loiKetNoi = null;
                return docNguoiDung(rs);
            }

            loiKetNoi = null;

        } catch (Exception e) {
            loiKetNoi = e.getMessage();
            e.printStackTrace();
        }

        return null;
    }

    public boolean tonTaiTenDangNhap(String tenDangNhap) {
        return timTheoTenDangNhap(tenDangNhap) != null;
    }

    public NguoiDung dangKyKhachHang(String hoTen, String soDienThoai, String matKhau) {
        String sql = """
                INSERT INTO nguoi_dung
                       (ten_dang_nhap, mat_khau, ho_ten, vai_tro, trang_thai)
                VALUES (?, ?, ?, ?, TRUE)
                """;

        try (
                Connection ketNoi = DatabaseConnection.getConnection();
                PreparedStatement ps = ketNoi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, soDienThoai);
            ps.setString(2, matKhau);
            ps.setString(3, hoTen);
            ps.setString(4, KHACH_HANG);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            NguoiDung nguoiDung = new NguoiDung();

            try (ResultSet khoa = ps.getGeneratedKeys()) {
                if (khoa.next()) {
                    nguoiDung.setMaNguoiDung(khoa.getInt(1));
                }
            }

            nguoiDung.setTenDangNhap(soDienThoai);
            nguoiDung.setMatKhau(matKhau);
            nguoiDung.setHoTen(hoTen);
            nguoiDung.setVaiTro(KHACH_HANG);
            nguoiDung.setTrangThai(true);

            return nguoiDung;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean doiMatKhau(int maNguoiDung, String matKhauMoi) {
        String sql = """
                UPDATE nguoi_dung
                SET mat_khau = ?
                WHERE ma_nguoi_dung = ?
                """;

        try (
                Connection ketNoi = DatabaseConnection.getConnection();
                PreparedStatement ps = ketNoi.prepareStatement(sql)
        ) {
            ps.setString(1, matKhauMoi);
            ps.setInt(2, maNguoiDung);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public java.util.List<NguoiDung> layDanhSachNhanVien() {
        java.util.List<NguoiDung> list = new java.util.ArrayList<>();

        String sql = """
                SELECT *
                FROM nguoi_dung
                WHERE vai_tro != 'KHACH_HANG'
                ORDER BY ma_nguoi_dung ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(docNguoiDung(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public NguoiDung timTheoMa(int maNguoiDung) {
        String sql = """
                SELECT *
                FROM nguoi_dung
                WHERE ma_nguoi_dung = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, maNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return docNguoiDung(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean themNhanVien(NguoiDung nd) {
        String sql = """
                INSERT INTO nguoi_dung
                (
                    ten_dang_nhap,
                    mat_khau,
                    ho_ten,
                    vai_tro,
                    so_dien_thoai,
                    email,
                    ca_lam,
                    luong,
                    trang_thai
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, nd.getTenDangNhap());
            ps.setString(2, nd.getMatKhau() != null ? nd.getMatKhau() : "123456");
            ps.setString(3, nd.getHoTen());
            ps.setString(4, nd.getVaiTro() != null ? chuanHoaVaiTro(nd.getVaiTro()) : PHUC_VU);
            ps.setString(5, nd.getSoDienThoai());
            ps.setString(6, nd.getEmail());
            ps.setString(7, nd.getCaLam() != null ? nd.getCaLam() : "Sáng");
            ps.setDouble(8, nd.getLuong());
            ps.setBoolean(9, nd.isTrangThai());

            int affected = ps.executeUpdate();

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        nd.setMaNguoiDung(keys.getInt(1));
                    }
                }

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean suaNhanVien(NguoiDung nd) {
        String sql = """
                UPDATE nguoi_dung
                SET ho_ten = ?,
                    mat_khau = ?,
                    vai_tro = ?,
                    so_dien_thoai = ?,
                    email = ?,
                    ca_lam = ?,
                    luong = ?,
                    trang_thai = ?
                WHERE ma_nguoi_dung = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getMatKhau());
            ps.setString(3, chuanHoaVaiTro(nd.getVaiTro()));
            ps.setString(4, nd.getSoDienThoai());
            ps.setString(5, nd.getEmail());
            ps.setString(6, nd.getCaLam());
            ps.setDouble(7, nd.getLuong());
            ps.setBoolean(8, nd.isTrangThai());
            ps.setInt(9, nd.getMaNguoiDung());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhanVien(int maNguoiDung) {
        String checkSql = "SELECT COUNT(*) FROM don_hang WHERE ma_nguoi_dung = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, maNguoiDung);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return doiTrangThai(maNguoiDung, false);
                    }
                }
            }

            String deleteSql = "DELETE FROM nguoi_dung WHERE ma_nguoi_dung = ?";

            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setInt(1, maNguoiDung);
                return psDel.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean doiTrangThai(int maNguoiDung, boolean trangThai) {
        String sql = """
                UPDATE nguoi_dung
                SET trang_thai = ?
                WHERE ma_nguoi_dung = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, trangThai);
            ps.setInt(2, maNguoiDung);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private NguoiDung docNguoiDung(ResultSet rs) throws Exception {
        NguoiDung nguoiDung = new NguoiDung();

        nguoiDung.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
        nguoiDung.setTenDangNhap(rs.getString("ten_dang_nhap"));
        nguoiDung.setMatKhau(rs.getString("mat_khau"));
        nguoiDung.setHoTen(rs.getString("ho_ten"));
        nguoiDung.setVaiTro(rs.getString("vai_tro"));
        nguoiDung.setTrangThai(rs.getBoolean("trang_thai"));

        try {
            nguoiDung.setSoDienThoai(rs.getString("so_dien_thoai"));
        } catch (Exception ignored) {
        }

        try {
            nguoiDung.setEmail(rs.getString("email"));
        } catch (Exception ignored) {
        }

        try {
            nguoiDung.setCaLam(rs.getString("ca_lam"));
        } catch (Exception ignored) {
        }

        try {
            nguoiDung.setLuong(rs.getDouble("luong"));
        } catch (Exception ignored) {
        }

        return nguoiDung;
    }
}