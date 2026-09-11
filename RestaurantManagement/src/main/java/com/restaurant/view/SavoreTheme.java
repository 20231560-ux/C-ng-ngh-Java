package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

/** Lối tắt tới bộ thiết kế SAVORÉ — dùng cho màn đăng nhập và sơ đồ mặt bằng. */
public final class SavoreTheme {

    private SavoreTheme() { }

    public static final Color BG_MAIN           = SavoreDesignSystem.Colors.BG_MAIN;
    public static final Color BG_CARD           = SavoreDesignSystem.Colors.BG_CARD;
    public static final Color BORDER            = SavoreDesignSystem.Colors.BORDER;
    public static final Color BORDER_GOLD       = SavoreDesignSystem.Colors.BORDER_GOLD;
    public static final Color GOLD_PRIMARY      = SavoreDesignSystem.Colors.GOLD_PRIMARY;
    public static final Color GOLD_DARK         = SavoreDesignSystem.Colors.GOLD_DARK;
    public static final Color GOLD_LIGHT        = SavoreDesignSystem.Colors.GOLD_LIGHT;
    public static final Color GOLD_BG           = SavoreDesignSystem.Colors.GOLD_BG;
    public static final Color TEXT_DARK         = SavoreDesignSystem.Colors.TEXT_DARK;
    public static final Color TEXT_MUTED        = SavoreDesignSystem.Colors.TEXT_MUTED;
    public static final Color STATUS_SUCCESS    = SavoreDesignSystem.Colors.STATUS_SUCCESS;
    public static final Color STATUS_SUCCESS_BG = SavoreDesignSystem.Colors.STATUS_SUCCESS_BG;
    public static final Color STATUS_WARNING    = SavoreDesignSystem.Colors.STATUS_WARNING;
    public static final Color STATUS_WARNING_BG = SavoreDesignSystem.Colors.STATUS_WARNING_BG;
    public static final Color STATUS_DANGER     = SavoreDesignSystem.Colors.STATUS_DANGER;
    public static final Color STATUS_DANGER_BG  = SavoreDesignSystem.Colors.STATUS_DANGER_BG;
    public static final Color STATUS_INFO       = SavoreDesignSystem.Colors.STATUS_INFO;
    public static final Color STATUS_PURPLE     = SavoreDesignSystem.Colors.STATUS_PURPLE;

    public static Font font(int size, int style) {
        return SavoreDesignSystem.Fonts.get(size, style);
    }

    public static Font display(int size) {
        return SavoreDesignSystem.Fonts.display(size);
    }

    public static JScrollPane taoCuon(Component c) {
        return SavoreDesignSystem.createScroll(c);
    }

    public static String tien(double v) {
        return SavoreDesignSystem.formatTien(v);
    }

    /** Nút dùng ở màn đăng nhập. */
    public static class Button extends JButton {

        public static final int STYLE_PRIMARY   = 0;
        public static final int STYLE_SECONDARY = 1;
        public static final int STYLE_GHOST     = 2;
        public static final int STYLE_DANGER    = 3;

        private final int style;
        private float hover = 0f;
        private int radius = 12;

        public Button(String text, int style) {
            super(text);
            this.style = style;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(SavoreDesignSystem.Fonts.get(13, Font.BOLD));
            setPreferredSize(new Dimension(
                    getFontMetrics(getFont()).stringWidth(text) + 44, 46));
            Timer t = new Timer(15, null);
            t.addActionListener(e -> {
                float d = getModel().isRollover() ? 1f : 0f;
                hover += (d - hover) * 0.28f;
                if (Math.abs(hover - d) < 0.02f) { hover = d; t.stop(); }
                repaint();
            });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { t.start(); }
                @Override public void mouseExited(MouseEvent e) { t.start(); }
            });
        }

        public void setRadius(int r) { radius = r; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean nhan = getModel().isPressed();
            Color chu;
            switch (style) {
                case STYLE_SECONDARY:
                    g2.setColor(hover > 0.02f ? GOLD_BG : BG_CARD);
                    g2.fillRoundRect(0, 0, w - 1, h - 3, radius, radius);
                    g2.setColor(hover > 0.02f ? BORDER_GOLD : BORDER);
                    g2.drawRoundRect(0, 0, w - 1, h - 3, radius, radius);
                    chu = hover > 0.02f ? GOLD_DARK : TEXT_DARK;
                    break;
                case STYLE_GHOST:
                    if (hover > 0.02f) {
                        g2.setColor(new Color(0x1F, 0x29, 0x33, (int) (14 * hover)));
                        g2.fillRoundRect(0, 0, w - 1, h - 3, radius, radius);
                    }
                    chu = TEXT_MUTED;
                    break;
                case STYLE_DANGER:
                    g2.setColor(nhan ? STATUS_DANGER.darker() : STATUS_DANGER);
                    g2.fillRoundRect(0, 0, w - 1, h - 3, radius, radius);
                    chu = Color.WHITE;
                    break;
                default:
                    if (hover > 0.02f) {
                        g2.setColor(new Color(0xC9, 0xA2, 0x27, (int) (70 * hover)));
                        g2.fillRoundRect(-3, -1, w + 6, h + 2, radius + 3, radius + 3);
                    }
                    g2.setPaint(new GradientPaint(0, 0,
                            nhan ? GOLD_DARK : GOLD_LIGHT, 0, h, nhan ? GOLD_DARK : GOLD_PRIMARY));
                    g2.fillRoundRect(0, 0, w - 1, h - 3, radius, radius);
                    chu = new Color(0x241B03);
            }
            g2.setFont(getFont());
            g2.setColor(chu);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2,
                    (h - 3) / 2 + fm.getAscent() / 2 - 2);
            g2.dispose();
        }
    }
}