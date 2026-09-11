package com.restaurant.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EmployeeAccountPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color NEN = new Color(18, 18, 18);
    private static final Color KHUNG = new Color(24, 24, 24);
    private static final Color VIEN = new Color(52, 52, 52);
    private static final Color CHU = new Color(238, 235, 230);
    private static final Color PHU = new Color(145, 140, 133);
    private static final Color VANG = new Color(212, 163, 89);
    private static final Color XANH = new Color(86, 178, 125);

    public EmployeeAccountPanel() {
        setLayout(new BorderLayout());
        setBackground(NEN);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        add(taoTieuDe(), BorderLayout.NORTH);
        add(taoNoiDung(), BorderLayout.CENTER);
    }

    private JPanel taoTieuDe() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new BoxLayout(trai, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TÀI KHOẢN NHÂN VIÊN");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel sub = new JLabel("Thông tin tài khoản và ca làm việc");
        sub.setForeground(PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        trai.add(title);
        trai.add(Box.createVerticalStrut(5));
        trai.add(sub);

        JLabel status = new JLabel("TÀI KHOẢN ĐANG HOẠT ĐỘNG");
        status.setForeground(XANH);
        status.setFont(new Font("Segoe UI", Font.BOLD, 10));

        p.add(trai, BorderLayout.WEST);
        p.add(status, BorderLayout.EAST);

        return p;
    }

    private JPanel taoNoiDung() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);

        main.add(taoHoSo(), BorderLayout.NORTH);
        main.add(taoQuyen(), BorderLayout.CENTER);

        return main;
    }

    private JPanel taoHoSo() {
        JPanel p = new JPanel(new BorderLayout(25, 0));
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                BorderFactory.createEmptyBorder(24, 26, 24, 26)
        ));

        JPanel avatar = new JPanel(new BorderLayout());
        avatar.setPreferredSize(new Dimension(90, 90));
        avatar.setBackground(new Color(45, 38, 27));
        avatar.setBorder(BorderFactory.createLineBorder(VANG));

        JLabel av = new JLabel("QL", SwingConstants.CENTER);
        av.setForeground(VANG);
        av.setFont(new Font("Segoe UI", Font.BOLD, 27));

        avatar.add(av, BorderLayout.CENTER);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("Trần Minh Quản Lý");
        name.setForeground(CHU);
        name.setFont(new Font("Segoe UI", Font.BOLD, 23));

        JLabel role = new JLabel("QUẢN LÝ");
        role.setForeground(VANG);
        role.setFont(new Font("Segoe UI", Font.BOLD, 10));

        info.add(name);
        info.add(Box.createVerticalStrut(5));
        info.add(role);
        info.add(Box.createVerticalStrut(18));
        info.add(dong("Tên đăng nhập", "quanly"));
        info.add(Box.createVerticalStrut(7));
        info.add(dong("Mã nhân viên", "NV002"));

        JPanel ca = new JPanel(new BorderLayout());
        ca.setOpaque(false);
        ca.setPreferredSize(new Dimension(220, 80));

        JLabel caTitle = new JLabel("CA LÀM VIỆC");
        caTitle.setForeground(PHU);
        caTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));

        JLabel caValue = new JLabel("14:00 – 18:00");
        caValue.setForeground(VANG);
        caValue.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel caStatus = new JLabel("Đang trong ca");
        caStatus.setForeground(XANH);
        caStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        ca.add(caTitle, BorderLayout.NORTH);
        ca.add(caValue, BorderLayout.CENTER);
        ca.add(caStatus, BorderLayout.SOUTH);

        p.add(avatar, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        p.add(ca, BorderLayout.EAST);

        return p;
    }

    private JPanel dong(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(450, 24));

        JLabel l = new JLabel(label);
        l.setForeground(PHU);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel v = new JLabel(value);
        v.setForeground(CHU);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.CENTER);

        return p;
    }

    private JPanel taoQuyen() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setOpaque(false);

        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));

        JLabel t = new JLabel("QUYỀN TRUY CẬP");
        t.setForeground(CHU);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel s = new JLabel("Các chức năng được phép sử dụng trong ca làm việc");
        s.setForeground(PHU);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        title.add(t);
        title.add(Box.createVerticalStrut(3));
        title.add(s);

        JPanel ds = new JPanel(new GridLayout(1, 3, 12, 0));
        ds.setOpaque(false);

        ds.add(quyen("Sơ đồ bàn", "Theo dõi trạng thái bàn"));
        ds.add(quyen("Đơn hàng", "Theo dõi tiến trình đơn"));
        ds.add(quyen("Điều hành bếp", "Theo dõi hoạt động bếp"));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel security = new JLabel("Bảo mật tài khoản");
        security.setForeground(PHU);
        security.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JButton doiMatKhau = new JButton("Đổi mật khẩu");
        doiMatKhau.setForeground(VANG);
        doiMatKhau.setBackground(KHUNG);
        doiMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        doiMatKhau.setBorder(BorderFactory.createLineBorder(VIEN));
        doiMatKhau.setFocusPainted(false);
        doiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doiMatKhau.setMargin(new Insets(8, 16, 8, 16));

        bottom.add(security, BorderLayout.WEST);
        bottom.add(doiMatKhau, BorderLayout.EAST);

        p.add(title, BorderLayout.NORTH);
        p.add(ds, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private JPanel quyen(String title, String desc) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel check = new JLabel("✓", SwingConstants.CENTER);
        check.setPreferredSize(new Dimension(32, 32));
        check.setForeground(XANH);
        check.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setForeground(CHU);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel d = new JLabel(desc);
        d.setForeground(PHU);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        info.add(t);
        info.add(Box.createVerticalStrut(4));
        info.add(d);

        p.add(check, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);

        return p;
    }
}