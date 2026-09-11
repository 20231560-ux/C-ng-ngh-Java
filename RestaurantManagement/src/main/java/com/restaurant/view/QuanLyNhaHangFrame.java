package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * SAVORÉ RESTAURANT OPERATIONS COMMAND CENTER — MASTER APPLICATION SHELL
 * Thiết kế giao diện Dark Luxury chuẩn quốc tế theo Design Reference:
 * Sidebar Icon Rail thanh thoát (72px) | Glassmorphism Top Header | Content Area co giãn hoàn hảo.
 */
public class QuanLyNhaHangFrame extends JFrame {

    private final NguoiDung nguoiDung;
    private final boolean isAdmin;

    // Layout Containers
    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    private String currentCard = "DASHBOARD";

    // Header Controls
    private JTextField txtTimKiem;
    private JLabel lblNgayGio;
    private JLabel lblDongHoTo;
    private JLabel lblDbStatus;

    // Navigation Map
    private final Map<String, NavItem> navItems = new LinkedHashMap<>();

    // Sub-panels
    private SavoreDashboardPanel pnlDashboard;
    private PosQuanLyBanPanel pnlDatBan;
    private PosQuanLyBanPanel pnlQuanLyBan;
    private DonHangPanel pnlDonHang;
    private PosBanHangPanel pnlBanHang;
    private NhaBepPanel pnlNhaBep;
    private PosMonAnPanel pnlMonAn;
    private ComboPanel pnlCombo;
    private PosKhachHangPanel pnlKhachHang;
    private KhuyenMaiPanel pnlKhuyenMai;
    private KhoNguyenLieuPanel pnlKho;
    private PosNhanVienPanel pnlNhanVien;
    private BaoCaoDoanhThuPanel pnlDoanhThu;
    private CaiDatPanel pnlCaiDat;

    public QuanLyNhaHangFrame() {
        this(null);
    }

    public QuanLyNhaHangFrame(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.isAdmin = (nguoiDung == null || "QUAN_TRI".equalsIgnoreCase(nguoiDung.getVaiTro())
                || "Quản trị".equalsIgnoreCase(nguoiDung.getVaiTro()));

        setTitle("SAVORÉ RESTAURANT — OPERATIONS COMMAND CENTER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        dungGiaoDien();
        khoiDongDongHo();
        khoiTaoPhimTat();
        kiemTraKetNoiMySQL();

        // Mở mặc định trang chủ Command Center
        moPhanHe("DASHBOARD");
    }

    private void dungGiaoDien() {
    	JPanel root = new JPanel(new BorderLayout());

    	root.setBackground(SavoreDesignSystem.Colors.DARK_BG);
        setContentPane(root);

        // 1. SIDEBAR TRÁI: ICON RAIL (WIDTH = 72px)
        pnlSidebar = taoSidebarIconRail();
        root.add(pnlSidebar, BorderLayout.WEST);

        // 2. KHU VỰC CHÍNH: TOP BAR GLASSMORPHISM + CONTENT
        JPanel pnlCenterAndHeader = new JPanel(new BorderLayout());
        pnlCenterAndHeader.setOpaque(false);

        pnlCenterAndHeader.add(taoTopHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setOpaque(false);
        khoiTaoCacPhanHe();

        pnlCenterAndHeader.add(pnlContent, BorderLayout.CENTER);
        root.add(pnlCenterAndHeader, BorderLayout.CENTER);
    }

    /* =========================================================================
     * 1. SIDEBAR TRÁI: ICON RAIL NARROW (72px)
     * ========================================================================= */
    private JPanel taoSidebarIconRail() {
        JPanel sidebar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0x09, 0x0E, 0x17));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Viền vàng đồng siêu mỏng ngăn cách với nội dung
                g2.setColor(new Color(0xC8, 0x92, 0x45, 45));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(72, 800));
        sidebar.setOpaque(false);

        // TOP LOGO
        JPanel pnlTopLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        pnlTopLogo.setOpaque(false);

        JLabel lblLogo = new JLabel("⚜️") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Quầng sáng vàng nhẹ sau logo
                RadialGradientPaint rgp = new RadialGradientPaint(
                        new Point(getWidth() / 2, getHeight() / 2), 22f,
                        new float[]{0f, 1f},
                        new Color[]{new Color(0xC8, 0x92, 0x45, 60), new Color(0, 0, 0, 0)}
                );
                g2.setPaint(rgp);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblLogo.setPreferredSize(new Dimension(44, 44));
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setToolTipText("Savoré Restaurant Operations Center");
        lblLogo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogo.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { moPhanHe("DASHBOARD"); }
        });
        pnlTopLogo.add(lblLogo);
        sidebar.add(pnlTopLogo, BorderLayout.NORTH);

        // CENTER MENU ICONS
        JPanel pnlMenu = new JPanel();
        pnlMenu.setOpaque(false);
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBorder(new EmptyBorder(6, 0, 6, 0));

        themIconMenuItem(pnlMenu, "DASHBOARD",  "🏠", "Trang chủ (Operations Hub)");
        themIconMenuItem(pnlMenu, "BAN_DAT",    "📅", "Đặt bàn (Reservations)");
        themIconMenuItem(pnlMenu, "BAN",        "🪑", "Sơ đồ mặt bằng (Tables)");
        themIconMenuItem(pnlMenu, "DON_HANG",   "📋", "Quản lý đơn hàng (Orders)");
        themIconMenuItem(pnlMenu, "POS",        "🛒", "Bán hàng (POS)");
        themIconMenuItem(pnlMenu, "BEP",        "🔥", "Nhà bếp KDS (Kitchen)");
        themIconMenuItem(pnlMenu, "MON_AN",     "🍽️", "Quản lý món ăn (Menu)");
        themIconMenuItem(pnlMenu, "COMBO",      "🍱", "Thực đơn & Combo");
        themIconMenuItem(pnlMenu, "KHACH_HANG", "👥", "Quản lý khách hàng (CRM)");
        themIconMenuItem(pnlMenu, "KHO",        "📦", "Kho nguyên liệu (Inventory)");
        if (isAdmin) {
            themIconMenuItem(pnlMenu, "NHAN_VIEN",  "👤", "Nhân sự & Ca trực (Staff)");
        }
        themIconMenuItem(pnlMenu, "DOANH_THU",  "📊", "Doanh thu & Báo cáo (Reports)");
        themIconMenuItem(pnlMenu, "KHUYEN_MAI", "🎟️", "Khuyến mãi & Voucher");
        themIconMenuItem(pnlMenu, "CAI_DAT",    "⚙️", "Thiết lập hệ thống (Settings)");

        JScrollPane scroll = new JScrollPane(pnlMenu);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Ẩn thanh cuộn dọc nhưng vẫn cuộn được
        sidebar.add(scroll, BorderLayout.CENTER);

        // BOTTOM FOOTER: AVATAR & LOGOUT
        JPanel pnlBottom = new JPanel();
        pnlBottom.setOpaque(false);
        pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));
        pnlBottom.setBorder(new EmptyBorder(8, 0, 14, 0));

        // Avatar tròn "AD"
        JLabel lblAvatar = new JLabel("AD", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1F, 0x2A, 0x3E));
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.setColor(SavoreDesignSystem.Colors.COPPER_GOLD);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(2, 2, getWidth() - 5, getHeight() - 5);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblAvatar.setPreferredSize(new Dimension(36, 36));
        lblAvatar.setMaximumSize(new Dimension(36, 36));
        lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        String uName = (nguoiDung != null) ? nguoiDung.getHoTen() : "Nguyễn Văn Admin";
        lblAvatar.setToolTipText(uName + " • " + (isAdmin ? "Quản trị viên" : "Nhân viên"));

        JButton btnLogout = new JButton("🚪");
        btnLogout.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        btnLogout.setForeground(new Color(0x94, 0xA3, 0xB8));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setToolTipText("Đăng xuất hệ thống");
        btnLogout.addActionListener(e -> dangXuat());

        pnlBottom.add(lblAvatar);
        pnlBottom.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlBottom.add(btnLogout);

        sidebar.add(pnlBottom, BorderLayout.SOUTH);
        return sidebar;
    }

    private void themIconMenuItem(JPanel container, String ma, String icon, String tooltip) {
        NavItem item = new NavItem(icon, tooltip);
        item.addActionListener(e -> moPhanHe(ma));
        navItems.put(ma, item);

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
        wrap.setOpaque(false);
        wrap.add(item);

        container.add(wrap);
    }

    /* =========================================================================
     * 2. TOP BAR: GLASSMORPHISM HEADER (56px)
     * ========================================================================= */
    private JPanel taoTopHeader() {
        JPanel header = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0x09, 0x0E, 0x18, 240));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Viền đáy kính
                g2.setColor(new Color(0xC8, 0x92, 0x45, 40));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(100, 54));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 20, 8, 20));

        // TRÁI: Ô TÌM KIẾM BO TRÒN (Ctrl + K)
        JPanel pnlSearch = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x13, 0x1A, 0x27));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(0xC8, 0x92, 0x45, 45));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnlSearch.setOpaque(false);
        pnlSearch.setPreferredSize(new Dimension(300, 36));
        pnlSearch.setBorder(new EmptyBorder(0, 12, 0, 8));

        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        lblSearchIcon.setForeground(new Color(0x94, 0xA3, 0xB8));
        pnlSearch.add(lblSearchIcon, BorderLayout.WEST);

        txtTimKiem = new JTextField();
        txtTimKiem.setText("Tìm kiếm nhanh (món, bàn, đơn)...");
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtTimKiem.setForeground(new Color(0x94, 0xA3, 0xB8));
        txtTimKiem.setOpaque(false);
        txtTimKiem.setBorder(null);
        txtTimKiem.setCaretColor(Color.WHITE);
        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtTimKiem.getText().startsWith("Tìm kiếm")) {
                    txtTimKiem.setText("");
                    txtTimKiem.setForeground(Color.WHITE);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtTimKiem.getText().trim().isEmpty()) {
                    txtTimKiem.setText("Tìm kiếm nhanh (món, bàn, đơn)...");
                    txtTimKiem.setForeground(new Color(0x94, 0xA3, 0xB8));
                }
            }
        });
        txtTimKiem.addActionListener(e -> moPhanHe("POS"));
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);

        JLabel lblKbd = new JLabel("Ctrl + K");
        lblKbd.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblKbd.setForeground(new Color(0x94, 0xA3, 0xB8));
        lblKbd.setOpaque(true);
        lblKbd.setBackground(new Color(0x1E, 0x29, 0x3B));
        lblKbd.setBorder(new EmptyBorder(2, 5, 2, 5));
        pnlSearch.add(lblKbd, BorderLayout.EAST);

        header.add(pnlSearch, BorderLayout.WEST);

        // PHẢI: TRẠNG THÁI MYSQL + CHUÔNG + TIN NHẮN + ĐỒNG HỒ
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlRight.setOpaque(false);

        // Trạng thái MySQL Live Pill
        lblDbStatus = new JLabel("● MySQL LIVE 1.8ms") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x10, 0xB9, 0x81, 28));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(0x10, 0xB9, 0x81, 90));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblDbStatus.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblDbStatus.setForeground(new Color(0x10, 0xB9, 0x81));
        lblDbStatus.setBorder(new EmptyBorder(4, 10, 4, 10));
        pnlRight.add(lblDbStatus);

        // Chuông thông báo
        JButton btnBell = new JButton("🔔") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xEF, 0x44, 0x44));
                g2.fillOval(getWidth() - 11, 2, 9, 9);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 7));
                g2.drawString("5", getWidth() - 8, 9);
                g2.dispose();
            }
        };
        btnBell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnBell.setForeground(new Color(0xC5, 0xD1, 0xDE));
        btnBell.setContentAreaFilled(false);
        btnBell.setBorderPainted(false);
        btnBell.setFocusPainted(false);
        btnBell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBell.setToolTipText("5 cảnh báo vận hành mới");
        pnlRight.add(btnBell);

        // Tin nhắn
        JButton btnMsg = taoHeaderIconBtn("💬", "Tin nhắn nội bộ bếp & phục vụ");
        pnlRight.add(btnMsg);

        // Toàn màn hình
        JButton btnFull = taoHeaderIconBtn("⛶", "Toàn màn hình");
        btnFull.addActionListener(e -> {
            if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        pnlRight.add(btnFull);

        // Phân cách
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 22));
        sep.setForeground(new Color(0x27, 0x33, 0x44));
        pnlRight.add(sep);

        // Đồng hồ số
        JPanel pnlClock = new JPanel(new GridLayout(2, 1, 0, 0));
        pnlClock.setOpaque(false);

        lblNgayGio = new JLabel("Thứ 2, 08/09/2026", SwingConstants.RIGHT);
        lblNgayGio.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblNgayGio.setForeground(new Color(0x94, 0xA3, 0xB8));

        lblDongHoTo = new JLabel("13:45", SwingConstants.RIGHT);
        lblDongHoTo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDongHoTo.setForeground(Color.WHITE);

        pnlClock.add(lblNgayGio);
        pnlClock.add(lblDongHoTo);
        pnlRight.add(pnlClock);

        header.add(pnlRight, BorderLayout.EAST);
        return header;
    }

    private JButton taoHeaderIconBtn(String text, String tooltip) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        b.setForeground(new Color(0xC5, 0xD1, 0xDE));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setToolTipText(tooltip);
        return b;
    }

    /* =========================================================================
     * 3. KHỞI TẠO CÁC PHÂN HỆ
     * ========================================================================= */
    private void khoiTaoCacPhanHe() {
        // 1. DASHBOARD COMMAND CENTER (SavoreDashboardPanel tự quản lý scroll nội bộ và dock cố định ở đáy)
        pnlDashboard = new SavoreDashboardPanel(nguoiDung, this::moPhanHe);
        pnlContent.add(pnlDashboard, "DASHBOARD");

        // 2. ĐẶT BÀN
        pnlDatBan = new PosQuanLyBanPanel(nguoiDung, ban -> {
            moPhanHe("POS");
            if (pnlBanHang != null) pnlBanHang.napDuLieu();
        });
        pnlContent.add(pnlDatBan, "BAN_DAT");

        // 3. BÁN HÀNG POS
        pnlBanHang = new PosBanHangPanel(nguoiDung);
        pnlContent.add(pnlBanHang, "POS");

        // 4. MÓN ĂN
        pnlMonAn = new PosMonAnPanel(nguoiDung);
        pnlContent.add(pnlMonAn, "MON_AN");

        // 5. COMBO
        pnlCombo = new ComboPanel();
        pnlContent.add(pnlCombo, "COMBO");

        // 6. KHÁCH HÀNG
        pnlKhachHang = new PosKhachHangPanel(nguoiDung);
        pnlContent.add(pnlKhachHang, "KHACH_HANG");

        // 7. KHUYẾN MÃI
        pnlKhuyenMai = new KhuyenMaiPanel();
        pnlContent.add(pnlKhuyenMai, "KHUYEN_MAI");

        // 8. KHO NGUYÊN LIỆU
        pnlKho = new KhoNguyenLieuPanel();
        pnlContent.add(pnlKho, "KHO");

        // 9. NHÂN SỰ
        if (isAdmin) {
            pnlNhanVien = new PosNhanVienPanel(nguoiDung);
            pnlContent.add(pnlNhanVien, "NHAN_VIEN");
        }

        // 10. DOANH THU & BÁO CÁO
        pnlDoanhThu = new BaoCaoDoanhThuPanel();
        pnlContent.add(pnlDoanhThu, "DOANH_THU");

        // 11. THIẾT LẬP
        pnlCaiDat = new CaiDatPanel();
        pnlContent.add(pnlCaiDat, "CAI_DAT");

        // Bổ sung: Bếp KDS, Sơ đồ bàn, Đơn hàng
        pnlNhaBep = new NhaBepPanel();
        pnlContent.add(pnlNhaBep, "BEP");

        pnlQuanLyBan = new PosQuanLyBanPanel(nguoiDung, ban -> {
            moPhanHe("POS");
            if (pnlBanHang != null) pnlBanHang.napDuLieu();
        });
        pnlContent.add(pnlQuanLyBan, "BAN");

        pnlDonHang = new DonHangPanel();
        pnlContent.add(pnlDonHang, "DON_HANG");
    }

    public void moPhanHe(String ma) {
        String target = ma;
        if ("BAN".equalsIgnoreCase(ma) && !navItems.containsKey("BAN")) target = "BAN_DAT";
        if ("BEP".equalsIgnoreCase(ma) && !navItems.containsKey("BEP")) target = "POS";

        this.currentCard = target;
        cardLayout.show(pnlContent, target);

        for (Map.Entry<String, NavItem> entry : navItems.entrySet()) {
            entry.getValue().setActive(entry.getKey().equalsIgnoreCase(target));
        }

        if ("DASHBOARD".equalsIgnoreCase(target) && pnlDashboard != null) {
            pnlDashboard.napDuLieu();
        }
    }

    private void khoiDongDongHo() {
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
            if (lblNgayGio != null) lblNgayGio.setText(sdfDate.format(now));
            if (lblDongHoTo != null) lblDongHoTo.setText(sdfTime.format(now));
        });
        timer.start();
    }

    private void kiemTraKetNoiMySQL() {
        SwingWorker<Boolean, Void> sw = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try (Connection c = DatabaseConnection.getConnection()) {
                    return c != null && !c.isClosed();
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean ok = get();
                    if (lblDbStatus != null) {
                        lblDbStatus.setText(ok ? "● MySQL LIVE 1.8ms" : "● DEMO MODE");
                        lblDbStatus.setForeground(ok ? new Color(0x10, 0xB9, 0x81) : new Color(0xF5, 0x9E, 0x0B));
                    }
                } catch (Exception ignored) {}
            }
        };
        sw.execute();
    }

    private void khoiTaoPhimTat() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "kF1");
        am.put("kF1", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { moPhanHe("DASHBOARD"); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "kF2");
        am.put("kF2", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { moPhanHe("BAN_DAT"); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "kF3");
        am.put("kF3", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { moPhanHe("POS"); }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK), "kSearch");
        am.put("kSearch", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (txtTimKiem != null) txtTimKiem.requestFocusInWindow();
            }
        });
    }

    private void dangXuat() {
        int opt = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            dispose();
            new DangNhapFrame().setVisible(true);
        }
    }

    /* =========================================================================
     * NÚT ĐIỀU HƯỚNG ICON RAIL (72px)
     * ========================================================================= */
    private static class NavItem extends JButton {
        private final String iconText;
        private boolean active = false;

        NavItem(String icon, String tooltip) {
            this.iconText = icon;
            setPreferredSize(new Dimension(52, 44));
            setMaximumSize(new Dimension(52, 44));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setToolTipText(tooltip);
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            boolean hover = getModel().isRollover();

            if (active) {
                // Background viên thuốc vàng đồng tối
                g2.setColor(new Color(0xC8, 0x92, 0x45, 42));
                g2.fillRoundRect(2, 2, w - 4, h - 4, 12, 12);
                g2.setColor(SavoreDesignSystem.Colors.COPPER_GOLD);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(2, 2, w - 5, h - 5, 12, 12);

                // Chỉ báo thanh dọc vàng đồng mép trái
                g2.setColor(SavoreDesignSystem.Colors.WARM_GOLD);
                g2.fillRoundRect(0, 8, 3, h - 16, 2, 2);
            } else if (hover) {
                g2.setColor(new Color(0x18, 0x22, 0x33));
                g2.fillRoundRect(2, 2, w - 4, h - 4, 12, 12);
                g2.setColor(new Color(0xC8, 0x92, 0x45, 45));
                g2.drawRoundRect(2, 2, w - 5, h - 5, 12, 12);
            }

            // Icon trung tâm
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            FontMetrics fm = g2.getFontMetrics();
            int ix = (w - fm.stringWidth(iconText)) / 2;
            int iy = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(active ? Color.WHITE : (hover ? Color.WHITE : new Color(0x9E, 0xA9, 0xB6)));
            g2.drawString(iconText, ix, iy);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> new QuanLyNhaHangFrame().setVisible(true));
    }
}
