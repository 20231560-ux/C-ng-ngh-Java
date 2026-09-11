package com.restaurant.view;

import java.util.List;
import javax.swing.JPanel;

public class KhoNguyenLieuPanel extends BangQuanLy {

    public KhoNguyenLieuPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "nguyên liệu"; }

    @Override protected String bangDB() { return "nguyen_lieu"; }

    @Override protected String khoaDB() { return "ma_nguyen_lieu"; }

    @Override protected String sapXepDB() { return "ma_nguyen_lieu DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã NL", 100, Cot.THUONG).db("ma_nguyen_lieu_code"),
                new Cot("Tên nguyên liệu", 230, Cot.DAM).db("ten_nguyen_lieu"),
                new Cot("Đơn vị", 100, Cot.THUONG).db("don_vi_tinh").chon("kg", "gram", "lít", "chai", "thùng", "con", "vỉ", "hộp"),
                new Cot("Tồn kho", 110, Cot.SO).db("so_luong"),
                new Cot("Định mức", 110, Cot.SO).db("so_luong_toi_thieu"),
                new Cot("Đơn giá", 130, Cot.TIEN).db("don_gia"),
                new Cot("Hạn sử dụng", 130, Cot.THUONG).db("han_su_dung"),
                new Cot("Tình trạng", 130, Cot.NHAN).khoa("tinh_trang")
        };
    }

    @Override protected void tinhThem(Object[] r) {
        double ton = 0, dinh = 0;
        int viTri = -1;
        for (int i = 0; i < cot.length; i++) {
            if ("so_luong".equals(cot[i].db)) ton = soTu(r[i]);
            if ("so_luong_toi_thieu".equals(cot[i].db)) dinh = soTu(r[i]);
            if ("tinh_trang".equals(cot[i].khoa)) viTri = i;
        }
        if (viTri < 0) return;
        r[viTri] = ton <= 0 ? "Đã hết" : (ton <= dinh ? "Sắp hết" : "Còn đủ");
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"NL01", "Thịt bò Úc", "kg", "2.5", "10", "320.000 đ", "20/09/2026", ""},
                {"NL02", "Tôm sú tươi", "kg", "6", "8", "280.000 đ", "10/09/2026", ""},
                {"NL03", "Gà ta thả vườn", "con", "12", "5", "150.000 đ", "12/09/2026", ""},
                {"NL04", "Rau xà lách", "kg", "1.2", "5", "25.000 đ", "07/09/2026", ""},
                {"NL05", "Gạo ST25", "kg", "80", "20", "32.000 đ", "01/03/2027", ""},
                {"NL06", "Bia Tiger", "thùng", "15", "10", "380.000 đ", "01/06/2027", ""},
                {"NL07", "Dầu ăn Neptune", "lít", "22", "10", "45.000 đ", "15/12/2026", ""},
                {"NL08", "Phô mai Mozzarella", "kg", "0", "3", "210.000 đ", "30/09/2026", ""}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), sap = 0, het = 0;
        double giaTri = 0;
        for (Object[] r : duLieu) {
            String t = gt(r, "tinh_trang");
            if ("Sắp hết".equals(t)) sap++;
            if ("Đã hết".equals(t)) het++;
            giaTri += soTu(gt(r, "so_luong")) * soTu(gt(r, "don_gia"));
        }
        return new JPanel[]{
                the("Mặt hàng trong kho", String.valueOf(tong), "Đang được theo dõi", NGOC),
                the("Sắp hết hàng", String.valueOf(sap), "Dưới định mức tối thiểu", CAM),
                the("Đã hết hàng", String.valueOf(het), "Cần nhập bổ sung gấp", DO),
                the("Giá trị tồn kho", tienVN(giaTri), "Tồn kho nhân đơn giá", LUC)
        };
    }
}