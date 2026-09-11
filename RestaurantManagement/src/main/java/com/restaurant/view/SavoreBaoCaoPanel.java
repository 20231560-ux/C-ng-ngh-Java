package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 10/12 — DOANH THU & PHÂN TÍCH BÁO CÁO (ANALYTICS DASHBOARD)
 * Thiết kế chuẩn 1:1 theo ô 10/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreBaoCaoPanel extends JPanel implements Scrollable {

    private final DecimalFormat fmt = new DecimalFormat("#,### đ");
    private String selectedPeriod = "HÔM NAY";

    public SavoreBaoCaoPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

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

        // 1. TOP CONTROLS: PERIOD TABS + CSV EXPORT
        container.add(taoTopFilterBar(), BorderLayout.NORTH);

        // 2. MAIN 2-COLUMN ANALYTICS
        JPanel pnlBody = new JPanel(new GridBagLayout());
        pnlBody.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Cột trái (60%): Biểu đồ phân tích doanh thu & Cơ cấu thanh toán
        gbc.gridx = 0;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 12);
        pnlBody.add(taoCotBieuDo(), gbc);

        // Cột phải (38%): Chỉ số tài chính & Top món bán chạy
        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlBody.add(taoCotChiSo(), gbc);

        container.add(pnlBody, BorderLayout.CENTER);
        return container;
    }

    /* =========================================================================
     * 1. TOP PERIOD TABS & NÚT XUẤT CSV
     * ========================================================================= */
    private JPanel taoTopFilterBar() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setOpaque(false);

        // Hàng tab kỳ báo cáo
        JPanel pnlTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTabs.setOpaque(false);

        String[] periods = {"HÔM NAY", "7 NGÀY QUA", "THÁNG NÀY", "NĂM NAY"};
        for (String p : periods) {
            JButton btn = new JButton(p) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean isSel = selectedPeriod.equals(p);
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
            btn.setFont(SavoreDesignSystem.Fonts.get(12, selectedPeriod.equals(p) ? Font.BOLD : Font.PLAIN));
            btn.setForeground(selectedPeriod.equals(p) ? Color.WHITE : SavoreDesignSystem.Colors.TEXT_DARK);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(8, 14, 8, 14));

            btn.addActionListener(e -> {
                selectedPeriod = p;
                pnlTabs.repaint();
                repaint();
            });
            pnlTabs.add(btn);
        }

        pnl.add(pnlTabs, BorderLayout.WEST);

        // Nút Xuất CSV bên phải
        SavoreDesignSystem.ModernButton btnExport = new SavoreDesignSystem.ModernButton("📥 Xuất Báo Cáo CSV", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnExport.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        btnExport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã xuất báo cáo doanh thu & đơn hàng Savoré thành công ra thư mục Desktop!", "Xuất Báo Cáo Hoàn Tất", JOptionPane.INFORMATION_MESSAGE);
        });
        pnl.add(btnExport, BorderLayout.EAST);

        return pnl;
    }

    /* =========================================================================
     * 2. CỘT TRÁI: BIỂU ĐỒ DOANH THU & PHƯƠNG THỨC THANH TOÁN
     * ========================================================================= */
    private JPanel taoCotBieuDo() {
        JPanel pnl = new JPanel(new BorderLayout(0, 12));
        pnl.setOpaque(false);

        // Card 1: Biểu đồ cột phân tích doanh thu theo giờ có đỉnh highlight
        JPanel cardChart = new JPanel(new BorderLayout(0, 10)) {
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
        cardChart.setOpaque(false);
        cardChart.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Header Card
        JPanel pnlH = new JPanel(new BorderLayout());
        pnlH.setOpaque(false);

        JLabel lblTitle = new JLabel("BIỂU ĐỒ DOANH THU THEO KHUNG GIỜ HOẠT ĐỘNG");
        lblTitle.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblTitle.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnlH.add(lblTitle, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Đỉnh doanh thu lúc 18h - 20h (Dạ tiệc)");
        lblSub.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblSub.setForeground(SavoreDesignSystem.Colors.STATUS_SUCCESS);
        pnlH.add(lblSub, BorderLayout.EAST);

        cardChart.add(pnlH, BorderLayout.NORTH);

        // Canvas vẽ biểu đồ cột
        JPanel chartCanvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                // Lưới ngang
                g2.setColor(new Color(0xF1, 0xEB, 0xE1));
                for (int i = 1; i <= 4; i++) {
                    int y = h - 25 - (i * (h - 45) / 4);
                    g2.drawLine(20, y, w - 20, y);
                }

                // Dữ liệu cột doanh thu các mốc giờ
                String[] hours = {"08h", "10h", "12h", "14h", "16h", "18h", "20h", "22h"};
                double[] revs = {1.8, 3.2, 12.5, 4.2, 5.8, 17.2, 15.6, 6.4}; // Triệu đồng
                double max = 20.0;

                int barWidth = Math.max(18, (w - 60) / (hours.length * 2));
                int gap = (w - 60) / hours.length;

                for (int i = 0; i < hours.length; i++) {
                    int cx = 35 + i * gap;
                    int barH = (int) ((revs[i] / max) * (h - 55));
                    int barY = h - 25 - barH;

                    boolean isPeak = (i == 5); // 18h là đỉnh

                    if (isPeak) {
                        // Cột vàng kim nổi bật
                        GradientPaint gp = new GradientPaint(0, barY, new Color(0xD4, 0xA3, 0x59), 0, h - 25, new Color(0x9E, 0x6D, 0x31));
                        g2.setPaint(gp);
                        g2.fillRoundRect(cx, barY, barWidth, barH, 6, 6);

                        // Tooltip lá cờ trên đỉnh cột
                        g2.setColor(SavoreDesignSystem.Colors.BG_SIDEBAR);
                        g2.fillRoundRect(cx - 30, barY - 26, 76, 20, 6, 6);
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        g2.drawString("17.247.600 đ", cx - 26, barY - 12);
                    } else {
                        // Cột xanh navy trung tính sang trọng
                        GradientPaint gp = new GradientPaint(0, barY, new Color(0x3B, 0x82, 0xF6), 0, h - 25, new Color(0x1D, 0x4E, 0xD8));
                        g2.setPaint(gp);
                        g2.fillRoundRect(cx, barY, barWidth, barH, 6, 6);
                    }

                    // Nhãn trục X
                    g2.setColor(SavoreDesignSystem.Colors.TEXT_MUTED);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    g2.drawString(hours[i], cx + (barWidth / 2) - 8, h - 8);
                }

                g2.dispose();
            }
        };
        chartCanvas.setPreferredSize(new Dimension(0, 210));
        chartCanvas.setOpaque(false);
        cardChart.add(chartCanvas, BorderLayout.CENTER);

        pnl.add(cardChart, BorderLayout.CENTER);

        // Card 2: Phương thức thanh toán (Tiền mặt, QR, Thẻ)
        JPanel cardPayment = new JPanel(new BorderLayout(0, 10)) {
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
        cardPayment.setOpaque(false);
        cardPayment.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblPayTitle = new JLabel("CƠ CẤU PHƯƠNG THỨC THANH TOÁN");
        lblPayTitle.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblPayTitle.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        cardPayment.add(lblPayTitle, BorderLayout.NORTH);

        JPanel pnlPayBars = new JPanel(new GridLayout(3, 1, 0, 8));
        pnlPayBars.setOpaque(false);

        pnlPayBars.add(taoPayRow("📱 Chuyển khoản QR (VietQR / MoMo)", "38.900.000 đ (45%)", 45, new Color(0x3B, 0x82, 0xF6)));
        pnlPayBars.add(taoPayRow("💵 Tiền mặt tại quầy thu ngân", "32.850.000 đ (38%)", 38, new Color(0x10, 0xB9, 0x81)));
        pnlPayBars.add(taoPayRow("💳 Thẻ tín dụng / Ghi nợ POS", "14.700.000 đ (17%)", 17, new Color(0xB0, 0x82, 0x46)));

        cardPayment.add(pnlPayBars, BorderLayout.CENTER);
        pnl.add(cardPayment, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel taoPayRow(String label, String value, int pct, Color color) {
        JPanel pnl = new JPanel(new BorderLayout(0, 4));
        pnl.setOpaque(false);

        JPanel pnlText = new JPanel(new BorderLayout());
        pnlText.setOpaque(false);

        JLabel lblL = new JLabel(label);
        lblL.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblL.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnlText.add(lblL, BorderLayout.WEST);

        JLabel lblR = new JLabel(value);
        lblR.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblR.setForeground(color);
        pnlText.add(lblR, BorderLayout.EAST);

        pnl.add(pnlText, BorderLayout.NORTH);

        JProgressBar pb = new JProgressBar(0, 100);
        pb.setValue(pct);
        pb.setPreferredSize(new Dimension(0, 6));
        pb.setForeground(color);
        pb.setBackground(new Color(0xEB, 0xE5, 0xDC));
        pb.setBorderPainted(false);
        pnl.add(pb, BorderLayout.SOUTH);

        return pnl;
    }

    /* =========================================================================
     * 3. CỘT PHẢI: CHỈ SỐ TÀI CHÍNH & TOP MÓN BÁN CHẠY
     * ========================================================================= */
    private JPanel taoCotChiSo() {
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        // Card 1: Tổng doanh thu lớn
        SavoreDesignSystem.StatCard sc1 = new SavoreDesignSystem.StatCard("DOANH THU KỲ BÁO CÁO", "86.450.000 đ", "💰", "+14.2% so với kỳ trước", SavoreDesignSystem.Colors.GOLD_PRIMARY, 82);
        sc1.setMaximumSize(new Dimension(9999, 100));
        pnl.add(sc1);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card 2: Tổng đơn & Giá trị TB
        SavoreDesignSystem.StatCard sc2 = new SavoreDesignSystem.StatCard("TỔNG ĐƠN HOÀN TẤT", "324 đơn", "🧾", "Giá trị trung bình 266.800 đ/đơn", SavoreDesignSystem.Colors.STATUS_SUCCESS, 70);
        sc2.setMaximumSize(new Dimension(9999, 100));
        pnl.add(sc2);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card 3: Top món bán chạy nhất
        JPanel cardTop = new JPanel(new BorderLayout(0, 8)) {
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
        cardTop.setOpaque(false);
        cardTop.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblTopTitle = new JLabel("TOP MÓN DOANH THU CAO NHẤT");
        lblTopTitle.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblTopTitle.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        cardTop.add(lblTopTitle, BorderLayout.NORTH);

        JPanel pnlList = new JPanel(new GridLayout(3, 1, 0, 6));
        pnlList.setOpaque(false);

        pnlList.add(taoTopDishRow("1. Bò Wagyu A5 Nướng Đá", "48 phần", "21.600.000 đ"));
        pnlList.add(taoTopDishRow("2. Lẩu Nấm Savoré Đặc Biệt", "36 nồi", "14.004.000 đ"));
        pnlList.add(taoTopDishRow("3. Combo Hoàng Gia Savoré", "14 set", "13.860.000 đ"));

        cardTop.add(pnlList, BorderLayout.CENTER);
        pnl.add(cardTop);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card 4: Cơ cấu tài chính ước tính
        JPanel cardProfit = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFAF7F2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cardProfit.setOpaque(false);
        cardProfit.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblProfTitle = new JLabel("ƯỚC TÍNH TÀI CHÍNH & LỢI NHUẬN GỘP");
        lblProfTitle.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblProfTitle.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        cardProfit.add(lblProfTitle, BorderLayout.NORTH);

        JPanel pnlStats = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlStats.setOpaque(false);

        JLabel lblCost = new JLabel("• Chi phí nguyên vật liệu: 25.935.000 đ (30%)");
        lblCost.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblCost.setForeground(SavoreDesignSystem.Colors.STATUS_WARNING);
        pnlStats.add(lblCost);

        JLabel lblMargin = new JLabel("• Lợi nhuận gộp ước tính: 60.515.000 đ (70%)");
        lblMargin.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblMargin.setForeground(SavoreDesignSystem.Colors.STATUS_SUCCESS);
        pnlStats.add(lblMargin);

        cardProfit.add(pnlStats, BorderLayout.CENTER);
        pnl.add(cardProfit);

        return pnl;
    }

    private JPanel taoTopDishRow(String name, String qty, String rev) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);

        JLabel lblN = new JLabel(name);
        lblN.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblN.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
        pnl.add(lblN, BorderLayout.WEST);

        JLabel lblR = new JLabel(qty + " • " + rev);
        lblR.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblR.setForeground(SavoreDesignSystem.Colors.GOLD_PRIMARY);
        pnl.add(lblR, BorderLayout.EAST);

        return pnl;
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
