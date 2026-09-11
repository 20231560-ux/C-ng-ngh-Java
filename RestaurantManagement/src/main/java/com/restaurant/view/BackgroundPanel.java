package com.restaurant.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    public BackgroundPanel() {

        backgroundImage = new ImageIcon(
                getClass().getResource("/images/background.png")
        ).getImage();

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imageWidth = backgroundImage.getWidth(null);
        int imageHeight = backgroundImage.getHeight(null);

        double scale = Math.max(
                (double) panelWidth / imageWidth,
                (double) panelHeight / imageHeight
        );

        int newWidth = (int) (imageWidth * scale);
        int newHeight = (int) (imageHeight * scale);

        int x = (panelWidth - newWidth) / 2;
        int y = (panelHeight - newHeight) / 2;

        // Vẽ ảnh nền
        g2.drawImage(
                backgroundImage,
                x,
                y,
                newWidth,
                newHeight,
                this
        );

        // Làm tối ảnh nền để giao diện phía trên nổi bật
        g2.setColor(new java.awt.Color(5, 12, 18, 140));
        g2.fillRect(0, 0, panelWidth, panelHeight);

        g2.dispose();
    }
}