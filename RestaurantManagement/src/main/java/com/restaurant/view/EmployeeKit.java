package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public final class EmployeeKit {

    private EmployeeKit() { }

    public static final Color NEN      = new Color(0xF7F4ED);
    public static final Color NEN_SAU  = new Color(0xFBF9F4);
    public static final Color LOP      = new Color(0xFFFFFF);
    public static final Color LOP_CAO  = new Color(0xFFFDF9);
    public static final Color LINE     = new Color(0xE6E0D3);
    public static final Color LINE_RO  = new Color(0xD2C9B6);

    public static final Color CHU      = new Color(0x23201B);
    public static final Color CHU_PHU  = new Color(0x6E675B);
    public static final Color CHU_MO   = new Color(0x9C9385);

    public static final Color VANG     = new Color(0xA8802A);
    public static final Color VANG_S   = new Color(0xC79A38);
    public static final Color VANG_T   = new Color(0x7A5C16);

    public static final Color LUC      = new Color(0x2F7D5B);
    public static final Color CAM      = new Color(0xB0741C);
    public static final Color DO       = new Color(0xB03A2E);
    public static final Color LAM      = new Color(0x2C5F94);
    public static final Color TIM      = new Color(0x6B4E9B);

    public static final String SANS = chon(new String[]{
            "Segoe UI"});
    public static final String SERIF = chon(new String[]{
            "Segoe UI"});
    public static final String MONO = chon(new String[]{
            "Segoe UI"});

    public static Font sans(int co, int kieu) { return new Font(SANS, kieu, co); }
    public static Font serif(int co) { return new Font(SERIF, Font.BOLD, co); }
    public static Font mono(int co, int kieu) { return new Font(MONO, kieu, co); }

    public static Color mo(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public static Color pha(Color a, Color b, float t) {
        t = Math.max(0, Math.min(1, t));
        return new Color((int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    public static JLabel chu(String s, int co, int kieu, Color m) {
        JLabel l = new JLabel(s);
        l.setFont(sans(co, kieu));
        l.setForeground(m);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    public static String cat(Graphics2D g2, String s, int rong) {
        if (s == null) return "";
        FontMetrics fm = g2.getFontMetrics();
        if (fm.stringWidth(s) <= rong) return s;
        while (fm.stringWidth(s + "…") > rong && s.length() > 1) s = s.substring(0, s.length() - 1);
        return s + "…";
    }

    public static String tien(double v) {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(java.util.Locale.US);
        s.setGroupingSeparator('.');
        return new java.text.DecimalFormat("#,##0", s).format(v) + " đ";
    }

    private static String chon(String[] muon) {
        java.util.List<String> co = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String m : muon) if (co.contains(m)) return m;
        return "SansSerif";
    }

    public static class Khung extends JPanel {
        public int bo = 4;
        public boolean vien = true;
        public Color nen = LOP;

        public Khung() { this(new BorderLayout()); }

        public Khung(LayoutManager lm) {
            super(lm);
            setOpaque(false);
            setBorder(new EmptyBorder(22, 24, 22, 24));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(nen);
            g2.fillRoundRect(0, 0, w - 1, h - 1, bo, bo);
            if (vien) {
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, w - 1, h - 1, bo, bo);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JPanel tieuDe(String t, String phu, JComponent phai) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(LINE);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel t1 = new JPanel();
        t1.setOpaque(false);
        t1.setLayout(new BoxLayout(t1, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(t);
        l.setFont(serif(21));
        l.setForeground(CHU);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        t1.add(l);
        if (phu != null && !phu.isEmpty()) {
            t1.add(Box.createRigidArea(new Dimension(0, 4)));
            t1.add(chu(phu, 11, Font.PLAIN, CHU_MO));
        }
        p.add(t1, BorderLayout.WEST);
        if (phai != null) p.add(phai, BorderLayout.EAST);
        return p;
    }

    public static void nhanTrangThai(Graphics2D g2, String s, int x, int y, Color m) {
        g2.setFont(sans(10, Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(s) + 26, h = 22;
        g2.setColor(mo(m, 26));
        g2.fillRoundRect(x, y, w, h, 3, 3);
        g2.setColor(mo(m, 90));
        g2.drawRoundRect(x, y, w, h, 3, 3);
        g2.setColor(m);
        g2.fillOval(x + 9, y + h / 2 - 3, 6, 6);
        g2.drawString(s, x + 20, y + 15);
    }

    public static int rongNhan(Graphics2D g2, String s) {
        return g2.getFontMetrics(sans(10, Font.BOLD)).stringWidth(s) + 26;
    }

    public static class Nut extends JButton {
        public static final int VANG_DAC = 0, VIEN_MANH = 1, CHU_KHONG = 2, NGUY_HIEM = 3;
        private final int kieu;
        private String icon;
        private float hv = 0;

        public Nut(String t, int kieu) {
            super(t);
            this.kieu = kieu;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(sans(12, Font.BOLD));
            doKichThuoc();
            Timer tm = new Timer(15, null);
            tm.addActionListener(e -> {
                float d = getModel().isRollover() ? 1f : 0f;
                hv += (d - hv) * .3f;
                if (Math.abs(hv - d) < .02f) { hv = d; tm.stop(); }
                repaint();
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { tm.start(); }
                @Override public void mouseExited(MouseEvent e) { tm.start(); }
            });
        }

        public Nut icon(String k) { icon = k; doKichThuoc(); return this; }

        private void doKichThuoc() {
            int w = getFontMetrics(sans(12, Font.BOLD)).stringWidth(getText()) + 34 + (icon != null ? 22 : 0);
            setPreferredSize(new Dimension(w, 36));
            setMaximumSize(new Dimension(9999, 36));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean nhan = getModel().isPressed();
            Color ct;
            switch (kieu) {
                case VANG_DAC:
                    g2.setPaint(new GradientPaint(0, 0, nhan ? VANG : VANG_S, 0, h, nhan ? VANG_T : VANG));
                    g2.fillRoundRect(0, 0, w, h, 4, 4);
                    ct = Color.WHITE;
                    break;
                case NGUY_HIEM:
                    g2.setColor(mo(DO, nhan ? 60 : (int) (18 + 26 * hv)));
                    g2.fillRoundRect(0, 0, w, h, 4, 4);
                    g2.setColor(mo(DO, 120));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 4, 4);
                    ct = DO;
                    break;
                case CHU_KHONG:
                    if (hv > .02f) {
                        g2.setColor(mo(Color.BLACK, (int) (10 * hv)));
                        g2.fillRoundRect(0, 0, w, h, 4, 4);
                    }
                    ct = pha(CHU_PHU, CHU, hv);
                    break;
                default:
                    if (hv > .02f) {
                        g2.setColor(mo(VANG, (int) (16 * hv)));
                        g2.fillRoundRect(0, 0, w, h, 4, 4);
                    }
                    g2.setColor(hv > .3f ? VANG : LINE_RO);
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 4, 4);
                    ct = hv > .3f ? VANG_S : CHU;
            }
            FontMetrics fm = g2.getFontMetrics(getFont());
            int tw = fm.stringWidth(getText());
            int x = (w - (tw + (icon != null ? 22 : 0))) / 2;
            if (icon != null) { Ic.ve(g2, icon, x, (h - 16) / 2, 16, ct); x += 22; }
            g2.setFont(getFont());
            g2.setColor(ct);
            g2.drawString(getText(), x, h / 2 + fm.getAscent() / 2 - 2);
            g2.dispose();
        }
    }

    public static class O extends JTextField {
        private final String goiY;
        private final boolean lup;

        public O(String goiY, boolean lup) {
            this.goiY = goiY;
            this.lup = lup;
            setOpaque(false);
            setBorder(new EmptyBorder(0, lup ? 40 : 14, 0, 14));
            setFont(sans(13, Font.PLAIN));
            setForeground(CHU);
            setCaretColor(VANG);
            setPreferredSize(new Dimension(240, 40));
            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { repaint(); }
                @Override public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean fc = isFocusOwner();
            g2.setColor(LOP);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 4, 4);
            g2.setColor(fc ? VANG : LINE_RO);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 4, 4);
            if (fc) {
                g2.setColor(VANG);
                g2.fillRect(0, h - 2, w, 2);
            }
            if (lup) Ic.ve(g2, "tim", 14, h / 2 - 8, 17, fc ? VANG : CHU_MO);
            g2.dispose();
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setFont(sans(13, Font.PLAIN));
                g3.setColor(CHU_MO);
                g3.drawString(goiY, lup ? 41 : 15, getHeight() / 2 + 5);
                g3.dispose();
            }
        }
    }

    public static JScrollPane cuon(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(24);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { thumbColor = LINE_RO; }
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
                g2.setColor(LINE_RO);
                g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 3, 3);
                g2.dispose();
            }
        });
        return sp;
    }

    public static void bao(Component nguon, String noiDung, Color mau) {
        Window w = SwingUtilities.getWindowAncestor(nguon);
        if (w == null || !w.isShowing()) return;
        JWindow tb = new JWindow(w);
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int wd = getWidth(), h = getHeight();
                g2.setColor(LOP);
                g2.fillRoundRect(0, 0, wd - 1, h - 1, 4, 4);
                g2.setColor(mo(mau, 140));
                g2.drawRoundRect(0, 0, wd - 1, h - 1, 4, 4);
                g2.setColor(mau);
                g2.fillRect(0, 0, 3, h);
                g2.setFont(sans(13, Font.BOLD));
                g2.setColor(CHU);
                g2.drawString(noiDung, 22, h / 2 + 5);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(60 + p.getFontMetrics(sans(13, Font.BOLD)).stringWidth(noiDung), 54));
        tb.setContentPane(p);
        tb.pack();
        try { tb.setBackground(new Color(0, 0, 0, 0)); } catch (Exception ignore) { }
        Point g = w.getLocationOnScreen();
        tb.setLocation(g.x + w.getWidth() - tb.getWidth() - 30, g.y + w.getHeight() - tb.getHeight() - 30);
        tb.setVisible(true);
        final float[] a = {0};
        Timer hien = new Timer(15, null);
        hien.addActionListener(e -> {
            a[0] += .14f;
            if (a[0] >= 1) {
                a[0] = 1;
                hien.stop();
                Timer cho = new Timer(2200, ev -> {
                    Timer an = new Timer(15, null);
                    an.addActionListener(e2 -> {
                        a[0] -= .1f;
                        if (a[0] <= 0) { an.stop(); tb.dispose(); }
                        else try { tb.setOpacity(a[0]); } catch (Exception ignore) { }
                    });
                    an.start();
                });
                cho.setRepeats(false);
                cho.start();
            }
            try { tb.setOpacity(Math.min(1f, a[0])); } catch (Exception ignore) { }
        });
        hien.start();
    }

    public static final class Ic {
        private Ic() { }

        public static void ve(Graphics gg, String k, int x, int y, int s, Color c) {
            Graphics2D g = (Graphics2D) gg.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate(x, y);
            g.scale(s / 24.0, s / 24.0);
            g.setColor(c);
            g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (k) {
                case "tongquan":
                    g.drawRect(3, 3, 8, 8); g.drawRect(13, 3, 8, 5);
                    g.drawRect(13, 10, 8, 11); g.drawRect(3, 13, 8, 8); break;
                case "ban":
                    g.drawOval(4, 6, 16, 11); g.drawLine(12, 17, 12, 21); g.drawLine(8, 21, 16, 21); break;
                case "don":
                    g.drawRect(5, 3, 14, 18); g.drawLine(9, 8, 15, 8);
                    g.drawLine(9, 12, 15, 12); g.drawLine(9, 16, 13, 16); break;
                case "bep":
                    g.draw(new Arc2D.Float(6, 5, 12, 12, 20, 140, Arc2D.OPEN));
                    g.drawLine(12, 2, 12, 7); g.draw(new Arc2D.Float(4, 12, 16, 10, 180, 180, Arc2D.OPEN));
                    g.drawLine(4, 20, 20, 20); break;
                case "tien":
                    g.drawRect(2, 6, 20, 12); g.drawOval(9, 9, 6, 6);
                    g.drawLine(5, 9, 5, 9); g.drawLine(19, 15, 19, 15); break;
                case "khach":
                    g.drawOval(8, 3, 8, 8); g.draw(new Arc2D.Float(3.5f, 13, 17, 16, 0, 180, Arc2D.OPEN)); break;
                case "taikhoan":
                    g.drawOval(7, 2, 10, 10); g.draw(new Arc2D.Float(2, 13, 20, 18, 0, 180, Arc2D.OPEN)); break;
                case "thoat":
                    g.draw(new Arc2D.Float(3, 3, 18, 18, 50, 260, Arc2D.OPEN)); g.drawLine(12, 2, 12, 11); break;
                case "tim":
                    g.drawOval(4, 4, 13, 13); g.drawLine(16, 16, 21, 21); break;
                case "chuong":
                    g.draw(new Arc2D.Float(5, 3, 14, 14, 0, 180, Arc2D.OPEN));
                    g.drawLine(5, 10, 5, 17); g.drawLine(19, 10, 19, 17);
                    g.drawLine(3, 17, 21, 17); g.drawLine(10, 20, 14, 20); break;
                case "dongho":
                    g.drawOval(3, 3, 18, 18); g.drawLine(12, 7, 12, 12); g.drawLine(12, 12, 16, 14); break;
                case "mui":
                    g.drawLine(9, 5, 16, 12); g.drawLine(16, 12, 9, 19); break;
                case "lam":
                    g.drawLine(5, 12, 10, 17); g.drawLine(10, 17, 19, 7); break;
                case "canhbao":
                    g.drawLine(12, 3, 22, 20); g.drawLine(22, 20, 2, 20); g.drawLine(2, 20, 12, 3);
                    g.drawLine(12, 9, 12, 14); g.fillOval(11, 16, 2, 2); break;
                default:
                    g.drawOval(4, 4, 16, 16);
            }
            g.dispose();
        }
    }
}
