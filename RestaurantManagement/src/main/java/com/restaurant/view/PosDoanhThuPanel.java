package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.geom.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PosDoanhThuPanel extends JPanel {

    private final NguoiDung nhanVien;
    private int soNgay = 30;

    private final List<String> bieuDoNgay = new ArrayList<>();
    private final List<Double> bieuDoDoanhThu = new ArrayList<>();
    private final List<Object[]> topMon = new ArrayList<>();

    private JLabel lblTongDoanhThu;
    private JLabel lblSoDonHang;
    private JLabel lblGiaTriTB;
    private JLabel lblDoanhThuHomNay;
    private BieuDoCotPos bieuDo;
    private JPanel pnlTopMon;
    private JPanel pnlTimeChips;

    public PosDoanhThuPanel(NguoiDung nhanVien) {
        this.nhanVien = nhanVien;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(16, 20, 18, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP: 4 Thẻ KPI Doanh thu + Lọc thời gian
        JPanel pnlTop = new JPanel(new BorderLayout(0, 12));
        pnlTop.setOpaque(false);

        // 4 KPI Cards
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 14, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(100, 86));

        lblTongDoanhThu = new JLabel("0 đ", SwingConstants.CENTER);
        lblSoDonHang = new JLabel("0", SwingConstants.CENTER);
        lblGiaTriTB = new JLabel("0 đ", SwingConstants.CENTER);
        lblDoanhThuHomNay = new JLabel("0 đ", SwingConstants.CENTER);

        pnlCards.add(taoTheKPI("TỔNG DOANH THU", lblTongDoanhThu, GiaoPos.DONG_CHINH, GiaoPos.DONG_NHAT));
        pnlCards.add(taoTheKPI("TỔNG ĐƠN HOÀN THÀNH", lblSoDonHang, GiaoPos.BAN_TRONG, GiaoPos.BAN_TRONG_NEN));
        pnlCards.add(taoTheKPI("GIÁ TRỊ ĐƠN TB", lblGiaTriTB, GiaoPos.BAN_DAT_TRUOC, GiaoPos.BAN_DAT_NEN));
        pnlCards.add(taoTheKPI("DOANH THU HÔM NAY", lblDoanhThuHomNay, GiaoPos.DONG_DAM, GiaoPos.DONG_NHAT));
        pnlTop.add(pnlCards, BorderLayout.NORTH);

        // Time filter bar
        JPanel pnlFilterBar = new JPanel(new BorderLayout());
        pnlFilterBar.setOpaque(false);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);
        JLabel lblL = new JLabel("KHOẢNG THỜI GIAN:");
        lblL.setFont(GiaoPos.f(12, Font.BOLD));
        lblL.setForeground(GiaoPos.CHU_PHU);
        pnlLeft.add(lblL);

        pnlTimeChips = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlTimeChips.setOpaque(false);
        veTimeChips();
        pnlLeft.add(pnlTimeChips);
        pnlFilterBar.add(pnlLeft, BorderLayout.WEST);

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄 Tải lại dữ liệu", GiaoPos.NutPos.STYLE_DONG_PHU);
        btnLamMoi.setFont(GiaoPos.f(12, Font.BOLD));
        btnLamMoi.setPreferredSize(new Dimension(140, 38));
        btnLamMoi.addActionListener(e -> napDuLieu());
        pnlFilterBar.add(btnLamMoi, BorderLayout.EAST);

        pnlTop.add(pnlFilterBar, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Biểu đồ doanh thu (Trái) + Top món bán chạy (Phải)
        JPanel pnlCenter = new JPanel(new BorderLayout(14, 0));
        pnlCenter.setOpaque(false);

        // Biểu đồ cột
        GiaoPos.ThePos pnlChartWrapper = new GiaoPos.ThePos(new BorderLayout(0, 10));
        JLabel lblChartTitle = new JLabel("BIỂU ĐỒ DOANH THU THEO NGÀY");
        lblChartTitle.setFont(GiaoPos.f(14, Font.BOLD));
        lblChartTitle.setForeground(GiaoPos.DONG_DAM);
        pnlChartWrapper.add(lblChartTitle, BorderLayout.NORTH);

        bieuDo = new BieuDoCotPos();
        pnlChartWrapper.add(bieuDo, BorderLayout.CENTER);
        pnlCenter.add(pnlChartWrapper, BorderLayout.CENTER);

        // Cột Top món bán chạy
        GiaoPos.ThePos pnlTopWrapper = new GiaoPos.ThePos(new BorderLayout(0, 10));
        pnlTopWrapper.setPreferredSize(new Dimension(380, 100));

        JLabel lblTopTitle = new JLabel("TOP MÓN BÁN CHẠY NHẤT");
        lblTopTitle.setFont(GiaoPos.f(14, Font.BOLD));
        lblTopTitle.setForeground(GiaoPos.DONG_DAM);
        pnlTopWrapper.add(lblTopTitle, BorderLayout.NORTH);

        pnlTopMon = new JPanel();
        pnlTopMon.setOpaque(false);
        pnlTopMon.setLayout(new BoxLayout(pnlTopMon, BoxLayout.Y_AXIS));

        JPanel wrapperMon = new JPanel(new BorderLayout());
        wrapperMon.setOpaque(false);
        wrapperMon.add(pnlTopMon, BorderLayout.NORTH);

        JScrollPane spMon = new JScrollPane(wrapperMon);
        spMon.setBorder(null);
        spMon.setOpaque(false);
        spMon.getViewport().setOpaque(false);
        pnlTopWrapper.add(spMon, BorderLayout.CENTER);

        pnlCenter.add(pnlTopWrapper, BorderLayout.EAST);
        add(pnlCenter, BorderLayout.CENTER);
    }

    private JPanel taoTheKPI(String tieuDe, JLabel lblVal, Color cText, Color cBg) {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout());
        pnl.setTheBackground(cBg);
        pnl.setBorderColor(new Color(cText.getRed(), cText.getGreen(), cText.getBlue(), 80));

        JLabel lt = new JLabel(tieuDe, SwingConstants.CENTER);
        lt.setFont(GiaoPos.f(11, Font.BOLD));
        lt.setForeground(cText);

        lblVal.setFont(GiaoPos.f(22, Font.BOLD));
        lblVal.setForeground(cText);

        pnl.add(lt, BorderLayout.NORTH);
        pnl.add(lblVal, BorderLayout.CENTER);
        return pnl;
    }

    private void veTimeChips() {
        pnlTimeChips.removeAll();
        int[] days = {7, 14, 30, 90, 365};
        String[] labels = {"7 ngày", "14 ngày", "30 ngày", "90 ngày", "1 năm"};

        for (int i = 0; i < days.length; i++) {
            final int d = days[i];
            GiaoPos.NutPos btn = new GiaoPos.NutPos(labels[i], GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 34));
            btn.setActive(d == soNgay);
            btn.addActionListener(e -> {
                soNgay = d;
                veTimeChips();
                napDuLieu();
            });
            pnlTimeChips.add(btn);
        }
        pnlTimeChips.revalidate();
        pnlTimeChips.repaint();
    }

    public void napDuLieu() {
        bieuDoNgay.clear();
        bieuDoDoanhThu.clear();
        topMon.clear();

        double tong = 0;
        int soDon = 0;
        double homNay = 0;

        String sqlBieuDo = "SELECT DATE(ngay_tao) AS ngay, SUM(tong_tien) AS tien, COUNT(*) AS sodon "
                         + "FROM don_hang "
                         + "WHERE ngay_tao >= DATE_SUB(CURDATE(), INTERVAL ? DAY) AND trang_thai = 'HOAN_THANH' "
                         + "GROUP BY DATE(ngay_tao) ORDER BY ngay ASC";

        String sqlTopMon = "SELECT m.ten_mon, SUM(c.so_luong) AS sl, SUM(c.so_luong * c.don_gia) AS tien "
                         + "FROM chi_tiet_don_hang c "
                         + "JOIN mon_an m ON m.ma_mon = c.ma_mon "
                         + "JOIN don_hang d ON d.ma_don_hang = c.ma_don_hang "
                         + "WHERE d.ngay_tao >= DATE_SUB(CURDATE(), INTERVAL ? DAY) AND d.trang_thai = 'HOAN_THANH' "
                         + "GROUP BY c.ma_mon ORDER BY tien DESC LIMIT 8";

        String sqlHomNay = "SELECT COALESCE(SUM(tong_tien), 0) FROM don_hang "
                         + "WHERE DATE(ngay_tao) = CURDATE() AND trang_thai = 'HOAN_THANH'";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlBieuDo)) {
                ps.setInt(1, soNgay);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String ngayStr = rs.getString("ngay");
                        double tien = rs.getDouble("tien");
                        int count = rs.getInt("sodon");

                        bieuDoNgay.add(ngayStr.length() >= 10 ? ngayStr.substring(8, 10) + "/" + ngayStr.substring(5, 7) : ngayStr);
                        bieuDoDoanhThu.add(tien);
                        tong += tien;
                        soDon += count;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlTopMon)) {
                ps.setInt(1, soNgay);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        topMon.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getDouble(3)});
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlHomNay);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) homNay = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật thẻ KPI
        lblTongDoanhThu.setText(GiaoPos.formatTien(tong));
        lblSoDonHang.setText(String.valueOf(soDon) + " đơn");
        lblGiaTriTB.setText(GiaoPos.formatTien(soDon > 0 ? tong / soDon : 0));
        lblDoanhThuHomNay.setText(GiaoPos.formatTien(homNay));

        // Vẽ lại biểu đồ cột
        bieuDo.datDuLieu(bieuDoNgay, bieuDoDoanhThu);

        // Vẽ danh sách top món
        veTopMon();
    }

    private void veTopMon() {
        pnlTopMon.removeAll();
        if (topMon.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có món nào bán ra trong khoảng thời gian này.");
            lblEmpty.setFont(GiaoPos.f(12, Font.ITALIC));
            lblEmpty.setForeground(GiaoPos.CHU_MO);
            lblEmpty.setBorder(new EmptyBorder(20, 10, 0, 0));
            pnlTopMon.add(lblEmpty);
        } else {
            int rank = 1;
            for (Object[] item : topMon) {
                String ten = (String) item[0];
                int sl = (Integer) item[1];
                double tien = (Double) item[2];
                pnlTopMon.add(taoDongTopMon(rank++, ten, sl, tien));
                pnlTopMon.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        pnlTopMon.revalidate();
        pnlTopMon.repaint();
    }

    private JPanel taoDongTopMon(int rank, String ten, int sl, double tien) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoPos.THE_VIEN, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblRank = new JLabel(String.valueOf(rank), SwingConstants.CENTER);
        lblRank.setFont(GiaoPos.f(13, Font.BOLD));
        lblRank.setForeground(rank <= 3 ? GiaoPos.DONG_CHINH : GiaoPos.CHU_PHU);
        lblRank.setPreferredSize(new Dimension(24, 24));
        p.add(lblRank, BorderLayout.WEST);

        JPanel pCenter = new JPanel(new GridLayout(2, 1, 0, 2));
        pCenter.setOpaque(false);
        JLabel lblTen = new JLabel(ten);
        lblTen.setFont(GiaoPos.f(13, Font.BOLD));
        lblTen.setForeground(GiaoPos.CHU_CHINH);

        JLabel lblSL = new JLabel("Đã bán: " + sl + " suất");
        lblSL.setFont(GiaoPos.f(11, Font.PLAIN));
        lblSL.setForeground(GiaoPos.CHU_MO);

        pCenter.add(lblTen);
        pCenter.add(lblSL);
        p.add(pCenter, BorderLayout.CENTER);

        JLabel lblTien = new JLabel(GiaoPos.formatTien(tien));
        lblTien.setFont(GiaoPos.f(13, Font.BOLD));
        lblTien.setForeground(GiaoPos.DONG_DAM);
        p.add(lblTien, BorderLayout.EAST);

        return p;
    }

    // Biểu đồ cột vẽ bằng Graphics2D tông màu Đồng
    private static class BieuDoCotPos extends JPanel {
        private final List<String> labels = new ArrayList<>();
        private final List<Double> values = new ArrayList<>();

        BieuDoCotPos() { setOpaque(false); }

        void datDuLieu(List<String> l, List<Double> v) {
            labels.clear();
            labels.addAll(l);
            values.clear();
            values.addAll(v);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int n = values.size();

            if (n == 0) {
                g2.setFont(GiaoPos.f(13, Font.ITALIC));
                g2.setColor(GiaoPos.CHU_MO);
                g2.drawString("Không có dữ liệu trong khoảng thời gian này.", w / 2 - 120, h / 2);
                g2.dispose();
                return;
            }

            double max = 1;
            for (double val : values) max = Math.max(max, val);

            int btmPad = 36;
            int topPad = 24;
            int leftPad = 20;
            int rightPad = 20;
            int chartH = h - btmPad - topPad;
            int barW = Math.max(12, Math.min(48, (w - leftPad - rightPad) / n - 8));

            // Đường kẻ đáy
            g2.setColor(GiaoPos.THE_VIEN);
            g2.drawLine(leftPad, h - btmPad, w - rightPad, h - btmPad);

            int x = leftPad + 10;
            int step = (w - leftPad - rightPad) / n;

            for (int i = 0; i < n; i++) {
                double val = values.get(i);
                int barH = (int) ((val / max) * chartH);
                int y = h - btmPad - barH;

                // Cột Gradient Đồng
                g2.setPaint(new GradientPaint(0, y, GiaoPos.DONG_SANG, 0, h - btmPad, GiaoPos.DONG_CHINH));
                g2.fillRoundRect(x, y, barW, barH, 8, 8);

                // Viền cột
                g2.setColor(GiaoPos.DONG_DAM);
                g2.drawRoundRect(x, y, barW, barH, 8, 8);

                // Nhãn ngày bên dưới
                g2.setFont(GiaoPos.f(10, Font.PLAIN));
                g2.setColor(GiaoPos.CHU_PHU);
                FontMetrics fm = g2.getFontMetrics();
                String lbl = labels.get(i);
                int tw = fm.stringWidth(lbl);
                g2.drawString(lbl, x + (barW - tw) / 2, h - btmPad + 16);

                x += step;
            }
            g2.dispose();
        }
    }
}
