package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class TaiKhoanPanel extends BangQuanLy {

    public TaiKhoanPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "tài khoản"; }

    @Override protected String bangDB() { return "nguoi_dung"; }

    @Override protected String khoaDB() { return "ma_nguoi_dung"; }

    @Override protected String sapXepDB() { return "ma_nguoi_dung"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã", 80, Cot.THUONG).db("ma_nguoi_dung").khoaSua(),
                new Cot("Tên đăng nhập", 170, Cot.DAM).db("ten_dang_nhap"),
                new Cot("Họ và tên", 210, Cot.THUONG).db("ho_ten"),
                new Cot("Mật khẩu", 130, Cot.THUONG).db("mat_khau"),
                new Cot("Vai trò", 160, Cot.NHAN).db("vai_tro")
                        .chon("QUAN_TRI", "QUAN_LY", "THU_NGAN", "BEP", "PHUC_VU", "KHACH_HANG"),
                new Cot("Phạm vi truy cập", 300, Cot.THUONG).khoa("quyen"),
                new Cot("Trạng thái", 150, Cot.NHAN).db("trang_thai").batTat("Đang kích hoạt", "Đã khoá")
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("QUAN_TRI".equals(v)) return TIM;
        if ("QUAN_LY".equals(v)) return CAM;
        if ("THU_NGAN".equals(v)) return LAM;
        if ("BEP".equals(v)) return new Color(0xFF9E6D);
        if ("PHUC_VU".equals(v)) return NGOC;
        if ("KHACH_HANG".equals(v)) return CHU_MO;
        return super.mauNhan(v);
    }

    @Override protected void tinhThem(Object[] r) {
        String vai = "";
        int viTri = -1;
        for (int i = 0; i < cot.length; i++) {
            if ("vai_tro".equals(cot[i].db)) vai = String.valueOf(r[i]);
            if ("quyen".equals(cot[i].khoa)) viTri = i;
        }
        if (viTri < 0) return;
        switch (vai) {
            case "QUAN_TRI": r[viTri] = "Toàn quyền trên mọi phân hệ"; break;
            case "QUAN_LY": r[viTri] = "Quản lý, báo cáo, nhân sự (không sửa hệ thống)"; break;
            case "THU_NGAN": r[viTri] = "Đơn hàng, thanh toán, khách hàng"; break;
            case "BEP": r[viTri] = "Nhà bếp, thực đơn, kho nguyên liệu"; break;
            case "PHUC_VU": r[viTri] = "Sơ đồ bàn, gọi món"; break;
            case "KHACH_HANG": r[viTri] = "Chỉ tra cứu thông tin cá nhân"; break;
            default: r[viTri] = "Chưa phân quyền";
        }
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"1", "admin", "Quản trị viên", "123456", "QUAN_TRI", "", "Đang kích hoạt"},
                {"2", "quanly", "Nguyễn Văn Quản Lý", "123456", "QUAN_LY", "", "Đang kích hoạt"},
                {"3", "thungan", "Nguyễn Văn Thu Ngân", "123456", "THU_NGAN", "", "Đang kích hoạt"},
                {"4", "bep", "Trần Văn Bếp", "123456", "BEP", "", "Đang kích hoạt"},
                {"5", "phucvu", "Lê Văn Phục Vụ", "123456", "PHUC_VU", "", "Đang kích hoạt"},
                {"6", "0374869209", "Mai Ngọc Tấn", "Ton12345.", "KHACH_HANG", "", "Đang kích hoạt"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), hoat = 0, quanTri = 0, khach = 0;
        for (Object[] r : duLieu) {
            if (!"Đã khoá".equals(gt(r, "trang_thai"))) hoat++;
            String v = gt(r, "vai_tro");
            if ("QUAN_TRI".equals(v) || "QUAN_LY".equals(v)) quanTri++;
            if ("KHACH_HANG".equals(v)) khach++;
        }
        return new JPanel[]{
                the("Tổng tài khoản", String.valueOf(tong), "Toàn hệ thống", NGOC),
                the("Đang kích hoạt", String.valueOf(hoat), (tong - hoat) + " tài khoản bị khoá", LUC),
                the("Quyền quản trị", String.valueOf(quanTri), "Cấp quản trị và quản lý", TIM),
                the("Tài khoản khách", String.valueOf(khach), "Khách tự đăng ký", LAM)
        };
    }
}