package com.restaurant.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class CustomerFoodDetailFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color CREAM = new Color(248, 244, 235);
    private final Color WHITE = new Color(255, 253, 248);
    private final Color DARK = new Color(47, 39, 33);
    private final Color GOLD = new Color(178, 135, 75);
    private final Color GOLD_DARK = new Color(139, 98, 48);
    private final Color TEXT_LIGHT = new Color(125, 107, 87);
    private final Color BORDER = new Color(226, 215, 197);

    private String maBan = "B08";
    private String tenMon = "Bò lúc lắc Savoré";
    private String gia = "189.000đ";
    private String danhMuc = "MÓN CHÍNH";
    private String moTa = "Bò mềm thơm kết hợp rau củ và sốt đặc biệt của Savoré.";
    private String danhGia = "4.9";

    private int soLuong = 1;
    private JLabel lblSoLuong;
    private JTextArea txtGhiChu;

    public CustomerFoodDetailFrame() {
        khoiTao();
    }

    public CustomerFoodDetailFrame(String maBan) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan;
        khoiTao();
    }

    public CustomerFoodDetailFrame(
            String maBan,
            String tenMon,
            String gia,
            String danhMuc,
            String moTa,
            String danhGia,
            int hinh
    ) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan;
        this.tenMon = tenMon;
        this.gia = gia;
        this.danhMuc = danhMuc;
        this.moTa = moTa;
        this.danhGia = danhGia;
        khoiTao();
    }

    private void khoiTao() {
        setTitle("Savoré - Chi tiết món");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430, 820);
        setMinimumSize(new Dimension(430, 820));
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);
        setContentPane(root);

        root.add(taoHeader(), BorderLayout.NORTH);
        root.add(taoNoiDung(), BorderLayout.CENTER);
        root.add(taoBottom(), BorderLayout.SOUTH);
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(13, 18, 13, 18)
        ));

        JButton back = new JButton("QUAY LẠI");
        back.setForeground(DARK);
        back.setFont(new Font("Segoe UI", Font.BOLD, 10));
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 10));
        back.setContentAreaFilled(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        back.addActionListener(e -> dispose());

        JLabel title = new JLabel("CHI TIẾT MÓN", SwingConstants.CENTER);
        title.setForeground(DARK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel ban = new JLabel("BÀN " + maBan, SwingConstants.RIGHT);
        ban.setForeground(TEXT_LIGHT);
        ban.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        ban.setPreferredSize(new Dimension(55, 25));

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(ban, BorderLayout.EAST);

        return header;
    }

    private JPanel taoNoiDung() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CREAM);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(14, 22, 20, 22));

        FoodImagePanel image = new FoodImagePanel();
        image.setAlignmentX(LEFT_ALIGNMENT);
        image.setPreferredSize(new Dimension(386, 235));
        image.setMaximumSize(new Dimension(Integer.MAX_VALUE, 235));
        image.setMinimumSize(new Dimension(360, 235));

        body.add(image);
        body.add(Box.createVerticalStrut(12));

        JLabel category = new JLabel(danhMuc);
        category.setForeground(GOLD_DARK);
        category.setFont(new Font("Segoe UI", Font.BOLD, 9));
        category.setAlignmentX(LEFT_ALIGNMENT);

        body.add(category);
        body.add(Box.createVerticalStrut(4));

        JLabel name = new JLabel(tenMon);
        name.setForeground(DARK);
        name.setFont(new Font("Segoe UI", Font.BOLD, 25));
        name.setAlignmentX(LEFT_ALIGNMENT);

        body.add(name);
        body.add(Box.createVerticalStrut(4));

        JPanel priceRating = new JPanel(new BorderLayout());
        priceRating.setOpaque(false);
        priceRating.setAlignmentX(LEFT_ALIGNMENT);
        priceRating.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel price = new JLabel(gia);
        price.setForeground(GOLD_DARK);
        price.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel rating = new JLabel("DANH GIA  " + danhGia);
        rating.setForeground(TEXT_LIGHT);
        rating.setFont(new Font("Segoe UI", Font.BOLD, 10));

        priceRating.add(price, BorderLayout.WEST);
        priceRating.add(rating, BorderLayout.EAST);

        body.add(priceRating);
        body.add(Box.createVerticalStrut(4));

        JLabel description = new JLabel(
                "<html><div style='width:380px;'>" + moTa + "</div></html>"
        );
        description.setForeground(TEXT_LIGHT);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        description.setAlignmentX(LEFT_ALIGNMENT);
        description.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        body.add(description);
        body.add(Box.createVerticalStrut(10));

        JLabel quantityTitle = new JLabel("SỐ LƯỢNG");
        quantityTitle.setForeground(DARK);
        quantityTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        quantityTitle.setAlignmentX(LEFT_ALIGNMENT);

        body.add(quantityTitle);
        body.add(Box.createVerticalStrut(5));

        JPanel quantity = taoSoLuong();
        quantity.setAlignmentX(LEFT_ALIGNMENT);

        body.add(quantity);
        body.add(Box.createVerticalStrut(10));

        JLabel noteTitle = new JLabel("GHI CHÚ CHO NHÀ BẾP");
        noteTitle.setForeground(DARK);
        noteTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        noteTitle.setAlignmentX(LEFT_ALIGNMENT);

        body.add(noteTitle);
        body.add(Box.createVerticalStrut(5));

        txtGhiChu = new JTextArea();
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setForeground(DARK);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtGhiChu.setBackground(WHITE);
        txtGhiChu.setBorder(BorderFactory.createEmptyBorder(9, 11, 9, 11));

        JScrollPane noteScroll = new JScrollPane(txtGhiChu);
        noteScroll.setAlignmentX(LEFT_ALIGNMENT);
        noteScroll.setPreferredSize(new Dimension(386, 62));
        noteScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        noteScroll.setMinimumSize(new Dimension(360, 62));
        noteScroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        noteScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        noteScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER
        );

        body.add(noteScroll);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scroll, BorderLayout.CENTER);

        return content;
    }

    private JPanel taoSoLuong() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 0, 0));
        panel.setBackground(WHITE);
        panel.setPreferredSize(new Dimension(386, 50));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setMinimumSize(new Dimension(360, 50));
        panel.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JButton minus = new JButton("-");
        minus.setFocusPainted(false);
        minus.setBorderPainted(false);
        minus.setContentAreaFilled(true);
        minus.setBackground(new Color(238, 230, 216));
        minus.setForeground(DARK);
        minus.setFont(new Font("Segoe UI", Font.BOLD, 20));
        minus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        minus.addActionListener(e -> {
            if (soLuong > 1) {
                soLuong--;
                capNhatSoLuong();
            }
        });

        lblSoLuong = new JLabel("1", SwingConstants.CENTER);
        lblSoLuong.setOpaque(true);
        lblSoLuong.setBackground(WHITE);
        lblSoLuong.setForeground(DARK);
        lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 17));

        JButton plus = new JButton("+");
        plus.setFocusPainted(false);
        plus.setBorderPainted(false);
        plus.setContentAreaFilled(true);
        plus.setBackground(GOLD);
        plus.setForeground(Color.WHITE);
        plus.setFont(new Font("Segoe UI", Font.BOLD, 20));
        plus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        plus.addActionListener(e -> {
            if (soLuong < 99) {
                soLuong++;
                capNhatSoLuong();
            }
        });

        panel.add(minus);
        panel.add(lblSoLuong);
        panel.add(plus);

        return panel;
    }

    private JPanel taoBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(WHITE);
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(10, 22, 14, 22)
        ));

        JButton add = new JButton("THÊM VÀO GIỎ");
        add.setForeground(Color.WHITE);
        add.setBackground(GOLD);
        add.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add.setFocusPainted(false);
        add.setBorderPainted(false);
        add.setPreferredSize(new Dimension(386, 50));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add.addActionListener(e -> themVaoGio());

        bottom.add(add, BorderLayout.CENTER);

        return bottom;
    }

    private void capNhatSoLuong() {
        lblSoLuong.setText(String.valueOf(soLuong));
    }

    private void themVaoGio() {
        String ghiChu = txtGhiChu == null ? "" : txtGhiChu.getText().trim();

        System.out.println("Món: " + tenMon);
        System.out.println("Số lượng: " + soLuong);
        System.out.println("Ghi chú: " + ghiChu);

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerFoodDetailFrame frame =
                    new CustomerFoodDetailFrame("B08");
            frame.setVisible(true);
        });
    }

    private class FoodImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint gradient = new GradientPaint(
                    0,
                    0,
                    new Color(91, 65, 44),
                    w,
                    h,
                    new Color(194, 146, 78)
            );

            g2.setPaint(gradient);
            g2.fillRoundRect(
                    0,
                    0,
                    w - 1,
                    h - 1,
                    24,
                    24
            );

            g2.setColor(new Color(255, 249, 229, 220));
            g2.fillOval(
                    w / 2 - 88,
                    h / 2 - 88,
                    176,
                    176
            );

            g2.setColor(new Color(255, 255, 255, 235));
            g2.fillOval(
                    w / 2 - 72,
                    h / 2 - 72,
                    144,
                    144
            );

            g2.setColor(new Color(130, 71, 35));
            g2.fillOval(
                    w / 2 - 52,
                    h / 2 - 48,
                    104,
                    96
            );

            g2.setColor(new Color(211, 126, 50));
            g2.fillOval(
                    w / 2 - 40,
                    h / 2 - 36,
                    80,
                    72
            );

            g2.setColor(new Color(229, 171, 78));
            g2.fillOval(
                    w / 2 - 17,
                    h / 2 - 16,
                    34,
                    32
            );

            g2.setColor(new Color(82, 60, 40));
            g2.fillOval(
                    w / 2 - 9,
                    h / 2 - 9,
                    18,
                    18
            );

            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));

            String text = "SAVORÉ";
            int tw = g2.getFontMetrics().stringWidth(text);

            g2.drawString(
                    text,
                    w / 2 - tw / 2,
                    h - 24
            );

            g2.dispose();
        }
    }
}