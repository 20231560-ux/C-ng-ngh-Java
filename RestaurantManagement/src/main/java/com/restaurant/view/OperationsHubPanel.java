package com.restaurant.view;

import java.awt.BasicStroke;
import java.awt.Shape;

import java.awt.Color;

import java.awt.Font;

import java.awt.FontMetrics;

import java.awt.GradientPaint;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.RadialGradientPaint;

import java.awt.geom.Ellipse2D;

import java.awt.geom.Line2D;

import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.awt.geom.Point2D;

import java.util.ArrayList;

import java.util.List;

import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import javax.swing.Timer;

/**

 * SAVORÉ OPERATIONS HUB

 *

 * Bản này giữ nguyên bố cục/data/animation của Operations Hub hiện tại,

 * nhưng thay icon emoji bằng icon Java2D vẽ trực tiếp.

 *

 * Vì icon được vẽ bằng hình học nên:

 * - không phụ thuộc font Emoji của Windows;

 * - icon luôn nét;

 * - icon nằm đúng giữa node;

 * - màu icon theo accent của từng node;

 * - không bị lỗi ô vuông/emoji lệch.

 */

public class OperationsHubPanel extends JPanel {

    private final Consumer<String> onNavigate;

    private final List<HubNode> nodes =

            new ArrayList<HubNode>();

    private float tick = 0f;

    private Timer animTimer;

    private int hoveredIndex = -1;

    // Ảnh nền nhà hàng sáng cho Operations Hub.
    private BufferedImage hubBackground;

    private String txtBan = "12/24";

    private String subBan = "50% công suất";

    private String txtBep = "Đang nấu";

    private String subBep = "8 món chờ";

    private String txtPos = "36 đơn";

    private String subPos = "Ổn định";

    private String txtKho = "Bình thường";

    private String subKho = "98% tồn an toàn";

    private String txtVip = "15 khách";

    private String subVip = "Đang dùng bữa";

    private String txtOnline = "8 đơn";

    private String subOnline = "Đang giao";

    public static class HubNode {

        public final String id;

        public final String title;

        public String value;

        public String sub;

        /*

         * Không dùng String icon để vẽ emoji nữa.

         * iconType quyết định icon Java2D.

         */

        public final String iconType;

        public final Color accentColor;

        public final String targetModule;

        public float x;

        public float y;

        public float r;

        public HubNode(

                String id,

                String title,

                String value,

                String sub,

                String iconType,

                Color accent,

                String target) {

            this.id = id;

            this.title = title;

            this.value = value;

            this.sub = sub;

            this.iconType = iconType;

            this.accentColor = accent;

            this.targetModule = target;

        }

    }

    public OperationsHubPanel(

            Consumer<String> onNavigate) {

        this.onNavigate = onNavigate;

        try {
            hubBackground = ImageIO.read(
                    getClass().getResource("/images/operations-hub-background.png"));
        } catch (Exception ex) {
            hubBackground = null;
        }

        setOpaque(false);

        setLayout(null);

        khoiTaoNodes();

        khoiDongAnimation();

        khoiTaoSuKien();

    }

    private void khoiTaoNodes() {

        nodes.clear();

        /*

         * Icon được vẽ trực tiếp:

         *

         * TABLE     = bàn ăn

         * KITCHEN   = bếp

         * POS       = máy POS

         * BOX       = kho

         * CROWN     = VIP

         * DELIVERY  = đơn online

         */

        nodes.add(

                new HubNode(

                        "BAN",

                        "BÀN ĂN",

                        txtBan,

                        subBan,

                        "TABLE",

                        new Color(16, 185, 129),

                        "DAT_BAN"));

        nodes.add(

                new HubNode(

                        "BEP",

                        "NHÀ BẾP",

                        txtBep,

                        subBep,

                        "KITCHEN",

                        new Color(245, 158, 11),

                        "POS"));

        nodes.add(

                new HubNode(

                        "POS",

                        "POS THU NGÂN",

                        txtPos,

                        subPos,

                        "POS",

                        new Color(16, 185, 129),

                        "POS"));

        nodes.add(

                new HubNode(

                        "KHO",

                        "KHO VẬT TƯ",

                        txtKho,

                        subKho,

                        "BOX",

                        new Color(59, 130, 246),

                        "KHO"));

        nodes.add(

                new HubNode(

                        "VIP",

                        "KHÁCH VIP",

                        txtVip,

                        subVip,

                        "CROWN",

                        new Color(168, 85, 247),

                        "KHACH_HANG"));

        nodes.add(

                new HubNode(

                        "ONLINE",

                        "ĐƠN ONLINE",

                        txtOnline,

                        subOnline,

                        "DELIVERY",

                        new Color(59, 130, 246),

                        "DON_HANG"));

    }

    public void capNhatSoLieu(

            int banDung,

            int tongBan,

            int donHang,

            int khachVip,

            int donOnline,

            String khoStatus,

            String bepStatus) {

        this.txtBan =

                banDung

                        + "/"

                        + Math.max(

                                tongBan,

                                1);

        int pt =

                tongBan > 0

                        ? (int)

                                (banDung

                                        * 100.0

                                        / tongBan)

                        : 0;

        this.subBan =

                pt

                        + "% công suất";

        if (bepStatus != null

                && !bepStatus.isEmpty()) {

            this.txtBep =

                    bepStatus;

        }

        if (donHang > 0) {

            this.txtPos =

                    donHang

                            + " đơn";

        }

        if (khachVip >= 0) {

            this.txtVip =

                    khachVip

                            + " khách";

        }

        if (donOnline >= 0) {

            this.txtOnline =

                    donOnline

                            + " đơn";

        }

        if (khoStatus != null

                && !khoStatus.isEmpty()) {

            this.txtKho =

                    khoStatus;

        }

        if (nodes.size() == 6) {

            nodes.get(0).value =

                    txtBan;

            nodes.get(0).sub =

                    subBan;

            nodes.get(1).value =

                    txtBep;

            nodes.get(1).sub =

                    subBep;

            nodes.get(2).value =

                    txtPos;

            nodes.get(2).sub =

                    subPos;

            nodes.get(3).value =

                    txtKho;

            nodes.get(3).sub =

                    subKho;

            nodes.get(4).value =

                    txtVip;

            nodes.get(4).sub =

                    subVip;

            nodes.get(5).value =

                    txtOnline;

            nodes.get(5).sub =

                    subOnline;

        }

        repaint();

    }

    private void khoiDongAnimation() {

        animTimer =

                new Timer(

                        26,

                        e -> {

                            tick +=

                                    0.035f;

                            if (tick > 10000f) {

                                tick = 0f;

                            }

                            repaint();

                        });

        animTimer.start();

    }

    private void khoiTaoSuKien() {

        MouseAdapter ma =

                new MouseAdapter() {

                    @Override

                    public void mouseMoved(

                            MouseEvent e) {

                        int oldHover =

                                hoveredIndex;

                        hoveredIndex =

                                timNodeTaiToaDo(

                                        e.getX(),

                                        e.getY());

                        if (hoveredIndex

                                != oldHover) {

                            setCursor(

                                    hoveredIndex >= 0

                                            ? new java.awt.Cursor(

                                                    java.awt.Cursor.HAND_CURSOR)

                                            : java.awt.Cursor.getDefaultCursor());

                            repaint();

                        }

                    }

                    @Override

                    public void mouseExited(

                            MouseEvent e) {

                        hoveredIndex =

                                -1;

                        setCursor(

                                java.awt.Cursor.getDefaultCursor());

                        repaint();

                    }

                    @Override

                    public void mouseClicked(

                            MouseEvent e) {

                        int idx =

                                timNodeTaiToaDo(

                                        e.getX(),

                                        e.getY());

                        if (idx >= 0

                                && onNavigate != null) {

                            onNavigate.accept(

                                    nodes.get(idx)

                                            .targetModule);

                        } else {

                            float cx =

                                    getWidth()

                                            / 2f;

                            float cy =

                                    getHeight()

                                            / 2f;

                            float dist =

                                    (float)

                                            Point2D.distance(

                                                    e.getX(),

                                                    e.getY(),

                                                    cx,

                                                    cy);

                            if (dist <= 52

                                    && onNavigate != null) {

                                onNavigate.accept(

                                        "DASHBOARD");

                            }

                        }

                    }

                };

        addMouseListener(ma);

        addMouseMotionListener(ma);

    }

    private int timNodeTaiToaDo(

            int x,

            int y) {

        for (int i = 0;

                i < nodes.size();

                i++) {

            HubNode n =

                    nodes.get(i);

            float d =

                    (float)

                            Point2D.distance(

                                    x,

                                    y,

                                    n.x,

                                    n.y);

            if (d <= n.r + 5) {

                return i;

            }

        }

        return -1;

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

                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(

                RenderingHints.KEY_TEXT_ANTIALIASING,

                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setRenderingHint(

                RenderingHints.KEY_RENDERING,

                RenderingHints.VALUE_RENDER_QUALITY);

        int w =

                getWidth();

        int h =

                getHeight();

        if (w <= 10

                || h <= 10) {

            g2.dispose();

            return;

        }

        float cx =

                w / 2f;

        float cy =

                h / 2f;

        float minDim =

                Math.min(

                        w,

                        h);

        float orbitRadius =

                Math.max(

                        80f,

                        minDim * 0.32f);

        /*

         * Node hơi lớn hơn bản cũ một chút để

         * icon vector có đủ chỗ nhưng vẫn gọn.

         */

        float nodeRadius =

                Math.max(

                        29f,

                        minDim * 0.078f);

        /*

         * -------------------------------------------------

         * 1. DARK GLASS BACKGROUND

         * -------------------------------------------------

         */

        RoundRectangle2D cardShape =
                new RoundRectangle2D.Float(
                        0,
                        0,
                        w,
                        h,
                        20,
                        20);

        /*
         * NỀN ẢNH NHÀ HÀNG SÁNG:
         * lấy phần cảnh nhà hàng bên phải của operations-hub-background.png,
         * không lấy phần chữ bên trái nên không bị trùng tiêu đề.
         */
        if (hubBackground != null) {
            Shape oldClip = g2.getClip();
            g2.clip(cardShape);

            int iw = hubBackground.getWidth();
            int ih = hubBackground.getHeight();

            // Cover toàn bộ panel, giữ đúng tỉ lệ ảnh và cắt nhẹ hai bên nếu cần.
            double scale = Math.max((double) w / iw, (double) h / ih);
            int dw = (int) Math.ceil(iw * scale);
            int dh = (int) Math.ceil(ih * scale);
            int dx = (w - dw) / 2;
            int dy = (h - dh) / 2;

            g2.drawImage(
                    hubBackground,
                    dx,
                    dy,
                    dw,
                    dh,
                    null);

            // Làm tối nhẹ để node và chữ nổi lên, nhưng vẫn thấy nhà hàng.
            g2.setColor(new Color(5, 12, 20, 92));
            g2.fillRect(0, 0, w, h);

            // Ánh vàng nhẹ ở trung tâm.
            RadialGradientPaint imageGlow =
                    new RadialGradientPaint(
                            new java.awt.geom.Point2D.Float(cx, cy),
                            Math.max(100f, minDim * 0.60f),
                            new float[] {0f, 0.55f, 1f},
                            new Color[] {
                                    new Color(230, 170, 70, 42),
                                    new Color(20, 25, 32, 16),
                                    new Color(5, 10, 16, 40)
                            });

            g2.setPaint(imageGlow);
            g2.fillRect(0, 0, w, h);

            g2.setClip(oldClip);
        } else {
            g2.setColor(new Color(0x0C, 0x12, 0x1D, 235));
            g2.fill(cardShape);
        }

        /*

         * Ambient gold glow.

         */

        float glowRadius =

                minDim * 0.55f;

        java.awt.RadialGradientPaint rgp =

                new java.awt.RadialGradientPaint(

                        new Point2D.Float(

                                cx,

                                cy),

                        Math.max(

                                80f,

                                glowRadius),

                        new float[]{

                                0f,

                                0.45f,

                                1f

                        },

                        new Color[]{

                                new Color(

                                        0xC8,

                                        0x92,

                                        0x45,

                                        (int)

                                                (32

                                                        + 12

                                                        * Math.sin(

                                                                tick

                                                                        * 1.5f))),

                                new Color(

                                        0x10,

                                        0x17,

                                        0x22,

                                        100),

                                new Color(

                                        0x0C,

                                        0x12,

                                        0x1D,

                                        0)

                        });

        g2.setPaint(rgp);

        g2.fill(cardShape);

        /*

         * Viền card.

         */

        g2.setColor(

                new Color(

                        0xC8,

                        0x92,

                        0x45,

                        55));

        g2.setStroke(

                new BasicStroke(

                        1f));

        g2.draw(

                new RoundRectangle2D.Float(

                        0.5f,

                        0.5f,

                        w - 1f,

                        h - 1f,

                        20,

                        20));

        /*

         * -------------------------------------------------

         * 2. ORBIT RINGS

         * -------------------------------------------------

         */

        float phase1 =

                Math.abs(

                        (tick * 15f)

                                % 12f);

        g2.setStroke(

                new BasicStroke(

                        1.2f,

                        BasicStroke.CAP_ROUND,

                        BasicStroke.JOIN_ROUND,

                        10f,

                        new float[]{

                                6f,

                                6f

                        },

                        phase1));

        g2.setColor(

                new Color(

                        0xC8,

                        0x92,

                        0x45,

                        55));

        g2.draw(

                new Ellipse2D.Float(

                        cx - orbitRadius,

                        cy - orbitRadius,

                        orbitRadius * 2,

                        orbitRadius * 2));

        float innerOrbit =

                orbitRadius * 0.58f;

        float phase2 =

                Math.abs(

                        (tick * 10f)

                                % 8f);

        g2.setStroke(

                new BasicStroke(

                        1f,

                        BasicStroke.CAP_ROUND,

                        BasicStroke.JOIN_ROUND,

                        10f,

                        new float[]{

                                3f,

                                5f

                        },

                        phase2));

        g2.setColor(

                new Color(

                        0xC8,

                        0x92,

                        0x45,

                        30));

        g2.draw(

                new Ellipse2D.Float(

                        cx - innerOrbit,

                        cy - innerOrbit,

                        innerOrbit * 2,

                        innerOrbit * 2));

        /*

         * -------------------------------------------------

         * 3. NODE POSITIONS + CONNECTING LINES

         * -------------------------------------------------

         */

        int n =

                nodes.size();

        for (int i = 0;

                i < n;

                i++) {

            HubNode node =

                    nodes.get(i);

            double angle =

                    -Math.PI / 2.0

                            + (i

                                    * (2.0

                                            * Math.PI

                                            / n));

            node.x =

                    cx

                            + (float)

                                    (Math.cos(

                                            angle)

                                            * orbitRadius);

            node.y =

                    cy

                            + (float)

                                    (Math.sin(

                                            angle)

                                            * orbitRadius);

            node.r =

                    nodeRadius;

            /*

             * Connecting line.

             */

            g2.setStroke(

                    new BasicStroke(

                            1.2f));

            g2.setColor(

                    new Color(

                            0xC8,

                            0x92,

                            0x45,

                            40));

            g2.drawLine(

                    (int) cx,

                    (int) cy,

                    (int) node.x,

                    (int) node.y);

            /*

             * Particle chạy theo đường nối.

             */

            float pulseT =

                    (float)

                            ((tick * 0.4

                                    + (i * 0.166))

                                    % 1.0);

            float px =

                    cx

                            + (node.x - cx)

                            * pulseT;

            float py =

                    cy

                            + (node.y - cy)

                            * pulseT;

            int particleAlpha =

                    (int)

                            (180

                                    * Math.sin(

                                            pulseT

                                                    * Math.PI));

            g2.setColor(

                    new Color(

                            0xE5,

                            0xB7,

                            0x66,

                            Math.max(

                                    0,

                                    particleAlpha)));

            g2.fill(

                    new Ellipse2D.Float(

                            px - 2.5f,

                            py - 2.5f,

                            5f,

                            5f));

        }

        /*

         * -------------------------------------------------

         * 4. CENTRAL HUB

         * -------------------------------------------------

         */

        float centerR =

                Math.max(

                        42f,

                        minDim * 0.11f);

        float haloR =

                centerR

                        + 8f

                        + (float)

                                (5f

                                        * Math.sin(

                                                tick

                                                        * 2.2f));

        g2.setColor(

                new Color(

                        0xC8,

                        0x92,

                        0x45,

                        (int)

                                (40

                                        + 20

                                        * Math.sin(

                                                tick

                                                        * 2.2f))));

        g2.fill(

                new Ellipse2D.Float(

                        cx - haloR,

                        cy - haloR,

                        haloR * 2,

                        haloR * 2));

        GradientPaint gpCenter =

                new GradientPaint(

                        cx - centerR,

                        cy - centerR,

                        new Color(

                                0x27,

                                0x1B,

                                0x0E),

                        cx + centerR,

                        cy + centerR,

                        new Color(

                                0x0C,

                                0x13,

                                0x20));

        g2.setPaint(gpCenter);

        g2.fill(

                new Ellipse2D.Float(

                        cx - centerR,

                        cy - centerR,

                        centerR * 2,

                        centerR * 2));

        g2.setColor(

                new Color(

                        0xE5,

                        0xB7,

                        0x66));

        g2.setStroke(

                new BasicStroke(

                        2f));

        g2.draw(

                new Ellipse2D.Float(

                        cx - centerR,

                        cy - centerR,

                        centerR * 2,

                        centerR * 2));

        g2.setColor(

                new Color(

                        0xC8,

                        0x92,

                        0x45,

                        80));

        g2.setStroke(

                new BasicStroke(

                        1f));

        g2.draw(

                new Ellipse2D.Float(

                        cx - centerR + 4f,

                        cy - centerR + 4f,

                        (centerR - 4f) * 2,

                        (centerR - 4f) * 2));

        /*

         * Icon chef/table ở tâm:

         * dùng biểu tượng nhỏ dạng line thay vì emoji.

         */

        veIconCenter(

                g2,

                cx,

                cy - 9f,

                17f);

        g2.setFont(

                SavoreDesignSystem.Fonts.get(

                        10,

                        Font.BOLD));

        g2.setColor(

                new Color(

                        0xE5,

                        0xB7,

                        0x66));

        FontMetrics fmCore =

                g2.getFontMetrics();

        String titleCore =

                "SAVORÉ";

        g2.drawString(

                titleCore,

                cx

                        - fmCore.stringWidth(

                                titleCore)

                                / 2f,

                cy + 10);

        g2.setFont(

                SavoreDesignSystem.Fonts.get(

                        8,

                        Font.BOLD));

        g2.setColor(

                new Color(

                        0x94,

                        0xA3,

                        0xB8));

        FontMetrics fmHub =

                g2.getFontMetrics();

        String subCore =

                "OPERATIONS HUB";

        g2.drawString(

                subCore,

                cx

                        - fmHub.stringWidth(

                                subCore)

                                / 2f,

                cy + 22);

        /*

         * -------------------------------------------------

         * 5. SIX SATELLITE NODES

         * -------------------------------------------------

         */

        for (int i = 0;

                i < n;

                i++) {

            HubNode node =

                    nodes.get(i);

            boolean isHovered =

                    i == hoveredIndex;

            /*

             * Hover chỉ tăng 2 px,

             * không làm node phình to.

             */

            float curR =

                    isHovered

                            ? node.r + 2f

                            : node.r;

            /*

             * Glow nhỏ phía sau.

             */

            if (isHovered) {

                for (int glow = 10;

                        glow >= 4;

                        glow -= 2) {

                    int alpha =

                            9

                                    + (10

                                            - glow);

                    g2.setColor(

                            new Color(

                                    node.accentColor

                                            .getRed(),

                                    node.accentColor

                                            .getGreen(),

                                    node.accentColor

                                            .getBlue(),

                                    alpha));

                    g2.fill(

                            new Ellipse2D.Float(

                                    node.x

                                            - curR

                                            - glow,

                                    node.y

                                            - curR

                                            - glow,

                                    (curR

                                            + glow)

                                            * 2,

                                    (curR

                                            + glow)

                                            * 2));

                }

            }

            /*

             * Node nền.

             */

            GradientPaint gpNode =

                    new GradientPaint(

                            node.x - curR,

                            node.y - curR,

                            new Color(

                                    0x18,

                                    0x22,

                                    0x33),

                            node.x + curR,

                            node.y + curR,

                            new Color(

                                    0x0C,

                                    0x12,

                                    0x1C));

            g2.setPaint(gpNode);

            g2.fill(

                    new Ellipse2D.Float(

                            node.x - curR,

                            node.y - curR,

                            curR * 2,

                            curR * 2));

            /*

             * Viền node.

             */

            g2.setColor(

                    isHovered

                            ? node.accentColor

                            : new Color(

                                    node.accentColor

                                            .getRed(),

                                    node.accentColor

                                            .getGreen(),

                                    node.accentColor

                                            .getBlue(),

                                    175));

            g2.setStroke(

                    new BasicStroke(

                            isHovered

                                    ? 2.1f

                                    : 1.5f));

            g2.draw(

                    new Ellipse2D.Float(

                            node.x - curR,

                            node.y - curR,

                            curR * 2,

                            curR * 2));

            /*

             * ============================

             * ICON MỚI

             * ============================

             *

             * Icon nằm phía trên value,

             * giống mẫu ảnh bạn gửi.

             */

            veIconNode(

                    g2,

                    node.iconType,

                    node.x,

                    node.y - 12f,

                    18f,

                    node.accentColor);

            /*

             * Value.

             */

            g2.setFont(

                    SavoreDesignSystem.Fonts.get(

                            10,

                            Font.BOLD));

            g2.setColor(Color.WHITE);

            FontMetrics fmVal =

                    g2.getFontMetrics();

            g2.drawString(

                    node.value,

                    node.x

                            - fmVal.stringWidth(

                                    node.value)

                                    / 2f,

                    node.y + 11);

            /*

             * Title bên ngoài.

             */

            g2.setFont(

                    SavoreDesignSystem.Fonts.get(

                            9,

                            Font.BOLD));

            g2.setColor(

                    node.accentColor);

            FontMetrics fmT =

                    g2.getFontMetrics();

            float labelY =

                    node.y < cy

                            ? Math.max(

                                    14f,

                                    node.y

                                            - curR

                                            - 5)

                            : Math.min(

                                    h - 14f,

                                    node.y

                                            + curR

                                            + 13);

            g2.drawString(

                    node.title,

                    node.x

                            - fmT.stringWidth(

                                    node.title)

                                    / 2f,

                    labelY);

            /*

             * Sub.

             */

            g2.setFont(

                    SavoreDesignSystem.Fonts.get(

                            8,

                            Font.PLAIN));

            g2.setColor(

                    new Color(

                            0x94,

                            0xA3,

                            0xB8));

            FontMetrics fmS =

                    g2.getFontMetrics();

            float subY =

                    node.y < cy

                            ? labelY - 10

                            : labelY + 11;

            if (subY >= 10

                    && subY <= h - 4) {

                g2.drawString(

                        node.sub,

                        node.x

                                - fmS.stringWidth(

                                        node.sub)

                                        / 2f,

                        subY);

            }

        }

        g2.dispose();

    }

    /*

     * =====================================================

     * ICONS - JAVA2D

     * =====================================================

     */

    private void veIconNode(

            Graphics2D g2,

            String type,

            float cx,

            float cy,

            float size,

            Color color) {

        Graphics2D g =

                (Graphics2D)

                        g2.create();

        g.setRenderingHint(

                RenderingHints.KEY_ANTIALIASING,

                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(color);

        g.setStroke(

                new BasicStroke(

                        1.8f,

                        BasicStroke.CAP_ROUND,

                        BasicStroke.JOIN_ROUND));

        float s =

                size / 2f;

        if ("TABLE".equals(type)) {

            /*

             * Bàn ăn:

             * mặt bàn + 4 ghế nhỏ.

             */

            g.fill(

                    new RoundRectangle2D.Float(

                            cx - s * 0.52f,

                            cy - s * 0.38f,

                            s * 1.04f,

                            s * 0.48f,

                            3,

                            3));

            g.drawLine(

                    (int) cx,

                    (int) (cy + s * 0.10f),

                    (int) cx,

                    (int) (cy + s * 0.62f));

            g.drawLine(

                    (int) (cx - s * 0.42f),

                    (int) (cy + s * 0.62f),

                    (int) (cx + s * 0.42f),

                    (int) (cy + s * 0.62f));

            g.fillRoundRect(

                    (int) (cx - s * 0.82f),

                    (int) (cy - s * 0.32f),

                    (int) (s * 0.22f),

                    (int) (s * 0.48f),

                    3,

                    3);

            g.fillRoundRect(

                    (int) (cx + s * 0.60f),

                    (int) (cy - s * 0.32f),

                    (int) (s * 0.22f),

                    (int) (s * 0.48f),

                    3,

                    3);

        } else if ("KITCHEN".equals(type)) {

            /*

             * Flame/bếp:

             * ngọn lửa cách điệu giống icon ảnh mẫu.

             */

            java.awt.geom.Path2D flame =

                    new java.awt.geom.Path2D.Float();

            flame.moveTo(

                    cx,

                    cy + s * 0.65f);

            flame.curveTo(

                    cx - s * 0.65f,

                    cy + s * 0.20f,

                    cx - s * 0.40f,

                    cy - s * 0.25f,

                    cx - s * 0.10f,

                    cy - s * 0.68f);

            flame.curveTo(

                    cx - s * 0.08f,

                    cy - s * 0.25f,

                    cx + s * 0.20f,

                    cy - s * 0.15f,

                    cx + s * 0.16f,

                    cy - s * 0.62f);

            flame.curveTo(

                    cx + s * 0.70f,

                    cy - s * 0.18f,

                    cx + s * 0.68f,

                    cy + s * 0.30f,

                    cx,

                    cy + s * 0.65f);

            flame.closePath();

            g.fill(flame);

            g.setColor(

                    new Color(

                            255,

                            225,

                            150));

            g.fill(

                    new Ellipse2D.Float(

                            cx - s * 0.16f,

                            cy + s * 0.02f,

                            s * 0.32f,

                            s * 0.34f));

        } else if ("POS".equals(type)) {

            /*

             * Máy POS / thẻ thanh toán.

             */

            g.drawRoundRect(

                    (int) (cx - s * 0.72f),

                    (int) (cy - s * 0.48f),

                    (int) (s * 1.44f),

                    (int) (s * 0.92f),

                    3,

                    3);

            g.fillRoundRect(

                    (int) (cx - s * 0.50f),

                    (int) (cy - s * 0.20f),

                    (int) (s * 1.00f),

                    (int) (s * 0.13f),

                    2,

                    2);

            g.fill(

                    new Ellipse2D.Float(

                            cx - s * 0.46f,

                            cy + s * 0.12f,

                            s * 0.14f,

                            s * 0.14f));

            g.fill(

                    new Ellipse2D.Float(

                            cx - s * 0.18f,

                            cy + s * 0.12f,

                            s * 0.14f,

                            s * 0.14f));

            g.fill(

                    new Ellipse2D.Float(

                            cx + s * 0.10f,

                            cy + s * 0.12f,

                            s * 0.14f,

                            s * 0.14f));

        } else if ("BOX".equals(type)) {

            /*

             * Hộp kho.

             */

            java.awt.Polygon box =

                    new java.awt.Polygon();

            box.addPoint(

                    (int) (cx - s * 0.68f),

                    (int) (cy - s * 0.25f));

            box.addPoint(

                    (int) cx,

                    (int) (cy - s * 0.62f));

            box.addPoint(

                    (int) (cx + s * 0.68f),

                    (int) (cy - s * 0.25f));

            box.addPoint(

                    (int) cx,

                    (int) (cy + s * 0.12f));

            g.draw(box);

            g.drawLine(

                    (int) (cx - s * 0.68f),

                    (int) (cy - s * 0.25f),

                    (int) (cx - s * 0.68f),

                    (int) (cy + s * 0.34f));

            g.drawLine(

                    (int) (cx + s * 0.68f),

                    (int) (cy - s * 0.25f),

                    (int) (cx + s * 0.68f),

                    (int) (cy + s * 0.34f));

            g.drawLine(

                    (int) (cx - s * 0.68f),

                    (int) (cy + s * 0.34f),

                    (int) cx,

                    (int) (cy + s * 0.70f));

            g.drawLine(

                    (int) (cx + s * 0.68f),

                    (int) (cy + s * 0.34f),

                    (int) cx,

                    (int) (cy + s * 0.70f));

            g.drawLine(

                    (int) cx,

                    (int) (cy + s * 0.12f),

                    (int) cx,

                    (int) (cy + s * 0.70f));

        } else if ("CROWN".equals(type)) {

            /*

             * Vương miện VIP.

             */

            java.awt.Polygon crown =

                    new java.awt.Polygon();

            crown.addPoint(

                    (int) (cx - s * 0.75f),

                    (int) (cy - s * 0.42f));

            crown.addPoint(

                    (int) (cx - s * 0.38f),

                    (int) (cy - s * 0.05f));

            crown.addPoint(

                    (int) cx,

                    (int) (cy - s * 0.60f));

            crown.addPoint(

                    (int) (cx + s * 0.38f),

                    (int) (cy - s * 0.05f));

            crown.addPoint(

                    (int) (cx + s * 0.75f),

                    (int) (cy - s * 0.42f));

            crown.addPoint(

                    (int) (cx + s * 0.55f),

                    (int) (cy + s * 0.48f));

            crown.addPoint(

                    (int) (cx - s * 0.55f),

                    (int) (cy + s * 0.48f));





            g.fill(crown);

            g.setColor(

                    new Color(

                            255,

                            220,

                            150));

            g.drawLine(

                    (int) (cx - s * 0.48f),

                    (int) (cy + s * 0.18f),

                    (int) (cx + s * 0.48f),

                    (int) (cy + s * 0.18f));

        } else if ("DELIVERY".equals(type)) {

            /*

             * Đơn online:

             * xe giao hàng + hộp.

             */

            g.drawRoundRect(

                    (int) (cx - s * 0.72f),

                    (int) (cy - s * 0.30f),

                    (int) (s * 0.90f),

                    (int) (s * 0.55f),

                    3,

                    3);

            g.drawLine(

                    (int) (cx + s * 0.18f),

                    (int) (cy - s * 0.30f),

                    (int) (cx + s * 0.58f),

                    (int) (cy - s * 0.30f));

            g.drawLine(

                    (int) (cx + s * 0.58f),

                    (int) (cy - s * 0.30f),

                    (int) (cx + s * 0.78f),

                    (int) (cy + s * 0.12f));

            g.fill(

                    new Ellipse2D.Float(

                            cx - s * 0.58f,

                            cy + s * 0.05f,

                            s * 0.25f,

                            s * 0.25f));

            g.fill(

                    new Ellipse2D.Float(

                            cx + s * 0.40f,

                            cy + s * 0.05f,

                            s * 0.25f,

                            s * 0.25f));

        }

        g.dispose();

    }

    private void veIconCenter(

            Graphics2D g2,

            float cx,

            float cy,

            float size) {

        Graphics2D g =

                (Graphics2D)

                        g2.create();

        g.setRenderingHint(

                RenderingHints.KEY_ANTIALIASING,

                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(

                new Color(

                        0xE5,

                        0xB7,

                        0x66));

        g.setStroke(

                new BasicStroke(

                        1.5f,

                        BasicStroke.CAP_ROUND,

                        BasicStroke.JOIN_ROUND));

        /*

         * Mũ đầu bếp nhỏ ở giữa.

         */

        g.fill(

                new Ellipse2D.Float(

                        cx - size * 0.45f,

                        cy - size * 0.30f,

                        size * 0.38f,

                        size * 0.38f));

        g.fill(

                new Ellipse2D.Float(

                        cx - size * 0.10f,

                        cy - size * 0.46f,

                        size * 0.48f,

                        size * 0.48f));

        g.fill(

                new Ellipse2D.Float(

                        cx + size * 0.22f,

                        cy - size * 0.26f,

                        size * 0.38f,

                        size * 0.38f));

        g.fillRoundRect(

                (int) (cx - size * 0.48f),

                (int) (cy - size * 0.03f),

                (int) (size * 1.10f),

                (int) (size * 0.28f),

                4,

                4);

        g.drawLine(

                (int) (cx - size * 0.38f),

                (int) (cy + size * 0.28f),

                (int) (cx + size * 0.38f),

                (int) (cy + size * 0.28f));

        g.dispose();

    }

    @Override

    public java.awt.Dimension getPreferredSize() {

        return new java.awt.Dimension(

                540,

                420);

    }

}
