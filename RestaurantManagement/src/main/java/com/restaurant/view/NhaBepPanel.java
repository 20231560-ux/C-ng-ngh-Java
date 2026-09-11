package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class NhaBepPanel extends JPanel {

    private static NhaBepPanel instance;

    static final Color NEN     = GiaoPos.NEN_TRANG_NGA;
    static final Color KHOI    = GiaoPos.THE_TRANG;
    static final Color KHOI_2  = new Color(0xFAF6F0);
    static final Color VIEN    = GiaoPos.THE_VIEN;
    static final Color NGOC    = GiaoPos.DONG_CHINH;
    static final Color NGOC_MO = GiaoPos.DONG_DAM;
    static final Color CHU     = GiaoPos.CHU_CHINH;
    static final Color CHU_MO  = GiaoPos.CHU_PHU;
    static final Color LUC     = GiaoPos.BAN_TRONG;
    static final Color CAM     = GiaoPos.BAN_PHUC_VU;
    static final Color DO      = GiaoPos.BAN_CAN_THANH_TOAN;
    static final Color LAM     = GiaoPos.BAN_DAT_TRUOC;
    static final String FONT   = GiaoPos.FONT_NAME;

    public static final int CHO = 0, DANG_NAU = 1, XONG = 2;

    private final List<Phieu> danhSach = new ArrayList<>();
    private final JPanel[] cot = new JPanel[3];
    private final JLabel[] demCot = new JLabel[3];
    private final JPanel oThongKe = new JPanel(new GridLayout(1, 4, 16, 0));
    private int daPhucVu = 38;
    private int soHieu = 1;

    public NhaBepPanel() {
        instance = this;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(16, 20, 20, 20));
        napDuLieu();

        oThongKe.setOpaque(false);
        oThongKe.setPreferredSize(new Dimension(100, 96));
        add(oThongKe, BorderLayout.NORTH);

        JPanel giua = new JPanel(new GridLayout(1, 3, 16, 0));
        giua.setOpaque(false);
        giua.add(taoCot(CHO, "CHỜ CHẾ BIẾN", "Phiếu vừa nhận từ quầy POS", DO));
        giua.add(taoCot(DANG_NAU, "ĐANG NẤU TRÊN BẾP", "Đầu bếp đang thực hiện", CAM));
        giua.add(taoCot(XONG, "SẴN SÀNG PHỤC VỤ", "Món đã xong, chờ ra bàn", LUC));
        add(giua, BorderLayout.CENTER);

        veLai();
        new Timer(1000, e -> {
            for (Phieu p : danhSach) p.giay++;
            veLai();
        }).start();
    }

    public static void themPhieuTuPos(String ban, List<String> monList) {
        if (instance != null) {
            Phieu p = new Phieu();
            p.ma = "PB" + String.format("%03d", instance.soHieu++);
            p.ban = ban;
            p.mon.addAll(monList);
            p.trangThai = CHO;
            p.giay = 0;
            p.uuTien = true;
            instance.danhSach.add(0, p);
            instance.veLai();
        }
    }

    private void napDuLieu() {
        String[][] mau = {
                {"Bàn 02", "Lẩu Thái hải sản x1|Bò nướng tảng đá x2|Trà đào cam sả x4"},
                {"Bàn 05", "Cơm chiên hoàng bào x2|Canh chua cá bớp x1"},
                {"Bàn 09", "Thịt thăn bò Úc x2|Nem chua rán x2|Bia Tiger x6"},
                {"Bàn 12", "Tôm sú rang me x1|Sườn xào chua ngọt x1"},
                {"Bàn 15", "Lẩu nấm chim kê x1|Hàu nướng phô mai x2"},
                {"Bàn 18", "Mì xào bò Wagyu x2|Sinh tố bơ x2"},
                {"VIP T2 - 01", "Cá hồi áp chảo sốt chanh leo x2|Vang đỏ Bordeaux x1"}
        };
        Random r = new Random(4);
        for (String[] m : mau) {
            Phieu p = new Phieu();
            p.ma = "PB" + String.format("%03d", soHieu++);
            p.ban = m[0];
            for (String s : m[1].split("\\|")) p.mon.add(s);
            p.trangThai = r.nextInt(3);
            p.giay = 40 + r.nextInt(900);
            p.uuTien = r.nextInt(5) == 0;
            danhSach.add(p);
        }
    }

    private JPanel taoCot(int loai, String ten, String phu, Color mau) {
        JPanel p = new JPanel(new BorderLayout(0, 12)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(0x40, 0x30, 0x20, 8));
                g2.fillRoundRect(2, 3, w - 4, h - 4, 16, 16);
                g2.setColor(KHOI);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
                g2.setColor(VIEN);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
                g2.setColor(mau);
                g2.fillRoundRect(16, 0, 70, 4, 4, 4);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel dau = new JPanel(new BorderLayout());
        dau.setOpaque(false);
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(nhan(ten, 14, Font.BOLD, mau));
        t.add(Box.createRigidArea(new Dimension(0, 3)));
        t.add(nhan(phu, 11, Font.PLAIN, CHU_MO));
        dau.add(t, BorderLayout.WEST);
        demCot[loai] = nhan("0", 22, Font.BOLD, CHU);
        dau.add(demCot[loai], BorderLayout.EAST);
        p.add(dau, BorderLayout.NORTH);

        cot[loai] = new JPanel();
        cot[loai].setOpaque(false);
        cot[loai].setLayout(new BoxLayout(cot[loai], BoxLayout.Y_AXIS));
        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(cot[loai], BorderLayout.NORTH);
        p.add(cuon(giu), BorderLayout.CENTER);

        if (loai == CHO) {
            Nut them = new Nut("+  Thêm phiếu mô phỏng", false, NGOC);
            them.addActionListener(e -> taoPhieu());
            p.add(them, BorderLayout.SOUTH);
        }
        return p;
    }

    private void taoPhieu() {
        Random r = new Random();
        String[] mon = {"Bò Fuji nướng đá", "Lẩu Thái TomYum", "Cá hồi áp chảo sốt chanh leo", "Cơm chiên hoàng bào",
                "Tôm sú nướng phô mai", "Salad cá hồi hun khói", "Trà đào cam sả", "Vang đỏ Bordeaux"};
        Phieu p = new Phieu();
        p.ma = "PB" + String.format("%03d", soHieu++);
        p.ban = "Bàn " + String.format("%02d", 1 + r.nextInt(24));
        int n = 1 + r.nextInt(3);
        for (int i = 0; i < n; i++) p.mon.add(mon[r.nextInt(mon.length)] + " x" + (1 + r.nextInt(2)));
        p.trangThai = CHO;
        p.giay = 0;
        p.uuTien = r.nextInt(4) == 0;
        danhSach.add(0, p);
        veLai();
    }

    private void veLai() {
        int[] dem = new int[3];
        int tongGiay = 0, sl = 0;
        for (Phieu p : danhSach) {
            dem[p.trangThai]++;
            tongGiay += p.giay;
            sl++;
        }
        int tb = sl == 0 ? 0 : tongGiay / sl / 60;

        oThongKe.removeAll();
        oThongKe.add(new TheSo("Phiếu chờ nấu", String.valueOf(dem[CHO]), "Cần bắt đầu ngay", DO));
        oThongKe.add(new TheSo("Đang nấu trên bếp", String.valueOf(dem[DANG_NAU]), "Đầu bếp đang thực hiện", CAM));
        oThongKe.add(new TheSo("Sẵn sàng ra bàn", String.valueOf(dem[XONG]), "Chờ chạy bàn mang ra", LUC));
        oThongKe.add(new TheSo("Đã phục vụ ca này", String.valueOf(daPhucVu),
                "Thời gian chuẩn bị TB " + tb + " phút", NGOC));
        oThongKe.revalidate();
        oThongKe.repaint();

        for (int i = 0; i < 3; i++) {
            cot[i].removeAll();
            demCot[i].setText(String.valueOf(dem[i]));
        }
        for (Phieu p : danhSach) cot[p.trangThai].add(new ThePhieu(p));
        for (int i = 0; i < 3; i++) {
            if (dem[i] == 0) {
                JLabel l = nhan("<html><div style='text-align:center;color:#9E948B;padding:40px 0;'>Không có phiếu nào ở cột này</div></html>",
                        12, Font.PLAIN, CHU_MO);
                l.setAlignmentX(Component.CENTER_ALIGNMENT);
                cot[i].add(l);
            }
            cot[i].revalidate();
            cot[i].repaint();
        }
    }

    private void chuyen(Phieu p, int buoc) {
        if (buoc > 0 && p.trangThai == XONG) {
            for (Iterator<Phieu> it = danhSach.iterator(); it.hasNext(); ) {
                if (it.next() == p) { it.remove(); break; }
            }
            daPhucVu++;
            veLai();
            return;
        }
        p.trangThai = Math.max(CHO, Math.min(XONG, p.trangThai + buoc));
        if (buoc > 0) p.giay = 0;
        veLai();
    }

    private class ThePhieu extends JPanel {
        ThePhieu(Phieu p) {
            setOpaque(false);
            setLayout(new BorderLayout(0, 10));
            setBorder(new EmptyBorder(12, 14, 12, 14));
            int cao = 120 + p.mon.size() * 22;
            setMaximumSize(new Dimension(9999, cao));
            setPreferredSize(new Dimension(280, cao));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            // Header phiếu
            JPanel dau = new JPanel(new BorderLayout());
            dau.setOpaque(false);
            JPanel t = new JPanel();
            t.setOpaque(false);
            t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
            t.add(nhan(p.ban, 14, Font.BOLD, CHU));
            t.add(Box.createRigidArea(new Dimension(0, 2)));
            t.add(nhan("Mã phiếu: " + p.ma + (p.uuTien ? " • ƯU TIÊN" : ""), 10, Font.PLAIN, p.uuTien ? DO : CHU_MO));
            dau.add(t, BorderLayout.WEST);

            JLabel lblTime = nhan("⏱️ " + phutGiay(p.giay), 12, Font.BOLD, mauCho(p.giay));
            dau.add(lblTime, BorderLayout.EAST);
            add(dau, BorderLayout.NORTH);

            // Danh sách món
            JPanel ds = new JPanel();
            ds.setOpaque(false);
            ds.setLayout(new BoxLayout(ds, BoxLayout.Y_AXIS));
            for (String m : p.mon) {
                JPanel d = new JPanel(new BorderLayout());
                d.setOpaque(false);
                d.setMaximumSize(new Dimension(9999, 20));
                d.add(nhan("•  " + m, 12, Font.PLAIN, CHU), BorderLayout.WEST);
                ds.add(d);
            }
            add(ds, BorderLayout.CENTER);

            // Nút bấm tiến độ
            JPanel nut = new JPanel(new GridLayout(1, 2, 8, 0));
            nut.setOpaque(false);
            nut.setPreferredSize(new Dimension(100, 34));
            Nut lui = new Nut("← Quay lại", false, CHU_MO);
            String nhanTien = p.trangThai == CHO ? "Bắt đầu nấu"
                    : (p.trangThai == DANG_NAU ? "Đã nấu xong" : "Đã ra bàn");
            Color mauTien = p.trangThai == CHO ? CAM : (p.trangThai == DANG_NAU ? LUC : NGOC);
            Nut tien = new Nut(nhanTien, true, mauTien);
            lui.addActionListener(e -> chuyen(p, -1));
            tien.addActionListener(e -> chuyen(p, 1));
            lui.setEnabled(p.trangThai != CHO);
            nut.add(lui);
            nut.add(tien);
            add(nut, BorderLayout.SOUTH);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0xFAF7F2));
            g2.fillRoundRect(0, 0, w - 1, h - 8, 12, 12);
            g2.setColor(VIEN);
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(0, 0, w - 1, h - 8, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class TheSo extends JPanel {
        TheSo(String nhan, String gt, String phu, Color m) {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(14, 18, 14, 18));
            JPanel in = new JPanel();
            in.setOpaque(false);
            in.setLayout(new BoxLayout(in, BoxLayout.Y_AXIS));
            in.add(NhaBepPanel.nhan(nhan.toUpperCase(), 10, Font.BOLD, CHU_MO));
            in.add(Box.createRigidArea(new Dimension(0, 6)));
            in.add(NhaBepPanel.nhan(gt, 24, Font.BOLD, m));
            in.add(Box.createRigidArea(new Dimension(0, 4)));
            in.add(NhaBepPanel.nhan(phu, 11, Font.PLAIN, CHU_MO));
            add(in, BorderLayout.CENTER);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(0x40, 0x30, 0x20, 8));
            g2.fillRoundRect(2, 3, w - 4, h - 4, 14, 14);
            g2.setColor(KHOI);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);
            g2.setColor(VIEN);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
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
            setFont(new Font(FONT, Font.BOLD, 12));
            setPreferredSize(new Dimension(100, 36));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean over = getModel().isRollover() && isEnabled();
            if (chinh) {
                g2.setColor(over ? mau.darker() : mau);
                g2.fillRoundRect(0, 0, w, h, 10, 10);
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(over ? new Color(0xF0EAE1) : Color.WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(over ? NGOC : VIEN);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(isEnabled() ? (over ? NGOC_MO : CHU) : CHU_MO);
            }
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, h / 2 + 5);
            g2.dispose();
        }
    }

    static Color mauCho(int giay) {
        int p = giay / 60;
        if (p >= 15) return DO;
        if (p >= 8) return CAM;
        return LUC;
    }

    static String phutGiay(int giay) {
        return (giay / 60) + ":" + String.format("%02d", giay % 60);
    }

    static JScrollPane cuon(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        return sp;
    }

    static JLabel nhan(String s, int co, int kieu, Color m) {
        JLabel l = new JLabel(s);
        l.setFont(new Font(FONT, kieu, co));
        l.setForeground(m);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static class Phieu {
        String ma, ban;
        List<String> mon = new ArrayList<>();
        int trangThai, giay;
        boolean uuTien;
    }
}