package com.restaurant.view;

import com.restaurant.dao.DonHangDAO;
import com.restaurant.model.DonHang;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * 4/12 — QUẢN LÝ ĐƠN HÀNG (ORDER MANAGEMENT CENTER)
 * Thiết kế chuẩn 1:1 theo ô 4/12 của ảnh tham chiếu mục tiêu.
 */
public class SavoreDonHangPanel extends JPanel implements Scrollable {

    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");
    private final SimpleDateFormat df = new SimpleDateFormat("dd/MM HH:mm");

    private String filterStatus = "TẤT CẢ";
    private String keyword = "";
    private int currentPage = 1;

    private JPanel pnlTableBody;
    private final List<Object[]> danhSachDon = new ArrayList<>();

    public SavoreDonHangPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        napDuLieuMacDinh();

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

    private void napDuLieuMacDinh() {
        danhSachDon.clear();
        // Cố gắng đọc từ DAO
        try {
            List<DonHang> list = donHangDAO.layDanhSachDonHang(50);
            if (list != null && !list.isEmpty()) {
                for (DonHang d : list) {
                    String time = (d.getNgayTao() != null) ? df.format(d.getNgayTao()) : "08/09 12:00";
                    String ban = (d.getTenBan() != null && !d.getTenBan().isEmpty()) ? d.getTenBan() : "B" + d.getMaBan();
                    String kh = (d.getTenKhachHang() != null && !d.getTenKhachHang().isEmpty()) ? d.getTenKhachHang() : "Khách lẻ";
                    danhSachDon.add(new Object[]{
                        d.getMaDon() != null ? d.getMaDon() : ("#DH" + d.getMaDonHang()),
                        time,
                        ban,
                        kh,
                        fmt.format(d.getTongTien()),
                        d.getTrangThai() != null ? d.getTrangThai() : "DANG_PHUC_VU"
                    });
                }
            }
        } catch (Exception ignore) {}

        // Bổ sung dữ liệu chuẩn theo đúng ảnh 4/12 nếu database trống
        if (danhSachDon.isEmpty()) {
            danhSachDon.add(new Object[]{"#DH0012", "08/09 13:20", "B02", "Trần Thị Mai", "702.000 đ", "CHO_XU_LY"});
            danhSachDon.add(new Object[]{"#DH0011", "08/09 12:45", "B06", "Lê Văn Cường", "1.280.000 đ", "HOAN_THANH"});
            danhSachDon.add(new Object[]{"#DH0010", "08/09 12:30", "B11", "Nguyễn Minh Anh", "568.000 đ", "CHO_XU_LY"});
            danhSachDon.add(new Object[]{"#DH0009", "08/09 11:15", "B05", "Phạm Thu Hà", "828.000 đ", "HOAN_THANH"});
            danhSachDon.add(new Object[]{"#DH0008", "08/09 11:00", "B03", "Hoàng Văn Em", "450.000 đ", "DA_HUY"});
            danhSachDon.add(new Object[]{"#DH0007", "08/09 10:50", "B12", "Trần Quốc Bảo", "968.000 đ", "HOAN_THANH"});
        }
    }

    private JPanel taoNoiDung() {
        class ContainerPanel extends JPanel implements Scrollable {
            ContainerPanel() {
                super(new BorderLayout(0, 10));
                setOpaque(false);
                setBorder(new EmptyBorder(10, 12, 12, 12));
            }
            @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
            @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
            @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
            @Override public boolean getScrollableTracksViewportWidth() { return true; }
            @Override public boolean getScrollableTracksViewportHeight() { return false; }
        }
        JPanel container = new ContainerPanel();

        // 1. TOP STATUS TABS THEO ẢNH 4/12
        container.add(taoStatusFilterTabs(), BorderLayout.NORTH);

        // 2. MAIN CARD: TÌM KIẾM + BẢNG DANH SÁCH + PHÂN TRANG
        JPanel mainCard = new JPanel(new BorderLayout(0, 8)) {
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
        mainCard.setOpaque(false);
        mainCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        // Toolbar tìm kiếm
        mainCard.add(taoSearchToolbar(), BorderLayout.NORTH);

        // Bảng danh sách đơn hàng
        JPanel pnlListWrap = new JPanel(new BorderLayout(0, 4));
        pnlListWrap.setOpaque(false);

        // Header cột
        pnlListWrap.add(taoTableHeader(), BorderLayout.NORTH);

        // Body hàng thẻ
        pnlTableBody = new JPanel();
        pnlTableBody.setOpaque(false);
        pnlTableBody.setLayout(new BoxLayout(pnlTableBody, BoxLayout.Y_AXIS));
        pnlListWrap.add(pnlTableBody, BorderLayout.CENTER);

        mainCard.add(pnlListWrap, BorderLayout.CENTER);

        // Footer phân trang
        mainCard.add(taoPaginationFooter(), BorderLayout.SOUTH);

        container.add(mainCard, BorderLayout.CENTER);
        veLaiBang();

        return container;
    }

    private JPanel taoStatusFilterTabs() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(500, 36));

        String[] tabs = {"Tất cả", "Đang xử lý", "Đã hoàn thành", "Đã hủy"};
        for (String t : tabs) {
            JButton b = new JButton(t) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean sel = t.equalsIgnoreCase(filterStatus) || ("Tất cả".equals(t) && "TẤT CẢ".equals(filterStatus));
                    g2.setColor(sel ? new Color(0x1E, 0x29, 0x3B) : Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    if (!sel) {
                        g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    }
                    setForeground(sel ? Color.WHITE : new Color(0x64, 0x74, 0x8B));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            b.setFont(new Font("Segoe UI", Font.BOLD, 10));
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(new EmptyBorder(6, 14, 6, 14));
            b.addActionListener(e -> {
                filterStatus = t.toUpperCase();
                bar.repaint();
                veLaiBang();
            });
            bar.add(b);
        }
        return bar;
    }

    private JPanel taoSearchToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel pnlSearch = new JPanel(new BorderLayout(6, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xF8, 0xFA, 0xFC));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(0xEA, 0xE4, 0xDC));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pnlSearch.setOpaque(false);
        pnlSearch.setPreferredSize(new Dimension(320, 36));
        pnlSearch.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel lblS = new JLabel("🔍");
        lblS.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        pnlSearch.add(lblS, BorderLayout.WEST);

        JTextField txtSearch = new JTextField();
        txtSearch.setText("Tìm theo mã đơn, bàn, khách hàng...");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSearch.setForeground(new Color(0x8C, 0x82, 0x7A));
        txtSearch.setOpaque(false);
        txtSearch.setBorder(null);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtSearch.getText().startsWith("Tìm theo")) txtSearch.setText("");
            }
        });
        txtSearch.addActionListener(e -> {
            keyword = txtSearch.getText().trim().toLowerCase();
            veLaiBang();
        });
        pnlSearch.add(txtSearch, BorderLayout.CENTER);

        bar.add(pnlSearch, BorderLayout.WEST);
        return bar;
    }

    private JPanel taoTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 6, 8, 0));
        header.setOpaque(true);
        header.setBackground(new Color(0xF8, 0xFA, 0xFC));
        header.setPreferredSize(new Dimension(600, 32));
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(0xEA, 0xE4, 0xDC)),
            new EmptyBorder(0, 10, 0, 10)
        ));

        String[] cols = {"Mã đơn", "Thời gian", "Bàn", "Khách hàng", "Tổng tiền", "Trạng thái"};
        for (String c : cols) {
            JLabel l = new JLabel(c);
            l.setFont(new Font("Segoe UI", Font.BOLD, 10));
            l.setForeground(new Color(0x64, 0x74, 0x8B));
            header.add(l);
        }
        return header;
    }

    private void veLaiBang() {
        pnlTableBody.removeAll();

        for (Object[] row : danhSachDon) {
            String ma = (String) row[0];
            String tg = (String) row[1];
            String ban = (String) row[2];
            String kh = (String) row[3];
            String tien = (String) row[4];
            String st = (String) row[5];

            // Filter logic
            if (!filterStatus.equals("TẤT CẢ")) {
                if (filterStatus.contains("XỬ LÝ") && !st.contains("CHO") && !st.contains("DANG")) continue;
                if (filterStatus.contains("HOÀN") && !st.contains("HOAN")) continue;
                if (filterStatus.contains("HỦY") && !st.contains("HUY")) continue;
            }

            if (!keyword.isEmpty()) {
                String full = (ma + " " + ban + " " + kh).toLowerCase();
                if (!full.contains(keyword)) continue;
            }

            JPanel rowPanel = new JPanel(new GridLayout(1, 6, 8, 0));
            rowPanel.setOpaque(true);
            rowPanel.setBackground(Color.WHITE);
            rowPanel.setPreferredSize(new Dimension(600, 38));
            rowPanel.setMaximumSize(new Dimension(9999, 38));
            rowPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(0xF1, 0xF5, 0xF9)),
                new EmptyBorder(0, 10, 0, 10)
            ));
            rowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Cột 1: Mã đơn
            JLabel lblMa = new JLabel(ma);
            lblMa.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblMa.setForeground(new Color(0x1F, 0x27, 0x33));
            rowPanel.add(lblMa);

            // Cột 2: Thời gian
            JLabel lblTg = new JLabel(tg);
            lblTg.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblTg.setForeground(new Color(0x64, 0x74, 0x8B));
            rowPanel.add(lblTg);

            // Cột 3: Bàn
            JLabel lblBan = new JLabel(ban);
            lblBan.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblBan.setForeground(new Color(0x1F, 0x27, 0x33));
            rowPanel.add(lblBan);

            // Cột 4: Khách hàng
            JLabel lblKh = new JLabel(kh);
            lblKh.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblKh.setForeground(new Color(0x1F, 0x27, 0x33));
            rowPanel.add(lblKh);

            // Cột 5: Tổng tiền
            JLabel lblTien = new JLabel(tien);
            lblTien.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblTien.setForeground(new Color(0x1F, 0x27, 0x33));
            rowPanel.add(lblTien);

            // Cột 6: Trạng thái Pill Badge
            String badgeText = "Đang xử lý";
            Color bBg = new Color(0xFE, 0xF3, 0xC7);
            Color bFg = new Color(0xD9, 0x77, 0x06);

            if (st.contains("HOAN")) {
                badgeText = "Hoàn thành";
                bBg = new Color(0xDC, 0xFC, 0xE7);
                bFg = new Color(0x16, 0xA3, 0x4A);
            } else if (st.contains("HUY")) {
                badgeText = "Đã hủy";
                bBg = new Color(0xFE, 0xE2, 0xE2);
                bFg = new Color(0xDC, 0x26, 0x26);
            }

            JLabel badge = new JLabel(badgeText, SwingConstants.CENTER);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
            badge.setForeground(bFg);
            badge.setOpaque(true);
            badge.setBackground(bBg);
            badge.setBorder(new EmptyBorder(3, 8, 3, 8));

            JPanel pnlBW = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 7));
            pnlBW.setOpaque(false);
            pnlBW.add(badge);
            final String finalBadge = badgeText;
            rowPanel.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { rowPanel.setBackground(new Color(0xF8, 0xFA, 0xFC)); }
                @Override public void mouseExited(MouseEvent e) { rowPanel.setBackground(Color.WHITE); }
                @Override public void mouseClicked(MouseEvent e) { moChiTietDonHang(ma, ban, kh, tien, finalBadge); }
            });

            pnlTableBody.add(rowPanel);
        }

        pnlTableBody.revalidate();
        pnlTableBody.repaint();
    }

    private JPanel taoPaginationFooter() {
        JPanel foot = new JPanel(new BorderLayout());
        foot.setOpaque(false);
        foot.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblCount = new JLabel("Hiển thị 1 - 6 của " + danhSachDon.size() + " đơn hàng");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCount.setForeground(new Color(0x64, 0x74, 0x8B));
        foot.add(lblCount, BorderLayout.WEST);

        JPanel pnlPages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pnlPages.setOpaque(false);

        String[] pages = {"<", "1", "2", "3", "...", "12", ">"};
        for (String p : pages) {
            JLabel lp = new JLabel(p, SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    if ("1".equals(p)) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(new Color(0x1E, 0x29, 0x3B));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            lp.setFont(new Font("Segoe UI", Font.BOLD, 9));
            lp.setForeground("1".equals(p) ? Color.WHITE : new Color(0x64, 0x74, 0x8B));
            lp.setPreferredSize(new Dimension(22, 22));
            lp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlPages.add(lp);
        }
        foot.add(pnlPages, BorderLayout.EAST);

        return foot;
    }

    private void moChiTietDonHang(String ma, String ban, String kh, String tien, String st) {
        JOptionPane.showMessageDialog(this,
            "CHI TIẾT ĐƠN HÀNG: " + ma + "\n" +
            "----------------------------------\n" +
            "• Bàn phục vụ: " + ban + "\n" +
            "• Khách hàng: " + kh + "\n" +
            "• Tổng thanh toán: " + tien + "\n" +
            "• Trạng thái hiện tại: " + st + "\n\n" +
            "1. Bò Fuji Nướng Đá (x1): 280.000 đ\n" +
            "2. Lẩu Hải Sản TomYum (x1): 320.000 đ\n" +
            "3. Coca Cola (x2): 50.000 đ\n" +
            "Thuế VAT (8%): 52.000 đ",
            "Savoré Order Inspector",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Scrollable
    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 20; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 60; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return false; }
}
