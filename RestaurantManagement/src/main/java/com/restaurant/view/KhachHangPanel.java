package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class KhachHangPanel extends BangQuanLy {

    public KhachHangPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "khách hàng"; }

    @Override protected String bangDB() { return "khach_hang"; }

    @Override protected String khoaDB() { return "ma_khach_hang"; }

    @Override protected String sapXepDB() { return "ma_khach_hang DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã KH", 90, Cot.THUONG).db("ma_khach_hang").khoaSua(),
                new Cot("Họ và tên", 210, Cot.DAM).db("ho_ten"),
                new Cot("Số điện thoại", 150, Cot.THUONG).db("so_dien_thoai"),
                new Cot("Email", 210, Cot.THUONG).db("email"),
                new Cot("Địa chỉ", 190, Cot.THUONG).db("dia_chi"),
                new Cot("Điểm tích luỹ", 120, Cot.SO).db("diem_tich_luy"),
                new Cot("Tổng chi tiêu", 150, Cot.TIEN).db("tong_chi_tieu"),
                new Cot("Hạng", 130, Cot.NHAN).khoa("hang"),
                new Cot("Ghi chú", 160, Cot.THUONG).db("ghi_chu")
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("Kim cương".equals(v)) return TIM;
        if ("Vàng".equals(v)) return CAM;
        if ("Bạc".equals(v)) return LAM;
        if ("Đồng".equals(v)) return new Color(0xB08968);
        return super.mauNhan(v);
    }

    @Override protected void tinhThem(Object[] r) {
        double diem = 0;
        int viTri = -1;
        for (int i = 0; i < cot.length; i++) {
            if ("diem_tich_luy".equals(cot[i].db)) diem = soTu(r[i]);
            if ("hang".equals(cot[i].khoa)) viTri = i;
        }
        if (viTri < 0) return;
        r[viTri] = diem >= 3000 ? "Kim cương" : diem >= 1000 ? "Vàng" : diem >= 300 ? "Bạc" : "Đồng";
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"1", "Nguyễn Văn An", "0901234567", "an@gmail.com", "Hà Nội", "1240", "24.800.000 đ", "", ""},
                {"2", "Trần Thị Bình", "0912345678", "binh@gmail.com", "Nam Định", "3410", "68.200.000 đ", "", ""},
                {"3", "Lê Văn Cường", "0987654321", "cuong@gmail.com", "Hải Phòng", "820", "16.400.000 đ", "", ""},
                {"4", "Phạm Thị Dung", "0978123456", "dung@gmail.com", "Hà Nội", "5600", "112.000.000 đ", "", ""},
                {"5", "Hoàng Văn Em", "0965432109", "em@gmail.com", "Nam Định", "180", "3.600.000 đ", "", ""}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), vip = 0;
        double diem = 0, chi = 0;
        for (Object[] r : duLieu) {
            String h = gt(r, "hang");
            if ("Vàng".equals(h) || "Kim cương".equals(h)) vip++;
            diem += soTu(gt(r, "diem_tich_luy"));
            chi += soTu(gt(r, "tong_chi_tieu"));
        }
        return new JPanel[]{
                the("Tổng khách hàng", String.valueOf(tong), "Trong hệ thống thành viên", NGOC),
                the("Khách VIP", String.valueOf(vip), "Hạng Vàng và Kim cương", CAM),
                the("Tổng chi tiêu", tienVN(chi), "Doanh thu từ khách thành viên", LUC),
                the("Tổng điểm tích luỹ", String.valueOf((long) diem), "Toàn bộ khách hàng", LAM)
        };
    }
}