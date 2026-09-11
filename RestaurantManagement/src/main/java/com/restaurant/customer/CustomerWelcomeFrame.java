package com.restaurant.customer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CustomerWelcomeFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color CREAM = new Color(247, 242, 232);
    private final Color DARK = new Color(48, 40, 34);
    private final Color GOLD = new Color(176, 132, 72);
    private final Color GOLD_DARK = new Color(139, 99, 48);
    private final Color WHITE = new Color(255, 253, 248);

    private String maBan = "B08";

    public CustomerWelcomeFrame() {
        khoiTao();
    }

    public CustomerWelcomeFrame(String maBan) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan;
        khoiTao();
    }

    private void khoiTao() {
        setTitle("Savoré - Chào mừng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430, 820);
        setMinimumSize(new Dimension(390, 720));
        setLocationRelativeTo(null);
        setResizable(false);

        GradientPanel background = new GradientPanel();
        background.setLayout(new BorderLayout());
        setContentPane(background);

        JPanel top = taoHeader();
        JPanel center = taoNoiDung();
        JPanel bottom = taoBottom();

        background.add(top, BorderLayout.NORTH);
        background.add(center, BorderLayout.CENTER);
        background.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel taoHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 10, 28));

        JLabel logo = new JLabel("SAVORÉ");
        logo.setForeground(DARK);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel subtitle = new JLabel("RESTAURANT & DINING");
        subtitle.setForeground(new Color(125, 108, 88));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 9));

        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BorderLayout(0, 2));
        logoPanel.add(logo, BorderLayout.NORTH);
        logoPanel.add(subtitle, BorderLayout.CENTER);

        JLabel tableLabel = new JLabel("BÀN " + maBan);
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableLabel.setForeground(GOLD_DARK);
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(195, 169, 126), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));

        panel.add(logoPanel, BorderLayout.WEST);
        panel.add(tableLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel taoNoiDung() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JPanel hero = new FoodHeroPanel();
        hero.setPreferredSize(new Dimension(360, 280));
        gbc.gridy = 0;
        gbc.weighty = 0;
        container.add(hero, gbc);

        JLabel welcome = new JLabel("CHÀO MỪNG ĐẾN VỚI", SwingConstants.CENTER);
        welcome.setForeground(new Color(119, 99, 75));
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 1;
        container.add(welcome, gbc);

        JLabel brand = new JLabel("SAVORÉ", SwingConstants.CENTER);
        brand.setForeground(DARK);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridy = 2;
        container.add(brand, gbc);

        JLabel line = new JLabel(
                "<html><div style='text-align:center;'>Trải nghiệm ẩm thực tinh tế<br>"
                + "được phục vụ riêng cho bạn.</div></html>",
                SwingConstants.CENTER
        );
        line.setForeground(new Color(111, 96, 79));
        line.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        container.add(line, gbc);

        JPanel tableCard = taoTableCard();
        gbc.gridy = 4;
        gbc.insets = new Insets(18, 0, 10, 0);
        container.add(tableCard, gbc);

        return container;
    }

    private JPanel taoTableCard() {
        RoundedPanel card = new RoundedPanel(22, WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel small = new JLabel("BẠN ĐANG NGỒI TẠI");
        small.setForeground(new Color(141, 119, 91));
        small.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JLabel table = new JLabel("BÀN " + maBan);
        table.setForeground(DARK);
        table.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel text = new JPanel(new BorderLayout(0, 3));
        text.setOpaque(false);
        text.add(small, BorderLayout.NORTH);
        text.add(table, BorderLayout.CENTER);

        JLabel status = new JLabel("● ĐÃ KẾT NỐI");
        status.setForeground(new Color(82, 119, 78));
        status.setFont(new Font("Segoe UI", Font.BOLD, 10));

        card.add(text, BorderLayout.WEST);
        card.add(status, BorderLayout.EAST);

        return card;
    }

    private JPanel taoBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 28, 30, 28));

        JButton btnMenu = new JButton("XEM THỰC ĐƠN");
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMenu.setFocusPainted(false);
        btnMenu.setBorderPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        RoundedButtonPanel buttonPanel = new RoundedButtonPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(360, 58));
        buttonPanel.add(btnMenu, BorderLayout.CENTER);

        btnMenu.addActionListener(e -> moThucDon());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(buttonPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel("Quét QR • Chọn món • Gửi gọi món", SwingConstants.CENTER);
        footer.setForeground(new Color(143, 126, 105));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        bottom.add(wrapper, BorderLayout.NORTH);
        bottom.add(footer, BorderLayout.SOUTH);

        return bottom;
    }

    private void moThucDon() {
        JOptionPane.showMessageDialog(
                this,
                "Bàn " + maBan + " đã sẵn sàng.\nGiao diện 2 - Thực đơn sẽ được kết nối ở bước tiếp theo.",
                "Savoré",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerWelcomeFrame frame = new CustomerWelcomeFrame("B08");
            frame.setVisible(true);
        });
    }

    private class GradientPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    new Color(252, 248, 239),
                    0,
                    getHeight(),
                    CREAM
            );

            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class FoodHeroPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public FoodHeroPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(
                    0,
                    0,
                    new Color(93, 70, 49),
                    w,
                    h,
                    new Color(181, 139, 82)
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 30, 30);

            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(w - 130, 25, 100, 100);

            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(25, h - 115, 130, 130);

            g2.setColor(new Color(255, 249, 232, 235));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(18, 18, w - 37, h - 37, 24, 24);

            int cx = w / 2;
            int cy = h / 2 - 5;

            g2.setColor(new Color(245, 232, 203));
            g2.fillOval(cx - 87, cy - 87, 174, 174);

            g2.setColor(new Color(255, 250, 239));
            g2.fillOval(cx - 70, cy - 70, 140, 140);

            g2.setColor(new Color(128, 77, 42));
            g2.fillOval(cx - 48, cy - 45, 96, 90);

            g2.setColor(new Color(203, 119, 59));
            g2.fillOval(cx - 37, cy - 35, 74, 70);

            g2.setColor(new Color(232, 174, 86));
            g2.fillOval(cx - 22, cy - 22, 44, 42);

            g2.setColor(new Color(103, 72, 41));
            g2.fillOval(cx - 9, cy - 9, 18, 18);

            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            String text = "WELCOME TO SAVORÉ";
            int tw = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, cx - tw / 2, h - 45);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private final int radius;
        private final Color background;

        public RoundedPanel(int radius, Color background) {
            this.radius = radius;
            this.background = background;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private class RoundedButtonPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public RoundedButtonPanel() {
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            boolean hover = getMousePosition() != null;

            g2.setColor(hover ? GOLD_DARK : GOLD);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    18,
                    18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}