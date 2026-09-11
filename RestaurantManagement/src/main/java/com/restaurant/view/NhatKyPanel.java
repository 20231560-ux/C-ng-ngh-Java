package com.restaurant.view;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;

public class NhatKyPanel extends BangQuanLy {

    public NhatKyPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "nhật ký"; }

    @Override protected String bangDB() { return "nhat_ky_he_thong"; }

    @Override protected String khoaDB() { return "ma_nhat_ky"; }

    @Override protected String sapXepDB() { return "ma_nhat_ky DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã", 80, Cot.THUONG).db("ma_nhat_ky").khoaSua(),
                new Cot("Người dùng", 200, Cot.DAM).db("ma_nguoi_dung").fk("nguoi_dung", "ma_nguoi_dung", "ho_ten"),
                new Cot("Hành động", 180, Cot.NHAN).db("hanh_dong")
                        .chon("DANG_NHAP", "DANG_XUAT", "THEM", "SUA", "XOA", "THANH_TOAN"),
                new Cot("Nội dung", 460, Cot.THUONG).db("noi_dung"),
                new Cot("Thời gian", 180, Cot.THUONG).db("thoi_gian").khoaSua()
        };
    }

    @Override protected Color mauNhan(String v) {
        if ("DANG_NHAP".equals(v)) return LUC;
        if ("DANG_XUAT".equals(v)) return CHU_MO;
        if ("THEM".equals(v)) return NGOC;
        if ("SUA".equals(v)) return LAM;
        if ("XOA".equals(v)) return DO;
        if ("THANH_TOAN".equals(v)) return CAM;
        return super.mauNhan(v);
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"8", "Quản trị viên", "DANG_NHAP", "Đăng nhập hệ thống từ máy quầy lễ tân", "04/09/2026"},
                {"7", "Nguyễn Văn Thu Ngân", "THANH_TOAN", "Thanh toán đơn DH0007 số tiền 626.940 đ", "04/09/2026"},
                {"6", "Quản trị viên", "THEM", "Thêm món mới: Combo gia đình", "04/09/2026"},
                {"5", "Nguyễn Văn Quản Lý", "SUA", "Cập nhật giá bán món Gà rán giòn", "03/09/2026"},
                {"4", "Quản trị viên", "XOA", "Xoá nguyên liệu hết hạn: Phô mai Mozzarella", "03/09/2026"},
                {"3", "Lê Văn Phục Vụ", "DANG_NHAP", "Đăng nhập ca chiều", "03/09/2026"},
                {"2", "Trần Văn Bếp", "SUA", "Cập nhật trạng thái món trong đơn DH0005", "02/09/2026"},
                {"1", "Quản trị viên", "DANG_XUAT", "Kết thúc phiên làm việc", "02/09/2026"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), dangNhap = 0, thayDoi = 0, xoa = 0;
        for (Object[] r : duLieu) {
            String h = gt(r, "hanh_dong");
            if ("DANG_NHAP".equals(h)) dangNhap++;
            if ("THEM".equals(h) || "SUA".equals(h)) thayDoi++;
            if ("XOA".equals(h)) xoa++;
        }
        return new JPanel[]{
                the("Tổng bản ghi", String.valueOf(tong), "Nhật ký thao tác hệ thống", NGOC),
                the("Lượt đăng nhập", String.valueOf(dangNhap), "Phiên làm việc đã mở", LUC),
                the("Thao tác dữ liệu", String.valueOf(thayDoi), "Thêm mới và chỉnh sửa", LAM),
                the("Thao tác xoá", String.valueOf(xoa), "Cần rà soát định kỳ", DO)
        };
    }
}