package com.restaurant.view;

import com.restaurant.dao.KhuyenMaiDAO;
import com.restaurant.model.KhuyenMai;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 11/12 — CHIẾN DỊCH KHUYẾN MÃI & VOUCHER (CAMPAIGN OPERATIONS)
 * Thiết kế chuẩn 1:1 theo ô 11/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreKhuyenMaiPanel extends JPanel implements Scrollable {

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private String selectedTab = "TẤT CẢ";
    private final List<KhuyenMaiItem> danhSachKm = new ArrayList<>();
    private JPanel pnlCardsContainer;

    public static class KhuyenMaiItem {
        public int id;
        public String maCode;
        public String tieuDe;
        public String loaiGiam; // "PHAN_TRAM", "TIEN_MAT"
        public double giaTriGiam;
        public double donToiThieu;
        public String ngayBatDau;
        public String ngayKetThuc;
        public String trangThai; // "ĐANG DIỄN RA", "SẮP DIỄN RA", "ĐÃ KẾT THÚC"
        public String dieuKien;

        public KhuyenMaiItem(int id, String maCode, String tieuDe, String loaiGiam, double giaTriGiam, double donToiThieu, String ngayBatDau, String ngayKetThuc, String trangThai, String dieuKien) {
            this.id = id;
            this.maCode = maCode;
            this.tieuDe = tieuDe;
            this.loaiGiam = loaiGiam;
            this.giaTriGiam = giaTriGiam;
            this.donToiThieu = donToiThieu;
            this.ngayBatDau = ngayBatDau;
            this.ngayKetThuc = ngayKetThuc;
            this.trangThai = trangThai;
            this.dieuKien = dieuKien;
        }

        public String getDiscountDisplay() {
            if ("PHAN_TRAM".equalsIgnoreCase(loaiGiam) || giaTriGiam <= 100) {
                return "GIẢM " + (int) giaTriGiam + "%";
            }
            DecimalFormat f = new DecimalFormat("#,### đ");
            return "GIẢM " + f.format(giaTriGiam);
        }
    }

    public SavoreKhuyenMaiPanel() {
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
        danhSachKm.clear();
        try {
            List<KhuyenMai> list = khuyenMaiDAO.findAll();
            if (list != null && !list.isEmpty()) {
                for (KhuyenMai k : list) {
                    String start = (k.getNgayBatDau() != null) ? df.format(k.getNgayBatDau()) : "01/09/2026";
                    String end = (k.getNgayKetThuc() != null) ? df.format(k.getNgayKetThuc()) : "30/09/2026";
                    danhSachKm.add(new KhuyenMaiItem(
                            k.getMaKhuyenMai(),
                            k.getMaCode(),
                            k.getTenKhuyenMai(),
                            k.getLoaiGiam(),
                            k.getGiaTriGiam(),
                            k.getDonHangToiThieu(),
                            start,
                            end,
                            "ĐANG DIỄN RA",
                            "Áp dụng cho hóa đơn từ " + fmt.format(k.getDonHangToiThieu())
                    ));
                }
            }
        } catch (Exception ignore) {}

        if (danhSachKm.isEmpty()) {
            danhSachKm.add(new KhuyenMaiItem(1, "SAVORE20", "Đại Tiệc Lẩu Nấm & Bò Wagyu", "PHAN_TRAM", 20, 500000, "01/09/2026", "30/09/2026", "ĐANG DIỄN RA", "Áp dụng hóa đơn từ 500k toàn hệ thống"));
            danhSachKm.add(new KhuyenMaiItem(2, "HAPPY30", "Happy Hour Trà Chiều & Rượu Vang", "PHAN_TRAM", 30, 200000, "05/09/2026", "25/09/2026", "ĐANG DIỄN RA", "Khung giờ vàng 14:00 - 17:00 mỗi ngày"));
            danhSachKm.add(new KhuyenMaiItem(3, "VIPGOLD100", "Tri Ân Khách Hàng VIP Tháng 9", "TIEN_MAT", 100000, 800000, "01/09/2026", "15/09/2026", "ĐANG DIỄN RA", "Dành riêng cho khách hạng Vàng & Kim Cương"));
            danhSachKm.add(new KhuyenMaiItem(4, "WEEKEND15", "Combo Sum Vầy Cuối Tuần", "PHAN_TRAM", 15, 600000, "15/09/2026", "30/09/2026", "SẮP DIỄN RA", "Áp dụng vào Thứ Bảy & Chủ Nhật hàng tuần"));
            danhSachKm.add(new KhuyenMaiItem(5, "MOONCAKE", "Tặng Bánh Tráng Miệng Thượng Hạng", "TIEN_MAT", 50000, 350000, "20/09/2026", "05/10/2026", "SẮP DIỄN RA", "Tặng kèm 01 phần bánh Mousse cho bàn từ 3 khách"));
            danhSachKm.add(new KhuyenMaiItem(6, "SUMMEREND", "Chào Hè Sảng Khoái Tươi Mát", "PHAN_TRAM", 25, 400000, "01/08/2026", "31/08/2026", "ĐÃ KẾT THÚC", "Chương trình đã kết thúc"));
        }
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

        // 1. TOP TABS & BUTTON THEO ẢNH 11/12
        container.add(taoTopTabs(), BorderLayout.NORTH);

        // 2. MAIN GRID CAMPAIGN CARDS
        pnlCardsContainer = new JPanel(new GridLayout(0, 3, 14, 14));
        pnlCardsContainer.setOpaque(false);
        capNhatCards();

        container.add(pnlCardsContainer, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. TOP TABS & NÚT TẠO CHIẾN DỊCH
     * ========================================================================= */
    private JPanel taoTopTabs() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setOpaque(false);

        JPanel pnlTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTabs.setOpaque(false);

        String[] tabs = {"TẤT CẢ", "ĐANG DIỄN RA", "SẮP DIỄN RA", "ĐÃ KẾT THÚC"};
        for (String tab : tabs) {
            long count = tab.equals("TẤT CẢ") ? danhSachKm.size() : danhSachKm.stream().filter(k -> k.trangThai.equalsIgnoreCase(tab)).count();
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
                capNhatCards();
            });

            pnlTabs.add(btn);
        }

        pnl.add(pnlTabs, BorderLayout.WEST);

        // Nút Tạo khuyến mãi bên phải
        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Tạo chương trình mới", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnAdd.addActionListener(e -> hienThiDialogThemKm(null));
        pnl.add(btnAdd, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 2. LƯỚI CARD CHIẾN DỊCH KHUYẾN MÃI
     * ========================================================================= */
    private void capNhatCards() {
        if (pnlCardsContainer == null) return;
        pnlCardsContainer.removeAll();

        for (KhuyenMaiItem km : danhSachKm) {
            if (selectedTab.equalsIgnoreCase("TẤT CẢ") || km.trangThai.equalsIgnoreCase(selectedTab)) {
                pnlCardsContainer.add(taoCardKhuyenMai(km));
            }
        }

        pnlCardsContainer.revalidate();
        pnlCardsContainer.repaint();
    }

    private JPanel taoCardKhuyenMai(KhuyenMaiItem km) {
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
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        // 1. BANNER LUXURY GRADIENT + DISCOUNT BADGE
        JPanel pnlBanner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, new Color(0x1F, 0x27, 0x33), getWidth(), getHeight(), new Color(0x3B, 0x2A, 0x1E));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Điểm nhấn vàng
                g2.setColor(new Color(0xD4, 0xA3, 0x59, 50));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                // Badge Giảm giá
                g2.setColor(new Color(0xEF, 0x44, 0x44));
                g2.fillRoundRect(8, 8, 100, 24, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(km.getDiscountDisplay(), 14, 25);

                g2.dispose();
            }
        };
        pnlBanner.setPreferredSize(new Dimension(200, 60));
        pnlBanner.setOpaque(false);
        card.add(pnlBanner, BorderLayout.NORTH);

        // 2. CENTER CONTENT
        JPanel pnlCenter = new JPanel();
        pnlCenter.setOpaque(false);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(km.tieuDe);
        lblTitle.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblTitle.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnlCenter.add(lblTitle);

        // Code box
        JPanel pnlCode = new JPanel(new BorderLayout(8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xFA, 0xF7, 0xF2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                // Viền nét đứt
                Stroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
                g2.setStroke(dashed);
                g2.setColor(SavoreDesignSystem.Colors.BORDER_GOLD);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnlCode.setOpaque(false);
        pnlCode.setBorder(new EmptyBorder(6, 10, 6, 8));

        JLabel lblCode = new JLabel("CODE:  " + km.maCode);
        lblCode.setFont(SavoreDesignSystem.Fonts.mono(12, Font.BOLD));
        lblCode.setForeground(SavoreDesignSystem.Colors.GOLD_PRIMARY);
        pnlCode.add(lblCode, BorderLayout.WEST);

        JButton btnCopy = new JButton("Copy");
        btnCopy.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        btnCopy.setPreferredSize(new Dimension(50, 20));
        btnCopy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCopy.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(km.maCode), null);
            JOptionPane.showMessageDialog(this, "Đã sao chép mã [" + km.maCode + "] vào Clipboard!", "Sao Chép Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlCode.add(btnCopy, BorderLayout.EAST);

        pnlCenter.add(Box.createRigidArea(new Dimension(0, 6)));
        pnlCenter.add(pnlCode);

        // Điều kiện áp dụng
        JLabel lblCond = new JLabel("• " + km.dieuKien);
        lblCond.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblCond.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        lblCond.setBorder(new EmptyBorder(6, 0, 4, 0));
        pnlCenter.add(lblCond);

        // Thời gian hiệu lực
        JLabel lblTime = new JLabel("📅 " + km.ngayBatDau + " — " + km.ngayKetThuc);
        lblTime.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblTime.setForeground(SavoreDesignSystem.Colors.TEXT_SUB);
        pnlCenter.add(lblTime);

        // Chip trạng thái
        JPanel pnlBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        pnlBadge.setOpaque(false);
        boolean active = km.trangThai.equals("ĐANG DIỄN RA");
        Color fg = active ? SavoreDesignSystem.Colors.STATUS_SUCCESS : (km.trangThai.equals("SẮP DIỄN RA") ? SavoreDesignSystem.Colors.STATUS_WARNING : Color.GRAY);
        Color bg = active ? SavoreDesignSystem.Colors.STATUS_SUCCESS_BG : (km.trangThai.equals("SẮP DIỄN RA") ? SavoreDesignSystem.Colors.STATUS_WARNING_BG : new Color(0xF1, 0xF5, 0xF9));
        pnlBadge.add(new SavoreDesignSystem.StatusBadge(km.trangThai, fg, bg));
        pnlCenter.add(pnlBadge);

        card.add(pnlCenter, BorderLayout.CENTER);

        // 3. BOTTOM ACTIONS
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pnlActions.setOpaque(false);
        pnlActions.setBorder(new MatteBorder(1, 0, 0, 0, SavoreDesignSystem.Colors.BORDER));

        SavoreDesignSystem.ModernButton btnEdit = new SavoreDesignSystem.ModernButton("✏️ Chỉnh sửa", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnEdit.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        btnEdit.addActionListener(e -> hienThiDialogThemKm(km));
        pnlActions.add(btnEdit);

        card.add(pnlActions, BorderLayout.SOUTH);
        return card;
    }

    /* =========================================================================
     * 3. DIALOG TẠO / SỬA KHUYẾN MÃI
     * ========================================================================= */
    private void hienThiDialogThemKm(KhuyenMaiItem existing) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), existing == null ? "Tạo Chiến Dịch Khuyến Mãi — Savoré" : "Chỉnh Sửa Chiến Dịch — " + existing.tieuDe, true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(440, 440);
        dlg.setLocationRelativeTo(this);

        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 10, 10));
        pnlForm.setBorder(new EmptyBorder(16, 20, 16, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtCode = new JTextField(existing != null ? existing.maCode : "SAVORE10");
        JTextField txtTieuDe = new JTextField(existing != null ? existing.tieuDe : "");
        String[] loais = {"PHAN_TRAM", "TIEN_MAT"};
        JComboBox<String> cboLoai = new JComboBox<>(loais);
        if (existing != null) cboLoai.setSelectedItem(existing.loaiGiam);

        JTextField txtGiaTri = new JTextField(existing != null ? String.valueOf((long)existing.giaTriGiam) : "10");
        JTextField txtMin = new JTextField(existing != null ? String.valueOf((long)existing.donToiThieu) : "300000");
        JTextField txtStart = new JTextField(existing != null ? existing.ngayBatDau : "01/09/2026");
        JTextField txtEnd = new JTextField(existing != null ? existing.ngayKetThuc : "30/09/2026");

        pnlForm.add(new JLabel("Mã Voucher (Code):"));
        pnlForm.add(txtCode);
        pnlForm.add(new JLabel("Tên chiến dịch:"));
        pnlForm.add(txtTieuDe);
        pnlForm.add(new JLabel("Loại giảm giá:"));
        pnlForm.add(cboLoai);
        pnlForm.add(new JLabel("Giá trị giảm (% hoặc VNĐ):"));
        pnlForm.add(txtGiaTri);
        pnlForm.add(new JLabel("Đơn hàng tối thiểu (VNĐ):"));
        pnlForm.add(txtMin);
        pnlForm.add(new JLabel("Ngày bắt đầu (dd/MM/yyyy):"));
        pnlForm.add(txtStart);
        pnlForm.add(new JLabel("Ngày kết thúc (dd/MM/yyyy):"));
        pnlForm.add(txtEnd);

        dlg.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlBtn.setBackground(new Color(0xF8, 0xF5, 0xF0));

        SavoreDesignSystem.ModernButton btnCancel = new SavoreDesignSystem.ModernButton("Đóng", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnCancel.addActionListener(e -> dlg.dispose());

        SavoreDesignSystem.ModernButton btnSave = new SavoreDesignSystem.ModernButton("Lưu Chiến Dịch", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnSave.addActionListener(e -> {
            String code = txtCode.getText().trim();
            String title = txtTieuDe.getText().trim();
            if (code.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập mã voucher và tên chiến dịch!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double val = 10;
            double min = 0;
            try {
                val = Double.parseDouble(txtGiaTri.getText().trim());
                min = Double.parseDouble(txtMin.getText().trim());
            } catch (Exception ignore) {}

            if (existing == null) {
                KhuyenMai km = new KhuyenMai();
                km.setMaCode(code);
                km.setTenKhuyenMai(title);
                km.setLoaiGiam((String) cboLoai.getSelectedItem());
                km.setGiaTriGiam(val);
                km.setDonHangToiThieu(min);
                try {
                    km.setNgayBatDau(new java.sql.Date(df.parse(txtStart.getText().trim()).getTime()));
                    km.setNgayKetThuc(new java.sql.Date(df.parse(txtEnd.getText().trim()).getTime()));
                } catch (Exception ignore) {}
                try { khuyenMaiDAO.insert(km); } catch (Exception ignore) {}

                KhuyenMaiItem itemNew = new KhuyenMaiItem(
                        danhSachKm.size() + 1,
                        code,
                        title,
                        (String) cboLoai.getSelectedItem(),
                        val,
                        min,
                        txtStart.getText().trim(),
                        txtEnd.getText().trim(),
                        "ĐANG DIỄN RA",
                        "Áp dụng hóa đơn từ " + fmt.format(min)
                );
                danhSachKm.add(0, itemNew);
            } else {
                existing.maCode = code;
                existing.tieuDe = title;
                existing.loaiGiam = (String) cboLoai.getSelectedItem();
                existing.giaTriGiam = val;
                existing.donToiThieu = min;
                existing.ngayBatDau = txtStart.getText().trim();
                existing.ngayKetThuc = txtEnd.getText().trim();
                existing.dieuKien = "Áp dụng hóa đơn từ " + fmt.format(min);
            }

            capNhatCards();
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
