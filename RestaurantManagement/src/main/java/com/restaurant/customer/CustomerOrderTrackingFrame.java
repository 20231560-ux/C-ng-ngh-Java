package com.restaurant.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class CustomerOrderTrackingFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Color CREAM = new Color(248, 244, 235);
    private final Color WHITE = new Color(255, 253, 248);
    private final Color DARK = new Color(47, 39, 33);
    private final Color GOLD = new Color(178, 135, 75);
    private final Color GOLD_DARK = new Color(139, 98, 48);
    private final Color TEXT_LIGHT = new Color(125, 107, 87);
    private final Color BORDER = new Color(226, 215, 197);
    private final Color SUCCESS = new Color(91, 112, 73);

    private String maBan = "B08";
    private String maDon = "SV20260001";

    private int trangThai = 1;

    private JLabel lblTrangThai;
    private JPanel timelinePanel;

    public CustomerOrderTrackingFrame() {
        khoiTao();
    }

    public CustomerOrderTrackingFrame(String maBan, String maDon) {
        this.maBan = maBan == null || maBan.trim().isEmpty()
                ? "B08"
                : maBan;

        this.maDon = maDon == null || maDon.trim().isEmpty()
                ? "SV20260001"
                : maDon;

        khoiTao();
    }

    private void khoiTao() {
        setTitle("Savoré - Theo dõi đơn hàng");
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
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, BORDER
                ),
                BorderFactory.createEmptyBorder(
                        13, 18, 13, 18
                )
        ));

        JButton back = new JButton("QUAY LẠI");
        back.setForeground(DARK);
        back.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                10
        ));
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(
                7, 0, 7, 10
        ));
        back.setContentAreaFilled(false);
        back.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        ));

        back.addActionListener(e -> dispose());

        JLabel title = new JLabel(
                "THEO DÕI ĐƠN",
                SwingConstants.CENTER
        );
        title.setForeground(DARK);
        title.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                15
        ));

        JLabel ban = new JLabel(
                "BÀN " + maBan,
                SwingConstants.RIGHT
        );
        ban.setForeground(TEXT_LIGHT);
        ban.setFont(new Font(
                "Segoe UI",
                Font.PLAIN,
                9
        ));
        ban.setPreferredSize(new Dimension(
                55,
                25
        ));

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
        body.setLayout(new BoxLayout(
                body,
                BoxLayout.Y_AXIS
        ));
        body.setBorder(new EmptyBorder(
                15, 22, 20, 22
        ));

        JPanel orderHeader = taoThongTinDon();

        orderHeader.setAlignmentX(
                LEFT_ALIGNMENT
        );

        body.add(orderHeader);
        body.add(Box.createVerticalStrut(15));

        JLabel statusTitle = new JLabel(
                "TRẠNG THÁI ĐƠN HÀNG"
        );
        statusTitle.setForeground(DARK);
        statusTitle.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                11
        ));
        statusTitle.setAlignmentX(
                LEFT_ALIGNMENT
        );

        body.add(statusTitle);
        body.add(Box.createVerticalStrut(8));

        timelinePanel = new JPanel();
        timelinePanel.setOpaque(false);
        timelinePanel.setLayout(new BoxLayout(
                timelinePanel,
                BoxLayout.Y_AXIS
        ));
        timelinePanel.setAlignmentX(
                LEFT_ALIGNMENT
        );

        capNhatTimeline();

        body.add(timelinePanel);
        body.add(Box.createVerticalStrut(12));

        JLabel foodTitle = new JLabel(
                "MÓN TRONG ĐƠN"
        );
        foodTitle.setForeground(DARK);
        foodTitle.setFont(new Font(
                "Segoe UI",
                Font.BOLD,
                11
        ));
        foodTitle.setAlignmentX(
                LEFT_ALIGNMENT
        );

        body.add(foodTitle);
        body.add(Box.createVerticalStrut(7));

        JPanel foods = taoDanhSachMon();
        foods.setAlignmentX(
                LEFT_ALIGNMENT
        );

        body.add(foods);

        JScrollPane scroll = new JScrollPane(
                body
        );

        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        scroll.getVerticalScrollBar().setUnitIncrement(
                16
        );

        content.add(
                scroll,
                BorderLayout.CENTER
        );

        return content;
    }

    private JPanel taoThongTinDon() {
        RoundedPanel card = new RoundedPanel(
                18,
                WHITE
        );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        14,
                        16,
                        14,
                        16
                )
        );

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel small = new JLabel(
                "ĐƠN HÀNG"
        );
        small.setForeground(
                GOLD_DARK
        );
        small.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        9
                )
        );

        JLabel code = new JLabel(
                "#" + maDon
        );
        code.setForeground(DARK);
        code.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        JLabel table = new JLabel(
                "Bàn " + maBan
        );
        table.setForeground(
                TEXT_LIGHT
        );
        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        left.add(small);
        left.add(
                Box.createVerticalStrut(3)
        );
        left.add(code);
        left.add(
                Box.createVerticalStrut(2)
        );
        left.add(table);

        lblTrangThai = new JLabel(
                "ĐÃ GỬI ĐƠN"
        );
        lblTrangThai.setForeground(
                SUCCESS
        );
        lblTrangThai.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );
        lblTrangThai.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        card.add(
                left,
                BorderLayout.WEST
        );

        card.add(
                lblTrangThai,
                BorderLayout.EAST
        );

        return card;
    }

    private void capNhatTimeline() {
        if (timelinePanel == null) {
            return;
        }

        timelinePanel.removeAll();

        String[] titles = {
                "Đã gửi đơn",
                "Bếp đã nhận",
                "Đang chế biến",
                "Đang phục vụ",
                "Hoàn thành"
        };

        String[] descriptions = {
                "Nhà hàng đã nhận yêu cầu gọi món.",
                "Bếp đã xác nhận và bắt đầu xử lý.",
                "Món ăn đang được chuẩn bị.",
                "Nhân viên đang mang món ra bàn.",
                "Đơn hàng đã hoàn thành."
        };

        for (int i = 0; i < titles.length; i++) {
            JPanel item = taoTimelineItem(
                    i,
                    titles[i],
                    descriptions[i]
            );

            item.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            timelinePanel.add(item);

            if (i < titles.length - 1) {
                JPanel line = new JPanel();
                line.setPreferredSize(
                        new Dimension(
                                2,
                                22
                        )
                );
                line.setMaximumSize(
                        new Dimension(
                                2,
                                22
                        )
                );

                if (i < trangThai) {
                    line.setBackground(
                            GOLD
                    );
                } else {
                    line.setBackground(
                            BORDER
                    );
                }

                line.setAlignmentX(
                        0.0f
                );

                JPanel lineWrap =
                        new JPanel(
                                new BorderLayout()
                        );

                lineWrap.setOpaque(false);
                lineWrap.setPreferredSize(
                        new Dimension(
                                24,
                                22
                        )
                );
                lineWrap.setMaximumSize(
                        new Dimension(
                                24,
                                22
                        )
                );
                lineWrap.add(
                        line,
                        BorderLayout.CENTER
                );

                timelinePanel.add(
                        lineWrap
                );
            }
        }

        timelinePanel.revalidate();
        timelinePanel.repaint();

        capNhatTrangThai();
    }

    private JPanel taoTimelineItem(
            int index,
            String title,
            String description
    ) {
        JPanel row = new JPanel(
                new BorderLayout(
                        10,
                        0
                )
        );

        row.setOpaque(false);
        row.setPreferredSize(
                new Dimension(
                        386,
                        55
                )
        );
        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        55
                )
        );

        JLabel circle = new JLabel();

        boolean completed =
                index < trangThai;

        boolean current =
                index == trangThai;

        if (completed) {
            circle.setText(
                    "OK"
            );
            circle.setForeground(
                    Color.WHITE
            );
            circle.setBackground(
                    GOLD
            );
        } else if (current) {
            circle.setText(
                    "..."
            );
            circle.setForeground(
                    Color.WHITE
            );
            circle.setBackground(
                    GOLD_DARK
            );
        } else {
            circle.setText(
                    "O"
            );
            circle.setForeground(
                    TEXT_LIGHT
            );
            circle.setBackground(
                    new Color(
                            238,
                            230,
                            216
                    )
            );
        }

        circle.setOpaque(true);
        circle.setHorizontalAlignment(
                SwingConstants.CENTER
        );
        circle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        8
                )
        );
        circle.setPreferredSize(
                new Dimension(
                        28,
                        28
                )
        );

        JPanel circleWrap =
                new JPanel(
                        new BorderLayout()
                );

        circleWrap.setOpaque(false);
        circleWrap.setPreferredSize(
                new Dimension(
                        28,
                        40
                )
        );

        circleWrap.add(
                circle,
                BorderLayout.NORTH
        );

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(
                completed || current
                        ? DARK
                        : TEXT_LIGHT
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setForeground(
                TEXT_LIGHT
        );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        text.add(titleLabel);
        text.add(
                Box.createVerticalStrut(2)
        );
        text.add(descriptionLabel);

        row.add(
                circleWrap,
                BorderLayout.WEST
        );

        row.add(
                text,
                BorderLayout.CENTER
        );

        return row;
    }

    private JPanel taoDanhSachMon() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.add(
                taoMon(
                        "Bò lúc lắc Savoré",
                        "189.000đ",
                        "x1"
                )
        );

        panel.add(
                Box.createVerticalStrut(7)
        );

        panel.add(
                taoMon(
                        "Trà đào Savoré",
                        "59.000đ",
                        "x2"
                )
        );

        return panel;
    }

    private JPanel taoMon(
            String ten,
            String gia,
            String soLuong
    ) {
        RoundedPanel card =
                new RoundedPanel(
                        14,
                        WHITE
                );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        JLabel name =
                new JLabel(ten);

        name.setForeground(DARK);
        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        JLabel price =
                new JLabel(gia);

        price.setForeground(
                GOLD_DARK
        );

        price.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JLabel quantity =
                new JLabel(
                        soLuong,
                        SwingConstants.RIGHT
                );

        quantity.setForeground(
                TEXT_LIGHT
        );

        quantity.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);
        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.add(name);
        left.add(
                Box.createVerticalStrut(4)
        );
        left.add(price);

        card.add(
                left,
                BorderLayout.CENTER
        );

        card.add(
                quantity,
                BorderLayout.EAST
        );

        return card;
    }

    private JPanel taoBottom() {
        JPanel bottom =
                new JPanel();

        bottom.setBackground(
                WHITE
        );

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                22,
                                14,
                                22
                        )
                )
        );

        JButton add =
                new JButton(
                        "GỌI THÊM MÓN"
                );

        add.setForeground(
                Color.WHITE
        );

        add.setBackground(
                GOLD
        );

        add.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        add.setFocusPainted(false);
        add.setBorderPainted(false);

        add.setPreferredSize(
                new Dimension(
                        386,
                        50
                )
        );

        add.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        add.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        add.addActionListener(
                e -> moMenu()
        );

        bottom.add(add);

        return bottom;
    }

    private void capNhatTrangThai() {
        if (lblTrangThai == null) {
            return;
        }

        String[] names = {
                "ĐÃ GỬI ĐƠN",
                "BẾP ĐÃ NHẬN",
                "ĐANG CHẾ BIẾN",
                "ĐANG PHỤC VỤ",
                "HOÀN THÀNH"
        };

        lblTrangThai.setText(
                names[trangThai]
        );
    }

    private void moMenu() {
        CustomerMenuFrame menu =
                new CustomerMenuFrame(
                        maBan
                );

        menu.setVisible(true);
    }

    public void setTrangThai(
            int trangThaiMoi
    ) {
        if (trangThaiMoi < 0) {
            trangThaiMoi = 0;
        }

        if (trangThaiMoi > 4) {
            trangThaiMoi = 4;
        }

        this.trangThai =
                trangThaiMoi;

        capNhatTimeline();
    }

    public static void main(
            String[] args
    ) {
        SwingUtilities.invokeLater(
                () -> {
                    CustomerOrderTrackingFrame frame =
                            new CustomerOrderTrackingFrame(
                                    "B08",
                                    "SV20260001"
                            );

                    frame.setVisible(true);
                }
        );
    }

    private class RoundedPanel
            extends JPanel {

        private static final long serialVersionUID = 1L;

        private final int radius;
        private final Color background;

        RoundedPanel(
                int radius,
                Color background
        ) {
            this.radius = radius;
            this.background = background;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    background
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}