package com.restaurant.view;
import java.awt.Component;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * SAVORÉ - DOANH THU
 *
 * Giao diện Dark Luxury:
 * - Nền xanh đen
 * - Viền vàng đồng
 * - Bo góc
 * - Biểu đồ vàng phát sáng
 * - Bộ lọc Ngày / Tuần / Tháng / Năm
 * - Tooltip khi rê chuột
 * - Animation khi đổi bộ lọc
 */
public class SmoothRevenueChartPanel extends JPanel {

    private final Consumer<String> onNavigate;

    private final List<DataPoint> currentPoints =
            new ArrayList<DataPoint>();

    private String revenueTotal =
            "12.450.000 đ";

    private String revenueDiff =
            "+12% so với hôm qua";

    private String currentFilter =
            "NGAY";

    private float animProgress =
            1f;

    private Timer animTimer;

    private int hoveredIndex =
            -1;


    // =========================================================
    // DATA POINT
    // =========================================================

    public static class DataPoint {

        public final String label;
        public final double value;

        public DataPoint(
                String label,
                double value) {

            this.label = label;
            this.value = value;
        }
    }


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SmoothRevenueChartPanel(
            Consumer<String> onNavigate) {

        this.onNavigate = onNavigate;

        setOpaque(false);

        setLayout(
                new BorderLayout(
                        0,
                        4
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        14,
                        12,
                        14
                )
        );

        napDuLieuTheoBoLoc("NGAY");

        add(
                taoHeader(),
                BorderLayout.NORTH
        );

        add(
                new ChartCanvas(),
                BorderLayout.CENTER
        );

        khoiDongAnimation();
    }


    // =========================================================
    // CẬP NHẬT DOANH THU
    // =========================================================

    public void capNhatDoanhThu(
            double tongTien,
            double tienHomQua,
            List<String> cacNgay,
            List<Double> cacGiaTri) {

        this.revenueTotal =
                SavoreDesignSystem.formatTien(
                        tongTien
                );

        if (tienHomQua > 0) {

            double pt =
                    (tongTien - tienHomQua)
                    / tienHomQua
                    * 100.0;

            this.revenueDiff =
                    (pt >= 0 ? "+" : "")
                    + String.format(
                            "%.1f%%",
                            pt
                    )
                    + " so với hôm qua";

        } else {

            this.revenueDiff =
                    "+12.4% so với kỳ trước";
        }

        if (cacNgay != null
                && cacGiaTri != null
                && cacNgay.size()
                == cacGiaTri.size()
                && !cacNgay.isEmpty()) {

            currentPoints.clear();

            for (int i = 0;
                    i < cacNgay.size();
                    i++) {

                currentPoints.add(
                        new DataPoint(
                                cacNgay.get(i),
                                cacGiaTri.get(i)
                        )
                );
            }
        }

        khoiDongAnimation();

        repaint();
    }


    // =========================================================
    // DỮ LIỆU NGÀY / TUẦN / THÁNG / NĂM
    // =========================================================

    private void napDuLieuTheoBoLoc(
            String filter) {

        currentPoints.clear();

        currentFilter =
                filter;


        switch (filter) {

            case "TUAN":

                revenueTotal =
                        "84.320.000 đ";

                revenueDiff =
                        "+18% so với tuần trước";

                currentPoints.add(
                        new DataPoint("T2", 9.2)
                );

                currentPoints.add(
                        new DataPoint("T3", 11.4)
                );

                currentPoints.add(
                        new DataPoint("T4", 10.8)
                );

                currentPoints.add(
                        new DataPoint("T5", 13.5)
                );

                currentPoints.add(
                        new DataPoint("T6", 15.2)
                );

                currentPoints.add(
                        new DataPoint("T7", 18.6)
                );

                currentPoints.add(
                        new DataPoint("CN", 15.6)
                );

                break;


            case "THANG":

                revenueTotal =
                        "342.800.000 đ";

                revenueDiff =
                        "+24% so với tháng trước";

                currentPoints.add(
                        new DataPoint(
                                "Tuần 1",
                                78.0
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Tuần 2",
                                82.5
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Tuần 3",
                                89.0
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Tuần 4",
                                93.3
                        )
                );

                break;


            case "NAM":

                revenueTotal =
                        "3.850.000.000 đ";

                revenueDiff =
                        "+31% so với năm 2025";

                currentPoints.add(
                        new DataPoint(
                                "Q1",
                                820.0
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Q2",
                                940.0
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Q3",
                                1020.0
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "Q4",
                                1070.0
                        )
                );

                break;


            default:

                revenueTotal =
                        "12.450.000 đ";

                revenueDiff =
                        "+12% so với hôm qua";

                currentPoints.add(
                        new DataPoint(
                                "08:00",
                                1.2
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "10:00",
                                2.4
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "12:00",
                                5.8
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "14:00",
                                3.2
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "16:00",
                                2.1
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "18:00",
                                8.4
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "20:00",
                                11.2
                        )
                );

                currentPoints.add(
                        new DataPoint(
                                "22:00",
                                6.8
                        )
                );

                break;
        }
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


        // -----------------------------------------------------
        // BÊN TRÁI
        // -----------------------------------------------------

        JPanel pnlLeft =
                new JPanel();

        pnlLeft.setOpaque(false);

        pnlLeft.setLayout(
                new javax.swing.BoxLayout(
                        pnlLeft,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );


        JLabel lblTitle =
                new JLabel(
                        "DOANH THU HÔM NAY"
                );

        lblTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        lblTitle.setForeground(
                new Color(
                        229,
                        183,
                        102
                )
        );


        JPanel valueRow =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        valueRow.setOpaque(false);


        JLabel lblValue =
                new JLabel(
                        revenueTotal
                );

        lblValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        lblValue.setForeground(
                new Color(
                        245,
                        201,
                        110
                )
        );


        JLabel lblDiff =
                new JLabel(
                        revenueDiff
                );

        lblDiff.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        9
                )
        );

        lblDiff.setForeground(
                new Color(
                        16,
                        185,
                        129
                )
        );


        valueRow.add(
                lblValue
        );

        valueRow.add(
                lblDiff
        );


        pnlLeft.add(
                lblTitle
        );

        pnlLeft.add(
                valueRow
        );


        // -----------------------------------------------------
        // BỘ LỌC
        // -----------------------------------------------------

        JPanel pnlFilters =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                4,
                                4
                        )
                );

        pnlFilters.setOpaque(false);


        String[] labels = {
                "NGÀY",
                "TUẦN",
                "THÁNG",
                "NĂM"
        };


        String[] keys = {
                "NGAY",
                "TUAN",
                "THANG",
                "NAM"
        };


        for (int i = 0;
                i < labels.length;
                i++) {

            final String key =
                    keys[i];

            final JButton btn =
                    taoNutLoc(
                            labels[i],
                            key.equals(
                                    currentFilter
                            )
                    );


            btn.addActionListener(
                    e -> {

                        napDuLieuTheoBoLoc(
                                key
                        );

                        capNhatMauNut(
                                pnlFilters
                        );

                        khoiDongAnimation();

                        repaint();
                    }
            );


            pnlFilters.add(
                    btn
            );
        }


        pnl.add(
                pnlLeft,
                BorderLayout.WEST
        );

        pnl.add(
                pnlFilters,
                BorderLayout.EAST
        );


        return pnl;
    }


    // =========================================================
    // TẠO NÚT BỘ LỌC
    // =========================================================

    private JButton taoNutLoc(
            String text,
            boolean active) {

        JButton btn =
                new JButton(
                        text
                );


        btn.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        8
                )
        );


        btn.setFocusPainted(
                false
        );

        btn.setBorderPainted(
                false
        );

        btn.setContentAreaFilled(
                false
        );

        btn.setOpaque(
                false
        );


        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        btn.setForeground(
                active
                        ? new Color(
                                255,
                                216,
                                140
                        )
                        : new Color(
                                145,
                                155,
                                165
                        )
        );


        btn.setBackground(
                active
                        ? new Color(
                                90,
                                62,
                                24
                        )
                        : new Color(
                                20,
                                28,
                                36
                        )
        );


        btn.setPreferredSize(
                new Dimension(
                        48,
                        25
                )
        );


        return btn;
    }


    // =========================================================
    // ĐỔI MÀU NÚT
    // =========================================================

    private void capNhatMauNut(
            JPanel pnlFilters) {

        Component[] components =
                pnlFilters.getComponents();


        String currentText =
                currentFilter;


        for (
                Component c
                : components
        ) {

            if (
                    c instanceof JButton
            ) {

                JButton btn =
                        (JButton) c;


                String text =
                        btn.getText();


                String key;


                if (text.equals("NGÀY")) {

                    key = "NGAY";

                } else if (
                        text.equals("TUẦN")
                ) {

                    key = "TUAN";

                } else if (
                        text.equals("THÁNG")
                ) {

                    key = "THANG";

                } else {

                    key = "NAM";
                }


                if (
                        key.equals(
                                currentText
                        )
                ) {

                    btn.setBackground(
                            new Color(
                                    90,
                                    62,
                                    24
                            )
                    );

                    btn.setForeground(
                            new Color(
                                    255,
                                    216,
                                    140
                            )
                    );

                } else {

                    btn.setBackground(
                            new Color(
                                    20,
                                    28,
                                    36
                            )
                    );

                    btn.setForeground(
                            new Color(
                                    145,
                                    155,
                                    165
                            )
                    );
                }
            }
        }
    }


    // =========================================================
    // ANIMATION
    // =========================================================

    private void khoiDongAnimation() {

        animProgress =
                0f;


        if (animTimer != null) {

            animTimer.stop();
        }


        animTimer =
                new Timer(
                        20,
                        e -> {

                            animProgress +=
                                    0.05f;


                            if (
                                    animProgress
                                    >= 1f
                            ) {

                                animProgress =
                                        1f;

                                animTimer.stop();
                            }


                            repaint();
                        }
                );


        animTimer.start();
    }


    // =========================================================
    // CHART CANVAS
    // =========================================================

    private class ChartCanvas
            extends JPanel {


        ChartCanvas() {

            setOpaque(false);


            MouseAdapter ma =
                    new MouseAdapter() {

                        @Override
                        public void mouseMoved(
                                MouseEvent e) {

                            int old =
                                    hoveredIndex;


                            hoveredIndex =
                                    timDiemTaiToaDo(
                                            e.getX()
                                    );


                            if (
                                    old
                                    != hoveredIndex
                            ) {

                                repaint();
                            }
                        }


                        @Override
                        public void mouseExited(
                                MouseEvent e) {

                            hoveredIndex =
                                    -1;

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
                                        "BAO_CAO"
                                );
                            }
                        }
                    };


            addMouseListener(
                    ma
            );

            addMouseMotionListener(
                    ma
            );
        }


        // =====================================================
        // TÌM ĐIỂM
        // =====================================================

        private int timDiemTaiToaDo(
                int mx) {

            int n =
                    currentPoints.size();


            if (n < 2) {

                return -1;
            }


            int padL =
                    36;

            int padR =
                    20;


            int usableW =
                    getWidth()
                    - padL
                    - padR;


            if (usableW <= 0) {

                return -1;
            }


            float step =
                    (float) usableW
                    / (n - 1);


            int idx =
                    Math.round(
                            (
                                    mx
                                    - padL
                            )
                            / step
                    );


            if (
                    idx >= 0
                    && idx < n
            ) {

                return idx;
            }


            return -1;
        }


        // =====================================================
        // VẼ BIỂU ĐỒ
        // =====================================================

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(
                    g
            );


            Graphics2D g2 =
                    (Graphics2D)
                    g.create();


            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );


            int w =
                    getWidth();

            int h =
                    getHeight();


            if (
                    w < 50
                    || h < 50
                    || currentPoints.size() < 2
            ) {

                g2.dispose();

                return;
            }


            int padL =
                    36;

            int padR =
                    20;

            int padT =
                    14;

            int padB =
                    27;


            int usableW =
                    w
                    - padL
                    - padR;


            int usableH =
                    h
                    - padT
                    - padB;


            // =================================================
            // MAX
            // =================================================

            double maxVal =
                    1.0;


            for (
                    DataPoint dp
                    : currentPoints
            ) {

                if (
                        dp.value
                        > maxVal
                ) {

                    maxVal =
                            dp.value;
                }
            }


            maxVal *=
                    1.15;


            int n =
                    currentPoints.size();


            float[] xs =
                    new float[n];

            float[] ys =
                    new float[n];


            for (
                    int i = 0;
                    i < n;
                    i++
            ) {

                xs[i] =
                        padL
                        + usableW
                        * i
                        / (float)
                        (n - 1);


                float value =
                        (float)
                        (
                                currentPoints
                                        .get(i)
                                        .value
                                * animProgress
                        );


                ys[i] =
                        padT
                        + usableH
                        * (
                                1f
                                - value
                                / (float)
                                maxVal
                        );
            }


            // =================================================
            // GRID
            // =================================================

            g2.setStroke(
                    new BasicStroke(
                            1f
                    )
            );


            g2.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            8
                    )
            );


            for (
                    int i = 0;
                    i <= 3;
                    i++
            ) {

                int y =
                        padT
                        + (int)
                        (
                                usableH
                                * (
                                        1
                                        - i
                                        / 3.0
                                )
                        );


                g2.setColor(
                        new Color(
                                110,
                                120,
                                130,
                                38
                        )
                );


                g2.drawLine(
                        padL,
                        y,
                        w - padR,
                        y
                );


                String label =
                        String.format(
                                "%.0f",
                                maxVal
                                * i
                                / 3.0
                        );


                g2.setColor(
                        new Color(
                                145,
                                155,
                                165,
                                180
                        )
                );


                g2.drawString(
                        label + "tr",
                        3,
                        y + 3
                );
            }


            // =================================================
            // SPLINE
            // =================================================

            Path2D path =
                    new Path2D.Float();


            path.moveTo(
                    xs[0],
                    ys[0]
            );


            for (
                    int i = 0;
                    i < n - 1;
                    i++
            ) {

                float x1 =
                        xs[i];

                float y1 =
                        ys[i];

                float x2 =
                        xs[i + 1];

                float y2 =
                        ys[i + 1];


                float mid =
                        (
                                x1
                                + x2
                        )
                        / 2f;


                path.curveTo(
                        mid,
                        y1,
                        mid,
                        y2,
                        x2,
                        y2
                );
            }


            // =================================================
            // VÙNG DƯỚI BIỂU ĐỒ
            // =================================================

            Path2D area =
                    new Path2D.Float(
                            path
                    );


            area.lineTo(
                    xs[n - 1],
                    h - padB
            );


            area.lineTo(
                    xs[0],
                    h - padB
            );


            area.closePath();


            GradientPaint areaPaint =
                    new GradientPaint(
                            0,
                            padT,
                            new Color(
                                    200,
                                    146,
                                    69,
                                    100
                            ),
                            0,
                            h - padB,
                            new Color(
                                    200,
                                    146,
                                    69,
                                    0
                            )
                    );


            g2.setPaint(
                    areaPaint
            );


            g2.fill(
                    area
            );


            // =================================================
            // GLOW CỦA ĐƯỜNG
            // =================================================

            g2.setColor(
                    new Color(
                            200,
                            146,
                            69,
                            55
                    )
            );


            g2.setStroke(
                    new BasicStroke(
                            7f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            g2.draw(
                    path
            );


            // =================================================
            // ĐƯỜNG VÀNG CHÍNH
            // =================================================

            g2.setColor(
                    new Color(
                            245,
                            198,
                            105
                    )
            );


            g2.setStroke(
                    new BasicStroke(
                            2.2f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            g2.draw(
                    path
            );


            // =================================================
            // CÁC ĐIỂM
            // =================================================

            for (
                    int i = 0;
                    i < n;
                    i++
            ) {

                boolean hover =
                        i == hoveredIndex;


                float r =
                        hover
                                ? 5f
                                : 3.5f;


                // Glow

                g2.setColor(
                        new Color(
                                229,
                                183,
                                102,
                                hover
                                        ? 100
                                        : 45
                        )
                );


                g2.fill(
                        new Ellipse2D.Float(
                                xs[i] - r - 3,
                                ys[i] - r - 3,
                                (r + 3) * 2,
                                (r + 3) * 2
                        )
                );


                // tâm tối

                g2.setColor(
                        new Color(
                                7,
                                13,
                                20
                        )
                );


                g2.fill(
                        new Ellipse2D.Float(
                                xs[i] - r,
                                ys[i] - r,
                                r * 2,
                                r * 2
                        )
                );


                // viền vàng

                g2.setColor(
                        hover
                                ? Color.WHITE
                                : new Color(
                                        245,
                                        198,
                                        105
                                )
                );


                g2.setStroke(
                        new BasicStroke(
                                hover
                                        ? 2.2f
                                        : 1.4f
                        )
                );


                g2.draw(
                        new Ellipse2D.Float(
                                xs[i] - r,
                                ys[i] - r,
                                r * 2,
                                r * 2
                        )
                );


                // =================================================
                // LABEL TRỤC X
                // =================================================

                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                8
                        )
                );


                g2.setColor(
                        new Color(
                                148,
                                163,
                                184,
                                200
                        )
                );


                FontMetrics fm =
                        g2.getFontMetrics();


                String text =
                        currentPoints
                                .get(i)
                                .label;


                g2.drawString(
                        text,
                        xs[i]
                                - fm.stringWidth(
                                        text
                                )
                                / 2f,
                        h - 7
                );
            }


            // =================================================
            // TOOLTIP
            // =================================================

            if (
                    hoveredIndex >= 0
                    && hoveredIndex < n
            ) {

                float hx =
                        xs[hoveredIndex];

                float hy =
                        ys[hoveredIndex];


                DataPoint dp =
                        currentPoints.get(
                                hoveredIndex
                        );


                // đường dọc

                g2.setColor(
                        new Color(
                                229,
                                183,
                                102,
                                110
                        )
                );


                g2.setStroke(
                        new BasicStroke(
                                1f,
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_BEVEL,
                                0,
                                new float[]{
                                        3f,
                                        3f
                                },
                                0
                        )
                );


                g2.drawLine(
                        (int) hx,
                        padT,
                        (int) hx,
                        h - padB
                );


                // nội dung tooltip

                String tip =
                        dp.label
                        + ": "
                        + String.format(
                                "%.1f",
                                dp.value
                        )
                        + " tr";


                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                9
                        )
                );


                FontMetrics fm =
                        g2.getFontMetrics();


                int tipW =
                        fm.stringWidth(
                                tip
                        )
                        + 18;


                int tipH =
                        25;


                int tipX =
                        (int) hx
                        - tipW / 2;


                if (
                        tipX < 3
                ) {

                    tipX = 3;
                }


                if (
                        tipX
                        + tipW
                        > w - 3
                ) {

                    tipX =
                            w
                            - tipW
                            - 3;
                }


                int tipY =
                        (int) hy
                        - tipH
                        - 10;


                if (
                        tipY < 3
                ) {

                    tipY = 3;
                }


                // tooltip background

                g2.setColor(
                        new Color(
                                7,
                                14,
                                21,
                                245
                        )
                );


                g2.fillRoundRect(
                        tipX,
                        tipY,
                        tipW,
                        tipH,
                        9,
                        9
                );


                // tooltip border

                g2.setColor(
                        new Color(
                                229,
                                183,
                                102,
                                190
                        )
                );


                g2.setStroke(
                        new BasicStroke(
                                1f
                        )
                );


                g2.drawRoundRect(
                        tipX,
                        tipY,
                        tipW,
                        tipH,
                        9,
                        9
                );


                g2.setColor(
                        new Color(
                                255,
                                225,
                                160
                        )
                );


                g2.drawString(
                        tip,
                        tipX + 9,
                        tipY + 16
                );
            }


            g2.dispose();
        }
    }


    // =========================================================
    // NỀN CARD CHÍNH
    // =========================================================

    @Override
    protected void paintComponent(
            Graphics g) {

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


        if (
                w > 5
                && h > 5
        ) {

            // -------------------------------------------------
            // NỀN KÍNH
            // -------------------------------------------------

            GradientPaint bg =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    17,
                                    25,
                                    34,
                                    248
                            ),
                            w,
                            h,
                            new Color(
                                    5,
                                    12,
                                    19,
                                    250
                            )
                    );


            g2.setPaint(
                    bg
            );


            g2.fillRoundRect(
                    0,
                    0,
                    w - 1,
                    h - 1,
                    22,
                    22
            );


            // -------------------------------------------------
            // ÁNH VÀNG
            // -------------------------------------------------

            GradientPaint goldLight =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    200,
                                    146,
                                    69,
                                    22
                            ),
                            w,
                            h,
                            new Color(
                                    200,
                                    146,
                                    69,
                                    0
                            )
                    );


            g2.setPaint(
                    goldLight
            );


            g2.fillRoundRect(
                    1,
                    1,
                    w - 2,
                    h - 2,
                    22,
                    22
            );


            // -------------------------------------------------
            // VIỀN NGOÀI
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            200,
                            146,
                            69,
                            145
                    )
            );


            g2.setStroke(
                    new BasicStroke(
                            1.2f
                    )
            );


            g2.drawRoundRect(
                    0,
                    0,
                    w - 1,
                    h - 1,
                    22,
                    22
            );


            // -------------------------------------------------
            // VIỀN TRONG
            // -------------------------------------------------

            g2.setColor(
                    new Color(
                            229,
                            183,
                            102,
                            35
                    )
            );


            g2.setStroke(
                    new BasicStroke(
                            1f
                    )
            );


            g2.drawRoundRect(
                    5,
                    5,
                    w - 11,
                    h - 11,
                    18,
                    18
            );
        }


        g2.dispose();


        // Quan trọng: super ở cuối để component con vẫn hiển thị
        super.paintComponent(
                g
        );
    }


    // =========================================================
    // SIZE
    // =========================================================

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(
                440,
                250
        );
    }
}