package com.restaurant.view;

import java.util.List;
import javax.swing.JPanel;

public class NhaCungCapPanel extends BangQuanLy {

    public NhaCungCapPanel() {
        dungGiaoDien();
    }

    @Override protected String tenThucThe() { return "nhà cung cấp"; }

    @Override protected String bangDB() { return "nha_cung_cap"; }

    @Override protected String khoaDB() { return "ma_nha_cung_cap"; }

    @Override protected String sapXepDB() { return "ma_nha_cung_cap DESC"; }

    @Override protected Cot[] dinhNghiaCot() {
        return new Cot[]{
                new Cot("Mã NCC", 110, Cot.THUONG).db("ma_nha_cung_cap_code"),
                new Cot("Tên nhà cung cấp", 250, Cot.DAM).db("ten_nha_cung_cap"),
                new Cot("Người liên hệ", 180, Cot.THUONG).db("nguoi_lien_he"),
                new Cot("Điện thoại", 140, Cot.THUONG).db("so_dien_thoai"),
                new Cot("Email", 200, Cot.THUONG).db("email"),
                new Cot("Địa chỉ", 220, Cot.THUONG).db("dia_chi"),
                new Cot("Hợp tác", 150, Cot.NHAN).db("trang_thai").batTat("Đang hợp tác", "Ngừng hợp tác")
        };
    }

    @Override protected void napMau(List<Object[]> ds) {
        Object[][] d = {
                {"NCC01", "Cty TP Sạch Miền Bắc", "Nguyễn Văn Đạt", "0903112334", "dat@tpsach.vn", "Hà Nội", "Đang hợp tác"},
                {"NCC02", "Hải sản Cát Bà", "Trần Thị Hương", "0912445667", "huong@haisan.vn", "Hải Phòng", "Đang hợp tác"},
                {"NCC03", "HTX Rau Mộc Châu", "Lê Minh Quang", "0987220118", "quang@raumc.vn", "Sơn La", "Đang hợp tác"},
                {"NCC04", "Trại gà Ba Vì", "Phạm Quốc Huy", "0977331552", "huy@gabavi.vn", "Hà Nội", "Đang hợp tác"},
                {"NCC05", "NPP Bia Hà Nội", "Đỗ Thanh Tùng", "0932668990", "tung@biahn.vn", "Hà Nội", "Đang hợp tác"},
                {"NCC06", "Cty Sữa An Phát", "Hoàng Nam Sơn", "0968554003", "son@anphat.vn", "Nam Định", "Ngừng hợp tác"}
        };
        for (Object[] x : d) ds.add(x);
    }

    @Override protected JPanel[] thongKe() {
        int tong = duLieu.size(), dang = 0, coMail = 0;
        for (Object[] r : duLieu) {
            if (!"Ngừng hợp tác".equals(gt(r, "trang_thai"))) dang++;
            if (!gt(r, "email").isEmpty()) coMail++;
        }
        return new JPanel[]{
                the("Tổng nhà cung cấp", String.valueOf(tong), "Trong danh sách đối tác", NGOC),
                the("Đang hợp tác", String.valueOf(dang), (tong - dang) + " đối tác đã ngừng", LUC),
                the("Có email liên hệ", String.valueOf(coMail), "Đủ thông tin liên lạc", LAM),
                the("Cần bổ sung", String.valueOf(tong - coMail), "Hồ sơ còn thiếu email", CAM)
        };
    }
}