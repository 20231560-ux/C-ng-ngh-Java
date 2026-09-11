package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BanHangPanel extends JPanel {

    static final Color NEN     = new Color(0x0A171B);
    static final Color KHOI    = new Color(0x0E2429);
    static final Color KHOI_2  = new Color(0x102C31);
    static final Color VIEN    = new Color(0x1A3A42);
    static final Color NGOC    = new Color(0x2BE8C8);
    static final Color NGOC_MO = new Color(0x14A88F);
    static final Color CHU     = new Color(0xE6F2F0);
    static final Color CHU_MO  = new Color(0x7E9A98);
    static final Color LUC     = new Color(0x2BE89A);
    static final Color CAM     = new Color(0xF0A22E);
    static final Color DO      = new Color(0xFF6B6B);
    static final Color LAM     = new Color(0x59B4FF);
    static final String FONT   = font();

    private final List<Mon> thucDon = new ArrayList<>();
    private final List<Dong> gio = new ArrayList<>();
    private final JPanel luoiMon = new JPanel(new GridLayout(0, 4, 14, 14));
    private final JPanel oNhom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private final JPanel dsGio = new JPanel();
    private final OTim tim = new OTim();
    private JComboBox<String> chonBan;
    private JLabel lbTam, lbVat, lbTong, lbSoMon;
    private String nhomChon = "Tất cả";

    public BanHangPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(16, 0));
        napThucDon();

        JPanel trai = new JPanel(new BorderLayout(0, 14));
        trai.setOpaque(false);

        JPanel dau = new JPanel(new BorderLayout(12, 12));
        dau.setOpaque(false);
        dau.setPreferredSize(new Dimension(100, 106));
        JPanel hang = new JPanel(new BorderLayout(12, 0));
        hang.setOpaque(false);
        JPanel oBan = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        oBan.setOpaque(false);
        oBan.add(nhan("BÀN PHỤC VỤ", 11, Font.BOLD, CHU_MO));
        chonBan = new JComboBox<>();
        napBan();
        chonBan.setFont(new Font(FONT, Font.BOLD, 13));
        chonBan.setPreferredSize(new Dimension(150, 38));
        chonBan.setBackground(KHOI);
        chonBan.setForeground(CHU);
        oBan.add(chonBan);
        hang.add(oBan, BorderLayout.WEST);
        tim.setPreferredSize(new Dimension(280, 40));
        tim.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { veMon(); }
        });
        JPanel oPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        oPhai.setOpaque(false);
        oPhai.add(tim);
        hang.add(oPhai, BorderLayout.EAST);
        dau.add(hang, BorderLayout.NORTH);
        oNhom.setOpaque(false);
        dau.add(oNhom, BorderLayout.SOUTH);
        trai.add(dau, BorderLayout.NORTH);

        luoiMon.setOpaque(false);
        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(luoiMon, BorderLayout.NORTH);
        trai.add(cuon(giu), BorderLayout.CENTER);
        add(trai, BorderLayout.CENTER);

        add(bangGio(), BorderLayout.EAST);
        veNhom();
        veMon();
        veGio();
    }

    private void napThucDon() {
        if (napMonTuCSDL()) return;
        Object[][] d = {
                {"Phở bò đặc biệt", "Món chính", 65000d}, {"Gà rán giòn", "Món chính", 75000d},
                {"Burger bò phô mai", "Món chính", 85000d}, {"Coca Cola", "Đồ uống", 15000d},
                {"Trà đào cam sả", "Đồ uống", 35000d}, {"Chè khúc bạch", "Tráng miệng", 30000d},
                {"Combo gia đình", "Combo", 250000d}
        };
        for (Object[] x : d) thucDon.add(new Mon((String) x[0], (String) x[1], (Double) x[2]));
    }

    private boolean napMonTuCSDL() {
        String sql = "SELECT m.ten_mon, m.gia_ban, d.ten_danh_muc "
                + "FROM mon_an m LEFT JOIN danh_muc_mon d ON d.ma_danh_muc = m.ma_danh_muc "
                + "WHERE m.trang_thai = 1 ORDER BY m.ma_danh_muc, m.ma_mon";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nhom = rs.getString(3);
                thucDon.add(new Mon(rs.getString(1), nhom == null ? "Khác" : nhom, rs.getDouble(2)));
            }
            return !thucDon.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void napBan() {
        chonBan.removeAllItems();
        String sql = "SELECT ten_ban FROM ban_an ORDER BY ma_ban";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) chonBan.addItem(rs.getString(1));
        } catch (Exception ignore) {
        }
        if (chonBan.getItemCount() == 0)
            for (int i = 1; i <= 10; i++) chonBan.addItem("Bàn " + String.format("%02d", i));
    }

    private void veNhom() {
        oNhom.removeAll();
        Map<String, Integer> dem = new LinkedHashMap<>();
        dem.put("Tất cả", thucDon.size());
        for (Mon m : thucDon) dem.merge(m.nhom, 1, Integer::sum);
        for (Map.Entry<String, Integer> e : dem.entrySet()) {
            Chip c = new Chip(e.getKey() + "  " + e.getValue(), e.getKey().equals(nhomChon));
            c.addActionListener(a -> { nhomChon = e.getKey(); veNhom(); veMon(); });
            oNhom.add(c);
        }
        oNhom.revalidate();
        oNhom.repaint();
    }

    private void veMon() {
        String q = tim.getText().trim().toLowerCase();
        luoiMon.removeAll();
        for (Mon m : thucDon) {
            if (!nhomChon.equals("Tất cả") && !m.nhom.equals(nhomChon)) continue;
            if (!q.isEmpty() && !m.ten.toLowerCase().contains(q)) continue;
            luoiMon.add(new TheMon(m));
        }
        luoiMon.revalidate();
        luoiMon.repaint();
    }

    private JPanel bangGio() {
        JPanel p = new JPanel(new BorderLayout(0, 12)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 26));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 18, 18);
                g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, getHeight(), KHOI));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.setColor(VIEN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(430, 100));
        p.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel dau = new JPanel(new BorderLayout());
        dau.setOpaque(false);
        JPanel tt = new JPanel();
        tt.setOpaque(false);
        tt.setLayout(new BoxLayout(tt, BoxLayout.Y_AXIS));
        tt.add(nhan("Phiếu gọi món", 17, Font.BOLD, CHU));
        lbSoMon = nhan("Chưa chọn món nào", 11, Font.PLAIN, CHU_MO);
        tt.add(Box.createRigidArea(new Dimension(0, 4)));
        tt.add(lbSoMon);
        dau.add(tt, BorderLayout.WEST);
        Nut xoa = new Nut("Xoá hết", false, DO);
        xoa.addActionListener(e -> { gio.clear(); veGio(); });
        dau.add(xoa, BorderLayout.EAST);
        p.add(dau, BorderLayout.NORTH);

        dsGio.setOpaque(false);
        dsGio.setLayout(new BoxLayout(dsGio, BoxLayout.Y_AXIS));
        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(dsGio, BorderLayout.NORTH);
        p.add(cuon(giu), BorderLayout.CENTER);

        JPanel duoi = new JPanel();
        duoi.setOpaque(false);
        duoi.setLayout(new BoxLayout(duoi, BoxLayout.Y_AXIS));
        lbTam = nhan("0 đ", 13, Font.BOLD, CHU);
        lbVat = nhan("0 đ", 13, Font.BOLD, CHU_MO);
        lbTong = nhan("0 đ", 26, Font.BOLD, NGOC);
        duoi.add(dong("Tạm tính", lbTam));
        duoi.add(dong("Thuế VAT (8%)", lbVat));
        duoi.add(Box.createRigidArea(new Dimension(0, 8)));
        JPanel vach = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(VIEN);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        vach.setOpaque(false);
        vach.setMaximumSize(new Dimension(9999, 1));
        vach.setPreferredSize(new Dimension(100, 1));
        duoi.add(vach);
        duoi.add(Box.createRigidArea(new Dimension(0, 10)));
        duoi.add(dong("TỔNG CỘNG", lbTong));
        duoi.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel h1 = new JPanel(new GridLayout(1, 2, 10, 0));
        h1.setOpaque(false);
        h1.setMaximumSize(new Dimension(9999, 42));
        Nut bep = new Nut("Gửi bếp", false, CAM);
        Nut giu2 = new Nut("Lưu tạm", false, LAM);
        bep.addActionListener(e -> baoTin("Đã gửi " + gio.size() + " món xuống bếp cho "
                + chonBan.getSelectedItem() + "."));
        giu2.addActionListener(e -> baoTin("Đã lưu phiếu tạm cho " + chonBan.getSelectedItem() + "."));
        h1.add(bep);
        h1.add(giu2);
        duoi.add(h1);
        duoi.add(Box.createRigidArea(new Dimension(0, 10)));
        Nut tt2 = new Nut("THANH TOÁN", true, NGOC);
        tt2.setMaximumSize(new Dimension(9999, 50));
        tt2.addActionListener(e -> thanhToan());
        duoi.add(tt2);
        p.add(duoi, BorderLayout.SOUTH);
        return p;
    }

    private JPanel dong(String k, JLabel v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(9999, v.getFont().getSize() + 14));
        p.setBorder(new EmptyBorder(3, 0, 3, 0));
        p.add(nhan(k, 12, Font.BOLD, CHU_MO), BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }

    private void them(Mon m) {
        for (Dong d : gio) {
            if (d.mon == m) { d.sl++; veGio(); return; }
        }
        gio.add(new Dong(m));
        veGio();
    }

    private void veGio() {
        dsGio.removeAll();
        double tam = 0;
        int n = 0;
        for (Dong d : gio) { tam += d.mon.gia * d.sl; n += d.sl; }
        if (gio.isEmpty()) {
            JLabel l = nhan("<html><div style='text-align:center;color:#53706F'>Chưa có món nào<br>"
                    + "Nhấn vào món bên trái để thêm</div></html>", 12, Font.PLAIN, CHU_MO);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            l.setBorder(new EmptyBorder(50, 0, 0, 0));
            dsGio.add(l);
        }
        for (Dong d : gio) dsGio.add(new DongGio(d));
        double vat = Math.round(tam * 0.08);
        lbSoMon.setText(gio.isEmpty() ? "Chưa chọn món nào" : n + " món • " + gio.size() + " loại");
        lbTam.setText(tienVN(tam));
        lbVat.setText(tienVN(vat));
        lbTong.setText(tienVN(tam + vat));
        dsGio.revalidate();
        dsGio.repaint();
    }

    private void thanhToan() {
        if (gio.isEmpty()) { baoTin("Phiếu đang trống, chưa thể thanh toán."); return; }
        double tam = 0;
        for (Dong d : gio) tam += d.mon.gia * d.sl;
        double tong = tam + Math.round(tam * 0.08);
        int r = JOptionPane.showConfirmDialog(this,
                "Thanh toán " + chonBan.getSelectedItem() + "\nTổng cộng: " + tienVN(tong),
                "Xác nhận thanh toán", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) return;
        gio.clear();
        veGio();
        baoTin("Thanh toán thành công " + tienVN(tong));
    }

    private void baoTin(String s) {
        JOptionPane.showMessageDialog(this, s, "NOVA RESTAURANT", JOptionPane.INFORMATION_MESSAGE);
    }

    private class TheMon extends JComponent {
        Mon m;
        boolean hover;
        TheMon(Mon m) {
            this.m = m;
            setPreferredSize(new Dimension(160, 138));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText("<html><b>" + m.ten + "</b><br>" + tienVN(m.gia) + "</html>");
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { them(m); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color c = mauNhom(m.nhom);
            g2.setColor(new Color(0, 0, 0, 26));
            g2.fillRoundRect(2, 4, w - 4, h - 4, 16, 16);
            g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, h, KHOI));
            g2.fillRoundRect(0, 0, w - 1, h - 6, 16, 16);
            g2.setStroke(new BasicStroke(hover ? 1.9f : 1.1f));
            g2.setColor(hover ? c : VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 6, 16, 16);

            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 38));
            g2.fillRoundRect(14, 14, w - 28, 26, 9, 9);
            g2.setFont(new Font(FONT, Font.BOLD, 10));
            g2.setColor(c);
            g2.drawString(m.nhom.toUpperCase(), 22, 31);

            g2.setFont(new Font(FONT, Font.BOLD, 12));
            g2.setColor(CHU);
            String t = m.ten;
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(t) > w - 28) {
                while (fm.stringWidth(t + "…") > w - 28 && t.length() > 2) t = t.substring(0, t.length() - 1);
                t += "…";
            }
            g2.drawString(t, 15, 66);

            g2.setFont(new Font(FONT, Font.BOLD, 14));
            g2.setColor(LUC);
            g2.drawString(tienVN(m.gia), 15, 96);

            if (hover) {
                g2.setPaint(new GradientPaint(0, 0, NGOC, w, h, NGOC_MO));
                g2.fillRoundRect(w - 44, h - 48, 30, 30, 10, 10);
                g2.setColor(new Color(0x03211E));
                g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(w - 29, h - 40, w - 29, h - 26);
                g2.drawLine(w - 36, h - 33, w - 22, h - 33);
            }
            g2.dispose();
        }
    }

    private class DongGio extends JPanel {
        DongGio(Dong d) {
            setOpaque(false);
            setLayout(new BorderLayout(8, 0));
            setBorder(new EmptyBorder(10, 14, 10, 12));
            setMaximumSize(new Dimension(9999, 74));
            setPreferredSize(new Dimension(380, 74));

            JPanel t = new JPanel();
            t.setOpaque(false);
            t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
            t.add(nhan(d.mon.ten, 13, Font.BOLD, CHU));
            t.add(Box.createRigidArea(new Dimension(0, 4)));
            t.add(nhan(tienVN(d.mon.gia) + (d.ghiChu.isEmpty() ? "" : "  •  " + d.ghiChu), 11, Font.PLAIN, CHU_MO));
            add(t, BorderLayout.CENTER);

            JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            p.setOpaque(false);
            JLabel tien = nhan(tienVN(d.mon.gia * d.sl), 13, Font.BOLD, LUC);
            NutNho tru = new NutNho("−");
            JLabel sl = nhan(String.valueOf(d.sl), 13, Font.BOLD, CHU);
            sl.setPreferredSize(new Dimension(22, 24));
            sl.setHorizontalAlignment(SwingConstants.CENTER);
            NutNho cong = new NutNho("+");
            NutNho gc = new NutNho("✎");
            tru.addActionListener(e -> { d.sl--; if (d.sl <= 0) gio.remove(d); veGio(); });
            cong.addActionListener(e -> { d.sl++; veGio(); });
            gc.addActionListener(e -> {
                String s = JOptionPane.showInputDialog(this, "Ghi chú cho \"" + d.mon.ten + "\":", d.ghiChu);
                if (s != null) { d.ghiChu = s.trim(); veGio(); }
            });
            p.add(tien); p.add(tru); p.add(sl); p.add(cong); p.add(gc);
            add(p, BorderLayout.EAST);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 10));
            g2.fillRoundRect(0, 2, getWidth(), getHeight() - 8, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class NutNho extends JButton {
        NutNho(String s) {
            super(s);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 14));
            setPreferredSize(new Dimension(30, 30));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean over = getModel().isRollover();
            g2.setColor(over ? new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 46) : KHOI);
            g2.fillRoundRect(0, 0, 29, 29, 9, 9);
            g2.setColor(over ? NGOC : VIEN);
            g2.drawRoundRect(0, 0, 29, 29, 9, 9);
            g2.setFont(getFont());
            g2.setColor(over ? NGOC : CHU);
            g2.drawString(getText(), 15 - g2.getFontMetrics().stringWidth(getText()) / 2, 20);
            g2.dispose();
        }
    }

    private class Chip extends JButton {
        boolean chon;
        Chip(String s, boolean chon) {
            super(s);
            this.chon = chon;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 12));
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 32, 38));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover();
            g2.setColor(chon ? new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 44) : KHOI);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(chon || over ? NGOC : VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setFont(getFont());
            g2.setColor(chon ? NGOC : CHU_MO);
            g2.drawString(getText(), (w - g2.getFontMetrics().stringWidth(getText())) / 2, h / 2 + 5);
            g2.dispose();
        }
    }

    private class Nut extends JButton {
        boolean chinh;
        Color mau;
        Nut(String s, boolean chinh, Color mau) {
            super(s);
            this.chinh = chinh;
            this.mau = mau;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 13));
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 40, 42));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover();
            if (chinh) {
                if (over) {
                    g2.setColor(new Color(mau.getRed(), mau.getGreen(), mau.getBlue(), 70));
                    g2.fillRoundRect(-3, -2, w + 6, h + 4, 15, 15);
                }
                g2.setPaint(new GradientPaint(0, 0, NGOC, w, h, NGOC_MO));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(new Color(0x03211E));
            } else {
                g2.setColor(over ? new Color(mau.getRed(), mau.getGreen(), mau.getBlue(), 34) : KHOI);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
                g2.setColor(over ? mau : VIEN);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
                g2.setColor(over ? mau : CHU);
            }
            g2.setFont(getFont());
            g2.drawString(getText(), (w - g2.getFontMetrics().stringWidth(getText())) / 2, h / 2 + 5);
            g2.dispose();
        }
    }

    private class OTim extends JTextField {
        OTim() {
            setOpaque(false);
            setBorder(new EmptyBorder(0, 42, 0, 14));
            setFont(new Font(FONT, Font.PLAIN, 13));
            setForeground(CHU);
            setCaretColor(NGOC);
            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { repaint(); }
                @Override public void focusLost(FocusEvent e) { repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(KHOI);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(isFocusOwner() ? NGOC : VIEN);
            g2.setStroke(new BasicStroke(isFocusOwner() ? 1.6f : 1.1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(isFocusOwner() ? NGOC : CHU_MO);
            g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(16, h / 2 - 8, 11, 11);
            g2.drawLine(26, h / 2 + 3, 30, h / 2 + 7);
            g2.dispose();
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g3.setFont(new Font(FONT, Font.PLAIN, 13));
                g3.setColor(new Color(0x53706F));
                g3.drawString("Tìm món ăn, đồ uống…", 44, getHeight() / 2 + 5);
                g3.dispose();
            }
        }
    }

    static Color mauNhom(String n) {
        if (n == null) return LUC;
        switch (n) {
            case "Khai vị": return LAM;
            case "Món chính": return NGOC;
            case "Lẩu - Nướng": return CAM;
            case "Combo": return new Color(0xFF9E6D);
            case "Tráng miệng": return new Color(0xC77DFF);
            default: return LUC;
        }
    }

    static JScrollPane cuon(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { thumbColor = NGOC; }
            @Override protected JButton createDecreaseButton(int o) { return z(); }
            @Override protected JButton createIncreaseButton(int o) { return z(); }
            private JButton z() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
            @Override protected void paintTrack(Graphics g, JComponent c2, Rectangle r) { }
            @Override protected void paintThumb(Graphics g, JComponent c2, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 90));
                g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
                g2.dispose();
            }
        });
        return sp;
    }

    static JLabel nhan(String s, int co, int kieu, Color m) {
        JLabel l = new JLabel(s);
        l.setFont(new Font(FONT, kieu, co));
        l.setForeground(m);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static class Mon {
        String ten, nhom;
        double gia;
        Mon(String t, String n, double g) { ten = t; nhom = n; gia = g; }
    }

    static class Dong {
        Mon mon;
        int sl = 1;
        String ghiChu = "";
        Dong(Mon m) { mon = m; }
    }

    static String tienVN(double v) {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(java.util.Locale.US);
        s.setGroupingSeparator('.');
        return new java.text.DecimalFormat("#,##0", s).format(v) + " đ";
    }

    static String font() {
        String[] muon = {"Segoe UI", "Roboto", "Noto Sans", "DejaVu Sans", "Tahoma", "Arial"};
        java.util.List<String> co = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String m : muon) if (co.contains(m)) return m;
        return "SansSerif";
    }
}