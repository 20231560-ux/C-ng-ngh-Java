package com.restaurant.view;

import com.restaurant.model.BanAn;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * SAVORÉ - SƠ ĐỒ MẶT BẰNG VẬN HÀNH
 *
 * Giao diện mô phỏng theo mẫu SAVORÉ:
 *
 * 1. Header + bộ lọc.
 * 2. Sơ đồ mặt bằng dùng ảnh floor-map-savore.png.
 * 3. Khi rê chuột vào bàn: bàn được spotlight/glow.
 * 4. Khi click bàn: hiệu ứng zoom nhẹ vào khu vực bàn.
 * 5. Thông tin bàn + thao tác nhanh nằm phía dưới.
 * 6. Có trạng thái bàn và banner footer, không che mặt bằng.
 *
 * Ảnh cần có:
 * src/main/resources/images/floor-map-savore.png
 *
 * Có thể dùng thêm:
 * src/main/resources/images/restaurant-banner.png
 */
public class FloorMapVisualPanel extends JPanel {

    private final Consumer<String> onNavigate;
    private final List<TableItem> tables =
            new ArrayList<TableItem>();

    private int selectedTableIndex = 0;
    private int hoveredTableIndex = -1;

    private String selectedZone = "ALL";

    private JLabel lblInfoName;
    private JLabel lblInfoStatus;
    private JLabel lblInfoDetail;

    private JPanel filtersPanel;

    private Timer zoomTimer;
    private float zoomProgress = 0f;

    public static class TableItem {

        public final int id;
        public final String name;
        public final String zone;
        public final int capacity;

        public String status;

        /*
         * relX / relY là tọa độ tương đối trên ảnh.
         * Chỉ dùng để xác định vị trí click/zoom.
         */
        public final float relX;
        public final float relY;

        public TableItem(
                int id,
                String name,
                String zone,
                int capacity,
                String status,
                float relX,
                float relY) {

            this.id = id;
            this.name = name;
            this.zone = zone;
            this.capacity = capacity;
            this.status = status;
            this.relX = relX;
            this.relY = relY;
        }

        public Color getStatusColor() {

            if ("PHUC_VU".equals(status)) {
                return new Color(245, 158, 11);
            }

            if ("DA_DAT".equals(status)) {
                return new Color(59, 130, 246);
            }

            if ("CAN_DON".equals(status)) {
                return new Color(168, 85, 247);
            }

            if ("BAO_TRI".equals(status)) {
                return new Color(239, 68, 68);
            }

            return new Color(16, 185, 129);
        }

        public String getStatusText() {

            if ("PHUC_VU".equals(status)) {
                return "Đang phục vụ";
            }

            if ("DA_DAT".equals(status)) {
                return "Đã đặt";
            }

            if ("CAN_DON".equals(status)) {
                return "Cần dọn";
            }

            if ("BAO_TRI".equals(status)) {
                return "Bảo trì";
            }

            return "Bàn trống";
        }
    }

    public FloorMapVisualPanel(
            Consumer<String> onNavigate) {

        this.onNavigate = onNavigate;

        setOpaque(false);

        setLayout(
                new BorderLayout(
                        0,
                        7));

        khoiTaoDanhSachBan();

        add(
                taoHeader(),
                BorderLayout.NORTH);

        add(
                taoMapPanel(),
                BorderLayout.CENTER);

        add(
                taoBottomArea(),
                BorderLayout.SOUTH);

        capNhatThongTinBan();
    }

    private void khoiTaoDanhSachBan() {

        tables.clear();

        /*
         * Các vị trí này là vùng click.
         * KHÔNG vẽ bàn lên ảnh.
         */

        // VIP bên trái
        tables.add(new TableItem(
                1,
                "VIP 01",
                "VIP",
                8,
                "PHUC_VU",
                0.125f,
                0.235f));

        tables.add(new TableItem(
                2,
                "VIP 02",
                "VIP",
                10,
                "DA_DAT",
                0.125f,
                0.575f));

        // Khu trong nhà
        tables.add(new TableItem(
                3,
                "Bàn 01",
                "TRONG_NHA",
                4,
                "TRONG",
                0.310f,
                0.250f));

        tables.add(new TableItem(
                4,
                "Bàn 02",
                "TRONG_NHA",
                4,
                "DA_DAT",
                0.440f,
                0.250f));

        tables.add(new TableItem(
                5,
                "Bàn 03",
                "TRONG_NHA",
                6,
                "TRONG",
                0.310f,
                0.445f));

        tables.add(new TableItem(
                6,
                "Bàn 04",
                "TRONG_NHA",
                4,
                "PHUC_VU",
                0.440f,
                0.445f));

        tables.add(new TableItem(
                7,
                "Bàn 05",
                "TRONG_NHA",
                4,
                "TRONG",
                0.560f,
                0.445f));

        tables.add(new TableItem(
                8,
                "Bàn 06",
                "TRONG_NHA",
                4,
                "PHUC_VU",
                0.560f,
                0.250f));

        tables.add(new TableItem(
                9,
                "Bàn 07",
                "TRONG_NHA",
                4,
                "BAO_TRI",
                0.560f,
                0.650f));

        // Ngoài trời
        tables.add(new TableItem(
                10,
                "Bàn T1",
                "NGOAI_TROI",
                4,
                "TRONG",
                0.875f,
                0.235f));

        tables.add(new TableItem(
                11,
                "Bàn T2",
                "NGOAI_TROI",
                4,
                "TRONG",
                0.875f,
                0.545f));
    }

    /**
     * Hàm này vẫn được giữ để Dashboard truyền dữ liệu
     * trạng thái bàn thực tế từ database.
     */
    public void napDuLieuThucTe(
            List<BanAn> dsBan) {

        if (dsBan == null || dsBan.isEmpty()) {
            return;
        }

        for (int i = 0;
                i < Math.min(
                        dsBan.size(),
                        tables.size());
                i++) {

            String tt =
                    dsBan.get(i).getTrangThai();

            TableItem table =
                    tables.get(i);

            if ("DANG_DUNG".equalsIgnoreCase(tt)
                    || "CO_KHACH".equalsIgnoreCase(tt)) {

                table.status = "PHUC_VU";

            } else if ("DA_DAT".equalsIgnoreCase(tt)) {

                table.status = "DA_DAT";

            } else if ("CAN_DON".equalsIgnoreCase(tt)) {

                table.status = "CAN_DON";

            } else if ("BAO_TRI".equalsIgnoreCase(tt)) {

                table.status = "BAO_TRI";

            } else {

                table.status = "TRONG";
            }
        }

        capNhatThongTinBan();

        repaint();
    }

    private JPanel taoHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout());

        header.setOpaque(false);

        JPanel titleBox =
                new JPanel();

        titleBox.setOpaque(false);

        titleBox.setLayout(
                new BoxLayout(
                        titleBox,
                        BoxLayout.Y_AXIS));

        JLabel title =
                new JLabel(
                        "SƠ ĐỒ MẶT BẰNG VẬN HÀNH");

        title.setFont(
                SavoreDesignSystem.Fonts.get(
                        13,
                        Font.BOLD));

        title.setForeground(
                SavoreDesignSystem.Colors.WARM_GOLD);

        JLabel subtitle =
                new JLabel(
                        "Theo dõi trực quan • Quản lý hiệu quả");

        subtitle.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.PLAIN));

        subtitle.setForeground(
                new Color(
                        145,
                        155,
                        165));

        titleBox.add(title);

        titleBox.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                1)));

        titleBox.add(subtitle);

        filtersPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                6,
                                0));

        filtersPanel.setOpaque(false);

        String[] names = {
                "TẤT CẢ",
                "TRONG NHÀ",
                "KHU VIP",
                "NGOÀI TRỜI"
        };

        String[] keys = {
                "ALL",
                "TRONG_NHA",
                "VIP",
                "NGOAI_TROI"
        };

        for (int i = 0;
                i < names.length;
                i++) {

            final String key =
                    keys[i];

            JButton button =
                    taoFilterButton(
                            names[i],
                            key.equals(
                                    selectedZone));

            button.addActionListener(e -> {

                selectedZone = key;

                capNhatFilter();

                chonBanTheoKhu();

                repaint();
            });

            filtersPanel.add(button);
        }

        header.add(
                titleBox,
                BorderLayout.WEST);

        header.add(
                filtersPanel,
                BorderLayout.EAST);

        return header;
    }

    private JButton taoFilterButton(
            String text,
            boolean active) {

        JButton button =
                new JButton(text);

        button.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.BOLD));

        button.setForeground(
                active
                        ? new Color(
                                255,
                                220,
                                145)
                        : new Color(
                                150,
                                165,
                                180));

        button.setBackground(
                active
                        ? new Color(
                                77,
                                52,
                                19)
                        : new Color(
                                11,
                                24,
                                37));

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setContentAreaFilled(true);

        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        button.setPreferredSize(
                new Dimension(
                        88,
                        30));

        return button;
    }

    private void capNhatFilter() {

        if (filtersPanel == null) {
            return;
        }

        for (Component component :
                filtersPanel.getComponents()) {

            if (!(component instanceof JButton)) {
                continue;
            }

            JButton button =
                    (JButton) component;

            String text =
                    button.getText();

            boolean active =
                    ("TẤT CẢ".equals(text)
                            && "ALL".equals(
                                    selectedZone))
                    || ("TRONG NHÀ".equals(text)
                            && "TRONG_NHA".equals(
                                    selectedZone))
                    || ("KHU VIP".equals(text)
                            && "VIP".equals(
                                    selectedZone))
                    || ("NGOÀI TRỜI".equals(text)
                            && "NGOAI_TROI".equals(
                                    selectedZone));

            button.setForeground(
                    active
                            ? new Color(
                                    255,
                                    220,
                                    145)
                            : new Color(
                                    150,
                                    165,
                                    180));

            button.setBackground(
                    active
                            ? new Color(
                                    77,
                                    52,
                                    19)
                            : new Color(
                                    11,
                                    24,
                                    37));
        }
    }

    private void chonBanTheoKhu() {

        if ("ALL".equals(selectedZone)) {
            return;
        }

        for (int i = 0;
                i < tables.size();
                i++) {

            if (tables.get(i)
                    .zone
                    .equals(selectedZone)) {

                selectedTableIndex = i;

                capNhatThongTinBan();

                return;
            }
        }
    }

    private JPanel taoMapPanel() {

        return new MapCanvas();
    }

    private JPanel taoBottomArea() {

        /*
         * Bố cục hàng dưới theo đúng kiểu mẫu:
         *
         * [ THÔNG TIN BÀN ]
         * [ THAO TÁC NHANH ]
         * [ TRẠNG THÁI BÀN ]
         * [ GOOD FOOD / GOOD MOOD ]
         *
         * Không còn footer đen kéo dài toàn bộ chiều ngang.
         */
        JPanel root =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                8,
                                0));

        root.setOpaque(false);

        root.add(
                taoInformationPanel());

        root.add(
                taoQuickActionPanel());

        root.add(
                taoStatusPanel());

        root.add(
                taoQuotePanel());

        return root;
    }

    private JPanel taoInformationPanel() {

        JPanel panel =
                taoCardPanel();

        panel.setPreferredSize(
                new Dimension(
                        0,
                        92));

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        JLabel caption =
                new JLabel(
                        "THÔNG TIN BÀN");

        caption.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.BOLD));

        caption.setForeground(
                SavoreDesignSystem.Colors.WARM_GOLD);

        lblInfoName =
                new JLabel(
                        "Bàn 01 (4 khách)");

        lblInfoName.setFont(
                SavoreDesignSystem.Fonts.get(
                        11,
                        Font.BOLD));

        lblInfoName.setForeground(
                Color.WHITE);

        lblInfoStatus =
                new JLabel(
                        "● Bàn trống");

        lblInfoStatus.setFont(
                SavoreDesignSystem.Fonts.get(
                        9,
                        Font.BOLD));

        lblInfoDetail =
                new JLabel(
                        "Sẵn sàng đón khách mới");

        lblInfoDetail.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.PLAIN));

        lblInfoDetail.setForeground(
                new Color(
                        165,
                        175,
                        185));

        panel.add(caption);

        panel.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                2)));

        panel.add(lblInfoName);
        panel.add(lblInfoStatus);
        panel.add(lblInfoDetail);

        return panel;
    }

    private JPanel taoQuickActionPanel() {

        JPanel panel =
                taoCardPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        4));

        JLabel title =
                new JLabel(
                        "THAO TÁC NHANH");

        title.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.BOLD));

        title.setForeground(
                SavoreDesignSystem.Colors.WARM_GOLD);

        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                6,
                                0));

        buttons.setOpaque(false);

        buttons.add(
                taoActionButton(
                        "Xem đơn",
                        new Color(
                                28,
                                122,
                                214),
                        e -> navigate(
                                "DON_HANG")));

        buttons.add(
                taoActionButton(
                        "Gọi món",
                        new Color(
                                205,
                                151,
                                55),
                        e -> navigate(
                                "POS")));

        buttons.add(
                taoActionButton(
                        "Chuyển bàn",
                        new Color(
                                145,
                                76,
                                215),
                        e -> navigate(
                                "DAT_BAN")));

        buttons.add(
                taoActionButton(
                        "Thanh toán",
                        new Color(
                                12,
                                178,
                                119),
                        e -> navigate(
                                "POS")));

        panel.add(
                title,
                BorderLayout.NORTH);

        panel.add(
                buttons,
                BorderLayout.CENTER);

        return panel;
    }

    private JButton taoActionButton(
            String text,
            Color borderColor,
            ActionListener action) {

        final String iconType;

        if ("Xem đơn".equals(text)) {
            iconType = "ORDER";
        } else if ("Gọi món".equals(text)) {
            iconType = "FOOD";
        } else if ("Chuyển bàn".equals(text)) {
            iconType = "MOVE";
        } else {
            iconType = "PAY";
        }

        JButton button =
                new JButton(text) {

            @Override
            protected void paintComponent(
                    Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                boolean hover =
                        getModel().isRollover()
                                || getModel().isPressed();

                int w = getWidth();
                int h = getHeight();

                /*
                 * Glow ngoài rất nhẹ.
                 * Bình thường đã có viền sáng,
                 * hover thì viền sáng mạnh hơn.
                 */
                if (hover) {

                    for (int i = 8;
                            i >= 2;
                            i -= 2) {

                        int alpha =
                                5 + (8 - i) * 2;

                        g2.setColor(
                                new Color(
                                        borderColor.getRed(),
                                        borderColor.getGreen(),
                                        borderColor.getBlue(),
                                        alpha));

                        g2.drawRoundRect(
                                i / 2,
                                i / 2,
                                w - i,
                                h - i,
                                11,
                                11);
                    }
                }

                Color bg =
                        hover
                                ? new Color(
                                        borderColor.getRed(),
                                        borderColor.getGreen(),
                                        borderColor.getBlue(),
                                        55)
                                : new Color(
                                        15,
                                        28,
                                        43,
                                        245);

                g2.setColor(bg);

                g2.fillRoundRect(
                        0,
                        0,
                        w,
                        h,
                        10,
                        10);

                /*
                 * Viền luôn sáng.
                 */
                g2.setColor(
                        new Color(
                                borderColor.getRed(),
                                borderColor.getGreen(),
                                borderColor.getBlue(),
                                hover
                                        ? 245
                                        : 190));

                g2.setStroke(
                        new BasicStroke(
                                hover
                                        ? 1.7f
                                        : 1.1f));

                g2.drawRoundRect(
                        1,
                        1,
                        w - 3,
                        h - 3,
                        10,
                        10);

                /*
                 * Thanh sáng nhỏ phía trên.
                 */
                if (hover) {

                    g2.setColor(
                            new Color(
                                    255,
                                    220,
                                    145,
                                    210));

                    g2.fillRoundRect(
                            10,
                            2,
                            Math.max(
                                    5,
                                    w - 20),
                            2,
                            2,
                            2);
                }

                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.BOLD));

        button.setForeground(
                Color.WHITE);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setContentAreaFilled(false);

        button.setOpaque(false);

        button.setMargin(
                new java.awt.Insets(
                        2,
                        2,
                        4,
                        2));

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        /*
         * Icon vector tự vẽ, không phụ thuộc emoji/font.
         */
        button.setIcon(
                new ActionIcon(
                        iconType,
                        borderColor));

        button.setHorizontalAlignment(
                javax.swing.SwingConstants.CENTER);

        button.setHorizontalTextPosition(
                javax.swing.SwingConstants.CENTER);

        button.setVerticalTextPosition(
                javax.swing.SwingConstants.BOTTOM);

        button.setIconTextGap(3);

        button.addActionListener(action);

        return button;
    }

    /**
     * Icon vector nhỏ cho các nút thao tác.
     * Không cần thêm file PNG nào.
     */
    private static class ActionIcon
            implements javax.swing.Icon {

        private final String type;
        private final Color color;

        ActionIcon(
                String type,
                Color color) {

            this.type = type;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return 25;
        }

        @Override
        public int getIconHeight() {
            return 25;
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            boolean active =
                    c instanceof JButton
                            && ((JButton) c)
                                    .getModel()
                                    .isRollover();

            Color iconColor =
                    active
                            ? new Color(
                                    255,
                                    220,
                                    145)
                            : color;

            g2.setColor(iconColor);

            g2.setStroke(
                    new BasicStroke(
                            1.8f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));

            int cx = x + 12;
            int cy = y + 11;

            if ("ORDER".equals(type)) {

                // Tờ đơn / hóa đơn
                g2.drawRoundRect(
                        x + 6,
                        y + 3,
                        13,
                        18,
                        2,
                        2);

                g2.drawLine(
                        x + 9,
                        y + 8,
                        x + 16,
                        y + 8);

                g2.drawLine(
                        x + 9,
                        y + 12,
                        x + 16,
                        y + 12);

                g2.drawLine(
                        x + 9,
                        y + 16,
                        x + 14,
                        y + 16);

            } else if ("FOOD".equals(type)) {

                // Khay phục vụ
                g2.drawArc(
                        x + 5,
                        y + 6,
                        14,
                        10,
                        0,
                        180);

                g2.drawLine(
                        x + 4,
                        y + 15,
                        x + 20,
                        y + 15);

                g2.drawLine(
                        x + 12,
                        y + 15,
                        x + 12,
                        y + 20);

                g2.drawLine(
                        x + 8,
                        y + 20,
                        x + 16,
                        y + 20);

                g2.drawLine(
                        x + 9,
                        y + 5,
                        x + 9,
                        y + 2);

                g2.drawLine(
                        x + 12,
                        y + 5,
                        x + 12,
                        y + 2);

            } else if ("MOVE".equals(type)) {

                // Hai mũi tên chuyển bàn
                g2.drawLine(
                        x + 4,
                        y + 8,
                        x + 19,
                        y + 8);

                g2.drawLine(
                        x + 19,
                        y + 8,
                        x + 15,
                        y + 5);

                g2.drawLine(
                        x + 19,
                        y + 8,
                        x + 15,
                        y + 11);

                g2.drawLine(
                        x + 20,
                        y + 16,
                        x + 5,
                        y + 16);

                g2.drawLine(
                        x + 5,
                        y + 16,
                        x + 9,
                        y + 13);

                g2.drawLine(
                        x + 5,
                        y + 16,
                        x + 9,
                        y + 19);

            } else {

                // Thẻ thanh toán
                g2.drawRoundRect(
                        x + 3,
                        y + 6,
                        18,
                        13,
                        2,
                        2);

                g2.drawLine(
                        x + 4,
                        y + 10,
                        x + 20,
                        y + 10);

                g2.drawLine(
                        x + 7,
                        y + 15,
                        x + 12,
                        y + 15);
            }

            g2.dispose();
        }
    }

    private JPanel taoStatusPanel() {

        JPanel panel =
                taoCardPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        4));

        JLabel title =
                new JLabel(
                        "TRẠNG THÁI BÀN");

        title.setFont(
                SavoreDesignSystem.Fonts.get(
                        8,
                        Font.BOLD));

        title.setForeground(
                SavoreDesignSystem.Colors.WARM_GOLD);

        JPanel list =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                3,
                                3));

        list.setOpaque(false);

        addStatusItem(
                list,
                "Trống",
                new Color(
                        16,
                        185,
                        129));

        addStatusItem(
                list,
                "Đang phục vụ",
                new Color(
                        245,
                        158,
                        11));

        addStatusItem(
                list,
                "Đã đặt",
                new Color(
                        59,
                        130,
                        246));

        addStatusItem(
                list,
                "Cần dọn",
                new Color(
                        168,
                        85,
                        247));

        addStatusItem(
                list,
                "Bảo trì",
                new Color(
                        239,
                        68,
                        68));

        panel.add(
                title,
                BorderLayout.NORTH);

        panel.add(
                list,
                BorderLayout.CENTER);

        return panel;
    }

    private void addStatusItem(
            JPanel parent,
            String text,
            Color color) {

        JPanel item =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                2,
                                0));

        item.setOpaque(false);

        JLabel dot =
                new JLabel("●");

        dot.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10));

        dot.setForeground(color);

        JLabel label =
                new JLabel(text);

        label.setFont(
                SavoreDesignSystem.Fonts.get(
                        7,
                        Font.PLAIN));

        label.setForeground(
                new Color(
                        185,
                        195,
                        205));

        item.add(dot);
        item.add(label);

        parent.add(item);
    }

    private JPanel taoCardPanel() {

        JPanel panel =
                new JPanel() {

            @Override
            protected void paintComponent(
                    Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gradient =
                        new GradientPaint(
                                0,
                                0,
                                new Color(
                                        14,
                                        25,
                                        35,
                                        250),
                                w,
                                h,
                                new Color(
                                        5,
                                        13,
                                        20,
                                        250));

                g2.setPaint(gradient);

                g2.fillRoundRect(
                        0,
                        0,
                        w,
                        h,
                        12,
                        12);

                g2.setColor(
                        new Color(
                                200,
                                146,
                                69,
                                145));

                g2.setStroke(
                        new BasicStroke(
                                1.0f));

                g2.drawRoundRect(
                        0,
                        0,
                        w - 1,
                        h - 1,
                        12,
                        12);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        7,
                        9,
                        7,
                        9));

        return panel;
    }

    private JPanel taoFooterBanner() {

        JPanel banner =
                new JPanel(
                        new BorderLayout()) {

            private Image bannerImage;

            {
                java.net.URL url =
                        getClass().getResource(
                                "/images/restaurant-banner.png");

                if (url != null) {
                    bannerImage =
                            new ImageIcon(url)
                                    .getImage();
                }
            }

            @Override
            protected void paintComponent(
                    Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (bannerImage != null) {

                    int iw =
                            bannerImage.getWidth(
                                    this);

                    int ih =
                            bannerImage.getHeight(
                                    this);

                    if (iw > 0 && ih > 0) {

                        double sx =
                                (double) w / iw;

                        double sy =
                                (double) h / ih;

                        double scale =
                                Math.max(
                                        sx,
                                        sy);

                        int dw =
                                (int) (iw * scale);

                        int dh =
                                (int) (ih * scale);

                        int dx =
                                (w - dw) / 2;

                        int dy =
                                (h - dh) / 2;

                        g2.drawImage(
                                bannerImage,
                                dx,
                                dy,
                                dw,
                                dh,
                                this);

                        g2.setColor(
                                new Color(
                                        3,
                                        10,
                                        16,
                                        125));

                        g2.fillRect(
                                0,
                                0,
                                w,
                                h);
                    }

                } else {

                    GradientPaint gp =
                            new GradientPaint(
                                    0,
                                    0,
                                    new Color(
                                            55,
                                            38,
                                            18),
                                    w,
                                    0,
                                    new Color(
                                            8,
                                            18,
                                            27));

                    g2.setPaint(gp);

                    g2.fillRoundRect(
                            0,
                            0,
                            w,
                            h,
                            12,
                            12);
                }

                g2.setColor(
                        new Color(
                                231,
                                190,
                                115,
                                220));

                g2.setFont(
                        new Font(
                                "Serif",
                                Font.ITALIC,
                                11));

                g2.drawString(
                        "Tinh hoa ẩm thực   •   Trải nghiệm khác biệt",
                        15,
                        h / 2 + 4);

                g2.setFont(
                        SavoreDesignSystem.Fonts.get(
                                8,
                                Font.BOLD));

                g2.drawString(
                        "SAVORÉ  •  GOOD FOOD  •  GOOD MOOD",
                        Math.max(
                                15,
                                w - 220),
                        h / 2 + 4);

                g2.setColor(
                        new Color(
                                210,
                                155,
                                68,
                                175));

                g2.drawRoundRect(
                        0,
                        0,
                        w - 1,
                        h - 1,
                        12,
                        12);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        banner.setOpaque(false);

        banner.setPreferredSize(
                new Dimension(
                        0,
                        34));

        return banner;
    }

    private JPanel taoQuotePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()) {

            private Image image;

            {
                java.net.URL url =
                        getClass().getResource(
                                "/images/restaurant-banner.png");

                if (url != null) {
                    image =
                            new ImageIcon(url)
                                    .getImage();
                }
            }

            @Override
            protected void paintComponent(
                    Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                if (image != null) {

                    int iw =
                            image.getWidth(this);

                    int ih =
                            image.getHeight(this);

                    if (iw > 0 && ih > 0) {

                        double sx =
                                (double) w / iw;

                        double sy =
                                (double) h / ih;

                        double scale =
                                Math.max(sx, sy);

                        int dw =
                                (int) (iw * scale);

                        int dh =
                                (int) (ih * scale);

                        int dx =
                                (w - dw) / 2;

                        int dy =
                                (h - dh) / 2;

                        g2.drawImage(
                                image,
                                dx,
                                dy,
                                dw,
                                dh,
                                this);

                        g2.setColor(
                                new Color(
                                        3,
                                        10,
                                        16,
                                        155));

                        g2.fillRect(
                                0,
                                0,
                                w,
                                h);
                    }
                } else {

                    g2.setColor(
                            new Color(
                                    8,
                                    18,
                                    27));

                    g2.fillRect(
                            0,
                            0,
                            w,
                            h);
                }

                g2.setColor(
                        new Color(
                                218,
                                170,
                                86));

                g2.setFont(
                        new Font(
                                "Serif",
                                Font.ITALIC,
                                17));

                g2.drawString(
                        "Good Food",
                        18,
                        h / 2 - 2);

                g2.drawString(
                        "Good Mood",
                        18,
                        h / 2 + 18);

                g2.setFont(
                        SavoreDesignSystem.Fonts.get(
                                7,
                                Font.BOLD));

                g2.drawString(
                        "SAVORÉ RESTAURANT",
                        20,
                        h - 9);

                g2.setColor(
                        new Color(
                                205,
                                150,
                                65,
                                180));

                g2.drawRoundRect(
                        0,
                        0,
                        w - 1,
                        h - 1,
                        12,
                        12);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        10));

        return panel;
    }

    private void navigate(
            String target) {

        if (onNavigate != null) {
            onNavigate.accept(target);
        }
    }

    private void capNhatThongTinBan() {

        if (lblInfoName == null
                || tables.isEmpty()
                || selectedTableIndex < 0
                || selectedTableIndex >= tables.size()) {

            return;
        }

        TableItem table =
                tables.get(
                        selectedTableIndex);

        lblInfoName.setText(
                table.name
                        + " ("
                        + table.capacity
                        + " khách)");

        lblInfoStatus.setText(
                "● "
                        + table.getStatusText());

        lblInfoStatus.setForeground(
                table.getStatusColor());

        if ("PHUC_VU".equals(
                table.status)) {

            lblInfoDetail.setText(
                    "Hóa đơn đang mở • Bếp đang xử lý");

        } else if ("DA_DAT".equals(
                table.status)) {

            lblInfoDetail.setText(
                    "Đã đặt trước • Chờ khách đến");

        } else if ("CAN_DON".equals(
                table.status)) {

            lblInfoDetail.setText(
                    "Đã thanh toán • Chờ dọn bàn");

        } else if ("BAO_TRI".equals(
                table.status)) {

            lblInfoDetail.setText(
                    "Bàn đang được bảo trì");

        } else {

            lblInfoDetail.setText(
                    "Sẵn sàng đón khách mới");
        }
    }

    private class MapCanvas
            extends JPanel {

        private Image floorMapImage;

        MapCanvas() {

            setOpaque(false);

            java.net.URL url =
                    getClass().getResource(
                            "/images/floor-map-savore.png");

            if (url != null) {

                floorMapImage =
                        new ImageIcon(url)
                                .getImage();

            } else {

                System.err.println(
                        "Không tìm thấy: "
                                + "/images/floor-map-savore.png");
            }

            MouseAdapter mouse =
                    new MouseAdapter() {

                @Override
                public void mouseMoved(
                        MouseEvent e) {

                    int index =
                            timBanTaiToaDo(
                                    e.getX(),
                                    e.getY());

                    if (index != hoveredTableIndex) {

                        hoveredTableIndex =
                                index;

                        if (index >= 0) {
                            batDauZoom();
                        } else {
                            dungZoom();
                        }

                        repaint();
                    }

                    setCursor(
                            index >= 0
                                    ? Cursor.getPredefinedCursor(
                                            Cursor.HAND_CURSOR)
                                    : Cursor.getDefaultCursor());
                }

                @Override
                public void mouseExited(
                        MouseEvent e) {

                    hoveredTableIndex = -1;

                    dungZoom();

                    setCursor(
                            Cursor.getDefaultCursor());

                    repaint();
                }

                @Override
                public void mouseClicked(
                        MouseEvent e) {

                    int index =
                            timBanTaiToaDo(
                                    e.getX(),
                                    e.getY());

                    if (index >= 0) {

                        selectedTableIndex =
                                index;

                        /*
                         * Click chỉ chọn bàn.
                         * Hiệu ứng chính vẫn là hover nhẹ.
                         */
                        hoveredTableIndex =
                                index;

                        capNhatThongTinBan();

                        repaint();
                    }
                }
            };

            addMouseListener(mouse);
            addMouseMotionListener(mouse);
        }

        private void batDauZoom() {

            if (zoomTimer != null
                    && zoomTimer.isRunning()) {

                return;
            }

            zoomTimer =
                    new Timer(
                            16,
                            e -> {

                                zoomProgress +=
                                        0.10f;

                                if (zoomProgress >= 1f) {

                                    zoomProgress =
                                            1f;

                                    zoomTimer.stop();
                                }

                                repaint();
                            });

            zoomTimer.start();
        }

        private void dungZoom() {

            if (zoomTimer != null
                    && zoomTimer.isRunning()) {

                zoomTimer.stop();
            }

            zoomProgress = 0f;

            repaint();
        }

        private int timBanTaiToaDo(
                int mouseX,
                int mouseY) {

            int w = getWidth();
            int h = getHeight();

            if (w <= 0 || h <= 0) {
                return -1;
            }

            for (int i = 0;
                    i < tables.size();
                    i++) {

                TableItem table =
                        tables.get(i);

                if (!"ALL".equals(
                        selectedZone)
                        && !table.zone.equals(
                                selectedZone)) {

                    continue;
                }

                float x =
                        table.relX * w;

                float y =
                        table.relY * h;

                if (Point2D.distance(
                        mouseX,
                        mouseY,
                        x,
                        y) < 55) {

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
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int w = getWidth();
            int h = getHeight();

            if (w <= 10 || h <= 10) {
                g2.dispose();
                return;
            }

            g2.setColor(
                    new Color(
                            4,
                            9,
                            14));

            g2.fillRoundRect(
                    0,
                    0,
                    w,
                    h,
                    16,
                    16);

            int drawX = 0;
            int drawY = 0;
            int drawW = w;
            int drawH = h;

            if (floorMapImage != null) {

                int iw =
                        floorMapImage.getWidth(
                                this);

                int ih =
                        floorMapImage.getHeight(
                                this);

                if (iw > 0 && ih > 0) {

                    /*
                     * FIT: giữ toàn bộ mặt bằng.
                     */
                    double sx =
                            (double) w / iw;

                    double sy =
                            (double) h / ih;

                    double scale =
                            Math.min(
                                    sx,
                                    sy);

                    drawW =
                            (int) (iw * scale);

                    drawH =
                            (int) (ih * scale);

                    drawX =
                            (w - drawW) / 2;

                    drawY =
                            (h - drawH) / 2;

                    g2.drawImage(
                            floorMapImage,
                            drawX,
                            drawY,
                            drawW,
                            drawH,
                            this);
                }
            }

            /*
             * HIỆU ỨNG HOVER NHẸ:
             *
             * Không zoom cả ảnh nữa.
             * Không làm tối toàn bộ mặt bằng.
             * Chỉ tạo một lớp glow nhỏ tại đúng vị trí
             * của bàn đang được rê chuột vào.
             *
             * Mục tiêu là cảm giác "bàn đang được chọn"
             * giống UI dashboard cao cấp, nhưng không phá
             * bố cục của ảnh nền.
             */
            int focusIndex =
                    hoveredTableIndex >= 0
                            ? hoveredTableIndex
                            : -1;

            if (focusIndex >= 0
                    && focusIndex < tables.size()) {

                TableItem focus =
                        tables.get(focusIndex);

                float cx =
                        drawX
                                + focus.relX * drawW;

                float cy =
                        drawY
                                + focus.relY * drawH;

                Color glow =
                        focus.getStatusColor();

                /*
                 * Glow rất nhẹ: 3 lớp, nhỏ dần.
                 */
                for (int r = 34;
                        r >= 22;
                        r -= 4) {

                    int alpha =
                            (int)
                                    (4
                                            + zoomProgress
                                            * 8);

                    g2.setColor(
                            new Color(
                                    glow.getRed(),
                                    glow.getGreen(),
                                    glow.getBlue(),
                                    alpha));

                    g2.fillOval(
                            (int) cx - r,
                            (int) cy - r,
                            r * 2,
                            r * 2);
                }

                /*
                 * Một vòng vàng mảnh, không phóng ảnh.
                 */
                int ringAlpha =
                        (int)
                                (105
                                        + 65
                                        * zoomProgress);

                g2.setColor(
                        new Color(
                                245,
                                190,
                                80,
                                ringAlpha));

                g2.setStroke(
                        new BasicStroke(
                                1.4f));

                g2.drawOval(
                        (int) cx - 29,
                        (int) cy - 29,
                        58,
                        58);

                /*
                 * 4 góc nhỏ tạo cảm giác focus.
                 */
                g2.setStroke(
                        new BasicStroke(
                                2.0f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND));

                int s = 8;
                int d = 33;

                g2.drawLine(
                        (int) cx - d,
                        (int) cy - d,
                        (int) cx - d + s,
                        (int) cy - d);

                g2.drawLine(
                        (int) cx - d,
                        (int) cy - d,
                        (int) cx - d,
                        (int) cy - d + s);

                g2.drawLine(
                        (int) cx + d,
                        (int) cy - d,
                        (int) cx + d - s,
                        (int) cy - d);

                g2.drawLine(
                        (int) cx + d,
                        (int) cy - d,
                        (int) cx + d,
                        (int) cy - d + s);

                g2.drawLine(
                        (int) cx - d,
                        (int) cy + d,
                        (int) cx - d + s,
                        (int) cy + d);

                g2.drawLine(
                        (int) cx - d,
                        (int) cy + d,
                        (int) cx - d,
                        (int) cy + d - s);

                g2.drawLine(
                        (int) cx + d,
                        (int) cy + d,
                        (int) cx + d - s,
                        (int) cy + d);

                g2.drawLine(
                        (int) cx + d,
                        (int) cy + d,
                        (int) cx + d,
                        (int) cy + d - s);
            }

            /*
             * Viền vàng.
             */
            g2.setColor(
                    new Color(
                            200,
                            146,
                            69,
                            175));

            g2.setStroke(
                    new BasicStroke(
                            1.1f));

            g2.drawRoundRect(
                    0,
                    0,
                    w - 1,
                    h - 1,
                    16,
                    16);

            g2.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(
                900,
                570);
    }
}
