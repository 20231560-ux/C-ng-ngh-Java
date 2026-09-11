package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

public class PosDashboardPanel extends JPanel {

    private final NguoiDung nguoiDung;
    private final Consumer<String> chuyenPhanHeCallback;

    // UI Components
    private final JPanel pnlKpi = new JPanel(new GridLayout(1, 4, 16, 0));
    private final BieuDoDoanhThu bieuDo = new BieuDoDoanhThu();
    private final JPanel pnlTopMon = new JPanel();
    private final JPanel pnlDonHang = new JPanel();
    private final JPanel pnlCanhBaoKho = new JPanel();
    private JLabel lblTrangThaiDB;

    private boolean coDuLieuMySQL = false;

    public PosDashboardPanel(NguoiDung nguoiDung, Consumer<String> chuyenPhanHeCallback) {
        this.nguoiDung = nguoiDung;
        this.chuyenPhanHeCallback = chuyenPhanHeCallback;

        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(16, 20, 20, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP: Header hành động nhanh + 4 thẻ KPI
        JPanel pnlTop = new JPanel(new BorderLayout(0, 12));
        pnlTop.setOpaque(false);

        // Thanh thao tác nhanh
        JPanel pnlActionBar = new JPanel(new BorderLayout());
        pnlActionBar.setOpaque(false);

        JPanel pnlLoiChao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLoiChao.setOpaque(false);
        String tenNd = nguoiDung != null ? nguoiDung.getHoTen() : "Quản trị viên";
        JLabel lblXinChao = new JLabel("Xin chào, " + tenNd + "!");
        lblXinChao.setFont(GiaoPos.f(18, Font.BOLD));
        lblXinChao.setForeground(GiaoPos.CHU_CHINH);

        lblTrangThaiDB = new JLabel("● Đang tải CSDL...");
        lblTrangThaiDB.setFont(GiaoPos.f(11, Font.BOLD));
        lblTrangThaiDB.setForeground(GiaoPos.DONG_CHINH);

        pnlLoiChao.add(lblXinChao);
        pnlLoiChao.add(lblTrangThaiDB);
        pnlActionBar.add(pnlLoiChao, BorderLayout.WEST);

        // Nút tắt thao tác nhanh
        JPanel pnlNutNhanh = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlNutNhanh.setOpaque(false);

        GiaoPos.NutPos btnVaoPos = new GiaoPos.NutPos("🍽️ Vào Bán hàng POS", GiaoPos.NutPos.STYLE_DONG);
        btnVaoPos.setPreferredSize(new Dimension(170, 36));
        btnVaoPos.setFont(GiaoPos.f(12, Font.BOLD));
        btnVaoPos.addActionListener(e -> {
            if (chuyenPhanHeCallback != null) chuyenPhanHeCallback.accept("POS");
        });

        GiaoPos.NutPos btnSoDoBan = new GiaoPos.NutPos("🪑 Sơ đồ bàn", GiaoPos.NutPos.STYLE_TRANG);
        btnSoDoBan.setPreferredSize(new Dimension(130, 36));
        btnSoDoBan.setFont(GiaoPos.f(12, Font.BOLD));
        btnSoDoBan.addActionListener(e -> {
            if (chuyenPhanHeCallback != null) chuyenPhanHeCallback.accept("BAN");
        });

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄 Làm mới", GiaoPos.NutPos.STYLE_TRANG);
        btnLamMoi.setPreferredSize(new Dimension(110, 36));
        btnLamMoi.setFont(GiaoPos.f(12, Font.BOLD));
        btnLamMoi.addActionListener(e -> napDuLieu());

        pnlNutNhanh.add(btnVaoPos);
        pnlNutNhanh.add(btnSoDoBan);
        pnlNutNhanh.add(btnLamMoi);
        pnlActionBar.add(pnlNutNhanh, BorderLayout.EAST);

        pnlTop.add(pnlActionBar, BorderLayout.NORTH);

        // 4 Thẻ KPI
        pnlKpi.setOpaque(false);
        pnlKpi.setPreferredSize(new Dimension(100, 105));
        pnlTop.add(pnlKpi, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Vùng phân tích (Biểu đồ 14 ngày + Top món) & Vận hành (Đơn mới + Kho)
        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 0, 16));
        pnlCenter.setOpaque(false);

        // Hàng 2: Biểu đồ (Trái) + Top Món (Phải)
        JPanel pnlHang2 = new JPanel(new BorderLayout(16, 0));
        pnlHang2.setOpaque(false);

        GiaoPos.ThePos theBieuDo = new GiaoPos.ThePos(new BorderLayout(0, 10), 14);
        theBieuDo.add(taoTieuDeThe("DOANH THU 14 NGÀY GẦN NHẤT", "Biến động tổng tiền các đơn hoàn thành"), BorderLayout.NORTH);
        theBieuDo.add(bieuDo, BorderLayout.CENTER);
        pnlHang2.add(theBieuDo, BorderLayout.CENTER);

        GiaoPos.ThePos theTopMon = new GiaoPos.ThePos(new BorderLayout(0, 10), 14);
        theTopMon.setPreferredSize(new Dimension(380, 100));
        theTopMon.add(taoTieuDeThe("TOP MÓN BÁN CHẠY", "Xếp theo số lượng phục vụ"), BorderLayout.NORTH);
        pnlTopMon.setOpaque(false);
        pnlTopMon.setLayout(new BoxLayout(pnlTopMon, BoxLayout.Y_AXIS));
        theTopMon.add(taoVungCuon(pnlTopMon), BorderLayout.CENTER);
        pnlHang2.add(theTopMon, BorderLayout.EAST);

        pnlCenter.add(pnlHang2);

        // Hàng 3: Đơn hàng mới nhất (Trái) + Cảnh báo kho (Phải)
        JPanel pnlHang3 = new JPanel(new BorderLayout(16, 0));
        pnlHang3.setOpaque(false);

        GiaoPos.ThePos theDonHang = new GiaoPos.ThePos(new BorderLayout(0, 10), 14);
        theDonHang.add(taoTieuDeThe("ĐƠN HÀNG GẦN ĐÂY", "Các giao dịch mới nhất trong hệ thống"), BorderLayout.NORTH);
        pnlDonHang.setOpaque(false);
        pnlDonHang.setLayout(new BoxLayout(pnlDonHang, BoxLayout.Y_AXIS));
        theDonHang.add(taoVungCuon(pnlDonHang), BorderLayout.CENTER);
        pnlHang3.add(theDonHang, BorderLayout.CENTER);

        GiaoPos.ThePos theKho = new GiaoPos.ThePos(new BorderLayout(0, 10), 14);
        theKho.setPreferredSize(new Dimension(380, 100));
        theKho.add(taoTieuDeThe("CẢNH BÁO TỒN KHO", "Nguyên liệu dưới định mức tối thiểu"), BorderLayout.NORTH);
        pnlCanhBaoKho.setOpaque(false);
        pnlCanhBaoKho.setLayout(new BoxLayout(pnlCanhBaoKho, BoxLayout.Y_AXIS));
        theKho.add(taoVungCuon(pnlCanhBaoKho), BorderLayout.CENTER);
        pnlHang3.add(theKho, BorderLayout.EAST);

        pnlCenter.add(pnlHang3);

        add(pnlCenter, BorderLayout.CENTER);
    }

    private JPanel taoTieuDeThe(String tieuDe, String moTa) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));

        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(GiaoPos.f(13, Font.BOLD));
        lblTieuDe.setForeground(GiaoPos.DONG_DAM);

        JLabel lblMoTa = new JLabel(moTa);
        lblMoTa.setFont(GiaoPos.f(11, Font.PLAIN));
        lblMoTa.setForeground(GiaoPos.CHU_PHU);

        t.add(lblTieuDe);
        t.add(Box.createRigidArea(new Dimension(0, 2)));
        t.add(lblMoTa);

        p.add(t, BorderLayout.WEST);
        return p;
    }

    private JScrollPane taoVungCuon(JPanel pnl) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(pnl, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    /* ==================== TRUY VẤN CƠ SỞ DỮ LIỆU MYSQL ==================== */
    public void napDuLieu() {
        double dtHomNay = 0, dtHomQua = 0;
        int donHomNay = 0, banDangDung = 0, tongBan = 0, soKhach = 0;

        List<String> ngayList = new ArrayList<>();
        List<Double> tienList = new ArrayList<>();
        List<Object[]> dsMon = new ArrayList<>();
        List<Object[]> dsDon = new ArrayList<>();
        List<Object[]> dsKho = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {

            // 1. Doanh thu & số đơn hôm nay
            try (ResultSet rs = st.executeQuery(
                    "SELECT IFNULL(SUM(tong_tien), 0), COUNT(*) FROM don_hang "
                            + "WHERE trang_thai = 'HOAN_THANH' AND DATE(ngay_tao) = CURDATE()")) {
                if (rs.next()) {
                    dtHomNay = rs.getDouble(1);
                    donHomNay = rs.getInt(2);
                }
            }

            // 2. Doanh thu hôm qua
            try (ResultSet rs = st.executeQuery(
                    "SELECT IFNULL(SUM(tong_tien), 0) FROM don_hang "
                            + "WHERE trang_thai = 'HOAN_THANH' AND DATE(ngay_tao) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)")) {
                if (rs.next()) {
                    dtHomQua = rs.getDouble(1);
                }
            }

            // 3. Số bàn đang dùng & tổng số bàn
            try (ResultSet rs = st.executeQuery(
                    "SELECT SUM(trang_thai <> 'TRONG'), COUNT(*) FROM ban_an")) {
                if (rs.next()) {
                    banDangDung = rs.getInt(1);
                    tongBan = rs.getInt(2);
                }
            }

            // 4. Số khách hàng
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM khach_hang")) {
                if (rs.next()) soKhach = rs.getInt(1);
            }

            // 5. Doanh thu 14 ngày gần nhất
            try (ResultSet rs = st.executeQuery(
                    "SELECT DATE(ngay_tao) as d, SUM(tong_tien) as t FROM don_hang "
                            + "WHERE trang_thai = 'HOAN_THANH' AND ngay_tao >= DATE_SUB(CURDATE(), INTERVAL 13 DAY) "
                            + "GROUP BY d ORDER BY d")) {
                while (rs.next()) {
                    String d = rs.getString("d");
                    ngayList.add(d.substring(8, 10) + "/" + d.substring(5, 7));
                    tienList.add(rs.getDouble("t"));
                }
            }

            // 6. Top 5 món bán chạy nhất
            try (ResultSet rs = st.executeQuery(
                    "SELECT m.ten_mon, SUM(c.so_luong) as sl, SUM(c.so_luong * c.don_gia) as t "
                            + "FROM chi_tiet_don_hang c "
                            + "JOIN mon_an m ON m.ma_mon = c.ma_mon "
                            + "JOIN don_hang d ON d.ma_don_hang = c.ma_don_hang "
                            + "WHERE d.trang_thai = 'HOAN_THANH' "
                            + "GROUP BY c.ma_mon ORDER BY sl DESC LIMIT 5")) {
                while (rs.next()) {
                    dsMon.add(new Object[]{rs.getString(1), rs.getDouble(2), rs.getDouble(3)});
                }
            }

            // 7. Đơn hàng gần đây nhất
            try (ResultSet rs = st.executeQuery(
                    "SELECT d.ma_don, b.ten_ban, IFNULL(k.ho_ten, 'Khách lẻ'), d.tong_tien, d.trang_thai, "
                            + "TIME_FORMAT(d.ngay_tao, '%H:%i') FROM don_hang d "
                            + "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban "
                            + "LEFT JOIN khach_hang k ON k.ma_khach_hang = d.ma_khach_hang "
                            + "ORDER BY d.ma_don_hang DESC LIMIT 7")) {
                while (rs.next()) {
                    dsDon.add(new Object[]{
                            rs.getString(1), rs.getString(2), rs.getString(3),
                            rs.getDouble(4), rs.getString(5), rs.getString(6)
                    });
                }
            }

            // 8. Cảnh báo kho nguyên liệu
            try (ResultSet rs = st.executeQuery(
                    "SELECT ten_nguyen_lieu, so_luong, so_luong_toi_thieu, don_vi_tinh FROM nguyen_lieu "
                            + "WHERE so_luong <= so_luong_toi_thieu ORDER BY so_luong/GREATEST(so_luong_toi_thieu, 1) LIMIT 6")) {
                while (rs.next()) {
                    dsKho.add(new Object[]{
                            rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4)
                    });
                }
            }

            coDuLieuMySQL = true;
            lblTrangThaiDB.setText("● CSDL MySQL: Trực tuyến");
            lblTrangThaiDB.setForeground(GiaoPos.BAN_TRONG);

        } catch (Exception ex) {
            coDuLieuMySQL = false;
            lblTrangThaiDB.setText("● Chưa kết nối CSDL (Đang dùng dữ liệu mẫu)");
            lblTrangThaiDB.setForeground(GiaoPos.DONG_CHINH);

            // Dữ liệu mẫu an toàn nếu CSDL ngoại tuyến
            dtHomNay = 8500000;
            dtHomQua = 7800000;
            donHomNay = 14;
            tongBan = 16;
            banDangDung = 5;
            soKhach = 12;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -6);
            double[] arr = {6.2, 7.5, 5.8, 8.4, 9.1, 7.8, 8.5};
            for (double v : arr) {
                ngayList.add(new SimpleDateFormat("dd/MM").format(cal.getTime()));
                tienList.add(v * 1000000);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            dsMon.add(new Object[]{"Bò Wagyu Nướng Sốt Tiêu", 38d, 12500000d});
            dsMon.add(new Object[]{"Cua Hoàng Đế Sốt Bơ Tỏi", 24d, 18200000d});
            dsMon.add(new Object[]{"Phở Bò Thố Đá Đặc Biệt", 45d, 4050000d});
            dsKho.add(new Object[]{"Phô mai Mozzarella", 0d, 3d, "kg"});
            dsKho.add(new Object[]{"Thịt thăn bò Úc", 2.5, 8d, "kg"});
        }

        // Cập nhật 4 Card KPI
        pnlKpi.removeAll();

        double chenhLech = dtHomQua <= 0 ? 0 : ((dtHomNay - dtHomQua) / dtHomQua * 100);
        String sChenh = (chenhLech >= 0 ? "▲ +" : "▼ ") + String.format("%.1f", Math.abs(chenhLech)) + "% so với hôm qua";
        Color clrChenh = chenhLech >= 0 ? GiaoPos.BAN_TRONG : GiaoPos.BAN_CAN_THANH_TOAN;

        pnlKpi.add(new TheKpi("DOANH THU HÔM NAY", GiaoPos.formatTien(dtHomNay), sChenh, clrChenh, "💰", GiaoPos.DONG_CHINH));

        double tbDon = donHomNay == 0 ? 0 : (dtHomNay / donHomNay);
        pnlKpi.add(new TheKpi("ĐƠN HOÀN THÀNH", donHomNay + " đơn", "TB: " + GiaoPos.formatTien(tbDon) + " / đơn", GiaoPos.CHU_PHU, "🧾", new Color(0x2563EB)));

        int ptBan = tongBan == 0 ? 0 : (int) Math.round(banDangDung * 100.0 / tongBan);
        pnlKpi.add(new TheKpi("BÀN ĐANG PHỤC VỤ", banDangDung + " / " + tongBan + " bàn", ptBan + "% công suất phục vụ", GiaoPos.BAN_PHUC_VU, "🪑", GiaoPos.BAN_PHUC_VU));

        pnlKpi.add(new TheKpi("KHÁCH THÀNH VIÊN", soKhach + " khách", "Đang tích lũy điểm thưởng", GiaoPos.BAN_TRONG, "👥", GiaoPos.BAN_TRONG));

        pnlKpi.revalidate();
        pnlKpi.repaint();

        // Cập nhật biểu đồ
        bieuDo.capNhatDuLieu(ngayList, tienList);

        // Cập nhật Top món
        pnlTopMon.removeAll();
        double maxSl = 1;
        for (Object[] m : dsMon) maxSl = Math.max(maxSl, (Double) m[1]);
        int rank = 0;
        for (Object[] m : dsMon) {
            pnlTopMon.add(new DongTopMon(++rank, (String) m[0], (Double) m[1], (Double) m[2], maxSl));
            pnlTopMon.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        pnlTopMon.revalidate();
        pnlTopMon.repaint();

        // Cập nhật đơn hàng mới
        pnlDonHang.removeAll();
        for (Object[] d : dsDon) {
            pnlDonHang.add(new DongDonHang(d));
            pnlDonHang.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        pnlDonHang.revalidate();
        pnlDonHang.repaint();

        // Cập nhật cảnh báo kho
        pnlCanhBaoKho.removeAll();
        if (dsKho.isEmpty()) {
            JLabel lblOk = new JLabel("✓ Mọi nguyên liệu đều đạt định mức an toàn");
            lblOk.setFont(GiaoPos.f(12, Font.PLAIN));
            lblOk.setForeground(GiaoPos.BAN_TRONG);
            lblOk.setBorder(new EmptyBorder(12, 8, 8, 8));
            pnlCanhBaoKho.add(lblOk);
        } else {
            for (Object[] k : dsKho) {
                pnlCanhBaoKho.add(new DongCanhBaoKho(k));
                pnlCanhBaoKho.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }
        pnlCanhBaoKho.revalidate();
        pnlCanhBaoKho.repaint();
    }

    /* ==================== CÁC COMPONENT TÙY BIẾN ==================== */

    // 1. Thẻ KPI
    private static class TheKpi extends GiaoPos.ThePos {
        private final String tieuDe, giaTri, phuDe, icon;
        private final Color mauPhuDe, mauChuDao;

        TheKpi(String tieuDe, String giaTri, String phuDe, Color mauPhuDe, String icon, Color mauChuDao) {
            super(new BorderLayout(), 14);
            this.tieuDe = tieuDe;
            this.giaTri = giaTri;
            this.phuDe = phuDe;
            this.mauPhuDe = mauPhuDe;
            this.icon = icon;
            this.mauChuDao = mauChuDao;
            setBorder(new EmptyBorder(12, 16, 12, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            // Icon nền tròn
            g2.setColor(new Color(mauChuDao.getRed(), mauChuDao.getGreen(), mauChuDao.getBlue(), 28));
            g2.fillRoundRect(w - 52, 14, 38, 38, 12, 12);

            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            g2.setColor(mauChuDao);
            FontMetrics fmI = g2.getFontMetrics();
            g2.drawString(icon, w - 52 + (38 - fmI.stringWidth(icon)) / 2, 14 + (38 + fmI.getAscent() - 8) / 2);

            // Tiêu đề KPI
            g2.setFont(GiaoPos.f(10, Font.BOLD));
            g2.setColor(GiaoPos.CHU_PHU);
            g2.drawString(tieuDe, 16, 26);

            // Giá trị chính
            g2.setFont(GiaoPos.f(giaTri.length() > 14 ? 18 : 22, Font.BOLD));
            g2.setColor(GiaoPos.CHU_CHINH);
            g2.drawString(giaTri, 16, 56);

            // Phụ đề (tăng trưởng / chú thích)
            g2.setFont(GiaoPos.f(11, Font.PLAIN));
            g2.setColor(mauPhuDe);
            g2.drawString(phuDe, 16, h - 14);

            g2.dispose();
        }
    }

    // 2. Biểu đồ diện tích Gradient màu Đồng
    private static class BieuDoDoanhThu extends JPanel {
        private List<String> ngayList = new ArrayList<>();
        private List<Double> tienList = new ArrayList<>();
        private float animProgress = 0f;
        private int hoverIndex = -1;
        private Timer animTimer;

        BieuDoDoanhThu() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (tienList.size() < 2) return;
                    int padL = 65, w = getWidth() - padL - 20;
                    int idx = Math.round((e.getX() - padL) / (float) w * (tienList.size() - 1));
                    hoverIndex = (idx >= 0 && idx < tienList.size()) ? idx : -1;
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hoverIndex = -1;
                    repaint();
                }
            });
        }

        void capNhatDuLieu(List<String> ngay, List<Double> tien) {
            this.ngayList = new ArrayList<>(ngay);
            this.tienList = new ArrayList<>(tien);
            this.animProgress = 0f;
            if (animTimer != null) animTimer.stop();
            animTimer = new Timer(15, e -> {
                animProgress += 0.05f;
                if (animProgress >= 1f) {
                    animProgress = 1f;
                    animTimer.stop();
                }
                repaint();
            });
            animTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            int padL = 65, padB = 26, padT = 14;
            int n = tienList.size();

            if (n < 2) {
                g2.setColor(GiaoPos.CHU_PHU);
                g2.setFont(GiaoPos.f(12, Font.PLAIN));
                g2.drawString("Chưa có đủ dữ liệu doanh thu gần đây...", W / 3, H / 2);
                g2.dispose();
                return;
            }

            double maxTien = 1;
            for (double t : tienList) maxTien = Math.max(maxTien, t);
            double trCap = Math.ceil(maxTien / 2000000.0) * 2000000;
            if (trCap <= 0) trCap = 5000000;

            // Vẽ lưới ngang
            g2.setFont(GiaoPos.f(10, Font.PLAIN));
            for (int i = 0; i <= 4; i++) {
                int y = padT + (int) ((H - padT - padB) * (1 - i / 4.0));
                g2.setColor(new Color(0xE8DFD5));
                g2.drawLine(padL, y, W - 14, y);

                g2.setColor(GiaoPos.CHU_PHU);
                String nhanTien = (long) (trCap * i / 4 / 1000000) + " tr";
                g2.drawString(nhanTien, 14, y + 4);
            }

            // Tọa độ các điểm
            int[] xs = new int[n];
            int[] ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = padL + (W - padL - 20) * i / (n - 1);
                ys[i] = (int) (padT + (H - padT - padB) * (1 - (tienList.get(i) * animProgress) / trCap));
            }

            // Gradient Vùng (Màu Đồng)
            Path2D pathVung = new Path2D.Float();
            pathVung.moveTo(xs[0], H - padB);
            for (int i = 0; i < n; i++) pathVung.lineTo(xs[i], ys[i]);
            pathVung.lineTo(xs[n - 1], H - padB);
            pathVung.closePath();

            g2.setPaint(new GradientPaint(
                    0, padT, new Color(176, 130, 70, 75),
                    0, H - padB, new Color(176, 130, 70, 0)
            ));
            g2.fill(pathVung);

            // Đường nối màu Đồng sang trọng
            g2.setColor(GiaoPos.DONG_CHINH);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < n - 1; i++) {
                g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
            }

            // Điểm nút tròn & nhãn ngày
            for (int i = 0; i < n; i++) {
                boolean hot = (i == hoverIndex);
                int r = hot ? 6 : 4;
                g2.setColor(Color.WHITE);
                g2.fillOval(xs[i] - r, ys[i] - r, r * 2, r * 2);
                g2.setColor(GiaoPos.DONG_DAM);
                g2.setStroke(new BasicStroke(hot ? 2.5f : 1.8f));
                g2.drawOval(xs[i] - r, ys[i] - r, r * 2, r * 2);

                // Nhãn ngày
                if (n <= 14 || i % 2 == 0) {
                    g2.setFont(GiaoPos.f(10, Font.PLAIN));
                    g2.setColor(GiaoPos.CHU_PHU);
                    String lblD = ngayList.get(i);
                    int tw = g2.getFontMetrics().stringWidth(lblD);
                    g2.drawString(lblD, xs[i] - tw / 2, H - 8);
                }
            }

            // Tooltip khi hover
            if (hoverIndex >= 0 && hoverIndex < n) {
                String tip = GiaoPos.formatTien(tienList.get(hoverIndex));
                g2.setFont(GiaoPos.f(12, Font.BOLD));
                int tw = g2.getFontMetrics().stringWidth(tip) + 18;
                int bx = Math.min(W - tw - 8, Math.max(8, xs[hoverIndex] - tw / 2));
                int by = Math.max(4, ys[hoverIndex] - 34);

                g2.setColor(GiaoPos.DONG_DAM);
                g2.fillRoundRect(bx, by, tw, 26, 8, 8);
                g2.setColor(Color.WHITE);
                g2.drawString(tip, bx + 9, by + 18);
            }

            g2.dispose();
        }
    }

    // 3. Dòng Top Món ăn
    private static class DongTopMon extends JPanel {
        DongTopMon(int rank, String tenMon, double soLuong, double tongTien, double maxSl) {
            setOpaque(false);
            setLayout(new BorderLayout(8, 0));
            setPreferredSize(new Dimension(300, 36));
            setMaximumSize(new Dimension(9999, 36));

            // Huy hiệu thứ hạng
            Color clrBadge = rank == 1 ? GiaoPos.DONG_CHINH : rank == 2 ? GiaoPos.DONG_SANG : rank == 3 ? GiaoPos.BAN_TRONG : GiaoPos.CHU_PHU;
            JLabel lblRank = new JLabel(String.valueOf(rank), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(clrBadge.getRed(), clrBadge.getGreen(), clrBadge.getBlue(), 28));
                    g2.fillRoundRect(0, 4, 24, 24, 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblRank.setPreferredSize(new Dimension(26, 32));
            lblRank.setFont(GiaoPos.f(11, Font.BOLD));
            lblRank.setForeground(clrBadge);
            add(lblRank, BorderLayout.WEST);

            // Tên món + Thanh phần trăm
            JPanel pnlGiua = new JPanel(new GridLayout(2, 1, 0, 2));
            pnlGiua.setOpaque(false);

            JLabel lblTen = new JLabel(tenMon);
            lblTen.setFont(GiaoPos.f(12, Font.BOLD));
            lblTen.setForeground(GiaoPos.CHU_CHINH);
            pnlGiua.add(lblTen);

            int pt = (int) Math.round((soLuong / maxSl) * 100);
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(pt);
            bar.setPreferredSize(new Dimension(100, 5));
            bar.setForeground(clrBadge);
            bar.setBackground(new Color(0xEAE4DC));
            bar.setBorderPainted(false);
            pnlGiua.add(bar);

            add(pnlGiua, BorderLayout.CENTER);

            // Số lượng & tiền
            JPanel pnlPhai = new JPanel(new GridLayout(2, 1, 0, 2));
            pnlPhai.setOpaque(false);

            JLabel lblSl = new JLabel((int) soLuong + " phần", SwingConstants.RIGHT);
            lblSl.setFont(GiaoPos.f(11, Font.BOLD));
            lblSl.setForeground(GiaoPos.DONG_DAM);

            JLabel lblTien = new JLabel(GiaoPos.formatTien(tongTien), SwingConstants.RIGHT);
            lblTien.setFont(GiaoPos.f(10, Font.PLAIN));
            lblTien.setForeground(GiaoPos.CHU_PHU);

            pnlPhai.add(lblSl);
            pnlPhai.add(lblTien);
            add(pnlPhai, BorderLayout.EAST);
        }
    }

    // 4. Dòng Đơn hàng gần đây
    private static class DongDonHang extends JPanel {
        DongDonHang(Object[] don) {
            setOpaque(false);
            setLayout(new BorderLayout(10, 0));
            setPreferredSize(new Dimension(300, 32));
            setMaximumSize(new Dimension(9999, 32));

            String maDon = (String) don[0];
            String tenBan = (String) don[1];
            String tenKhach = (String) don[2];
            double tien = (Double) don[3];
            String trangThai = (String) don[4];
            String gio = (String) don[5];

            // Cột trái: Mã đơn + Tên bàn
            JPanel pnlTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            pnlTrai.setOpaque(false);

            JLabel lblMa = new JLabel(maDon);
            lblMa.setFont(GiaoPos.f(12, Font.BOLD));
            lblMa.setForeground(GiaoPos.DONG_DAM);

            JLabel lblBan = new JLabel(tenBan != null ? tenBan : "Mang về");
            lblBan.setFont(GiaoPos.f(12, Font.PLAIN));
            lblBan.setForeground(GiaoPos.CHU_CHINH);

            JLabel lblKhach = new JLabel("(" + tenKhach + ")");
            lblKhach.setFont(GiaoPos.f(11, Font.ITALIC));
            lblKhach.setForeground(GiaoPos.CHU_PHU);

            pnlTrai.add(lblMa);
            pnlTrai.add(lblBan);
            pnlTrai.add(lblKhach);
            add(pnlTrai, BorderLayout.WEST);

            // Cột phải: Giờ + Tiền + Badge trạng thái
            JPanel pnlPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
            pnlPhai.setOpaque(false);

            JLabel lblGio = new JLabel(gio);
            lblGio.setFont(GiaoPos.f(11, Font.PLAIN));
            lblGio.setForeground(GiaoPos.CHU_PHU);

            JLabel lblTien = new JLabel(GiaoPos.formatTien(tien));
            lblTien.setFont(GiaoPos.f(12, Font.BOLD));
            lblTien.setForeground(GiaoPos.CHU_CHINH);

            JLabel lblStatus = taoHuyHieu(trangThai);

            pnlPhai.add(lblGio);
            pnlPhai.add(lblTien);
            pnlPhai.add(lblStatus);
            add(pnlPhai, BorderLayout.EAST);
        }

        private JLabel taoHuyHieu(String status) {
            String text = status;
            Color c = GiaoPos.DONG_CHINH;
            if ("HOAN_THANH".equalsIgnoreCase(status)) {
                text = "Hoàn thành";
                c = GiaoPos.BAN_TRONG;
            } else if ("DANG_PHUC_VU".equalsIgnoreCase(status)) {
                text = "Đang phục vụ";
                c = GiaoPos.BAN_PHUC_VU;
            } else if ("CHO_XU_LY".equalsIgnoreCase(status)) {
                text = "Chờ xử lý";
                c = new Color(0x2563EB);
            } else if ("DA_HUY".equalsIgnoreCase(status)) {
                text = "Đã hủy";
                c = GiaoPos.BAN_CAN_THANH_TOAN;
            }

            final Color mauHuyHieu = c;
            final String nhan = text;

            JLabel lbl = new JLabel(nhan, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(mauHuyHieu.getRed(), mauHuyHieu.getGreen(), mauHuyHieu.getBlue(), 24));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lbl.setFont(GiaoPos.f(10, Font.BOLD));
            lbl.setForeground(mauHuyHieu);
            lbl.setPreferredSize(new Dimension(90, 22));
            return lbl;
        }
    }

    // 5. Dòng Cảnh báo Kho
    private static class DongCanhBaoKho extends JPanel {
        DongCanhBaoKho(Object[] k) {
            setOpaque(false);
            setLayout(new BorderLayout(8, 0));
            setPreferredSize(new Dimension(300, 32));
            setMaximumSize(new Dimension(9999, 32));

            String tenNl = (String) k[0];
            double soLuong = (Double) k[1];
            double dinhMuc = (Double) k[2];
            String dvt = (String) k[3];

            boolean daHet = (soLuong <= 0);

            JLabel lblTen = new JLabel(tenNl);
            lblTen.setFont(GiaoPos.f(12, Font.BOLD));
            lblTen.setForeground(daHet ? GiaoPos.BAN_CAN_THANH_TOAN : GiaoPos.CHU_CHINH);
            add(lblTen, BorderLayout.WEST);

            JPanel pnlPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
            pnlPhai.setOpaque(false);

            String strTon = (soLuong == (long) soLuong ? String.valueOf((long) soLuong) : String.format("%.1f", soLuong))
                    + " / " + (dinhMuc == (long) dinhMuc ? String.valueOf((long) dinhMuc) : String.format("%.1f", dinhMuc))
                    + " " + dvt;

            JLabel lblTon = new JLabel(strTon);
            lblTon.setFont(GiaoPos.f(11, Font.PLAIN));
            lblTon.setForeground(GiaoPos.CHU_PHU);

            JLabel lblTrangThai = new JLabel(daHet ? "Hết hàng" : "Sắp hết", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bg = daHet ? GiaoPos.BAN_CAN_THANH_TOAN : GiaoPos.BAN_PHUC_VU;
                    g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 26));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblTrangThai.setFont(GiaoPos.f(10, Font.BOLD));
            lblTrangThai.setForeground(daHet ? GiaoPos.BAN_CAN_THANH_TOAN : GiaoPos.BAN_PHUC_VU);
            lblTrangThai.setPreferredSize(new Dimension(65, 20));

            pnlPhai.add(lblTon);
            pnlPhai.add(lblTrangThai);
            add(pnlPhai, BorderLayout.EAST);
        }
    }
}
