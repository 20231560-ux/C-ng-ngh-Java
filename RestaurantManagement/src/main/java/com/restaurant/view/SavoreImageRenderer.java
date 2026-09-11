package com.restaurant.view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * SAVORÉ IMAGE RENDERER
 * Cung cấp hình ảnh ẩm thực, tiệc bàn và avatar chân dung nhân viên chất lượng cao
 * dựa trên Java 2D vector graphics, đảm bảo giao diện luôn sống động và sang trọng.
 */
public final class SavoreImageRenderer {

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    private SavoreImageRenderer() {}

    /**
     * Tạo hình ảnh món ăn theo loại
     */
    public static BufferedImage getDishImage(String dishName, int w, int h) {
        String key = dishName + "_" + w + "_" + h;
        if (CACHE.containsKey(key)) return CACHE.get(key);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Nền đĩa tối màu sang trọng
        GradientPaint bgPaint = new GradientPaint(0, 0, new Color(0x18, 0x1E, 0x27), w, h, new Color(0x28, 0x1F, 0x18));
        g2.setPaint(bgPaint);
        g2.fillRoundRect(0, 0, w, h, 8, 8);

        // Viền đĩa vàng mờ
        g2.setColor(new Color(0xD4, 0xA3, 0x59, 80));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);

        int cx = w / 2;
        int cy = h / 2;
        int r = Math.min(w, h) / 2 - 8;

        String nameLower = (dishName != null) ? dishName.toLowerCase() : "";

        if (nameLower.contains("bò") || nameLower.contains("steak") || nameLower.contains("nướng")) {
            // Đĩa nướng đá / Steak
            g2.setColor(new Color(0x33, 0x33, 0x33));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            // Miếng bò chín tới viền cháy xém
            g2.setColor(new Color(0x5C, 0x2C, 0x16));
            g2.fillRoundRect(cx - r + 8, cy - r / 2, r * 2 - 16, r, 8, 8);
            // Vệt sốt tiêu đen
            g2.setColor(new Color(0x22, 0x12, 0x08));
            g2.drawArc(cx - r + 12, cy - r / 3, r * 2 - 24, r / 2, 20, 140);
            // Cà chua bi đỏ & rau hương thảo
            g2.setColor(new Color(0xEF, 0x44, 0x44));
            g2.fillOval(cx + r / 3, cy - r / 3, 8, 8);
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx - r / 2, cy + r / 4, 6, 6);
        } else if (nameLower.contains("lẩu") || nameLower.contains("tomyum") || nameLower.contains("súp")) {
            // Nồi lẩu hải sản vàng óng ánh
            g2.setColor(new Color(0x78, 0x35, 0x0F));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            // Nước dùng đỏ cay TomYum
            g2.setColor(new Color(0xDC, 0x26, 0x26));
            g2.fillOval(cx - r + 6, cy - r + 10, r * 2 - 12, r * 2 - 20);
            // Tôm sú & nấm
            g2.setColor(new Color(0xF9, 0x73, 0x16));
            g2.fillArc(cx - r / 2, cy - r / 3, r, r / 2, 45, 180);
            g2.setColor(new Color(0xFE, 0xF3, 0xC7));
            g2.fillOval(cx + r / 4, cy - r / 4, 7, 7);
            // Rau ngò xanh
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx, cy + r / 6, 6, 6);
        } else if (nameLower.contains("cơm") || nameLower.contains("chiên")) {
            // Đĩa cơm chiên hoàng kim
            g2.setColor(new Color(0x28, 0x23, 0x1F));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            // Cơm vàng
            g2.setColor(new Color(0xF5, 0x9E, 0x0B));
            g2.fillOval(cx - r + 6, cy - r + 8, r * 2 - 12, r * 2 - 16);
            // Hạt đậu hà lan & cà rốt & tôm
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx - r / 3, cy - r / 4, 5, 5);
            g2.fillOval(cx + r / 4, cy + r / 5, 5, 5);
            g2.setColor(new Color(0xF9, 0x73, 0x16));
            g2.fillOval(cx + r / 5, cy - r / 3, 6, 6);
            g2.fillOval(cx - r / 4, cy + r / 4, 6, 6);
        } else if (nameLower.contains("gà") || nameLower.contains("sốt")) {
            // Gà sốt óng ả
            g2.setColor(new Color(0x3B, 0x2A, 0x1E));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            g2.setColor(new Color(0xD9, 0x77, 0x06));
            g2.fillRoundRect(cx - r + 8, cy - r / 2, r * 2 - 16, r, 12, 12);
            // Sốt chanh dây vàng sáng
            g2.setColor(new Color(0xFB, 0xBF, 0x24));
            g2.fillOval(cx - r / 3, cy - r / 4, r / 2, r / 3);
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx + r / 3, cy, 6, 6);
        } else if (nameLower.contains("salad") || nameLower.contains("cá ngừ") || nameLower.contains("khai vị")) {
            // Đĩa salad tươi xanh
            g2.setColor(new Color(0x1C, 0x2A, 0x22));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            // Rau xà lách xanh tươi
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx - r + 6, cy - r + 8, r * 2 - 12, r * 2 - 16);
            // Cá ngừ tím hồng & trứng
            g2.setColor(new Color(0xE1, 0x1D, 0x48));
            g2.fillRoundRect(cx - r / 3, cy - r / 4, 12, 8, 3, 3);
            g2.setColor(new Color(0xFE, 0xF0, 0x8A));
            g2.fillOval(cx + r / 4, cy - r / 5, 8, 8);
        } else if (nameLower.contains("tôm") || nameLower.contains("hải sản")) {
            // Tôm nướng phô mai
            g2.setColor(new Color(0x28, 0x1E, 0x18));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            g2.setColor(new Color(0xEA, 0x58, 0x0C));
            g2.fillArc(cx - r + 8, cy - r / 2, r * 2 - 16, r + 4, 30, 200);
            // Phô mai nướng chảy
            g2.setColor(new Color(0xFE, 0xE2, 0xE2));
            g2.fillOval(cx - r / 4, cy - r / 6, r / 2, r / 3);
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx + r / 3, cy - r / 4, 5, 5);
        } else if (nameLower.contains("uống") || nameLower.contains("trà") || nameLower.contains("coca") || nameLower.contains("rượu")) {
            // Ly đồ uống / Cocktail cao cấp
            g2.setColor(new Color(0x1E, 0x29, 0x3B));
            g2.fillOval(cx - r / 2, cy + r / 2 - 4, r, 6);
            // Chân ly
            g2.setColor(new Color(0x94, 0xA3, 0xB8));
            g2.fillRect(cx - 1, cy - r / 4, 2, r * 3 / 4);
            // Thân ly chứa rượu vang / trà
            g2.setColor(new Color(0x99, 0x1B, 0x1B, 200));
            g2.fillArc(cx - r / 2, cy - r * 3 / 4, r, r, 0, 180);
            // Lát chanh vàng
            g2.setColor(new Color(0xFA, 0xCC, 0x15));
            g2.fillOval(cx + r / 4, cy - r * 3 / 4, 8, 8);
        } else {
            // Món ăn tổng hợp cao cấp
            g2.setColor(new Color(0x2A, 0x22, 0x1B));
            g2.fillOval(cx - r, cy - r + 4, r * 2, r * 2 - 8);
            g2.setColor(new Color(0xB4, 0x53, 0x09));
            g2.fillOval(cx - r + 8, cy - r + 10, r * 2 - 16, r * 2 - 20);
            g2.setColor(new Color(0xFE, 0xF3, 0xC7));
            g2.fillOval(cx - r / 3, cy - r / 4, 8, 8);
            g2.setColor(new Color(0x10, 0xB9, 0x81));
            g2.fillOval(cx + r / 4, cy + r / 4, 6, 6);
        }

        g2.dispose();
        CACHE.put(key, img);
        return img;
    }

    /**
     * Tạo hình ảnh bàn tiệc dạ tiệc ấm cúng (Dining Ambience)
     */
    public static BufferedImage getDiningTableBanner(int w, int h) {
        String key = "dining_banner_" + w + "_" + h;
        if (CACHE.containsKey(key)) return CACHE.get(key);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, new Color(0x1E, 0x17, 0x12), w, h, new Color(0x38, 0x2B, 0x20));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w, h, 8, 8);

        // Ánh nến vàng ấm lung linh
        g2.setColor(new Color(0xD4, 0xA3, 0x59, 45));
        g2.fillOval(w / 2 - 50, h / 2 - 25, 100, 50);

        // Icon bộ dao nĩa & ly rượu
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        g2.drawString("🍷 🕯️ 🍽️ 🥖", w / 2 - 32, h / 2 + 6);

        g2.dispose();
        CACHE.put(key, img);
        return img;
    }

    /**
     * Tạo avatar chân dung nhân viên cách điệu sang trọng
     */
    public static BufferedImage getStaffAvatar(String name, int size, Color bg) {
        String key = "avatar_" + name + "_" + size;
        if (CACHE.containsKey(key)) return CACHE.get(key);

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền tròn
        g2.setColor(bg != null ? bg : new Color(0x1E, 0x29, 0x3B));
        g2.fillOval(0, 0, size, size);

        // Viền vàng mỏng
        g2.setColor(new Color(0xD4, 0xA3, 0x59));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawOval(0, 0, size - 1, size - 1);

        // Viết chữ cái đầu (Initials)
        String initials = "NV";
        if (name != null && !name.trim().isEmpty()) {
            String[] parts = name.trim().split("\\s+");
            if (parts.length >= 2) {
                initials = ("" + parts[parts.length - 2].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
            } else {
                initials = name.substring(0, Math.min(2, name.length())).toUpperCase();
            }
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3 + 1));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (size - fm.stringWidth(initials)) / 2;
        int ty = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initials, tx, ty);

        g2.dispose();
        CACHE.put(key, img);
        return img;
    }
}
