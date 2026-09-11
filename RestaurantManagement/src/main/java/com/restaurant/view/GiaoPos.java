package com.restaurant.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class GiaoPos {

    // === BẢNG MÀU TRẮNG NGÀ + ĐỒNG (IVORY & BRONZE LUXURY) ===
    public static final Color NEN_TRANG_NGA  = new Color(0xFDFBF7); // Trắng ngà chính
    public static final Color NEN_PHU        = new Color(0xF5EFEB); // Kem ngà ấm
    public static final Color THE_TRANG      = new Color(0xFFFFFF); // Nền thẻ trắng tinh
    public static final Color THE_VIEN       = new Color(0xE6DCCE); // Viền đồng nhạt
    public static final Color THE_VIEN_DAM   = new Color(0xD4C4AF); // Viền đồng rõ

    // Màu nhấn ĐỒNG (Bronze / Brushed Gold)
    public static final Color DONG_CHINH     = new Color(0xB08246); // Đồng chuẩn sang trọng
    public static final Color DONG_DAM       = new Color(0x7F5927); // Đồng trầm
    public static final Color DONG_SANG      = new Color(0xD4A359); // Đồng vàng ánh kim
    public static final Color DONG_NHAT      = new Color(0xFAF3E8); // Nền đồng rất nhạt

    // Màu chữ
    public static final Color CHU_CHINH      = new Color(0x2D2723); // Espresso / Nâu đen ấm
    public static final Color CHU_PHU        = new Color(0x6E645D); // Nâu xám trung tính
    public static final Color CHU_MO         = new Color(0x9E948B); // Màu chữ phụ mờ

    // Màu trạng thái bàn trực quan
    public static final Color BAN_TRONG       = new Color(0x198754); // Xanh lục: Bàn trống
    public static final Color BAN_TRONG_NEN   = new Color(0xEBF8F1);
    public static final Color BAN_PHUC_VU     = new Color(0xC26D00); // Đồng cam: Đang phục vụ
    public static final Color BAN_PHUC_VU_NEN = new Color(0xFEF3E6);
    public static final Color BAN_DAT_TRUOC   = new Color(0x2563EB); // Xanh dương: Đã đặt trước
    public static final Color BAN_DAT_NEN     = new Color(0xEFF6FF);
    public static final Color BAN_CAN_THANH_TOAN = new Color(0xDC2626); // Đỏ: Cần thanh toán / Dọn
    public static final Color BAN_CAN_NEN     = new Color(0xFEF2F2);

    // Font chữ hệ thống
    public static final String FONT_NAME = chonFont();

    private GiaoPos() {}

    public static Font f(int size, int style) {
        return new Font(FONT_NAME, style, size);
    }

    public static String formatTien(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return df.format(amount) + " đ";
    }

    private static String chonFont() {
        String[] fonts = {"Segoe UI", "Roboto", "Noto Sans", "Tahoma", "Arial"};
        java.util.List<String> available = java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()
        );
        for (String f : fonts) {
            if (available.contains(f)) return f;
        }
        return "SansSerif";
    }

    // === THẺ BO GÓC SANG TRỌNG ===
    public static class ThePos extends JPanel {
        private int radius = 14;
        private Color borderColor = THE_VIEN;
        private Color bgColor = THE_TRANG;

        public ThePos() {
            this(new BorderLayout(), 14);
        }

        public ThePos(LayoutManager layout) {
            this(layout, 14);
        }

        public ThePos(LayoutManager layout, int radius) {
            super(layout);
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(14, 14, 14, 14));
        }

        public void setRadius(int radius) { this.radius = radius; repaint(); }
        public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
        public void setTheBackground(Color c) { this.bgColor = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            // Đổ bóng nhẹ sang trọng
            g2.setColor(new Color(0x40, 0x30, 0x20, 10));
            g2.fillRoundRect(1, 2, w - 2, h - 2, radius, radius);

            // Nền thẻ
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            // Viền thẻ
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // === NÚT CẢM ỨNG POS LỚN DỄ BẤM ===
    public static class NutPos extends JButton {
        public static final int STYLE_DONG = 0;      // Đồng vàng chính
        public static final int STYLE_DONG_PHU = 1;  // Viền đồng nền trắng
        public static final int STYLE_LUC = 2;       // Xanh lá (Hoàn thành / In / Mở bàn)
        public static final int STYLE_CAM = 3;       // Cam (Gửi bếp / Đặt trước)
        public static final int STYLE_DO = 4;        // Đỏ (Hủy / Xóa)
        public static final int STYLE_TRANG = 5;     // Nền trắng ngà bình thường
        public static final int STYLE_TAB = 6;       // Tab chọn màn hình

        private final int style;
        private boolean active = false;
        private float hover = 0f;
        private Timer hoverTimer;
        private int customRadius = 12;

        public NutPos(String text, int style) {
            super(text);
            this.style = style;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(f(14, Font.BOLD));
            setPreferredSize(new Dimension(getPreferredSize().width, 46)); // Nút to tối thiểu 46px

            hoverTimer = new Timer(15, e -> {
                float target = getModel().isRollover() ? 1f : 0f;
                hover += (target - hover) * 0.25f;
                if (Math.abs(hover - target) < 0.03f) {
                    hover = target;
                    hoverTimer.stop();
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hoverTimer.start(); }
                @Override public void mouseExited(MouseEvent e) { hoverTimer.start(); }
            });
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        public boolean isActive() {
            return active;
        }

        public void setCustomRadius(int r) {
            this.customRadius = r;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            Color bg1, bg2, border, textColor;

            switch (style) {
                case STYLE_DONG:
                    bg1 = DONG_SANG;
                    bg2 = DONG_CHINH;
                    border = DONG_DAM;
                    textColor = Color.WHITE;
                    break;
                case STYLE_DONG_PHU:
                    bg1 = hover > 0 ? DONG_NHAT : Color.WHITE;
                    bg2 = hover > 0 ? new Color(0xF5E6CF) : Color.WHITE;
                    border = DONG_CHINH;
                    textColor = DONG_DAM;
                    break;
                case STYLE_LUC:
                    bg1 = new Color(0x22C55E);
                    bg2 = BAN_TRONG;
                    border = new Color(0x15803D);
                    textColor = Color.WHITE;
                    break;
                case STYLE_CAM:
                    bg1 = new Color(0xF59E0B);
                    bg2 = new Color(0xD97706);
                    border = new Color(0xB45309);
                    textColor = Color.WHITE;
                    break;
                case STYLE_DO:
                    bg1 = new Color(0xEF4444);
                    bg2 = BAN_CAN_THANH_TOAN;
                    border = new Color(0xB91C1C);
                    textColor = Color.WHITE;
                    break;
                case STYLE_TAB:
                    if (active) {
                        bg1 = DONG_SANG;
                        bg2 = DONG_CHINH;
                        border = DONG_DAM;
                        textColor = Color.WHITE;
                    } else {
                        bg1 = hover > 0 ? DONG_NHAT : Color.WHITE;
                        bg2 = hover > 0 ? new Color(0xF4EFE6) : Color.WHITE;
                        border = hover > 0 ? DONG_SANG : THE_VIEN;
                        textColor = hover > 0 ? DONG_DAM : CHU_CHINH;
                    }
                    break;
                default: // STYLE_TRANG
                    bg1 = hover > 0 ? NEN_PHU : Color.WHITE;
                    bg2 = hover > 0 ? new Color(0xEDE6DB) : Color.WHITE;
                    border = hover > 0 ? DONG_CHINH : THE_VIEN;
                    textColor = CHU_CHINH;
                    break;
            }

            // Fill nền gradient
            g2.setPaint(new GradientPaint(0, 0, bg1, 0, h, bg2));
            g2.fillRoundRect(1, 1, w - 2, h - 2, customRadius, customRadius);

            // Viền
            g2.setColor(border);
            g2.setStroke(new BasicStroke(active ? 2.0f : 1.2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, customRadius, customRadius);

            // Vẽ chữ căn giữa
            g2.setFont(getFont());
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(getText());
            int th = fm.getAscent();
            g2.drawString(getText(), (w - tw) / 2, (h + th) / 2 - 3);

            g2.dispose();
        }
    }

    // === Ô NHẬP LIỆU CẢM ỨNG CÓ TÌM KIẾM ===
    public static class OTextPos extends JTextField {
        private String placeholder = "";
        private boolean isSearch = false;

        public OTextPos(String placeholder, boolean isSearch) {
            this.placeholder = placeholder;
            this.isSearch = isSearch;
            setOpaque(false);
            setFont(f(14, Font.PLAIN));
            setForeground(CHU_CHINH);
            setCaretColor(DONG_CHINH);
            setBorder(new EmptyBorder(0, isSearch ? 40 : 14, 0, 14));
            setPreferredSize(new Dimension(getPreferredSize().width, 44));

            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { repaint(); }
                @Override public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(1, 1, w - 2, h - 2, 12, 12);

            g2.setColor(isFocusOwner() ? DONG_CHINH : THE_VIEN);
            g2.setStroke(new BasicStroke(isFocusOwner() ? 1.8f : 1.1f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);

            if (isSearch) {
                // Vẽ icon kính lúp đồng
                g2.setColor(isFocusOwner() ? DONG_CHINH : CHU_MO);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(14, h / 2 - 7, 11, 11);
                g2.drawLine(23, h / 2 + 3, 28, h / 2 + 8);
            }

            g2.dispose();
            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner() && !placeholder.isEmpty()) {
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g3.setFont(f(13, Font.ITALIC));
                g3.setColor(CHU_MO);
                g3.drawString(placeholder, isSearch ? 40 : 14, h / 2 + 5);
                g3.dispose();
            }
        }
    }

    // === HUY HIỆU TRẠNG THÁI ===
    public static void veHuyHieu(Graphics2D g2, String text, int x, int y, Color textCol, Color bgCol) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(f(11, Font.BOLD));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(text);
        int pad = 16;
        int w = tw + pad;
        int h = 24;

        g.setColor(bgCol);
        g.fillRoundRect(x, y, w, h, 12, 12);

        g.setColor(textCol);
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(x, y, w, h, 12, 12);
        g.drawString(text, x + pad / 2, y + fm.getAscent() + (h - fm.getHeight()) / 2 + 1);
        g.dispose();
    }
}
