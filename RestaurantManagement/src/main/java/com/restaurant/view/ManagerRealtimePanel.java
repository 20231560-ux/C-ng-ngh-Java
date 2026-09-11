package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.model.DonHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManagerRealtimePanel extends JPanel {

    private final JPanel contentPanel;
    private final JLabel lblSoBan;
    private final JLabel lblTrangThai;
    private final Timer timer;
    private final DonHangDAO donHangDAO;

    private final Color NEN = new Color(247, 243, 236);
    private final Color TRANG = new Color(255, 253, 249);
    private final Color NAU = new Color(62, 48, 39);
    private final Color VANG = new Color(181, 137, 73);
    private final Color CHU = new Color(48, 40, 32);
    private final Color XAM = new Color(126, 111, 96);
    private final Color VIEN = new Color(231, 220, 206);

    public ManagerRealtimePanel() {
        donHangDAO = new DonHangDAO();

        setLayout(new BorderLayout());
        setBackground(NEN);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NEN);
        header.setBorder(new EmptyBorder(25, 28, 18, 28));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Vận hành Realtime");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(CHU);

        JLabel subtitle = new JLabel("Theo dõi bàn, khách đang chọn món và đơn mới");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(XAM);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);

        JPanel right = new JPanel(new GridLayout(2, 1, 0, 3));
        right.setOpaque(false);

        lblSoBan = new JLabel("0 bàn");
        lblSoBan.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSoBan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSoBan.setForeground(VANG);

        lblTrangThai = new JLabel("Đang cập nhật");
        lblTrangThai.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTrangThai.setForeground(XAM);

        right.add(lblSoBan);
        right.add(lblTrangThai);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(NEN);
        contentPanel.setBorder(new EmptyBorder(0, 28, 30, 28));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(NEN);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        add(scroll, BorderLayout.CENTER);

        timer = new Timer(1500, e -> taiDuLieu());
        timer.start();

        taiDuLieu();
    }

    private void taiDuLieu() {
        SwingUtilities.invokeLater(() -> {

            List<BanRealtime> danhSach = new ArrayList<>();

            String sql = """
                    SELECT
                        b.ma_ban,
                        b.ma_ban_so,
                        b.ten_ban,
                        b.trang_thai
                    FROM ban_an b
                    WHERE
                        b.trang_thai = 'DANG_CHON_MON'
                        OR EXISTS (
                            SELECT 1
                            FROM gio_hang_tam g
                            WHERE g.ma_ban = b.ma_ban
                        )
                        OR EXISTS (
                            SELECT 1
                            FROM don_hang d
                            WHERE d.ma_ban = b.ma_ban
                            AND d.trang_thai = 'CHO_XU_LY'
                        )
                    ORDER BY b.ma_ban_so
                    """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()
            ) {

                while (rs.next()) {

                    BanRealtime ban = new BanRealtime();

                    ban.maBan = rs.getInt("ma_ban");
                    ban.maBanSo = rs.getString("ma_ban_so");
                    ban.tenBan = rs.getString("ten_ban");
                    ban.trangThai = rs.getString("trang_thai");

                    layGioHang(conn, ban);
                    layDonCho(conn, ban);

                    if (ban.coGioHang || ban.coDonCho) {
                        danhSach.add(ban);
                    }
                }

            } catch (Exception e) {
                lblTrangThai.setText("Lỗi kết nối dữ liệu");
                hienThiLoi(e);
                return;
            }

            hienThiDanhSach(danhSach);
        });
    }

    private void layGioHang(Connection conn, BanRealtime ban) throws SQLException {

        String sql = """
                SELECT
                    g.ma_mon,
                    g.so_luong,
                    g.ghi_chu,
                    m.ten_mon,
                    m.gia_ban
                FROM gio_hang_tam g
                JOIN mon_an m
                    ON g.ma_mon = m.ma_mon
                WHERE g.ma_ban = ?
                ORDER BY g.ma_gio_hang
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ban.maBan);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    MonRealtime mon = new MonRealtime();

                    mon.maMon = rs.getInt("ma_mon");
                    mon.tenMon = rs.getString("ten_mon");
                    mon.soLuong = rs.getInt("so_luong");
                    mon.gia = rs.getDouble("gia_ban");
                    mon.ghiChu = rs.getString("ghi_chu");

                    ban.gioHang.add(mon);
                    ban.coGioHang = true;
                }
            }
        }
    }

    private void layDonCho(Connection conn, BanRealtime ban) throws SQLException {

        String sql = """
                SELECT
                    d.ma_don_hang,
                    d.ma_don,
                    d.tien_tam_tinh,
                    d.tong_tien,
                    d.ghi_chu,
                    d.ngay_tao
                FROM don_hang d
                WHERE d.ma_ban = ?
                AND d.trang_thai = 'CHO_XU_LY'
                ORDER BY d.ngay_tao DESC
                LIMIT 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ban.maBan);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    ban.maDonHang = rs.getInt("ma_don_hang");
                    ban.maDon = rs.getString("ma_don");
                    ban.tienTamTinh = rs.getDouble("tien_tam_tinh");
                    ban.tongTien = rs.getDouble("tong_tien");
                    ban.ghiChuDon = rs.getString("ghi_chu");
                    ban.coDonCho = true;

                    layChiTietDon(conn, ban);
                }
            }
        }
    }

    private void layChiTietDon(Connection conn, BanRealtime ban) throws SQLException {

        String sql = """
                SELECT
                    m.ten_mon,
                    ct.so_luong,
                    ct.don_gia,
                    ct.ghi_chu,
                    ct.trang_thai_mon
                FROM chi_tiet_don_hang ct
                JOIN mon_an m
                    ON ct.ma_mon = m.ma_mon
                WHERE ct.ma_don_hang = ?
                ORDER BY ct.ma_chi_tiet
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ban.maDonHang);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    MonRealtime mon = new MonRealtime();

                    mon.tenMon = rs.getString("ten_mon");
                    mon.soLuong = rs.getInt("so_luong");
                    mon.gia = rs.getDouble("don_gia");
                    mon.ghiChu = rs.getString("ghi_chu");
                    mon.trangThaiMon = rs.getString("trang_thai_mon");

                    ban.donChinhThuc.add(mon);
                }
            }
        }
    }

    private void hienThiDanhSach(List<BanRealtime> danhSach) {

        contentPanel.removeAll();

        int soBan = danhSach.size();

        lblSoBan.setText(soBan + (soBan == 1 ? " bàn" : " bàn"));
        lblTrangThai.setText("Tự động cập nhật");

        if (danhSach.isEmpty()) {

            JPanel empty = new JPanel();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setBackground(TRANG);
            empty.setBorder(new EmptyBorder(55, 30, 55, 30));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel title = new JLabel("Chưa có bàn đang gọi món");
            title.setFont(new Font("Segoe UI", Font.BOLD, 21));
            title.setForeground(CHU);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel text = new JLabel("Khi khách quét QR hoặc bắt đầu chọn món, bàn sẽ xuất hiện tại đây.");
            text.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            text.setForeground(XAM);
            text.setAlignmentX(Component.CENTER_ALIGNMENT);

            empty.add(title);
            empty.add(Box.createVerticalStrut(8));
            empty.add(text);

            contentPanel.add(empty);

        } else {

            for (BanRealtime ban : danhSach) {

                JPanel card = taoCardBan(ban);

                contentPanel.add(card);
                contentPanel.add(Box.createVerticalStrut(15));
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel taoCardBan(BanRealtime ban) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(TRANG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                new EmptyBorder(18, 20, 18, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel tenBan = new JLabel("BÀN " + ban.maBanSo);
        tenBan.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tenBan.setForeground(CHU);

        JLabel tenBanNhaHang = new JLabel(
                ban.tenBan == null ? "" : ban.tenBan
        );
        tenBanNhaHang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tenBanNhaHang.setForeground(XAM);

        titlePanel.add(tenBan);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(tenBanNhaHang);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);

        String textStatus;

        if (ban.coDonCho) {
            textStatus = "CHỜ TIẾP NHẬN";
        } else {
            textStatus = "KHÁCH ĐANG CHỌN";
        }

        JLabel status = new JLabel(textStatus);
        status.setOpaque(true);
        status.setBackground(ban.coDonCho ? new Color(238, 225, 201) : new Color(245, 235, 219));
        status.setForeground(ban.coDonCho ? new Color(130, 88, 39) : new Color(120, 91, 61));
        status.setBorder(new EmptyBorder(7, 12, 7, 12));
        status.setFont(new Font("Segoe UI", Font.BOLD, 11));

        statusPanel.add(status, BorderLayout.NORTH);

        top.add(titlePanel, BorderLayout.WEST);
        top.add(statusPanel, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(15, 0, 0, 0));

        if (ban.coGioHang) {

            JLabel section = new JLabel("KHÁCH ĐANG CHỌN MÓN");
            section.setFont(new Font("Segoe UI", Font.BOLD, 12));
            section.setForeground(VANG);

            body.add(section);
            body.add(Box.createVerticalStrut(8));

            double tamTinh = 0;

            for (MonRealtime mon : ban.gioHang) {

                JPanel row = taoDongMon(
                        mon.tenMon,
                        mon.soLuong,
                        mon.gia
                );

                body.add(row);

                tamTinh += mon.soLuong * mon.gia;
            }

            JPanel total = taoDongTong(
                    "Tạm tính hiện tại",
                    tamTinh
            );

            body.add(Box.createVerticalStrut(8));
            body.add(total);
        }

        if (ban.coDonCho) {

            if (ban.coGioHang) {
                body.add(Box.createVerticalStrut(18));
            }

            JSeparator separator = new JSeparator();
            separator.setForeground(VIEN);
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);

            body.add(separator);
            body.add(Box.createVerticalStrut(15));

            JLabel section = new JLabel(
                    "ĐƠN ĐÃ GỬI • " + ban.maDon
            );

            section.setFont(new Font("Segoe UI", Font.BOLD, 12));
            section.setForeground(VANG);

            body.add(section);
            body.add(Box.createVerticalStrut(8));

            for (MonRealtime mon : ban.donChinhThuc) {

                JPanel row = taoDongMon(
                        mon.tenMon,
                        mon.soLuong,
                        mon.gia
                );

                body.add(row);
            }

            JPanel total = taoDongTong(
                    "Tổng cộng",
                    ban.tongTien
            );

            body.add(Box.createVerticalStrut(8));
            body.add(total);

            JPanel action = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
            action.setOpaque(false);

            JButton btn = new JButton("TIẾP NHẬN ĐƠN");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(VANG);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(12, 22, 12, 22));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> {

                boolean ok = donHangDAO.tiepNhanDonHang(ban.maDonHang);

                if (ok) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Đã tiếp nhận đơn " + ban.maDon + ".",
                            "Savoré",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    taiDuLieu();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Không thể tiếp nhận đơn.",
                            "Savoré",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            action.add(btn);

            body.add(action);
        }

        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel taoDongMon(
            String tenMon,
            int soLuong,
            double gia
    ) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(7, 0, 7, 0));

        JLabel name = new JLabel(
                tenMon + "  × " + soLuong
        );

        name.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        name.setForeground(CHU);

        JLabel price = new JLabel(
                dinhDangTien(gia * soLuong)
        );

        price.setFont(new Font("Segoe UI", Font.BOLD, 12));
        price.setForeground(new Color(125, 82, 38));

        row.add(name, BorderLayout.WEST);
        row.add(price, BorderLayout.EAST);

        return row;
    }

    private JPanel taoDongTong(
            String ten,
            double tien
    ) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 3, 0));

        JLabel name = new JLabel(ten);
        name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        name.setForeground(XAM);

        JLabel value = new JLabel(dinhDangTien(tien));
        value.setFont(new Font("Segoe UI", Font.BOLD, 15));
        value.setForeground(VANG);

        row.add(name, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);

        return row;
    }

    private String dinhDangTien(double tien) {

        NumberFormat format = NumberFormat.getNumberInstance(
                new Locale("vi", "VN")
        );

        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);

        return format.format(tien) + "đ";
    }

    private void hienThiLoi(Exception e) {

        System.out.println("ManagerRealtimePanel: " + e.getMessage());
    }

    private static class BanRealtime {

        int maBan;
        String maBanSo;
        String tenBan;
        String trangThai;

        boolean coGioHang;
        boolean coDonCho;

        int maDonHang;
        String maDon;

        double tienTamTinh;
        double tongTien;

        String ghiChuDon;

        List<MonRealtime> gioHang = new ArrayList<>();
        List<MonRealtime> donChinhThuc = new ArrayList<>();
    }

    private static class MonRealtime {

        int maMon;
        String tenMon;
        int soLuong;
        double gia;
        String ghiChu;
        String trangThaiMon;
    }
}