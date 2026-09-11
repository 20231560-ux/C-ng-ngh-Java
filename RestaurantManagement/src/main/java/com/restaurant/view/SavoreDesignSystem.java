package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * SAVORÉ DESIGN SYSTEM
 * Bộ thiết kế giao diện chuẩn quốc tế cho hệ thống quản lý vận hành nhà hàng cao cấp
 */
public final class SavoreDesignSystem {

    private SavoreDesignSystem() {}

    /* =========================================================================
     * 1. HỆ THỐNG MÀU SẮC (APP COLORS)
     * ========================================================================= */
    public static final class Colors {
        // Nền & Khung
        public static final Color BG_MAIN       = new Color(0xF8F5F0); // Nền chính Warm Neutral
        public static final Color BG_CARD       = Color.WHITE;          // Nền thẻ trắng tinh tế
        public static final Color BG_CARD_ALT   = new Color(0xFAF7F2); // Nền thẻ phụ ấm
        public static final Color BG_SIDEBAR    = new Color(0x0F172A); // Executive Midnight Navy
        public static final Color BG_SIDEBAR_HOVER = new Color(0x1E293B);
        public static final Color BG_SIDEBAR_ACTIVE= new Color(0x273549);

        // DARK LUXURY COMMAND CENTER PALETTE
        public static final Color DARK_BG        = new Color(0x080B10); // Deep Black
        public static final Color DARK_NAVY      = new Color(0x101722); // Dark Navy
        public static final Color COPPER_GOLD    = new Color(0xC89245); // Copper Gold
        public static final Color WARM_GOLD      = new Color(0xE5B766); // Warm Gold
        public static final Color SOFT_WHITE     = new Color(0xF5F1E8); // Soft White
        public static final Color GLASS_BORDER   = new Color(0xC8, 0x92, 0x45, 45);
        public static final Color GLASS_SURFACE  = new Color(0x10, 0x17, 0x22, 220);

        // Đường viền & Đổ bóng
        public static final Color BORDER        = new Color(0xE8DFD5); // Viền ấm nhạt
        public static final Color BORDER_GOLD   = new Color(0xD4C4AF); // Viền vàng mờ
        public static final Color BORDER_SIDEBAR= new Color(0x1E293B);
        public static final Color SHADOW        = new Color(0x2D, 0x27, 0x23, 14);

        // Nhận diện thương hiệu Champagne Gold
        public static final Color GOLD_PRIMARY  = new Color(0xB08246);
        public static final Color GOLD_DARK     = new Color(0x7F5927);
        public static final Color GOLD_LIGHT    = new Color(0xD4A359);
        public static final Color GOLD_BG       = new Color(0xFAF3E8);

        // Chữ (Typography)
        public static final Color TEXT_DARK     = new Color(0x2D2723); // Chữ chính
        public static final Color TEXT_MUTED    = new Color(0x8C827A); // Chữ phụ/mờ
        public static final Color TEXT_SUB      = new Color(0x6E645D); // Chữ ghi chú
        public static final Color TEXT_GOLD     = new Color(0x7F5927); // Chữ điểm nhấn vàng

        // Trạng thái vận hành chuẩn quốc tế
        public static final Color STATUS_SUCCESS    = new Color(0x10B981); // Bàn trống / Bình thường
        public static final Color STATUS_SUCCESS_BG = new Color(0xEBF8F1);
        public static final Color STATUS_WARNING    = new Color(0xF59E0B); // Đặt trước / Cảnh báo
        public static final Color STATUS_WARNING_BG = new Color(0xFEF3E6);
        public static final Color STATUS_DANGER     = new Color(0xEF4444); // Cần dọn / Khẩn cấp
        public static final Color STATUS_DANGER_BG  = new Color(0xFEF2F2);
        public static final Color STATUS_INFO       = new Color(0x3B82F6); // Đang phục vụ / Thông tin
        public static final Color STATUS_INFO_BG    = new Color(0xEFF6FF);
        public static final Color STATUS_PURPLE     = new Color(0x8B5CF6);
        public static final Color STATUS_PURPLE_BG  = new Color(0xF5F3FF);
    }

    /* =========================================================================
     * 2. TYPOGRAPHY (APP FONTS)
     * ========================================================================= */
    public static final class Fonts {
        private static final String FONT_FAMILY = "Segoe UI";

        public static Font get(int size, int style) {
            return new Font(FONT_FAMILY, style, size);
        }

        public static Font display(int size) {
            return get(size, Font.BOLD);
        }

        public static Font mono(int size, int style) {
            return new Font("Consolas", style, size);
        }
    }

    /* =========================================================================
     * 3. THẺ HIỆN ĐẠI (MODERN CARD)
     * ========================================================================= */
    public static class ModernCard extends JPanel {
        private int cornerRadius = 16;
        private Color bgColor = Colors.BG_CARD;
        private Color borderColor = Colors.BORDER;
        private boolean showShadow = true;
        private boolean hoverEffect = false;
        private boolean isHovered = false;

        public ModernCard() {
            this(16, Colors.BG_CARD);
        }

        public ModernCard(int radius, Color bg) {
            this.cornerRadius = radius;
            this.bgColor = bg;
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(16, 16, 16, 16));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if (hoverEffect) { isHovered = true; repaint(); }
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (hoverEffect) { isHovered = false; repaint(); }
                }
            });
        }

        public void setCornerRadius(int r) { this.cornerRadius = r; repaint(); }
        public void setBgColor(Color c) { this.bgColor = c; repaint(); }
        public void setCardBackground(Color c) { this.bgColor = c; repaint(); }
        public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
        public void setShowShadow(boolean b) { this.showShadow = b; repaint(); }
        public void setShadow(boolean b) { this.showShadow = b; repaint(); }
        public void setHoverEffect(boolean b) { this.hoverEffect = b; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            if (showShadow) {
                g2.setColor(isHovered ? new Color(0x2D, 0x27, 0x23, 24) : Colors.SHADOW);
                g2.fillRoundRect(2, 4, w - 4, h - 4, cornerRadius, cornerRadius);
            }

            g2.setColor(bgColor != null ? bgColor : Colors.BG_CARD);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

            if (borderColor != null) {
                g2.setColor(isHovered ? Colors.GOLD_PRIMARY : borderColor);
                g2.setStroke(new BasicStroke(isHovered ? 1.5f : 1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /* =========================================================================
     * 4. THẺ KÍNH (GLASS CARD)
     * ========================================================================= */
    public static class GlassCard extends JPanel {
        private int cornerRadius = 18;
        private Color bgColor = Colors.GLASS_SURFACE;
        private Color borderColor = Colors.GLASS_BORDER;

        public GlassCard() { this(18, Colors.GLASS_SURFACE); }

        public GlassCard(int radius, Color bg) {
            this.cornerRadius = radius;
            this.bgColor = bg;
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            // Background glass
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

            // Subtle border glow
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /* =========================================================================
     * 5. NÚT BẤM (SAVORÉ BUTTON & MODERN BUTTON)
     * ========================================================================= */
    public static class ModernButton extends JButton {
        public enum Variant {
            PRIMARY_GOLD,
            SECONDARY_WHITE,
            OUTLINE_GOLD,
            DANGER_RED
        }

        private final Variant variant;
        private float hoverAlpha = 0f;
        private int cornerRadius = 10;

        public ModernButton(String text) {
            this(text, Variant.PRIMARY_GOLD);
        }

        public ModernButton(String text, Variant variant) {
            super(text);
            this.variant = variant != null ? variant : Variant.PRIMARY_GOLD;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(Fonts.get(12, Font.BOLD));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hoverAlpha = 1f; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hoverAlpha = 0f; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            Color fill = Colors.GOLD_PRIMARY;
            Color textColor = Color.WHITE;
            Color border = null;

            switch (variant) {
                case PRIMARY_GOLD:
                    fill = hoverAlpha > 0 ? Colors.GOLD_DARK : Colors.GOLD_PRIMARY;
                    textColor = Color.WHITE;
                    break;
                case SECONDARY_WHITE:
                    fill = hoverAlpha > 0 ? Colors.GOLD_BG : Color.WHITE;
                    textColor = Colors.TEXT_DARK;
                    border = Colors.BORDER;
                    break;
                case OUTLINE_GOLD:
                    fill = hoverAlpha > 0 ? Colors.GOLD_BG : new Color(0, 0, 0, 0);
                    textColor = Colors.GOLD_PRIMARY;
                    border = Colors.GOLD_PRIMARY;
                    break;
                case DANGER_RED:
                    fill = hoverAlpha > 0 ? new Color(0xDC2626) : Colors.STATUS_DANGER;
                    textColor = Color.WHITE;
                    break;
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
            }

            setForeground(textColor);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class Button extends JButton {
        public static final int STYLE_PRIMARY   = 0; // Vàng Champagne chính
        public static final int STYLE_SECONDARY = 1; // Nền ấm viền vàng
        public static final int STYLE_GHOST     = 2; // Trong suốt
        public static final int STYLE_DANGER    = 3; // Đỏ cảnh báo

        private final int style;
        private float hoverAlpha = 0f;
        private int radius = 10;

        public Button(String text, int style) {
            super(text);
            this.style = style;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(Fonts.get(12, Font.BOLD));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hoverAlpha = 1f; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hoverAlpha = 0f; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            Color fill = Colors.GOLD_PRIMARY;
            Color text = Color.WHITE;
            Color border = null;

            switch (style) {
                case STYLE_PRIMARY:
                    fill = hoverAlpha > 0 ? Colors.GOLD_DARK : Colors.GOLD_PRIMARY;
                    text = Color.WHITE;
                    break;
                case STYLE_SECONDARY:
                    fill = hoverAlpha > 0 ? Colors.GOLD_BG : Colors.BG_CARD_ALT;
                    text = Colors.TEXT_DARK;
                    border = Colors.BORDER_GOLD;
                    break;
                case STYLE_GHOST:
                    fill = hoverAlpha > 0 ? new Color(0xFAF3E8) : new Color(0, 0, 0, 0);
                    text = Colors.GOLD_PRIMARY;
                    break;
                case STYLE_DANGER:
                    fill = hoverAlpha > 0 ? new Color(0xDC2626) : Colors.STATUS_DANGER;
                    text = Color.WHITE;
                    break;
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);
            }

            setForeground(text);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /* =========================================================================
     * 6. Ô NHẬP LIỆU & TÌM KIẾM
     * ========================================================================= */
    public static class SearchField extends JTextField {
        private final String placeholder;

        public SearchField(String placeholder) {
            this.placeholder = placeholder;
            setFont(Fonts.get(12, Font.PLAIN));
            setForeground(Colors.TEXT_DARK);
            setCaretColor(Colors.GOLD_PRIMARY);
            setBorder(new EmptyBorder(8, 34, 8, 12));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            g2.setColor(hasFocus() ? Colors.GOLD_PRIMARY : Colors.BORDER);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

            // Icon kính lúp
            g2.setFont(Fonts.get(12, Font.PLAIN));
            g2.setColor(Colors.TEXT_MUTED);
            g2.drawString("🔍", 10, h / 2 + 5);

            // Placeholder
            if (getText().isEmpty() && !hasFocus()) {
                g2.setFont(Fonts.get(12, Font.ITALIC));
                g2.drawString(placeholder, 34, h / 2 + 4);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /* =========================================================================
     * 7. TIÊU ĐỀ KHU VỰC (SECTION HEADER)
     * ========================================================================= */
    public static JPanel createSectionHeader(String title, String subtitle) {
        return createSectionHeader(title, subtitle, null);
    }

    public static JPanel createSectionHeader(String title, String subtitle, JComponent rightComponent) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel pnlLeft = new JPanel();
        pnlLeft.setOpaque(false);
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));

        JPanel pnlTitleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTitleRow.setOpaque(false);

        // Thanh vàng nhấn bên cạnh tiêu đề
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Colors.GOLD_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(4, 16));
        bar.setOpaque(false);
        pnlTitleRow.add(bar);

        JLabel lblT = new JLabel(title);
        lblT.setFont(Fonts.get(14, Font.BOLD));
        lblT.setForeground(Colors.TEXT_DARK);
        pnlTitleRow.add(lblT);

        pnlLeft.add(pnlTitleRow);

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel lblSub = new JLabel(subtitle);
            lblSub.setFont(Fonts.get(11, Font.PLAIN));
            lblSub.setForeground(Colors.TEXT_MUTED);
            lblSub.setBorder(new EmptyBorder(2, 10, 0, 0));
            pnlLeft.add(lblSub);
        }

        pnl.add(pnlLeft, BorderLayout.WEST);
        if (rightComponent != null) {
            pnl.add(rightComponent, BorderLayout.EAST);
        }
        return pnl;
    }

    /* =========================================================================
     * 8. STAT CARD (THẺ CHỈ SỐ NHỎ)
     * ========================================================================= */
    public static class StatCard extends JPanel {
        private final String title;
        private final String value;
        private final String icon;
        private final String subtitle;
        private final Color accentColor;
        private final int percent;

        public StatCard(String title, String value, String icon, String subtitle, Color color, int percent) {
            this.title = title;
            this.value = value;
            this.icon = icon;
            this.subtitle = subtitle;
            this.accentColor = color != null ? color : Colors.GOLD_PRIMARY;
            this.percent = Math.max(0, Math.min(100, percent));

            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 12, 10, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            // Background card
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 12, 12);
            g2.setColor(Colors.BORDER);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

            // Icon background
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
            g2.fillRoundRect(12, 12, 34, 34, 8, 8);

            // Icon
            g2.setFont(Fonts.get(16, Font.PLAIN));
            FontMetrics fmI = g2.getFontMetrics();
            int ix = 12 + (34 - fmI.stringWidth(icon)) / 2;
            int iy = 12 + (34 - fmI.getHeight()) / 2 + fmI.getAscent();
            g2.drawString(icon, ix, iy);

            // Title
            g2.setFont(Fonts.get(10, Font.BOLD));
            g2.setColor(Colors.TEXT_MUTED);
            g2.drawString(title, 54, 22);

            // Value
            g2.setFont(Fonts.get(16, Font.BOLD));
            g2.setColor(Colors.TEXT_DARK);
            g2.drawString(value, 54, 42);

            // Subtitle
            g2.setFont(Fonts.get(10, Font.PLAIN));
            g2.setColor(Colors.TEXT_SUB);
            g2.drawString(subtitle, 12, h - 14);

            // Mini progress bar
            int bw = w - 24;
            g2.setColor(new Color(0xE8DFD5));
            g2.fillRoundRect(12, h - 8, bw, 3, 2, 2);
            g2.setColor(accentColor);
            g2.fillRoundRect(12, h - 8, (int) (bw * (percent / 100.0)), 3, 2, 2);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(160, 76);
        }
    }

    /* =========================================================================
     * 9. STATUS BADGE
     * ========================================================================= */
    public static class StatusBadge extends JPanel {
        public StatusBadge(String text, Color textColor, Color bgColor) {
            setOpaque(false);
            setBorder(new EmptyBorder(3, 8, 3, 8));
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            JLabel lbl = new JLabel(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bgColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lbl.setFont(Fonts.get(10, Font.BOLD));
            lbl.setForeground(textColor);
            lbl.setOpaque(false);
            lbl.setBorder(new EmptyBorder(2, 6, 2, 6));
            add(lbl);
        }
    }

    /* =========================================================================
     * 10. SCROLL & FORMAT HELPERS
     * ========================================================================= */
    public static JScrollPane createScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    public static String formatTien(double v) {
        return String.format("%,.0f đ", v).replace(',', '.');
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(Fonts.get(12, Font.PLAIN));
        table.setSelectionBackground(Colors.GOLD_BG);
        table.setSelectionForeground(Colors.TEXT_DARK);
        table.setBackground(Color.WHITE);

        JTableHeader h = table.getTableHeader();
        h.setPreferredSize(new Dimension(10, 42));
        h.setReorderingAllowed(false);
        h.setBackground(Colors.BG_CARD_ALT);
        h.setForeground(Colors.TEXT_MUTED);
        h.setFont(Fonts.get(11, Font.BOLD));
        h.setBorder(new MatteBorder(0, 0, 1, 0, Colors.BORDER));

        ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
    }
}