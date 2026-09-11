package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class BaoCaoDoanhThuPanel extends JPanel {

    static final Color KHOI = Color.WHITE;
    static final Color KHOI_2 = new Color(0xFAF6F0);
    static final Color VIEN = new Color(0xE6DCCE);
    static final Color NGOC = new Color(0xB08246);
    static final Color NGOC_MO = new Color(0x7F5927);
    static final Color CHU = new Color(0x2D2723);
    static final Color CHU_MO = new Color(0x6E645D);
    static final Color LUC = new Color(0x198754);
    static final Color CAM = new Color(0xC26D00);
    static final Color LAM = new Color(0x2563EB);
    static final Color TIM = new Color(0x7C3AED);
    static final String FONT = font();

    private final List<String> nhan = new ArrayList<>();
    private final List<Double> gt = new ArrayList<>();
    private final List<Object[]> topMon = new ArrayList<>();
    private final JPanel oThongKe = new JPanel(new GridLayout(1, 4, 16, 0));
    private final BieuDoCot bieuDo = new BieuDoCot();
    private final JPanel dsMon = new JPanel();
    private final JLabel lbNguon = nhan("", 11, Font.BOLD, CAM);
    private int soNgay = 7;
    private boolean dungCSDL = false;

    public BaoCaoDoanhThuPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        JPanel tren = new JPanel(new BorderLayout(0, 16));
        tren.setOpaque(false);
        oThongKe.setOpaque(false);
        oThongKe.setPreferredSize(new Dimension(100, 104));
        tren.add(oThongKe, BorderLayout.NORTH);
        tren.add(thanhLoc(), BorderLayout.SOUTH);
        add(tren, BorderLayout.NORTH);

        JPanel giua = new JPanel(new BorderLayout(16, 0));
        giua.setOpaque(false);

        Khoi kBieuDo = new Khoi(new BorderLayout(0, 14));
        kBieuDo.add(tieuDe("Doanh thu theo ngày", "Cột càng cao doanh thu càng lớn, di chuột để xem chi tiết"),
                BorderLayout.NORTH);
        kBieuDo.add(bieuDo, BorderLayout.CENTER);
        giua.add(kBieuDo, BorderLayout.CENTER);

        Khoi kMon = new Khoi(new BorderLayout(0, 14));
        kMon.setPreferredSize(new Dimension(420, 100));
        kMon.add(tieuDe("Món bán chạy", "Xếp theo doanh thu mang lại"), BorderLayout.NORTH);
        dsMon.setOpaque(false);
        dsMon.setLayout(new BoxLayout(dsMon, BoxLayout.Y_AXIS));
        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(dsMon, BorderLayout.NORTH);
        kMon.add(cuon(giu), BorderLayout.CENTER);
        giua.add(kMon, BorderLayout.EAST);
        add(giua, BorderLayout.CENTER);

        napDuLieu();
    }

    private JPanel thanhLoc() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 52));
        JPanel trai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        trai.setOpaque(false);
        trai.add(nhan("KHOẢNG THỜI GIAN", 11, Font.BOLD, CHU_MO));
        int[] ngay = {7, 14, 30, 90};
        String[] ten = {"7 ngày", "14 ngày", "30 ngày", "90 ngày"};
        for (int i = 0; i < ngay.length; i++) {
            final int n = ngay[i];
            Chip c = new Chip(ten[i], soNgay == n);
            c.addActionListener(e -> { soNgay = n; napDuLieu(); lamMoiChip(trai); });
            trai.add(c);
        }
        trai.add(lbNguon);
        p.add(trai, BorderLayout.WEST);

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        phai.setOpaque(false);
        Nut tai = new Nut("Tải lại", false);
        Nut xuat = new Nut("Xuất báo cáo CSV", true);
        tai.addActionListener(e -> napDuLieu());
        xuat.addActionListener(e -> xuatCsv());
        phai.add(tai);
        phai.add(xuat);
        p.add(phai, BorderLayout.EAST);
        return p;
    }

    private void lamMoiChip(JPanel trai) {
        for (Component c : trai.getComponents())
            if (c instanceof Chip) {
                Chip ch = (Chip) c;
                ch.chon = ch.getText().startsWith(String.valueOf(soNgay));
                ch.repaint();
            }
    }

    private void napDuLieu() {
        nhan.clear();
        gt.clear();
        topMon.clear();
        dungCSDL = docCSDL();
        if (!dungCSDL) docMau();

        double tong = 0, cao = 0;
        String ngayCao = "";
        for (int i = 0; i < gt.size(); i++) {
            tong += gt.get(i);
            if (gt.get(i) > cao) { cao = gt.get(i); ngayCao = nhan.get(i); }
        }
        double tb = gt.isEmpty() ? 0 : tong / gt.size();

        oThongKe.removeAll();
        oThongKe.add(the("Tổng doanh thu", tienVN(tong), soNgay + " ngày gần nhất", LUC));
        oThongKe.add(the("Trung bình mỗi ngày", tienVN(tb), "Mức doanh thu ổn định", NGOC));
        oThongKe.add(the("Ngày cao nhất", tienVN(cao), ngayCao.isEmpty() ? "Chưa có dữ liệu" : "Vào ngày " + ngayCao, CAM));
        oThongKe.add(the("Số món bán chạy", String.valueOf(topMon.size()), "Xuất hiện trong báo cáo", LAM));
        oThongKe.revalidate();
        oThongKe.repaint();

        bieuDo.datDuLieu(nhan, gt);

        dsMon.removeAll();
        double max = 1;
        for (Object[] m : topMon) max = Math.max(max, (Double) m[2]);
        if (topMon.isEmpty()) {
            JLabel l = nhan("Chưa có dữ liệu bán hàng trong kỳ", 12, Font.PLAIN, CHU_MO);
            l.setBorder(new EmptyBorder(30, 6, 0, 0));
            dsMon.add(l);
        }
        int i = 0;
        for (Object[] m : topMon) dsMon.add(new DongMon(++i, (String) m[0], (Double) m[1], (Double) m[2], max));
        dsMon.revalidate();
        dsMon.repaint();

        lbNguon.setText(dungCSDL ? "   ●  Dữ liệu từ MySQL" : "   ●  Dữ liệu mẫu (chưa nối CSDL)");
        lbNguon.setForeground(dungCSDL ? LUC : CAM);
    }

    private boolean docCSDL() {
        String q1 = "SELECT DATE(ngay_tao) AS ngay, SUM(tong_tien) AS tien FROM don_hang "
                + "WHERE ngay_tao >= DATE_SUB(CURDATE(), INTERVAL ? DAY) "
                + "GROUP BY DATE(ngay_tao) ORDER BY ngay";
        String q2 = "SELECT m.ten_mon, SUM(c.so_luong) AS sl, SUM(c.so_luong * c.don_gia) AS tien "
                + "FROM chi_tiet_don_hang c JOIN mon_an m ON m.ma_mon = c.ma_mon "
                + "JOIN don_hang d ON d.ma_don_hang = c.ma_don_hang "
                + "WHERE d.ngay_tao >= DATE_SUB(CURDATE(), INTERVAL ? DAY) "
                + "GROUP BY c.ma_mon ORDER BY tien DESC LIMIT 8";
        try (Connection c = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(q1)) {
                ps.setInt(1, soNgay);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String d = rs.getString("ngay");
                        nhan.add(d.length() >= 10 ? d.substring(8, 10) + "/" + d.substring(5, 7) : d);
                        gt.add(rs.getDouble("tien"));
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(q2)) {
                ps.setInt(1, soNgay);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        topMon.add(new Object[]{rs.getString(1), rs.getDouble(2), rs.getDouble(3)});
                }
            }
            return !gt.isEmpty() || !topMon.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void docMau() {
        double[] d = {3900000, 5300000, 4650000, 6300000, 5750000, 7450000, 6100000,
                5200000, 4800000, 6900000, 7100000, 5400000, 6250000, 8100000};
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.add(java.util.Calendar.DAY_OF_MONTH, -Math.min(soNgay, d.length) + 1);
        for (int i = 0; i < Math.min(soNgay, d.length); i++) {
            nhan.add(new java.text.SimpleDateFormat("dd/MM").format(c.getTime()));
            gt.add(d[i]);
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }
        Object[][] m = {
                {"Combo gia đình", 86d, 21500000d}, {"Burger bò phô mai", 142d, 12070000d},
                {"Gà rán giòn", 128d, 9600000d}, {"Phở bò đặc biệt", 134d, 8710000d},
                {"Trà đào cam sả", 186d, 6510000d}, {"Chè khúc bạch", 121d, 3630000d},
                {"Coca Cola", 208d, 3120000d}
        };
        for (Object[] x : m) topMon.add(x);
    }

    private void xuatCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("BaoCaoDoanhThu.csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (java.io.Writer w = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(fc.getSelectedFile()), java.nio.charset.StandardCharsets.UTF_8)) {
            w.write('\ufeff');
            w.write("Ngày;Doanh thu\n");
            for (int i = 0; i < gt.size(); i++) w.write(nhan.get(i) + ";" + gt.get(i).longValue() + "\n");
            w.write("\nMón;Số lượng;Doanh thu\n");
            for (Object[] m : topMon)
                w.write(m[0] + ";" + ((Double) m[1]).longValue() + ";" + ((Double) m[2]).longValue() + "\n");
            JOptionPane.showMessageDialog(this, "Đã xuất báo cáo ra CSV.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + e.getMessage());
        }
    }

    private class BieuDoCot extends JPanel {
        List<String> nh = new ArrayList<>();
        List<Double> vl = new ArrayList<>();
        float anim = 0;
        int hover = -1;
        Timer tm;

        BieuDoCot() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    int n = vl.size();
                    if (n == 0) return;
                    int pad = 60, w = getWidth() - pad - 20;
                    int i = (int) ((e.getX() - pad) / (double) w * n);
                    hover = (i >= 0 && i < n) ? i : -1;
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseExited(MouseEvent e) { hover = -1; repaint(); }
            });
        }

        void datDuLieu(List<String> a, List<Double> b) {
            nh = new ArrayList<>(a);
            vl = new ArrayList<>(b);
            anim = 0;
            if (tm != null) tm.stop();
            tm = new Timer(16, e -> {
                anim += 0.05f;
                if (anim >= 1f) { anim = 1f; tm.stop(); }
                repaint();
            });
            tm.start();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight(), padL = 62, padB = 34, padT = 14;
            double max = 1;
            for (double v : vl) max = Math.max(max, v);
            double top = Math.ceil(max / 1000000.0) * 1000000;
            if (top <= 0) top = 1000000;
            g2.setFont(new Font(FONT, Font.PLAIN, 10));
            for (int i = 0; i <= 4; i++) {
                int y = padT + (int) ((H - padT - padB) * (1 - i / 4.0));
                g2.setColor(new Color(255, 255, 255, 12));
                g2.drawLine(padL, y, W - 14, y);
                g2.setColor(CHU_MO);
                g2.drawString(Math.round(top * i / 4 / 1000000.0 * 10) / 10.0 + "tr", 14, y + 4);
            }
            int n = vl.size();
            if (n == 0) { g2.dispose(); return; }
            int slot = (W - padL - 20) / n;
            int bw = Math.max(6, Math.min(42, slot - 10));
            for (int i = 0; i < n; i++) {
                double v = vl.get(i) * anim;
                int bh = (int) ((H - padT - padB) * (v / top));
                int x = padL + slot * i + (slot - bw) / 2;
                int y = H - padB - bh;
                boolean hot = i == hover;
                g2.setPaint(new GradientPaint(0, y, hot ? NGOC : NGOC_MO, 0, H - padB,
                        new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), hot ? 200 : 90)));
                g2.fillRoundRect(x, y, bw, Math.max(bh, 3), 8, 8);
                if (n <= 16 || i % 3 == 0) {
                    g2.setColor(CHU_MO);
                    g2.setFont(new Font(FONT, Font.PLAIN, 10));
                    String lb = nh.get(i);
                    int tw = g2.getFontMetrics().stringWidth(lb);
                    g2.drawString(lb, padL + slot * i + (slot - tw) / 2, H - 12);
                }
                if (hot) {
                    String s = tienVN(vl.get(i));
                    g2.setFont(new Font(FONT, Font.BOLD, 12));
                    int tw = g2.getFontMetrics().stringWidth(s) + 18;
                    int bx = Math.min(W - tw - 6, Math.max(6, x + bw / 2 - tw / 2));
                    int by = Math.max(4, y - 32);
                    g2.setColor(new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 40));
                    g2.fillRoundRect(bx, by, tw, 26, 9, 9);
                    g2.setColor(NGOC);
                    g2.drawRoundRect(bx, by, tw, 26, 9, 9);
                    g2.setColor(CHU);
                    g2.drawString(s, bx + 9, by + 18);
                }
            }
            g2.dispose();
        }
    }

    private class DongMon extends JComponent {
        int hang;
        String ten;
        double sl, tien, max;

        DongMon(int hang, String ten, double sl, double tien, double max) {
            this.hang = hang; this.ten = ten; this.sl = sl; this.tien = tien; this.max = max;
            setPreferredSize(new Dimension(360, 64));
            setMaximumSize(new Dimension(9999, 64));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color m = hang == 1 ? CAM : hang == 2 ? LAM : hang == 3 ? TIM : NGOC;
            g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 34));
            g2.fillRoundRect(0, 6, 30, 30, 10, 10);
            g2.setColor(m);
            g2.setFont(new Font(FONT, Font.BOLD, 13));
            g2.drawString(String.valueOf(hang), 11, 27);

            g2.setColor(CHU);
            g2.setFont(new Font(FONT, Font.BOLD, 13));
            String t = ten;
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(t) > w - 160) {
                while (fm.stringWidth(t + "…") > w - 160 && t.length() > 2) t = t.substring(0, t.length() - 1);
                t += "…";
            }
            g2.drawString(t, 42, 22);
            g2.setColor(LUC);
            String s = tienVN(tien);
            g2.drawString(s, w - fm.stringWidth(s) - 4, 22);

            g2.setColor(new Color(255, 255, 255, 14));
            g2.fillRoundRect(42, 34, w - 46, 8, 4, 4);
            g2.setColor(m);
            g2.fillRoundRect(42, 34, (int) ((w - 46) * (tien / max)), 8, 4, 4);
            g2.setColor(CHU_MO);
            g2.setFont(new Font(FONT, Font.PLAIN, 10));
            g2.drawString((long) sl + " phần đã bán", 42, 56);
            g2.dispose();
        }
    }

    private class Khoi extends JPanel {
        Khoi(LayoutManager lm) {
            super(lm);
            setOpaque(false);
            setBorder(new EmptyBorder(20, 22, 20, 22));
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

    private JPanel the(String n, String v, String phu, Color m) {
        JPanel p = new Khoi(new BorderLayout());
        p.setBorder(new EmptyBorder(16, 22, 16, 20));
        JPanel in = new JPanel();
        in.setOpaque(false);
        in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
        in.add(nhan(n.toUpperCase(), 10, Font.BOLD, CHU_MO));
        in.add(Box.createRigidArea(new Dimension(0, 8)));
        in.add(nhan(v, 24, Font.BOLD, m));
        in.add(Box.createRigidArea(new Dimension(0, 6)));
        in.add(nhan(phu, 11, Font.PLAIN, CHU_MO));
        p.add(in, BorderLayout.CENTER);
        return p;
    }

    private JPanel tieuDe(String a, String b) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(nhan(a, 16, Font.BOLD, CHU));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(nhan(b, 11, Font.PLAIN, CHU_MO));
        return p;
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
        Nut(String s, boolean chinh) {
            super(s);
            this.chinh = chinh;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 13));
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 40, 42));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover();
            if (chinh) {
                g2.setPaint(new GradientPaint(0, 0, NGOC, w, h, NGOC_MO));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(Color.WHITE);
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