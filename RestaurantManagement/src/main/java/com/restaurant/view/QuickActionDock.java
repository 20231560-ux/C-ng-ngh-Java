package com.restaurant.view;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * QUICK ACTION DOCK
 * Thanh dock nổi dạng viên thuốc thủy tinh (Glass Pill Dock) phía dưới màn hình,
 * tích hợp hiệu ứng hover magnification, glow ánh vàng đồng và điều hướng tức thì.
 */
public class QuickActionDock extends JPanel {

    private final Consumer<String> onNavigate;
    private final List<DockItem> items = new ArrayList<>();
    private int hoveredIndex = -1;

    public static class DockItem {
        public final String icon;
        public final String label;
        public final String targetModule;
        public final Color accentColor;
        public float hoverAnim = 0f;

        public DockItem(String icon, String label, String target, Color color) {
            this.icon = icon;
            this.label = label;
            this.targetModule = target;
            this.accentColor = color;
        }
    }

    public QuickActionDock(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

        khoiTaoItems();
        dungGiaoDien();
    }

    private void khoiTaoItems() {
        items.clear();
        items.add(new DockItem("➕", "Đặt bàn", "DAT_BAN", SavoreDesignSystem.Colors.COPPER_GOLD));
        items.add(new DockItem("🍴", "Tạo đơn hàng", "POS", SavoreDesignSystem.Colors.WARM_GOLD));
        items.add(new DockItem("📱", "Quét QR", "POS", new Color(0x3B, 0x82, 0xF6)));
        items.add(new DockItem("💳", "Thanh toán", "POS", new Color(0x10, 0xB9, 0x81)));
        items.add(new DockItem("🔥", "Gửi bếp", "POS", new Color(0xEF, 0x44, 0x44)));
    }

    private void dungGiaoDien() {
        removeAll();
        for (int i = 0; i < items.size(); i++) {
            final int idx = i;
            DockItem item = items.get(i);
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();
                    boolean isHover = (idx == hoveredIndex);

                    // Pill button background
                    if (isHover) {
                        g2.setColor(new Color(item.accentColor.getRed(), item.accentColor.getGreen(), item.accentColor.getBlue(), 60));
                        g2.fillRoundRect(0, 0, w, h, 20, 20);
                        g2.setColor(item.accentColor);
                        g2.setStroke(new BasicStroke(1.4f));
                        g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                    } else {
                        g2.setColor(new Color(0x1A, 0x24, 0x36, 180));
                        g2.fillRoundRect(0, 0, w, h, 20, 20);
                        g2.setColor(new Color(0xC8, 0x92, 0x45, 40));
                        g2.setStroke(new BasicStroke(1.0f));
                        g2.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                    }

                    // Icon
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, isHover ? 14 : 12));
                    FontMetrics fmI = g2.getFontMetrics();
                    g2.drawString(item.icon, 12, h / 2 + 5);

                    // Label
                    g2.setFont(SavoreDesignSystem.Fonts.get(11, Font.BOLD));
                    g2.setColor(isHover ? Color.WHITE : new Color(0xEA, 0xF0, 0xF8));
                    g2.drawString(item.label, 32, h / 2 + 4);

                    g2.dispose();
                }
            };
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(120, 36));

            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hoveredIndex = idx; repaint(); }
                @Override public void mouseExited(MouseEvent e) { if (hoveredIndex == idx) { hoveredIndex = -1; repaint(); } }
                @Override public void mouseClicked(MouseEvent e) {
                    if (onNavigate != null) onNavigate.accept(item.targetModule);
                }
            });

            add(btn);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        // Nền dock viên thuốc nổi thủy tinh (Glass Pill Surface)
        g2.setColor(new Color(0x0A, 0x0F, 0x1A, 240));
        g2.fillRoundRect(2, 2, w - 4, h - 4, 28, 28);

        // Đổ bóng quầng sáng vàng đồng êm dịu
        g2.setColor(new Color(0xC8, 0x92, 0x45, 55));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(2, 2, w - 5, h - 5, 28, 28);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(670, 48);
    }
}
