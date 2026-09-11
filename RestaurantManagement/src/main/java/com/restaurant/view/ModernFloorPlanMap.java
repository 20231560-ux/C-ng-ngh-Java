package com.restaurant.view;

import com.restaurant.model.BanAn;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * MODERN FLOOR PLAN MAP (SƠ ĐỒ MẶT BẰNG NHÀ HÀNG 2D TRỰC QUAN)
 * Phân chia theo khu vực không gian thực tế, đa dạng hình dáng bàn và hiển thị ghế trực quan
 */
public class ModernFloorPlanMap extends JPanel {

    public enum TableShape { SQUARE, RECTANGLE, ROUND, VIP }
    public enum TableStatus { TRONG, DANG_DUNG, DAT_TRUOC, CAN_DON, KHOA }

    public static class TableNode {
        public int id;
        public String name;
        public String area;
        public TableShape shape;
        public TableStatus status;
        public int seats;
        public int x, y, width, height;
        public String customerName = "";
        public String timeSeated = "";
        public long currentBill = 0;
        public String note = "";

        public TableNode(int id, String name, String area, TableShape shape, TableStatus status, int seats, int x, int y, int width, int height) {
            this.id = id;
            this.name = name;
            this.area = area;
            this.shape = shape;
            this.status = status;
            this.seats = seats;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(Point p) {
            if (shape == TableShape.ROUND || shape == TableShape.VIP) {
                Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, height);
                return ellipse.contains(p);
            }
            return (p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height);
        }
    }

    private final List<TableNode> allTables = new ArrayList<>();
    private String currentArea = "Tầng 1";
    private TableNode selectedTable = null;
    private TableNode hoveredTable = null;
    private Consumer<TableNode> onTableSelected;

    private final JPanel pnlTabs;
    private final Canvas2D canvas;

    public ModernFloorPlanMap(Consumer<TableNode> onTableSelected) {
        this.onTableSelected = onTableSelected;
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));

        khoiTaoDanhSachBanMau();

        // 1. THANH TABS CHỌN KHU VỰC & CHÚ THÍCH TRẠNG THÁI
        JPanel pnlHeader = new JPanel(new BorderLayout(10, 0));
        pnlHeader.setOpaque(false);

        pnlTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTabs.setOpaque(false);

        String[] areas = {"Tầng 1", "Tầng 2", "Phòng VIP", "Sân Vườn", "Phòng Riêng"};
        for (String area : areas) {
            JButton btn = taoTabButton(area);
            pnlTabs.add(btn);
        }
        pnlHeader.add(pnlTabs, BorderLayout.WEST);

        // Chú thích trạng thái chuẩn
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        pnlLegend.setOpaque(false);
        pnlLegend.add(taoLegendItem("Trống", SavoreDesignSystem.Colors.STATUS_SUCCESS));
        pnlLegend.add(taoLegendItem("Đang dùng", new Color(0x1E3A8A)));
        pnlLegend.add(taoLegendItem("Đặt trước", SavoreDesignSystem.Colors.STATUS_WARNING));
        pnlLegend.add(taoLegendItem("Cần dọn", SavoreDesignSystem.Colors.STATUS_PURPLE));
        pnlLegend.add(taoLegendItem("Tạm khóa", new Color(0x94A3B8)));
        pnlHeader.add(pnlLegend, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // 2. CANVAS VẼ SƠ ĐỒ MẶT BẰNG 2D
        canvas = new Canvas2D();
        canvas.setPreferredSize(new Dimension(800, 380));
        add(canvas, BorderLayout.CENTER);

        // Chọn mặc định bàn đầu tiên
        if (!allTables.isEmpty()) {
            selectedTable = allTables.get(0);
            if (onTableSelected != null) onTableSelected.accept(selectedTable);
        }
    }

    private JButton taoTabButton(String area) {
        JButton b = new JButton(area) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = area.equals(currentArea);
                if (active) {
                    g2.setColor(SavoreDesignSystem.Colors.GOLD_PRIMARY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    setForeground(Color.WHITE);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0xFAF3E8));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(SavoreDesignSystem.Colors.GOLD_PRIMARY);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    setForeground(SavoreDesignSystem.Colors.GOLD_DARK);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(SavoreDesignSystem.Colors.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.addActionListener(e -> {
            currentArea = area;
            pnlTabs.repaint();
            canvas.repaint();
        });
        return b;
    }

    private JPanel taoLegendItem(String label, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(10, 10));
        dot.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        l.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        p.add(dot);
        p.add(l);
        return p;
    }

    public void dongBoVoiCSDL(List<BanAn> dsCSDL) {
        if (dsCSDL == null || dsCSDL.isEmpty()) return;
        for (int i = 0; i < Math.min(dsCSDL.size(), allTables.size()); i++) {
            BanAn b = dsCSDL.get(i);
            TableNode node = allTables.get(i);
            node.name = b.getTenBan();
            node.seats = b.getSoCho();
            String tt = b.getTrangThai();
            if (tt.contains("Trống") || tt.contains("TRONG")) node.status = TableStatus.TRONG;
            else if (tt.contains("Đang") || tt.contains("DUNG")) node.status = TableStatus.DANG_DUNG;
            else if (tt.contains("Đặt") || tt.contains("DAT")) node.status = TableStatus.DAT_TRUOC;
            else if (tt.contains("Dọn") || tt.contains("DON")) node.status = TableStatus.CAN_DON;
            else node.status = TableStatus.KHOA;
        }
        canvas.repaint();
    }

    private void khoiTaoDanhSachBanMau() {
        allTables.clear();
        // === TẦNG 1: SẢNH CHÍNH ===
        allTables.add(taoNode(1, "Bàn 01", "Tầng 1", TableShape.SQUARE, TableStatus.TRONG, 4, 60, 60, 64, 64, "", "", 0, "Gần cửa sổ sảnh đón"));
        allTables.add(taoNode(2, "Bàn 02", "Tầng 1", TableShape.SQUARE, TableStatus.TRONG, 4, 180, 60, 64, 64, "", "", 0, "Bàn cạnh tiểu cảnh thác nước"));
        allTables.add(taoNode(3, "Bàn 03", "Tầng 1", TableShape.RECTANGLE, TableStatus.DANG_DUNG, 6, 310, 52, 100, 72, "Anh Hoàng (VIP)", "45 phút", 1850000, "Khách kỷ niệm ngày cưới"));
        allTables.add(taoNode(4, "Bàn 04", "Tầng 1", TableShape.RECTANGLE, TableStatus.DAT_TRUOC, 6, 460, 52, 100, 72, "Chị Mai Phương", "19:00", 0, "Đã cọc set Menu Fine Dining"));
        allTables.add(taoNode(5, "Bàn 05", "Tầng 1", TableShape.ROUND, TableStatus.TRONG, 4, 610, 52, 72, 72, "", "", 0, "Khu vực trung tâm"));

        allTables.add(taoNode(6, "Bàn 06", "Tầng 1", TableShape.SQUARE, TableStatus.DANG_DUNG, 2, 60, 190, 56, 56, "Cô Vân", "15 phút", 420000, "2 Khách gọi set Trưa"));
        allTables.add(taoNode(7, "Bàn 07", "Tầng 1", TableShape.ROUND, TableStatus.TRONG, 5, 180, 180, 72, 72, "", "", 0, "Bàn tròn view vườn"));
        allTables.add(taoNode(8, "Bàn 08", "Tầng 1", TableShape.RECTANGLE, TableStatus.DANG_DUNG, 8, 310, 180, 110, 76, "Gia đình Bác Trực", "1h 10p", 3450000, "Đang dùng món tráng miệng"));
        allTables.add(taoNode(9, "Bàn 09", "Tầng 1", TableShape.SQUARE, TableStatus.CAN_DON, 4, 470, 190, 64, 64, "", "", 0, "Khách vừa rời bàn, cần lau dọn"));
        allTables.add(taoNode(10, "Bàn 10", "Tầng 1", TableShape.ROUND, TableStatus.TRONG, 4, 610, 180, 72, 72, "", "", 0, "Gần quầy bánh ngọt"));

        // === TẦNG 2: TIỆC & ĐÔNG NGƯỜI ===
        allTables.add(taoNode(11, "Bàn 201", "Tầng 2", TableShape.RECTANGLE, TableStatus.TRONG, 8, 80, 70, 120, 80, "", "", 0, "Phòng tiệc Tầng 2 view phố"));
        allTables.add(taoNode(12, "Bàn 202", "Tầng 2", TableShape.RECTANGLE, TableStatus.DANG_DUNG, 10, 260, 70, 140, 80, "Công ty FPT Software", "2 tiếng", 8900000, "Tiệc liên hoan quý"));
        allTables.add(taoNode(13, "Bàn 203", "Tầng 2", TableShape.ROUND, TableStatus.DAT_TRUOC, 6, 460, 70, 84, 84, "Anh Tuấn Anh", "18:30", 0, "Yêu cầu set rượu vang hảo hạng"));
        allTables.add(taoNode(14, "Bàn 204", "Tầng 2", TableShape.SQUARE, TableStatus.TRONG, 4, 620, 70, 68, 68, "", "", 0, "Bàn góc yên tĩnh"));
        allTables.add(taoNode(15, "Bàn 205", "Tầng 2", TableShape.RECTANGLE, TableStatus.TRONG, 8, 140, 200, 130, 80, "", "", 0, "Sảnh tiệc mở"));
        allTables.add(taoNode(16, "Bàn 206", "Tầng 2", TableShape.ROUND, TableStatus.DANG_DUNG, 8, 380, 195, 90, 90, "Đoàn khách Hàn Quốc", "50 phút", 4800000, "Thích đồ nướng BBQ"));

        // === PHÒNG VIP ===
        allTables.add(taoNode(17, "VIP 01 — Hoàng Gia", "Phòng VIP", TableShape.VIP, TableStatus.DANG_DUNG, 16, 100, 80, 180, 110, "Tập đoàn Vingroup", "1 tiếng", 18500000, "Phòng cách âm tuyệt đối, sommelier riêng"));
        allTables.add(taoNode(18, "VIP 02 — Thượng Đỉnh", "Phòng VIP", TableShape.VIP, TableStatus.DAT_TRUOC, 12, 340, 80, 160, 100, "Bộ Ngoại Giao", "19:30", 0, "Thực đơn thiết kế riêng"));
        allTables.add(taoNode(19, "VIP 03 — Kim Cương", "Phòng VIP", TableShape.ROUND, TableStatus.TRONG, 8, 560, 85, 100, 100, "", "", 0, "Bàn tròn xoay thông minh"));

        // === SÂN VƯỜN ===
        allTables.add(taoNode(20, "Vườn 01", "Sân Vườn", TableShape.ROUND, TableStatus.TRONG, 4, 90, 80, 72, 72, "", "", 0, "Khu vườn hồng thoáng đãng"));
        allTables.add(taoNode(21, "Vườn 02", "Sân Vườn", TableShape.ROUND, TableStatus.DANG_DUNG, 4, 230, 80, 72, 72, "Nhóm bạn trẻ", "30 phút", 860000, "Gọi đồ uống cocktail"));
        allTables.add(taoNode(22, "Vườn 03", "Sân Vườn", TableShape.RECTANGLE, TableStatus.DAT_TRUOC, 8, 380, 75, 120, 75, "Chị Hải Yến", "18:00", 0, "Ngắm hoàng hôn"));
        allTables.add(taoNode(23, "Vườn 04", "Sân Vườn", TableShape.SQUARE, TableStatus.TRONG, 2, 570, 85, 60, 60, "", "", 0, "Bàn đôi lãng mạn cạnh hồ cá"));

        // === PHÒNG RIÊNG ===
        allTables.add(taoNode(24, "Suite 01", "Phòng Riêng", TableShape.RECTANGLE, TableStatus.TRONG, 6, 120, 90, 110, 75, "", "", 0, "Phòng riêng cho gia đình"));
        allTables.add(taoNode(25, "Suite 02", "Phòng Riêng", TableShape.VIP, TableStatus.DANG_DUNG, 10, 320, 80, 150, 95, "Gia đình Giám Đốc Minh", "1h 20p", 6200000, "Cần bảo mật thông tin"));
        allTables.add(taoNode(26, "Suite 03", "Phòng Riêng", TableShape.SQUARE, TableStatus.TRONG, 4, 540, 90, 72, 72, "", "", 0, "Phòng họp ăn trưa kinh doanh"));
    }

    private TableNode taoNode(int id, String name, String area, TableShape shape, TableStatus status, int seats, int x, int y, int w, int h, String cust, String time, long bill, String note) {
        TableNode node = new TableNode(id, name, area, shape, status, seats, x, y, w, h);
        node.customerName = cust;
        node.timeSeated = time;
        node.currentBill = bill;
        node.note = note;
        return node;
    }

    /* =========================================================================
     * CANVAS 2D VẼ KIẾN TRÚC & CÁC BÀN VỚI GHẾ NGỒI
     * ========================================================================= */
    private class Canvas2D extends JPanel {
        public Canvas2D() {
            setOpaque(false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    TableNode old = hoveredTable;
                    hoveredTable = null;
                    for (TableNode n : allTables) {
                        if (n.area.equals(currentArea) && n.contains(e.getPoint())) {
                            hoveredTable = n;
                            break;
                        }
                    }
                    if (old != hoveredTable) {
                        setCursor(hoveredTable != null ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    for (TableNode n : allTables) {
                        if (n.area.equals(currentArea) && n.contains(e.getPoint())) {
                            selectedTable = n;
                            repaint();
                            if (onTableSelected != null) onTableSelected.accept(selectedTable);
                            break;
                        }
                    }
                }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 1. NỀN PHÒNG & MẶT BẰNG KIẾN TRÚC
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2.setColor(SavoreDesignSystem.Colors.BORDER);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);

            // Vẽ lưới ô tinh tế của bản vẽ mặt bằng
            g2.setColor(new Color(0xFAF6F0));
            for (int x = 20; x < w; x += 25) g2.drawLine(x, 0, x, h);
            for (int y = 20; y < h; y += 25) g2.drawLine(0, y, w, y);

            // 2. VẼ CÁC KHU VỰC ĐẶC TRƯNG CỦA MẶT BẰNG
            veKhuVucKienTruc(g2, w, h);

            // 3. VẼ CÁC BÀN ĂN & GHẾ NGỒI THUỘC TẦNG HIỆN TẠI
            for (TableNode node : allTables) {
                if (node.area.equals(currentArea)) {
                    veBanVaGhe(g2, node);
                }
            }

            g2.dispose();
        }

        private void veKhuVucKienTruc(Graphics2D g2, int w, int h) {
            // Cửa ra vào / Lối vào
            g2.setColor(new Color(0xFAF3E8));
            g2.fillRoundRect(10, h / 2 - 40, 24, 80, 6, 6);
            g2.setColor(SavoreDesignSystem.Colors.BORDER_GOLD);
            g2.drawRoundRect(10, h / 2 - 40, 24, 80, 6, 6);
            g2.setColor(SavoreDesignSystem.Colors.GOLD_DARK);
            g2.setFont(SavoreDesignSystem.Fonts.get(9, Font.BOLD));

            // Quầy bar & Pha chế
            g2.setColor(new Color(0x0F, 0x17, 0x2A, 12));
            g2.fillRoundRect(w - 74, 30, 56, h - 60, 10, 10);
            g2.setColor(SavoreDesignSystem.Colors.BORDER);
            g2.drawRoundRect(w - 74, 30, 56, h - 60, 10, 10);

            g2.setColor(SavoreDesignSystem.Colors.TEXT_MUTED);
            g2.setFont(SavoreDesignSystem.Fonts.get(9, Font.BOLD));
            g2.drawString("QUẦY BAR", w - 68, 60);
            g2.drawString("& PHA CHẾ", w - 68, 75);
            g2.drawString("🍸 ☕ 🍷", w - 65, 95);
        }

        private void veBanVaGhe(Graphics2D g2, TableNode node) {
            boolean isSel = (node == selectedTable);
            boolean isHov = (node == hoveredTable);

            Color fillColor;
            Color borderColor;
            Color textColor = Color.WHITE;

            switch (node.status) {
                case TRONG:
                    fillColor = SavoreDesignSystem.Colors.STATUS_SUCCESS_BG;
                    borderColor = SavoreDesignSystem.Colors.STATUS_SUCCESS;
                    textColor = new Color(0x047857);
                    break;
                case DANG_DUNG:
                    fillColor = new Color(0x1E3A8A);
                    borderColor = new Color(0x1D4ED8);
                    textColor = Color.WHITE;
                    break;
                case DAT_TRUOC:
                    fillColor = SavoreDesignSystem.Colors.STATUS_WARNING_BG;
                    borderColor = SavoreDesignSystem.Colors.STATUS_WARNING;
                    textColor = new Color(0xB45309);
                    break;
                case CAN_DON:
                    fillColor = SavoreDesignSystem.Colors.STATUS_PURPLE_BG;
                    borderColor = SavoreDesignSystem.Colors.STATUS_PURPLE;
                    textColor = new Color(0x6D28D9);
                    break;
                default:
                    fillColor = new Color(0xF1F5F9);
                    borderColor = new Color(0x94A3B8);
                    textColor = new Color(0x475569);
                    break;
            }

            // 1. VẼ GHẾ NGỒI BAO QUANH BÀN
            veGheNgoi(g2, node, borderColor);

            // 2. VẼ BÓNG NỔI KHI HOVER HOẶC ĐƯỢC CHỌN
            if (isSel || isHov) {
                g2.setColor(new Color(0xB0, 0x82, 0x46, isSel ? 60 : 35));
                if (node.shape == TableShape.ROUND || node.shape == TableShape.VIP) {
                    g2.fillOval(node.x - 4, node.y - 4, node.width + 8, node.height + 8);
                } else {
                    g2.fillRoundRect(node.x - 4, node.y - 4, node.width + 8, node.height + 8, 14, 14);
                }
            }

            // 3. VẼ THÂN BÀN
            g2.setColor(fillColor);
            if (node.shape == TableShape.ROUND || node.shape == TableShape.VIP) {
                g2.fillOval(node.x, node.y, node.width, node.height);
                g2.setColor(isSel ? SavoreDesignSystem.Colors.GOLD_PRIMARY : borderColor);
                g2.setStroke(new BasicStroke(isSel ? 2.5f : 1.5f));
                g2.drawOval(node.x, node.y, node.width, node.height);
            } else {
                g2.fillRoundRect(node.x, node.y, node.width, node.height, 10, 10);
                g2.setColor(isSel ? SavoreDesignSystem.Colors.GOLD_PRIMARY : borderColor);
                g2.setStroke(new BasicStroke(isSel ? 2.5f : 1.5f));
                g2.drawRoundRect(node.x, node.y, node.width, node.height, 10, 10);
            }

            // 4. THÔNG TIN TRONG BÀN (Tên bàn & Sức chứa)
            g2.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
            FontMetrics fm = g2.getFontMetrics();
            String name = node.name;
            if (name.length() > 10) name = name.substring(0, 8) + "…";
            int tw = fm.stringWidth(name);
            int cx = node.x + (node.width - tw) / 2;
            int cy = node.y + node.height / 2 - 2;

            g2.setColor(textColor);
            g2.drawString(name, cx, cy);

            g2.setFont(SavoreDesignSystem.Fonts.get(9, Font.PLAIN));
            String sub = node.seats + " chỗ";
            if (node.status == TableStatus.DANG_DUNG && node.currentBill > 0) {
                sub = (node.currentBill / 1000) + "k";
            }
            int sw = g2.getFontMetrics().stringWidth(sub);
            g2.drawString(sub, node.x + (node.width - sw) / 2, cy + 12);
        }

        private void veGheNgoi(Graphics2D g2, TableNode node, Color chairColor) {
            g2.setColor(new Color(chairColor.getRed(), chairColor.getGreen(), chairColor.getBlue(), 160));
            int gw = 14, gh = 6; // Kích thước ghế

            if (node.shape == TableShape.SQUARE) {
                // 4 ghế 4 phía
                g2.fillRoundRect(node.x + (node.width - gw) / 2, node.y - gh - 2, gw, gh, 3, 3);
                g2.fillRoundRect(node.x + (node.width - gw) / 2, node.y + node.height + 2, gw, gh, 3, 3);
                g2.fillRoundRect(node.x - gh - 2, node.y + (node.height - gw) / 2, gh, gw, 3, 3);
                g2.fillRoundRect(node.x + node.width + 2, node.y + (node.height - gw) / 2, gh, gw, 3, 3);
            } else if (node.shape == TableShape.RECTANGLE) {
                // Ghế dọc 2 bên
                int count = Math.max(2, node.seats / 2);
                int step = node.width / (count + 1);
                for (int i = 1; i <= count; i++) {
                    g2.fillRoundRect(node.x + i * step - gw / 2, node.y - gh - 2, gw, gh, 3, 3);
                    g2.fillRoundRect(node.x + i * step - gw / 2, node.y + node.height + 2, gw, gh, 3, 3);
                }
                g2.fillRoundRect(node.x - gh - 2, node.y + (node.height - gw) / 2, gh, gw, 3, 3);
                g2.fillRoundRect(node.x + node.width + 2, node.y + (node.height - gw) / 2, gh, gw, 3, 3);
            } else {
                // Ghế tỏa tròn quanh bàn tròn hoặc oval
                int nChairs = Math.max(4, node.seats);
                double rx = node.width / 2.0 + 7;
                double ry = node.height / 2.0 + 7;
                double centerX = node.x + node.width / 2.0;
                double centerY = node.y + node.height / 2.0;

                for (int i = 0; i < nChairs; i++) {
                    double angle = 2 * Math.PI * i / nChairs;
                    int cx = (int) (centerX + rx * Math.cos(angle));
                    int cy = (int) (centerY + ry * Math.sin(angle));
                    g2.fillOval(cx - 5, cy - 5, 10, 10);
                }
            }
        }
    }
}
