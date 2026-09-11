package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class QuanLyBanPanel extends JPanel {

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

    static final String TRONG = "Trống";
    static final String PHUC_VU = "Đang phục vụ";
    static final String DAT = "Đã đặt";
    static final String DON = "Cần dọn";

    private final List<Ban> danhSach = new ArrayList<>();
    private final JPanel luoi = new JPanel(new GridLayout(0, 5, 16, 16));
    private final JPanel oThongKe = new JPanel(new GridLayout(1, 4, 16, 0));
    private final JPanel oLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private String locKhu = "Tất cả", locTrangThai = "Tất cả";
    private boolean dungCSDL = false;
    private final OTim tim = new OTim();

    public QuanLyBanPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        napDuLieu();

        JPanel tren = new JPanel(new BorderLayout(0, 16));
        tren.setOpaque(false);
        oThongKe.setOpaque(false);
        oThongKe.setPreferredSize(new Dimension(100, 104));
        tren.add(oThongKe, BorderLayout.NORTH);
        tren.add(thanhLoc(), BorderLayout.SOUTH);
        add(tren, BorderLayout.NORTH);

        luoi.setOpaque(false);
        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(luoi, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(giu);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        sp.getVerticalScrollBar().setUI(new Cuon());
        add(sp, BorderLayout.CENTER);

        veLai();
        new Timer(80, e -> { if (isShowing()) luoi.repaint(); }).start();
    }

    private void napDuLieu() {
        danhSach.clear();
        if (napTuCSDL()) { dungCSDL = true; return; }
        dungCSDL = false;
        napMau();
    }

    private boolean napTuCSDL() {
        String sql = "SELECT ma_ban, ma_ban_so, ten_ban, suc_chua, khu_vuc, trang_thai FROM ban_an ORDER BY ma_ban";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ban b = new Ban();
                b.maBan = rs.getInt("ma_ban");
                b.id = b.maBan;
                b.maSo = rs.getString("ma_ban_so");
                b.ten = rs.getString("ten_ban");
                b.soCho = rs.getInt("suc_chua");
                b.khu = rs.getString("khu_vuc");
                if (b.khu == null) b.khu = "Chưa phân khu";
                b.trangThai = hienTrangThai(rs.getString("trang_thai"));
                danhSach.add(b);
            }
            return !danhSach.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    static String hienTrangThai(String db) {
        if (db == null) return TRONG;
        String v = db.trim().toUpperCase().replace(' ', '_');
        if (v.startsWith("DANG") || v.contains("SU_DUNG") || v.contains("PHUC_VU")) return PHUC_VU;
        if (v.startsWith("DAT") || v.contains("DAT_TRUOC")) return DAT;
        if (v.contains("DON")) return DON;
        return TRONG;
    }

    static String maTrangThai(String hien) {
        if (PHUC_VU.equals(hien)) return "DANG_PHUC_VU";
        if (DAT.equals(hien)) return "DAT_TRUOC";
        if (DON.equals(hien)) return "CAN_DON";
        return "TRONG";
    }

    private void luuTrangThai(Ban b) {
        if (!dungCSDL) return;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE ban_an SET trang_thai=? WHERE ma_ban=?")) {
            ps.setString(1, maTrangThai(b.trangThai));
            ps.setInt(2, b.maBan);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không cập nhật được trạng thái bàn:\n" + e.getMessage());
        }
    }

    private void napMau() {
        String[] khu = {"Tầng 1", "Tầng 1", "Tầng 1", "Tầng 1", "Tầng 1", "Tầng 1", "Tầng 2", "Tầng 2",
                "Tầng 2", "Tầng 2", "Sân vườn", "Sân vườn"};
        int[] cho = {2, 4, 4, 6, 4, 8, 2, 4, 6, 8, 4, 6};
        Random r = new Random(9);
        for (int i = 0; i < khu.length; i++) {
            Ban b = new Ban();
            b.maBan = i + 1;
            b.id = i + 1;
            b.maSo = "B" + String.format("%02d", i + 1);
            b.ten = "Bàn " + String.format("%02d", i + 1);
            b.khu = khu[i];
            b.soCho = cho[i];
            int k = r.nextInt(10);
            b.trangThai = k < 5 ? PHUC_VU : (k < 8 ? TRONG : (k < 9 ? DAT : DON));
            if (b.trangThai.equals(PHUC_VU)) {
                b.phut = 8 + r.nextInt(95);
                b.tien = (2 + r.nextInt(18)) * 125000;
                b.khach = 1 + r.nextInt(b.soCho);
            }
            danhSach.add(b);
        }
    }

    private JPanel thanhLoc() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 52));
        oLoc.setOpaque(false);
        dungChip();
        p.add(oLoc, BorderLayout.CENTER);

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        phai.setOpaque(false);
        tim.setPreferredSize(new Dimension(220, 40));
        tim.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { veLai(); }
        });
        Nut lam = new Nut("Làm mới", false);
        lam.addActionListener(e -> { napDuLieu(); veLai(); });
        Nut them = new Nut("+  Thêm bàn", true);
        them.addActionListener(e -> themBan());
        phai.add(tim);
        phai.add(lam);
        phai.add(them);
        p.add(phai, BorderLayout.EAST);
        return p;
    }

    private void dungChip() {
        oLoc.removeAll();
        java.util.LinkedHashSet<String> tapKhu = new java.util.LinkedHashSet<>();
        tapKhu.add("Tất cả");
        for (Ban b : danhSach) tapKhu.add(b.khu);
        for (String k : tapKhu) {
            Chip c = new Chip(k, k.equals(locKhu), NGOC);
            c.addActionListener(e -> { locKhu = k; dungChip(); veLai(); });
            oLoc.add(c);
        }
        oLoc.add(Box.createRigidArea(new Dimension(14, 1)));
        String[] tt = {"Tất cả", TRONG, PHUC_VU, DAT, DON};
        for (String t : tt) {
            Chip c = new Chip(t, t.equals(locTrangThai), mauTrangThai(t));
            c.addActionListener(e -> { locTrangThai = t; dungChip(); veLai(); });
            oLoc.add(c);
        }
        oLoc.revalidate();
        oLoc.repaint();
    }

    static Color mauTrangThai(String t) {
        if (PHUC_VU.equals(t)) return CAM;
        if (DAT.equals(t)) return LAM;
        if (DON.equals(t)) return DO;
        if (TRONG.equals(t)) return LUC;
        return NGOC;
    }

    private void xaoTrang() {
        Random r = new Random();
        for (Ban b : danhSach) {
            if (b.trangThai.equals(PHUC_VU)) {
                b.phut += r.nextInt(6);
                b.tien += r.nextInt(3) * 65000;
            }
        }
    }

    private void veLai() {
        int tong = danhSach.size(), trong = 0, pv = 0, dat = 0;
        for (Ban b : danhSach) {
            if (b.trangThai.equals(TRONG)) trong++;
            else if (b.trangThai.equals(PHUC_VU)) pv++;
            else if (b.trangThai.equals(DAT)) dat++;
        }
        oThongKe.removeAll();
        oThongKe.add(new TheSo("Tổng số bàn", String.valueOf(tong), "Toàn bộ khu vực", NGOC));
        oThongKe.add(new TheSo("Bàn trống", String.valueOf(trong), "Sẵn sàng đón khách", LUC));
        oThongKe.add(new TheSo("Đang phục vụ", String.valueOf(pv),
                Math.round(pv * 100f / tong) + "% công suất", CAM));
        oThongKe.add(new TheSo("Đã đặt trước", String.valueOf(dat), "Chờ khách tới nhận bàn", LAM));
        oThongKe.revalidate();
        oThongKe.repaint();

        String q = tim.getText().trim().toLowerCase();
        luoi.removeAll();
        for (Ban b : danhSach) {
            if (!locKhu.equals("Tất cả") && !b.khu.equals(locKhu)) continue;
            if (!locTrangThai.equals("Tất cả") && !b.trangThai.equals(locTrangThai)) continue;
            if (!q.isEmpty() && !b.ten.toLowerCase().contains(q) && !b.khu.toLowerCase().contains(q)) continue;
            luoi.add(new TheBan(b));
        }
        luoi.revalidate();
        luoi.repaint();
    }

    private void themBan() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 10));
        p.setOpaque(false);
        JTextField maSo = new JTextField("B" + String.format("%02d", danhSach.size() + 1));
        JTextField ten = new JTextField("Bàn " + String.format("%02d", danhSach.size() + 1));
        JTextField khu = new JTextField("Tầng 1");
        JSpinner cho = new JSpinner(new SpinnerNumberModel(4, 1, 30, 1));
        p.add(new JLabel("Mã bàn")); p.add(maSo);
        p.add(new JLabel("Tên bàn")); p.add(ten);
        p.add(new JLabel("Khu vực")); p.add(khu);
        p.add(new JLabel("Sức chứa")); p.add(cho);
        int r = JOptionPane.showConfirmDialog(this, p, "Thêm bàn mới",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) return;

        Ban b = new Ban();
        b.maSo = maSo.getText().trim();
        b.ten = ten.getText().trim();
        b.khu = khu.getText().trim();
        b.soCho = (Integer) cho.getValue();
        b.trangThai = TRONG;

        if (dungCSDL) {
            try (Connection c = DatabaseConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "INSERT INTO ban_an (ma_ban_so, ten_ban, suc_chua, khu_vuc, trang_thai) VALUES (?,?,?,?,'TRONG')",
                         Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, b.maSo);
                ps.setString(2, b.ten);
                ps.setInt(3, b.soCho);
                ps.setString(4, b.khu);
                ps.executeUpdate();
                napDuLieu();
                veLai();
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thêm được bàn:\n" + e.getMessage());
                return;
            }
        }
        b.maBan = danhSach.size() + 1;
        b.id = b.maBan;
        danhSach.add(b);
        veLai();
    }

    private void moChiTiet(Ban b) {
        Window w = SwingUtilities.getWindowAncestor(this);
        JDialog d = new JDialog(w, b.ten, Dialog.ModalityType.APPLICATION_MODAL);
        JPanel goc = new JPanel(new BorderLayout(0, 16)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, getHeight(), NEN));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        goc.setBorder(new EmptyBorder(24, 26, 22, 26));

        JPanel dau = new JPanel(new BorderLayout());
        dau.setOpaque(false);
        JPanel tt = new JPanel();
        tt.setOpaque(false);
        tt.setLayout(new BoxLayout(tt, BoxLayout.Y_AXIS));
        tt.add(nhan(b.ten, 24, Font.BOLD, CHU));
        tt.add(Box.createRigidArea(new Dimension(0, 6)));
        tt.add(nhan(b.khu + "  •  " + b.soCho + " chỗ ngồi", 12, Font.PLAIN, CHU_MO));
        dau.add(tt, BorderLayout.WEST);
        dau.add(new Vien(b.trangThai), BorderLayout.EAST);
        goc.add(dau, BorderLayout.NORTH);

        JPanel than = new JPanel(new GridLayout(2, 2, 12, 12));
        than.setOpaque(false);
        than.add(oNho("Trạng thái", b.trangThai, mauTrangThai(b.trangThai)));
        than.add(oNho("Số khách", b.khach > 0 ? b.khach + " người" : "—", NGOC));
        than.add(oNho("Thời gian", b.phut > 0 ? b.phut + " phút" : "—", CAM));
        than.add(oNho("Tạm tính", b.tien > 0 ? tienVN(b.tien) : "—", LUC));
        goc.add(than, BorderLayout.CENTER);

        JPanel nut = new JPanel(new GridLayout(1, 3, 10, 0));
        nut.setOpaque(false);
        Nut n1 = new Nut(b.trangThai.equals(TRONG) ? "Mở bàn" : "Gọi thêm món", true);
        Nut n2 = new Nut(b.trangThai.equals(DAT) ? "Nhận bàn" : "Đặt trước", false);
        Nut n3 = new Nut(b.trangThai.equals(PHUC_VU) ? "Thanh toán" : "Dọn xong", false);
        n1.addActionListener(e -> {
            if (b.trangThai.equals(TRONG)) { b.trangThai = PHUC_VU; b.phut = 1; b.khach = 2; }
            luuTrangThai(b); d.dispose(); veLai();
        });
        n2.addActionListener(e -> {
            b.trangThai = b.trangThai.equals(DAT) ? PHUC_VU : DAT;
            if (b.trangThai.equals(PHUC_VU)) b.phut = 1;
            luuTrangThai(b); d.dispose(); veLai();
        });
        n3.addActionListener(e -> {
            b.trangThai = b.trangThai.equals(PHUC_VU) ? DON : TRONG;
            b.tien = 0; b.phut = 0; b.khach = 0; b.ghiChu = "";
            luuTrangThai(b); d.dispose(); veLai();
        });
        nut.add(n1); nut.add(n2); nut.add(n3);
        goc.add(nut, BorderLayout.SOUTH);

        d.setContentPane(goc);
        d.setSize(520, 420);
        d.setLocationRelativeTo(w);
        d.setVisible(true);
    }

    private JPanel oNho(String k, String v, Color m) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(KHOI);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(VIEN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(14, 16, 14, 16));
        JPanel in = new JPanel();
        in.setOpaque(false);
        in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
        in.add(nhan(k.toUpperCase(), 10, Font.BOLD, CHU_MO));
        in.add(Box.createRigidArea(new Dimension(0, 8)));
        in.add(nhan(v, 17, Font.BOLD, m));
        p.add(in, BorderLayout.CENTER);
        return p;
    }

    static JLabel nhan(String s, int co, int kieu, Color m) {
        JLabel l = new JLabel(s);
        l.setFont(new Font(FONT, kieu, co));
        l.setForeground(m);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private class TheBan extends JComponent {
        Ban b;
        boolean hover = false;
        float nhip = 0;

        TheBan(Ban b) {
            this.b = b;
            setPreferredSize(new Dimension(210, 196));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText("<html><b>" + b.ten + "</b><br>" + b.khu + " • " + b.soCho + " chỗ</html>");
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { moChiTiet(b); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color m = mauTrangThai(b.trangThai);
            nhip += 0.05f;

            if (b.trangThai.equals(PHUC_VU)) {
                float k = (float) (0.5 + 0.5 * Math.sin(nhip));
                g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), (int) (16 + 22 * k)));
                g2.fillRoundRect(0, 0, w - 1, h - 1, 20, 20);
            }
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(3, 5, w - 6, h - 6, 18, 18);
            g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, h, KHOI));
            g2.fillRoundRect(2, 2, w - 6, h - 10, 18, 18);
            g2.setStroke(new BasicStroke(hover ? 2f : 1.1f));
            g2.setColor(hover ? m : VIEN);
            g2.drawRoundRect(2, 2, w - 6, h - 10, 18, 18);

            g2.setFont(new Font(FONT, Font.BOLD, 15));
            g2.setColor(CHU);
            g2.drawString(b.ten, 18, 32);

            g2.setFont(new Font(FONT, Font.BOLD, 10));
            String tt = b.trangThai.toUpperCase();
            int tw = g2.getFontMetrics().stringWidth(tt) + 18;
            g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 40));
            g2.fillRoundRect(w - tw - 18, 16, tw, 21, 10, 10);
            g2.setColor(m);
            g2.drawString(tt, w - tw - 9, 31);

            veBanAn(g2, w / 2, 98, m, b.soCho);

            g2.setColor(new Color(255, 255, 255, 14));
            g2.drawLine(16, h - 54, w - 18, h - 54);

            g2.setFont(new Font(FONT, Font.PLAIN, 11));
            g2.setColor(CHU_MO);
            g2.drawString(b.soCho + " chỗ • " + b.khu, 18, h - 34);

            if (b.trangThai.equals(PHUC_VU)) {
                g2.setColor(CHU_MO);
                g2.drawString(b.phut + " phút", 18, h - 16);
                g2.setFont(new Font(FONT, Font.BOLD, 13));
                g2.setColor(LUC);
                String s = tienVN(b.tien);
                g2.drawString(s, w - 18 - g2.getFontMetrics().stringWidth(s), h - 15);
            } else if (b.trangThai.equals(DAT)) {
                g2.setColor(LAM);
                g2.drawString(b.ghiChu == null ? "Đã đặt trước" : b.ghiChu, 18, h - 16);
            } else if (b.trangThai.equals(DON)) {
                g2.setColor(DO);
                g2.drawString("Cần dọn dẹp trước khi nhận khách", 18, h - 16);
            } else {
                g2.setColor(hover ? NGOC : CHU_MO);
                g2.drawString(hover ? "Nhấn để mở bàn →" : "Sẵn sàng đón khách", 18, h - 16);
            }
            g2.dispose();
        }

        private void veBanAn(Graphics2D g2, int cx, int cy, Color m, int soCho) {
            int n = Math.min(10, Math.max(2, soCho));
            int r = soCho >= 8 ? 30 : 26;
            g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 70));
            for (int i = 0; i < n; i++) {
                double a = Math.PI * 2 * i / n - Math.PI / 2;
                int gx = (int) (cx + Math.cos(a) * (r + 16));
                int gy = (int) (cy + Math.sin(a) * (r + 14));
                g2.fillRoundRect(gx - 7, gy - 7, 14, 14, 5, 5);
            }
            g2.setPaint(new GradientPaint(cx - r, cy - r, new Color(m.getRed(), m.getGreen(), m.getBlue(), 70),
                    cx + r, cy + r, new Color(m.getRed(), m.getGreen(), m.getBlue(), 26)));
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(m);
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            g2.setFont(new Font(FONT, Font.BOLD, 15));
            g2.setColor(CHU);
            String s = b.maSo == null || b.maSo.isEmpty() ? String.valueOf(b.id) : b.maSo;
            g2.drawString(s, cx - g2.getFontMetrics().stringWidth(s) / 2, cy + 5);
        }
    }

    private class TheSo extends JPanel {
        TheSo(String nhan, String gt, String phu, Color m) {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(16, 20, 16, 20));
            JPanel in = new JPanel();
            in.setOpaque(false);
            in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
            in.add(QuanLyBanPanel.nhan(nhan.toUpperCase(), 10, Font.BOLD, CHU_MO));
            in.add(Box.createRigidArea(new Dimension(0, 8)));
            in.add(QuanLyBanPanel.nhan(gt, 26, Font.BOLD, m));
            in.add(Box.createRigidArea(new Dimension(0, 6)));
            in.add(QuanLyBanPanel.nhan(phu, 11, Font.PLAIN, CHU_MO));
            add(in, BorderLayout.CENTER);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0, 0, 0, 26));
            g2.fillRoundRect(2, 4, w - 4, h - 4, 16, 16);
            g2.setPaint(new GradientPaint(0, 0, KHOI_2, 0, h, KHOI));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2.setColor(VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class Vien extends JComponent {
        String s;
        Vien(String s) { this.s = s; setPreferredSize(new Dimension(140, 34)); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color m = mauTrangThai(s);
            g2.setFont(new Font(FONT, Font.BOLD, 12));
            int tw = g2.getFontMetrics().stringWidth(s) + 26;
            g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 40));
            g2.fillRoundRect(140 - tw, 4, tw, 28, 14, 14);
            g2.setColor(m);
            g2.drawRoundRect(140 - tw, 4, tw, 28, 14, 14);
            g2.drawString(s, 140 - tw + 13, 23);
            g2.dispose();
        }
    }

    private class Chip extends JButton {
        boolean chon;
        Color mau;
        Chip(String s, boolean chon, Color mau) {
            super(s);
            this.chon = chon;
            this.mau = mau;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font(FONT, Font.BOLD, 12));
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 34, 38));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover();
            g2.setColor(chon ? new Color(mau.getRed(), mau.getGreen(), mau.getBlue(), 44) : KHOI);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(chon || over ? mau : VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setFont(getFont());
            g2.setColor(chon ? mau : CHU_MO);
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
            setPreferredSize(new Dimension(getFontMetrics(getFont()).stringWidth(s) + 46, 40));
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
                g2.setColor(over ? new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 26) : KHOI);
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
                g3.drawString("Tìm bàn hoặc khu vực…", 44, getHeight() / 2 + 5);
                g3.dispose();
            }
        }
    }

    private class Cuon extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() { thumbColor = NGOC; }
        @Override protected JButton createDecreaseButton(int o) { return khong(); }
        @Override protected JButton createIncreaseButton(int o) { return khong(); }
        private JButton khong() {
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
            g2.setColor(new Color(NGOC.getRed(), NGOC.getGreen(), NGOC.getBlue(), 90));
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }

    static class Ban {
        int id, maBan, soCho, phut, khach;
        double tien;
        String maSo, ten, khu, trangThai, ghiChu;
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