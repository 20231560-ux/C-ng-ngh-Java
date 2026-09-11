package com.restaurant.view;

import java.awt.*;
import javax.swing.*;

public class RestaurantBanner extends JPanel {

    private Image backgroundImage;

    public RestaurantBanner() {

        setOpaque(false);

        backgroundImage = new ImageIcon(
                getClass().getResource("/images/background.png")
        ).getImage();

        setPreferredSize(new Dimension(1000, 170));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        int w = getWidth();
        int h = getHeight();

        int iw = backgroundImage.getWidth(null);
        int ih = backgroundImage.getHeight(null);

        double scale = Math.max(
                (double) w / iw,
                (double) h / ih
        );

        int nw = (int) (iw * scale);
        int nh = (int) (ih * scale);

        int x = (w - nw) / 2;
        int y = (h - nh) / 2;

        g2.drawImage(
                backgroundImage,
                x,
                y,
                nw,
                nh,
                this
        );

        // Lớp tối
        g2.setColor(new Color(5, 10, 15, 155));
        g2.fillRect(0, 0, w, h);

        // Viền vàng
        g2.setColor(new Color(200, 146, 69, 180));
        g2.drawRoundRect(
                1,
                1,
                w - 3,
                h - 3,
                20,
                20
        );

        // Tiêu đề
        g2.setColor(new Color(230, 180, 80));
        g2.setFont(new Font(
                "Serif",
                Font.BOLD,
                25
        ));

        g2.drawString(
                "SAVORÉ RESTAURANT",
                35,
                50
        );

        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                13
        ));

        g2.drawString(
                "Tinh hoa ẩm thực — Vận hành xuất sắc",
                37,
                78
        );

        g2.setColor(new Color(220, 180, 100));
        g2.setFont(new Font(
                "Serif",
                Font.ITALIC,
                18
        ));

        g2.drawString(
                "Good Food • Good People • Great Memories",
                37,
                115
        );

        g2.dispose();
    }
}