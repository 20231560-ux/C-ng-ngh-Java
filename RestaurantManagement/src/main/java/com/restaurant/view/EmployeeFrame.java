package com.restaurant.view;

import com.restaurant.model.NguoiDung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;

public class EmployeeFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final List<MucBen> cacMuc = new ArrayList<>();
    private final NguoiDung nguoiDung;
    private final EmployeePaymentPanel paymentPanel;

    private JLabel lbTieuDe;
    private JLabel lbPhuDe;
    private JLabel lbDongHo;

    private static final Object[][] MENU = {
            {"dashboard", "tongquan", "Tổng quan", "Bức tranh toàn cảnh ca làm việc"},
            {"tables", "ban", "Sơ đồ bàn", "Trạng thái từng bàn trong nhà hàng"},
            {"orders", "don", "Đơn hàng", "Theo dõi đơn theo tiến trình phục vụ"},
            {"kitchen", "bep", "Bếp", "Hàng chờ chế biến và món đã xong"},
            {"payment", "tien", "Thanh toán", "Các bàn đang chờ thanh toán"},
            {"customers", "khach", "Khách hàng", "Tra cứu khách và hạng thành viên"},
            {"account", "taikhoan", "Tài khoản", "Hồ sơ cá nhân và ca làm việc"}
    };

    public EmployeeFrame() {
        this(null);
    }

    public EmployeeFrame(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.paymentPanel = new EmployeePaymentPanel();
        setTitle("SAVORÉ — Không gian làm việc nhân viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1240, 760));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        dungGiaoDien();
        moTrang("dashboard");
    }

    private void dungGiaoDien() {
        JPanel goc = new JPanel(new BorderLayout());
        goc.setBackground(EmployeeKit.NEN);
        goc.add(taoThanhBen(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(taoHeader(), BorderLayout.NORTH);

        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 26, 22, 26));
        contentPanel.add(new EmployeeDashboardPanel(nguoiDung, this::moTrang), "dashboard");
        contentPanel.add(new EmployeeFloorMapPanel(this::moBanTuSoDo), "tables");
        contentPanel.add(new EmployeeOrderPanel(), "orders");
        contentPanel.add(new EmployeeKitchenPanel(), "kitchen");
        contentPanel.add(paymentPanel, "payment");
        contentPanel.add(new EmployeeCustomerPanel(), "customers");
        contentPanel.add(new EmployeeAccountPanel(), "account");
        main.add(contentPanel, BorderLayout.CENTER);

        goc.add(main, BorderLayout.CENTER);
        setContentPane(goc);
    }

    private void moBanTuSoDo(String maBan) {
        paymentPanel.hienThiBan(maBan);
        moTrang("payment");
    }

    private JPanel taoThanhBen() {
        JPanel s = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(EmployeeKit.NEN_SAU);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(EmployeeKit.LINE);
                g2.fillRect(getWidth() - 1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        s.setPreferredSize(new Dimension(238, 100));
        s.add(taoLogo(), BorderLayout.NORTH);

        JPanel ds = new JPanel();
        ds.setOpaque(false);
        ds.setLayout(new BoxLayout(ds, BoxLayout.Y_AXIS));
        ds.setBorder(new EmptyBorder(10, 14, 10, 14));

        cacMuc.clear();
        for (Object[] m : MENU) {
            MucBen mm = new MucBen((String) m[0], (String) m[1], (String) m[2]);
            cacMuc.add(mm);
            ds.add(mm);
            ds.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        JPanel giu = new JPanel(new BorderLayout());
        giu.setOpaque(false);
        giu.add(ds, BorderLayout.NORTH);
        s.add(EmployeeKit.cuon(giu), BorderLayout.CENTER);
        s.add(taoDangXuat(), BorderLayout.SOUTH);
        return s;
    }

    private JPanel taoLogo() {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth();
                g2.setFont(EmployeeKit.serif(26));
                g2.setColor(EmployeeKit.CHU);
                g2.drawString("SAVORÉ", 24, 54);
                g2.setFont(EmployeeKit.sans(9, Font.BOLD));
                g2.setColor(EmployeeKit.VANG);
                g2.drawString("N H Â N   V I Ê N", 26, 72);
                g2.setColor(EmployeeKit.LINE);
                g2.fillRect(24, 92, w - 48, 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(238, 106));
        return p;
    }

    private JPanel taoDangXuat() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 14, 16, 14));
        MucBen out = new MucBen("logout", "thoat", "Đăng xuất");
        out.laThoat = true;
        p.add(out, BorderLayout.CENTER);
        return p;
    }

    private class MucBen extends JComponent {
        final String ma;
        final String icon;
        final String ten;
        boolean chon;
        boolean hover;
        boolean laThoat;
        float s;

        MucBen(String ma, String icon, String ten) {
            this.ma = ma;
            this.icon = icon;
            this.ten = ten;
            setPreferredSize(new Dimension(210, 44));
            setMaximumSize(new Dimension(9999, 44));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            Timer t = new Timer(15, e -> {
                float d = chon ? 1f : (hover ? .5f : 0f);
                s += (d - s) * .3f;
                repaint();
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    t.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    t.start();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (laThoat) dangXuat();
                    else moTrang(ma);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            Color acc = laThoat ? EmployeeKit.DO : EmployeeKit.VANG;
            if (s > .02f) {
                g2.setColor(EmployeeKit.mo(acc, (int) (22 * s)));
                g2.fillRoundRect(0, 0, w, h, 4, 4);
            }
            if (chon) {
                g2.setColor(acc);
                g2.fillRect(0, 10, 2, h - 20);
            }
            Color c = chon ? EmployeeKit.VANG_S
                    : (hover ? (laThoat ? EmployeeKit.DO : EmployeeKit.CHU) : EmployeeKit.CHU_PHU);
            EmployeeKit.Ic.ve(g2, icon, 18, (h - 19) / 2, 19, c);
            g2.setFont(EmployeeKit.sans(13, chon ? Font.BOLD : Font.PLAIN));
            g2.setColor(c);
            g2.drawString(ten, 50, h / 2 + 5);
            g2.dispose();
        }
    }

    private JPanel taoHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(EmployeeKit.NEN);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(EmployeeKit.LINE);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(100, 68));
        h.setBorder(new EmptyBorder(0, 26, 0, 22));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new BoxLayout(trai, BoxLayout.Y_AXIS));
        trai.setBorder(new EmptyBorder(13, 0, 0, 0));
        lbTieuDe = new JLabel("Tổng quan");
        lbTieuDe.setFont(EmployeeKit.serif(22));
        lbTieuDe.setForeground(EmployeeKit.CHU);
        lbTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbPhuDe = EmployeeKit.chu("", 11, Font.PLAIN, EmployeeKit.CHU_MO);
        trai.add(lbTieuDe);
        trai.add(Box.createRigidArea(new Dimension(0, 2)));
        trai.add(lbPhuDe);
        h.add(trai, BorderLayout.WEST);

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 14));
        phai.setOpaque(false);
        lbDongHo = EmployeeKit.chu("", 12, Font.BOLD, EmployeeKit.CHU_PHU);
        new Timer(1000, e -> capNhatDongHo()).start();
        capNhatDongHo();
        phai.add(lbDongHo);
        phai.add(new ThongTinNhanVien());
        h.add(phai, BorderLayout.EAST);
        return h;
    }

    private void capNhatDongHo() {
        String[] th = {"CN", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
        Calendar c = Calendar.getInstance();
        lbDongHo.setText(th[c.get(Calendar.DAY_OF_WEEK) - 1] + "  ·  "
                + new SimpleDateFormat("dd/MM  ·  HH:mm:ss").format(new Date()));
    }

    private class ThongTinNhanVien extends JComponent {
        ThongTinNhanVien() {
            setPreferredSize(new Dimension(228, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            g2.setColor(EmployeeKit.LINE);
            g2.fillRect(0, 6, 1, 28);
            String ten = hoTen();
            String vai = vaiTro();
            String ca = caLam();
            g2.setFont(EmployeeKit.sans(13, Font.BOLD));
            g2.setColor(EmployeeKit.CHU);
            int tw = g2.getFontMetrics().stringWidth(ten);
            g2.drawString(ten, w - 52 - tw, 18);
            g2.setFont(EmployeeKit.sans(10, Font.PLAIN));
            g2.setColor(EmployeeKit.CHU_MO);
            String d = vai + "  ·  " + ca;
            g2.drawString(d, w - 52 - g2.getFontMetrics().stringWidth(d), 33);
            g2.setColor(EmployeeKit.mo(EmployeeKit.VANG, 40));
            g2.fillRoundRect(w - 40, 2, 38, 38, 4, 4);
            g2.setColor(EmployeeKit.mo(EmployeeKit.VANG, 110));
            g2.drawRoundRect(w - 40, 2, 38, 38, 4, 4);
            g2.setFont(EmployeeKit.serif(17));
            g2.setColor(EmployeeKit.VANG_S);
            String vt = viTat(ten);
            g2.drawString(vt, w - 21 - g2.getFontMetrics().stringWidth(vt) / 2, 27);
            g2.dispose();
        }
    }

    private String hoTen() {
        return nguoiDung == null || nguoiDung.getHoTen() == null ? "Lê Văn Phục Vụ" : nguoiDung.getHoTen();
    }

    private String vaiTro() {
        String v = nguoiDung == null || nguoiDung.getVaiTro() == null ? "PHUC_VU" : nguoiDung.getVaiTro();
        switch (v) {
            case "PHUC_VU": return "Phục vụ";
            case "THU_NGAN": return "Thu ngân";
            case "BEP": return "Bếp";
            case "QUAN_LY": return "Quản lý";
            default: return v;
        }
    }

    private String caLam() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h < 14) return "Ca sáng";
        if (h < 18) return "Ca chiều";
        return "Ca tối";
    }

    private String viTat(String s) {
        String[] p = s.trim().split("\\s+");
        if (p.length == 1) return p[0].substring(0, 1).toUpperCase();
        return ("" + p[p.length - 2].charAt(0) + p[p.length - 1].charAt(0)).toUpperCase();
    }

    public void moTrang(String ma) {
        cardLayout.show(contentPanel, ma);
        for (MucBen m : cacMuc) {
            m.chon = m.ma.equals(ma);
            m.repaint();
        }
        for (Object[] x : MENU) {
            if (x[0].equals(ma)) {
                lbTieuDe.setText((String) x[2]);
                lbPhuDe.setText((String) x[3]);
                break;
            }
        }
    }

    private void dangXuat() {
        int r = JOptionPane.showConfirmDialog(this,
                "Kết thúc ca làm việc và đăng xuất?", "SAVORÉ",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;
        dispose();
        new DangNhapFrame().setVisible(true);
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new EmployeeFrame().setVisible(true));
    }
}
