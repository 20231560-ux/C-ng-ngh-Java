package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CaiDatPanel extends JPanel {

    static final Color KHOI = new Color(0x0E2429);
    static final Color KHOI_2 = new Color(0x102C31);
    static final Color VIEN = new Color(0x1A3A42);
    static final Color NGOC = new Color(0x2BE8C8);
    static final Color NGOC_MO = new Color(0x14A88F);
    static final Color CHU = new Color(0xE6F2F0);
    static final Color CHU_MO = new Color(0x7E9A98);
    static final Color LUC = new Color(0x2BE89A);
    static final Color DO = new Color(0xFF6B6B);
    static final String FONT = font();

    private final JLabel trangThaiKetNoi = nhan("Đang kiểm tra…", 13, Font.BOLD, CHU_MO);
    private final JLabel soBang = nhan("—", 13, Font.BOLD, CHU);

    public CaiDatPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(16, 0));
        add(cotTrai(), BorderLayout.CENTER);
        add(cotPhai(), BorderLayout.EAST);
        kiemTraKetNoi();
    }

    private JPanel cotTrai() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        Khoi k1 = new Khoi();
        k1.setLayout(new BoxLayout(k1, BoxLayout.Y_AXIS));
        k1.add(tieuDe("Thông tin nhà hàng", "Hiển thị trên hoá đơn in cho khách"));
        k1.add(Box.createRigidArea(new Dimension(0, 16)));
        k1.add(o("Tên nhà hàng", "NOVA RESTAURANT"));
        k1.add(o("Địa chỉ", "123 Nguyễn Trãi, Thanh Xuân, Hà Nội"));
        k1.add(o("Số điện thoại", "1900 6868"));
        k1.add(o("Mã số thuế", "0101234567"));
        k1.add(o("Thuế VAT áp dụng (%)", "8"));
        k1.add(Box.createRigidArea(new Dimension(0, 8)));
        Nut luu = new Nut("Lưu thông tin", true);
        luu.setAlignmentX(Component.LEFT_ALIGNMENT);
        luu.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Đã lưu thông tin nhà hàng.", "NOVA RESTAURANT", JOptionPane.INFORMATION_MESSAGE));
        k1.add(luu);
        p.add(k1);
        p.add(Box.createRigidArea(new Dimension(0, 16)));

        Khoi k2 = new Khoi();
        k2.setLayout(new BoxLayout(k2, BoxLayout.Y_AXIS));
        k2.add(tieuDe("Tuỳ chọn vận hành", "Ảnh hưởng tới cách hệ thống hoạt động"));
        k2.add(Box.createRigidArea(new Dimension(0, 14)));
        k2.add(congTac("Tự động in hoá đơn sau khi thanh toán", true));
        k2.add(congTac("Cảnh báo khi nguyên liệu dưới định mức", true));
        k2.add(congTac("Cho phép khách hàng tự đăng ký tài khoản", true));
        k2.add(congTac("Ghi nhật ký mọi thao tác của nhân viên", true));
        k2.add(congTac("Yêu cầu xác nhận khi huỷ đơn hàng", false));
        p.add(k2);
        return p;
    }

    private JPanel cotPhai() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(420, 100));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        Khoi k1 = new Khoi();
        k1.setLayout(new BoxLayout(k1, BoxLayout.Y_AXIS));
        k1.add(tieuDe("Kết nối cơ sở dữ liệu", "Thông tin lấy từ DatabaseConnection"));
        k1.add(Box.createRigidArea(new Dimension(0, 14)));
        k1.add(dong("Máy chủ", "localhost:3306"));
        k1.add(dong("Cơ sở dữ liệu", "quan_ly_nha_hang"));
        k1.add(dong("Tài khoản", "root"));
        k1.add(dong("Số bảng", soBang));
        k1.add(dong("Trạng thái", trangThaiKetNoi));
        k1.add(Box.createRigidArea(new Dimension(0, 12)));
        Nut ktra = new Nut("Kiểm tra kết nối", false);
        ktra.setAlignmentX(Component.LEFT_ALIGNMENT);
        ktra.addActionListener(e -> kiemTraKetNoi());
        k1.add(ktra);
        p.add(k1);
        p.add(Box.createRigidArea(new Dimension(0, 16)));

        Khoi k2 = new Khoi();
        k2.setLayout(new BoxLayout(k2, BoxLayout.Y_AXIS));
        k2.add(tieuDe("Thông tin hệ thống", "Phiên bản và môi trường chạy"));
        k2.add(Box.createRigidArea(new Dimension(0, 14)));
        k2.add(dong("Ứng dụng", "NOVA Restaurant v1.0"));
        k2.add(dong("Nền tảng", "Java Swing + JDBC"));
        k2.add(dong("Phiên bản Java", System.getProperty("java.version")));
        k2.add(dong("Hệ điều hành", System.getProperty("os.name")));
        p.add(k2);
        return p;
    }

    private void kiemTraKetNoi() {
        try (Connection c = DatabaseConnection.getConnection(); Statement st = c.createStatement()) {
            int n = 0;
            try (ResultSet rs = st.executeQuery("SHOW TABLES")) {
                while (rs.next()) n++;
            }
            soBang.setText(n + " bảng");
            trangThaiKetNoi.setText("●  Đang kết nối tốt");
            trangThaiKetNoi.setForeground(LUC);
        } catch (Exception e) {
            soBang.setText("—");
            trangThaiKetNoi.setText("●  Mất kết nối");
            trangThaiKetNoi.setForeground(DO);
        }
    }

    private JPanel o(String nhanO, String giaTri) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(9999, 66));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(0, 0, 10, 0));
        p.add(nhan(nhanO.toUpperCase(), 10, Font.BOLD, CHU_MO), BorderLayout.NORTH);
        JTextField tf = new JTextField(giaTri) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x0A1C1F));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 11, 11);
                g2.setColor(isFocusOwner() ? NGOC : VIEN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 11, 11);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(0, 14, 0, 14));
        tf.setFont(new Font(FONT, Font.PLAIN, 13));
        tf.setForeground(CHU);
        tf.setCaretColor(NGOC);
        tf.setPreferredSize(new Dimension(200, 38));
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { tf.repaint(); }
            @Override public void focusLost(FocusEvent e) { tf.repaint(); }
        });
        p.add(tf, BorderLayout.CENTER);
        return p;
    }

    private JPanel dong(String k, String v) {
        return dong(k, nhan(v, 13, Font.BOLD, CHU));
    }

    private JPanel dong(String k, JLabel v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(9999, 32));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(4, 0, 4, 0));
        p.add(nhan(k, 12, Font.PLAIN, CHU_MO), BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }

    private JComponent congTac(String ten, boolean bat) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(9999, 46));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));
        p.add(nhan(ten, 13, Font.PLAIN, CHU), BorderLayout.WEST);
        p.add(new NutGat(bat), BorderLayout.EAST);
        return p;
    }

    private class NutGat extends JComponent {
        boolean bat;
        float pos;

        NutGat(boolean bat) {
            this.bat = bat;
            this.pos = bat ? 1f : 0f;
            setPreferredSize(new Dimension(52, 28));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    NutGat.this.bat = !NutGat.this.bat;
                    new javax.swing.Timer(14, ev -> {
                        float t = NutGat.this.bat ? 1f : 0f;
                        pos += (t - pos) * 0.3f;
                        if (Math.abs(pos - t) < 0.02f) {
                            pos = t;
                            ((javax.swing.Timer) ev.getSource()).stop();
                        }
                        repaint();
                    }).start();
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = 48, h = 26;
            g2.setColor(new Color(
                    (int) (0x1A + (0x2B - 0x1A) * pos),
                    (int) (0x3A + (0xE8 - 0x3A) * pos),
                    (int) (0x42 + (0xC8 - 0x42) * pos)));
            g2.fillRoundRect(0, 1, w, h, h, h);
            g2.setColor(pos > 0.5f ? new Color(0x03211E) : CHU_MO);
            g2.fillOval((int) (3 + pos * (w - 24)), 4, 20, 20);
            g2.dispose();
        }
    }

    private class Khoi extends JPanel {
        Khoi() {
            setOpaque(false);
            setBorder(new EmptyBorder(22, 24, 22, 24));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0, 0, 0, 26));
            g2.fillRoundRect(2, 4, w - 4, h - 4, 18, 18);
            g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, h, KHOI));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 18, 18);
            g2.setColor(VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel tieuDe(String a, String b) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setMaximumSize(new Dimension(9999, 50));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(nhan(a, 16, Font.BOLD, CHU));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(nhan(b, 11, Font.PLAIN, CHU_MO));
        return p;
    }

    private class Nut extends JButton {
        boolean chinh;
        Nut(String s, boolean chinh) {
            super(s);
            this.chinh = chinh;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 13));
            setMaximumSize(new Dimension(9999, 44));
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 44, 44));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover();
            if (chinh) {
                g2.setPaint(new GradientPaint(0, 0, NGOC, w, h, NGOC_MO));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(new Color(0x03211E));
            } else {
                g2.setColor(over ? new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 30) : KHOI);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
                g2.setColor(over ? NGOC : VIEN);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
                g2.setColor(over ? NGOC : CHU);
            }
            g2.setFont(getFont());
            g2.drawString(getText(), (w - g2.getFontMetrics().stringWidth(getText())) / 2, h / 2 + 5);
            g2.dispose();
        }
    }

    static JLabel nhan(String s, int co, int kieu, Color m) {
        JLabel l = new JLabel(s);
        l.setFont(new Font(FONT, kieu, co));
        l.setForeground(m);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static String font() {
        String[] muon = {"Segoe UI", "Roboto", "Noto Sans", "DejaVu Sans", "Tahoma", "Arial"};
        java.util.List<String> co = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String m : muon) if (co.contains(m)) return m;
        return "SansSerif";
    }
}