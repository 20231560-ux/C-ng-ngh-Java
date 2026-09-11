package com.restaurant.view;

import java.util.List;
import javax.swing.JPanel;

public class DanhMucPanel extends BangQuanLy {

    public DanhMucPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "danh mục"; }

    @Override protected String bangDB() { return "danh_muc_mon"; }

    @Override protected String khoaDB() { return "ma_danh_muc"; }

    @Override protected String sapXepDB() { return "ma_danh_muc"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã", 90, Cot.THUONG).db("ma_danh_muc").khoaSua(),
                new Cot("Tên danh mục", 240, Cot.DAM).db("ten_danh_muc"),
                new Cot("Mô tả", 420, Cot.THUONG).db("mo_ta"),
                new Cot("Thứ tự", 110, Cot.SO).db("thu_tu"),
                new Cot("Trạng thái", 150, Cot.NHAN).db("trang_thai").batTat("Đang hiển thị", "Đang ẩn")
        };
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"1", "Món chính", "Các món ăn chính", "1", "Đang hiển thị"},
                {"2", "Đồ uống", "Các loại đồ uống", "2", "Đang hiển thị"},
                {"3", "Tráng miệng", "Các món tráng miệng", "3", "Đang hiển thị"},
                {"4", "Combo", "Các combo tiết kiệm", "4", "Đang hiển thị"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), hien = 0;
        for (Object[] r : duLieu) if (!"Đang ẩn".equals(gt(r, "trang_thai"))) hien++;
        int mon = demMon();
        return new JPanel[]{
                the("Tổng danh mục", String.valueOf(tong), "Nhóm món trong thực đơn", NGOC),
                the("Đang hiển thị", String.valueOf(hien), (tong - hien) + " danh mục đang ẩn", LUC),
                the("Món được phân loại", String.valueOf(mon), "Lấy từ bảng mon_an", LAM),
                the("Trung bình mỗi nhóm", tong == 0 ? "0" : String.valueOf(mon / tong), "Món trên một danh mục", CAM)
        };
    }

    private int demMon() {
        if (!dungCSDL) return 7;
        try (java.sql.Connection c = com.restaurant.config.DatabaseConnection.getConnection();
             java.sql.Statement st = c.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM mon_an")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}