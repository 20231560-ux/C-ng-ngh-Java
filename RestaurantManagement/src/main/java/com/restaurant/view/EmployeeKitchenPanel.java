package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class EmployeeKitchenPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color BG = new Color(15, 15, 15);
    private final Color PANEL = new Color(22, 22, 22);
    private final Color PANEL2 = new Color(28, 28, 28);
    private final Color LINE = new Color(48, 48, 48);
    private final Color WHITE = new Color(242, 242, 242);
    private final Color MUTED = new Color(145, 145, 145);
    private final Color GOLD = new Color(220, 177, 55);
    private final Color GREEN = new Color(55, 190, 125);
    private final Color ORANGE = new Color(235, 155, 55);
    private final Color BLUE = new Color(80, 145, 230);

    private JLabel lbCho;
    private JLabel lbDangLam;
    private JLabel lbXong;
    private JPanel danhSach;

    private final List<DonBep> dsDon = new ArrayList<>();

    public EmployeeKitchenPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(new EmptyBorder(28, 30, 28, 30));

        taoGiaoDien();
        napDuLieu();
    }

    private void taoGiaoDien() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Điều hành bếp");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel sub = new JLabel("Theo dõi và xử lý các món đang chế biến");
        sub.setForeground(MUTED);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(sub);

        JButton refresh = nut("Làm mới", GOLD);
        refresh.addActionListener(e -> napDuLieu());

        header.add(titleBox, BorderLayout.WEST);
        header.add(refresh, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 18));
        center.setOpaque(false);

        center.add(taoThongKe(), BorderLayout.NORTH);

        danhSach = new JPanel();
        danhSach.setOpaque(false);
        danhSach.setLayout(new BoxLayout(danhSach, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(danhSach);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        center.add(scroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private JPanel taoThongKe() {
        JPanel p = new JPanel(new GridLayout(1, 3, 12, 0));
        p.setOpaque(false);

        lbCho = new JLabel("0");
        lbDangLam = new JLabel("0");
        lbXong = new JLabel("0");

        p.add(theThongKe("ĐƠN CHỜ", lbCho, ORANGE));
        p.add(theThongKe("ĐANG CHẾ BIẾN", lbDangLam, BLUE));
        p.add(theThongKe("ĐÃ HOÀN THÀNH", lbXong, GREEN));

        return p;
    }

    private JPanel theThongKe(String ten, JLabel so, Color mau) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel t = new JLabel(ten);
        t.setForeground(MUTED);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));

        so.setForeground(mau);
        so.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel dot = new JLabel("●");
        dot.setForeground(mau);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(t, BorderLayout.WEST);
        top.add(dot, BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);
        p.add(so, BorderLayout.CENTER);

        return p;
    }

    private void napDuLieu() {
        dsDon.clear();

        boolean coDuLieu = docCSDL();

        if (!coDuLieu || dsDon.isEmpty()) {
            taoDuLieuMau();
        }

        capNhatThongKe();
        hienThiDanhSach();
    }

    private boolean docCSDL() {
        String sql =
                "SELECT d.ma_don_hang, " +
                "COALESCE(b.ten_ban, 'Mang đi') AS ten_ban, " +
                "m.ten_mon, " +
                "c.so_luong, " +
                "d.trang_thai, " +
                "TIME_FORMAT(d.ngay_tao,'%H:%i') AS gio " +
                "FROM chi_tiet_don_hang c " +
                "JOIN don_hang d ON d.ma_don_hang = c.ma_don_hang " +
                "JOIN mon_an m ON m.ma_mon = c.ma_mon " +
                "LEFT JOIN ban_an b ON b.ma_ban = d.ma_ban " +
                "WHERE d.trang_thai IN ('CHO_XU_LY','DANG_CHUAN_BI','HOAN_THANH') " +
                "ORDER BY d.ma_don_hang DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String trangThai = rs.getString("trang_thai");

                String trangThaiHienThi;

                if ("HOAN_THANH".equalsIgnoreCase(trangThai)) {
                    trangThaiHienThi = "Đã hoàn thành";
                } else if ("DANG_CHUAN_BI".equalsIgnoreCase(trangThai)) {
                    trangThaiHienThi = "Đang chế biến";
                } else {
                    trangThaiHienThi = "Đang chờ";
                }

                dsDon.add(new DonBep(
                        rs.getString("ma_don_hang"),
                        rs.getString("ten_ban"),
                        rs.getString("ten_mon"),
                        rs.getInt("so_luong"),
                        trangThaiHienThi,
                        rs.getString("gio")
                ));
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void taoDuLieuMau() {
        dsDon.add(new DonBep(
                "DH1025",
                "Bàn B05",
                "Bò lúc lắc sốt tiêu đen",
                2,
                "Đang chờ",
                "17:18"
        ));

        dsDon.add(new DonBep(
                "DH1024",
                "Bàn B08",
                "Mì Ý hải sản",
                2,
                "Đang chế biến",
                "17:12"
        ));

        dsDon.add(new DonBep(
                "DH1023",
                "Bàn B03",
                "Cá hồi áp chảo",
                1,
                "Đang chế biến",
                "17:05"
        ));

        dsDon.add(new DonBep(
                "DH1022",
                "Bàn B10",
                "Salad Caesar",
                2,
                "Đã hoàn thành",
                "16:58"
        ));

        dsDon.add(new DonBep(
                "DH1021",
                "Bàn B06",
                "Soup nấm kem",
                3,
                "Đã hoàn thành",
                "16:52"
        ));
    }

    private void capNhatThongKe() {
        int cho = 0;
        int dangLam = 0;
        int xong = 0;

        for (DonBep d : dsDon) {
            if ("Đang chờ".equals(d.trangThai)) {
                cho++;
            } else if ("Đang chế biến".equals(d.trangThai)) {
                dangLam++;
            } else {
                xong++;
            }
        }

        lbCho.setText(String.valueOf(cho));
        lbDangLam.setText(String.valueOf(dangLam));
        lbXong.setText(String.valueOf(xong));
    }

    private void hienThiDanhSach() {
        danhSach.removeAll();

        JLabel title = new JLabel("Danh sách đơn trong bếp");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setBorder(new EmptyBorder(4, 2, 8, 0));

        danhSach.add(title);

        for (DonBep don : dsDon) {
            danhSach.add(taoDon(don));
            danhSach.add(Box.createVerticalStrut(10));
        }

        if (dsDon.isEmpty()) {
            JPanel empty = new JPanel(new BorderLayout());
            empty.setBackground(PANEL);
            empty.setBorder(BorderFactory.createLineBorder(LINE));
            empty.setPreferredSize(new Dimension(0, 100));

            JLabel text = new JLabel(
                    "Hiện không có đơn hàng trong bếp",
                    SwingConstants.CENTER
            );
            text.setForeground(MUTED);
            text.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            empty.add(text);

            danhSach.add(empty);
        }

        danhSach.revalidate();
        danhSach.repaint();
    }

    private JPanel taoDon(DonBep don) {
        JPanel card = new JPanel(new BorderLayout(18, 0));
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(15, 18, 15, 18)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new BoxLayout(trai, BoxLayout.Y_AXIS));

        JLabel ma = new JLabel(don.maDon);
        ma.setForeground(GOLD);
        ma.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel ban = new JLabel(don.ban + "  •  " + don.gio);
        ban.setForeground(MUTED);
        ban.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        trai.add(ma);
        trai.add(Box.createVerticalStrut(5));
        trai.add(ban);

        JPanel mon = new JPanel();
        mon.setOpaque(false);
        mon.setLayout(new BoxLayout(mon, BoxLayout.Y_AXIS));

        JLabel tenMon = new JLabel(don.mon);
        tenMon.setForeground(WHITE);
        tenMon.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel soLuong = new JLabel("Số lượng: " + don.soLuong);
        soLuong.setForeground(MUTED);
        soLuong.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        mon.add(tenMon);
        mon.add(Box.createVerticalStrut(5));
        mon.add(soLuong);

        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        phai.setOpaque(false);

        JLabel trangThai = new JLabel(don.trangThai);
        trangThai.setHorizontalAlignment(SwingConstants.CENTER);
        trangThai.setPreferredSize(new Dimension(125, 30));
        trangThai.setFont(new Font("Segoe UI", Font.BOLD, 11));

        if ("Đang chờ".equals(don.trangThai)) {
            trangThai.setForeground(ORANGE);
            trangThai.setBorder(BorderFactory.createLineBorder(ORANGE));
        } else if ("Đang chế biến".equals(don.trangThai)) {
            trangThai.setForeground(BLUE);
            trangThai.setBorder(BorderFactory.createLineBorder(BLUE));
        } else {
            trangThai.setForeground(GREEN);
            trangThai.setBorder(BorderFactory.createLineBorder(GREEN));
        }

        phai.add(trangThai);

        if ("Đang chờ".equals(don.trangThai)) {
            JButton batDau = nut("Bắt đầu", GOLD);
            batDau.addActionListener(e -> {
                don.trangThai = "Đang chế biến";
                capNhatThongKe();
                hienThiDanhSach();
            });
            phai.add(batDau);
        } else if ("Đang chế biến".equals(don.trangThai)) {
            JButton xong = nut("Hoàn thành", GREEN);
            xong.addActionListener(e -> {
                don.trangThai = "Đã hoàn thành";
                capNhatThongKe();
                hienThiDanhSach();
            });
            phai.add(xong);
        }

        card.add(trai, BorderLayout.WEST);
        card.add(mon, BorderLayout.CENTER);
        card.add(phai, BorderLayout.EAST);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return card;
    }

    private JButton nut(String text, Color color) {
        JButton b = new JButton(text);
        b.setForeground(color);
        b.setBackground(PANEL2);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(color));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(100, 32));
        return b;
    }

    private static class DonBep {
        String maDon;
        String ban;
        String mon;
        int soLuong;
        String trangThai;
        String gio;

        DonBep(
                String maDon,
                String ban,
                String mon,
                int soLuong,
                String trangThai,
                String gio
        ) {
            this.maDon = maDon;
            this.ban = ban;
            this.mon = mon;
            this.soLuong = soLuong;
            this.trangThai = trangThai;
            this.gio = gio;
        }
    }
}