package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

/** Tổng quan ca làm việc của nhân viên — gọn, rõ việc cần làm. */
public class EmployeeDashboardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final NguoiDung nguoiDung;
    private final Consumer<String> dieuHuong;

    private final JPanel dsViec = new JPanel();
    private final JPanel dsTimeline = new JPanel();
    private DaiTrangThai daiTrangThai;
    private BieuDoGio bieuDo;
    private JLabel lbTongCa;
    private final List<String> gioLabel = new ArrayList<>();
    private final List<Double> gioTien = new ArrayList<>();
    private LuoiBan luoiBan;

    private int banPhucVu = 0, tongBan = 0, donMoi = 0, donChoBep = 0, banChoTra = 0;
    private double doanhThuCa = 0;
    private final List<Object[]> viecCanLam = new ArrayList<>();
    private final List<Object[]> timeline = new ArrayList<>();
    private final List<Object[]> banList = new ArrayList<>();
    private boolean coCSDL = false;

    public EmployeeDashboardPanel() { this(null, null); }

    public EmployeeDashboardPanel(NguoiDung nguoiDung, Consumer<String> dieuHuong) {
        this.nguoiDung = nguoiDung;
        this.dieuHuong = dieuHuong;
        setOpaque(false);
        setLayout(new BorderLayout(0, 22));
        docDuLieu();
        dungGiaoDien();
        new Timer(30000, e -> { docDuLieu(); capNhat(); }).start();
    }

    private void di(String ma) {
        if (dieuHuong != null) dieuHuong.accept(ma);
    }

    private void dungGiaoDien() {
        add(khoiTren(), BorderLayout.NORTH);

        JPanel luoi = new JPanel(new GridLayout(2, 2, 18, 18));
        luoi.setOpaque(false);
        luoi.add(khoiBieuDo());
        luoi.add(khoiBan());
        luoi.add(khoiViec());
        luoi.add(khoiTimeline());
        add(luoi, BorderLayout.CENTER);
        capNhat();
    }

    private JPanel khoiTren() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 96));

        JPanel loi = new JPanel();
        loi.setOpaque(false);
        loi.setLayout(new BoxLayout(loi, BoxLayout.Y_AXIS));
        loi.add(EmployeeKit.chu(loiChao(), 12, Font.PLAIN, EmployeeKit.VANG));
        loi.add(Box.createRigidArea(new Dimension(0, 6)));
        JLabel ten = new JLabel(hoTen());
        ten.setFont(EmployeeKit.serif(30));
        ten.setForeground(EmployeeKit.CHU);
        ten.setAlignmentX(Component.LEFT_ALIGNMENT);
        loi.add(ten);
        p.add(loi, BorderLayout.WEST);

        daiTrangThai = new DaiTrangThai();
        p.add(daiTrangThai, BorderLayout.EAST);
        return p;
    }

    private class DaiTrangThai extends JComponent {
        DaiTrangThai() { setPreferredSize(new Dimension(880, 96)); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            String[][] o = {
                    {"BÀN ĐANG PHỤC VỤ", banPhucVu + " / " + tongBan, ""},
                    {"ĐƠN MỚI", String.valueOf(donMoi), ""},
                    {"CHỜ BẾP", String.valueOf(donChoBep), ""},
                    {"CHỜ THANH TOÁN", String.valueOf(banChoTra), ""},
                    {"DOANH THU CA", EmployeeKit.tien(doanhThuCa), ""}
            };
            Color[] m = {EmployeeKit.CHU, EmployeeKit.LAM, EmployeeKit.CAM,
                    EmployeeKit.DO, EmployeeKit.VANG_S};
            int w = getWidth(), h = getHeight();
            int cell = w / o.length;
            for (int i = 0; i < o.length; i++) {
                int x = i * cell;
                if (i > 0) {
                    g2.setColor(EmployeeKit.LINE);
                    g2.fillRect(x, 20, 1, h - 44);
                }
                g2.setFont(EmployeeKit.sans(9, Font.BOLD));
                g2.setColor(EmployeeKit.CHU_MO);
                g2.drawString(o[i][0], x + 18, 34);
                String v = o[i][1];
                int co = 28;
                while (co > 13) { g2.setFont(EmployeeKit.serif(co)); if (g2.getFontMetrics().stringWidth(v) <= cell - 34) break; co -= 1; }
                g2.setFont(EmployeeKit.serif(co));
                g2.setColor(m[i]);
                g2.drawString(EmployeeKit.cat(g2, v, cell - 30), x + 17, 68);
            }
            g2.dispose();
        }
    }

    private JPanel khoiBieuDo() {
        EmployeeKit.Khung k = new EmployeeKit.Khung(new BorderLayout(0, 12));
        JLabel tong = EmployeeKit.chu("", 20, Font.BOLD, EmployeeKit.VANG);
        tong.setFont(EmployeeKit.serif(24));
        lbTongCa = tong;
        k.add(EmployeeKit.tieuDe("Doanh thu theo giờ", "Các đơn đã hoàn thành trong ngày", tong),
                BorderLayout.NORTH);
        bieuDo = new BieuDoGio();
        k.add(bieuDo, BorderLayout.CENTER);
        return k;
    }

    private class BieuDoGio extends JComponent {
        private float anim = 0;
        private int hover = -1;
        private Timer tm;

        BieuDoGio() {
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    int n = gioLabel.size();
                    if (n == 0) return;
                    int i = (e.getX() - 46) * n / Math.max(1, getWidth() - 56);
                    hover = (i >= 0 && i < n) ? i : -1;
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseExited(MouseEvent e) { hover = -1; repaint(); }
            });
        }

        void chay() {
            anim = 0;
            if (tm != null) tm.stop();
            tm = new Timer(16, e -> {
                anim += .06f;
                if (anim >= 1) { anim = 1; tm.stop(); }
                repaint();
            });
            tm.start();
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight(), padL = 46, padB = 26, padT = 8;
            int n = gioLabel.size();
            if (n == 0) { g2.dispose(); return; }
            double max = 1;
            for (double v : gioTien) max = Math.max(max, v);

            g2.setFont(EmployeeKit.sans(9, Font.PLAIN));
            for (int i = 0; i <= 3; i++) {
                int y = padT + (int) ((H - padT - padB) * (1 - i / 3.0));
                g2.setColor(EmployeeKit.LINE);
                g2.drawLine(padL, y, W - 6, y);
                g2.setColor(EmployeeKit.CHU_MO);
                g2.drawString((long) (max * i / 3 / 1000) + "k", 6, y + 4);
            }

            int slot = (W - padL - 10) / n;
            int bw = Math.max(6, Math.min(26, slot - 8));
            for (int i = 0; i < n; i++) {
                double v = gioTien.get(i) * anim;
                int bh = (int) ((H - padT - padB) * (v / max));
                int x = padL + slot * i + (slot - bw) / 2;
                int y = H - padB - bh;
                boolean hot = i == hover;
                g2.setPaint(new GradientPaint(0, y, hot ? EmployeeKit.VANG_S : EmployeeKit.VANG,
                        0, H - padB, EmployeeKit.mo(EmployeeKit.VANG, hot ? 150 : 90)));
                g2.fillRoundRect(x, y, bw, Math.max(bh, 2), 3, 3);
                if (n <= 16 || i % 2 == 0) {
                    g2.setColor(EmployeeKit.CHU_MO);
                    g2.setFont(EmployeeKit.sans(9, Font.PLAIN));
                    String lb = gioLabel.get(i);
                    g2.drawString(lb, padL + slot * i + (slot - g2.getFontMetrics().stringWidth(lb)) / 2,
                            H - 8);
                }
                if (hot) {
                    String t = EmployeeKit.tien(gioTien.get(i));
                    g2.setFont(EmployeeKit.sans(11, Font.BOLD));
                    int tw = g2.getFontMetrics().stringWidth(t) + 16;
                    int bx = Math.min(W - tw - 4, Math.max(4, x + bw / 2 - tw / 2));
                    int by = Math.max(2, y - 26);
                    g2.setColor(EmployeeKit.CHU);
                    g2.fillRoundRect(bx, by, tw, 22, 3, 3);
                    g2.setColor(EmployeeKit.LOP);
                    g2.drawString(t, bx + 8, by + 15);
                }
            }
            g2.dispose();
        }
    }

    private JPanel khoiViec() {
        EmployeeKit.Khung k = new EmployeeKit.Khung(new BorderLayout(0, 14));
        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        phai.setOpaque(false);
        EmployeeKit.Nut n1 = new EmployeeKit.Nut("Mở sơ đồ bàn", EmployeeKit.Nut.VIEN_MANH);
        n1.addActionListener(e -> di("tables"));
        EmployeeKit.Nut n2 = new EmployeeKit.Nut("Xem đơn hàng", EmployeeKit.Nut.VIEN_MANH);
        n2.addActionListener(e -> di("orders"));
        phai.add(n1);
        phai.add(n2);
        k.add(EmployeeKit.tieuDe("Việc cần xử lý", "Ưu tiên theo thời gian chờ", phai), BorderLayout.NORTH);

        dsViec.setOpaque(false);
        dsViec.setLayout(new BoxLayout(dsViec, BoxLayout.Y_AXIS));
        JPanel g = new JPanel(new BorderLayout());
        g.setOpaque(false);
        g.add(dsViec, BorderLayout.NORTH);
        k.add(EmployeeKit.cuon(g), BorderLayout.CENTER);
        return k;
    }

    private JPanel khoiBan() {
        EmployeeKit.Khung k = new EmployeeKit.Khung(new BorderLayout(0, 14));
        EmployeeKit.Nut n = new EmployeeKit.Nut("Chi tiết", EmployeeKit.Nut.CHU_KHONG);
        n.addActionListener(e -> di("tables"));
        k.add(EmployeeKit.tieuDe("Tình trạng bàn", "Toàn nhà hàng", n), BorderLayout.NORTH);
        luoiBan = new LuoiBan();
        k.add(luoiBan, BorderLayout.CENTER);
        k.add(chuThich(), BorderLayout.SOUTH);
        return k;
    }

    private JPanel chuThich() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 0, 0));
        p.add(cham("Trống", EmployeeKit.LUC));
        p.add(cham("Đang phục vụ", EmployeeKit.CAM));
        p.add(cham("Chờ thanh toán", EmployeeKit.DO));
        p.add(cham("Đặt trước", EmployeeKit.LAM));
        return p;
    }

    private JComponent cham(String s, Color c) {
        JLabel l = new JLabel(s) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c);
                g2.fillRect(0, getHeight() / 2 - 3, 7, 7);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(EmployeeKit.sans(10, Font.PLAIN));
        l.setForeground(EmployeeKit.CHU_MO);
        l.setBorder(new EmptyBorder(0, 13, 0, 0));
        return l;
    }

    private class LuoiBan extends JComponent {
        private int hover = -1;

        LuoiBan() {
            setOpaque(true);
            setBackground(EmployeeKit.LOP);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText(null);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    int i = tim(e.getX(), e.getY());
                    if (i != hover) {
                        hover = i;
                        repaint();
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mouseExited(MouseEvent e) {
                    hover = -1;
                    repaint();
                }

                @Override public void mousePressed(MouseEvent e) {
                    int i = tim(e.getX(), e.getY());
                    if (i >= 0) di("tables");
                }
            });
        }

        private int cot() {
            return Math.max(4, Math.min(8, getWidth() / 72));
        }

        private int oRong() {
            int c = cot();
            return Math.max(48, (getWidth() - 8 * (c - 1)) / c);
        }

        private int tim(int x, int y) {
            int c = cot();
            int o = oRong();
            int gap = 8;

            for (int i = 0; i < banList.size(); i++) {
                int r = i / c;
                int cc = i % c;
                int bx = cc * (o + gap);
                int by = r * 52;

                if (x >= bx && x <= bx + o && y >= by && y <= by + 46) {
                    return i;
                }
            }

            return -1;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            int c = cot();
            int o = oRong();
            int gap = 8;

            for (int i = 0; i < banList.size(); i++) {
                Object[] b = banList.get(i);
                int r = i / c;
                int cc = i % c;
                int x = cc * (o + gap);
                int y = r * 52;

                if (y + 46 > getHeight()) break;

                Color m = mauBan((String) b[1]);
                boolean hot = i == hover;

                if (hot) {
                    g2.setColor(EmployeeKit.mo(m, 34));
                    g2.fillRoundRect(x, y, o, 46, 5, 5);
                    g2.setColor(m);
                    g2.setStroke(new BasicStroke(1.6f));
                    g2.drawRoundRect(x, y, o, 46, 5, 5);
                } else {
                    g2.setColor(EmployeeKit.mo(m, 10));
                    g2.fillRoundRect(x, y, o, 46, 5, 5);
                    g2.setColor(EmployeeKit.mo(m, 150));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(x, y, o, 46, 5, 5);
                }

                g2.setColor(m);
                g2.fillRoundRect(x, y, o, 2, 2, 2);

                g2.setFont(EmployeeKit.sans(13, Font.BOLD));
                g2.setColor(EmployeeKit.CHU);
                String t = String.valueOf(b[0]);
                int tw = g2.getFontMetrics().stringWidth(t);
                g2.drawString(t, x + (o - tw) / 2, y + 24);

                g2.setFont(EmployeeKit.sans(9, Font.PLAIN));
                g2.setColor(EmployeeKit.CHU_MO);
                String d = String.valueOf(b[2]);
                int dw = g2.getFontMetrics().stringWidth(d);
                g2.drawString(d, x + (o - dw) / 2, y + 38);
            }

            g2.dispose();
        }
    }

    private Color mauBan(String tt) {
        if (tt == null) return EmployeeKit.LUC;
        switch (tt) {
            case "DANG_PHUC_VU": return EmployeeKit.CAM;
            case "CHO_THANH_TOAN": return EmployeeKit.DO;
            case "DAT_TRUOC": return EmployeeKit.LAM;
            default: return EmployeeKit.LUC;
        }
    }

    private JPanel khoiTimeline() {
        EmployeeKit.Khung k = new EmployeeKit.Khung(new BorderLayout(0, 12));
        k.add(EmployeeKit.tieuDe("Hoạt động trong ca", "Mới nhất ở trên cùng", null), BorderLayout.NORTH);
        dsTimeline.setOpaque(false);
        dsTimeline.setLayout(new BoxLayout(dsTimeline, BoxLayout.Y_AXIS));
        JPanel g = new JPanel(new BorderLayout());
        g.setOpaque(false);
        g.add(dsTimeline, BorderLayout.NORTH);
        k.add(EmployeeKit.cuon(g), BorderLayout.CENTER);
        return k;
    }

    private class DongViec extends JComponent {
        final Object[] d;
        private boolean hover;

        DongViec(Object[] d) {
            this.d = d;
            setPreferredSize(new Dimension(600, 56));
            setMaximumSize(new Dimension(9999, 56));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { di((String) d[5]); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color m = (Color) d[4];
            if (hover) {
                g2.setColor(EmployeeKit.mo(Color.WHITE, 8));
                g2.fillRect(0, 0, w, h - 1);
            }
            g2.setColor(EmployeeKit.LINE);
            g2.fillRect(0, h - 1, w, 1);
            g2.setColor(m);
            g2.fillRect(0, 14, 2, h - 29);

            g2.setFont(EmployeeKit.sans(13, Font.BOLD));
            g2.setColor(EmployeeKit.CHU);
            g2.drawString(EmployeeKit.cat(g2, (String) d[0], w - 300), 20, 27);

            g2.setFont(EmployeeKit.sans(11, Font.PLAIN));
            g2.setColor(EmployeeKit.CHU_MO);
            g2.drawString(EmployeeKit.cat(g2, (String) d[1], w - 300), 20, 45);

            String nhan = (String) d[2];
            int nw = EmployeeKit.rongNhan(g2, nhan);
            EmployeeKit.nhanTrangThai(g2, nhan, w - nw - 116, h / 2 - 11, m);

            g2.setFont(EmployeeKit.mono(12, Font.BOLD));
            g2.setColor(m);
            String t = (String) d[3];
            g2.drawString(t, w - 96 + (60 - g2.getFontMetrics().stringWidth(t)) / 2, h / 2 + 4);

            if (hover) EmployeeKit.Ic.ve(g2, "mui", w - 24, h / 2 - 8, 16, EmployeeKit.VANG);
            g2.dispose();
        }
    }

    private class MocTimeline extends JComponent {
        final Object[] d;

        MocTimeline(Object[] d) {
            this.d = d;
            setPreferredSize(new Dimension(400, 46));
            setMaximumSize(new Dimension(9999, 46));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color m = (Color) d[3];

            g2.setFont(EmployeeKit.mono(11, Font.BOLD));
            g2.setColor(EmployeeKit.CHU_MO);
            g2.drawString((String) d[0], 0, h / 2 + 4);

            g2.setColor(EmployeeKit.LINE);
            g2.fillRect(42, 0, 1, h);
            g2.setColor(m);
            g2.fillRect(39, h / 2 - 3, 7, 7);

            g2.setFont(EmployeeKit.sans(12, Font.BOLD));
            g2.setColor(EmployeeKit.CHU);
            g2.drawString(EmployeeKit.cat(g2, (String) d[1], w - 150), 58, h / 2 - 1);

            g2.setFont(EmployeeKit.sans(10, Font.PLAIN));
            g2.setColor(EmployeeKit.CHU_MO);
            g2.drawString(EmployeeKit.cat(g2, (String) d[2], w - 150), 58, h / 2 + 14);

            g2.setColor(EmployeeKit.LINE);
            g2.fillRect(58, h - 1, w - 58, 1);
            g2.dispose();
        }
    }

    private void docDuLieu() {
        viecCanLam.clear();
        timeline.clear();
        gioLabel.clear();
        gioTien.clear();
        banList.clear();
        banPhucVu = tongBan = donMoi = donChoBep = banChoTra = 0;
        doanhThuCa = 0;

        try (Connection c = DatabaseConnection.getConnection(); Statement st = c.createStatement()) {
            try (ResultSet r = st.executeQuery("SELECT ma_ban_so, trang_thai, suc_chua FROM ban_an ORDER BY ma_ban")) {
                while (r.next()) {
                    String tt = r.getString(2);
                    banList.add(new Object[]{r.getString(1), tt, r.getInt(3) + " chỗ"});
                    tongBan++;
                    if (!"TRONG".equals(tt)) banPhucVu++;
                }
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM don_hang WHERE trang_thai='CHO_XU_LY'")) {
                if (r.next()) donMoi = r.getInt(1);
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(DISTINCT c.ma_don_hang) FROM chi_tiet_don_hang c "
                    + "WHERE c.trang_thai_mon='CHO_NAU'")) {
                if (r.next()) donChoBep = r.getInt(1);
            }
            try (ResultSet r = st.executeQuery("SELECT COUNT(*) FROM don_hang WHERE trang_thai='DANG_PHUC_VU'")) {
                if (r.next()) banChoTra = r.getInt(1);
            }
            try (ResultSet r = st.executeQuery("SELECT IFNULL(SUM(tong_tien),0) FROM don_hang "
                    + "WHERE trang_thai='HOAN_THANH' AND DATE(ngay_tao)=CURDATE()")) {
                if (r.next()) doanhThuCa = r.getDouble(1);
            }
            try (ResultSet r = st.executeQuery("SELECT HOUR(ngay_tao) h, SUM(tong_tien) t FROM don_hang "
                    + "WHERE trang_thai='HOAN_THANH' AND DATE(ngay_tao)=CURDATE() GROUP BY h ORDER BY h")) {
                while (r.next()) {
                    gioLabel.add(String.format("%02dh", r.getInt(1)));
                    gioTien.add(r.getDouble(2));
                }
            }
            try (ResultSet r = st.executeQuery("SELECT d.ma_don, IFNULL(b.ten_ban,'Mang về'), d.trang_thai, "
                    + "d.tong_tien, TIMESTAMPDIFF(MINUTE, d.ngay_tao, NOW()) FROM don_hang d "
                    + "LEFT JOIN ban_an b ON b.ma_ban=d.ma_ban "
                    + "WHERE d.trang_thai IN ('CHO_XU_LY','DANG_PHUC_VU') ORDER BY d.ngay_tao LIMIT 10")) {
                while (r.next()) {
                    String tt = r.getString(3);
                    boolean cho = "CHO_XU_LY".equals(tt);
                    int phut = Math.max(0, r.getInt(5));
                    viecCanLam.add(new Object[]{
                            r.getString(2) + "  ·  " + r.getString(1),
                            cho ? "Đơn mới, cần xác nhận và gửi bếp"
                                : "Đang phục vụ · " + EmployeeKit.tien(r.getDouble(4)),
                            cho ? "Chờ xử lý" : "Chờ thanh toán",
                            phut + "′",
                            cho ? EmployeeKit.LAM : (phut > 60 ? EmployeeKit.DO : EmployeeKit.CAM),
                            cho ? "orders" : "payment"});
                }
            }
            try (ResultSet r = st.executeQuery("SELECT TIME_FORMAT(d.ngay_tao,'%H:%i'), d.ma_don, "
                    + "IFNULL(b.ten_ban,'Mang về'), d.trang_thai, d.tong_tien FROM don_hang d "
                    + "LEFT JOIN ban_an b ON b.ma_ban=d.ma_ban ORDER BY d.ma_don_hang DESC LIMIT 8")) {
                while (r.next()) {
                    String tt = r.getString(4);
                    timeline.add(new Object[]{r.getString(1),
                            "HOAN_THANH".equals(tt) ? "Thanh toán " + r.getString(2) : "Mở " + r.getString(3),
                            "HOAN_THANH".equals(tt) ? EmployeeKit.tien(r.getDouble(5)) : r.getString(2),
                            "HOAN_THANH".equals(tt) ? EmployeeKit.LUC : EmployeeKit.CAM});
                }
            }
            coCSDL = true;
        } catch (Exception e) {
            coCSDL = false;
        }

        if (!coCSDL || banList.isEmpty()) {
            String[] tt = {"TRONG", "DANG_PHUC_VU", "TRONG", "DANG_PHUC_VU", "TRONG", "CHO_THANH_TOAN",
                    "TRONG", "DAT_TRUOC", "DANG_PHUC_VU", "TRONG", "DAT_TRUOC", "TRONG"};
            banList.clear();
            tongBan = banPhucVu = 0;
            for (int i = 0; i < tt.length; i++) {
                banList.add(new Object[]{"B" + String.format("%02d", i + 1), tt[i], (2 + (i % 4) * 2) + " chỗ"});
                tongBan++;
                if (!"TRONG".equals(tt[i])) banPhucVu++;
            }
            donMoi = 3; donChoBep = 5; banChoTra = 2; doanhThuCa = 4260000;
        }
        if (gioLabel.isEmpty()) {
            String[] gg = {"10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h", "21h"};
            double[] vv = {180000, 420000, 960000, 640000, 210000, 150000,
                    260000, 480000, 880000, 1250000, 1020000, 540000};
            double tong = 0;
            for (int i = 0; i < gg.length; i++) { gioLabel.add(gg[i]); gioTien.add(vv[i]); tong += vv[i]; }
            if (doanhThuCa <= 0) doanhThuCa = tong;
        }
        if (viecCanLam.isEmpty()) {
            viecCanLam.add(new Object[]{"Bàn 05  ·  DH00281", "Đơn mới, cần xác nhận và gửi bếp",
                    "Chờ xử lý", "3′", EmployeeKit.LAM, "orders"});
            viecCanLam.add(new Object[]{"Bàn 09  ·  DH00279", "Đang phục vụ · 1.305.000 đ",
                    "Chờ thanh toán", "72′", EmployeeKit.DO, "payment"});
            viecCanLam.add(new Object[]{"Bàn 02  ·  DH00280", "Đang phục vụ · 640.000 đ",
                    "Chờ thanh toán", "28′", EmployeeKit.CAM, "payment"});
        }
        if (timeline.isEmpty()) {
            timeline.add(new Object[]{"19:42", "Thanh toán DH00277", "1.468.800 đ", EmployeeKit.LUC});
            timeline.add(new Object[]{"19:31", "Mở Bàn 13", "DH00280", EmployeeKit.CAM});
            timeline.add(new Object[]{"19:14", "Thanh toán DH00276", "982.400 đ", EmployeeKit.LUC});
            timeline.add(new Object[]{"18:55", "Mở Bàn 09", "DH00279", EmployeeKit.CAM});
        }
    }

    private void capNhat() {
        dsViec.removeAll();
        if (viecCanLam.isEmpty()) {
            JLabel l = EmployeeKit.chu("Không còn việc nào cần xử lý — ca đang trôi chảy.",
                    12, Font.PLAIN, EmployeeKit.CHU_MO);
            l.setBorder(new EmptyBorder(28, 4, 0, 0));
            dsViec.add(l);
        }
        for (Object[] o : viecCanLam) dsViec.add(new DongViec(o));
        dsViec.revalidate();
        dsViec.repaint();

        dsTimeline.removeAll();
        for (Object[] o : timeline) dsTimeline.add(new MocTimeline(o));
        dsTimeline.revalidate();
        dsTimeline.repaint();

        if (lbTongCa != null) lbTongCa.setText(EmployeeKit.tien(doanhThuCa));
        if (bieuDo != null) bieuDo.chay();
        if (daiTrangThai != null) daiTrangThai.repaint();
        if (luoiBan != null) luoiBan.repaint();
    }

    private String hoTen() {
        return nguoiDung == null || nguoiDung.getHoTen() == null ? "Lê Văn Phục Vụ" : nguoiDung.getHoTen();
    }

    private String loiChao() {
        int h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (h < 11) return "Chào buổi sáng,";
        if (h < 14) return "Chào buổi trưa,";
        if (h < 18) return "Chào buổi chiều,";
        return "Chào buổi tối,";
    }
}