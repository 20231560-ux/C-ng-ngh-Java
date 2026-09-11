package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * RESERVATION TIMELINE PANEL (LỊCH ĐẶT BÀN THEO DÒNG THỜI GIAN)
 * Hiển thị các mốc khách đặt bàn trong ngày với điểm mốc trực quan và trạng thái
 */
public class ReservationTimelinePanel extends SavoreDesignSystem.ModernCard {

    public static class ReservationItem {
        public String time;
        public String customer;
        public String table;
        public int guests;
        public String status;
        public Color statusColor;
        public Color statusBg;
        public String note;

        public ReservationItem(String time, String customer, String table, int guests, String status, Color sc, Color sbg, String note) {
            this.time = time;
            this.customer = customer;
            this.table = table;
            this.guests = guests;
            this.status = status;
            this.statusColor = sc;
            this.statusBg = sbg;
            this.note = note;
        }
    }

    private final List<ReservationItem> items = new ArrayList<>();
    private final Consumer<String> chuyenPhanHeCallback;

    public ReservationTimelinePanel(Consumer<String> chuyenPhanHeCallback) {
        super(16, Color.WHITE);
        this.chuyenPhanHeCallback = chuyenPhanHeCallback;
        setLayout(new BorderLayout(0, 12));

        khoiTaoDuLieuMau();

        // Header
        JLabel lblHeaderCount = new JLabel(items.size() + " lượt đặt");
        lblHeaderCount.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblHeaderCount.setForeground(SavoreDesignSystem.Colors.GOLD_DARK);
        lblHeaderCount.setOpaque(true);
        lblHeaderCount.setBackground(SavoreDesignSystem.Colors.GOLD_BG);
        lblHeaderCount.setBorder(new EmptyBorder(3, 8, 3, 8));

        JPanel pnlHeader = SavoreDesignSystem.createSectionHeader("LỊCH ĐẶT BÀN HÔM NAY", "Theo dõi lịch hẹn khách đến theo khung giờ", lblHeaderCount);
        add(pnlHeader, BorderLayout.NORTH);

        // Danh sách Timeline
        JPanel pnlList = new JPanel();
        pnlList.setOpaque(false);
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));

        for (int i = 0; i < items.size(); i++) {
            ReservationItem item = items.get(i);
            boolean isLast = (i == items.size() - 1);
            pnlList.add(taoTimelineNode(item, isLast));
            if (!isLast) pnlList.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        add(pnlList, BorderLayout.CENTER);

        // Nút bấm đặt bàn nhanh
        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Tiếp Nhận Đặt Bàn Mới", SavoreDesignSystem.ModernButton.Variant.OUTLINE_GOLD);
        btnAdd.addActionListener(e -> {
            if (chuyenPhanHeCallback != null) chuyenPhanHeCallback.accept("BAN_DAT");
        });
        add(btnAdd, BorderLayout.SOUTH);
    }

    private void khoiTaoDuLieuMau() {
        items.clear();
        items.add(new ReservationItem("11:00", "Khách Hoàng Nam", "Bàn 05 (Tầng 1)", 4, "ĐÃ ĐẾN",
                SavoreDesignSystem.Colors.STATUS_SUCCESS, SavoreDesignSystem.Colors.STATUS_SUCCESS_BG, "Khách đến đúng giờ, đã nhận bàn"));
        items.add(new ReservationItem("12:30", "Khách Mai Lan", "VIP 02 (Phòng VIP)", 8, "ĐÃ ĐẶT",
                SavoreDesignSystem.Colors.STATUS_WARNING, SavoreDesignSystem.Colors.STATUS_WARNING_BG, "Tiệc sinh nhật, đã chuẩn bị hoa"));
        items.add(new ReservationItem("14:00", "Khách Quốc Bảo", "Bàn 03 (Tầng 1)", 2, "SẮP ĐẾN",
                SavoreDesignSystem.Colors.STATUS_INFO, SavoreDesignSystem.Colors.STATUS_INFO_BG, "Hẹn ăn trưa đối tác kinh doanh"));
        items.add(new ReservationItem("18:30", "Khách Thu Hà", "Vườn 03 (Sân Vườn)", 6, "CHỜ XÁC NHẬN",
                SavoreDesignSystem.Colors.STATUS_PURPLE, SavoreDesignSystem.Colors.STATUS_PURPLE_BG, "Đặt bàn ngắm hoàng hôn"));
        items.add(new ReservationItem("19:45", "Khách Minh Trí", "VIP 01 (Hoàng Gia)", 12, "ĐÃ ĐẶT",
                SavoreDesignSystem.Colors.STATUS_WARNING, SavoreDesignSystem.Colors.STATUS_WARNING_BG, "Tiệc tiếp đãi đoàn khách quốc tế"));
    }

    private JPanel taoTimelineNode(ReservationItem item, boolean isLast) {
        JPanel node = new JPanel(new BorderLayout(10, 0));
        node.setOpaque(false);

        // Cột thời gian bên trái
        JPanel pnlTime = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlTime.setOpaque(false);
        pnlTime.setPreferredSize(new Dimension(50, 40));

        JLabel lblT = new JLabel(item.time);
        lblT.setFont(SavoreDesignSystem.Fonts.get(12, Font.BOLD));
        lblT.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblG = new JLabel(item.guests + " khách");
        lblG.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblG.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlTime.add(lblT);
        pnlTime.add(lblG);
        node.add(pnlTime, BorderLayout.WEST);

        // Điểm nối timeline
        JPanel pnlDotTrack = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                if (!isLast) {
                    g2.setColor(SavoreDesignSystem.Colors.BORDER);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(cx, 16, cx, getHeight());
                }

                g2.setColor(item.statusColor);
                g2.fillOval(cx - 5, 6, 10, 10);
                g2.setColor(Color.WHITE);
                g2.fillOval(cx - 2, 9, 4, 4);

                g2.dispose();
            }
        };
        pnlDotTrack.setPreferredSize(new Dimension(18, 48));
        pnlDotTrack.setOpaque(false);
        node.add(pnlDotTrack, BorderLayout.CENTER);

        // Khối thông tin khách & Bàn
        JPanel pnlContent = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xFAF7F2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnlContent.setOpaque(false);
        pnlContent.setBorder(new EmptyBorder(6, 10, 6, 10));

        JPanel pnlText = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlText.setOpaque(false);

        JLabel lblCust = new JLabel(item.customer);
        lblCust.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblCust.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblTable = new JLabel("📍 " + item.table + (item.note.isEmpty() ? "" : " • " + item.note));
        lblTable.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblTable.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlText.add(lblCust);
        pnlText.add(lblTable);
        pnlContent.add(pnlText, BorderLayout.CENTER);

        JLabel badge = new JLabel(item.status, SwingConstants.CENTER);
        badge.setFont(SavoreDesignSystem.Fonts.get(9, Font.BOLD));
        badge.setForeground(item.statusColor);
        badge.setOpaque(true);
        badge.setBackground(item.statusBg);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));

        JPanel pnlBadgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        pnlBadgeWrap.setOpaque(false);
        pnlBadgeWrap.add(badge);
        pnlContent.add(pnlBadgeWrap, BorderLayout.EAST);

        node.add(pnlContent, BorderLayout.EAST);
        return node;
    }
}
