package com.restaurant.view;

import com.restaurant.dao.BanAnDAO;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.model.BanAn;
import com.restaurant.model.DonHang;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 2/12 — ĐẶT BÀN & SƠ ĐỒ MẶT BẰNG (RESERVATION MANAGEMENT)
 * Thiết kế chuẩn 1:1 theo ô 2/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreDatBanPanel extends JPanel implements Scrollable {

    private final NguoiDung nguoiDung;
    private final Consumer<String> chuyenPhanHeCallback;
    private final BanAnDAO banAnDAO = new BanAnDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedFloor = "Tầng 1";
    private int selectedTableId = 2;
    private String selectedTableName = "Bàn 02";
    private String selectedTableStatus = "DANG_PHUC_VU";

    // Components thông tin bàn bên phải
    private JLabel lblTableTitle;
    private JLabel lblTableStatus;
    private JLabel lblTableGuests;
    private JLabel lblTableTime;
    private JLabel lblTableCust;
    private JLabel lblTableBill;
    private JLabel lblTableNote;

    private JPanel pnlFloorMapCanvas;

    public SavoreDatBanPanel() {
        this(null, null);
    }

    public SavoreDatBanPanel(NguoiDung nguoiDung, Consumer<String> chuyenPhanHeCallback) {
        this.nguoiDung = nguoiDung;
        this.chuyenPhanHeCallback = chuyenPhanHeCallback;

        setLayout(new BorderLayout());
        setOpaque(false);

        // Bọc trong ScrollPane chống tràn ngang
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

        // 1. TOP TOOLBAR THEO ẢNH 2/12
        container.add(taoTopToolbar(), BorderLayout.NORTH);

        // 2. MAIN BODY (SƠ ĐỒ BÀN 65% + THÔNG TIN BÀN 35%)
        JPanel pnlBody = new JPanel(new GridBagLayout());
        pnlBody.setOpaque(false);
        pnlBody.setPreferredSize(new Dimension(800, 580));
        pnlBody.setMinimumSize(new Dimension(500, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Trái: Sơ đồ mặt bằng 2D
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 10);
        pnlBody.add(taoSơDoPanel(), gbc);

        // Phải: Thẻ thông tin bàn
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlBody.add(taoThongTinBanCard(), gbc);

        container.add(pnlBody, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. TOP TOOLBAR: NGÀY | BỘ LỌC KHU VỰC | BỘ LỌC GIỜ | NÚT + ĐẶT BÀN NHANH
     * ========================================================================= */
    private JPanel taoTopToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(500, 42));

        JPanel pnlFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlFilters.setOpaque(false);

        // Ô chọn ngày bo tròn
        JPanel pnlDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6)) {
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
        pnlDate.setOpaque(false);
        pnlDate.setPreferredSize(new Dimension(130, 36));
        JLabel lblCal = new JLabel("📅");
        lblCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        JLabel lblDateVal = new JLabel("08/09/2026");
        lblDateVal.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblDateVal.setForeground(new Color(0x1F, 0x27, 0x33));
        pnlDate.add(lblCal);
        pnlDate.add(lblDateVal);
        pnlFilters.add(pnlDate);

        // Dropdown Tất cả ca
        pnlFilters.add(taoDropdownPill("Tất cả ca  ▾", 100));
        // Dropdown Tất cả giờ
        pnlFilters.add(taoDropdownPill("Tất cả giờ  ▾", 100));
        // Dropdown Tất cả khu vực
        pnlFilters.add(taoDropdownPill("Tất cả khu vực  ▾", 125));

        bar.add(pnlFilters, BorderLayout.WEST);

        // Nút + Đặt bàn nhanh (Gold prominent)
        JButton btnAddBooking = new JButton("+ Đặt bàn nhanh") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD4, 0xA3, 0x59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAddBooking.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAddBooking.setForeground(new Color(0x1F, 0x27, 0x33));
        btnAddBooking.setContentAreaFilled(false);
        btnAddBooking.setBorderPainted(false);
        btnAddBooking.setFocusPainted(false);
        btnAddBooking.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddBooking.setPreferredSize(new Dimension(135, 36));
        btnAddBooking.addActionListener(e -> moDialogDatBanNhanh());
        bar.add(btnAddBooking, BorderLayout.EAST);

        return bar;
    }

    private JPanel taoDropdownPill(String text, int width) {
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 8)) {
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
        pill.setOpaque(false);
        pill.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pill.setPreferredSize(new Dimension(width, 36));

        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l.setForeground(new Color(0x64, 0x74, 0x8B));
        pill.add(l);
        return pill;
    }

    /* =========================================================================
     * 2. SƠ ĐỒ MẶT BẰNG KHU VỰC & BÀN (FLOOR MAP CANVAS)
     * ========================================================================= */
    private JPanel taoSơDoPanel() {
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
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Top Floor Tabs
        JPanel pnlTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlTabs.setOpaque(false);

        String[] floors = {"Tầng 1", "Tầng 2 (VIP)", "Phòng riêng", "Khu ngoài trời"};
        for (String f : floors) {
            JButton b = new JButton(f) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean sel = f.equals(selectedFloor);
                    g2.setColor(sel ? new Color(0x1E, 0x29, 0x3B) : new Color(0xF1, 0xF5, 0xF9));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    setForeground(sel ? Color.WHITE : new Color(0x64, 0x74, 0x8B));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            b.setFont(new Font("Segoe UI", Font.BOLD, 9));
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(new EmptyBorder(4, 10, 4, 10));
            b.addActionListener(e -> {
                selectedFloor = f;
                pnlTabs.repaint();
                pnlFloorMapCanvas.repaint();
            });
            pnlTabs.add(b);
        }
        card.add(pnlTabs, BorderLayout.NORTH);

        // Canvas vẽ mặt bằng nhà hàng
        pnlFloorMapCanvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Sàn gỗ màu ấm sang trọng
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x2B, 0x24, 0x1E), w, h, new Color(0x3B, 0x32, 0x28));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 8, 8);

                // Đường viền ngăn cách khu vực
                int inW = (int)(w * 0.70);
                g2.setColor(new Color(0x4A, 0x40, 0x34));
                g2.fillRoundRect(4, 4, inW, h - 8, 6, 6);

                // Quầy Bar
                g2.setColor(new Color(0x1F, 0x1A, 0x15));
                g2.fillRoundRect(10, 24, 28, h - 48, 4, 4);
                g2.setColor(new Color(0xD4, 0xA3, 0x59));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                g2.drawString("B", 18, 56);
                g2.drawString("A", 18, 74);
                g2.drawString("R", 18, 92);

                // Khu ngoài trời
                int outX = inW + 8;
                int outW = w - outX - 4;
                g2.setColor(new Color(0x4B, 0x55, 0x46));
                g2.fillRoundRect(outX, 4, outW, h - 8, 6, 6);
                g2.setColor(new Color(0xDC, 0xFC, 0xE7));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
                g2.drawString("NGOÀI TRỜI", outX + 8, 18);

                // Cây xanh trang trí
                g2.setColor(new Color(0x15, 0x80, 0x3D));
                g2.fillOval(outX + 6, 30, 16, 16);
                g2.fillOval(w - 24, 30, 16, 16);
                g2.fillOval(outX + 6, h - 34, 16, 16);

                // Tọa độ tính toán động cho các bàn
                int stepX = (inW - 54) / 4;
                int r1Y = 36;
                int r2Y = 120;
                int bw = Math.min(42, stepX - 10);
                int bh = Math.min(42, stepX - 10);

                // Hàng 1 trong nhà
                veBanInteractive(g2, 48, r1Y, bw, bh, "01", 1, new Color(0xEE, 0xEE, 0xEE), Color.BLACK, false);
                veBanInteractive(g2, 48 + stepX, r1Y, bw + 2, bh + 2, "02", 2, new Color(0xEF, 0x44, 0x44), Color.WHITE, true);
                veBanInteractive(g2, 48 + stepX * 2, r1Y, bw, bh, "03", 3, new Color(0xEE, 0xEE, 0xEE), Color.BLACK, false);
                veBanInteractive(g2, 48 + stepX * 3, r1Y, bw, bh, "04", 4, new Color(0x10, 0xB9, 0x81), Color.WHITE, false);

                // Hàng 2 trong nhà
                veBanInteractive(g2, 48, r2Y, bw, bh, "05", 5, new Color(0xEE, 0xEE, 0xEE), Color.BLACK, false);
                veBanInteractive(g2, 48 + stepX, r2Y, bw, bh, "06", 6, new Color(0x10, 0xB9, 0x81), Color.WHITE, false);
                veBanInteractive(g2, 48 + stepX * 2, r2Y, bw, bh, "07", 7, new Color(0xEE, 0xEE, 0xEE), Color.BLACK, false);

                // Bàn tròn ngoài trời
                int rd = Math.min(40, outW / 2 - 8);
                veBanTron(g2, outX + 12, 40, rd, "08", 8, new Color(0x10, 0xB9, 0x81), Color.WHITE);
                veBanTron(g2, outX + 16 + rd, 40, rd, "09", 9, new Color(0xF5, 0x9E, 0x0B), Color.WHITE);
                veBanTron(g2, outX + 12 + rd / 2, 114, rd + 2, "10", 10, new Color(0x94, 0xA3, 0xB8), Color.BLACK);

                g2.dispose();
            }

            private void veBanInteractive(Graphics2D g2, int x, int y, int w, int h, String name, int id, Color c, Color tc, boolean glow) {
                // Ghế tựa 4 cạnh
                g2.setColor(new Color(0x78, 0x71, 0x6C));
                g2.fillRoundRect(x + 8, y - 5, w - 16, 5, 2, 2);
                g2.fillRoundRect(x + 8, y + h, w - 16, 5, 2, 2);
                g2.fillRoundRect(x - 5, y + 8, 5, h - 16, 2, 2);
                g2.fillRoundRect(x + w, y + 8, 5, h - 16, 2, 2);

                if (selectedTableId == id || glow) {
                    g2.setColor(new Color(0xEF, 0x44, 0x44, 110));
                    g2.fillRoundRect(x - 3, y - 3, w + 6, h + 6, 10, 10);
                }

                g2.setColor(c);
                g2.fillRoundRect(x, y, w, h, 6, 6);
                g2.setColor(tc);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(name, x + (w - fm.stringWidth(name)) / 2, y + (h - fm.getHeight()) / 2 + fm.getAscent());
            }

            private void veBanTron(Graphics2D g2, int x, int y, int d, String name, int id, Color c, Color tc) {
                if (selectedTableId == id) {
                    g2.setColor(new Color(0xD4, 0xA3, 0x59, 130));
                    g2.fillOval(x - 3, y - 3, d + 6, d + 6);
                }
                g2.setColor(c);
                g2.fillOval(x, y, d, d);
                g2.setColor(tc);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(name, x + (d - fm.stringWidth(name)) / 2, y + (d - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        pnlFloorMapCanvas.setOpaque(false);
        pnlFloorMapCanvas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlFloorMapCanvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                // Click chọn bàn tương tác
                chonBan(2, "Bàn 02", "DANG_PHUC_VU", "Trần Thị Mai", "00:45:12", "1.280.000 đ", "Không cay, ít muối");
                pnlFloorMapCanvas.repaint();
            }
        });
        card.add(pnlFloorMapCanvas, BorderLayout.CENTER);

        // Legend bar ở đáy
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 4));
        pnlLegend.setOpaque(false);
        pnlLegend.add(taoLegendItem(new Color(0xEE, 0xEE, 0xEE), "Trống"));
        pnlLegend.add(taoLegendItem(new Color(0xEF, 0x44, 0x44), "Đang sử dụng"));
        pnlLegend.add(taoLegendItem(new Color(0x10, 0xB9, 0x81), "Đặt trước"));
        pnlLegend.add(taoLegendItem(new Color(0xF5, 0x9E, 0x0B), "Cần dọn"));
        pnlLegend.add(taoLegendItem(new Color(0x94, 0xA3, 0xB8), "Tạm khóa"));
        card.add(pnlLegend, BorderLayout.SOUTH);

        return card;
    }

    private JPanel taoLegendItem(Color c, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dot.setForeground(c);
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        l.setForeground(new Color(0x64, 0x74, 0x8B));
        p.add(dot);
        p.add(l);
        return p;
    }

    /* =========================================================================
     * 3. RIGHT PANEL: THÔNG TIN BÀN ĐANG CHỌN (TABLE DETAILS CARD)
     * ========================================================================= */
    private JPanel taoThongTinBanCard() {
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
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Header: Thông tin bàn
        JPanel pnlHead = new JPanel(new BorderLayout());
        pnlHead.setOpaque(false);
        JLabel lblT = new JLabel("Thông tin bàn");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblT.setForeground(new Color(0x1F, 0x27, 0x33));
        pnlHead.add(lblT, BorderLayout.WEST);

        JLabel lblClose = new JLabel("✕");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblClose.setForeground(new Color(0x9E, 0x94, 0x8B));
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlHead.add(lblClose, BorderLayout.EAST);
        card.add(pnlHead, BorderLayout.NORTH);

        // Body
        JPanel pnlBody = new JPanel();
        pnlBody.setOpaque(false);
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));

        // Ảnh mô phỏng bàn tiệc rượu vang
        JPanel pnlPhoto = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage img = SavoreImageRenderer.getDiningTableBanner(getWidth(), getHeight());
                g.drawImage(img, 0, 0, null);
            }
        };
        pnlPhoto.setPreferredSize(new Dimension(160, 60));
        pnlPhoto.setMaximumSize(new Dimension(9999, 60));
        pnlPhoto.setOpaque(false);
        pnlBody.add(pnlPhoto);
        pnlBody.add(Box.createRigidArea(new Dimension(0, 8)));

        // Tiêu đề bàn + Pill Badge Đang sử dụng
        JPanel pnlTableHead = new JPanel(new BorderLayout());
        pnlTableHead.setOpaque(false);

        lblTableTitle = new JLabel("Bàn 02 - Tầng 1");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTableTitle.setForeground(new Color(0x1F, 0x27, 0x33));
        pnlTableHead.add(lblTableTitle, BorderLayout.WEST);

        lblTableStatus = new JLabel("Đang sử dụng");
        lblTableStatus.setFont(new Font("Segoe UI", Font.BOLD, 8));
        lblTableStatus.setForeground(Color.WHITE);
        lblTableStatus.setOpaque(true);
        lblTableStatus.setBackground(new Color(0xEF, 0x44, 0x44));
        lblTableStatus.setBorder(new EmptyBorder(2, 6, 2, 6));

        JPanel pnlBadgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBadgeWrap.setOpaque(false);
        pnlBadgeWrap.add(lblTableStatus);
        pnlTableHead.add(pnlBadgeWrap, BorderLayout.EAST);
        pnlBody.add(pnlTableHead);
        pnlBody.add(Box.createRigidArea(new Dimension(0, 6)));

        // Thông tin chi tiết
        lblTableGuests = taoRowDetail("👥", "Sức chứa:", "4 người");
        lblTableTime = taoRowDetail("⏱️", "Thời gian:", "00:45:12");
        lblTableCust = taoRowDetail("👤", "Khách hàng:", "Trần Thị Mai");
        lblTableBill = taoRowDetail("💰", "Tổng tiền:", "1.280.000 đ");
        lblTableBill.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTableBill.setForeground(new Color(0xB0, 0x82, 0x46));
        lblTableNote = taoRowDetail("📝", "Ghi chú:", "Không cay, ít muối");

        pnlBody.add(lblTableGuests.getParent());
        pnlBody.add(lblTableTime.getParent());
        pnlBody.add(lblTableCust.getParent());
        pnlBody.add(lblTableBill.getParent());
        pnlBody.add(lblTableNote.getParent());
        pnlBody.add(Box.createRigidArea(new Dimension(0, 10)));

        // 3 Nút hành động theo đúng ảnh 2/12:
        // [Xem chi tiết] (Navy) | [Chuyển bàn] (White outline) | [Thanh toán] (Gold prominent)
        JButton btnView = taoActionButton("Xem chi tiết", new Color(0x1E, 0x29, 0x3B), Color.WHITE, false);
        btnView.addActionListener(e -> {
            if (chuyenPhanHeCallback != null) chuyenPhanHeCallback.accept("POS");
        });

        JButton btnMove = taoActionButton("Chuyển bàn", Color.WHITE, new Color(0x1F, 0x27, 0x33), true);
        btnMove.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chọn bàn trống mục tiêu để chuyển Bàn 02."));

        JButton btnPay = taoActionButton("Thanh toán", new Color(0xD4, 0xA3, 0x59), new Color(0x1F, 0x27, 0x33), false);
        btnPay.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở thanh toán cho Bàn 02 - Tầng 1\nTổng cộng: 1.280.000 đ"));

        JPanel pnlRow1 = new JPanel(new GridLayout(1, 2, 6, 0));
        pnlRow1.setOpaque(false);
        pnlRow1.add(btnView);
        pnlRow1.add(btnMove);
        pnlBody.add(pnlRow1);
        pnlBody.add(Box.createRigidArea(new Dimension(0, 6)));

        btnPay.setPreferredSize(new Dimension(200, 30));
        btnPay.setMaximumSize(new Dimension(9999, 30));
        pnlBody.add(btnPay);

        card.add(pnlBody, BorderLayout.CENTER);
        return card;
    }

    private JLabel taoRowDetail(String icon, String label, String val) {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(9999, 18));

        JPanel pnlL = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlL.setOpaque(false);

        JLabel lblI = new JLabel(icon);
        lblI.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        lblI.setForeground(new Color(0x64, 0x74, 0x8B));
        pnlL.add(lblI);

        JLabel lblL = new JLabel(label);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblL.setForeground(new Color(0x64, 0x74, 0x8B));
        pnlL.add(lblL);

        JLabel lblV = new JLabel(val, SwingConstants.RIGHT);
        lblV.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblV.setForeground(new Color(0x1F, 0x27, 0x33));

        row.add(pnlL, BorderLayout.WEST);
        row.add(lblV, BorderLayout.EAST);
        return lblV;
    }

    private JButton taoActionButton(String text, Color bg, Color fg, boolean outline) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                if (outline) {
                    g2.setColor(new Color(0xD1, 0xD5, 0xDB));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 10));
        b.setForeground(fg);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(80, 28));
        return b;
    }

    private void chonBan(int id, String name, String status, String cust, String time, String bill, String note) {
        this.selectedTableId = id;
        this.selectedTableName = name;
        this.selectedTableStatus = status;

        lblTableTitle.setText(name + " - " + selectedFloor);
        lblTableCust.setText(cust);
        lblTableTime.setText(time);
        lblTableBill.setText(bill);
        lblTableNote.setText(note);

        if ("DANG_PHUC_VU".equals(status)) {
            lblTableStatus.setText("Đang sử dụng");
            lblTableStatus.setBackground(new Color(0xEF, 0x44, 0x44));
        } else if ("DAT_TRUOC".equals(status)) {
            lblTableStatus.setText("Đặt trước");
            lblTableStatus.setBackground(new Color(0x10, 0xB9, 0x81));
        } else {
            lblTableStatus.setText("Trống");
            lblTableStatus.setBackground(new Color(0x64, 0x74, 0x8B));
        }
    }

    private void moDialogDatBanNhanh() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đặt Bàn Nhanh — Savoré Restaurant", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(400, 360);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(6, 2, 8, 8));
        p.setBorder(new EmptyBorder(16, 20, 16, 20));
        p.setBackground(Color.WHITE);

        p.add(new JLabel("Tên khách hàng:"));
        JTextField txtTen = new JTextField("Nguyễn Văn A");
        p.add(txtTen);

        p.add(new JLabel("Số điện thoại:"));
        JTextField txtSdt = new JTextField("0912345678");
        p.add(txtSdt);

        p.add(new JLabel("Số lượng khách:"));
        JSpinner spnKhach = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        p.add(spnKhach);

        p.add(new JLabel("Khu vực / Bàn:"));
        JComboBox<String> cboBan = new JComboBox<>(new String[]{"Bàn 01 - Tầng 1", "Bàn 03 - Tầng 1", "Bàn 04 - Tầng 1", "Bàn 07 - Tầng 1"});
        p.add(cboBan);

        p.add(new JLabel("Giờ đặt:"));
        JTextField txtGio = new JTextField("18:30");
        p.add(txtGio);

        JButton btnSave = new JButton("Xác nhận đặt bàn");
        btnSave.setBackground(new Color(0xD4, 0xA3, 0x59));
        btnSave.setForeground(new Color(0x1F, 0x27, 0x33));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(dlg, "Đặt bàn thành công cho " + txtTen.getText() + " lúc " + txtGio.getText());
            dlg.dispose();
        });

        dlg.add(p, BorderLayout.CENTER);
        dlg.add(btnSave, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // Scrollable implementation
    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
