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
 * COMBO MANAGEMENT & SELECTION PANEL
 * Quản lý các set thực đơn combo ưu đãi dành cho bàn tiệc, gia đình và doanh nghiệp
 */
public class ComboPanel extends JPanel {

    public static class ComboItem {
        public String code;
        public String name;
        public String desc;
        public String[] dishes;
        public long originalPrice;
        public long comboPrice;
        public String discountBadge;
        public String targetGuests;
        public String icon;

        public ComboItem(String code, String name, String desc, String[] dishes, long orig, long combo, String discount, String guests, String icon) {
            this.code = code;
            this.name = name;
            this.desc = desc;
            this.dishes = dishes;
            this.originalPrice = orig;
            this.comboPrice = combo;
            this.discountBadge = discount;
            this.targetGuests = guests;
            this.icon = icon;
        }
    }

    private final List<ComboItem> comboList = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("#,### đ");

    public ComboPanel() {
        setBackground(SavoreDesignSystem.Colors.BG_MAIN);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(16, 20, 20, 20));

        khoiTaoDuLieuCombo();

        // 1. HEADER
        JPanel pnlHeader = new JPanel(new BorderLayout(10, 0));
        pnlHeader.setOpaque(false);

        JPanel pnlTitle = SavoreDesignSystem.createSectionHeader("QUẢN LÝ COMBO & SET MENU ƯU ĐÃI",
                "Danh sách các gói thực đơn kết hợp dành cho bàn tiệc, cặp đôi và sự kiện", null);
        pnlHeader.add(pnlTitle, BorderLayout.WEST);

        JPanel pnlHeaderActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlHeaderActions.setOpaque(false);

        SavoreDesignSystem.ModernButton btnAdd = new SavoreDesignSystem.ModernButton("+ Tạo Combo Mới", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Mở hộp thoại tạo gói Combo món ăn mới", "Thêm Combo", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlHeaderActions.add(btnAdd);

        pnlHeader.add(pnlHeaderActions, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. LƯỚI THẺ COMBO
        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);

        for (ComboItem item : comboList) {
            grid.add(taoCardCombo(item));
        }

        JScrollPane scroll = SavoreTheme.taoCuon(grid);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void khoiTaoDuLieuCombo() {
        comboList.clear();
        comboList.add(new ComboItem("CB-ROYAL", "SET MENU ROYAL MICHELIN DINING", "Set tiệc thượng lưu dành cho 2 khách với nguyên liệu nhập khẩu",
                new String[]{"• 2x Thăn Bò Wagyu A5 Nướng Đá", "• 2x Súp Bào Ngư Vi Cá", "• 1x Salad Cá Hồi Hun Khói", "• 1x Chai Vang Đỏ Chateau Margaux"},
                3850000, 2990000, "TIẾT KIỆM 22%", "2 - 3 Khách", "👑"));

        comboList.add(new ComboItem("CB-OCEAN", "SET HẢI SẢN HOÀNG GIA ĐẠI DƯƠNG", "Trọn vẹn hương vị hải sản tươi sống chọn lọc trong ngày",
                new String[]{"• 1x Cua Hoàng Đế Hấp Rượu Vang", "• 2x Tôm Hùm Bông Nướng Bơ Tỏi", "• 1x Lẩu Thái TomYum Hải Sản", "• 1x Đĩa Tráng Miệng Trái Cây"},
                4200000, 3450000, "TIẾT KIỆM 18%", "4 - 6 Khách", "🦞"));

        comboList.add(new ComboItem("CB-FAMILY", "SET GIA ĐÌNH ĐOÀN VIÊN HẠNH PHÚC", "Thực đơn ấm cúng, tròn vị phù hợp mọi lứa tuổi trong gia đình",
                new String[]{"• 1x Lẩu Gà Tiềm Nấm Đông Trùng", "• 1x Bò Fuji Nướng Tảng", "• 1x Cơm Chiên Hoàng Bào", "• 1x Canh Sườn Hầm Rau Củ"},
                2100000, 1680000, "TIẾT KIỆM 20%", "4 - 6 Khách", "🍲"));

        comboList.add(new ComboItem("CB-LUNCH", "SET BUSINESS EXECUTIVE LUNCH", "Phục vụ nhanh chóng trong 15 phút, tiện lợi cho các cuộc hẹn trưa",
                new String[]{"• 1x Steak Bò Thăn Úc Sốt Tiêu Đen", "• 1x Búp Xà Lách Caesar", "• 1x Bánh Mì Bơ Tỏi Nướng Giòn", "• 1x Cà Phê Espresso Ý"},
                650000, 480000, "TIẾT KIỆM 26%", "1 - 2 Khách", "💼"));
    }

    private JPanel taoCardCombo(ComboItem item) {
        SavoreDesignSystem.ModernCard card = new SavoreDesignSystem.ModernCard(16, Color.WHITE);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Top
        JPanel pnlTop = new JPanel(new BorderLayout(10, 0));
        pnlTop.setOpaque(false);

        JLabel lblIcon = new JLabel(item.icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setPreferredSize(new Dimension(44, 44));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(SavoreDesignSystem.Colors.GOLD_BG);
        lblIcon.setForeground(SavoreDesignSystem.Colors.GOLD_DARK);
        pnlTop.add(lblIcon, BorderLayout.WEST);

        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitle.setOpaque(false);

        JLabel lblN = new JLabel(item.name);
        lblN.setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
        lblN.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);

        JLabel lblG = new JLabel("👥 Phù hợp: " + item.targetGuests + "  •  " + item.desc);
        lblG.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
        lblG.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlTitle.add(lblN);
        pnlTitle.add(lblG);
        pnlTop.add(pnlTitle, BorderLayout.CENTER);

        JLabel lblBadge = new JLabel(item.discountBadge);
        lblBadge.setFont(SavoreDesignSystem.Fonts.get(10, Font.BOLD));
        lblBadge.setForeground(SavoreDesignSystem.Colors.STATUS_DANGER);
        lblBadge.setOpaque(true);
        lblBadge.setBackground(SavoreDesignSystem.Colors.STATUS_DANGER_BG);
        lblBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        pnlTop.add(lblBadge, BorderLayout.EAST);

        card.add(pnlTop, BorderLayout.NORTH);

        // Center: Danh sách món trong combo
        JPanel pnlDishes = new JPanel();
        pnlDishes.setOpaque(false);
        pnlDishes.setLayout(new BoxLayout(pnlDishes, BoxLayout.Y_AXIS));
        pnlDishes.setBorder(new EmptyBorder(8, 4, 8, 4));

        for (String d : item.dishes) {
            JLabel lblD = new JLabel(d);
            lblD.setFont(SavoreDesignSystem.Fonts.get(11, Font.PLAIN));
            lblD.setForeground(SavoreDesignSystem.Colors.TEXT_DARK);
            lblD.setBorder(new EmptyBorder(2, 0, 2, 0));
            pnlDishes.add(lblD);
        }
        card.add(pnlDishes, BorderLayout.CENTER);

        // Bottom: Giá & Nút chọn
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);
        pnlBottom.setBorder(new MatteBorder(1, 0, 0, 0, SavoreDesignSystem.Colors.BORDER));
        pnlBottom.setBorder(BorderFactory.createCompoundBorder(
                pnlBottom.getBorder(),
                new EmptyBorder(8, 0, 0, 0)
        ));

        JPanel pnlPrice = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlPrice.setOpaque(false);

        JLabel lblComboPrice = new JLabel(df.format(item.comboPrice));
        lblComboPrice.setFont(SavoreDesignSystem.Fonts.display(18));
        lblComboPrice.setForeground(SavoreDesignSystem.Colors.GOLD_DARK);

        JLabel lblOrigPrice = new JLabel("<html><strike>" + df.format(item.originalPrice) + "</strike></html>");
        lblOrigPrice.setFont(SavoreDesignSystem.Fonts.get(12, Font.PLAIN));
        lblOrigPrice.setForeground(SavoreDesignSystem.Colors.TEXT_MUTED);

        pnlPrice.add(lblComboPrice);
        pnlPrice.add(lblOrigPrice);
        pnlBottom.add(pnlPrice, BorderLayout.WEST);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pnlBtn.setOpaque(false);

        SavoreDesignSystem.ModernButton btnOrder = new SavoreDesignSystem.ModernButton("Thêm Vào Đơn POS", SavoreDesignSystem.ModernButton.Variant.PRIMARY_GOLD);
        btnOrder.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã thêm " + item.name + " vào đơn gọi món!", "Gọi món thành công", JOptionPane.INFORMATION_MESSAGE);
        });

        SavoreDesignSystem.ModernButton btnEdit = new SavoreDesignSystem.ModernButton("Sửa", SavoreDesignSystem.ModernButton.Variant.SECONDARY_WHITE);
        btnEdit.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chỉnh sửa cấu hình " + item.name, "Chỉnh sửa Combo", JOptionPane.INFORMATION_MESSAGE);
        });

        pnlBtn.add(btnEdit);
        pnlBtn.add(btnOrder);
        pnlBottom.add(pnlBtn, BorderLayout.EAST);

        card.add(pnlBottom, BorderLayout.SOUTH);
        return card;
    }
}
