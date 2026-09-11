package com.restaurant.view;

import com.restaurant.dao.NguoiDungDAO;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 9/12 — QUẢN LÝ NHÂN SỰ & PHÂN CA (STAFF MANAGEMENT HUB)
 * Thiết kế chuẩn 1:1 theo ô 9/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreNhanSuPanel extends JPanel implements Scrollable {

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedDept = "TẤT CẢ";
    private String selectedShift = "TẤT CẢ";
    private String keyword = "";
    private final List<NhanSuItem> danhSachNhanSu = new ArrayList<>();
    private JPanel pnlGrid;

    public static class NhanSuItem {
        public int id;
        public String maNv;
        public String hoTen;
        public String vaiTro;
        public String caLam;
        public String soDienThoai;
        public String email;
        public double luong;
        public boolean dangLamViec;
        public String trangThaiCa; // "ĐANG TRONG CA", "NGHỈ CA", "NGHỈ PHÉP"

        public NhanSuItem(int id, String maNv, String hoTen, String vaiTro, String caLam, String soDienThoai, String email, double luong, boolean dangLamViec, String trangThaiCa) {
            this.id = id;
            this.maNv = maNv;
            this.hoTen = hoTen;
            this.vaiTro = vaiTro;
            this.caLam = caLam;
            this.soDienThoai = soDienThoai;
            this.email = email;
            this.luong = luong;
            this.dangLamViec = dangLamViec;
            this.trangThaiCa = trangThaiCa;
        }
    }

    public SavoreNhanSuPanel() {
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
        danhSachNhanSu.clear();
        try {
            List<NguoiDung> list = nguoiDungDAO.layDanhSachNhanVien();
            if (list != null && !list.isEmpty()) {
                int count = 0;
                for (NguoiDung nd : list) {
                    count++;
                    String shift = nd.getCaLam() != null ? nd.getCaLam() : ((count % 2 == 0) ? "Ca Sáng (06:00 - 14:00)" : "Ca Chiều (14:00 - 22:00)");
                    String tt = (count % 3 == 0) ? "NGHỈ CA" : "ĐANG TRONG CA";
                    danhSachNhanSu.add(new NhanSuItem(
                            nd.getMaNguoiDung(),
                            "NV" + String.format("%03d", nd.getMaNguoiDung()),
                            nd.getHoTen(),
                            nd.getVaiTro() != null ? nd.getVaiTro() : "PHỤC VỤ",
                            shift,
                            nd.getSoDienThoai() != null ? nd.getSoDienThoai() : "0912 888 777",
                            nd.getEmail() != null ? nd.getEmail() : "staff" + nd.getMaNguoiDung() + "@savore.vn",
                            nd.getLuong() > 0 ? nd.getLuong() : 8500000,
                            nd.isTrangThai(),
                            tt
                    ));
                }
            }
        } catch (Exception ignore) {}

        if (danhSachNhanSu.isEmpty()) {
            danhSachNhanSu.add(new NhanSuItem(1, "NV001", "Nguyễn Văn Hưng", "Tổng Quản Lý", "Ca Sáng (08:00 - 17:00)", "0903 123 456", "hung.nv@savore.vn", 25000000, true, "ĐANG TRONG CA"));
            danhSachNhanSu.add(new NhanSuItem(2, "NV002", "Trần Đình Trọng", "Bếp Trưởng Điều Hành", "Ca Sáng (07:00 - 15:00)", "0914 234 567", "trong.td@savore.vn", 22000000, true, "ĐANG TRONG CA"));
            danhSachNhanSu.add(new NhanSuItem(3, "NV003", "Lê Thanh Thảo", "Trưởng Ca Thu Ngân", "Ca Sáng (06:00 - 14:00)", "0987 345 678", "thao.lt@savore.vn", 12000000, true, "ĐANG TRONG CA"));
            danhSachNhanSu.add(new NhanSuItem(4, "NV004", "Phạm Minh Hoàng", "Giám Sát Bàn VIP", "Ca Chiều (14:00 - 22:00)", "0932 456 789", "hoang.pm@savore.vn", 14000000, true, "NGHỈ CA"));
            danhSachNhanSu.add(new NhanSuItem(5, "NV005", "Đỗ Diệu Linh", "Lễ Tân Tiếp Đón", "Ca Sáng (08:00 - 16:00)", "0965 567 890", "linh.dd@savore.vn", 9500000, true, "ĐANG TRONG CA"));
            danhSachNhanSu.add(new NhanSuItem(6, "NV006", "Vũ Hoàng Long", "Bartender Pha Chế", "Ca Chiều (15:00 - 23:00)", "0944 678 901", "long.vh@savore.vn", 11000000, true, "NGHỈ CA"));
            danhSachNhanSu.add(new NhanSuItem(7, "NV007", "Hoàng Bích Ngân", "Phục Vụ Bàn VIP", "Ca Sáng (06:00 - 14:00)", "0921 789 012", "ngan.hb@savore.vn", 8500000, true, "ĐANG TRONG CA"));
            danhSachNhanSu.add(new NhanSuItem(8, "NV008", "Đặng Tuấn Kiệt", "Phụ Bếp Nóng", "Ca Sáng (06:30 - 14:30)", "0908 890 123", "kiet.dt@savore.vn", 8000000, true, "ĐANG TRONG CA"));
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

        // 1. TOP 4 KPI CARDS THEO ẢNH 9/12
        container.add(tao4KpiCards(), BorderLayout.NORTH);

        // 2. MAIN SECTION: TOOLBAR + STAFF CARDS GRID
        JPanel pnlMain = new JPanel(new BorderLayout(0, 10));
        pnlMain.setOpaque(false);

        // Toolbar
        pnlMain.add(taoToolbar(), BorderLayout.NORTH);

        // Staff Cards Grid
        pnlGrid = new JPanel(new GridLayout(0, 4, 12, 12));
        pnlGrid.setOpaque(false);
        capNhatGrid();

        pnlMain.add(pnlGrid, BorderLayout.CENTER);
        container.add(pnlMain, BorderLayout.CENTER);

        return container;
    }

    /* =========================================================================
     * 1. 4 KPI CARDS NHÂN SỰ
     * ========================================================================= */
    private JPanel tao4KpiCards() {
        JPanel pnl = new JPanel(new GridLayout(1, 4, 10, 0));
        pnl.setOpaque(false);

        int total = danhSachNhanSu.size();
        long onDuty = danhSachNhanSu.stream().filter(n -> n.trangThaiCa.equals("ĐANG TRONG CA")).count();
        long off = total - onDuty;

        SavoreDesignSystem.StatCard c1 = new SavoreDesignSystem.StatCard("TỔNG NHÂN VIÊN", total + " người", "👔", "Chính thức & Part-time", SavoreDesignSystem.Colors.STATUS_INFO, 100);
        SavoreDesignSystem.StatCard c2 = new SavoreDesignSystem.StatCard("ĐANG TRONG CA", onDuty + " người", "🟢", "Ca hiện tại đang làm", SavoreDesignSystem.Colors.STATUS_SUCCESS, total > 0 ? (int)(onDuty * 100 / total) : 0);
        SavoreDesignSystem.StatCard c3 = new SavoreDesignSystem.StatCard("NGHỈ PHÉP / CA", off + " người", "🏖️", "Đã được phê duyệt", SavoreDesignSystem.Colors.STATUS_WARNING, total > 0 ? (int)(off * 100 / total) : 0);
        SavoreDesignSystem.StatCard c4 = new SavoreDesignSystem.StatCard("PHÒNG BAN", "5 nhóm", "🏢", "Bếp, Phục vụ, Thu ngân...", SavoreDesignSystem.Colors.STATUS_PURPLE, 100);

        pnl.add(c1);
        pnl.add(c2);
        pnl.add(c3);
        pnl.add(c4);
        return pnl;
    }

    /* =========================================================================
     * 2. TOOLBAR TÌM KIẾM, PHÂN CA & BỘ PHẬN
     * ========================================================================= */
    private JPanel taoToolbar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JLabel lblIcon = new JLabel("🔍");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        pnlLeft.add(lblIcon);

        JTextField txtSearch = new JTextField(18);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm nhân viên theo tên, SĐT, mã...");
        txtSearch.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SavoreDesignSystem.Colors.BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                keyword = txtSearch.getText().trim().toLowerCase();
                capNhatGrid();
            }
        });
        pnlLeft.add(txtSearch);

        // Lọc Ca trực
        String[] shifts = {"Tất cả các ca", "Ca Sáng", "Ca Chiều"};
        JComboBox<String> cboShift = new JComboBox<>(shifts);
        cboShift.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        cboShift.setBackground(Color.WHITE);
        cboShift.setPreferredSize(new Dimension(130, 28));
        cboShift.addActionListener(e -> {
            int idx = cboShift.getSelectedIndex();
            if (idx == 0) selectedShift = "TẤT CẢ";
            else if (idx == 1) selectedShift = "Ca Sáng";
            else selectedShift = "Ca Chiều";
            capNhatGrid();
        });
        pnlLeft.add(cboShift);

        // Lọc Bộ phận
        String[] depts = {"Tất cả bộ phận", "Quản lý", "Bếp", "Thu ngân", "Phục vụ"};
        JComboBox<String> cboDept = new JComboBox<>(depts);
        cboDept.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        cboDept.setBackground(Color.WHITE);
        cboDept.setPreferredSize(new Dimension(140, 28));
        cboDept.addActionListener(e -> {
            int idx = cboDept.getSelectedIndex();
            if (idx == 0) selectedDept = "TẤT CẢ";
            else selectedDept = depts[idx];
            capNhatGrid();
        });
        pnlLeft.add(cboDept);

        pnl.add(pnlLeft, BorderLayout.WEST);

        // Nút Thêm nhân sự mới bên phải
        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Thêm nhân sự", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnAdd.addActionListener(e -> hienThiDialogThemNhanVien(null));
        pnl.add(btnAdd, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 3. LƯỚI STAFF CARDS CHÂN DUNG & CA TRỰC
     * ========================================================================= */
    private void capNhatGrid() {
        if (pnlGrid == null) return;
        pnlGrid.removeAll();

        List<NhanSuItem> filtered = new ArrayList<>();
        for (NhanSuItem ns : danhSachNhanSu) {
            boolean matchShift = selectedShift.equalsIgnoreCase("TẤT CẢ") || ns.caLam.contains(selectedShift);
            boolean matchDept = selectedDept.equalsIgnoreCase("TẤT CẢ") || ns.vaiTro.toLowerCase().contains(selectedDept.toLowerCase());
            boolean matchKw = keyword.isEmpty() || ns.hoTen.toLowerCase().contains(keyword) || ns.soDienThoai.contains(keyword) || ns.maNv.toLowerCase().contains(keyword);
            if (matchShift && matchDept && matchKw) {
                filtered.add(ns);
            }
        }

        for (NhanSuItem ns : filtered) {
            pnlGrid.add(taoStaffCard(ns));
        }

        pnlGrid.revalidate();
        pnlGrid.repaint();
    }

    private JPanel taoStaffCard(NhanSuItem ns) {
        JPanel card = new JPanel(new BorderLayout(0, 8)) {
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
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 10, 10, 10));

        // 1. TOP AVATAR CHÂN DUNG + ONLINE STATUS DOT
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlTop.setOpaque(false);

        JLabel lblAvatar = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Image avt = SavoreImageRenderer.getStaffAvatar(ns.hoTen, 52, new Color(0x1E, 0x29, 0x3B));
                g2.drawImage(avt, 0, 0, 52, 52, null);

                // Chấm tròn trạng thái hoạt động
                boolean on = ns.trangThaiCa.equals("ĐANG TRONG CA");
                g2.setColor(on ? SavoreDesignSystem.Colors.STATUS_SUCCESS : Color.LIGHT_GRAY);
                g2.fillOval(38, 38, 12, 12);
                g2.setColor(Color.WHITE);
                g2.drawOval(38, 38, 12, 12);

                g2.dispose();
            }
        };
        lblAvatar.setPreferredSize(new Dimension(52, 52));
        pnlTop.add(lblAvatar);
        card.add(pnlTop, BorderLayout.NORTH);

        // 2. CENTER THÔNG TIN NHÂN SỰ
        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(ns.hoTen, SwingConstants.CENTER);
        lblName.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblName.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInfo.add(lblName);

        JLabel lblRole = new JLabel(ns.vaiTro.toUpperCase(), SwingConstants.CENTER);
        lblRole.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblRole.setForeground(SavoreDesignSystem.Colors.GOLD_PRIMARY);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRole.setBorder(new EmptyBorder(2, 0, 6, 0));
        pnlInfo.add(lblRole);

        // Ca trực & SĐT
        JLabel lblShift = new JLabel("🕒 " + ns.caLam, SwingConstants.CENTER);
        lblShift.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblShift.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        lblShift.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInfo.add(lblShift);

        JLabel lblPhone = new JLabel("📞 " + ns.soDienThoai, SwingConstants.CENTER);
        lblPhone.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblPhone.setForeground(SavoreDesignSystem.Colors.TEXT_SUB);
        lblPhone.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPhone.setBorder(new EmptyBorder(2, 0, 6, 0));
        pnlInfo.add(lblPhone);

        // Chip trạng thái ca trực
        JPanel pnlBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlBadge.setOpaque(false);
        boolean onDuty = ns.trangThaiCa.equals("ĐANG TRONG CA");
        pnlBadge.add(new SavoreDesignSystem.StatusBadge(
                ns.trangThaiCa,
                onDuty ? SavoreDesignSystem.Colors.STATUS_SUCCESS : Color.GRAY,
                onDuty ? SavoreDesignSystem.Colors.STATUS_SUCCESS_BG : new Color(0xF1, 0xF5, 0xF9)
        ));
        pnlInfo.add(pnlBadge);

        card.add(pnlInfo, BorderLayout.CENTER);

        // 3. BOTTOM BUTTONS: [Hồ sơ chi tiết] & [✏️]
        JPanel pnlBottom = new JPanel(new GridLayout(1, 2, 6, 0));
        pnlBottom.setOpaque(false);
        pnlBottom.setBorder(new MatteBorder(1, 0, 0, 0, SavoreDesignSystem.Colors.BORDER));

        SavoreDesignSystem.ModernButton btnProfile = new SavoreDesignSystem.ModernButton("👤 Hồ sơ", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnProfile.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        btnProfile.addActionListener(e -> hienThiDialogThemNhanVien(ns));
        pnlBottom.add(btnProfile);

        SavoreDesignSystem.ModernButton btnEdit = new SavoreDesignSystem.ModernButton("✏️ Sửa", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnEdit.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        btnEdit.addActionListener(e -> hienThiDialogThemNhanVien(ns));
        pnlBottom.add(btnEdit);

        card.add(pnlBottom, BorderLayout.SOUTH);
        return card;
    }

    /* =========================================================================
     * 4. DIALOG THÊM / SỬA NHÂN SỰ
     * ========================================================================= */
    private void hienThiDialogThemNhanVien(NhanSuItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Thêm Nhân Sự Mới — Savoré HR" : "Hồ Sơ Nhân Viên — " + existing.hoTen, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(440, 440);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 10, 10));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(existing != null ? existing.hoTen : "");
        String[] roles = {"Tổng Quản Lý", "Bếp Trưởng Điều Hành", "Trưởng Ca Thu Ngân", "Giám Sát Bàn VIP", "Phục Vụ Bàn VIP", "Bartender Pha Chế"};
        JComboBox<String> cboRole = new JComboBox<>(roles);
        if (existing != null) cboRole.setSelectedItem(existing.vaiTro);

        String[] shifts = {"Ca Sáng (06:00 - 14:00)", "Ca Chiều (14:00 - 22:00)", "Ca Gãy (10:00 - 14:00, 18:00 - 22:00)"};
        JComboBox<String> cboShift = new JComboBox<>(shifts);
        if (existing != null) cboShift.setSelectedItem(existing.caLam);

        JTextField txtSdt = new JTextField(existing != null ? existing.soDienThoai : "");
        JTextField txtEmail = new JTextField(existing != null ? existing.email : "");
        JTextField txtLuong = new JTextField(existing != null ? String.valueOf((long)existing.luong) : "9000000");

        String[] statuses = {"ĐANG TRONG CA", "NGHỈ CA", "NGHỈ PHÉP"};
        JComboBox<String> cboStatus = new JComboBox<>(statuses);
        if (existing != null) cboStatus.setSelectedItem(existing.trangThaiCa);

        pnlForm.add(new JLabel("Họ và tên nhân sự:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Vị trí / Vai trò:"));
        pnlForm.add(cboRole);
        pnlForm.add(new JLabel("Ca làm việc phân công:"));
        pnlForm.add(cboShift);
        pnlForm.add(new JLabel("Số điện thoại:"));
        pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Email liên hệ:"));
        pnlForm.add(txtEmail);
        pnlForm.add(new JLabel("Mức lương tháng (VNĐ):"));
        pnlForm.add(txtLuong);
        pnlForm.add(new JLabel("Trạng thái ca hôm nay:"));
        pnlForm.add(cboStatus);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu nhân sự", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String name = txtTen.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập họ tên nhân viên!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double luong = 9000000;
            try { luong = Double.parseDouble(txtLuong.getText().trim()); } catch (Exception ignore) {}

            if (existing == null) {
                NguoiDung nd = new NguoiDung();
                nd.setHoTen(name);
                nd.setTenDangNhap("nv" + (danhSachNhanSu.size() + 1));
                nd.setVaiTro((String) cboRole.getSelectedItem());
                nd.setCaLam((String) cboShift.getSelectedItem());
                nd.setSoDienThoai(txtSdt.getText().trim());
                nd.setEmail(txtEmail.getText().trim());
                nd.setLuong(luong);
                nd.setTrangThai(true);
                try { nguoiDungDAO.themNhanVien(nd); } catch (Exception ignore) {}

                NhanSuItem itemNew = new NhanSuItem(
                        nd.getMaNguoiDung() > 0 ? nd.getMaNguoiDung() : (danhSachNhanSu.size() + 1),
                        "NV" + String.format("%03d", danhSachNhanSu.size() + 1),
                        name,
                        (String) cboRole.getSelectedItem(),
                        (String) cboShift.getSelectedItem(),
                        txtSdt.getText().trim(),
                        txtEmail.getText().trim(),
                        luong,
                        true,
                        (String) cboStatus.getSelectedItem()
                );
                danhSachNhanSu.add(0, itemNew);
            } else {
                existing.hoTen = name;
                existing.vaiTro = (String) cboRole.getSelectedItem();
                existing.caLam = (String) cboShift.getSelectedItem();
                existing.soDienThoai = txtSdt.getText().trim();
                existing.email = txtEmail.getText().trim();
                existing.luong = luong;
                existing.trangThaiCa = (String) cboStatus.getSelectedItem();

                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(existing.id);
                nd.setHoTen(name);
                nd.setVaiTro(existing.vaiTro);
                nd.setCaLam(existing.caLam);
                nd.setSoDienThoai(existing.soDienThoai);
                nd.setEmail(existing.email);
                nd.setLuong(luong);
                nd.setTrangThai(true);
                try { nguoiDungDAO.suaNhanVien(nd); } catch (Exception ignore) {}
            }

            capNhatGrid();
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
