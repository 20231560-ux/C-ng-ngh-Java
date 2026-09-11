package com.restaurant.view;

import com.restaurant.dao.BanAnDAO;
import com.restaurant.dao.ChiTietDonHangDAO;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.dao.MonAnDAO;
import com.restaurant.model.BanAn;
import com.restaurant.model.DonHang;
import com.restaurant.model.MonAn;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 3/12 — BÁN HÀNG POS (3-COLUMN POS COMMAND CENTER)
 * Thiết kế chuẩn 1:1 theo ô 3/12 của ảnh tham chiếu mục tiêu.
 */
public class SavorePosPanel extends JPanel implements Scrollable {

    private final NguoiDung nguoiDung;
    private final MonAnDAO monAnDAO = new MonAnDAO();
    private final BanAnDAO banAnDAO = new BanAnDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    private String selectedCategory = "Món chính";
    private int selectedTableId = 2;
    private String selectedTableName = "Bàn 02 - Tầng 1";

    private final List<OrderItem> orderItems = new ArrayList<>();
    private JPanel pnlOrderList;
    private JLabel lblSubtotal;
    private JLabel lblDiscount;
    private JLabel lblTax;
    private JLabel lblTotal;
    private JLabel lblTableTitle;
    private JPanel pnlMenuGrid;

    public static class OrderItem {
        public String name;
        public double price;
        public int qty;

        public OrderItem(String name, double price, int qty) {
            this.name = name;
            this.price = price;
            this.qty = qty;
        }
    }

    public SavorePosPanel() {
        this(null);
    }

    public SavorePosPanel(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;

        setLayout(new BorderLayout());
        setOpaque(false);

        // Nạp một số món mặc định vào phiếu order bàn 02
        orderItems.add(new OrderItem("Bò Fuji Nướng Đá", 280000, 1));
        orderItems.add(new OrderItem("Lẩu Hải Sản TomYum", 320000, 1));
        orderItems.add(new OrderItem("Coca Cola", 25000, 2));

        JScrollPane scrollPane = new JScrollPane(taoNoiDung());
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel taoNoiDung() {
        class ContainerPanel extends JPanel implements Scrollable {
            ContainerPanel() {
                super(new BorderLayout(10, 0));
                setOpaque(false);
                setBorder(new EmptyBorder(10, 10, 12, 10));
            }
            @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
            @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
            @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
            @Override public boolean getScrollableTracksViewportWidth() { return true; }
            @Override public boolean getScrollableTracksViewportHeight() { return false; }
        }
        JPanel container = new ContainerPanel();

        // 1. CỘT TRÁI: THANH DANH MỤC DỌC (125px)
        container.add(taoCategoryRail(), BorderLayout.WEST);

        // 2. CỘT GIỮA: LƯỚI CARD MÓN ĂN (FLEXIBLE CENTER)
        container.add(taoMenuCatalogArea(), BorderLayout.CENTER);

        // 3. CỘT PHẢI: PHIẾU ORDER BÀN ĐANG CHỌN (310px)
        container.add(taoOrderTicketPanel(), BorderLayout.EAST);

        return container;
    }

    /* =========================================================================
     * 1. CỘT TRÁI: THANH DANH MỤC ICON DỌC
     * ========================================================================= */
    private JPanel taoCategoryRail() {
        JPanel rail = new JPanel();
        rail.setOpaque(false);
        rail.setLayout(new BoxLayout(rail, BoxLayout.Y_AXIS));
        rail.setPreferredSize(new Dimension(125, 560));
        rail.setMinimumSize(new Dimension(125, 500));
        rail.setMaximumSize(new Dimension(125, 9999));

        String[][] cats = {
            {"🍲", "Món chính"},
            {"🥗", "Khai vị"},
            {"🥩", "Lẩu - Nướng"},
            {"🍷", "Đồ uống"},
            {"🍨", "Tráng miệng"},
            {"🍱", "Món mới"}
        };

        for (String[] c : cats) {
            JButton btnCat = taoCategoryButton(c[0], c[1]);
            rail.add(btnCat);
            rail.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        return rail;
    }

    private JButton taoCategoryButton(String icon, String title) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                boolean sel = title.equals(selectedCategory);

                if (sel) {
                    // Active màu vàng đồng / nâu cam ấm theo ảnh 3/12
                    g2.setColor(new Color(0xB0, 0x82, 0x46));
                    g2.fillRoundRect(0, 0, w, h, 10, 10);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, w, h, 10, 10);
                    g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
                }

                // Icon
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                g2.setColor(sel ? Color.WHITE : new Color(0x64, 0x74, 0x8B));
                g2.drawString(icon, 12, h / 2 + 6);

                // Text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.setColor(sel ? Color.WHITE : new Color(0x1F, 0x27, 0x33));
                g2.drawString(title, 38, h / 2 + 4);

                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(125, 46));
        b.setMaximumSize(new Dimension(125, 46));
        b.setMinimumSize(new Dimension(125, 46));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            selectedCategory = title;
            repaint();
            capNhatMenuTheoCategory();
        });
        return b;
    }

    /* =========================================================================
     * 2. CỘT GIỮA: LƯỚI THẺ MÓN ĂN KÈM TÌM KIẾM
     * ========================================================================= */
    private JPanel taoMenuCatalogArea() {
        JPanel area = new JPanel(new BorderLayout(0, 8));
        area.setOpaque(false);

        // Top Search Bar
        JPanel pnlSearch = new JPanel(new BorderLayout(6, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnlSearch.setOpaque(false);
        pnlSearch.setPreferredSize(new Dimension(300, 36));
        pnlSearch.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel lblS = new JLabel("🔍");
        lblS.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlSearch.add(lblS, BorderLayout.WEST);

        JTextField txtSearch = new JTextField();
        txtSearch.setText("Tìm món ăn...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSearch.setForeground(new Color(0x8C, 0x82, 0x7A));
        txtSearch.setOpaque(false);
        txtSearch.setBorder(null);
        pnlSearch.add(txtSearch, BorderLayout.CENTER);

        area.add(pnlSearch, BorderLayout.NORTH);

        // Lưới card món ăn
        pnlMenuGrid = new JPanel(new GridLayout(2, 3, 8, 8));
        pnlMenuGrid.setOpaque(false);

        napDanhSachMonAnVaoLuoi();
        area.add(pnlMenuGrid, BorderLayout.CENTER);

        return area;
    }

    private void napDanhSachMonAnVaoLuoi() {
        pnlMenuGrid.removeAll();

        String[][] dishes = {
            {"Bò Fuji Nướng Đá", "280.000 đ", "280000"},
            {"Lẩu Hải Sản TomYum", "320.000 đ", "320000"},
            {"Cơm Chiên Hải Sản", "150.000 đ", "150000"},
            {"Gà Sốt Chanh Dây", "180.000 đ", "180000"},
            {"Salad Cá Ngừ", "135.000 đ", "135000"},
            {"Tôm Sú Nướng Phô Mai", "250.000 đ", "250000"}
        };

        for (String[] d : dishes) {
            pnlMenuGrid.add(taoFoodCard(d[0], d[1], Double.parseDouble(d[2])));
        }
        pnlMenuGrid.revalidate();
        pnlMenuGrid.repaint();
    }

    private void capNhatMenuTheoCategory() {
        pnlMenuGrid.removeAll();
        if ("Đồ uống".equals(selectedCategory)) {
            pnlMenuGrid.add(taoFoodCard("Coca Cola", "25.000 đ", 25000));
            pnlMenuGrid.add(taoFoodCard("Rượu Vang Đỏ Chateau", "750.000 đ", 750000));
            pnlMenuGrid.add(taoFoodCard("Trà Đào Cam Sả", "45.000 đ", 45000));
            pnlMenuGrid.add(taoFoodCard("Nước Khoáng Lavie", "20.000 đ", 20000));
        } else {
            napDanhSachMonAnVaoLuoi();
        }
        pnlMenuGrid.revalidate();
        pnlMenuGrid.repaint();
    }

    private JPanel taoFoodCard(String name, String priceStr, double price) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(6, 6, 8, 6));

        // Ảnh món ăn
        JPanel pnlImg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage img = SavoreImageRenderer.getDishImage(name, getWidth(), getHeight());
                g.drawImage(img, 0, 0, null);
            }
        };
        pnlImg.setPreferredSize(new Dimension(140, 100));
        pnlImg.setOpaque(false);
        card.add(pnlImg, BorderLayout.NORTH);

        // Tên món + Giá
        JPanel pnlInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlInfo.setOpaque(false);

        JLabel lblN = new JLabel(name);
        lblN.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblN.setForeground(new Color(0x1F, 0x27, 0x33));

        JLabel lblP = new JLabel(priceStr);
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblP.setForeground(new Color(0xB0, 0x82, 0x46));

        pnlInfo.add(lblN);
        pnlInfo.add(lblP);
        card.add(pnlInfo, BorderLayout.CENTER);

        // Click món -> Thêm vào order ticket
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                themMonVaoDon(name, price);
            }
        });

        return card;
    }

    /* =========================================================================
     * 3. CỘT PHẢI: PHIẾU ORDER BÀN ĐANG CHỌN (ORDER TICKET)
     * ========================================================================= */
    private JPanel taoOrderTicketPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(310, 560));
        card.setMinimumSize(new Dimension(300, 500));
        card.setMaximumSize(new Dimension(310, 9999));
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Header: Đơn hàng - Bàn 02 - Tầng 1 + [Chọn bàn]
        JPanel pnlHead = new JPanel(new BorderLayout());
        pnlHead.setOpaque(false);

        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 0, 1));
        pnlTitle.setOpaque(false);
        JLabel lblTopT = new JLabel("Đơn hàng");
        lblTopT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTopT.setForeground(new Color(0x1F, 0x27, 0x33));

        lblTableTitle = new JLabel(selectedTableName);
        lblTableTitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblTableTitle.setForeground(new Color(0x64, 0x74, 0x8B));
        pnlTitle.add(lblTopT);
        pnlTitle.add(lblTableTitle);
        pnlHead.add(pnlTitle, BorderLayout.WEST);

        JButton btnChangeTable = new JButton("Chọn bàn");
        btnChangeTable.setFont(new Font("Segoe UI", Font.BOLD, 9));
        btnChangeTable.setBackground(new Color(0xF1, 0xF5, 0xF9));
        btnChangeTable.setForeground(new Color(0x1F, 0x27, 0x33));
        btnChangeTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangeTable.addActionListener(e -> moDialogChonBan());
        pnlHead.add(btnChangeTable, BorderLayout.EAST);

        card.add(pnlHead, BorderLayout.NORTH);

        // Body: Danh sách món trong đơn
        pnlOrderList = new JPanel();
        pnlOrderList.setOpaque(false);
        pnlOrderList.setLayout(new BoxLayout(pnlOrderList, BoxLayout.Y_AXIS));

        JScrollPane scrollItems = new JScrollPane(pnlOrderList);
        scrollItems.setOpaque(false);
        scrollItems.getViewport().setOpaque(false);
        scrollItems.setBorder(null);
        scrollItems.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollItems.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        card.add(scrollItems, BorderLayout.CENTER);

        // Footer: Tạm tính, Giảm giá, Thuế 8%, Tổng cộng + Nút hành động
        JPanel pnlFoot = new JPanel(new BorderLayout(0, 6));
        pnlFoot.setOpaque(false);
        pnlFoot.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0xEA, 0xE4, 0xDC)));

        JPanel pnlSummary = new JPanel(new GridLayout(4, 2, 0, 3));
        pnlSummary.setOpaque(false);
        pnlSummary.setBorder(new EmptyBorder(6, 0, 6, 0));

        pnlSummary.add(taoLabel("Tạm tính:", false, false));
        lblSubtotal = taoLabel("650.000 đ", false, true);
        pnlSummary.add(lblSubtotal);

        pnlSummary.add(taoLabel("Giảm giá:", false, false));
        lblDiscount = taoLabel("0 đ", false, true);
        pnlSummary.add(lblDiscount);

        pnlSummary.add(taoLabel("Thuế (8%):", false, false));
        lblTax = taoLabel("52.000 đ", false, true);
        pnlSummary.add(lblTax);

        pnlSummary.add(taoLabel("Tổng cộng:", true, false));
        lblTotal = taoLabel("702.000 đ", true, true);
        lblTotal.setForeground(new Color(0xEF, 0x44, 0x44));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlSummary.add(lblTotal);

        pnlFoot.add(pnlSummary, BorderLayout.NORTH);

        // Nút [Lưu tạm] và [Thanh toán (F9)]
        JPanel pnlBtns = new JPanel(new GridLayout(1, 2, 8, 0));
        pnlBtns.setOpaque(false);

        JButton btnSave = new JButton("Lưu tạm");
        btnSave.setBackground(new Color(0x1E, 0x29, 0x3B));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã lưu tạm đơn hàng cho " + selectedTableName));

        JButton btnCheckout = new JButton("Thanh toán (F9)");
        btnCheckout.setBackground(new Color(0xD4, 0xA3, 0x59));
        btnCheckout.setForeground(new Color(0x1F, 0x27, 0x33));
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.addActionListener(e -> moThanhToan());

        pnlBtns.add(btnSave);
        pnlBtns.add(btnCheckout);
        pnlFoot.add(pnlBtns, BorderLayout.SOUTH);

        card.add(pnlFoot, BorderLayout.SOUTH);

        veLaiDanhSachOrder();
        return card;
    }

    private JLabel taoLabel(String text, boolean bold, boolean right) {
        JLabel l = new JLabel(text, right ? SwingConstants.RIGHT : SwingConstants.LEFT);
        l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 10));
        l.setForeground(new Color(0x1F, 0x27, 0x33));
        return l;
    }

    private void themMonVaoDon(String name, double price) {
        boolean found = false;
        for (OrderItem item : orderItems) {
            if (item.name.equals(name)) {
                item.qty++;
                found = true;
                break;
            }
        }
        if (!found) {
            orderItems.add(new OrderItem(name, price, 1));
        }
        veLaiDanhSachOrder();
    }

    private void veLaiDanhSachOrder() {
        pnlOrderList.removeAll();
        double subtotal = 0;

        for (OrderItem item : orderItems) {
            double lineTotal = item.price * item.qty;
            subtotal += lineTotal;

            JPanel row = new JPanel(new BorderLayout(4, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(9999, 32));
            row.setBorder(new EmptyBorder(3, 0, 3, 0));

            // Tên & Giá
            JPanel pnlInfo = new JPanel(new GridLayout(2, 1, 0, 0));
            pnlInfo.setOpaque(false);
            JLabel lblN = new JLabel(item.name);
            lblN.setFont(new Font("Segoe UI", Font.BOLD, 9));
            JLabel lblP = new JLabel(item.qty + " x " + fmt.format(item.price));
            lblP.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            lblP.setForeground(new Color(0x64, 0x74, 0x8B));
            pnlInfo.add(lblN);
            pnlInfo.add(lblP);
            row.add(pnlInfo, BorderLayout.CENTER);

            // Stepper [-] [+] và Xóa
            JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
            pnlRight.setOpaque(false);

            JButton btnMinus = new JButton("-");
            btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 8));
            btnMinus.setPreferredSize(new Dimension(18, 18));
            btnMinus.addActionListener(e -> {
                if (item.qty > 1) {
                    item.qty--;
                } else {
                    orderItems.remove(item);
                }
                veLaiDanhSachOrder();
            });

            JButton btnPlus = new JButton("+");
            btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 8));
            btnPlus.setPreferredSize(new Dimension(18, 18));
            btnPlus.addActionListener(e -> {
                item.qty++;
                veLaiDanhSachOrder();
            });

            pnlRight.add(btnMinus);
            pnlRight.add(btnPlus);
            row.add(pnlRight, BorderLayout.EAST);

            pnlOrderList.add(row);
        }

        double tax = subtotal * 0.08;
        double total = subtotal + tax;

        if (lblSubtotal != null) lblSubtotal.setText(fmt.format(subtotal));
        if (lblTax != null) lblTax.setText(fmt.format(tax));
        if (lblTotal != null) lblTotal.setText(fmt.format(total));

        pnlOrderList.revalidate();
        pnlOrderList.repaint();
    }

    private void moDialogChonBan() {
        String[] bans = {"Bàn 01 - Tầng 1", "Bàn 02 - Tầng 1", "Bàn 03 - Tầng 1", "Bàn 04 - Tầng 1", "Bàn 05 - Tầng 1"};
        String chon = (String) JOptionPane.showInputDialog(this, "Chọn bàn phục vụ:", "Chọn Bàn",
                JOptionPane.PLAIN_MESSAGE, null, bans, selectedTableName);
        if (chon != null) {
            selectedTableName = chon;
            lblTableTitle.setText(chon);
        }
    }

    private void moThanhToan() {
        JOptionPane.showMessageDialog(this, "Xác nhận thanh toán cho " + selectedTableName + "\nTổng tiền: " + lblTotal.getText());
        orderItems.clear();
        veLaiDanhSachOrder();
    }

    public void napDuLieu() {
        veLaiDanhSachOrder();
    }

    // Scrollable
    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
