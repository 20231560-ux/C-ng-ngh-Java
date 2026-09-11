package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class NhanVienPanel extends BangQuanLy {

    public NhanVienPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "nhân viên"; }

    @Override protected String bangDB() { return "nguoi_dung"; }

    @Override protected String khoaDB() { return "ma_nguoi_dung"; }

    @Override protected String sapXepDB() { return "ma_nguoi_dung"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã NV", 90, Cot.THUONG).db("ma_nguoi_dung").khoaSua(),
                new Cot("Họ và tên", 220, Cot.DAM).db("ho_ten"),
                new Cot("Tên đăng nhập", 170, Cot.THUONG).db("ten_dang_nhap"),
                new Cot("Mật khẩu", 140, Cot.THUONG).db("mat_khau"),
                new Cot("Vai trò", 170, Cot.NHAN).db("vai_tro")
                        .chon("QUAN_TRI", "QUAN_LY", "THU_NGAN", "BEP", "PHUC_VU", "KHACH_HANG"),
                new Cot("Ngày tạo", 150, Cot.THUONG).db("ngay_tao").khoaSua(),
                new Cot("Trạng thái", 150, Cot.NHAN).db("trang_thai").batTat("Đang làm việc", "Đã nghỉ việc")
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

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"1", "Quản trị viên", "admin", "123456", "QUAN_TRI", "03/09/2026", "Đang làm việc"},
                {"2", "Nguyễn Văn Quản Lý", "quanly", "123456", "QUAN_LY", "03/09/2026", "Đang làm việc"},
                {"3", "Nguyễn Văn Thu Ngân", "thungan", "123456", "THU_NGAN", "03/09/2026", "Đang làm việc"},
                {"4", "Trần Văn Bếp", "bep", "123456", "BEP", "03/09/2026", "Đang làm việc"},
                {"5", "Lê Văn Phục Vụ", "phucvu", "123456", "PHUC_VU", "03/09/2026", "Đang làm việc"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = 0, lam = 0, quanLy = 0, khach = 0;
        for (Object[] r : duLieu) {
            String v = gt(r, "vai_tro");
            if ("KHACH_HANG".equals(v)) { khach++; continue; }
            tong++;
            if (!"Đã nghỉ việc".equals(gt(r, "trang_thai"))) lam++;
            if ("QUAN_TRI".equals(v) || "QUAN_LY".equals(v)) quanLy++;
        }
        return new JPanel[]{
                the("Tổng nhân sự", String.valueOf(tong), "Không tính tài khoản khách", NGOC),
                the("Đang làm việc", String.valueOf(lam), (tong - lam) + " người đã nghỉ", LUC),
                the("Cấp quản lý", String.valueOf(quanLy), "Quản trị và quản lý", CAM),
                the("Tài khoản khách", String.valueOf(khach), "Khách tự đăng ký", LAM)
        };
    }
}