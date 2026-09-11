package com.restaurant.view;

import com.restaurant.model.BanAn;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PosMainFrame extends JFrame {

    private final NguoiDung nguoiDung;
    private final boolean isAdmin;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pnlContent = new JPanel(cardLayout);
    private final Map<String, GiaoPos.NutPos> navButtons = new HashMap<>();

    // Subpanels
    private PosBanHangPanel pnlBanHang;
    private PosQuanLyBanPanel pnlQuanLyBan;
    private PosMonAnPanel pnlMonAn;
    private PosKhachHangPanel pnlKhachHang;
    private PosDoanhThuPanel pnlDoanhThu;
    private PosNhanVienPanel pnlNhanVien;

    private JLabel lblDongHo;
    private String currentCard = "POS";

    public PosMainFrame() {
        this(null);
    }

    public PosMainFrame(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.isAdmin = kiemTraAdmin(nguoiDung);

        setTitle("NOVA RESTAURANT — HỆ THỐNG QUẢN LÝ VẬN HÀNH & POS CẢM ỨNG");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 750));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        dungKhungPos();
        khoiTaoPhimTat();
        khoiDongDongHo();
        moManHinh("POS");
    }

    private boolean kiemTraAdmin(NguoiDung nd) {
        if (nd == null) return true; // Default admin nếu mở trực tiếp
        String role = nd.getVaiTro();
        if (role == null) return false;
        String r = role.toUpperCase();
        return r.contains("ADMIN") || r.contains("QUAN_TRI") || r.contains("QUAN_LY");
    }

    private void dungKhungPos() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(GiaoPos.NEN_TRANG_NGA);
        setContentPane(root);

        // TOP HEADER: Thanh điều hướng cảm ứng toàn màn hình (Không dùng menu trái)
        root.add(taoTopHeader(), BorderLayout.NORTH);

        // CENTER: Vùng nội dung màn hình chính
        pnlContent.setOpaque(false);

        // Khởi tạo các subpanels
        pnlBanHang = new PosBanHangPanel(nguoiDung);
        pnlQuanLyBan = new PosQuanLyBanPanel(nguoiDung, ban -> {
            // Khi chọn bàn từ Sơ đồ bàn -> chuyển ngay sang màn hình POS với bàn này
            moManHinh("POS");
            pnlBanHang.napDuLieu();
        });
        pnlMonAn = new PosMonAnPanel(nguoiDung);
        pnlKhachHang = new PosKhachHangPanel(nguoiDung);
        pnlDoanhThu = new PosDoanhThuPanel(nguoiDung);

        pnlContent.add(pnlBanHang, "POS");
        pnlContent.add(pnlQuanLyBan, "BAN");
        pnlContent.add(pnlMonAn, "MON_AN");
        pnlContent.add(pnlKhachHang, "KHACH_HANG");
        pnlContent.add(pnlDoanhThu, "DOANH_THU");

        if (isAdmin) {
            pnlNhanVien = new PosNhanVienPanel(nguoiDung);
            pnlContent.add(pnlNhanVien, "NHAN_VIEN");
        }

        root.add(pnlContent, BorderLayout.CENTER);
    }

    private JPanel taoTopHeader() {
        GiaoPos.ThePos header = new GiaoPos.ThePos(new BorderLayout(16, 0), 0);
        header.setPreferredSize(new Dimension(100, 72));
        header.setTheBackground(Color.WHITE);
        header.setBorderColor(GiaoPos.THE_VIEN);
        header.setBorder(new EmptyBorder(10, 18, 10, 18));

        // BÊN TRÁI: Logo thương hiệu sang trọng
        JPanel pnlBrand = new JPanel(new BorderLayout(10, 0));
        pnlBrand.setOpaque(false);

        JLabel lblLogoIcon = new JLabel("👑");
        lblLogoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        pnlBrand.add(lblLogoIcon, BorderLayout.WEST);

        JPanel pnlBrandText = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlBrandText.setOpaque(false);

        JLabel lblBrandName = new JLabel("NOVA RESTAURANT");
        lblBrandName.setFont(GiaoPos.f(18, Font.BOLD));
        lblBrandName.setForeground(GiaoPos.DONG_DAM);

        JLabel lblSlogan = new JLabel("HỆ THỐNG POS MÁY TÍNH TIỀN");
        lblSlogan.setFont(GiaoPos.f(10, Font.BOLD));
        lblSlogan.setForeground(GiaoPos.DONG_CHINH);

        pnlBrandText.add(lblBrandName);
        pnlBrandText.add(lblSlogan);
        pnlBrand.add(pnlBrandText, BorderLayout.CENTER);

        header.add(pnlBrand, BorderLayout.WEST);

        // Ở GIỮA: Thanh Tabs chức năng lớn cảm ứng (F1, F2, F3...)
        JPanel pnlNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pnlNav.setOpaque(false);

        themNutNav(pnlNav, "POS", "🍽️ Bán hàng (F1)", 150);
        themNutNav(pnlNav, "BAN", "🪑 Sơ đồ bàn (F2)", 140);
        themNutNav(pnlNav, "MON_AN", "📋 Thực đơn (F3)", 135);
        themNutNav(pnlNav, "KHACH_HANG", "👥 Khách hàng (F4)", 145);
        themNutNav(pnlNav, "DOANH_THU", "📊 Doanh thu (F5)", 145);

        if (isAdmin) {
            themNutNav(pnlNav, "NHAN_VIEN", "🛡️ Nhân sự (F6)", 140);
        }

        header.add(pnlNav, BorderLayout.CENTER);

        // BÊN PHẢI: Đồng hồ thực tế + Thông tin nhân viên + Đăng xuất
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlRight.setOpaque(false);

        // Đồng hồ số
        lblDongHo = new JLabel("00:00:00");
        lblDongHo.setFont(GiaoPos.f(13, Font.BOLD));
        lblDongHo.setForeground(GiaoPos.CHU_PHU);
        pnlRight.add(lblDongHo);

        // Huy hiệu nhân viên
        String tenNV = nguoiDung != null ? nguoiDung.getHoTen() : "Quản trị viên";
        String vaiTro = isAdmin ? "ADMIN" : "NHÂN VIÊN";

        JPanel pnlUserBadge = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlUserBadge.setOpaque(false);

        JLabel lblName = new JLabel(tenNV, SwingConstants.RIGHT);
        lblName.setFont(GiaoPos.f(13, Font.BOLD));
        lblName.setForeground(GiaoPos.CHU_CHINH);

        JLabel lblRole = new JLabel("● " + vaiTro, SwingConstants.RIGHT);
        lblRole.setFont(GiaoPos.f(10, Font.BOLD));
        lblRole.setForeground(isAdmin ? GiaoPos.DONG_CHINH : GiaoPos.BAN_TRONG);

        pnlUserBadge.add(lblName);
        pnlUserBadge.add(lblRole);
        pnlRight.add(pnlUserBadge);

        // Nút Đăng xuất
        GiaoPos.NutPos btnLogout = new GiaoPos.NutPos("Đăng xuất", GiaoPos.NutPos.STYLE_TRANG);
        btnLogout.setFont(GiaoPos.f(12, Font.BOLD));
        btnLogout.setPreferredSize(new Dimension(100, 38));
        btnLogout.addActionListener(e -> xuLyDangXuat());
        pnlRight.add(btnLogout);

        header.add(pnlRight, BorderLayout.EAST);

        return header;
    }

    private void themNutNav(JPanel container, String cardName, String title, int width) {
        GiaoPos.NutPos btn = new GiaoPos.NutPos(title, GiaoPos.NutPos.STYLE_TAB);
        btn.setFont(GiaoPos.f(13, Font.BOLD));
        btn.setPreferredSize(new Dimension(width, 46));
        btn.addActionListener(e -> moManHinh(cardName));
        navButtons.put(cardName, btn);
        container.add(btn);
    }

    public void moManHinh(String cardName) {
        this.currentCard = cardName;
        cardLayout.show(pnlContent, cardName);

        for (Map.Entry<String, GiaoPos.NutPos> entry : navButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(cardName));
        }

        // Tự động làm mới dữ liệu khi chuyển tab
        if ("POS".equals(cardName) && pnlBanHang != null) pnlBanHang.napDuLieu();
        else if ("BAN".equals(cardName) && pnlQuanLyBan != null) pnlQuanLyBan.napDuLieu();
        else if ("MON_AN".equals(cardName) && pnlMonAn != null) pnlMonAn.napDuLieu();
        else if ("KHACH_HANG".equals(cardName) && pnlKhachHang != null) pnlKhachHang.napDuLieu();
        else if ("DOANH_THU".equals(cardName) && pnlDoanhThu != null) pnlDoanhThu.napDuLieu();
        else if ("NHAN_VIEN".equals(cardName) && pnlNhanVien != null) pnlNhanVien.napDuLieu();
    }

    private void khoiDongDongHo() {
        Timer t = new Timer(1000, e -> {
            lblDongHo.setText(new SimpleDateFormat("HH:mm:ss  •  dd/MM/yyyy").format(new Date()));
        });
        t.start();
    }

    private void khoiTaoPhimTat() {
        JRootPane root = getRootPane();

        ganPhimTat(root, KeyEvent.VK_F1, () -> moManHinh("POS"));
        ganPhimTat(root, KeyEvent.VK_F2, () -> moManHinh("BAN"));
        ganPhimTat(root, KeyEvent.VK_F3, () -> moManHinh("MON_AN"));
        ganPhimTat(root, KeyEvent.VK_F4, () -> moManHinh("KHACH_HANG"));
        ganPhimTat(root, KeyEvent.VK_F5, () -> moManHinh("DOANH_THU"));
        if (isAdmin) {
            ganPhimTat(root, KeyEvent.VK_F6, () -> moManHinh("NHAN_VIEN"));
        }
    }

    private void ganPhimTat(JRootPane root, int keyCode, Runnable action) {
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyCode, 0), "ACTION_" + keyCode);
        root.getActionMap().put("ACTION_" + keyCode, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }

    private void xuLyDangXuat() {
        int r = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất khỏi hệ thống POS?",
                "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                DangNhapFrame login = new DangNhapFrame();
                login.setVisible(true);
            });
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> {
            PosMainFrame frame = new PosMainFrame(null);
            frame.setVisible(true);
        });
    }
}
