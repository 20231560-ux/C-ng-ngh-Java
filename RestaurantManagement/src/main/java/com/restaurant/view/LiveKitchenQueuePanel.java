package com.restaurant.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * SAVORÉ - LIVE KITCHEN QUEUE
 *
 * Hàng đợi món ăn đang chế biến.
 * Giao diện Dark Luxury đồng bộ với Operations Hub
 * và Revenue Chart.
 */
public class LiveKitchenQueuePanel extends JPanel {

    private final Consumer<String> onNavigate;

    private final List<KitchenOrder> items =
            new ArrayList<KitchenOrder>();

    private Timer animTimer;


    // =========================================================
    // MODEL
    // =========================================================

    public static class KitchenOrder {

        public final String dishName;
        public final String table;
        public final String chef;
        public final String icon;

        public int etaMinutes;

        public float progress;

        public float targetProgress;

        public final Color accentColor;


        public KitchenOrder(
                String dish,
                String table,
                String chef,
                String icon,
                int eta,
                float prog,
                Color color) {

            this.dishName = dish;
            this.table = table;
            this.chef = chef;
            this.icon = icon;
            this.etaMinutes = eta;
            this.progress = prog;
            this.targetProgress = prog;
            this.accentColor = color;
        }
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LiveKitchenQueuePanel(
            Consumer<String> onNavigate) {

        this.onNavigate = onNavigate;

        setOpaque(false);

        setLayout(
                new BorderLayout(
                        0,
                        7
                )
        );


        khoiTaoDuLieuMau();


        add(
                taoHeader(),
                BorderLayout.NORTH
        );


        add(
                taoDanhSach(),
                BorderLayout.CENTER
        );


        khoiDongAnimation();
    }


    // =========================================================
    // DATA MẪU
    // =========================================================

    private void khoiTaoDuLieuMau() {

        items.clear();


        items.add(
                new KitchenOrder(
                        "Bò Fuji Nướng Đá",
                        "Bàn 03",
                        "Chef Hùng",
                        "🥩",
                        3,
                        0.75f,
                        new Color(
                                239,
                                68,
                                68
                        )
                )
        );


        items.add(
                new KitchenOrder(
                        "Lẩu Hải Sản TomYum",
                        "Bàn 05",
                        "Chef Linh",
                        "🍲",
                        7,
                        0.45f,
                        new Color(
                                245,
                                158,
                                11
                        )
                )
        );


        items.add(
                new KitchenOrder(
                        "Cá Hồi Sốt Chanh Leo",
                        "Bàn 07",
                        "Chef Nam",
                        "🐟",
                        5,
                        0.60f,
                        new Color(
                                16,
                                185,
                                129
                        )
                )
        );


        items.add(
                new KitchenOrder(
                        "Súp Bào Ngư Thượng Hạng",
                        "VIP 01",
                        "Chef Tuấn",
                        "🥣",
                        2,
                        0.88f,
                        new Color(
                                200,
                                146,
                                69
                        )
                )
        );
    }


    // =========================================================
    // HEADER
    // =========================================================

    private JPanel taoHeader() {

        JPanel pnl =
                new JPanel(
                        new BorderLayout()
                );

        pnl.setOpaque(false);


        JLabel lblTitle =
                new JLabel(
                        "🔥 LIVE KITCHEN QUEUE"
                );


        lblTitle.setFont(
                SavoreDesignSystem.Fonts.get(
                        12,
                        Font.BOLD
                )
        );


        lblTitle.setForeground(
                SavoreDesignSystem
                        .Colors
                        .WARM_GOLD
        );


        JLabel lblBadge =
                new JLabel(
                        "● 4 món đang nấu"
                );


        lblBadge.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD
                )
        );


        lblBadge.setForeground(
                new Color(
                        16,
                        185,
                        129
                )
        );


        pnl.add(
                lblTitle,
                BorderLayout.WEST
        );


        pnl.add(
                lblBadge,
                BorderLayout.EAST
        );


        return pnl;
    }


    // =========================================================
    // DANH SÁCH
    // =========================================================

    private JPanel taoDanhSach() {

        JPanel container =
                new JPanel();


        container.setOpaque(false);


        container.setLayout(
                new BoxLayout(
                        container,
                        BoxLayout.Y_AXIS
                )
        );


        container.setBorder(
                BorderFactory.createEmptyBorder(
                        1,
                        0,
                        0,
                        0
                )
        );


        for (
                KitchenOrder ord
                : items
        ) {

            container.add(
                    taoKitchenItemRow(
                            ord
                    )
            );


            container.add(
                    Box.createRigidArea(
                            new Dimension(
                                    0,
                                    5
                            )
                    )
            );
        }


        return container;
    }


    // =========================================================
    // KITCHEN ITEM
    // =========================================================

    private JPanel taoKitchenItemRow(
            KitchenOrder ord) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                ) {

                    private boolean isHover =
                            false;


                    {
                        addMouseListener(
                                new MouseAdapter() {

                                    @Override
                                    public void mouseEntered(
                                            MouseEvent e) {

                                        isHover =
                                                true;


                                        setCursor(
                                                new Cursor(
                                                        Cursor.HAND_CURSOR
                                                )
                                        );


                                        repaint();
                                    }


                                    @Override
                                    public void mouseExited(
                                            MouseEvent e) {

                                        isHover =
                                                false;


                                        setCursor(Cursor.getDefaultCursor());


                                        repaint();
                                    }


                                    @Override
                                    public void mouseClicked(
                                            MouseEvent e) {

                                        if (
                                                onNavigate
                                                != null
                                        ) {

                                            onNavigate.accept(
                                                    "POS"
                                            );
                                        }
                                    }
                                }
                        );
                    }


                    @Override
                    protected void paintComponent(
                            Graphics g) {

                        super.paintComponent(g);


                        Graphics2D g2 =
                                (Graphics2D)
                                g.create();


                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );


                        int w =
                                getWidth();

                        int h =
                                getHeight();


                        // =========================================
                        // NỀN CARD
                        // =========================================

                        GradientPaint bg =
                                new GradientPaint(
                                        0,
                                        0,
                                        isHover
                                                ? new Color(
                                                        29,
                                                        38,
                                                        50,
                                                        245
                                                )
                                                : new Color(
                                                        18,
                                                        27,
                                                        38,
                                                        235
                                                ),
                                        w,
                                        h,
                                        new Color(
                                                7,
                                                14,
                                                21,
                                                245
                                        )
                                );


                        g2.setPaint(bg);


                        g2.fillRoundRect(
                                0,
                                0,
                                w,
                                h,
                                12,
                                12
                        );


                        // =========================================
                        // VIỀN VÀNG
                        // =========================================

                        g2.setColor(
                                isHover
                                        ? new Color(
                                                229,
                                                183,
                                                102,
                                                170
                                        )
                                        : new Color(
                                                200,
                                                146,
                                                69,
                                                65
                                        )
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        isHover
                                                ? 1.3f
                                                : 0.8f
                                )
                        );


                        g2.drawRoundRect(
                                0,
                                0,
                                w - 1,
                                h - 1,
                                12,
                                12
                        );


                        // =========================================
                        // PROGRESS BAR BACKGROUND
                        // =========================================

                        int barX = 9;

                        int barY =
                                h - 6;

                        int barW =
                                w - 18;


                        g2.setColor(
                                new Color(
                                        30,
                                        41,
                                        59,
                                        210
                                )
                        );


                        g2.fillRoundRect(
                                barX,
                                barY,
                                barW,
                                3,
                                3,
                                3
                        );


                        // =========================================
                        // PROGRESS
                        // =========================================

                        int fillW =
                                (int)
                                (
                                        barW
                                        * ord.progress
                                );


                        if (
                                fillW > 0
                        ) {

                            g2.setColor(
                                    ord.accentColor
                            );


                            g2.fillRoundRect(
                                    barX,
                                    barY,
                                    fillW,
                                    3,
                                    3,
                                    3
                            );


                            // glow

                            g2.setColor(
                                    new Color(
                                            ord.accentColor.getRed(),
                                            ord.accentColor.getGreen(),
                                            ord.accentColor.getBlue(),
                                            45
                                    )
                            );


                            g2.fillRoundRect(
                                    barX,
                                    barY - 2,
                                    fillW,
                                    7,
                                    4,
                                    4
                            );
                        }


                        g2.dispose();
                    }
                };


        row.setOpaque(false);


        row.setBorder(
                new EmptyBorder(
                        7,
                        8,
                        9,
                        8
                )
        );


        row.setPreferredSize(
                new Dimension(
                        280,
                        55
                )
        );


        row.setMinimumSize(
                new Dimension(
                        150,
                        55
                )
        );


        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        55
                )
        );


        // =========================================================
        // ICON
        // =========================================================

        JLabel lblIcon =
                new JLabel(
                        ord.icon
                );


        lblIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        17
                )
        );


        lblIcon.setPreferredSize(
                new Dimension(
                        31,
                        31
                )
        );


        row.add(
                lblIcon,
                BorderLayout.WEST
        );


        // =========================================================
        // CENTER
        // =========================================================

        JPanel center =
                new JPanel();


        center.setOpaque(false);


        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel lblName =
                new JLabel(
                        ord.dishName
                );


        lblName.setFont(
                SavoreDesignSystem.Fonts.get(
                        10,
                        Font.BOLD
                )
        );


        lblName.setForeground(
                Color.WHITE
        );


        JLabel lblSub =
                new JLabel(
                        ord.table
                        + " • "
                        + ord.chef
                );


        lblSub.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.PLAIN
                )
        );


        lblSub.setForeground(
                new Color(
                        148,
                        163,
                        184
                )
        );


        center.add(
                lblName
        );


        center.add(
                lblSub
        );


        row.add(
                center,
                BorderLayout.CENTER
        );


        // =========================================================
        // ETA
        // =========================================================

        JLabel lblEta =
                new JLabel(
                        "ETA "
                        + ord.etaMinutes
                        + "p"
                );


        lblEta.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD
                )
        );


        lblEta.setForeground(
                ord.accentColor
        );


        row.add(
                lblEta,
                BorderLayout.EAST
        );


        return row;
    }


    // =========================================================
    // ANIMATION
    // =========================================================

    private void khoiDongAnimation() {

        if (
                animTimer != null
        ) {

            animTimer.stop();
        }


        animTimer =
                new Timer(
                        40,
                        e -> {

                            boolean changed =
                                    false;


                            for (
                                    KitchenOrder ord
                                    : items
                            ) {

                                if (
                                        Math.abs(
                                                ord.progress
                                                - ord.targetProgress
                                        )
                                        > 0.001f
                                ) {

                                    ord.progress +=
                                            (
                                                    ord.targetProgress
                                                    - ord.progress
                                            )
                                            * 0.08f;


                                    changed =
                                            true;
                                }
                            }


                            if (changed) {

                                repaint();
                            }
                        }
                );


        animTimer.start();
    }


    // =========================================================
    // SIZE
    // =========================================================

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(
                360,
                245
        );
    }
}