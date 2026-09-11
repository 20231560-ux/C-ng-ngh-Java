package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class EmployeeFloorMapPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color NEN = new Color(0xF5F1E8);
    private final Color KHUNG = new Color(0xFFFCF7);
    private final Color VIEN = new Color(0xDDD3C5);
    private final Color CHU = new Color(0x29251F);
    private final Color PHU = new Color(0x756B60);
    private final Color VANG = new Color(0xC28A2E);
    private final Color TRONG = new Color(0x42B883);
    private final Color PHUCVU = new Color(0xE2A044);
    private final Color THANHTOAN = new Color(0xE35D55);
    private final Color DATTRUOC = new Color(0x5B96D6);

    private final JPanel danhSach = new JPanel();
    private final JLabel lbTong = new JLabel("0");
    private final JLabel lbTrong = new JLabel("0");
    private final JLabel lbPhucVu = new JLabel("0");
    private final JLabel lbThanhToan = new JLabel("0");
    private final JLabel lbDatTruoc = new JLabel("0");
    private final List<Ban> dsBan = new ArrayList<>();
    private final Consumer<String> onBanClick;
    private Timer timer;

    public EmployeeFloorMapPanel() {
        this(null);
    }

    public EmployeeFloorMapPanel(Consumer<String> onBanClick) {
        this.onBanClick = onBanClick;
        setLayout(new BorderLayout());
        setBackground(NEN);
        setBorder(new EmptyBorder(28, 30, 28, 30));
        add(taoHeader(), BorderLayout.NORTH);
        add(taoNoiDung(), BorderLayout.CENTER);
        napDuLieu();
        timer = new Timer(2000, e -> napDuLieu());
        timer.start();
    }

    private JPanel taoHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 18, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Quản lý bàn");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel sub = new JLabel("Theo dõi trạng thái và điều phối khu vực phục vụ");
        sub.setForeground(PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(sub);

        JButton refresh = new JButton("Làm mới");
        refresh.setForeground(VANG);
        refresh.setBackground(KHUNG);
        refresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refresh.setFocusPainted(false);
        refresh.setBorder(BorderFactory.createLineBorder(VIEN));
        refresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refresh.setMargin(new Insets(8, 15, 8, 15));
        refresh.addActionListener(e -> napDuLieu());

        p.add(left, BorderLayout.WEST);
        p.add(refresh, BorderLayout.EAST);
        return p;
    }

    private JPanel taoNoiDung() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);
        main.add(taoThongKe(), BorderLayout.NORTH);
        main.add(taoDanhSach(), BorderLayout.CENTER);
        return main;
    }

    private JPanel taoThongKe() {
        JPanel p = new JPanel(new GridLayout(1, 5, 10, 0));
        p.setOpaque(false);
        p.add(the("TỔNG BÀN", lbTong, VANG));
        p.add(the("TRỐNG", lbTrong, TRONG));
        p.add(the("ĐANG PHỤC VỤ", lbPhucVu, PHUCVU));
        p.add(the("THANH TOÁN", lbThanhToan, THANHTOAN));
        p.add(the("ĐẶT TRƯỚC", lbDatTruoc, DATTRUOC));
        return p;
    }

    private JPanel the(String title, JLabel value, Color mau) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                new EmptyBorder(13, 15, 13, 15)
        ));
        JLabel t = new JLabel(title);
        t.setForeground(PHU);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        value.setForeground(mau);
        value.setFont(new Font("Segoe UI", Font.BOLD, 27));
        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private JPanel taoDanhSach() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setOpaque(false);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TÌNH TRẠNG BÀN");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));

        JLabel sub = new JLabel("Bấm vào đúng bàn để xem thông tin bàn và đơn hiện tại");
        sub.setForeground(PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);
        top.add(left, BorderLayout.WEST);

        danhSach.setOpaque(false);
        danhSach.setLayout(new GridLayout(0, 5, 10, 10));
        danhSach.setBorder(new EmptyBorder(4, 2, 4, 2));

        JScrollPane scroll = new JScrollPane(danhSach);
        scroll.setBorder(BorderFactory.createLineBorder(VIEN));
        scroll.setBackground(KHUNG);
        scroll.getViewport().setBackground(KHUNG);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel legend = taoChuThich();
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(KHUNG);
        center.setBorder(BorderFactory.createEmptyBorder(12, 12, 10, 12));
        center.add(scroll, BorderLayout.CENTER);
        center.add(legend, BorderLayout.SOUTH);

        wrap.add(top, BorderLayout.NORTH);
        wrap.add(center, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel taoChuThich() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 7));
        p.setOpaque(false);
        p.add(chuThich("Trống", TRONG));
        p.add(chuThich("Phục vụ", PHUCVU));
        p.add(chuThich("Thanh toán", THANHTOAN));
        p.add(chuThich("Đặt trước", DATTRUOC));
        return p;
    }

    private JPanel chuThich(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 11));
        JLabel label = new JLabel(text);
        label.setForeground(PHU);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(dot);
        p.add(label);
        return p;
    }

    private void napDuLieu() {
        List<Ban> moi = new ArrayList<>();
        String sql = "SELECT ma_ban_so, trang_thai, suc_chua FROM ban_an ORDER BY ma_ban";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                moi.add(new Ban(
                        rs.getString("ma_ban_so"),
                        rs.getString("trang_thai"),
                        rs.getInt("suc_chua")
                ));
            }
        } catch (Exception e) {
            return;
        }
        dsBan.clear();
        dsBan.addAll(moi);
        capNhatThongKe();
        hienThi();
    }

    private void capNhatThongKe() {
        int tong = dsBan.size();
        int trong = 0;
        int phucVu = 0;
        int thanhToan = 0;
        int datTruoc = 0;

        for (Ban ban : dsBan) {
            switch (chuanHoa(ban.trangThai)) {
                case "TRONG": trong++; break;
                case "PHUC_VU": phucVu++; break;
                case "THANH_TOAN": thanhToan++; break;
                case "DAT_TRUOC": datTruoc++; break;
            }
        }

        lbTong.setText(String.valueOf(tong));
        lbTrong.setText(String.valueOf(trong));
        lbPhucVu.setText(String.valueOf(phucVu));
        lbThanhToan.setText(String.valueOf(thanhToan));
        lbDatTruoc.setText(String.valueOf(datTruoc));
    }

    private void hienThi() {
        danhSach.removeAll();
        for (Ban ban : dsBan) {
            danhSach.add(taoBan(ban));
        }
        danhSach.revalidate();
        danhSach.repaint();
    }

    private JPanel taoBan(Ban ban) {
        Color mau = mauTrangThai(ban.trangThai);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mau, 1),
                new EmptyBorder(12, 8, 10, 8)
        ));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ten = new JLabel(ban.maBan);
        ten.setAlignmentX(CENTER_ALIGNMENT);
        ten.setForeground(CHU);
        ten.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel suc = new JLabel(ban.sucChua + " chỗ");
        suc.setAlignmentX(CENTER_ALIGNMENT);
        suc.setForeground(PHU);
        suc.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel trangThai = new JLabel(tenTrangThai(ban.trangThai));
        trangThai.setAlignmentX(CENTER_ALIGNMENT);
        trangThai.setForeground(mau);
        trangThai.setFont(new Font("Segoe UI", Font.BOLD, 10));

        p.add(Box.createVerticalGlue());
        p.add(ten);
        p.add(Box.createVerticalStrut(4));
        p.add(suc);
        p.add(Box.createVerticalStrut(6));
        p.add(trangThai);
        p.add(Box.createVerticalGlue());

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                p.setBackground(new Color(
                        Math.min(255, mau.getRed() + 225),
                        Math.min(255, mau.getGreen() + 225),
                        Math.min(255, mau.getBlue() + 225)
                ));
                p.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(mau, 2),
                        new EmptyBorder(11, 7, 9, 7)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                p.setBackground(KHUNG);
                p.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(mau, 1),
                        new EmptyBorder(12, 8, 10, 8)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && onBanClick != null) {
                    onBanClick.accept(ban.maBan);
                }
            }
        };

        ganMouseListener(p, listener);
        ganMouseListener(ten, listener);
        ganMouseListener(suc, listener);
        ganMouseListener(trangThai, listener);
        return p;
    }

    private void ganMouseListener(java.awt.Component c, MouseAdapter listener) {
        c.addMouseListener(listener);
    }

    private String chuanHoa(String s) {
        if (s == null) return "TRONG";
        String v = s.trim().toUpperCase();
        if (v.contains("THANH_TOAN") || v.contains("THANH TOAN")) return "THANH_TOAN";
        if (v.contains("DAT_TRUOC") || v.contains("DAT TRUOC") || v.contains("DA_DAT")) return "DAT_TRUOC";
        if (v.contains("DANG_PHUC_VU") || v.contains("PHUC_VU") || v.contains("PHUC VU")) return "PHUC_VU";
        return "TRONG";
    }

    private Color mauTrangThai(String s) {
        switch (chuanHoa(s)) {
            case "PHUC_VU": return PHUCVU;
            case "THANH_TOAN": return THANHTOAN;
            case "DAT_TRUOC": return DATTRUOC;
            default: return TRONG;
        }
    }

    private String tenTrangThai(String s) {
        switch (chuanHoa(s)) {
            case "PHUC_VU": return "Đang phục vụ";
            case "THANH_TOAN": return "Thanh toán";
            case "DAT_TRUOC": return "Đặt trước";
            default: return "Trống";
        }
    }

    private static class Ban {
        String maBan;
        String trangThai;
        int sucChua;

        Ban(String maBan, String trangThai, int sucChua) {
            this.maBan = maBan;
            this.trangThai = trangThai;
            this.sucChua = sucChua;
        }
    }
}
