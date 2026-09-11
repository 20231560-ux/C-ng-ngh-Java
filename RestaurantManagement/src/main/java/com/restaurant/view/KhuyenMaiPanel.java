package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class KhuyenMaiPanel extends BangQuanLy {

    public KhuyenMaiPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "khuyến mãi"; }

    @Override protected String bangDB() { return "khuyen_mai"; }

    @Override protected String khoaDB() { return "ma_khuyen_mai"; }

    @Override protected String sapXepDB() { return "ma_khuyen_mai DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã code", 120, Cot.THUONG).db("ma_code"),
                new Cot("Tên chương trình", 260, Cot.DAM).db("ten_khuyen_mai"),
                new Cot("Hình thức", 150, Cot.NHAN).db("loai_giam").chon("Phần trăm", "Tiền mặt"),
                new Cot("Giá trị giảm", 140, Cot.SO).db("gia_tri_giam"),
                new Cot("Đơn tối thiểu", 150, Cot.TIEN).db("don_hang_toi_thieu"),
                new Cot("Từ ngày", 120, Cot.THUONG).db("ngay_bat_dau"),
                new Cot("Đến ngày", 120, Cot.THUONG).db("ngay_ket_thuc"),
                new Cot("Lượt dùng", 110, Cot.SO).db("so_luong"),
                new Cot("Trạng thái", 140, Cot.NHAN).db("trang_thai").batTat("Đang chạy", "Đã dừng")
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("Phần trăm".equals(v)) return CAM;
        if ("Tiền mặt".equals(v)) return LUC;
        return super.mauNhan(v);
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"KM10", "Giảm 10% cuối tuần", "Phần trăm", "10", "200.000 đ", "01/09/2026", "30/09/2026", "0", "Đang chạy"},
                {"KM50K", "Giảm 50.000đ", "Tiền mặt", "50000", "300.000 đ", "01/09/2026", "30/09/2026", "0", "Đang chạy"},
                {"KM15", "Ưu đãi khách hàng mới", "Phần trăm", "15", "150.000 đ", "01/09/2026", "31/10/2026", "0", "Đang chạy"},
                {"COMBO100", "Ưu đãi combo gia đình", "Tiền mặt", "100000", "500.000 đ", "05/09/2026", "30/09/2026", "0", "Đang chạy"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), chay = 0, phanTram = 0;
        double tienMat = 0;
        for (Object[] r : duLieu) {
            if (!"Đã dừng".equals(gt(r, "trang_thai"))) chay++;
            if ("Phần trăm".equals(gt(r, "loai_giam"))) phanTram++;
            else tienMat += soTu(gt(r, "gia_tri_giam"));
        }
        return new JPanel[]{
                the("Tổng chương trình", String.valueOf(tong), "Đã tạo trong hệ thống", NGOC),
                the("Đang áp dụng", String.valueOf(chay), (tong - chay) + " chương trình đã dừng", LUC),
                the("Giảm theo phần trăm", String.valueOf(phanTram), (tong - phanTram) + " chương trình giảm tiền", CAM),
                the("Tổng mức giảm tiền", tienVN(tienMat), "Cộng dồn các mã tiền mặt", LAM)
        };
    }
}