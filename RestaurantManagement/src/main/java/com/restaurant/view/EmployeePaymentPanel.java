package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class EmployeePaymentPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color NEN = new Color(0xF5F1E8);
    private final Color KHUNG = new Color(0xFFFCF7);
    private final Color VIEN = new Color(0xDDD3C5);
    private final Color CHU = new Color(0x29251F);
    private final Color PHU = new Color(0x756B60);
    private final Color VANG = new Color(0xC28A2E);
    private final Color XANH = new Color(0x42B883);
    private final DecimalFormat tien = new DecimalFormat("#,##0");

    private final JLabel lbBan = new JLabel("Chưa chọn bàn");
    private final JLabel lbTrangThai = new JLabel("-");
    private final JLabel lbMaDon = new JLabel("-");
    private final JLabel lbTong = new JLabel("0 đ");
    private final JLabel lbTamTinh = new JLabel("0 đ");
    private final JLabel lbGiam = new JLabel("0 đ");
    private final JLabel lbThue = new JLabel("0 đ");
    private final JTextArea danhSachMon = new JTextArea();
    private String maBanDangChon = "";
    private Timer timer;

    public EmployeePaymentPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(NEN);
        setBorder(new EmptyBorder(28, 30, 28, 30));
        add(taoHeader(), BorderLayout.NORTH);
        add(taoNoiDung(), BorderLayout.CENTER);
        timer = new Timer(3000, e -> {
            if (!maBanDangChon.isEmpty()) {
                taiDonCuaBan();
            }
        });
        timer.start();
    }

    public void hienThiBan(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) return;
        maBanDangChon = maBan.trim();
        lbBan.setText("Bàn " + maBanDangChon);
        taiDonCuaBan();
    }

    private JPanel taoHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new javax.swing.BoxLayout(left, javax.swing.BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Thanh toán");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel sub = new JLabel("Xem đơn hiện tại theo đúng bàn được chọn");
        sub.setForeground(PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        left.add(title);
        left.add(javax.swing.Box.createVerticalStrut(5));
        left.add(sub);

        JButton refresh = new JButton("Làm mới");
        refresh.setForeground(VANG);
        refresh.setBackground(KHUNG);
        refresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refresh.setFocusPainted(false);
        refresh.setBorder(BorderFactory.createLineBorder(VIEN));
        refresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refresh.addActionListener(e -> taiDonCuaBan());

        p.add(left, BorderLayout.WEST);
        p.add(refresh, BorderLayout.EAST);
        return p;
    }

    private JPanel taoNoiDung() {
        JPanel main = new JPanel(new BorderLayout(16, 16));
        main.setOpaque(false);
        main.add(taoThongTinBan(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);
        center.add(taoMon());
        center.add(taoHoaDon());
        main.add(center, BorderLayout.CENTER);
        return main;
    }

    private JPanel taoThongTinBan() {
        JPanel p = khung();
        p.setLayout(new GridLayout(1, 3, 20, 0));
        p.setPreferredSize(new Dimension(100, 92));
        p.add(thongTin("BÀN", lbBan));
        p.add(thongTin("TRẠNG THÁI", lbTrangThai));
        p.add(thongTin("MÃ ĐƠN", lbMaDon));
        return p;
    }

    private JPanel thongTin(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout(0, 7));
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setForeground(PHU);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        value.setForeground(CHU);
        value.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private JPanel taoMon() {
        JPanel p = khung();
        p.setLayout(new BorderLayout(0, 10));
        JLabel title = new JLabel("MÓN TRONG ĐƠN");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        danhSachMon.setEditable(false);
        danhSachMon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        danhSachMon.setForeground(CHU);
        danhSachMon.setBackground(KHUNG);
        danhSachMon.setLineWrap(true);
        danhSachMon.setWrapStyleWord(true);
        danhSachMon.setText("Chưa chọn bàn.");
        JScrollPane scroll = new JScrollPane(danhSachMon);
        scroll.setBorder(BorderFactory.createLineBorder(VIEN));
        p.add(title, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel taoHoaDon() {
        JPanel p = khung();
        p.setLayout(new BorderLayout(0, 12));
        JLabel title = new JLabel("THÔNG TIN HÓA ĐƠN");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel rows = new JPanel(new GridLayout(4, 2, 8, 12));
        rows.setOpaque(false);
        rows.add(nhan("Tạm tính"));
        rows.add(gia(lbTamTinh));
        rows.add(nhan("Giảm giá"));
        rows.add(gia(lbGiam));
        rows.add(nhan("Thuế"));
        rows.add(gia(lbThue));
        rows.add(nhan("Tổng cộng"));
        rows.add(gia(lbTong));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        JLabel note = new JLabel("Thanh toán sẽ được xử lý ở bước tiếp theo");
        note.setForeground(PHU);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bottom.add(note, BorderLayout.WEST);

        p.add(title, BorderLayout.NORTH);
        p.add(rows, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JLabel nhan(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(PHU);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private JLabel gia(JLabel l) {
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setForeground(CHU);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    private JPanel khung() {
        JPanel p = new JPanel();
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return p;
    }

    private void taiDonCuaBan() {
        if (maBanDangChon.isEmpty()) return;

        String sql = "SELECT d.ma_don_hang, d.ma_don, d.trang_thai, d.tien_tam_tinh, d.tien_giam, d.tien_thue, d.tong_tien "
                + "FROM don_hang d JOIN ban_an b ON b.ma_ban = d.ma_ban "
                + "WHERE b.ma_ban_so = ? AND d.trang_thai IN ('CHO_XU_LY','DANG_PHUC_VU') "
                + "ORDER BY d.ngay_tao DESC LIMIT 1";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maBanDangChon);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maDonHang = rs.getInt("ma_don_hang");
                    lbMaDon.setText(rs.getString("ma_don"));
                    lbTrangThai.setText(tenTrangThai(rs.getString("trang_thai")));
                    lbTamTinh.setText(dinhDang(rs.getDouble("tien_tam_tinh")));
                    lbGiam.setText(dinhDang(rs.getDouble("tien_giam")));
                    lbThue.setText(dinhDang(rs.getDouble("tien_thue")));
                    lbTong.setText(dinhDang(rs.getDouble("tong_tien")));
                    taiMon(c, maDonHang);
                    return;
                }
            }
        } catch (Exception e) {
            lbTrangThai.setText("Lỗi kết nối");
        }

        lbMaDon.setText("-");
        lbTrangThai.setText("Chưa có đơn");
        lbTamTinh.setText("0 đ");
        lbGiam.setText("0 đ");
        lbThue.setText("0 đ");
        lbTong.setText("0 đ");
        danhSachMon.setText("Bàn " + maBanDangChon + " hiện chưa có đơn hàng đang phục vụ.");
    }

    private void taiMon(Connection c, int maDonHang) throws Exception {
        String sql = "SELECT m.ten_mon, ct.so_luong, ct.don_gia, ct.ghi_chu "
                + "FROM chi_tiet_don_hang ct JOIN mon_an m ON m.ma_mon = ct.ma_mon "
                + "WHERE ct.ma_don_hang = ? ORDER BY ct.ma_chi_tiet";
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getString("ten_mon"))
                            .append(" x")
                            .append(rs.getInt("so_luong"))
                            .append("  ")
                            .append(dinhDang(rs.getDouble("don_gia")))
                            .append("\n");
                    String ghiChu = rs.getString("ghi_chu");
                    if (ghiChu != null && !ghiChu.trim().isEmpty()) {
                        sb.append("   ").append(ghiChu.trim()).append("\n");
                    }
                }
            }
        }
        danhSachMon.setText(sb.length() == 0 ? "Đơn chưa có món." : sb.toString());
    }

    private String dinhDang(double value) {
        return tien.format(value) + " đ";
    }

    private String tenTrangThai(String s) {
        if (s == null) return "-";
        if ("CHO_XU_LY".equalsIgnoreCase(s)) return "Chờ tiếp nhận";
        if ("DANG_PHUC_VU".equalsIgnoreCase(s)) return "Đang phục vụ";
        if ("HOAN_THANH".equalsIgnoreCase(s)) return "Hoàn thành";
        return s;
    }
}
