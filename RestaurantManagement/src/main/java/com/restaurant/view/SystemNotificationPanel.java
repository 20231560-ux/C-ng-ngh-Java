package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * SYSTEM NOTIFICATION PANEL (THÔNG BÁO HỆ THỐNG VẬN HÀNH)
 * Luồng cảnh báo thời gian thực: Tồn kho, Gọi bàn, Đặt chỗ mới, Bếp quá hạn
 */
public class SystemNotificationPanel extends SavoreDesignSystem.ModernCard {

    public static class NotifItem {
        public String icon;
        public String title;
        public String content;
        public String time;
        public Color statusColor;
        public Color statusBg;

        public NotifItem(String icon, String title, String content, String time, Color sc, Color sbg) {
            this.icon = icon;
            this.title = title;
            this.content = content;
            this.time = time;
            this.statusColor = sc;
            this.statusBg = sbg;
        }
    }

    private final List<NotifItem> notifList = new ArrayList<>();

    public SystemNotificationPanel() {
        super(16, Color.WHITE);
        setLayout(new BorderLayout(0, 10));

        khoiTaoThongBao();

        // Header
        JLabel lblLive = new JLabel("● 6 mới");
        lblLive.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblLive.setForeground(SavoreDesignSystem.Colors.STATUS_DANGER);
        lblLive.setOpaque(true);
        lblLive.setBackground(SavoreDesignSystem.Colors.STATUS_DANGER_BG);
        lblLive.setBorder(new EmptyBorder(2, 6, 2, 6));

        JPanel pnlHeader = SavoreDesignSystem.createSectionHeader("THÔNG BÁO HOẠT ĐỘNG", "Cảnh báo vận hành, phục vụ và bếp", lblLive);
        add(pnlHeader, BorderLayout.NORTH);

        // List
        JPanel pnlItems = new JPanel();
        pnlItems.setOpaque(false);
        pnlItems.setLayout(new BoxLayout(pnlItems, BoxLayout.Y_AXIS));

        for (int i = 0; i < notifList.size(); i++) {
            NotifItem item = notifList.get(i);
            pnlItems.add(taoNotifRow(item));
            if (i < notifList.size() - 1) {
                pnlItems.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        add(pnlItems, BorderLayout.CENTER);
    }

    private void khoiTaoThongBao() {
        notifList.clear();
        notifList.add(new NotifItem("⚠️", "Sắp hết nguyên liệu kho", "Thịt thăn bò Úc chỉ còn 2.5 kg (Mức an toàn: 10 kg)", "5p trước",
                SavoreDesignSystem.Colors.STATUS_DANGER, SavoreDesignSystem.Colors.STATUS_DANGER_BG));
        notifList.add(new NotifItem("🔔", "Bàn yêu cầu hỗ trợ", "Bàn 04 (Tầng 1) gọi nhân viên rót thêm rượu vang & đổi ly", "12p trước",
                SavoreDesignSystem.Colors.STATUS_WARNING, SavoreDesignSystem.Colors.STATUS_WARNING_BG));
        notifList.add(new NotifItem("📅", "Đặt chỗ mới trực tuyến", "Đoàn 10 khách vừa đặt phòng VIP 02 lúc 19:30 tối nay", "25p trước",
                SavoreDesignSystem.Colors.STATUS_SUCCESS, SavoreDesignSystem.Colors.STATUS_SUCCESS_BG));
        notifList.add(new NotifItem("👤", "Nhân sự nhận ca trực", "Bếp phó Trần Văn Bình đã điểm danh nhận ca làm tối", "40p trước",
                SavoreDesignSystem.Colors.STATUS_INFO, SavoreDesignSystem.Colors.STATUS_INFO_BG));
        notifList.add(new NotifItem("🧾", "Đơn hàng mới tạo", "Bàn 08 vừa gọi Combo Steak & Wine Thượng Hạng (3.450k)", "45p trước",
                SavoreDesignSystem.Colors.GOLD_PRIMARY, SavoreDesignSystem.Colors.GOLD_BG));
        notifList.add(new NotifItem("⏱️", "Món quá thời gian dự kiến", "Bàn 12 — Lẩu Hải Sản TomYum chế biến quá 15 phút", "1h trước",
                SavoreDesignSystem.Colors.STATUS_DANGER, SavoreDesignSystem.Colors.STATUS_DANGER_BG));
    }

    private JPanel taoNotifRow(NotifItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFAF7F2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                // Viền cạnh màu trạng thái
                g2.setColor(item.statusColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 10, 8, 10));

        // Icon
        JLabel lblIcon = new JLabel(item.icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        lblIcon.setPreferredSize(new Dimension(24, 24));
        row.add(lblIcon, BorderLayout.WEST);

        // Center
        JPanel pnlText = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlText.setOpaque(false);

        JLabel lblTitle = new JLabel(item.title);
        lblTitle.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
        lblTitle.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblDesc = new JLabel(item.content);
        lblDesc.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblDesc.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlText.add(lblTitle);
        pnlText.add(lblDesc);
        row.add(pnlText, BorderLayout.CENTER);

        // Time
        JLabel lblTime = new JLabel(item.time);
        lblTime.setFont(SavoreDesignSystem.Fonts.get(10, Font.PLAIN));
        lblTime.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);
        row.add(lblTime, BorderLayout.EAST);

        return row;
    }
}
