package com.restaurant.view;

import java.util.List;
import javax.swing.JPanel;

public class MonAnPanel extends BangQuanLy {

    public MonAnPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "món"; }

    @Override protected String bangDB() { return "mon_an"; }

    @Override protected String khoaDB() { return "ma_mon"; }

    @Override protected String sapXepDB() { return "ma_mon DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã món", 100, Cot.THUONG).db("ma_mon_an"),
                new Cot("Tên món", 230, Cot.DAM).db("ten_mon"),
                new Cot("Danh mục", 160, Cot.NHAN).db("ma_danh_muc").fk("danh_muc_mon", "ma_danh_muc", "ten_danh_muc"),
                new Cot("Mô tả", 260, Cot.THUONG).db("mo_ta"),
                new Cot("Giá bán", 130, Cot.TIEN).db("gia_ban"),
                new Cot("Giá vốn", 130, Cot.TIEN).db("gia_von"),
                new Cot("Ảnh", 160, Cot.THUONG).db("duong_dan_anh"),
                new Cot("Trạng thái", 130, Cot.NHAN).db("trang_thai").batTat("Đang bán", "Ngừng bán")
        };
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"MON001", "Phở bò đặc biệt", "Món chính", "Phở bò truyền thống", "65.000 đ", "35.000 đ", "", "Đang bán"},
                {"MON002", "Gà rán giòn", "Món chính", "Gà rán giòn sốt đặc biệt", "75.000 đ", "40.000 đ", "", "Đang bán"},
                {"MON003", "Burger bò phô mai", "Món chính", "Burger bò kèm phô mai", "85.000 đ", "45.000 đ", "", "Đang bán"},
                {"MON004", "Coca Cola", "Đồ uống", "Nước ngọt Coca Cola", "15.000 đ", "8.000 đ", "", "Đang bán"},
                {"MON005", "Trà đào cam sả", "Đồ uống", "Trà đào cam sả", "35.000 đ", "15.000 đ", "", "Đang bán"},
                {"MON006", "Chè khúc bạch", "Tráng miệng", "Chè khúc bạch trái cây", "30.000 đ", "12.000 đ", "", "Đang bán"},
                {"MON007", "Combo gia đình", "Combo", "Combo dành cho 4 người", "250.000 đ", "140.000 đ", "", "Đang bán"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), ban = 0;
        double tongBan = 0, loi = 0;
        for (Object[] r : duLieu) {
            if (!"Ngừng bán".equals(gt(r, "trang_thai"))) ban++;
            double gb = soTu(gt(r, "gia_ban")), gv = soTu(gt(r, "gia_von"));
            tongBan += gb;
            loi += gb - gv;
        }
        return new JPanel[]{
                the("Tổng số món", String.valueOf(tong), "Trong toàn bộ thực đơn", NGOC),
                the("Đang kinh doanh", String.valueOf(ban), (tong - ban) + " món đã ngừng bán", LUC),
                the("Giá bán trung bình", tienVN(tong == 0 ? 0 : tongBan / tong), "Trên mỗi món", CAM),
                the("Lãi gộp trung bình", tienVN(tong == 0 ? 0 : loi / tong), "Giá bán trừ giá vốn", LAM)
        };
    }
}