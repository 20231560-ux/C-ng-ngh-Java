package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 6/12 — COMBO & SET MENU (MENU COLLECTION)
 * Thiết kế chuẩn 1:1 theo ô 6/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreComboPanel extends JPanel implements Scrollable {

    private final DecimalFormat fmt = new DecimalFormat("#,### đ");
    private String selectedTab = "TẤT CẢ";
    private final List<ComboItem> danhSachCombo = new ArrayList<>();
    private JPanel pnlCardsContainer;

    public static class ComboItem {
        public int id;
        public String maCombo;
        public String tenCombo;
        public String phanLoai;
        public String phucVu; // e.g. "4 - 6 người"
        public String badge;   // e.g. "TIẾT KIỆM 28%", "BÁN CHẠY"
        public Color badgeColor;
        public double giaGoc;
        public double giaUuDai;
        public List<String> danhSachMon;

        public ComboItem(int id, String maCombo, String tenCombo, String phanLoai, String phucVu, String badge, Color badgeColor, double giaGoc, double giaUuDai, List<String> danhSachMon) {
            this.id = id;
            this.maCombo = maCombo;
            this.tenCombo = tenCombo;
            this.phanLoai = phanLoai;
            this.phucVu = phucVu;
            this.badge = badge;
            this.badgeColor = badgeColor;
            this.giaGoc = giaGoc;
            this.giaUuDai = giaUuDai;
            this.danhSachMon = danhSachMon;
        }
    }

    public SavoreComboPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        khoiTaoDuLieuMacDinh();

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

    private void khoiTaoDuLieuMacDinh() {
        danhSachCombo.clear();

        List<String> m1 = List.of("Salad Cá Ngừ Đại Dương", "Súp Bào Ngư Thượng Hạng", "Bò Wagyu A5 Nướng Đá", "Cơm Chiên Hoàng Kim", "Bánh Mousse Chanh Leo");
        danhSachCombo.add(new ComboItem(1, "CB01", "Combo Hoàng Gia Savoré", "Set tiệc VIP", "4 - 6 người", "TIẾT KIỆM 28%", new Color(0xEF, 0x44, 0x44), 1380000, 990000, m1));

        List<String> m2 = List.of("Khai Vị Nem Hải Sản Giòn Rụm", "Nồi Lẩu Nấm Savoré Đặc Biệt", "Đĩa Bò Úc & Tôm Sú Nhúng Lẩu", "Rau Tươi & Mì Trứng Sợi");
        danhSachCombo.add(new ComboItem(2, "CB02", "Set Lẩu Nấm Hải Sản Gia Đình", "Combo gia đình", "3 - 4 người", "BÁN CHẠY NHẤT", new Color(0xF5, 0x9E, 0x0B), 850000, 680000, m2));

        List<String> m3 = List.of("Cơm Bò Xào Tiêu Đen Sốt Savoré", "Canh Rong Biển Thịt Bằm", "Trà Đào Cam Sả Tươi Mát");
        danhSachCombo.add(new ComboItem(3, "CB03", "Lunch Express Thịnh Soạn", "Ưu đãi trưa", "1 - 2 người", "ƯU ĐÃI TRƯA", new Color(0x10, 0xB9, 0x81), 180000, 135000, m3));

        List<String> m4 = List.of("Salad Bò Nướng Thảo Mộc", "2 Phần Steak Bò Wagyu A5 200g", "2 Ly Rượu Vang Đỏ Bordeaux", "Bánh Chocolate Lava Nóng Hổi");
        danhSachCombo.add(new ComboItem(4, "CB04", "Set Hò Hẹn Lãng Mạn (Duo)", "Set tiệc VIP", "2 người", "CHEF SPECIAL", new Color(0x8B, 0x5C, 0xF6), 1150000, 890000, m4));

        List<String> m5 = List.of("Tôm Sú Hoàng Gia Bơ Tỏi", "Hàu Nướng Phô Mai Pháp (6 con)", "Mực Trứng Nướng Sa Tế", "Salad Rong Nho Xốt Mè");
        danhSachCombo.add(new ComboItem(5, "CB05", "Combo BBQ Hải Sản Nướng Than", "Combo gia đình", "4 người", "TIẾT KIỆM 22%", new Color(0xEF, 0x44, 0x44), 1080000, 840000, m5));

        List<String> m6 = List.of("Lẩu Gà Ta Tiềm Ớt Hiểm", "Bao Tử Hầm Tiêu Xanh", "Mì Tươi & Rau Mồng Tơi");
        danhSachCombo.add(new ComboItem(6, "CB06", "Set Ấm Áp Thu Đông", "Theo mùa", "3 - 4 người", "THEO MÙA", new Color(0x3B, 0x82, 0xF6), 720000, 590000, m6));
    }

    private JPanel taoNoiDung() {
        class ContainerPanel extends JPanel implements Scrollable {
            ContainerPanel() {
                super(new BorderLayout(0, 12));
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

        // 1. TOP HEADER & TABS THEO ẢNH 6/12
        container.add(taoTopTabs(), BorderLayout.NORTH);

        // 2. MAIN GRID CARDS
        pnlCardsContainer = new JPanel(new GridLayout(0, 3, 14, 14));
        pnlCardsContainer.setOpaque(false);
        capNhatDanhSachCombo();

        container.add(pnlCardsContainer, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. TOP TABS & NÚT TẠO COMBO MỚI
     * ========================================================================= */
    private JPanel taoTopTabs() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setOpaque(false);

        // Hàng tab bên trái
        JPanel pnlTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTabs.setOpaque(false);

        String[] tabs = {"TẤT CẢ", "Combo gia đình", "Set tiệc VIP", "Ưu đãi trưa", "Theo mùa"};
        for (String tab : tabs) {
            long count = tab.equals("TẤT CẢ") ? danhSachCombo.size() : danhSachCombo.stream().filter(c -> c.phanLoai.equalsIgnoreCase(tab)).count();
            String title = tab + " (" + count + ")";

            JButton btn = new JButton(title) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean isSel = selectedTab.equalsIgnoreCase(tab);
                    g2.setColor(isSel ? SavoreDesignSystem.Colors.GOLD_PRIMARY : Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    if (!isSel) {
                        g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(SavoreDesignSystem.Fonts.get(12, selectedTab.equalsIgnoreCase(tab) ? Font.BOLD : Font.PLAIN));
            btn.setForeground(selectedTab.equalsIgnoreCase(tab) ? Color.WHITE : SavoreDesignSystem.Colors.TEXT_DARK);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(8, 14, 8, 14));

            btn.addActionListener(e -> {
                selectedTab = tab;
                pnlTabs.repaint();
                capNhatDanhSachCombo();
            });

            pnlTabs.add(btn);
        }

        pnl.add(pnlTabs, BorderLayout.WEST);

        // Nút Tạo combo mới bên phải
        SavoreDesignSystem.ModernButton btnNew = new SavoreDesignSystem.ModernButton("+ Tạo combo mới", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnNew.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnNew.addActionListener(e -> hienThiDialogThemCombo(null));
        pnl.add(btnNew, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 2. HIỂN THỊ LƯỚI THẺ COMBO KHỔ LỚN
     * ========================================================================= */
    private void capNhatDanhSachCombo() {
        if (pnlCardsContainer == null) return;
        pnlCardsContainer.removeAll();

        for (ComboItem item : danhSachCombo) {
            if (selectedTab.equalsIgnoreCase("TẤT CẢ") || item.phanLoai.equalsIgnoreCase(selectedTab)) {
                pnlCardsContainer.add(taoCardCombo(item));
            }
        }

        pnlCardsContainer.revalidate();
        pnlCardsContainer.repaint();
    }

    private JPanel taoCardCombo(ComboItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. BANNER HÌNH ẢNH CÓ BADGE TIẾT KIỆM
        JPanel pnlBanner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Image img = SavoreImageRenderer.getDishImage(item.tenCombo, getWidth(), getHeight());
                g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);

                // Vẽ badge nổi bật
                if (item.badge != null && !item.badge.isEmpty()) {
                    g2.setColor(item.badgeColor);
                    g2.fillRoundRect(8, 8, 100, 20, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2.drawString(item.badge, 14, 22);
                }

                // Tag phục vụ
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(getWidth() - 85, 8, 77, 20, 6, 6);
                g2.setColor(new Color(0xF8, 0xF5, 0xF0));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString("👥 " + item.phucVu, getWidth() - 80, 22);

                g2.dispose();
            }
        };
        pnlBanner.setPreferredSize(new Dimension(200, 115));
        pnlBanner.setOpaque(false);
        card.add(pnlBanner, BorderLayout.NORTH);

        // 2. NỘI DUNG COMBO: TIÊU ĐỀ + DANH SÁCH MÓN ĂN
        JPanel pnlCenter = new JPanel();
        pnlCenter.setOpaque(false);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(item.tenCombo);
        lblName.setFont(SavoreDesignSystem.Fonts.get(14, Font.BOLD));
        lblName.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnlCenter.add(lblName);

        JLabel lblCat = new JLabel(item.maCombo + " • " + item.phanLoai);
        lblCat.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblCat.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        lblCat.setBorder(new EmptyBorder(2, 0, 6, 0));
        pnlCenter.add(lblCat);

        // Danh sách các món trong combo
        JPanel pnlDishes = new JPanel();
        pnlDishes.setOpaque(false);
        pnlDishes.setLayout(new BoxLayout(pnlDishes, BoxLayout.Y_AXIS));
        pnlDishes.setBorder(new MatteBorder(1, 0, 1, 0, SavoreDesignSystem.Colors.BORDER));

        for (String dish : item.danhSachMon) {
            JLabel lblDish = new JLabel("• " + dish);
            lblDish.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
            lblDish.setForeground(new Color(0x4A, 0x40, 0x38));
            lblDish.setBorder(new EmptyBorder(2, 2, 2, 2));
            pnlDishes.add(lblDish);
        }
        pnlCenter.add(pnlDishes);

        // 3. GIÁ GỐC GẠCH NGANG & GIÁ ƯU ĐÃI
        JPanel pnlPrice = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        pnlPrice.setOpaque(false);

        JLabel lblOldPrice = new JLabel("<html><strike>" + fmt.format(item.giaGoc) + "</strike></html>");
        lblOldPrice.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblOldPrice.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        pnlPrice.add(lblOldPrice);

        JLabel lblNewPrice = new JLabel(fmt.format(item.giaUuDai));
        lblNewPrice.setFont(SavoreDesignSystem.Fonts.get(16, Font.BOLD));
        lblNewPrice.setForeground(new Color(0xEF, 0x44, 0x44)); // Đỏ nổi bật
        pnlPrice.add(lblNewPrice);

        pnlCenter.add(pnlPrice);
        card.add(pnlCenter, BorderLayout.CENTER);

        // 4. ACTION BUTTONS: [Chỉnh sửa] & [Đặt combo]
        JPanel pnlActions = new JPanel(new GridLayout(1, 2, 8, 0));
        pnlActions.setOpaque(false);
        pnlActions.setBorder(new EmptyBorder(4, 0, 0, 0));

        SavoreDesignSystem.ModernButton btnEdit = new SavoreDesignSystem.ModernButton("✏️ Chỉnh sửa", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnEdit.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        btnEdit.addActionListener(e -> hienThiDialogThemCombo(item));
        pnlActions.add(btnEdit);

        SavoreDesignSystem.ModernButton btnOrder = new SavoreDesignSystem.ModernButton("🛒 Đặt combo", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnOrder.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        btnOrder.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã thêm [" + item.tenCombo + "] vào danh sách gọi món hiện tại!", "Chọn Combo Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlActions.add(btnOrder);

        card.add(pnlActions, BorderLayout.SOUTH);
        return card;
    }

    /* =========================================================================
     * 3. DIALOG TẠO / SỬA COMBO
     * ========================================================================= */
    private void hienThiDialogThemCombo(ComboItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Tạo Combo & Set Menu Mới — Savoré" : "Chỉnh Sửa Combo — " + existing.tenCombo, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(460, 460);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 12));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(existing != null ? existing.tenCombo : "");
        String[] types = {"Combo gia đình", "Set tiệc VIP", "Ưu đãi trưa", "Theo mùa"};
        JComboBox<String> cboType = new JComboBox<>(types);
        if (existing != null) cboType.setSelectedItem(existing.phanLoai);

        JTextField txtPhucVu = new JTextField(existing != null ? existing.phucVu : "3 - 4 người");
        JTextField txtGiaGoc = new JTextField(existing != null ? String.valueOf((long)existing.giaGoc) : "800000");
        JTextField txtGiaUuDai = new JTextField(existing != null ? String.valueOf((long)existing.giaUuDai) : "650000");
        JTextField txtDanhSach = new JTextField(existing != null ? String.join(", ", existing.danhSachMon) : "Salad, Món chính, Canh súp, Tráng miệng");

        pnlForm.add(new JLabel("Tên Combo:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Phân loại:"));
        pnlForm.add(cboType);
        pnlForm.add(new JLabel("Quy mô phục vụ:"));
        pnlForm.add(txtPhucVu);
        pnlForm.add(new JLabel("Giá gốc (VNĐ):"));
        pnlForm.add(txtGiaGoc);
        pnlForm.add(new JLabel("Giá ưu đãi (VNĐ):"));
        pnlForm.add(txtGiaUuDai);
        pnlForm.add(new JLabel("Món ăn (phẩy cách):"));
        pnlForm.add(txtDanhSach);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu Combo", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String name = txtTen.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập tên combo!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double oldPrice = 800000;
            double newPrice = 650000;
            try {
                oldPrice = Double.parseDouble(txtGiaGoc.getText().trim());
                newPrice = Double.parseDouble(txtGiaUuDai.getText().trim());
            } catch (Exception ignore) {}

            String[] rawDishes = txtDanhSach.getText().split(",");
            List<String> dishes = new ArrayList<>();
            for (String d : rawDishes) {
                if (!d.trim().isEmpty()) dishes.add(d.trim());
            }

            int discountPct = (oldPrice > 0) ? (int) Math.round((oldPrice - newPrice) * 100 / oldPrice) : 0;
            String badge = discountPct > 0 ? "TIẾT KIỆM " + discountPct + "%" : "ƯU ĐÃI";

            if (existing == null) {
                ComboItem itemNew = new ComboItem(
                        danhSachCombo.size() + 1,
                        "CB0" + (danhSachCombo.size() + 1),
                        name,
                        (String) cboType.getSelectedItem(),
                        txtPhucVu.getText().trim(),
                        badge,
                        new Color(0xEF, 0x44, 0x44),
                        oldPrice,
                        newPrice,
                        dishes
                );
                danhSachCombo.add(0, itemNew);
            } else {
                existing.tenCombo = name;
                existing.phanLoai = (String) cboType.getSelectedItem();
                existing.phucVu = txtPhucVu.getText().trim();
                existing.giaGoc = oldPrice;
                existing.giaUuDai = newPrice;
                existing.badge = badge;
                existing.danhSachMon = dishes;
            }

            capNhatDanhSachCombo();
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
