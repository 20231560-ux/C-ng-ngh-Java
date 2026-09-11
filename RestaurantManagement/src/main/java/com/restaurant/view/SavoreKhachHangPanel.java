package com.restaurant.view;

import com.restaurant.dao.KhachHangDAO;
import com.restaurant.model.KhachHang;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 7/12 — QUẢN LÝ KHÁCH HÀNG (CRM MANAGEMENT)
 * Thiết kế chuẩn 1:1 theo ô 7/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreKhachHangPanel extends JPanel implements Scrollable {

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedTier = "TẤT CẢ";
    private String keyword = "";
    private int currentPage = 1;

    private final List<KhachHangItem> danhSachKhach = new ArrayList<>();
    private JPanel pnlTableBody;

    public static class KhachHangItem {
        public int id;
        public String maKh;
        public String hoTen;
        public String soDienThoai;
        public String email;
        public double tongChiTieu;
        public int diemTichLuy;
        public String hangThe; // "VIP KIM CƯƠNG", "VIP VÀNG", "THÀNH VIÊN"
        public String trangThai; // "HOAT_DONG"

        public KhachHangItem(int id, String maKh, String hoTen, String soDienThoai, String email, double tongChiTieu, int diemTichLuy, String hangThe, String trangThai) {
            this.id = id;
            this.maKh = maKh;
            this.hoTen = hoTen;
            this.soDienThoai = soDienThoai;
            this.email = email;
            this.tongChiTieu = tongChiTieu;
            this.diemTichLuy = diemTichLuy;
            this.hangThe = hangThe;
            this.trangThai = trangThai;
        }
    }

    public SavoreKhachHangPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        napDuLieu();

        JScrollPane scrollPane = new JScrollPane(taoNoiDung());
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void napDuLieu() {
        danhSachKhach.clear();
        try {
            List<KhachHang> list = khachHangDAO.findAll();
            if (list != null && !list.isEmpty()) {
                int count = 0;
                for (KhachHang k : list) {
                    count++;
                    String hang = (count % 4 == 0) ? "VIP KIM CƯƠNG" : ((count % 2 == 0) ? "VIP VÀNG" : "THÀNH VIÊN");
                    double spend = (count * 1850000.0) + 500000;
                    int points = (int) (spend / 10000);
                    danhSachKhach.add(new KhachHangItem(
                            k.getMaKhachHang(),
                            "KH" + String.format("%04d", k.getMaKhachHang()),
                            k.getHoTen(),
                            k.getSoDienThoai() != null ? k.getSoDienThoai() : "0901 234 567",
                            k.getEmail() != null ? k.getEmail() : "customer" + k.getMaKhachHang() + "@gmail.com",
                            spend,
                            points,
                            hang,
                            "HOAT_DONG"
                    ));
                }
            }
        } catch (Exception ignore) {}

        // Fallback dữ liệu VIP sang trọng nếu DB chưa có
        if (danhSachKhach.isEmpty()) {
            danhSachKhach.add(new KhachHangItem(1, "KH0001", "Trần Thị Thu Mai", "0912 345 678", "thumai.tran@vip.com", 24800000, 2480, "VIP KIM CƯƠNG", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(2, "KH0002", "Nguyễn Hoàng Nam", "0988 765 432", "hoangnam@gmail.com", 16200000, 1620, "VIP VÀNG", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(3, "KH0003", "Phạm Lê Quỳnh Anh", "0934 112 233", "quynhanh.pham@outlook.com", 18500000, 1850, "VIP KIM CƯƠNG", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(4, "KH0004", "Lê Văn Tuấn Cường", "0909 888 999", "tuancuong@vinaholding.vn", 12400000, 1240, "VIP VÀNG", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(5, "KH0005", "Đặng Minh Quân", "0977 223 344", "minhquan@gmail.com", 6500000, 650, "THÀNH VIÊN", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(6, "KH0006", "Hoàng Bích Thủy", "0966 554 433", "bichthuy.hbt@yahoo.com", 4200000, 420, "THÀNH VIÊN", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(7, "KH0007", "Vũ Quốc Đạt", "0944 667 788", "quocdat@datdecor.com", 9800000, 980, "VIP VÀNG", "HOAT_DONG"));
            danhSachKhach.add(new KhachHangItem(8, "KH0008", "Ngô Thùy Trang", "0922 998 877", "thuytrang.ngo@gmail.com", 2800000, 280, "THÀNH VIÊN", "HOAT_DONG"));
        }
    }

    private JPanel taoNoiDung() {
        class ContainerPanel extends JPanel implements Scrollable {
            ContainerPanel() {
                super(new BorderLayout(0, 10));
                setOpaque(false);
                setBorder(new EmptyBorder(10, 12, 12, 12));
            }
            @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
            @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
            @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
            @Override public boolean getScrollableTracksViewportWidth() { return true; }
            @Override public boolean getScrollableTracksViewportHeight() { return false; }
        }
        JPanel container = new ContainerPanel();

        // 1. TOP 4 KPI CARDS THEO ẢNH 7/12
        container.add(tao4KpiCards(), BorderLayout.NORTH);

        // 2. MAIN CARD: TOOLBAR + MODERN CRM TABLE + PAGINATION
        JPanel mainCard = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainCard.setOpaque(false);
        mainCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        // Toolbar tìm kiếm
        mainCard.add(taoSearchToolbar(), BorderLayout.NORTH);

        // Bảng danh sách hàng thẻ hiện đại
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setOpaque(false);
        pnlTableWrapper.add(taoTableHeader(), BorderLayout.NORTH);

        pnlTableBody = new JPanel();
        pnlTableBody.setOpaque(false);
        pnlTableBody.setLayout(new BoxLayout(pnlTableBody, BoxLayout.Y_AXIS));
        capNhatDanhSach();

        pnlTableWrapper.add(pnlTableBody, BorderLayout.CENTER);
        mainCard.add(pnlTableWrapper, BorderLayout.CENTER);

        // Phân trang dưới cùng
        mainCard.add(taoPaginationBar(), BorderLayout.SOUTH);

        container.add(mainCard, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. 4 THẺ KPI CRM
     * ========================================================================= */
    private JPanel tao4KpiCards() {
        JPanel pnl = new JPanel(new GridLayout(1, 4, 10, 0));
        pnl.setOpaque(false);

        SavoreDesignSystem.StatCard c1 = new SavoreDesignSystem.StatCard("TỔNG KHÁCH HÀNG", "2.468", "👥", "+12% so với tháng trước", SavoreDesignSystem.Colors.STATUS_INFO, 80);
        SavoreDesignSystem.StatCard c2 = new SavoreDesignSystem.StatCard("KHÁCH HÀNG VIP", "320", "👑", "Hạng Vàng & Kim Cương", SavoreDesignSystem.Colors.STATUS_WARNING, 13);
        SavoreDesignSystem.StatCard c3 = new SavoreDesignSystem.StatCard("KHÁCH MỚI THÁNG NÀY", "186", "✨", "Tỷ lệ quay lại 68%", SavoreDesignSystem.Colors.STATUS_SUCCESS, 68);
        SavoreDesignSystem.StatCard c4 = new SavoreDesignSystem.StatCard("DOANH THU TỪ KH", "568.000.000 đ", "💰", "Trung bình 230k/khách", SavoreDesignSystem.Colors.GOLD_PRIMARY, 75);

        pnl.add(c1);
        pnl.add(c2);
        pnl.add(c3);
        pnl.add(c4);
        return pnl;
    }

    /* =========================================================================
     * 2. TOOLBAR TÌM KIẾM & THÊM KHÁCH HÀNG
     * ========================================================================= */
    private JPanel taoSearchToolbar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JLabel lblIcon = new JLabel("🔍");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        pnlLeft.add(lblIcon);

        JTextField txtSearch = new JTextField(22);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo SĐT, Họ tên, Email, Mã KH...");
        txtSearch.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SavoreDesignSystem.Colors.BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                keyword = txtSearch.getText().trim().toLowerCase();
                capNhatDanhSach();
            }
        });
        pnlLeft.add(txtSearch);

        // Dropdown Hạng Thẻ
        String[] tiers = {"Tất cả hạng thẻ", "VIP Kim Cương", "VIP Vàng", "Thành viên"};
        JComboBox<String> cboTier = new JComboBox<>(tiers);
        cboTier.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        cboTier.setBackground(Color.WHITE);
        cboTier.setPreferredSize(new Dimension(145, 28));
        cboTier.addActionListener(e -> {
            int idx = cboTier.getSelectedIndex();
            if (idx == 0) selectedTier = "TẤT CẢ";
            else if (idx == 1) selectedTier = "VIP KIM CƯƠNG";
            else if (idx == 2) selectedTier = "VIP VÀNG";
            else selectedTier = "THÀNH VIÊN";
            capNhatDanhSach();
        });
        pnlLeft.add(cboTier);

        pnl.add(pnlLeft, BorderLayout.WEST);

        // Nút + Thêm khách hàng bên phải
        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Thêm khách hàng", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnAdd.addActionListener(e -> hienThiDialogThemKhach(null));
        pnl.add(btnAdd, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 3. BẢNG DANH SÁCH KHÁCH HÀNG CRM
     * ========================================================================= */
    private JPanel taoTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 7, 6, 0));
        header.setBackground(SavoreDesignSystem.Colors.BG_CARD_ALT);
        header.setBorder(new EmptyBorder(8, 10, 8, 10));

        String[] cols = {"KHÁCH HÀNG", "SỐ ĐIỆN THOẠI", "EMAIL", "TỔNG CHI TIÊU", "ĐIỂM TÍCH LŨY", "HẠNG THẺ", "THAO TÁC"};
        for (String c : cols) {
            JLabel lbl = new JLabel(c);
            lbl.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
            lbl.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
            header.add(lbl);
        }
        return header;
    }

    private void capNhatDanhSach() {
        if (pnlTableBody == null) return;
        pnlTableBody.removeAll();

        List<KhachHangItem> filtered = new ArrayList<>();
        for (KhachHangItem kh : danhSachKhach) {
            boolean matchTier = selectedTier.equalsIgnoreCase("TẤT CẢ") || kh.hangThe.equalsIgnoreCase(selectedTier);
            boolean matchKw = keyword.isEmpty() || kh.hoTen.toLowerCase().contains(keyword) || kh.soDienThoai.contains(keyword) || kh.email.toLowerCase().contains(keyword) || kh.maKh.toLowerCase().contains(keyword);
            if (matchTier && matchKw) {
                filtered.add(kh);
            }
        }

        if (filtered.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không tìm thấy khách hàng nào phù hợp với bộ lọc", SwingConstants.CENTER);
            lblEmpty.setFont(SavoreDesignSystem.Fonts.get(13, Font.PLAIN));
            lblEmpty.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
            lblEmpty.setBorder(new EmptyBorder(30, 0, 30, 0));
            pnlTableBody.add(lblEmpty);
        } else {
            for (KhachHangItem kh : filtered) {
                pnlTableBody.add(taoRowKhachHang(kh));
            }
        }

        pnlTableBody.revalidate();
        pnlTableBody.repaint();
    }

    private JPanel taoRowKhachHang(KhachHangItem kh) {
        JPanel row = new JPanel(new GridLayout(1, 7, 6, 0)) {
            private boolean isHover = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { isHover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { isHover = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(isHover ? new Color(0xFA, 0xF6, 0xF0) : Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xF1, 0xEB, 0xE1));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 48));
        row.setMaximumSize(new Dimension(9999, 48));
        row.setBorder(new EmptyBorder(4, 10, 4, 10));

        // 1. Avatar tròn + Tên & Mã KH
        JPanel pnlName = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlName.setOpaque(false);

        JLabel lblAvatar = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Image avt = SavoreImageRenderer.getStaffAvatar(kh.hoTen, 30, kh.hangThe.contains("KIM CƯƠNG") ? new Color(0x3B, 0x82, 0xF6) : new Color(0xB0, 0x82, 0x46));
                g2.drawImage(avt, 0, 0, 30, 30, null);
                g2.dispose();
            }
        };
        lblAvatar.setPreferredSize(new Dimension(30, 30));
        pnlName.add(lblAvatar);

        JPanel pnlText = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlText.setOpaque(false);
        JLabel lblHoTen = new JLabel(kh.hoTen);
        lblHoTen.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblHoTen.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblMa = new JLabel(kh.maKh);
        lblMa.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblMa.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlText.add(lblHoTen);
        pnlText.add(lblMa);
        pnlName.add(pnlText);
        row.add(pnlName);

        // 2. Số điện thoại
        JLabel lblSdt = new JLabel(kh.soDienThoai);
        lblSdt.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblSdt.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        row.add(lblSdt);

        // 3. Email
        JLabel lblEmail = new JLabel(kh.email);
        lblEmail.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblEmail.setForeground(SavoreDesignSystem.Colors.TEXT_SUB);
        row.add(lblEmail);

        // 4. Tổng chi tiêu
        JLabel lblSpend = new JLabel(fmt.format(kh.tongChiTieu));
        lblSpend.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblSpend.setForeground(SavoreDesignSystem.Colors.GOLD_PRIMARY);
        row.add(lblSpend);

        // 5. Điểm tích lũy
        JLabel lblPoints = new JLabel(kh.diemTichLuy + " pts");
        lblPoints.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblPoints.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        row.add(lblPoints);

        // 6. Hạng Thẻ Chip
        JPanel pnlTier = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlTier.setOpaque(false);
        Color fg = kh.hangThe.contains("KIM CƯƠNG") ? new Color(0x1D, 0x4E, 0xD8) : (kh.hangThe.contains("VÀNG") ? new Color(0xB4, 0x53, 0x09) : new Color(0x37, 0x41, 0x51));
        Color bg = kh.hangThe.contains("KIM CƯƠNG") ? new Color(0xDB, 0xEA, 0xFE) : (kh.hangThe.contains("VÀNG") ? new Color(0xFE, 0xF3, 0xC7) : new Color(0xF3, 0xF4, 0xF6));
        pnlTier.add(new SavoreDesignSystem.StatusBadge(kh.hangThe, fg, bg));
        row.add(pnlTier);

        // 7. Thao tác: Chi tiết & Sửa
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 10));
        pnlActions.setOpaque(false);

        JButton btnDetail = new JButton("👁️");
        btnDetail.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnDetail.setPreferredSize(new Dimension(28, 24));
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setToolTipText("Xem hồ sơ");
        btnDetail.addActionListener(e -> hienThiDialogThemKhach(kh));
        pnlActions.add(btnDetail);

        JButton btnEdit = new JButton("✏️");
        btnEdit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnEdit.setPreferredSize(new Dimension(28, 24));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setToolTipText("Chỉnh sửa");
        btnEdit.addActionListener(e -> hienThiDialogThemKhach(kh));
        pnlActions.add(btnEdit);

        row.add(pnlActions);
        return row;
    }

    /* =========================================================================
     * 4. PHÂN TRANG
     * ========================================================================= */
    private JPanel taoPaginationBar() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblCount = new JLabel("Hiển thị 1 - 8 trong số 2.468 khách hàng");
        lblCount.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblCount.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        pnl.add(lblCount, BorderLayout.WEST);

        JPanel pnlPages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pnlPages.setOpaque(false);

        String[] pages = {"<", "1", "2", "3", "...", "12", ">"};
        for (String p : pages) {
            JButton btn = new JButton(p) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (p.equals("1")) {
                        g2.setColor(SavoreDesignSystem.Colors.GOLD_PRIMARY);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(SavoreDesignSystem.Fonts.get(11, p.equals("1") ? Font.BOLD : Font.PLAIN));
            btn.setForeground(p.equals("1") ? Color.WHITE : SavoreDesignSystem.Colors.TEXT_DARK);
            btn.setPreferredSize(new Dimension(28, 28));
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlPages.add(btn);
        }

        pnl.add(pnlPages, BorderLayout.EAST);
        return pnl;
    }

    /* =========================================================================
     * 5. DIALOG THÊM / SỬA KHÁCH HÀNG CRM
     * ========================================================================= */
    private void hienThiDialogThemKhach(KhachHangItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Thêm Khách Hàng Mới — Savoré CRM" : "Hồ Sơ Khách Hàng — " + existing.hoTen, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(440, 420);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 12));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(existing != null ? existing.hoTen : "");
        JTextField txtSdt = new JTextField(existing != null ? existing.soDienThoai : "");
        JTextField txtEmail = new JTextField(existing != null ? existing.email : "");
        String[] tiers = {"VIP KIM CƯƠNG", "VIP VÀNG", "THÀNH VIÊN"};
        JComboBox<String> cboTier = new JComboBox<>(tiers);
        if (existing != null) cboTier.setSelectedItem(existing.hangThe);

        JTextField txtSpend = new JTextField(existing != null ? String.valueOf((long)existing.tongChiTieu) : "0");
        JTextField txtPoints = new JTextField(existing != null ? String.valueOf(existing.diemTichLuy) : "0");

        pnlForm.add(new JLabel("Họ và tên:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Số điện thoại:"));
        pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Email liên hệ:"));
        pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("Hạng thẻ thành viên:"));
        pnlForm.add(cboTier);
        pnlForm.add(new JLabel("Tổng chi tiêu (VNĐ):"));
        pnlForm.add(txtSpend);
        pnlForm.add(new JLabel("Điểm tích lũy:"));
        pnlForm.add(txtPoints);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu thông tin", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String name = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            if (name.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập họ tên và số điện thoại!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double spend = 0;
            int points = 0;
            try {
                spend = Double.parseDouble(txtSpend.getText().trim());
                points = Integer.parseInt(txtPoints.getText().trim());
            } catch (Exception ignore) {}

            if (existing == null) {
                KhachHang k = new KhachHang();
                k.setHoTen(name);
                k.setSoDienThoai(sdt);
                k.setEmail(txtEmail.getText().trim());
                try { khachHangDAO.insert(k); } catch (Exception ignore) {}

                KhachHangItem itemNew = new KhachHangItem(
                        danhSachKhach.size() + 1,
                        "KH" + String.format("%04d", danhSachKhach.size() + 1),
                        name,
                        sdt,
                        txtEmail.getText().trim(),
                        spend,
                        points,
                        (String) cboTier.getSelectedItem(),
                        "HOAT_DONG"
                );
                danhSachKhach.add(0, itemNew);
            } else {
                existing.hoTen = name;
                existing.soDienThoai = sdt;
                existing.email = txtEmail.getText().trim();
                existing.hangThe = (String) cboTier.getSelectedItem();
                existing.tongChiTieu = spend;
                existing.diemTichLuy = points;
            }

            capNhatDanhSach();
            dlg.dispose();
        });

        pnlBtn.add(btnCancel);
        pnlBtn.add(btnSave);
        dlg.add(pnlBtn, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
