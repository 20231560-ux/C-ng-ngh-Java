package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class DanhGiaPanel extends BangQuanLy {

    public DanhGiaPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "đánh giá"; }

    @Override protected String bangDB() { return "danh_gia"; }

    @Override protected String khoaDB() { return "ma_danh_gia"; }

    @Override protected String sapXepDB() { return "ma_danh_gia DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã", 80, Cot.THUONG).db("ma_danh_gia").khoaSua(),
                new Cot("Khách hàng", 190, Cot.DAM).db("ma_khach_hang").fk("khach_hang", "ma_khach_hang", "ho_ten"),
                new Cot("Món ăn", 200, Cot.THUONG).db("ma_mon").fk("mon_an", "ma_mon", "ten_mon"),
                new Cot("Số sao", 90, Cot.SO).db("so_sao").chon("1", "2", "3", "4", "5"),
                new Cot("Mức độ", 150, Cot.NHAN).khoa("muc_do"),
                new Cot("Nội dung", 320, Cot.THUONG).db("noi_dung"),
                new Cot("Ngày đánh giá", 160, Cot.THUONG).db("ngay_danh_gia").khoaSua()
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("Rất hài lòng".equals(v)) return LUC;
        if ("Hài lòng".equals(v)) return NGOC;
        if ("Bình thường".equals(v)) return CAM;
        if ("Không hài lòng".equals(v)) return DO;
        return super.mauNhan(v);
    }

    @Override protected void tinhThem(Object[] r) {
        double sao = 0;
        int viTri = -1;
        for (int i = 0; i < cot.length; i++) {
            if ("so_sao".equals(cot[i].db)) sao = soTu(r[i]);
            if ("muc_do".equals(cot[i].khoa)) viTri = i;
        }
        if (viTri < 0) return;
        r[viTri] = sao >= 5 ? "Rất hài lòng" : sao >= 4 ? "Hài lòng" : sao >= 3 ? "Bình thường" : "Không hài lòng";
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"5", "Phạm Thị Dung", "Combo gia đình", "5", "", "Combo rất đáng tiền, phục vụ nhanh", "04/09/2026"},
                {"4", "Nguyễn Văn An", "Phở bò đặc biệt", "5", "", "Nước dùng đậm đà, thịt mềm", "03/09/2026"},
                {"3", "Lê Văn Cường", "Gà rán giòn", "4", "", "Gà giòn nhưng hơi mặn", "03/09/2026"},
                {"2", "Trần Thị Bình", "Trà đào cam sả", "3", "", "Trà hơi ngọt so với khẩu vị", "02/09/2026"},
                {"1", "Hoàng Văn Em", "Burger bò phô mai", "2", "", "Bánh nguội, chờ hơi lâu", "01/09/2026"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), tot = 0, kem = 0;
        double sao = 0;
        for (Object[] r : duLieu) {
            double s = soTu(gt(r, "so_sao"));
            sao += s;
            if (s >= 4) tot++;
            if (s <= 2) kem++;
        }
        double tb = tong == 0 ? 0 : sao / tong;
        return new JPanel[]{
                the("Tổng đánh giá", String.valueOf(tong), "Phản hồi từ khách hàng", NGOC),
                the("Điểm trung bình", String.format("%.1f / 5", tb), "Mức độ hài lòng chung", CAM),
                the("Đánh giá tích cực", String.valueOf(tot),
                        tong == 0 ? "Chưa có dữ liệu" : Math.round(tot * 100f / tong) + "% tổng số", LUC),
                the("Cần cải thiện", String.valueOf(kem), "Đánh giá từ 2 sao trở xuống", DO)
        };
    }
}