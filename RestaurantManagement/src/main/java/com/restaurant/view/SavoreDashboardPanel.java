package com.restaurant.view;

import com.restaurant.dao.BanAnDAO;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.dao.KhachHangDAO;
import com.restaurant.dao.NguyenLieuDAO;
import com.restaurant.model.BanAn;
import com.restaurant.model.DonHang;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * SAVORÉ RESTAURANT OPERATIONS COMMAND CENTER
 * Màn hình Trang Chủ cao cấp theo bản thiết kế Dark Luxury.
 *
 * Tích hợp:
 * - Banner nhà hàng
 * - Operations Hub
 * - Sơ đồ mặt bằng
 * - Live Kitchen Queue
 * - Activity Timeline
 * - Biểu đồ doanh thu
 * - Quick Action Dock
 */
public class SavoreDashboardPanel extends JPanel implements Scrollable {

    private final NguoiDung nguoiDung;
    private final Consumer<String> chuyenPhanHeCallback;

    // ==========================================================
    // SUB-COMPONENTS
    // ==========================================================

    private OperationsHubPanel hubPanel;
    private SmoothRevenueChartPanel chartPanel;
    private FloorMapVisualPanel floorMapPanel;
    private LiveKitchenQueuePanel kitchenPanel;
    private ActivityTimelinePanel timelinePanel;
    private QuickActionDock quickDock;

    // ==========================================================
    // DAOs
    // ==========================================================

    private final BanAnDAO banAnDAO = new BanAnDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO();

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public SavoreDashboardPanel(
            NguoiDung nguoiDung,
            Consumer<String> chuyenPhanHeCallback) {

        this.nguoiDung = nguoiDung;
        this.chuyenPhanHeCallback = chuyenPhanHeCallback;

        setOpaque(false);

        setLayout(
                new BorderLayout(0, 4)
        );

        setBorder(
                new EmptyBorder(6, 12, 6, 12)
        );

        dungGiaoDien();

        napDuLieu();
    }

    // ==========================================================
    // DỰNG GIAO DIỆN
    // ==========================================================

    private void dungGiaoDien() {

        // ======================================================
        // CONTAINER NỘI DUNG CUỘN DỌC
        // ======================================================

        class ScrollContentPanel
                extends JPanel
                implements Scrollable {

            public ScrollContentPanel() {

                super(
                        new BorderLayout(0, 8)
                );

                setOpaque(false);

                setBorder(
                        new EmptyBorder(6, 4, 6, 4)
                );
            }

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return getPreferredSize();
            }

            @Override
            public int getScrollableUnitIncrement(
                    Rectangle r,
                    int orientation,
                    int direction) {

                return 18;
            }

            @Override
            public int getScrollableBlockIncrement(
                    Rectangle r,
                    int orientation,
                    int direction) {

                return 48;
            }

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }

            @Override
            public boolean getScrollableTracksViewportHeight() {
                return false;
            }
        }

        ScrollContentPanel pnlScrollContent =
                new ScrollContentPanel();

        // ======================================================
        // 1. BANNER NHÀ HÀNG
        // ======================================================

        pnlScrollContent.add(
                taoHeroArea(),
                BorderLayout.NORTH
        );

        // ======================================================
        // 2. MAIN OPERATIONS CANVAS
        // ======================================================

        JPanel pnlCenter =
                new JPanel(
                        new GridBagLayout()
                );

        pnlCenter.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.BOTH;

        gbc.insets =
                new Insets(5, 5, 5, 5);

        // ======================================================
        // HÀNG 1
        //
        // OPERATIONS HUB 58%
        // +
        // REVENUE CHART 42%
        // ======================================================

        hubPanel =
                new OperationsHubPanel(
                        chuyenPhanHeCallback
                );

        chartPanel =
                new SmoothRevenueChartPanel(
                        chuyenPhanHeCallback
                );

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 0.58;
        gbc.weighty = 0.54;

        pnlCenter.add(
                hubPanel,
                gbc
        );

        gbc.gridx = 1;
        gbc.gridy = 0;

        gbc.weightx = 0.42;
        gbc.weighty = 0.54;

        pnlCenter.add(
                chartPanel,
                gbc
        );

        // ======================================================
        // HÀNG 2
        //
        // FLOOR MAP 52%
        // +
        // KITCHEN QUEUE & TIMELINE 48%
        // ======================================================

        floorMapPanel =
                new FloorMapVisualPanel(
                        chuyenPhanHeCallback
                );

        JPanel pnlRightOps =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                8,
                                0
                        )
                );

        pnlRightOps.setOpaque(false);

        kitchenPanel =
                new LiveKitchenQueuePanel(
                        chuyenPhanHeCallback
                );

        timelinePanel =
                new ActivityTimelinePanel(
                        chuyenPhanHeCallback
                );

        pnlRightOps.add(
                kitchenPanel
        );

        pnlRightOps.add(
                timelinePanel
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        gbc.weightx = 0.52;
        gbc.weighty = 0.46;

        pnlCenter.add(
                floorMapPanel,
                gbc
        );

        gbc.gridx = 1;
        gbc.gridy = 1;

        gbc.weightx = 0.48;
        gbc.weighty = 0.46;

        pnlCenter.add(
                pnlRightOps,
                gbc
        );

        pnlScrollContent.add(
                pnlCenter,
                BorderLayout.CENTER
        );

        // ======================================================
        // SCROLL
        // ======================================================

        JScrollPane scroll =
                new JScrollPane(
                        pnlScrollContent
                );

        scroll.setBorder(null);

        scroll.setOpaque(false);

        scroll.getViewport()
                .setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(18);

        scroll.getVerticalScrollBar()
                .setPreferredSize(
                        new Dimension(6, 0)
                );

        add(
                scroll,
                BorderLayout.CENTER
        );

        // ======================================================
        // QUICK ACTION DOCK
        // ======================================================

        quickDock =
                new QuickActionDock(
                        chuyenPhanHeCallback
                );

        JPanel pnlDockContainer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                4
                        )
                );

        pnlDockContainer.setOpaque(false);

        pnlDockContainer.add(
                quickDock
        );

        add(
                pnlDockContainer,
                BorderLayout.SOUTH
        );
    }

    // ==========================================================
    // BANNER NHÀ HÀNG
    // ==========================================================

    private JPanel taoHeroArea() {

        JPanel hero =
                new JPanel(
                        new BorderLayout()
                ) {

            private Image backgroundImage;

            // --------------------------------------------------
            // LOAD ẢNH
            // --------------------------------------------------

            {
                java.net.URL imageUrl =
                        getClass().getResource(
                                "/images/restaurant-banner.png"
                        );

                if (imageUrl != null) {

                    backgroundImage =
                            new ImageIcon(
                                    imageUrl
                            ).getImage();
                }
            }

            // --------------------------------------------------
            // VẼ BANNER
            // --------------------------------------------------

            @Override
            protected void paintComponent(
                    Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g.create();

                // Chống răng cưa
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                // Làm ảnh mượt khi phóng to
                g2.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR
                );

                int w = getWidth();
                int h = getHeight();

                // ==================================================
                // 1. NỀN TỐI
                // ==================================================

                g2.setColor(
                        new Color(
                                5,
                                10,
                                15
                        )
                );

                g2.fillRoundRect(
                        0,
                        0,
                        w,
                        h,
                        20,
                        20
                );

                // ==================================================
                // 2. VẼ ẢNH restaurant-banner.png
                // ==================================================

                if (backgroundImage != null) {

                    int imageWidth =
                            backgroundImage.getWidth(null);

                    int imageHeight =
                            backgroundImage.getHeight(null);

                    if (imageWidth > 0 &&
                            imageHeight > 0) {

                        /*
                         * Scale ảnh để phủ toàn bộ banner.
                         *
                         * Math.max giúp ảnh luôn phủ kín
                         * chiều rộng và chiều cao.
                         */

                        double scale =
                                Math.max(
                                        (double) w / imageWidth,
                                        (double) h / imageHeight
                                );

                        int newWidth =
                                (int)
                                (imageWidth * scale);

                        int newHeight =
                                (int)
                                (imageHeight * scale);

                        // Căn giữa ảnh
                        int x =
                                (w - newWidth) / 2;

                        int y =
                                (h - newHeight) / 2;

                        g2.drawImage(
                                backgroundImage,
                                x,
                                y,
                                newWidth,
                                newHeight,
                                this
                        );
                    }

                } else {

                    // ==================================================
                    // NẾU KHÔNG TÌM THẤY ẢNH
                    // ==================================================

                    g2.setColor(
                            new Color(
                                    15,
                                    22,
                                    32
                            )
                    );

                    g2.fillRoundRect(
                            0,
                            0,
                            w,
                            h,
                            20,
                            20
                    );

                    g2.setColor(
                            Color.WHITE
                    );

                    g2.setFont(
                            new Font(
                                    "Segoe UI",
                                    Font.BOLD,
                                    15
                            )
                    );

                    g2.drawString(
                            "Không tìm thấy restaurant-banner.png",
                            20,
                            30
                    );
                }

                // ==================================================
                // 3. LỚP TỐI NHẸ
                //
                // Giúp chữ / giao diện phía trên nổi bật
                // ==================================================

                g2.setColor(
                        new Color(
                                0,
                                0,
                                0,
                                25
                        )
                );

                g2.fillRoundRect(
                        0,
                        0,
                        w,
                        h,
                        20,
                        20
                );

                // ==================================================
                // 4. VIỀN VÀNG SAVORÉ
                // ==================================================

                g2.setColor(
                        new Color(
                                200,
                                146,
                                69,
                                140
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
                        20,
                        20
                );

                g2.dispose();
            }
        };

        // ======================================================
        // CẤU HÌNH KÍCH THƯỚC BANNER
        // ======================================================

        hero.setOpaque(false);

        /*
         * Banner cao hơn trước.
         *
         * 800  = chiều rộng tối thiểu tham khảo
         * 155  = chiều cao banner
         */

        hero.setPreferredSize(
                new Dimension(
                        800,
                        155
                )
        );

        hero.setMinimumSize(
                new Dimension(
                        500,
                        130
                )
        );

        hero.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        180
                )
        );

        return hero;
    }

    // ==========================================================
    // NẠP DỮ LIỆU
    // ==========================================================

    public void napDuLieu() {

        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {

            int tongBan = 24;

            int banDung = 12;

            int tongDon = 36;

            int khachVip = 15;

            int donOnline = 8;

            double tongDoanhThu =
                    12450000;

            List<BanAn> dsBan =
                    new ArrayList<>();

            // --------------------------------------------------
            // ĐỌC DATABASE
            // --------------------------------------------------

            @Override
            protected Void doInBackground() {

                // ==================================================
                // BÀN ĂN
                // ==================================================

                try {

                    List<BanAn> bans =
                            banAnDAO.layTatCa();

                    if (bans != null &&
                            !bans.isEmpty()) {

                        dsBan = bans;

                        tongBan =
                                bans.size();

                        banDung =
                                (int)
                                bans.stream()
                                        .filter(
                                                b ->
                                                        !"TRONG"
                                                        .equalsIgnoreCase(
                                                                b.getTrangThai()
                                                        )
                                        )
                                        .count();
                    }

                } catch (Exception ignored) {
                }

                // ==================================================
                // ĐƠN HÀNG
                // ==================================================

                try {

                    List<DonHang> dons =
                            donHangDAO.layDanhSachDonHang(
                                    200
                            );

                    if (dons != null &&
                            !dons.isEmpty()) {

                        tongDon =
                                dons.size();

                        tongDoanhThu =
                                dons.stream()
                                        .filter(
                                                d ->
                                                        "HOAN_THANH"
                                                        .equalsIgnoreCase(
                                                                d.getTrangThai()
                                                        )
                                        )
                                        .mapToDouble(
                                                DonHang::getTongTien
                                        )
                                        .sum();
                    }

                } catch (Exception ignored) {
                }

                // ==================================================
                // KHÁCH HÀNG
                // ==================================================

                try {

                    List<com.restaurant.model.KhachHang> khs =
                            khachHangDAO.findAll();

                    khachVip =
                            (
                                    khs != null &&
                                    !khs.isEmpty()
                            )
                            ?
                            khs.size()
                            :
                            15;

                } catch (Exception ignored) {
                }

                return null;
            }

            // --------------------------------------------------
            // CẬP NHẬT GIAO DIỆN
            // --------------------------------------------------

            @Override
            protected void done() {

                if (hubPanel != null) {

                    hubPanel.capNhatSoLieu(
                            banDung,
                            tongBan,
                            tongDon,
                            khachVip,
                            donOnline,
                            "Bình thường",
                            "Đang nấu 8 món"
                    );
                }

                if (floorMapPanel != null &&
                        !dsBan.isEmpty()) {

                    floorMapPanel.napDuLieuThucTe(
                            dsBan
                    );
                }

                if (chartPanel != null &&
                        tongDoanhThu > 0) {

                    chartPanel.capNhatDoanhThu(
                            tongDoanhThu,
                            tongDoanhThu * 0.88,
                            null,
                            null
                    );
                }
            }
        };

        worker.execute();
    }

    // ==========================================================
    // SCROLLABLE
    // ==========================================================

    @Override
    public Dimension getPreferredScrollableViewportSize() {

        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(
            Rectangle r,
            int orientation,
            int direction) {

        return 18;
    }

    @Override
    public int getScrollableBlockIncrement(
            Rectangle r,
            int orientation,
            int direction) {

        return 48;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {

        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {

        return false;
    }
}