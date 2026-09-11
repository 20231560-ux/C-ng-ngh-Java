package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class TongQuanPanel extends JPanel {

    private final NguoiDung nguoiDung;
    private final JPanel oChiSo = new JPanel(new GridLayout(1, 4, 16, 0));
    private final BieuDo bieuDo = new BieuDo();
    private final JPanel dsMon = new JPanel();
    private final JPanel dsDon = new JPanel();
    private final JPanel dsKho = new JPanel();
    private boolean coCSDL = false;

    public TongQuanPanel() { this(null); }

    public TongQuanPanel(NguoiDung nd) {
        this.nguoiDung = nd;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        JPanel tren = new JPanel(new BorderLayout(0, 16));
        tren.setOpaque(false);
        tren.add(new Banner(), BorderLayout.NORTH);
        oChiSo.setOpaque(false);
        oChiSo.setPreferredSize(new Dimension(100, 112));
        tren.add(oChiSo, BorderLayout.SOUTH);
        add(tren, BorderLayout.NORTH);

        JPanel giua = new JPanel(new BorderLayout(16, 0));
        giua.setOpaque(false);
        Giao.The kb = new Giao.The(new BorderLayout(0, 14));
        kb.add(dau("Doanh thu 7 ngày qua", "Tổng tiền các đơn đã hoàn thành", true), BorderLayout.NORTH);
        kb.add(bieuDo, BorderLayout.CENTER);
        giua.add(kb, BorderLayout.CENTER);

        Giao.The km = new Giao.The(new BorderLayout(0, 12));
        km.setPreferredSize(new Dimension(372, 100));
        km.add(dau("Top món bán chạy", "Theo số phần đã phục vụ", false), BorderLayout.NORTH);
        dsMon.setOpaque(false);
        dsMon.setLayout(new BoxLayout(dsMon, BoxLayout.Y_AXIS));
        km.add(boc(dsMon), BorderLayout.CENTER);
        giua.add(km, BorderLayout.EAST);
        add(giua, BorderLayout.CENTER);

        JPanel duoi = new JPanel(new BorderLayout(16, 0));
        duoi.setOpaque(false);
        duoi.setPreferredSize(new Dimension(100, 232));
        Giao.The kd = new Giao.The(new BorderLayout(0, 12));
        kd.add(dau("Đơn hàng gần đây", "8 giao dịch mới nhất", false), BorderLayout.NORTH);
        dsDon.setOpaque(false);
        dsDon.setLayout(new BoxLayout(dsDon, BoxLayout.Y_AXIS));
        kd.add(boc(dsDon), BorderLayout.CENTER);
        duoi.add(kd, BorderLayout.CENTER);

        Giao.The kk = new Giao.The(new BorderLayout(0, 12));
        kk.setPreferredSize(new Dimension(372, 100));
        kk.add(dau("Cảnh báo tồn kho", "Nguyên liệu dưới định mức", false), BorderLayout.NORTH);
        dsKho.setOpaque(false);
        dsKho.setLayout(new BoxLayout(dsKho, BoxLayout.Y_AXIS));
        kk.add(boc(dsKho), BorderLayout.CENTER);
        duoi.add(kk, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);

        nap();
    }

    private JScrollPane boc(JPanel p) {
        JPanel g = new JPanel(new BorderLayout());
        g.setOpaque(false);
        g.add(p, BorderLayout.NORTH);
        return Giao.cuon(g);
    }

    private JPanel dau(String a, String b, boolean nhan) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(Giao.chu(a, 16, Font.BOLD, Giao.CHU));
        t.add(Box.createRigidArea(new Dimension(0, 4)));
        t.add(Giao.chu(b, 11, Font.PLAIN, Giao.CHU_PHU));
        p.add(t, BorderLayout.WEST);
        if (nhan) {
            JComponent n = new JComponent() {
                @Override public Dimension getPreferredSize() { return new Dimension(150, 26); }
                @Override protected void paintComponent(Graphics gg) {
                    Graphics2D g2 = (Graphics2D) gg.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    String s = coCSDL ? "MySQL trực tiếp" : "Dữ liệu mẫu";
                    int w = g2.getFontMetrics(Giao.f(11, Font.BOLD)).stringWidth(s) + 22;
                    Giao.veHuyHieu(g2, s, 150 - w, 1, coCSDL ? Giao.LUC : Giao.CAM);
                    g2.dispose();
                }
            };
            p.add(n, BorderLayout.EAST);
        }
        return p;
    }

    /* ==================== BANNER ==================== */
    private class Banner extends JPanel {
        private float t = 0;

        Banner() {
            setOpaque(false);
            setPreferredSize(new Dimension(100, 148));
            new Timer(40, e -> { t += .012f; repaint(); }).start();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Shape cu = g2.getClip();
            g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, 18, 18));
            g2.setPaint(new GradientPaint(0, 0, new Color(0x2D2723), w, h, new Color(0x584635)));
            g2.fillRect(0, 0, w, h);
            for (int i = 0; i < 5; i++) {
                double a = t + i * 1.2;
                int r = 150 + i * 60;
                g2.setColor(new Color(0xD4, 0xA3, 0x59, 12));
                g2.fillOval((int) (w - 250 + Math.cos(a) * 40) - r / 2,
                        (int) (h / 2 + Math.sin(a * 1.3) * 26) - r / 2, r, r);
            }
            g2.setClip(cu);
            g2.setColor(Giao.CHINH);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

            g2.setFont(Giao.f(13, Font.PLAIN));
            g2.setColor(new Color(0xD4A359));
            g2.drawString(loiChao(), 32, 44);

            g2.setFont(Giao.f(32, Font.BOLD));
            g2.setColor(Color.WHITE);
            g2.drawString(ten(), 32, 86);

            g2.setFont(Giao.f(13, Font.PLAIN));
            g2.setColor(new Color(0xD8D0C6));
            g2.drawString("Mỗi bữa ăn là một trải nghiệm đáng nhớ  ★", 32, 114);

            int bx = w - 244;
            g2.setColor(new Color(255, 255, 255, 24));
            g2.fillRoundRect(bx, h / 2 - 33, 206, 66, 14, 14);
            g2.setColor(new Color(0xD4, 0xA3, 0x59, 120));
            g2.drawRoundRect(bx, h / 2 - 33, 206, 66, 14, 14);
            g2.setFont(Giao.f(10, Font.BOLD));
            g2.setColor(new Color(0xC9C0B4));
            g2.drawString("CA LÀM VIỆC", bx + 20, h / 2 - 11);
            g2.setFont(Giao.f(18, Font.BOLD));
            g2.setColor(new Color(0xE3C674));
            g2.drawString(caHienTai(), bx + 20, h / 2 + 15);
            g2.dispose();
        }
    }

    private String ten() {
        return nguoiDung == null || nguoiDung.getHoTen() == null ? "Nguyễn Quản Trị" : nguoiDung.getHoTen();
    }

    private String loiChao() {
        int h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (h < 11) return "Chào buổi sáng,";
        if (h < 14) return "Chào buổi trưa,";
        if (h < 18) return "Chào buổi chiều,";
        return "Chào buổi tối,";
    }

    private String caHienTai() {
        int h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (h < 14) return "Ca sáng · 06–14h";
        if (h < 18) return "Ca chiều · 14–18h";
        return "Ca tối · 18–23h";
    }

    /* ==================== DỮ LIỆU ==================== */
    private void nap() {
        double dt = 0, dtQua = 0;
        int don = 0, khach = 0, banDung = 0, tongBan = 0;
        List<String> ngay = new ArrayList<>();
        List<Double> tien = new ArrayList<>();
        List<Object[]> mon = new ArrayList<>();
        List<Object[]> dh = new ArrayList<>();
        List<Object[]> kho = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection(); Statement st = c.createStatement()) {
            try (ResultSet r = st.executeQuery("SELECT IFNULL(SUM(tong_tien),0),COUNT(*) FROM don_hang "
                    + "WHERE trang_thai='HOAN_THANH' AND DATE(ngay_tao)=CURDATE()")) {
                if (r.next()) { dt = r.getDouble(1); don = r.getInt(2); }
            }
            try (ResultSet r = st.executeQuery("SELECT IFNULL(SUM(tong_tien),0) FROM don_hang "
                    + "WHERE trang_thai='HOAN_THANH' AND DATE(ngay_tao)=DATE_SUB(CURDATE(),INTERVAL 1 DAY)")) {
                if (r.next()) dtQua = r.getDouble(1);
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM khach_hang")) {
                if (r.next()) khach = r.getInt(1);
            }
            try (ResultSet r = st.executeQuery("SELECT SUM(trang_thai<>'TRONG'),COUNT(*) FROM ban_an")) {
                if (r.next()) { banDung = r.getInt(1); tongBan = r.getInt(2); }
            }
            try (ResultSet r = st.executeQuery("SELECT DATE(ngay_tao) d,SUM(tong_tien) t FROM don_hang "
                    + "WHERE trang_thai='HOAN_THANH' AND ngay_tao>=DATE_SUB(CURDATE(),INTERVAL 6 DAY) "
                    + "GROUP BY d ORDER BY d")) {
                while (r.next()) {
                    String d = r.getString(1);
                    ngay.add(d.substring(8, 10) + "/" + d.substring(5, 7));
                    tien.add(r.getDouble(2));
                }
            }
            try (ResultSet r = st.executeQuery("SELECT m.ten_mon,SUM(c.so_luong) sl,d.ten_danh_muc "
                    + "FROM chi_tiet_don_hang c JOIN mon_an m ON m.ma_mon=c.ma_mon "
                    + "LEFT JOIN danh_muc_mon d ON d.ma_danh_muc=m.ma_danh_muc "
                    + "JOIN don_hang o ON o.ma_don_hang=c.ma_don_hang WHERE o.trang_thai='HOAN_THANH' "
                    + "GROUP BY c.ma_mon ORDER BY sl DESC LIMIT 5")) {
                while (r.next()) mon.add(new Object[]{r.getString(1), r.getDouble(2), r.getString(3)});
            }
            try (ResultSet r = st.executeQuery("SELECT d.ma_don,b.ten_ban,IFNULL(k.ho_ten,'Khách lẻ'),"
                    + "d.tong_tien,d.trang_thai,TIME_FORMAT(d.ngay_tao,'%H:%i') FROM don_hang d "
                    + "LEFT JOIN ban_an b ON b.ma_ban=d.ma_ban "
                    + "LEFT JOIN khach_hang k ON k.ma_khach_hang=d.ma_khach_hang "
                    + "ORDER BY d.ma_don_hang DESC LIMIT 8")) {
                while (r.next())
                    dh.add(new Object[]{r.getString(1), r.getString(2), r.getString(3),
                            r.getDouble(4), r.getString(5), r.getString(6)});
            }
            try (ResultSet r = st.executeQuery("SELECT ten_nguyen_lieu,so_luong,so_luong_toi_thieu,don_vi_tinh "
                    + "FROM nguyen_lieu WHERE so_luong<=so_luong_toi_thieu "
                    + "ORDER BY so_luong/GREATEST(so_luong_toi_thieu,1) LIMIT 6")) {
                while (r.next())
                    kho.add(new Object[]{r.getString(1), r.getDouble(2), r.getDouble(3), r.getString(4)});
            }
            coCSDL = true;
        } catch (Exception e) {
            coCSDL = false;
        }

        if (!coCSDL) {
            dt = 28650000; dtQua = 25500000; don = 48; khach = 168; banDung = 16; tongBan = 26;
            double[] v = {14.2, 16.8, 13.4, 18.6, 15.9, 21.2, 17.2};
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.add(java.util.Calendar.DAY_OF_MONTH, -6);
            for (double x : v) {
                ngay.add(new java.text.SimpleDateFormat("dd/MM").format(c.getTime()));
                tien.add(x * 1000000);
                c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
            mon.add(new Object[]{"Bò Fuji Nướng Đá", 68d, "Món chính"});
            mon.add(new Object[]{"Lẩu Thái Hải Sản", 52d, "Lẩu - Nướng"});
            mon.add(new Object[]{"Cơm Chiên Hải Sản", 46d, "Món chính"});
            kho.add(new Object[]{"Phô mai Mozzarella", 0d, 3d, "kg"});
            kho.add(new Object[]{"Thịt bò Úc", 2.5, 10d, "kg"});
        }

        double pt = dtQua <= 0 ? 0 : (dt - dtQua) / dtQua * 100;
        oChiSo.removeAll();
        oChiSo.add(new TheChiSo("Doanh thu hôm nay", Giao.tien(dt),
                (pt >= 0 ? "▲ +" : "▼ ") + String.format("%.1f", Math.abs(pt)) + "% so với hôm qua",
                Giao.CHINH, "tien", pt >= 0));
        oChiSo.add(new TheChiSo("Khách hàng", String.valueOf(khach), "Hồ sơ thành viên", Giao.LAM, "khach", true));
        oChiSo.add(new TheChiSo("Bàn đang phục vụ", banDung + " / " + tongBan,
                (tongBan == 0 ? 0 : Math.round(banDung * 100f / tongBan)) + "% công suất",
                Giao.CAM, "ban", true));
        oChiSo.add(new TheChiSo("Đơn hàng hôm nay", String.valueOf(don),
                "Đã hoàn thành trong ngày", Giao.LUC, "don", true));
        oChiSo.revalidate();
        oChiSo.repaint();

        bieuDo.dat(ngay, tien);

        dsMon.removeAll();
        double max = 1;
        for (Object[] m : mon) max = Math.max(max, (Double) m[1]);
        int i = 0;
        for (Object[] m : mon) dsMon.add(new DongMon(++i, (String) m[0], (Double) m[1], (String) m[2], max));
        dsMon.revalidate();
        dsMon.repaint();

        dsDon.removeAll();
        for (Object[] d : dh) dsDon.add(new DongDon(d));
        if (dh.isEmpty()) dsDon.add(trong("Chưa có đơn hàng nào"));
        dsDon.revalidate();
        dsDon.repaint();

        dsKho.removeAll();
        if (kho.isEmpty()) dsKho.add(trong("Tất cả nguyên liệu đều đủ tồn kho"));
        for (Object[] k : kho) dsKho.add(new DongKho(k));
        dsKho.revalidate();
        dsKho.repaint();
    }

    private JLabel trong(String s) {
        JLabel l = Giao.chu(s, 12, Font.PLAIN, Giao.CHU_NHAT);
        l.setBorder(new EmptyBorder(22, 4, 0, 0));
        return l;
    }

    /* ==================== THẺ CHỈ SỐ ==================== */
    private class TheChiSo extends Giao.The {
        private final String nhan, gt, phu, icon;
        private final Color mau;
        private final boolean tang;
        private float chay = 0;
        private double dich = 0;
        private String duoi = "";

        TheChiSo(String nhan, String gt, String phu, Color mau, String icon, boolean tang) {
            super(new BorderLayout());
            this.nhan = nhan; this.gt = gt; this.phu = phu; this.mau = mau; this.icon = icon; this.tang = tang;
            setBorder(new EmptyBorder(16, 18, 16, 18));
            String s = gt.replaceAll("[^0-9]", "");
            if (!s.isEmpty() && s.length() <= 15 && !gt.contains("/")) {
                dich = Double.parseDouble(s);
                duoi = gt.replaceAll("[0-9.,]", "").trim();
                Timer t = new Timer(16, null);
                t.addActionListener(e -> {
                    chay += .06f;
                    if (chay >= 1) { chay = 1; t.stop(); }
                    repaint();
                });
                t.start();
            } else chay = 1;
        }

        private String hien() {
            if (chay >= 1 || dich == 0) return gt;
            long v = (long) (dich * (1 - Math.pow(1 - chay, 3)));
            java.text.DecimalFormatSymbols k = new java.text.DecimalFormatSymbols(java.util.Locale.US);
            k.setGroupingSeparator('.');
            String s = new java.text.DecimalFormat("#,##0", k).format(v);
            return duoi.isEmpty() ? s : s + " " + duoi;
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight() - 3;

            g2.setColor(Giao.nhatHon(mau));
            g2.fillRoundRect(18, 16, 40, 40, 12, 12);
            Giao.Ic.ve(g2, icon, 28, 26, 20, mau);

            g2.setFont(Giao.f(11, Font.BOLD));
            g2.setColor(Giao.CHU_PHU);
            g2.drawString(nhan.toUpperCase(), 68, 32);

            String v = hien();
            g2.setFont(Giao.f(v.length() > 13 ? 19 : 22, Font.BOLD));
            g2.setColor(Giao.CHU);
            g2.drawString(v, 68, 58);

            g2.setFont(Giao.f(11, Font.PLAIN));
            g2.setColor(tang ? Giao.LUC : Giao.DO);
            FontMetrics fm = g2.getFontMetrics();
            String p = phu;
            if (fm.stringWidth(p) > w - 86) {
                while (fm.stringWidth(p + "…") > w - 86 && p.length() > 2) p = p.substring(0, p.length() - 1);
                p += "…";
            }
            g2.drawString(p, 68, h - 14);
            g2.dispose();
        }
    }

    /* ==================== BIỂU ĐỒ ==================== */
    private class BieuDo extends JPanel {
        List<String> nh = new ArrayList<>();
        List<Double> vl = new ArrayList<>();
        float anim = 0;
        int hover = -1;
        Timer tm;

        BieuDo() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    int n = vl.size();
                    if (n < 2) return;
                    int pad = 66, w = getWidth() - pad - 18;
                    int i = Math.round((e.getX() - pad) / (float) w * (n - 1));
                    hover = (i >= 0 && i < n) ? i : -1;
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseExited(MouseEvent e) { hover = -1; repaint(); }
            });
        }

        void dat(List<String> a, List<Double> b) {
            nh = new ArrayList<>(a);
            vl = new ArrayList<>(b);
            anim = 0;
            if (tm != null) tm.stop();
            tm = new Timer(16, e -> {
                anim += .05f;
                if (anim >= 1) { anim = 1; tm.stop(); }
                repaint();
            });
            tm.start();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight(), padL = 66, padB = 30, padT = 14;
            int n = vl.size();
            if (n < 2) { g2.dispose(); return; }
            double max = 1;
            for (double v : vl) max = Math.max(max, v);
            double top = Math.ceil(max / 5000000.0) * 5000000;
            if (top <= 0) top = 5000000;

            g2.setFont(Giao.f(10, Font.PLAIN));
            for (int i = 0; i <= 4; i++) {
                int y = padT + (int) ((H - padT - padB) * (1 - i / 4.0));
                g2.setColor(Giao.VIEN);
                g2.drawLine(padL, y, W - 14, y);
                g2.setColor(Giao.CHU_NHAT);
                g2.drawString((long) (top * i / 4 / 1000000) + " tr", 18, y + 4);
            }

            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = padL + (W - padL - 20) * i / (n - 1);
                ys[i] = (int) (padT + (H - padT - padB) * (1 - vl.get(i) * anim / top));
            }
            Path2D vung = new Path2D.Float();
            vung.moveTo(xs[0], H - padB);
            for (int i = 0; i < n; i++) vung.lineTo(xs[i], ys[i]);
            vung.lineTo(xs[n - 1], H - padB);
            vung.closePath();
            g2.setPaint(new GradientPaint(0, padT, new Color(0xB0, 0x82, 0x46, 60),
                    0, H - padB, new Color(0xB0, 0x82, 0x46, 0)));
            g2.fill(vung);

            g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(Giao.CHINH);
            for (int i = 0; i < n - 1; i++) g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);

            for (int i = 0; i < n; i++) {
                boolean hot = i == hover;
                g2.setColor(Giao.THE);
                g2.fillOval(xs[i] - (hot ? 7 : 5), ys[i] - (hot ? 7 : 5), hot ? 14 : 10, hot ? 14 : 10);
                g2.setColor(Giao.CHINH);
                g2.setStroke(new BasicStroke(2.4f));
                g2.drawOval(xs[i] - (hot ? 7 : 5), ys[i] - (hot ? 7 : 5), hot ? 14 : 10, hot ? 14 : 10);
                g2.setFont(Giao.f(10, Font.PLAIN));
                g2.setColor(Giao.CHU_NHAT);
                int tw = g2.getFontMetrics().stringWidth(nh.get(i));
                g2.drawString(nh.get(i), xs[i] - tw / 2, H - 10);
            }

            if (hover >= 0) {
                String s = Giao.tien(vl.get(hover));
                g2.setFont(Giao.f(12, Font.BOLD));
                int tw = g2.getFontMetrics().stringWidth(s) + 22;
                int bx = Math.min(W - tw - 6, Math.max(6, xs[hover] - tw / 2));
                int by = Math.max(2, ys[hover] - 38);
                g2.setColor(Giao.CHINH_DAM);
                g2.fillRoundRect(bx, by, tw, 28, 9, 9);
                g2.setColor(Color.WHITE);
                g2.drawString(s, bx + 11, by + 19);
            }
            g2.dispose();
        }
    }

    /* ==================== CÁC DÒNG ==================== */
    private class DongMon extends JComponent {
        final int hang;
        final String ten, nhom;
        final double sl, max;

        DongMon(int hang, String ten, double sl, String nhom, double max) {
            this.hang = hang; this.ten = ten; this.sl = sl; this.nhom = nhom; this.max = max;
            setPreferredSize(new Dimension(320, 60));
            setMaximumSize(new Dimension(9999, 60));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            Color[] pal = {Giao.CHINH, Giao.LAM, Giao.TIM, Giao.LUC, Giao.CAM};
            Color m = pal[(hang - 1) % pal.length];

            g2.setColor(Giao.nhatHon(m));
            g2.fillRoundRect(0, 8, 36, 36, 11, 11);
            g2.setFont(Giao.f(14, Font.BOLD));
            g2.setColor(m.darker());
            String r = String.valueOf(hang);
            g2.drawString(r, 18 - g2.getFontMetrics().stringWidth(r) / 2, 31);

            g2.setFont(Giao.f(13, Font.BOLD));
            g2.setColor(Giao.CHU);
            String t = ten;
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(t) > w - 130) {
                while (fm.stringWidth(t + "…") > w - 130 && t.length() > 2) t = t.substring(0, t.length() - 1);
                t += "…";
            }
            g2.drawString(t, 48, 24);

            g2.setFont(Giao.f(11, Font.PLAIN));
            g2.setColor(Giao.CHU_NHAT);
            g2.drawString(nhom == null ? "Khác" : nhom, 48, 40);

            g2.setFont(Giao.f(12, Font.BOLD));
            g2.setColor(Giao.CHINH_DAM);
            String s = (long) sl + " phần";
            g2.drawString(s, w - g2.getFontMetrics().stringWidth(s) - 2, 24);

            g2.setColor(Giao.VIEN);
            g2.fillRoundRect(48, 46, w - 52, 5, 3, 3);
            g2.setColor(m);
            g2.fillRoundRect(48, 46, (int) ((w - 52) * (sl / max)), 5, 3, 3);
            g2.dispose();
        }
    }

    private class DongDon extends JComponent {
        final Object[] d;

        DongDon(Object[] d) {
            this.d = d;
            setPreferredSize(new Dimension(400, 46));
            setMaximumSize(new Dimension(9999, 46));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            g2.setColor(Giao.VIEN);
            g2.fillRect(0, 45, w, 1);

            g2.setFont(Giao.f(12, Font.BOLD));
            g2.setColor(Giao.CHU);
            g2.drawString(String.valueOf(d[0]), 2, 20);
            g2.setFont(Giao.f(11, Font.PLAIN));
            g2.setColor(Giao.CHU_NHAT);
            g2.drawString(d[1] + " · " + d[2] + " · " + d[5], 2, 36);

            String tt = String.valueOf(d[4]);
            Color m = "HOAN_THANH".equals(tt) ? Giao.LUC : "DA_HUY".equals(tt) ? Giao.DO
                    : "DANG_PHUC_VU".equals(tt) ? Giao.CAM : Giao.LAM;
            String nh = "HOAN_THANH".equals(tt) ? "Hoàn thành" : "DA_HUY".equals(tt) ? "Đã huỷ"
                    : "DANG_PHUC_VU".equals(tt) ? "Đang phục vụ" : "Chờ xử lý";
            int bw = g2.getFontMetrics(Giao.f(11, Font.BOLD)).stringWidth(nh) + 22;
            Giao.veHuyHieu(g2, nh, w - bw - 2, 11, m);

            g2.setFont(Giao.f(13, Font.BOLD));
            g2.setColor(Giao.CHU);
            String s = Giao.tien((Double) d[3]);
            g2.drawString(s, w - bw - 14 - g2.getFontMetrics().stringWidth(s), 29);
            g2.dispose();
        }
    }

    private class DongKho extends JComponent {
        final Object[] k;

        DongKho(Object[] k) {
            this.k = k;
            setPreferredSize(new Dimension(320, 52));
            setMaximumSize(new Dimension(9999, 52));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            double ton = (Double) k[1], dinh = (Double) k[2];
            boolean het = ton <= 0;
            Color m = het ? Giao.DO : Giao.CAM;
            g2.setColor(Giao.nhatHon(m));
            g2.fillRoundRect(0, 4, w, 42, 10, 10);
            g2.setColor(m);
            g2.fillRoundRect(0, 4, 3, 42, 3, 3);

            g2.setFont(Giao.f(13, Font.BOLD));
            g2.setColor(Giao.CHU);
            g2.drawString(String.valueOf(k[0]), 14, 24);
            g2.setFont(Giao.f(11, Font.PLAIN));
            g2.setColor(Giao.CHU_PHU);
            g2.drawString("Còn " + Giao.so(ton) + " " + k[3] + " / định mức " + Giao.so(dinh), 14, 39);

            String s = het ? "Đã hết" : "Sắp hết";
            int bw = g2.getFontMetrics(Giao.f(11, Font.BOLD)).stringWidth(s) + 22;
            Giao.veHuyHieu(g2, s, w - bw - 10, 14, m);
            g2.dispose();
        }
    }
}