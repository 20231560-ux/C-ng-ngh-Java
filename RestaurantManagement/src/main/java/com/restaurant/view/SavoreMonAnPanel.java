package com.restaurant.view;

import com.restaurant.dao.MonAnDAO;
import com.restaurant.model.MonAn;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 5/12 — QUẢN LÝ MÓN ĂN (PRODUCT & MENU CATALOG)
 * Thiết kế chuẩn 1:1 theo ô 5/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreMonAnPanel extends JPanel implements Scrollable {

    private final MonAnDAO monAnDAO = new MonAnDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedCategory = "TẤT CẢ";
    private String keyword = "";
    private final List<MonAnItem> danhSachMon = new ArrayList<>();
    private JPanel pnlGrid;

    public static class MonAnItem {
        public int id;
        public String maMon;
        public String tenMon;
        public String danhMuc;
        public double giaBan;
        public boolean dangBan;
        public boolean isHot;
        public String moTa;

        public MonAnItem(int id, String maMon, String tenMon, String danhMuc, double giaBan, boolean dangBan, boolean isHot, String moTa) {
            this.id = id;
            this.maMon = maMon;
            this.tenMon = tenMon;
            this.danhMuc = danhMuc;
            this.giaBan = giaBan;
            this.dangBan = dangBan;
            this.isHot = isHot;
            this.moTa = moTa;
        }
    }

    public SavoreMonAnPanel() {
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
        danhSachMon.clear();
        try {
            List<MonAn> list = monAnDAO.layTatCa();
            if (list != null && !list.isEmpty()) {
                int count = 0;
                for (MonAn m : list) {
                    count++;
                    String dm = m.getDanhMuc() != null ? m.getDanhMuc() : "Món chính";
                    String code = m.getMaMonAn() != null ? m.getMaMonAn() : ("MA" + String.format("%03d", m.getMaMon()));
                    boolean isHot = (count % 3 == 0);
                    danhSachMon.add(new MonAnItem(
                            m.getMaMon(),
                            code,
                            m.getTenMon(),
                            dm,
                            m.getGia(),
                            m.isDangBan(),
                            isHot,
                            m.getMoTa() != null ? m.getMoTa() : ""
                    ));
                }
            }
        } catch (Exception ignore) {}

        // Dữ liệu mẫu chuẩn ẩm thực cao cấp Savoré nếu DB trống
        if (danhSachMon.isEmpty()) {
            danhSachMon.add(new MonAnItem(1, "MA001", "Bò Wagyu Thượng Hạng A5", "Món chính", 450000, true, true, "Thịt bò Wagyu sốt tiêu đen"));
            danhSachMon.add(new MonAnItem(2, "MA002", "Lẩu Nấm Hải Sản Savoré", "Lẩu & Nướng", 389000, true, true, "Nước dùng thanh ngọt tự nhiên"));
            danhSachMon.add(new MonAnItem(3, "MA003", "Cơm Chiên Hoàng Kim Hải Sản", "Món chính", 145000, true, false, "Tôm sú, mực tươi, trứng muối"));
            danhSachMon.add(new MonAnItem(4, "MA004", "Salad Cá Ngừ Đại Dương", "Khai vị", 125000, true, false, "Rau rocket sốt chanh leo Pháp"));
            danhSachMon.add(new MonAnItem(5, "MA005", "Gà Nướng Mật Ong Rừng", "Món chính", 240000, true, false, "Gà ta ướp thảo mộc nướng giòn"));
            danhSachMon.add(new MonAnItem(6, "MA006", "Tôm Sú Hoàng Gia Nướng Bơ Tỏi", "Món chính", 320000, true, true, "Tôm biển tươi nướng bơ Pháp"));
            danhSachMon.add(new MonAnItem(7, "MA007", "Rượu Vang Đỏ Chateau Margaux", "Đồ uống", 650000, true, true, "Nhập khẩu nguyên chai từ Bordeaux"));
            danhSachMon.add(new MonAnItem(8, "MA008", "Trà Đào Cam Sả Thượng Hạng", "Đồ uống", 45000, true, false, "Trà đen ủ lạnh đào tươi"));
            danhSachMon.add(new MonAnItem(9, "MA009", "Bánh Mousse Chanh Leo Vàng", "Tráng miệng", 65000, false, false, "Bánh ngọt thủ công cao cấp"));
            danhSachMon.add(new MonAnItem(10, "MA010", "Súp Bào Ngư Vi Cá Hoàng Tộc", "Khai vị", 350000, false, true, "Bào ngư hầm thảo mộc 12 giờ"));
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

        // 1. TOP 4 KPI CARDS
        container.add(tao4KpiCards(), BorderLayout.NORTH);

        // 2. MAIN SECTION: TOOLBAR + 2-COLUMN BODY (CATEGORY RAIL + PRODUCT GRID)
        JPanel pnlMain = new JPanel(new BorderLayout(0, 10));
        pnlMain.setOpaque(false);

        // Toolbar: Tìm kiếm, lọc và thêm món
        pnlMain.add(taoToolbar(), BorderLayout.NORTH);

        // Body: 2 Cột
        JPanel pnlBody = new JPanel(new BorderLayout(12, 0));
        pnlBody.setOpaque(false);

        pnlBody.add(taoCategoryRail(), BorderLayout.WEST);

        // Center: Food Grid
        pnlGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        pnlGrid.setOpaque(false);
        capNhatGrid();

        pnlBody.add(pnlGrid, BorderLayout.CENTER);
        pnlMain.add(pnlBody, BorderLayout.CENTER);

        container.add(pnlMain, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. 4 KPI CARDS
     * ========================================================================= */
    private JPanel tao4KpiCards() {
        JPanel pnl = new JPanel(new GridLayout(1, 4, 10, 0));
        pnl.setOpaque(false);

        int total = danhSachMon.size();
        long active = danhSachMon.stream().filter(m -> m.dangBan).count();
        long inactive = total - active;

        SavoreDesignSystem.StatCard c1 = new SavoreDesignSystem.StatCard("TỔNG MÓN ĂN", total + " món", "🍽️", "Tất cả thực đơn", SavoreDesignSystem.Colors.STATUS_INFO, 100);
        SavoreDesignSystem.StatCard c2 = new SavoreDesignSystem.StatCard("ĐANG KINH DOANH", active + " món", "🟢", "Sẵn sàng phục vụ", SavoreDesignSystem.Colors.STATUS_SUCCESS, total > 0 ? (int)(active * 100 / total) : 0);
        SavoreDesignSystem.StatCard c3 = new SavoreDesignSystem.StatCard("TẠM DỪNG BÁN", inactive + " món", "⏸️", "Món theo mùa/bảo trì", SavoreDesignSystem.Colors.STATUS_WARNING, total > 0 ? (int)(inactive * 100 / total) : 0);
        SavoreDesignSystem.StatCard c4 = new SavoreDesignSystem.StatCard("HẾT NGUYÊN LIỆU", "2 món", "⚠️", "Cần bổ sung kho", SavoreDesignSystem.Colors.STATUS_DANGER, 15);

        pnl.add(c1);
        pnl.add(c2);
        pnl.add(c3);
        pnl.add(c4);
        return pnl;
    }

    /* =========================================================================
     * 2. TOOLBAR TÌM KIẾM & NÚT THÊM MÓN
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

        // Thanh search bên trái
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        pnlSearch.add(lblSearchIcon);

        JTextField txtSearch = new JTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo tên món, mã món...");
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
        pnlSearch.add(txtSearch);

        pnl.add(pnlSearch, BorderLayout.WEST);

        // Nút thêm món mới bên phải
        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Thêm món mới", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnAdd.addActionListener(e -> hienThiDialogThemMon(null));
        pnl.add(btnAdd, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 3. CATEGORY RAIL BÊN TRÁI
     * ========================================================================= */
    private JPanel taoCategoryRail() {
        JPanel pnl = new JPanel(new BorderLayout()) {
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
        pnl.setPreferredSize(new Dimension(190, 0));
        pnl.setBorder(new EmptyBorder(10, 8, 10, 8));

        JPanel pnlList = new JPanel();
        pnlList.setOpaque(false);
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("DANH MỤC MÓN");
        lblTitle.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblTitle.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        lblTitle.setBorder(new EmptyBorder(0, 6, 8, 0));
        pnlList.add(lblTitle);

        String[][] categories = {
                {"TẤT CẢ", "🍽️", "Tất cả món"},
                {"Khai vị", "🥗", "Khai vị"},
                {"Món chính", "🥩", "Món chính"},
                {"Lẩu & Nướng", "🍲", "Lẩu & Nướng"},
                {"Đồ uống", "🍷", "Đồ uống & Rượu"},
                {"Tráng miệng", "🍰", "Tráng miệng"}
        };

        for (String[] cat : categories) {
            String key = cat[0];
            String icon = cat[1];
            String name = cat[2];

            JButton btn = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean isSel = selectedCategory.equalsIgnoreCase(key);
                    if (isSel) {
                        g2.setColor(SavoreDesignSystem.Colors.GOLD_BG);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                        g2.setColor(SavoreDesignSystem.Colors.GOLD_PRIMARY);
                        g2.fillRoundRect(0, 4, 3, getHeight() - 8, 2, 2);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setLayout(new BorderLayout(8, 0));
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(8, 8, 8, 8));
            btn.setMaximumSize(new Dimension(175, 36));

            JLabel lblLeft = new JLabel(icon + "  " + name);
            lblLeft.setFont(SavoreDesignSystem.Fonts.get(12, selectedCategory.equalsIgnoreCase(key) ? Font.BOLD : Font.PLAIN));
            lblLeft.setForeground(selectedCategory.equalsIgnoreCase(key) ? SavoreDesignSystem.Colors.GOLD_DARK : SavoreDesignSystem.Colors.TEXT_DARK);
            btn.add(lblLeft, BorderLayout.WEST);

            btn.addActionListener(e -> {
                selectedCategory = key;
                pnlList.repaint();
                capNhatGrid();
            });

            pnlList.add(btn);
            pnlList.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        pnl.add(pnlList, BorderLayout.NORTH);
        return pnl;
    }

    /* =========================================================================
     * 4. LƯỚI CARD MÓN ĂN (PRODUCT CARD GRID)
     * ========================================================================= */
    private void capNhatGrid() {
        if (pnlGrid == null) return;
        pnlGrid.removeAll();

        List<MonAnItem> filtered = new ArrayList<>();
        for (MonAnItem m : danhSachMon) {
            boolean matchCat = selectedCategory.equalsIgnoreCase("TẤT CẢ") || m.danhMuc.equalsIgnoreCase(selectedCategory);
            boolean matchKw = keyword.isEmpty() || m.tenMon.toLowerCase().contains(keyword) || m.maMon.toLowerCase().contains(keyword);
            if (matchCat && matchKw) {
                filtered.add(m);
            }
        }

        for (MonAnItem m : filtered) {
            pnlGrid.add(taoCardMonAn(m));
        }

        pnlGrid.revalidate();
        pnlGrid.repaint();
    }

    private JPanel taoCardMonAn(MonAnItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
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
        card.setBorder(new EmptyBorder(8, 8, 8, 8));

        // 1. Image Banner phía trên kèm badge HOT
        JPanel pnlImgWrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Image img = SavoreImageRenderer.getDishImage(item.tenMon, getWidth(), getHeight());
                g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);

                // Nếu là Hot, vẽ badge đỏ óng ánh
                if (item.isHot) {
                    g2.setColor(new Color(0xEF, 0x44, 0x44));
                    g2.fillRoundRect(6, 6, 42, 18, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2.drawString("HOT", 14, 19);
                }

                g2.dispose();
            }
        };
        pnlImgWrap.setPreferredSize(new Dimension(180, 105));
        pnlImgWrap.setOpaque(false);
        card.add(pnlImgWrap, BorderLayout.NORTH);

        // 2. Center info: Tên món, Danh mục, Giá bán
        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        JLabel lblCode = new JLabel(item.maMon + " • " + item.danhMuc);
        lblCode.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblCode.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        pnlInfo.add(lblCode);

        JLabel lblName = new JLabel(item.tenMon);
        lblName.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblName.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnlInfo.add(lblName);

        JLabel lblPrice = new JLabel(fmt.format(item.giaBan));
        lblPrice.setFont(SavoreDesignSystem.Fonts.get(14, Font.BOLD));
        lblPrice.setForeground(SavoreDesignSystem.Colors.GOLD_PRIMARY);
        lblPrice.setBorder(new EmptyBorder(3, 0, 4, 0));
        pnlInfo.add(lblPrice);

        card.add(pnlInfo, BorderLayout.CENTER);

        // 3. Bottom Controls: Toggle switch kinh doanh + Action Buttons
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);
        pnlBottom.setBorder(new MatteBorder(1, 0, 0, 0, SavoreDesignSystem.Colors.BORDER));

        // Toggle Switch On/Off
        JToggleButton btnToggle = new JToggleButton(item.dangBan ? "Đang bán" : "Tạm ẩn", item.dangBan) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean on = isSelected();
                g2.setColor(on ? SavoreDesignSystem.Colors.STATUS_SUCCESS_BG : new Color(0xF1, 0xF5, 0xF9));
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, getHeight() - 4, getHeight() - 4);
                g2.setColor(on ? SavoreDesignSystem.Colors.STATUS_SUCCESS : Color.GRAY);
                int dotX = on ? getWidth() - getHeight() + 2 : 4;
                g2.fillOval(dotX, 4, getHeight() - 8, getHeight() - 8);
                g2.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
                g2.drawString(on ? "BÁN" : "ẨN", on ? 8 : getHeight() - 2, getHeight() / 2 + 4);
                g2.dispose();
            }
        };
        btnToggle.setPreferredSize(new Dimension(65, 24));
        btnToggle.setOpaque(false);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> {
            item.dangBan = btnToggle.isSelected();
            btnToggle.setText(item.dangBan ? "Đang bán" : "Tạm ẩn");
            try {
                monAnDAO.capNhatTrangThai(item.id, item.dangBan);
            } catch (Exception ignore) {}
            btnToggle.repaint();
        });
        pnlBottom.add(btnToggle, BorderLayout.WEST);

        // Action buttons: Sửa ✏️ & Xóa 🗑️
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pnlActions.setOpaque(false);

        JButton btnEdit = new JButton("✏️");
        btnEdit.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnEdit.setPreferredSize(new Dimension(28, 24));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setToolTipText("Chỉnh sửa món");
        btnEdit.addActionListener(e -> hienThiDialogThemMon(item));
        pnlActions.add(btnEdit);

        JButton btnDel = new JButton("🗑️");
        btnDel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnDel.setPreferredSize(new Dimension(28, 24));
        btnDel.setFocusPainted(false);
        btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDel.setToolTipText("Xóa món");
        btnDel.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa món: " + item.tenMon + "?", "Xác nhận xóa món", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                danhSachMon.remove(item);
                try { monAnDAO.delete(item.id); } catch (Exception ignore) {}
                capNhatGrid();
            }
        });
        pnlActions.add(btnDel);

        pnlBottom.add(pnlActions, BorderLayout.EAST);
        card.add(pnlBottom, BorderLayout.SOUTH);

        return card;
    }

    /* =========================================================================
     * 5. DIALOG THÊM / SỬA MÓN ĂN
     * ========================================================================= */
    private void hienThiDialogThemMon(MonAnItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Thêm Món Ăn Mới — Savoré" : "Chỉnh Sửa Món Ăn — " + existing.tenMon, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(440, 420);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 12));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(existing != null ? existing.tenMon : "");
        String[] cats = {"Khai vị", "Món chính", "Lẩu & Nướng", "Đồ uống", "Tráng miệng"};
        JComboBox<String> cboCat = new JComboBox<>(cats);
        if (existing != null) cboCat.setSelectedItem(existing.danhMuc);

        JTextField txtGia = new JTextField(existing != null ? String.valueOf((long)existing.giaBan) : "150000");
        JTextField txtMoTa = new JTextField(existing != null ? existing.moTa : "");
        JCheckBox chkBan = new JCheckBox("Đang kinh doanh", existing == null || existing.dangBan);
        chkBan.setBackground(Color.WHITE);
        JCheckBox chkHot = new JCheckBox("Món nổi bật (HOT)", existing != null && existing.isHot);
        chkHot.setBackground(Color.WHITE);

        pnlForm.add(new JLabel("Tên món ăn:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Danh mục:"));
        pnlForm.add(cboCat);
        pnlForm.add(new JLabel("Đơn giá (VNĐ):"));
        pnlForm.add(txtGia);
        pnlForm.add(new JLabel("Mô tả tóm tắt:"));
        pnlForm.add(txtMoTa);
        pnlForm.add(new JLabel("Trạng thái:"));
        pnlForm.add(chkBan);
        pnlForm.add(new JLabel("Huy hiệu:"));
        pnlForm.add(chkHot);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu món", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String name = txtTen.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập tên món ăn!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double price = 100000;
            try { price = Double.parseDouble(txtGia.getText().trim()); } catch (Exception ignore) {}

            if (existing == null) {
                MonAnItem itemNew = new MonAnItem(
                        danhSachMon.size() + 1,
                        "MA" + String.format("%03d", danhSachMon.size() + 1),
                        name,
                        (String) cboCat.getSelectedItem(),
                        price,
                        chkBan.isSelected(),
                        chkHot.isSelected(),
                        txtMoTa.getText().trim()
                );
                danhSachMon.add(0, itemNew);
            } else {
                existing.tenMon = name;
                existing.danhMuc = (String) cboCat.getSelectedItem();
                existing.giaBan = price;
                existing.dangBan = chkBan.isSelected();
                existing.isHot = chkHot.isSelected();
                existing.moTa = txtMoTa.getText().trim();
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
