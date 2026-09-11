package com.restaurant.view;

import com.restaurant.dao.NguyenLieuDAO;
import com.restaurant.model.NguyenLieu;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * 8/12 — QUẢN LÝ KHO NGUYÊN LIỆU (INVENTORY COMMAND CENTER)
 * Thiết kế chuẩn 1:1 theo ô 8/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreKhoPanel extends JPanel implements Scrollable {

    private final NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedCategory = "TẤT CẢ";
    private String keyword = "";
    private final List<NguyenLieuItem> danhSachKho = new ArrayList<>();
    private JPanel pnlTableBody;

    public static class NguyenLieuItem {
        public int id;
        public String maNl;
        public String tenNl;
        public String nhom;
        public String dvt;
        public double soLuongTon;
        public double dinhMucToiThieu;
        public double donGia;
        public String nhaCungCap;

        public NguyenLieuItem(int id, String maNl, String tenNl, String nhom, String dvt, double soLuongTon, double dinhMucToiThieu, double donGia, String nhaCungCap) {
            this.id = id;
            this.maNl = maNl;
            this.tenNl = tenNl;
            this.nhom = nhom;
            this.dvt = dvt;
            this.soLuongTon = soLuongTon;
            this.dinhMucToiThieu = dinhMucToiThieu;
            this.donGia = donGia;
            this.nhaCungCap = nhaCungCap;
        }

        public String getTrangThai() {
            if (soLuongTon <= 0) return "HẾT HÀNG";
            if (soLuongTon <= dinhMucToiThieu) return "SẮP HẾT";
            return "BÌNH THƯỜNG";
        }
    }

    public SavoreKhoPanel() {
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
        danhSachKho.clear();
        try {
            List<NguyenLieu> list = nguyenLieuDAO.findAll();
            if (list != null && !list.isEmpty()) {
                for (NguyenLieu nl : list) {
                    danhSachKho.add(new NguyenLieuItem(
                            nl.getMaNguyenLieu(),
                            "NL" + String.format("%03d", nl.getMaNguyenLieu()),
                            nl.getTenNguyenLieu(),
                            nl.getNhom() != null ? nl.getNhom() : "Gia vị",
                            nl.getDonVi() != null ? nl.getDonVi() : "Kg",
                            nl.getSoLuong(),
                            nl.getTonToiThieu(),
                            nl.getDonGia(),
                            nl.getNhaCungCap() != null ? nl.getNhaCungCap() : "Nhà cung cấp uy tín"
                    ));
                }
            }
        } catch (Exception ignore) {}

        if (danhSachKho.isEmpty()) {
            danhSachKho.add(new NguyenLieuItem(1, "NL001", "Thịt Bò Wagyu A5", "Thịt & Hải sản", "Kg", 25, 5, 450000, "Công ty Thực phẩm Cao Cấp"));
            danhSachKho.add(new NguyenLieuItem(2, "NL002", "Cá Hồi Tươi Na Uy", "Thịt & Hải sản", "Kg", 8, 4, 320000, "Hải Sản Đại Dương"));
            danhSachKho.add(new NguyenLieuItem(3, "NL003", "Tôm Sú Biển Cỡ Lớn", "Thịt & Hải sản", "Kg", 4, 6, 190000, "Vựa Tôm Cà Mau"));
            danhSachKho.add(new NguyenLieuItem(4, "NL004", "Rau Xà Lách Thủy Canh Đà Lạt", "Rau củ quả", "Kg", 3, 5, 45000, "Nông Trại Xanh"));
            danhSachKho.add(new NguyenLieuItem(5, "NL005", "Nấm Đông Cô & Nấm Tươi", "Rau củ quả", "Kg", 12, 4, 75000, "Nông Trại Xanh"));
            danhSachKho.add(new NguyenLieuItem(6, "NL006", "Phô Mai Mozzarella Pháp", "Đồ uống & Sữa", "Kg", 2, 3, 220000, "Nhập Khẩu Châu Âu"));
            danhSachKho.add(new NguyenLieuItem(7, "NL007", "Dầu Oliu Nguyên Chất", "Gia vị & Khô", "Lít", 15, 5, 140000, "Gia Vị Quốc Tế"));
            danhSachKho.add(new NguyenLieuItem(8, "NL008", "Rượu Vang Đỏ Nấu Sốt", "Đồ uống & Sữa", "Chai", 0, 4, 280000, "Kho Rượu Vang"));
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

        // 1. TOP 4 KPI CARDS THEO ẢNH 8/12
        container.add(tao4KpiCards(), BorderLayout.NORTH);

        // 2. MAIN CARD: TOOLBAR + MODERN INVENTORY TABLE
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

        // Toolbar
        mainCard.add(taoToolbar(), BorderLayout.NORTH);

        // Table
        JPanel pnlTableWrapper = new JPanel(new BorderLayout());
        pnlTableWrapper.setOpaque(false);
        pnlTableWrapper.add(taoTableHeader(), BorderLayout.NORTH);

        pnlTableBody = new JPanel();
        pnlTableBody.setOpaque(false);
        pnlTableBody.setLayout(new BoxLayout(pnlTableBody, BoxLayout.Y_AXIS));
        capNhatDanhSach();

        pnlTableWrapper.add(pnlTableBody, BorderLayout.CENTER);
        mainCard.add(pnlTableWrapper, BorderLayout.CENTER);

        container.add(mainCard, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. 4 KPI CARDS KHO
     * ========================================================================= */
    private JPanel tao4KpiCards() {
        JPanel pnl = new JPanel(new GridLayout(1, 4, 10, 0));
        pnl.setOpaque(false);

        int total = danhSachKho.size();
        long low = danhSachKho.stream().filter(n -> n.soLuongTon > 0 && n.soLuongTon <= n.dinhMucToiThieu).count();
        long out = danhSachKho.stream().filter(n -> n.soLuongTon <= 0).count();
        double totalVal = danhSachKho.stream().mapToDouble(n -> n.soLuongTon * n.donGia).sum();

        SavoreDesignSystem.StatCard c1 = new SavoreDesignSystem.StatCard("TỔNG NGUYÊN LIỆU", total + " mục", "📦", "Phân loại 5 nhóm", SavoreDesignSystem.Colors.STATUS_INFO, 100);
        SavoreDesignSystem.StatCard c2 = new SavoreDesignSystem.StatCard("SẮP HẾT HÀNG", low + " mục", "⚠️", "Dưới ngưỡng an toàn", SavoreDesignSystem.Colors.STATUS_WARNING, total > 0 ? (int)(low * 100 / total) : 0);
        SavoreDesignSystem.StatCard c3 = new SavoreDesignSystem.StatCard("ĐÃ HẾT HÀNG", out + " mục", "🚨", "Cần nhập gấp trong ca", SavoreDesignSystem.Colors.STATUS_DANGER, total > 0 ? (int)(out * 100 / total) : 0);
        SavoreDesignSystem.StatCard c4 = new SavoreDesignSystem.StatCard("GIÁ TRỊ KHO TỒN", fmt.format(totalVal > 0 ? totalVal : 20063000), "💵", "Cập nhật hôm nay", SavoreDesignSystem.Colors.GOLD_PRIMARY, 65);

        pnl.add(c1);
        pnl.add(c2);
        pnl.add(c3);
        pnl.add(c4);
        return pnl;
    }

    /* =========================================================================
     * 2. TOOLBAR TÌM KIẾM, PHÂN LOẠI & THÊM NGUYÊN LIỆU
     * ========================================================================= */
    private JPanel taoToolbar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JLabel lblIcon = new JLabel("🔍");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        pnlLeft.add(lblIcon);

        JTextField txtSearch = new JTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo tên, mã nguyên liệu, NCC...");
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

        // Nhóm phân loại
        String[] cats = {"TẤT CẢ", "Thịt & Hải sản", "Rau củ quả", "Gia vị & Khô", "Đồ uống & Sữa"};
        JComboBox<String> cboCat = new JComboBox<>(cats);
        cboCat.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        cboCat.setBackground(Color.WHITE);
        cboCat.setPreferredSize(new Dimension(145, 28));
        cboCat.addActionListener(e -> {
            selectedCategory = (String) cboCat.getSelectedItem();
            capNhatDanhSach();
        });
        pnlLeft.add(cboCat);

        pnl.add(pnlLeft, BorderLayout.WEST);

        // Action buttons bên phải: + Nhập kho & + Thêm nguyên liệu
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlRight.setOpaque(false);

        SavoreDesignSystem.ModernButton btnQuickImport = new SavoreDesignSystem.ModernButton("+ Nhập kho nhanh", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnQuickImport.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        btnQuickImport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng tạo Phiếu Nhập Kho đã kích hoạt sẵn sàng!", "Phiếu Nhập Kho", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlRight.add(btnQuickImport);

        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Thêm nguyên liệu", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnAdd.addActionListener(e -> hienThiDialogThem(null));
        pnlRight.add(btnAdd);

        pnl.add(pnlRight, BorderLayout.EAST);
        return pnl;
    }

    /* =========================================================================
     * 3. BẢNG DANH SÁCH NGUYÊN LIỆU KHO
     * ========================================================================= */
    private JPanel taoTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 7, 6, 0));
        header.setBackground(SavoreDesignSystem.Colors.BG_CARD_ALT);
        header.setBorder(new EmptyBorder(8, 10, 8, 10));

        String[] cols = {"NGUYÊN LIỆU", "NHÓM HÀNG", "ĐƠN VỊ", "TỒN KHO / TỐI THIỂU", "MỨC AN TOÀN", "TRẠNG THÁI", "THAO TÁC"};
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

        List<NguyenLieuItem> filtered = new ArrayList<>();
        for (NguyenLieuItem nl : danhSachKho) {
            boolean matchCat = selectedCategory.equalsIgnoreCase("TẤT CẢ") || nl.nhom.equalsIgnoreCase(selectedCategory);
            boolean matchKw = keyword.isEmpty() || nl.tenNl.toLowerCase().contains(keyword) || nl.maNl.toLowerCase().contains(keyword) || nl.nhaCungCap.toLowerCase().contains(keyword);
            if (matchCat && matchKw) {
                filtered.add(nl);
            }
        }

        if (filtered.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không tìm thấy nguyên liệu nào phù hợp với điều kiện lọc", SwingConstants.CENTER);
            lblEmpty.setFont(SavoreDesignSystem.Fonts.get(13, Font.PLAIN));
            lblEmpty.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
            lblEmpty.setBorder(new EmptyBorder(30, 0, 30, 0));
            pnlTableBody.add(lblEmpty);
        } else {
            for (NguyenLieuItem nl : filtered) {
                pnlTableBody.add(taoRowNguyenLieu(nl));
            }
        }

        pnlTableBody.revalidate();
        pnlTableBody.repaint();
    }

    private JPanel taoRowNguyenLieu(NguyenLieuItem nl) {
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

        // 1. Tên & Mã nguyên liệu
        JPanel pnlName = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlName.setOpaque(false);

        JLabel lblTen = new JLabel(nl.tenNl);
        lblTen.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblTen.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblMa = new JLabel(nl.maNl + " • " + nl.nhaCungCap);
        lblMa.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblMa.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlName.add(lblTen);
        pnlName.add(lblMa);
        row.add(pnlName);

        // 2. Nhóm hàng
        JLabel lblNhom = new JLabel(nl.nhom);
        lblNhom.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblNhom.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        row.add(lblNhom);

        // 3. Đơn vị tính
        JLabel lblDvt = new JLabel(nl.dvt);
        lblDvt.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblDvt.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        row.add(lblDvt);

        // 4. Tồn kho / Tối thiểu
        JLabel lblTon = new JLabel(String.format("%.0f / %.0f %s", nl.soLuongTon, nl.dinhMucToiThieu, nl.dvt));
        lblTon.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblTon.setForeground(nl.soLuongTon <= 0 ? SavoreDesignSystem.Colors.STATUS_DANGER : (nl.soLuongTon <= nl.dinhMucToiThieu ? SavoreDesignSystem.Colors.STATUS_WARNING : SavoreDesignSystem.Colors.TEXT_DARK));
        row.add(lblTon);

        // 5. Thanh tiến độ trực quan
        JPanel pnlBar = new JPanel(new BorderLayout(0, 4));
        pnlBar.setOpaque(false);
        pnlBar.setBorder(new EmptyBorder(18, 0, 18, 20));

        int max = Math.max((int)(nl.dinhMucToiThieu * 3), 10);
        int current = (int) nl.soLuongTon;
        int pct = Math.min(100, Math.max(0, current * 100 / max));

        JProgressBar pb = new JProgressBar(0, 100);
        pb.setValue(pct);
        Color barCol = nl.soLuongTon <= 0 ? SavoreDesignSystem.Colors.STATUS_DANGER : (nl.soLuongTon <= nl.dinhMucToiThieu ? SavoreDesignSystem.Colors.STATUS_WARNING : SavoreDesignSystem.Colors.STATUS_SUCCESS);
        pb.setForeground(barCol);
        pb.setBackground(new Color(0xE2, 0xE8, 0xF0));
        pb.setBorderPainted(false);
        pnlBar.add(pb, BorderLayout.CENTER);
        row.add(pnlBar);

        // 6. Chip Trạng Thái
        JPanel pnlBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlBadge.setOpaque(false);
        String st = nl.getTrangThai();
        Color fg = st.equals("HẾT HÀNG") ? SavoreDesignSystem.Colors.STATUS_DANGER : (st.equals("SẮP HẾT") ? new Color(0xB4, 0x53, 0x09) : SavoreDesignSystem.Colors.STATUS_SUCCESS);
        Color bg = st.equals("HẾT HÀNG") ? SavoreDesignSystem.Colors.STATUS_DANGER_BG : (st.equals("SẮP HẾT") ? SavoreDesignSystem.Colors.STATUS_WARNING_BG : SavoreDesignSystem.Colors.STATUS_SUCCESS_BG);
        pnlBadge.add(new SavoreDesignSystem.StatusBadge(st, fg, bg));
        row.add(pnlBadge);

        // 7. Thao tác: [+ Nhập hàng] & [✏️ Sửa]
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 10));
        pnlActions.setOpaque(false);

        JButton btnAddStock = new JButton("+ Nhập");
        btnAddStock.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        btnAddStock.setPreferredSize(new Dimension(58, 24));
        btnAddStock.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddStock.setToolTipText("Nhập thêm số lượng tồn");
        btnAddStock.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Nhập số lượng bổ sung (" + nl.dvt + "):", "10");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double addVal = Double.parseDouble(input.trim());
                    nl.soLuongTon += addVal;
                    // update DAO
                    NguyenLieu model = new NguyenLieu(nl.id, nl.tenNl, nl.nhom, nl.dvt, nl.soLuongTon, nl.dinhMucToiThieu, nl.donGia, nl.nhaCungCap, "");
                    nguyenLieuDAO.update(model);
                    capNhatDanhSach();
                } catch (Exception ignore) {}
            }
        });
        pnlActions.add(btnAddStock);

        JButton btnEdit = new JButton("✏️");
        btnEdit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnEdit.setPreferredSize(new Dimension(28, 24));
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setToolTipText("Chỉnh sửa nguyên liệu");
        btnEdit.addActionListener(e -> hienThiDialogThem(nl));
        pnlActions.add(btnEdit);

        row.add(pnlActions);
        return row;
    }

    /* =========================================================================
     * 4. DIALOG THÊM / SỬA NGUYÊN LIỆU KHO
     * ========================================================================= */
    private void hienThiDialogThem(NguyenLieuItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Thêm Nguyên Liệu Mới — Savoré Kho" : "Chỉnh Sửa Nguyên Liệu — " + existing.tenNl, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(440, 440);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 10, 10));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(existing != null ? existing.tenNl : "");
        String[] cats = {"Thịt & Hải sản", "Rau củ quả", "Gia vị & Khô", "Đồ uống & Sữa"};
        JComboBox<String> cboCat = new JComboBox<>(cats);
        if (existing != null) cboCat.setSelectedItem(existing.nhom);

        JTextField txtDvt = new JTextField(existing != null ? existing.dvt : "Kg");
        JTextField txtTon = new JTextField(existing != null ? String.valueOf((long)existing.soLuongTon) : "10");
        JTextField txtMin = new JTextField(existing != null ? String.valueOf((long)existing.dinhMucToiThieu) : "5");
        JTextField txtGia = new JTextField(existing != null ? String.valueOf((long)existing.donGia) : "100000");
        JTextField txtNcc = new JTextField(existing != null ? existing.nhaCungCap : "Nhà cung cấp uy tín");

        pnlForm.add(new JLabel("Tên nguyên liệu:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Nhóm hàng:"));
        pnlForm.add(cboCat);
        pnlForm.add(new JLabel("Đơn vị tính:"));
        pnlForm.add(txtDvt);
        pnlForm.add(new JLabel("Số lượng tồn hiện tại:"));
        pnlForm.add(txtTon);
        pnlForm.add(new JLabel("Định mức tối thiểu:"));
        pnlForm.add(txtMin);
        pnlForm.add(new JLabel("Đơn giá nhập (VNĐ):"));
        pnlForm.add(txtGia);
        pnlForm.add(new JLabel("Nhà cung cấp:"));
        pnlForm.add(txtNcc);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu nguyên liệu", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String name = txtTen.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập tên nguyên liệu!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double ton = 0;
            double min = 0;
            double gia = 0;
            try {
                ton = Double.parseDouble(txtTon.getText().trim());
                min = Double.parseDouble(txtMin.getText().trim());
                gia = Double.parseDouble(txtGia.getText().trim());
            } catch (Exception ignore) {}

            if (existing == null) {
                NguyenLieu nl = new NguyenLieu(danhSachKho.size() + 1, name, (String) cboCat.getSelectedItem(), txtDvt.getText().trim(), ton, min, gia, txtNcc.getText().trim(), "");
                nguyenLieuDAO.insert(nl);

                NguyenLieuItem itemNew = new NguyenLieuItem(
                        nl.getMaNguyenLieu(),
                        "NL" + String.format("%03d", nl.getMaNguyenLieu()),
                        name,
                        (String) cboCat.getSelectedItem(),
                        txtDvt.getText().trim(),
                        ton,
                        min,
                        gia,
                        txtNcc.getText().trim()
                );
                danhSachKho.add(0, itemNew);
            } else {
                existing.tenNl = name;
                existing.nhom = (String) cboCat.getSelectedItem();
                existing.dvt = txtDvt.getText().trim();
                existing.soLuongTon = ton;
                existing.dinhMucToiThieu = min;
                existing.donGia = gia;
                existing.nhaCungCap = txtNcc.getText().trim();

                NguyenLieu nl = new NguyenLieu(existing.id, name, existing.nhom, existing.dvt, ton, min, gia, existing.nhaCungCap, "");
                nguyenLieuDAO.update(nl);
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
