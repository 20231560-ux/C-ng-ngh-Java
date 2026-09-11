package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class ThanhToanPanel extends BangQuanLy {

    public ThanhToanPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "giao dịch"; }

    @Override protected String bangDB() { return "thanh_toan"; }

    @Override protected String khoaDB() { return "ma_thanh_toan"; }

    @Override protected String sapXepDB() { return "ma_thanh_toan DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã GD", 100, Cot.THUONG).db("ma_thanh_toan").khoaSua(),
                new Cot("Đơn hàng", 150, Cot.DAM).db("ma_don_hang").fk("don_hang", "ma_don_hang", "ma_don"),
                new Cot("Phương thức", 170, Cot.NHAN).db("phuong_thuc")
                        .chon("TIEN_MAT", "CHUYEN_KHOAN", "THE_NGAN_HANG", "VI_DIEN_TU"),
                new Cot("Số tiền", 160, Cot.TIEN).db("so_tien"),
                new Cot("Mã giao dịch", 200, Cot.THUONG).db("ma_giao_dich"),
                new Cot("Thời gian", 170, Cot.THUONG).db("thoi_gian_thanh_toan").khoaSua(),
                new Cot("Trạng thái", 170, Cot.NHAN).db("trang_thai")
                        .chon("DA_THANH_TOAN", "CHO_XU_LY", "HOAN_TIEN", "THAT_BAI")
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("DA_THANH_TOAN".equals(v)) return LUC;
        if ("CHO_XU_LY".equals(v)) return CAM;
        if ("HOAN_TIEN".equals(v) || "THAT_BAI".equals(v)) return DO;
        if ("TIEN_MAT".equals(v)) return LUC;
        if ("CHUYEN_KHOAN".equals(v)) return LAM;
        if ("THE_NGAN_HANG".equals(v)) return TIM;
        if ("VI_DIEN_TU".equals(v)) return CAM;
        return super.mauNhan(v);
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"5", "DH0007", "CHUYEN_KHOAN", "626.940 đ", "VCB2609041732", "04/09/2026", "DA_THANH_TOAN"},
                {"4", "DH0006", "TIEN_MAT", "345.600 đ", "", "04/09/2026", "CHO_XU_LY"},
                {"3", "DH0004", "THE_NGAN_HANG", "865.080 đ", "POS88213445", "03/09/2026", "DA_THANH_TOAN"},
                {"2", "DH0002", "VI_DIEN_TU", "412.000 đ", "MOMO7712398", "02/09/2026", "DA_THANH_TOAN"},
                {"1", "DH0001", "TIEN_MAT", "158.000 đ", "", "01/09/2026", "HOAN_TIEN"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), xong = 0, tienMat = 0;
        double thu = 0;
        for (Object[] r : duLieu) {
            if ("DA_THANH_TOAN".equals(gt(r, "trang_thai"))) { xong++; thu += soTu(gt(r, "so_tien")); }
            if ("TIEN_MAT".equals(gt(r, "phuong_thuc"))) tienMat++;
        }
        return new JPanel[]{
                the("Tổng giao dịch", String.valueOf(tong), "Toàn bộ lượt thanh toán", NGOC),
                the("Đã thanh toán", String.valueOf(xong), (tong - xong) + " giao dịch chưa xong", LUC),
                the("Tiền đã thu", tienVN(thu), "Từ giao dịch thành công", CAM),
                the("Thanh toán tiền mặt", String.valueOf(tienMat), (tong - tienMat) + " giao dịch không tiền mặt", LAM)
        };
    }
}