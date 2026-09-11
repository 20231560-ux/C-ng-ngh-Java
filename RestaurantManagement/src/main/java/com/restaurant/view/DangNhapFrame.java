package com.restaurant.view;

import com.restaurant.dao.NguoiDungDAO;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class DangNhapFrame extends JFrame {

    static final String[] VAI_TRO = {"Quản trị", "Nhân viên", "Khách hàng"};

    private boolean dangKy = false;
    private int vaiTro = 0; // 0: Quản trị, 1: Nhân viên, 2: Khách hàng

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private NguoiDung nguoiDangNhap;

    private JPanel mainCard;
    private JTextField txtHoTen;
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JPasswordField txtNhapLai;
    private JButton tabDangNhap;
    private JButton tabDangKy;
    private JButton[] btnVaiTro = new JButton[3];
    private SavoreTheme.Button btnSubmit;
    private JLabel lblLoi;

    public DangNhapFrame() {
        setTitle("SAVORÉ RESTAURANT — CỔNG ĐĂNG NHẬP HỆ THỐNG");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        dungGiaoDien();
    }

    private void dungGiaoDien() {

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0A0F1D), w, h, new Color(0x050811));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                RadialGradientPaint rgp = new RadialGradientPaint(
                        new Point(w / 2, h / 2), Math.max(500, w * 0.45f),
                        new float[]{0f, 1f},
                        new Color[]{new Color(0xB0, 0x82, 0x46, 35), new Color(0x0A, 0x0F, 0x1D, 0)}
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, w, h);

                g2.setFont(SavoreTheme.font(11, Font.PLAIN));
                g2.setColor(new Color(0x64748B));
                String copy = "SAVORÉ RESTAURANT MANAGEMENT SYSTEM • BẢN QUYỀN 2026";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(copy, (w - fm.stringWidth(copy)) / 2, h - 25);

                g2.dispose();
            }
        };
        setContentPane(root);

        mainCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setColor(new Color(0, 0, 0, 90));
                g2.fillRoundRect(2, 6, w - 4, h - 8, 24, 24);

                g2.setColor(new Color(0x111927));
                g2.fillRoundRect(0, 0, w - 1, h - 1, 24, 24);

                g2.setColor(new Color(0x2E3E53));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 24, 24);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainCard.setOpaque(false);
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBorder(new EmptyBorder(36, 42, 36, 42));
        mainCard.setPreferredSize(new Dimension(460, 620));

        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pnlLogo.setOpaque(false);
        JLabel lblIcon = new JLabel("👑");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        pnlLogo.add(lblIcon);
        mainCard.add(pnlLogo);

        JLabel lblBrand = new JLabel("SAVORÉ", SwingConstants.CENTER);
        lblBrand.setFont(SavoreTheme.font(24, Font.BOLD));
        lblBrand.setForeground(SavoreTheme.GOLD_PRIMARY);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(lblBrand);

        JLabel lblSub = new JLabel("HỆ THỐNG QUẢN LÝ VẬN HÀNH NHÀ HÀNG", SwingConstants.CENTER);
        lblSub.setFont(SavoreTheme.font(10, Font.BOLD));
        lblSub.setForeground(new Color(0x94A3B8));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(lblSub);

        mainCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel pnlTabs = new JPanel(new GridLayout(1, 2, 8, 0));
        pnlTabs.setOpaque(false);
        pnlTabs.setMaximumSize(new Dimension(9999, 38));

        tabDangNhap = taoTabBtn("ĐĂNG NHẬP", true);
        tabDangKy = taoTabBtn("ĐĂNG KÝ", false);

        tabDangNhap.addActionListener(e -> doiCheDo(false));
        tabDangKy.addActionListener(e -> doiCheDo(true));

        pnlTabs.add(tabDangNhap);
        pnlTabs.add(tabDangKy);
        mainCard.add(pnlTabs);

        mainCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel lblRoleTitle = new JLabel("VAI TRÒ TRUY CẬP:");
        lblRoleTitle.setFont(SavoreTheme.font(10, Font.BOLD));
        lblRoleTitle.setForeground(new Color(0x64748B));
        lblRoleTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainCard.add(lblRoleTitle);
        mainCard.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel pnlRoles = new JPanel(new GridLayout(1, 3, 8, 0));
        pnlRoles.setOpaque(false);
        pnlRoles.setMaximumSize(new Dimension(9999, 34));

        for (int i = 0; i < 3; i++) {
            final int k = i;
            btnVaiTro[i] = taoRoleBtn(VAI_TRO[i], i == 0);
            btnVaiTro[i].addActionListener(e -> chonVaiTro(k));
            pnlRoles.add(btnVaiTro[i]);
        }
        mainCard.add(pnlRoles);

        mainCard.add(Box.createRigidArea(new Dimension(0, 16)));

        txtHoTen = taoInput("Nhập họ và tên...", false);
        txtTaiKhoan = taoInput("Tên đăng nhập / Số điện thoại...", false);
        txtMatKhau = (JPasswordField) taoInput("Nhập mật khẩu...", true);
        txtNhapLai = (JPasswordField) taoInput("Xác nhận lại mật khẩu...", true);

        mainCard.add(taoFormRow("HỌ VÀ TÊN", txtHoTen));
        mainCard.add(taoFormRow("TÀI KHOẢN ĐĂNG NHẬP", txtTaiKhoan));
        mainCard.add(taoFormRow("MẬT KHẨU", txtMatKhau));
        mainCard.add(taoFormRow("NHẬP LẠI MẬT KHẨU", txtNhapLai));

        lblLoi = new JLabel(" ");
        lblLoi.setFont(SavoreTheme.font(11, Font.PLAIN));
        lblLoi.setForeground(SavoreTheme.STATUS_DANGER);
        lblLoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainCard.add(lblLoi);

        mainCard.add(Box.createRigidArea(new Dimension(0, 10)));

        btnSubmit = new SavoreTheme.Button("ĐĂNG NHẬP VÀO HỆ THỐNG", SavoreTheme.Button.STYLE_PRIMARY);
        btnSubmit.setFont(SavoreTheme.font(14, Font.BOLD));
        btnSubmit.setMaximumSize(new Dimension(9999, 46));
        btnSubmit.setPreferredSize(new Dimension(380, 46));
        btnSubmit.addActionListener(e -> xuLy());
        mainCard.add(btnSubmit);

        txtTaiKhoan.addActionListener(e -> txtMatKhau.requestFocusInWindow());
        txtMatKhau.addActionListener(e -> { if (dangKy) txtNhapLai.requestFocusInWindow(); else xuLy(); });
        txtNhapLai.addActionListener(e -> xuLy());

        root.add(mainCard);
        capNhatCheDo();
    }

    private JPanel taoFormRow(String title, JComponent input) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setMaximumSize(new Dimension(9999, 62));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(title);
        lbl.setFont(SavoreTheme.font(10, Font.BOLD));
        lbl.setForeground(new Color(0x94A3B8));
        p.add(lbl);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(input);
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        return p;
    }

    private JTextField taoInput(String placeholder, boolean isPassword) {
        JTextField f = isPassword ? new JPasswordField() : new JTextField();
        f.setFont(SavoreTheme.font(13, Font.PLAIN));
        f.setForeground(Color.WHITE);
        f.setCaretColor(SavoreTheme.GOLD_PRIMARY);
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(0, 14, 0, 14));
        f.setPreferredSize(new Dimension(360, 38));
        f.setMaximumSize(new Dimension(9999, 38));

        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(0x1A2536));
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.setColor(f.isFocusOwner() ? SavoreTheme.GOLD_PRIMARY : new Color(0x2E3E53));
                g2.setStroke(new BasicStroke(f.isFocusOwner() ? 1.5f : 1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.add(f, BorderLayout.CENTER);

        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { wrapper.repaint(); }
            @Override public void focusLost(FocusEvent e) { wrapper.repaint(); }
        });

        return f;
    }

    private JButton taoTabBtn(String text, boolean active) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                boolean cur = (text.equals("ĐĂNG NHẬP") && !dangKy) || (text.equals("ĐĂNG KÝ") && dangKy);
                if (cur) {
                    g2.setColor(SavoreTheme.GOLD_PRIMARY);
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(new Color(0x182232));
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(new Color(0x94A3B8));
                }
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(SavoreTheme.font(12, Font.BOLD));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton taoRoleBtn(String text, boolean active) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int idx = text.equals("Quản trị") ? 0 : (text.equals("Nhân viên") ? 1 : 2);
                boolean isSel = (vaiTro == idx);
                if (isSel) {
                    g2.setColor(new Color(0x26374D));
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(SavoreTheme.GOLD_PRIMARY);
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(new Color(0x151E2B));
                    g2.fillRoundRect(0, 0, w, h, 8, 8);
                    g2.setColor(new Color(0x26374D));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
                    g2.setColor(new Color(0x94A3B8));
                }
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        b.setFont(SavoreTheme.font(11, Font.BOLD));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void doiCheDo(boolean sangDangKy) {
        dangKy = sangDangKy;
        if (dangKy) chonVaiTro(2);
        lblLoi.setText(" ");
        capNhatCheDo();
    }

    private void chonVaiTro(int k) {
        if (dangKy && k != 2) {
            baoLoi("Chỉ khách hàng mới được tự đăng ký tài khoản trực tuyến.");
            return;
        }
        vaiTro = k;
        mainCard.repaint();
    }

    private void capNhatCheDo() {
        txtHoTen.getParent().setVisible(dangKy);
        txtNhapLai.getParent().setVisible(dangKy);
        btnSubmit.setText(dangKy ? "HOÀN TẤT ĐĂNG KÝ" : "ĐĂNG NHẬP VÀO HỆ THỐNG");
        mainCard.setPreferredSize(new Dimension(460, dangKy ? 660 : 560));
        mainCard.revalidate();
        mainCard.repaint();
    }

    private void baoLoi(String s) {
        lblLoi.setForeground(SavoreTheme.STATUS_DANGER);
        lblLoi.setText("<html>" + s + "</html>");
    }

    private void baoOk(String s) {
        lblLoi.setForeground(SavoreTheme.STATUS_SUCCESS);
        lblLoi.setText("<html>" + s + "</html>");
    }

    private void xuLy() {
        if (dangKy) thucHienDangKy(); else thucHienDangNhap();
    }

    private void thucHienDangNhap() {
        String tk = txtTaiKhoan.getText().trim();
        String mk = new String(txtMatKhau.getPassword());
        if (tk.isEmpty() || mk.isEmpty()) {
            baoLoi("Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            return;
        }
        if (vaiTro == 2 && !hopLeSdt(tk)) {
            baoLoi("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0).");
            return;
        }

        String loi = thuDangNhap(VAI_TRO[vaiTro], tk, mk);
        if (loi != null) {
            baoLoi(loi);
            txtMatKhau.setText("");
            txtMatKhau.requestFocusInWindow();
            return;
        }

        lblLoi.setText(" ");
        moManHinhChinh(VAI_TRO[vaiTro], tk);
    }

    private void thucHienDangKy() {
        String ten = txtHoTen.getText().trim();
        String sdt = txtTaiKhoan.getText().trim();
        String mk = new String(txtMatKhau.getPassword());
        String ml = new String(txtNhapLai.getPassword());

        if (ten.isEmpty()) { baoLoi("Vui lòng nhập họ và tên."); txtHoTen.requestFocusInWindow(); return; }
        if (sdt.isEmpty()) { baoLoi("Số điện thoại là bắt buộc."); txtTaiKhoan.requestFocusInWindow(); return; }
        if (!hopLeSdt(sdt)) { baoLoi("Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0)."); txtTaiKhoan.requestFocusInWindow(); return; }
        if (mk.isEmpty()) { baoLoi("Mật khẩu là bắt buộc."); txtMatKhau.requestFocusInWindow(); return; }
        if (mk.length() < 6) { baoLoi("Mật khẩu phải có ít nhất 6 ký tự."); txtMatKhau.requestFocusInWindow(); return; }
        if (!mk.equals(ml)) { baoLoi("Mật khẩu xác nhận không khớp."); txtNhapLai.requestFocusInWindow(); return; }
        if (nguoiDungDAO.tonTaiTenDangNhap(sdt)) { baoLoi("Số điện thoại này đã được đăng ký trước đó."); return; }

        if (nguoiDungDAO.dangKyKhachHang(ten, sdt, mk) != null) {
            txtHoTen.setText("");
            txtMatKhau.setText("");
            txtNhapLai.setText("");
            doiCheDo(false);
            txtTaiKhoan.setText(sdt);
            baoOk("Đăng ký thành công! Hãy đăng nhập bằng số điện thoại vừa tạo.");
        } else {
            baoLoi("Đăng ký thất bại, vui lòng kiểm tra lại CSDL.");
        }
    }

    private boolean hopLeSdt(String s) {
        return s.matches("0\\d{9}");
    }

    private String maVaiTro(String vaiTroChon) {
        if ("Quản trị".equals(vaiTroChon)) return NguoiDungDAO.ADMIN;
        if ("Nhân viên".equals(vaiTroChon)) return "NHOM_NHAN_VIEN";
        return NguoiDungDAO.KHACH_HANG;
    }

    private boolean laNhanVien(String vaiTro) {
        String v = NguoiDungDAO.chuanHoaVaiTro(vaiTro);
        return NguoiDungDAO.QUAN_LY.equals(v)
                || NguoiDungDAO.NHAN_VIEN.equals(v)
                || NguoiDungDAO.PHUC_VU.equals(v)
                || NguoiDungDAO.THU_NGAN.equals(v)
                || NguoiDungDAO.BEP.equals(v);
    }

    private String thuDangNhap(String vaiTroChon, String taiKhoan, String matKhau) {
        nguoiDangNhap = null;
        NguoiDung nd = nguoiDungDAO.timTheoTenDangNhap(taiKhoan);

        if (nd == null) {
            if (nguoiDungDAO.loiKetNoi != null)
                return "Lỗi CSDL: " + nguoiDungDAO.loiKetNoi;
            return vaiTro == 2
                    ? "Số điện thoại này chưa được đăng ký."
                    : "Tài khoản \"" + taiKhoan + "\" không tồn tại trong hệ thống.";
        }

        if (!matKhau.equals(nd.getMatKhau()))
            return "Sai mật khẩu! Vui lòng thử lại.";

        if (!nd.isTrangThai())
            return "Tài khoản hiện đang bị khóa.";

        String cua = NguoiDungDAO.chuanHoaVaiTro(nd.getVaiTro());

        if ("Quản trị".equals(vaiTroChon)) {
            if (!NguoiDungDAO.ADMIN.equals(cua))
                return "Tài khoản này thuộc nhóm \"" + nd.getVaiTro() + "\", vui lòng chọn đúng nhóm vai trò.";
        } else if ("Nhân viên".equals(vaiTroChon)) {
            if (!laNhanVien(cua))
                return "Tài khoản này thuộc nhóm \"" + nd.getVaiTro() + "\", vui lòng chọn đúng nhóm vai trò.";
        } else {
            if (!NguoiDungDAO.KHACH_HANG.equals(cua))
                return "Tài khoản này thuộc nhóm \"" + nd.getVaiTro() + "\", vui lòng chọn đúng nhóm vai trò.";
        }

        nguoiDangNhap = nd;
        return null;
    }

    private void moManHinhChinh(String vaiTroChon, String taiKhoan) {
        String nhom = nguoiDangNhap == null
                ? NguoiDungDAO.ADMIN
                : NguoiDungDAO.chuanHoaVaiTro(nguoiDangNhap.getVaiTro());

        dispose();

        try {
            if (laNhanVien(nhom)) {
                EmployeeFrame frame = new EmployeeFrame(nguoiDangNhap);
                frame.setVisible(true);
            } else if (NguoiDungDAO.KHACH_HANG.equals(nhom)) {
                JOptionPane.showMessageDialog(null,
                        "Xin chào "
                                + (nguoiDangNhap == null ? taiKhoan : nguoiDangNhap.getHoTen())
                                + "!\n\nTài khoản khách hàng chưa có khu vực riêng.\n"
                                + "Vui lòng liên hệ nhân viên để được phục vụ.",
                        "SAVORÉ RESTAURANT", JOptionPane.INFORMATION_MESSAGE);
                new DangNhapFrame().setVisible(true);
            } else {
                QuanLyNhaHangFrame frame = new QuanLyNhaHangFrame(nguoiDangNhap);
                frame.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Không mở được màn hình làm việc.\n" + ex.getMessage(),
                    "SAVORÉ RESTAURANT", JOptionPane.ERROR_MESSAGE);
            new DangNhapFrame().setVisible(true);
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new DangNhapFrame().setVisible(true));
    }
}