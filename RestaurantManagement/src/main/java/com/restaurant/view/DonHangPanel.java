package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class DonHangPanel extends BangQuanLy {

    public DonHangPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "đơn hàng"; }

    @Override protected String bangDB() { return "don_hang"; }

    @Override protected String khoaDB() { return "ma_don_hang"; }

    @Override protected String sapXepDB() { return "ma_don_hang DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã đơn", 120, Cot.THUONG).db("ma_don"),
                new Cot("Bàn", 130, Cot.DAM).db("ma_ban").fk("ban_an", "ma_ban", "ten_ban"),
                new Cot("Khách hàng", 180, Cot.THUONG).db("ma_khach_hang").fk("khach_hang", "ma_khach_hang", "ho_ten"),
                new Cot("Nhân viên", 180, Cot.THUONG).db("ma_nguoi_dung").fk("nguoi_dung", "ma_nguoi_dung", "ho_ten"),
                new Cot("Tạm tính", 140, Cot.TIEN).db("tien_tam_tinh"),
                new Cot("Giảm giá", 130, Cot.TIEN).db("tien_giam"),
                new Cot("Thuế", 120, Cot.TIEN).db("tien_thue"),
                new Cot("Tổng tiền", 150, Cot.TIEN).db("tong_tien"),
                new Cot("Ngày tạo", 150, Cot.THUONG).db("ngay_tao").khoaSua(),
                new Cot("Trạng thái", 160, Cot.NHAN).db("trang_thai")
                        .chon("CHO_XU_LY", "DANG_PHUC_VU", "HOAN_THANH", "DA_HUY")
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("HOAN_THANH".equals(v)) return LUC;
        if ("DANG_PHUC_VU".equals(v)) return CAM;
        if ("CHO_XU_LY".equals(v)) return LAM;
        if ("DA_HUY".equals(v)) return DO;
        return super.mauNhan(v);
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"DH0007", "Bàn 05", "Trần Thị Bình", "Nguyễn Văn Thu Ngân", "645.000 đ", "64.500 đ", "46.440 đ", "626.940 đ", "04/09/2026", "HOAN_THANH"},
                {"DH0006", "Bàn 02", "Nguyễn Văn An", "Nguyễn Văn Thu Ngân", "320.000 đ", "0 đ", "25.600 đ", "345.600 đ", "04/09/2026", "DANG_PHUC_VU"},
                {"DH0005", "Bàn 09", "Khách lẻ", "Lê Văn Phục Vụ", "180.000 đ", "0 đ", "14.400 đ", "194.400 đ", "04/09/2026", "CHO_XU_LY"},
                {"DH0004", "Bàn 01", "Phạm Thị Dung", "Nguyễn Văn Thu Ngân", "890.000 đ", "89.000 đ", "64.080 đ", "865.080 đ", "03/09/2026", "HOAN_THANH"},
                {"DH0003", "Bàn 07", "Khách lẻ", "Lê Văn Phục Vụ", "250.000 đ", "0 đ", "20.000 đ", "270.000 đ", "03/09/2026", "DA_HUY"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), xong = 0, dang = 0;
        double doanhThu = 0;
        for (Object[] r : duLieu) {
            String t = gt(r, "trang_thai");
            if ("HOAN_THANH".equals(t)) { xong++; doanhThu += soTu(gt(r, "tong_tien")); }
            if ("DANG_PHUC_VU".equals(t) || "CHO_XU_LY".equals(t)) dang++;
        }
        return new JPanel[]{
                the("Tổng đơn hàng", String.valueOf(tong), "Toàn bộ lịch sử đơn", NGOC),
                the("Đã hoàn thành", String.valueOf(xong), dang + " đơn đang xử lý", LUC),
                the("Doanh thu ghi nhận", tienVN(doanhThu), "Từ các đơn hoàn thành", CAM),
                the("Giá trị đơn TB", tienVN(xong == 0 ? 0 : doanhThu / xong), "Trên mỗi đơn hoàn thành", LAM)
        };
    }
}