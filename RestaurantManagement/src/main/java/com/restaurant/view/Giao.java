package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

/** Bộ giao diện dùng chung — tông SAVORÉ: navy sâu + vàng đồng. */
public final class Giao {

    public static final Color NEN     = new Color(0x0B1220);
    public static final Color SIDE    = new Color(0x0E1626);
    public static final Color SIDE2   = new Color(0x0A111E);
    public static final Color THE     = new Color(0x141D30);
    public static final Color THE2    = new Color(0x1A2438);
    public static final Color VIEN    = new Color(0x22304A);
    public static final Color VIEN2   = new Color(0x2E3E5C);

    public static final Color CHU     = new Color(0xEAF0F8);
    public static final Color CHU_PHU = new Color(0x93A4BE);
    public static final Color CHU_MO  = new Color(0x64748B);

    public static final Color VANG    = new Color(0xD4A650);
    public static final Color VANG_S  = new Color(0xF0C674);
    public static final Color VANG_T  = new Color(0x9A7530);

    public static final Color LUC     = new Color(0x22C55E);
    public static final Color LAM     = new Color(0x3B82F6);
    public static final Color CAM     = new Color(0xF59E0B);
    public static final Color DO      = new Color(0xEF4444);
    public static final Color TIM     = new Color(0xA855F7);
    public static final Color HONG    = new Color(0xEC4899);

    // Compatibility aliases for legacy panels
    public static final Color CHINH      = VANG;
    public static final Color CHINH_DAM  = VANG_T;
    public static final Color CHINH_NHAT = new Color(0xFAF3E8);
    public static final Color VIEN_DAM   = VIEN2;
    public static final Color CHU_NHAT   = CHU_MO;
    public static Color nhatHon(Color c) { return pha(c, Color.WHITE, 0.25f); }
    public static void veHuyHieu(Graphics2D g, String s, int x, int y, Color c) { huyHieu(g, s, x, y, c); }

    public static final String FONT = chonFont();
    public static final String MONO = chonMono();

    private Giao() { }

    public static Font f(int co, int kieu) { return new Font(FONT, kieu, co); }
    public static Font m(int co, int kieu) { return new Font(MONO, kieu, co); }

    public static JLabel chu(String s, int co, int kieu, Color c) {
        JLabel l = new JLabel(s);
        l.setFont(f(co, kieu));
        l.setForeground(c);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    public static Color mo(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public static Color pha(Color a, Color b, float t) {
        t = Math.max(0, Math.min(1, t));
        return new Color((int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    /* ==================== THẺ ==================== */
    public static class The extends JPanel {
        public int bo = 16;
        public Color nen = THE;
        public boolean vien = true;

        public The() { this(new BorderLayout()); }

        public The(LayoutManager lm) {
            super(lm);
            setOpaque(false);
            setBorder(new EmptyBorder(20, 22, 20, 22));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0, 0, 0, 42));
            g2.fillRoundRect(0, 3, w, h - 1, bo, bo);
            g2.setPaint(new GradientPaint(0, 0, THE2, 0, h, nen));
            g2.fillRoundRect(0, 0, w - 1, h - 4, bo, bo);
            if (vien) {
                g2.setColor(VIEN);
                g2.drawRoundRect(0, 0, w - 1, h - 4, bo, bo);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /* ==================== NÚT ==================== */
    public static class Nut extends JButton {
        public static final int CHINH = 0, PHU = 1, NGUY = 2, PHANG = 3;
        public static final int CHINH_NUT = CHINH;
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
            setFont(f(13, Font.BOLD));
            co();
            Timer tm = new Timer(15, null);
            tm.addActionListener(e -> {
                float d = getModel().isRollover() ? 1f : 0f;
                hv += (d - hv) * 0.3f;
                if (Math.abs(hv - d) < .02f) { hv = d; tm.stop(); }
                repaint();
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { tm.start(); }
                @Override public void mouseExited(MouseEvent e) { tm.start(); }
            });
        }

        public Nut icon(String k) { icon = k; co(); return this; }

        private void co() {
            int w = getFontMetrics(f(13, Font.BOLD)).stringWidth(getText()) + 36 + (icon != null ? 24 : 0);
            setPreferredSize(new Dimension(w, 40));
            setMaximumSize(new Dimension(9999, 40));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean nhan = getModel().isPressed();
            Color ct;
            switch (kieu) {
                case CHINH:
                    if (hv > .02f) {
                        g2.setColor(mo(VANG, (int) (60 * hv)));
                        g2.fillRoundRect(-3, -1, w + 6, h + 4, 14, 14);
                    }
                    g2.setPaint(new GradientPaint(0, 0, nhan ? VANG_T : VANG_S, 0, h, nhan ? VANG_T : VANG));
                    g2.fillRoundRect(0, 0, w, h, 11, 11);
                    ct = new Color(0x1A1206);
                    break;
                case NGUY:
                    g2.setColor(mo(DO, nhan ? 60 : (int) (26 + 22 * hv)));
                    g2.fillRoundRect(0, 0, w, h, 11, 11);
                    g2.setColor(mo(DO, 130));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 11, 11);
                    ct = DO;
                    break;
                case PHANG:
                    if (hv > .02f) {
                        g2.setColor(mo(Color.WHITE, (int) (14 * hv)));
                        g2.fillRoundRect(0, 0, w, h, 11, 11);
                    }
                    ct = CHU_PHU;
                    break;
                default:
                    g2.setColor(pha(THE2, VIEN, hv * .7f));
                    g2.fillRoundRect(0, 0, w, h, 11, 11);
                    g2.setColor(hv > .3f ? VIEN2 : VIEN);
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 11, 11);
                    ct = CHU;
            }
            FontMetrics fm = g2.getFontMetrics(getFont());
            int tw = fm.stringWidth(getText());
            int x = (w - (tw + (icon != null ? 24 : 0))) / 2;
            if (icon != null) { Ic.ve(g2, icon, x, (h - 18) / 2, 17, ct); x += 24; }
            g2.setFont(getFont());
            g2.setColor(ct);
            g2.drawString(getText(), x, h / 2 + fm.getAscent() / 2 - 3);
            g2.dispose();
        }
    }

    /* ==================== Ô NHẬP ==================== */
    public static class O extends JTextField {
        private final String goiY;
        private final boolean lup;

        public O(String goiY, boolean lup) {
            this.goiY = goiY;
            this.lup = lup;
            setOpaque(false);
            setBorder(new EmptyBorder(0, lup ? 42 : 15, 0, 15));
            setFont(f(13, Font.PLAIN));
            setForeground(CHU);
            setCaretColor(VANG);
            setPreferredSize(new Dimension(240, 42));
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
            g2.setColor(SIDE2);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 11, 11);
            if (fc) {
                g2.setColor(mo(VANG, 46));
                g2.setStroke(new BasicStroke(3.4f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, 11, 11);
            }
            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(fc ? VANG : VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 11, 11);
            if (lup) Ic.ve(g2, "tim", 15, h / 2 - 9, 18, fc ? VANG : CHU_MO);
            g2.dispose();
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setFont(f(13, Font.PLAIN));
                g3.setColor(CHU_MO);
                g3.drawString(goiY, lup ? 43 : 16, getHeight() / 2 + 5);
                g3.dispose();
            }
        }
    }

    /* ==================== HUY HIỆU ==================== */
    public static void huyHieu(Graphics2D g2, String s, int x, int y, Color c) {
        g2.setFont(f(11, Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(s) + 22, h = 24;
        g2.setColor(mo(c, 34));
        g2.fillRoundRect(x, y, w, h, 12, 12);
        g2.setColor(mo(c, 110));
        g2.drawRoundRect(x, y, w, h, 12, 12);
        g2.setColor(c);
        g2.drawString(s, x + 11, y + 16);
    }

    public static int rongHuyHieu(Graphics2D g2, String s) {
        return g2.getFontMetrics(f(11, Font.BOLD)).stringWidth(s) + 22;
    }

    /* ==================== CUỘN ==================== */
    public static class Cuon extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() { thumbColor = VIEN2; }
        @Override protected JButton createDecreaseButton(int o) { return z(); }
        @Override protected JButton createIncreaseButton(int o) { return z(); }
        private JButton z() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) { }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(VIEN2);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }

    public static JScrollPane cuon(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(24);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        sp.getVerticalScrollBar().setUI(new Cuon());
        sp.getHorizontalScrollBar().setUI(new Cuon());
        return sp;
    }

    /* ==================== THÔNG BÁO ==================== */
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
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(3, 5, wd - 5, h - 7, 14, 14);
                g2.setPaint(new GradientPaint(0, 0, THE2, 0, h, THE));
                g2.fillRoundRect(0, 0, wd - 6, h - 9, 14, 14);
                g2.setColor(mo(mau, 150));
                g2.drawRoundRect(0, 0, wd - 6, h - 9, 14, 14);
                g2.setColor(mo(mau, 40));
                g2.fillRoundRect(16, (h - 9) / 2 - 15, 30, 30, 10, 10);
                g2.setColor(mau);
                g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cy = (h - 9) / 2;
                g2.drawLine(23, cy, 28, cy + 5);
                g2.drawLine(28, cy + 5, 39, cy - 6);
                g2.setFont(f(13, Font.BOLD));
                g2.setColor(CHU);
                g2.drawString(noiDung, 58, cy + 5);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(96 + p.getFontMetrics(f(13, Font.BOLD)).stringWidth(noiDung), 62));
        tb.setContentPane(p);
        tb.pack();
        try { tb.setBackground(new Color(0, 0, 0, 0)); } catch (Exception ignore) { }
        Point g = w.getLocationOnScreen();
        tb.setLocation(g.x + w.getWidth() - tb.getWidth() - 32, g.y + w.getHeight() - tb.getHeight() - 32);
        tb.setVisible(true);
        final float[] a = {0};
        Timer hien = new Timer(15, null);
        hien.addActionListener(e -> {
            a[0] += .13f;
            if (a[0] >= 1) {
                a[0] = 1;
                hien.stop();
                Timer cho = new Timer(2300, ev -> {
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

    /* ==================== HỘP THOẠI ==================== */
    public static class HopThoai extends JDialog {
        private boolean dongY = false;

        public HopThoai(Component cha, String tieuDe, String moTa, JComponent than, String nhanOk, int kieuOk) {
            super(SwingUtilities.getWindowAncestor(cha), tieuDe, ModalityType.APPLICATION_MODAL);
            setUndecorated(true);
            JPanel goc = new JPanel(new BorderLayout(0, 18)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();
                    g2.setColor(new Color(0, 0, 0, 70));
                    g2.fillRoundRect(0, 4, w, h - 2, 18, 18);
                    g2.setPaint(new GradientPaint(0, 0, THE2, 0, h, THE));
                    g2.fillRoundRect(0, 0, w - 1, h - 5, 18, 18);
                    g2.setColor(VIEN2);
                    g2.drawRoundRect(0, 0, w - 1, h - 5, 18, 18);
                    g2.setPaint(new GradientPaint(26, 0, VANG, w - 26, 0, mo(VANG, 0)));
                    g2.fillRoundRect(26, 0, w - 52, 3, 3, 3);
                    g2.dispose();
                }
            };
            goc.setOpaque(false);
            goc.setBorder(new EmptyBorder(26, 28, 24, 28));

            JPanel dau = new JPanel();
            dau.setOpaque(false);
            dau.setLayout(new BoxLayout(dau, BoxLayout.Y_AXIS));
            dau.add(chu(tieuDe, 19, Font.BOLD, CHU));
            dau.add(Box.createRigidArea(new Dimension(0, 6)));
            dau.add(chu(moTa, 12, Font.PLAIN, CHU_PHU));
            goc.add(dau, BorderLayout.NORTH);
            goc.add(cuon(than), BorderLayout.CENTER);

            JPanel nut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0));
            nut.setOpaque(false);
            Nut huy = new Nut("Huỷ", Nut.PHU);
            Nut ok = new Nut(nhanOk, kieuOk);
            huy.addActionListener(e -> { dongY = false; dispose(); });
            ok.addActionListener(e -> { dongY = true; dispose(); });
            nut.add(huy);
            nut.add(ok);
            goc.add(nut, BorderLayout.SOUTH);

            setContentPane(goc);
            try { setBackground(new Color(0, 0, 0, 0)); } catch (Exception ignore) { }
            int c = Math.min(430, Math.max(80, than.getPreferredSize().height + 14));
            setSize(460, c + 190);
            setLocationRelativeTo(SwingUtilities.getWindowAncestor(cha));
            getRootPane().registerKeyboardAction(e -> { dongY = false; dispose(); },
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
            getRootPane().setDefaultButton(ok);
            MouseAdapter keo = new MouseAdapter() {
                Point d;
                @Override public void mousePressed(MouseEvent e) { d = e.getPoint(); }
                @Override public void mouseDragged(MouseEvent e) {
                    if (d == null) return;
                    Point p = getLocation();
                    setLocation(p.x + e.getX() - d.x, p.y + e.getY() - d.y);
                }
            };
            dau.addMouseListener(keo);
            dau.addMouseMotionListener(keo);
        }

        public boolean moVaCho() { setVisible(true); return dongY; }
    }

    /* ==================== TIỆN ÍCH ==================== */
    public static String tien(double v) {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(java.util.Locale.US);
        s.setGroupingSeparator('.');
        return new java.text.DecimalFormat("#,##0", s).format(v) + " đ";
    }

    public static String so(double v) {
        java.text.DecimalFormatSymbols s = new java.text.DecimalFormatSymbols(java.util.Locale.US);
        s.setGroupingSeparator('.');
        return new java.text.DecimalFormat("#,##0.##", s).format(v);
    }

    public static String catChu(Graphics2D g2, String s, int rong) {
        FontMetrics fm = g2.getFontMetrics();
        if (s == null) return "";
        if (fm.stringWidth(s) <= rong) return s;
        while (fm.stringWidth(s + "…") > rong && s.length() > 1) s = s.substring(0, s.length() - 1);
        return s + "…";
    }

    private static String chonFont() {
        String[] w = {"Inter", "Segoe UI Variable", "Segoe UI", "Roboto", "Noto Sans", "DejaVu Sans", "Tahoma", "Arial"};
        java.util.List<String> co = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String x : w) if (co.contains(x)) return x;
        return "SansSerif";
    }

    private static String chonMono() {
        String[] w = {"JetBrains Mono", "IBM Plex Mono", "Consolas", "DejaVu Sans Mono", "Courier New"};
        java.util.List<String> co = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String x : w) if (co.contains(x)) return x;
        return "Monospaced";
    }

    /* ==================== ICON ==================== */
    public static final class Ic {
        private Ic() { }

        public static void ve(Graphics gg, String k, int x, int y, int s, Color c) {
            Graphics2D g = (Graphics2D) gg.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate(x, y);
            g.scale(s / 24.0, s / 24.0);
            g.setColor(c);
            g.setStroke(new BasicStroke(1.75f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (k) {
                case "trangchu":
                    g.drawLine(3, 11, 12, 3); g.drawLine(12, 3, 21, 11);
                    g.drawRect(6, 11, 12, 10); g.drawRect(10, 15, 4, 6); break;
                case "datban":
                    g.drawRoundRect(3, 5, 18, 16, 3, 3); g.drawLine(3, 10, 21, 10);
                    g.drawLine(8, 2, 8, 6); g.drawLine(16, 2, 16, 6);
                    g.fillOval(7, 13, 3, 3); g.fillOval(14, 13, 3, 3); break;
                case "pos":
                    g.drawRoundRect(3, 3, 18, 18, 3, 3); g.drawLine(3, 9, 21, 9);
                    g.drawLine(7, 14, 10, 14); g.drawLine(7, 18, 14, 18); break;
                case "mon":
                    g.drawOval(3, 8, 12, 12); g.drawLine(19, 3, 19, 21);
                    g.drawLine(17, 3, 17, 9); g.drawLine(21, 3, 21, 9); break;
                case "donhang":
                    g.drawRoundRect(5, 2, 14, 20, 3, 3); g.drawLine(9, 8, 15, 8);
                    g.drawLine(9, 12, 15, 12); g.drawLine(9, 16, 13, 16); break;
                case "khach":
                    g.drawOval(8, 3, 8, 8); g.draw(new Arc2D.Float(3.5f, 13, 17, 16, 0, 180, Arc2D.OPEN)); break;
                case "kho":
                    g.drawLine(12, 3, 21, 7); g.drawLine(3, 7, 12, 3);
                    g.drawLine(3, 7, 3, 17); g.drawLine(21, 7, 21, 17);
                    g.drawLine(3, 17, 12, 21); g.drawLine(21, 17, 12, 21);
                    g.drawLine(12, 11, 12, 21); g.drawLine(3, 7, 12, 11); g.drawLine(21, 7, 12, 11); break;
                case "nhansu":
                    g.drawOval(6, 3, 7, 7); g.draw(new Arc2D.Float(2, 12, 15, 14, 0, 180, Arc2D.OPEN));
                    g.drawLine(18, 6, 18, 12); g.drawLine(15, 9, 21, 9); break;
                case "baocao":
                    g.drawLine(3, 21, 21, 21); g.fillRoundRect(5, 12, 4, 8, 2, 2);
                    g.fillRoundRect(11, 6, 4, 14, 2, 2); g.fillRoundRect(17, 9, 4, 11, 2, 2); break;
                case "km":
                    g.drawRoundRect(3, 6, 18, 12, 3, 3); g.drawLine(8, 15, 16, 9);
                    g.fillOval(7, 8, 3, 3); g.fillOval(14, 13, 3, 3); break;
                case "caidat":
                    g.drawOval(9, 9, 6, 6);
                    for (int i = 0; i < 8; i++) {
                        double a = Math.PI * 2 * i / 8;
                        g.drawLine((int) (12 + Math.cos(a) * 8), (int) (12 + Math.sin(a) * 8),
                                (int) (12 + Math.cos(a) * 10.5), (int) (12 + Math.sin(a) * 10.5));
                    } break;
                case "tim":
                    g.drawOval(4, 4, 13, 13); g.drawLine(16, 16, 21, 21); break;
                case "chuong":
                    g.draw(new Arc2D.Float(5, 3, 14, 14, 0, 180, Arc2D.OPEN));
                    g.drawLine(5, 10, 5, 17); g.drawLine(19, 10, 19, 17);
                    g.drawLine(3, 17, 21, 17); g.drawLine(10, 20, 14, 20); break;
                case "them":
                    g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawLine(12, 5, 12, 19); g.drawLine(5, 12, 19, 12); break;
                case "sua":
                    g.drawLine(4, 20, 8, 19); g.drawLine(4, 20, 5, 16); g.drawLine(5, 16, 16, 5);
                    g.drawLine(8, 19, 19, 8); g.drawLine(16, 5, 19, 8); break;
                case "xoa":
                    g.drawLine(4, 6, 20, 6); g.drawRoundRect(6, 6, 12, 15, 2, 2);
                    g.drawLine(10, 3, 14, 3); g.drawLine(10, 10, 10, 17); g.drawLine(14, 10, 14, 17); break;
                case "tien":
                    g.drawRoundRect(2, 6, 20, 12, 3, 3); g.drawOval(9, 9, 6, 6); break;
                case "taive":
                    g.draw(new Arc2D.Float(4, 4, 16, 16, 60, 250, Arc2D.OPEN));
                    g.drawLine(20, 4, 20, 10); g.drawLine(14, 10, 20, 10); break;
                case "xuat":
                    g.drawLine(12, 3, 12, 15); g.drawLine(7, 10, 12, 15);
                    g.drawLine(17, 10, 12, 15); g.drawLine(4, 20, 20, 20); break;
                case "thoat":
                    g.draw(new Arc2D.Float(3, 3, 18, 18, 45, 270, Arc2D.OPEN)); g.drawLine(12, 2, 12, 11); break;
                case "sao":
                    int[] px = new int[10], py = new int[10];
                    for (int i = 0; i < 10; i++) {
                        double a = Math.PI / 5 * i - Math.PI / 2, r = i % 2 == 0 ? 10 : 4.4;
                        px[i] = (int) (12 + Math.cos(a) * r); py[i] = (int) (12 + Math.sin(a) * r);
                    }
                    g.fillPolygon(px, py, 10); break;
                case "bep":
                    g.draw(new Arc2D.Float(5, 4, 14, 12, 0, 180, Arc2D.OPEN));
                    g.drawLine(5, 10, 5, 14); g.drawLine(19, 10, 19, 14);
                    g.drawRoundRect(4, 14, 16, 6, 2, 2); break;
                case "mu":
                    g.draw(new Arc2D.Float(4, 3, 16, 14, 0, 180, Arc2D.OPEN));
                    g.drawOval(2, 6, 7, 7); g.drawOval(15, 6, 7, 7);
                    g.drawRoundRect(6, 15, 12, 6, 2, 2); break;
                default:
                    g.drawOval(4, 4, 16, 16);
            }
            g.dispose();
        }
    }
}