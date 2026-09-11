package com.restaurant.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.border.EmptyBorder;


/**
 * SAVORÉ - ACTIVITY TIMELINE
 *
 * Dòng thời gian vận hành.
 *
 * Dark Luxury:
 * - nền xanh đen
 * - card kính
 * - viền vàng đồng
 * - node phát sáng
 * - thời gian màu vàng
 */
public class ActivityTimelinePanel extends JPanel {

    private final Consumer<String> onNavigate;

    private final List<TimelineEvent> events =
            new ArrayList<TimelineEvent>();


    // =========================================================
    // MODEL
    // =========================================================

    public static class TimelineEvent {

        public final String time;

        public final String title;

        public final String detail;

        public final String icon;

        public final Color color;

        public final String targetModule;


        public TimelineEvent(
                String time,
                String title,
                String detail,
                String icon,
                Color color,
                String target) {

            this.time = time;
            this.title = title;
            this.detail = detail;
            this.icon = icon;
            this.color = color;
            this.targetModule = target;
        }
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ActivityTimelinePanel(
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
                taoTimelineContainer(),
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // DATA
    // =========================================================

    private void khoiTaoDuLieuMau() {

        events.clear();


        events.add(
                new TimelineEvent(
                        "08:45",
                        "Bàn 05 đã được đặt",
                        "Nguyễn Minh Hoàng – 4 khách",
                        "📅",
                        new Color(
                                59,
                                130,
                                246
                        ),
                        "DAT_BAN"
                )
        );


        events.add(
                new TimelineEvent(
                        "08:42",
                        "Đơn #DH1023 đã gửi bếp",
                        "Bàn 03 – 6 món khai vị & chính",
                        "🔥",
                        new Color(
                                245,
                                158,
                                11
                        ),
                        "POS"
                )
        );


        events.add(
                new TimelineEvent(
                        "08:38",
                        "Khách VIP check-in",
                        "Trần Thị Mai – Hạng Kim Cương",
                        "👑",
                        new Color(
                                139,
                                92,
                                246
                        ),
                        "KHACH_HANG"
                )
        );


        events.add(
                new TimelineEvent(
                        "08:31",
                        "Nhập kho nguyên liệu",
                        "Thịt bò Kobe – 10 kg",
                        "📦",
                        new Color(
                                16,
                                185,
                                129
                        ),
                        "KHO"
                )
        );


        events.add(
                new TimelineEvent(
                        "08:20",
                        "Thanh toán hoàn tất",
                        "Bàn 12 – 1.250.000 đ",
                        "💳",
                        SavoreDesignSystem
                                .Colors
                                .WARM_GOLD,
                        "POS"
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
                        "🕐 DÒNG THỜI GIAN VẬN HÀNH"
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


        JLabel lblLive =
                new JLabel(
                        "● Thời gian thực"
                );


        lblLive.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD
                )
        );


        lblLive.setForeground(
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
                lblLive,
                BorderLayout.EAST
        );


        return pnl;
    }


    // =========================================================
    // TIMELINE CONTAINER
    // =========================================================

    private JPanel taoTimelineContainer() {

        JPanel pnl =
                new JPanel() {

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
                        // CARD NỀN
                        // =========================================

                        GradientPaint bg =
                                new GradientPaint(
                                        0,
                                        0,
                                        new Color(
                                                16,
                                                25,
                                                35,
                                                245
                                        ),
                                        w,
                                        h,
                                        new Color(
                                                5,
                                                12,
                                                19,
                                                248
                                        )
                                );


                        g2.setPaint(bg);


                        g2.fillRoundRect(
                                0,
                                0,
                                w,
                                h,
                                15,
                                15
                        );


                        // =========================================
                        // VIỀN
                        // =========================================

                        g2.setColor(
                                new Color(
                                        200,
                                        146,
                                        69,
                                        105
                                )
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        1.0f
                                )
                        );


                        g2.drawRoundRect(
                                0,
                                0,
                                w - 1,
                                h - 1,
                                15,
                                15
                        );


                        // =========================================
                        // ĐƯỜNG TIMELINE
                        // =========================================

                        int lineX =
                                25;


                        g2.setColor(
                                new Color(
                                        200,
                                        146,
                                        69,
                                        80
                                )
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        1.4f,
                                        BasicStroke.CAP_ROUND,
                                        BasicStroke.JOIN_ROUND,
                                        0,
                                        new float[]{
                                                4f,
                                                4f
                                        },
                                        0
                                )
                        );


                        g2.drawLine(
                                lineX,
                                18,
                                lineX,
                                h - 18
                        );


                        g2.dispose();
                    }
                };


        pnl.setOpaque(false);


        pnl.setLayout(
                new BoxLayout(
                        pnl,
                        BoxLayout.Y_AXIS
                )
        );


        pnl.setBorder(
                new EmptyBorder(
                        8,
                        0,
                        8,
                        5
                )
        );


        for (
                TimelineEvent ev
                : events
        ) {

            pnl.add(
                    taoEventItem(ev)
            );


            pnl.add(
                    Box.createRigidArea(
                            new Dimension(
                                    0,
                                    4
                            )
                    )
            );
        }


        return pnl;
    }


    // =========================================================
    // EVENT ITEM
    // =========================================================

    private JPanel taoEventItem(
            TimelineEvent ev) {

        JPanel pnl =
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
                                    public void mouseExited(MouseEvent e) {
                                        isHover = false;
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
                                                    ev.targetModule
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

                        if (isHover) {

                            g2.setColor(
                                    new Color(
                                            28,
                                            38,
                                            51,
                                            235
                                    )
                            );

                        } else {

                            g2.setColor(
                                    new Color(
                                            10,
                                            18,
                                            27,
                                            165
                                    )
                            );
                        }


                        g2.fillRoundRect(
                                31,
                                1,
                                w - 33,
                                h - 2,
                                9,
                                9
                        );


                        // =========================================
                        // VIỀN CARD
                        // =========================================

                        g2.setColor(
                                isHover
                                        ? new Color(
                                                229,
                                                183,
                                                102,
                                                145
                                        )
                                        : new Color(
                                                200,
                                                146,
                                                69,
                                                30
                                        )
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        0.8f
                                )
                        );


                        g2.drawRoundRect(
                                31,
                                1,
                                w - 34,
                                h - 3,
                                9,
                                9
                        );


                        // =========================================
                        // NODE GLOW
                        // =========================================

                        int dotX =
                                25;


                        int dotY =
                                h / 2;


                        g2.setColor(
                                new Color(
                                        ev.color.getRed(),
                                        ev.color.getGreen(),
                                        ev.color.getBlue(),
                                        38
                                )
                        );


                        g2.fillOval(
                                dotX - 9,
                                dotY - 9,
                                18,
                                18
                        );


                        g2.setColor(
                                new Color(
                                        ev.color.getRed(),
                                        ev.color.getGreen(),
                                        ev.color.getBlue(),
                                        70
                                )
                        );


                        g2.fillOval(
                                dotX - 6,
                                dotY - 6,
                                12,
                                12
                        );


                        g2.setColor(
                                ev.color
                        );


                        g2.fillOval(
                                dotX - 3,
                                dotY - 3,
                                6,
                                6
                        );


                        // =========================================
                        // ĐƯỜNG NGANG NODE
                        // =========================================

                        g2.setColor(
                                new Color(
                                        ev.color.getRed(),
                                        ev.color.getGreen(),
                                        ev.color.getBlue(),
                                        75
                                )
                        );


                        g2.setStroke(
                                new BasicStroke(
                                        1f
                                )
                        );


                        g2.drawLine(
                                dotX + 7,
                                dotY,
                                38,
                                dotY
                        );


                        g2.dispose();
                    }
                };


        pnl.setOpaque(false);


        pnl.setBorder(
                new EmptyBorder(
                        4,
                        38,
                        4,
                        8
                )
        );


        pnl.setPreferredSize(
                new Dimension(
                        320,
                        43
                )
        );


        pnl.setMinimumSize(
                new Dimension(
                        180,
                        43
                )
        );


        pnl.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        43
                )
        );


        // =========================================================
        // CONTENT
        // =========================================================

        JPanel content =
                new JPanel();


        content.setOpaque(false);


        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );


        // =========================================================
        // TOP
        // =========================================================

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );


        top.setOpaque(false);


        JLabel lblTime =
                new JLabel(
                        ev.time
                );


        lblTime.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD
                )
        );


        lblTime.setForeground(
                SavoreDesignSystem
                        .Colors
                        .WARM_GOLD
        );


        JLabel lblIcon =
                new JLabel(
                        ev.icon
                );


        lblIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        11
                )
        );


        JPanel left =
                new JPanel(
                        new BorderLayout(
                                5,
                                0
                        )
                );


        left.setOpaque(false);


        left.add(
                lblTime,
                BorderLayout.WEST
        );


        left.add(
                lblIcon,
                BorderLayout.CENTER
        );


        JLabel lblTitle =
                new JLabel(
                        ev.title
                );


        lblTitle.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD
                )
        );


        lblTitle.setForeground(
                Color.WHITE
        );


        top.add(
                left,
                BorderLayout.WEST
        );


        top.add(
                lblTitle,
                BorderLayout.CENTER
        );


        // =========================================================
        // DETAIL
        // =========================================================

        JLabel lblDetail =
                new JLabel(
                        ev.detail
                );


        lblDetail.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.PLAIN
                )
        );


        lblDetail.setForeground(
                new Color(
                        148,
                        163,
                        184
                )
        );


        lblDetail.setBorder(
                new EmptyBorder(
                        2,
                        0,
                        0,
                        0
                )
        );


        content.add(
                top
        );


        content.add(
                lblDetail
        );


        pnl.add(
                content,
                BorderLayout.CENTER
        );


        return pnl;
    }


    // =========================================================
    // SIZE
    // =========================================================

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(
                340,
                245
        );
    }
}