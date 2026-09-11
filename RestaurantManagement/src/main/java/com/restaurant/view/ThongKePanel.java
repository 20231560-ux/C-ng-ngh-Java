package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class ThongKePanel extends JPanel {

    static final Color KHOI = Color.WHITE;
    static final Color KHOI_2 = new Color(0xFAF6F0);
    static final Color VIEN = new Color(0xE6DCCE);
    static final Color NGOC = new Color(0xB08246);
    static final Color CHU = new Color(0x2D2723);
    static final Color CHU_MO = new Color(0x6E645D);
    static final Color LUC = new Color(0x198754);
    static final Color CAM = new Color(0xC26D00);
    static final Color LAM = new Color(0x2563EB);
    static final Color TIM = new Color(0x7C3AED);
    static final Color DO = new Color(0xDC2626);
    static final Color[] PAL = {NGOC, LAM, CAM, TIM, LUC, DO};
    static final String FONT = font();

    private final List<String> nhomTen = new ArrayList<>();
    private final List<Double> nhomGT = new ArrayList<>();
    private final List<String> gioTen = new ArrayList<>();
    private final List<Double> gioGT = new ArrayList<>();
    private final JPanel oThongKe = new JPanel(new GridLayout(1, 4, 16, 0));
    private final Donut donut = new Donut();
    private final CotNgang cotNgang = new CotNgang();
    private boolean dungCSDL = false;

    public ThongKePanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        oThongKe.setOpaque(false);
        oThongKe.setPreferredSize(new Dimension(100, 104));
        add(oThongKe, BorderLayout.NORTH);

        JPanel giua = new JPanel(new GridLayout(1, 2, 16, 0));
        giua.setOpaque(false);

        Khoi k1 = new Khoi(new BorderLayout(0, 14));
        k1.add(tieuDe("Cơ cấu món theo danh mục", "Tỷ trọng số món trong thực đơn"), BorderLayout.NORTH);
        k1.add(donut, BorderLayout.CENTER);
        giua.add(k1);

        Khoi k2 = new Khoi(new BorderLayout(0, 14));
        k2.add(tieuDe("Khung giờ cao điểm", "Số đơn hàng ghi nhận theo khung giờ"), BorderLayout.NORTH);
        k2.add(cotNgang, BorderLayout.CENTER);
        giua.add(k2);
        add(giua, BorderLayout.CENTER);

        napDuLieu();
    }

    private void napDuLieu() {
        nhomTen.clear();
        nhomGT.clear();
        gioTen.clear();
        gioGT.clear();
        int soMon = 0, soBan = 0, soKhach = 0, soDon = 0;

        try (Connection c = DatabaseConnection.getConnection(); Statement st = c.createStatement()) {
            try (ResultSet rs = st.executeQuery(
                    "SELECT d.ten_danh_muc, COUNT(m.ma_mon) FROM danh_muc_mon d "
                            + "LEFT JOIN mon_an m ON m.ma_danh_muc = d.ma_danh_muc GROUP BY d.ma_danh_muc")) {
                while (rs.next()) {
                    nhomTen.add(rs.getString(1));
                    nhomGT.add(rs.getDouble(2));
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT HOUR(ngay_tao), COUNT(*) FROM don_hang GROUP BY HOUR(ngay_tao) ORDER BY 1")) {
                while (rs.next()) {
                    gioTen.add(String.format("%02d:00", rs.getInt(1)));
                    gioGT.add(rs.getDouble(2));
                }
            }
            soMon = dem(st, "mon_an");
            soBan = dem(st, "ban_an");
            soKhach = dem(st, "khach_hang");
            soDon = dem(st, "don_hang");
            dungCSDL = true;
        } catch (Exception e) {
            dungCSDL = false;
        }

        if (nhomTen.isEmpty()) {
            String[] t = {"Món chính", "Đồ uống", "Tráng miệng", "Combo"};
            double[] v = {3, 2, 1, 1};
            for (int i = 0; i < t.length; i++) { nhomTen.add(t[i]); nhomGT.add(v[i]); }
        }
        if (gioTen.isEmpty()) {
            String[] t = {"09:00", "11:00", "12:00", "13:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
            double[] v = {4, 18, 32, 21, 12, 28, 41, 35, 16};
            for (int i = 0; i < t.length; i++) { gioTen.add(t[i]); gioGT.add(v[i]); }
        }
        if (!dungCSDL) { soMon = 7; soBan = 10; soKhach = 5; soDon = 7; }

        int gioCao = 0;
        double max = 0;
        for (int i = 0; i < gioGT.size(); i++)
            if (gioGT.get(i) > max) { max = gioGT.get(i); gioCao = i; }

        oThongKe.removeAll();
        oThongKe.add(the("Món trong thực đơn", String.valueOf(soMon), nhomTen.size() + " danh mục đang dùng", NGOC));
        oThongKe.add(the("Bàn phục vụ", String.valueOf(soBan), "Tổng số bàn trong nhà hàng", LAM));
        oThongKe.add(the("Khách thành viên", String.valueOf(soKhach), "Đã có hồ sơ trong hệ thống", TIM));
        oThongKe.add(the("Khung giờ đông nhất", gioGT.isEmpty() ? "—" : gioTen.get(gioCao),
                soDon + " đơn hàng đã ghi nhận", CAM));
        oThongKe.revalidate();
        oThongKe.repaint();

        donut.datDuLieu(nhomTen, nhomGT);
        cotNgang.datDuLieu(gioTen, gioGT);
    }

    private int dem(Statement st, String bang) {
        try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + bang)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private class Donut extends JPanel {
        List<String> ten = new ArrayList<>();
        List<Double> gt = new ArrayList<>();
        float anim = 0;
        Timer tm;

        Donut() { setOpaque(false); }

        void datDuLieu(List<String> a, List<Double> b) {
            ten = new ArrayList<>(a);
            gt = new ArrayList<>(b);
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
            double tong = 0;
            for (double v : gt) tong += v;
            int d = Math.min(getHeight() - 20, 210);
            int cx = 20, cy = (getHeight() - d) / 2;
            if (tong <= 0) { g2.dispose(); return; }
            double start = 90;
            for (int i = 0; i < gt.size(); i++) {
                double ext = -360.0 * (gt.get(i) / tong) * anim;
                g2.setColor(PAL[i % PAL.length]);
                g2.fill(new Arc2D.Double(cx, cy, d, d, start, ext, Arc2D.PIE));
                start += ext;
            }
            g2.setColor(KHOI);
            g2.fillOval(cx + 46, cy + 46, d - 92, d - 92);
            g2.setColor(CHU);
            g2.setFont(new Font(FONT, Font.BOLD, 24));
            String s = String.valueOf((long) tong);
            g2.drawString(s, cx + d / 2 - g2.getFontMetrics().stringWidth(s) / 2, cy + d / 2 + 4);
            g2.setColor(CHU_MO);
            g2.setFont(new Font(FONT, Font.PLAIN, 11));
            g2.drawString("món", cx + d / 2 - 12, cy + d / 2 + 24);

            int ly = cy + 10;
            for (int i = 0; i < ten.size(); i++) {
                g2.setColor(PAL[i % PAL.length]);
                g2.fillRoundRect(cx + d + 28, ly, 12, 12, 4, 4);
                g2.setColor(CHU);
                g2.setFont(new Font(FONT, Font.BOLD, 13));
                g2.drawString(ten.get(i), cx + d + 48, ly + 11);
                g2.setColor(CHU_MO);
                g2.setFont(new Font(FONT, Font.PLAIN, 11));
                int pt = (int) Math.round(gt.get(i) / tong * 100);
                g2.drawString((long) (double) gt.get(i) + " món  •  " + pt + "%", cx + d + 48, ly + 27);
                ly += 40;
            }
            g2.dispose();
        }
    }

    private class CotNgang extends JPanel {
        List<String> ten = new ArrayList<>();
        List<Double> gt = new ArrayList<>();
        float anim = 0;
        Timer tm;

        CotNgang() { setOpaque(false); }

        void datDuLieu(List<String> a, List<Double> b) {
            ten = new ArrayList<>(a);
            gt = new ArrayList<>(b);
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
            int n = gt.size();
            if (n == 0) { g2.dispose(); return; }
            double max = 1;
            for (double v : gt) max = Math.max(max, v);
            int h = Math.max(20, Math.min(34, (getHeight() - 10) / n));
            int y = 4;
            for (int i = 0; i < n; i++) {
                Color m = gt.get(i) >= max * 0.8 ? CAM : NGOC;
                g2.setColor(CHU_MO);
                g2.setFont(new Font(FONT, Font.PLAIN, 11));
                g2.drawString(ten.get(i), 4, y + h / 2 + 4);
                int x0 = 58, wMax = getWidth() - x0 - 60;
                g2.setColor(new Color(0xEAE4DC));
                g2.fillRoundRect(x0, y + 4, wMax, h - 12, 7, 7);
                int wv = (int) (wMax * (gt.get(i) / max) * anim);
                g2.setPaint(new GradientPaint(x0, 0, m, x0 + wMax, 0,
                        new Color(m.getRed(), m.getGreen(), m.getBlue(), 120)));
                g2.fillRoundRect(x0, y + 4, Math.max(wv, 3), h - 12, 7, 7);
                g2.setColor(CHU);
                g2.setFont(new Font(FONT, Font.BOLD, 12));
                g2.drawString(String.valueOf((long) (double) gt.get(i)), x0 + wMax + 10, y + h / 2 + 4);
                y += h;
            }
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