package com.restaurant.view;

import com.restaurant.config.DatabaseConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EmployeeOrderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color NEN = new Color(18, 18, 18);
    private static final Color NEN_BANG = new Color(24, 24, 24);
    private static final Color NEN_HOVER = new Color(34, 34, 34);
    private static final Color VIEN = new Color(55, 55, 55);
    private static final Color CHU = new Color(238, 235, 230);
    private static final Color CHU_PHU = new Color(150, 145, 138);
    private static final Color VANG = new Color(212, 163, 89);
    private static final Color XANH = new Color(91, 180, 130);
    private static final Color CAM = new Color(230, 154, 82);
    private static final Color DO = new Color(210, 91, 91);
    private static final Color XAM = new Color(115, 115, 115);

    private final JLabel lbTatCa = new JLabel("0");
    private final JLabel lbChoXuLy = new JLabel("0");
    private final JLabel lbDangCheBien = new JLabel("0");
    private final JLabel lbHoanThanh = new JLabel("0");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã đơn", "Bàn", "Khách hàng", "Thời gian", "Tổng tiền", "Trạng thái"}, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    private final List<Object[]> duLieu = new ArrayList<>();

    private String boLoc = "Tất cả";

    public EmployeeOrderPanel() {
        setLayout(new BorderLayout());
        setBackground(NEN);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        add(taoTieuDe(), BorderLayout.NORTH);
        add(taoNoiDung(), BorderLayout.CENTER);

        napDuLieu();
    }

    private JPanel taoTieuDe() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 22, 0));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new javax.swing.BoxLayout(trai, javax.swing.BoxLayout.Y_AXIS));

        JLabel title = new JLabel("QUẢN LÝ ĐƠN HÀNG");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel sub = new JLabel("Theo dõi và xử lý đơn hàng trong ca làm việc");
        sub.setForeground(CHU_PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        trai.add(title);
        trai.add(javax.swing.Box.createVerticalStrut(5));
        trai.add(sub);

        JLabel ca = new JLabel("CA LÀM VIỆC  •  " + layCaHienTai());
        ca.setForeground(VANG);
        ca.setFont(new Font("Segoe UI", Font.BOLD, 11));
        ca.setHorizontalAlignment(SwingConstants.RIGHT);

        p.add(trai, BorderLayout.WEST);
        p.add(ca, BorderLayout.EAST);

        return p;
    }

    private JPanel taoNoiDung() {
        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setOpaque(false);

        main.add(taoThongKe(), BorderLayout.NORTH);
        main.add(taoDanhSach(), BorderLayout.CENTER);

        return main;
    }

    private JPanel taoThongKe() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 105));

        p.add(taoThe("TẤT CẢ ĐƠN", lbTatCa, "Tổng số đơn hàng", VANG));
        p.add(taoThe("CHỜ XỬ LÝ", lbChoXuLy, "Đơn cần tiếp nhận", CAM));
        p.add(taoThe("ĐANG CHẾ BIẾN", lbDangCheBien, "Đơn đang ở bếp", new Color(190, 145, 90)));
        p.add(taoThe("HOÀN THÀNH", lbHoanThanh, "Đơn đã hoàn tất", XANH));

        return p;
    }

    private JPanel taoThe(String tieuDe, JLabel giaTri, String moTa, Color mau) {
        JPanel p = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(NEN_BANG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(VIEN);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(14, 17, 13, 17));

        JLabel lbl = new JLabel(tieuDe);
        lbl.setForeground(CHU_PHU);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));

        giaTri.setForeground(mau);
        giaTri.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel desc = new JLabel(moTa);
        desc.setForeground(new Color(105, 105, 105));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(giaTri, BorderLayout.CENTER);
        center.add(desc, BorderLayout.SOUTH);

        p.add(lbl, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);

        return p;
    }

    private JPanel taoDanhSach() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setOpaque(false);

        JPanel dau = new JPanel(new BorderLayout());
        dau.setOpaque(false);

        JLabel title = new JLabel("DANH SÁCH ĐƠN HÀNG");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel moTa = new JLabel("Các đơn hàng mới nhất trong hệ thống");
        moTa.setForeground(CHU_PHU);
        moTa.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new javax.swing.BoxLayout(trai, javax.swing.BoxLayout.Y_AXIS));
        trai.add(title);
        trai.add(javax.swing.Box.createVerticalStrut(3));
        trai.add(moTa);

        dau.add(trai, BorderLayout.WEST);
        dau.add(taoBoLoc(), BorderLayout.EAST);

        p.add(dau, BorderLayout.NORTH);
        p.add(taoBang(), BorderLayout.CENTER);

        return p;
    }

    private JPanel taoBoLoc() {
        JPanel p = new JPanel(new GridLayout(1, 4, 5, 0));
        p.setOpaque(false);

        p.add(taoNutLoc("Tất cả"));
        p.add(taoNutLoc("Chờ xử lý"));
        p.add(taoNutLoc("Đang chế biến"));
        p.add(taoNutLoc("Hoàn thành"));

        return p;
    }

    private JButton taoNutLoc(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        b.setForeground(text.equals(boLoc) ? VANG : CHU_PHU);
        b.setBackground(text.equals(boLoc) ? new Color(45, 37, 25) : NEN);
        b.setBorder(BorderFactory.createLineBorder(text.equals(boLoc) ? VANG : VIEN));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(7, 12, 7, 12));

        b.addActionListener(e -> {
            boLoc = text;
            capNhatBoLoc();
            locDuLieu();
        });

        return b;
    }

    private JScrollPane taoBang() {
        table.setBackground(NEN_BANG);
        table.setForeground(CHU);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(43);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(42, 42, 42));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(NEN_HOVER);
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(CHU_PHU);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setForeground(CHU);
        center.setBackground(NEN_BANG);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        right.setForeground(CHU);
        right.setBackground(NEN_BANG);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(right);

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(230);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable t,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                JLabel l = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);

                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                l.setOpaque(true);

                String s = value == null ? "" : value.toString();

                if (s.equals("Chờ xử lý")) {
                    l.setForeground(CAM);
                    l.setBackground(new Color(55, 40, 25));
                } else if (s.equals("Đang chế biến")) {
                    l.setForeground(VANG);
                    l.setBackground(new Color(52, 43, 28));
                } else if (s.equals("Hoàn thành")) {
                    l.setForeground(XANH);
                    l.setBackground(new Color(25, 50, 38));
                } else {
                    l.setForeground(CHU_PHU);
                    l.setBackground(NEN_BANG);
                }

                if (isSelected) {
                    l.setBackground(NEN_HOVER);
                }

                return l;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(VIEN));
        scroll.getViewport().setBackground(NEN_BANG);
        scroll.setBackground(NEN_BANG);

        return scroll;
    }

    private void napDuLieu() {
        duLieu.clear();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT d.ma_don, " +
                     "IFNULL(b.ten_ban,'Mang đi'), " +
                     "IFNULL(k.ho_ten,'Khách lẻ'), " +
                     "TIME_FORMAT(d.ngay_tao,'%H:%i'), " +
                     "IFNULL(d.tong_tien,0), " +
                     "d.trang_thai " +
                     "FROM don_hang d " +
                     "LEFT JOIN ban_an b ON b.ma_ban=d.ma_ban " +
                     "LEFT JOIN khach_hang k ON k.ma_khach_hang=d.ma_khach_hang " +
                     "ORDER BY d.ma_don_hang DESC")) {

            while (rs.next()) {
                duLieu.add(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        chuyenTrangThai(rs.getString(6))
                });
            }

        } catch (Exception e) {
            duLieu.clear();
        }

        capNhatThongKe();
        locDuLieu();
    }

    private void capNhatThongKe() {
        int tatCa = duLieu.size();
        int choXuLy = 0;
        int dangCheBien = 0;
        int hoanThanh = 0;

        for (Object[] row : duLieu) {
            String trangThai = String.valueOf(row[5]);

            if ("Chờ xử lý".equals(trangThai)) {
                choXuLy++;
            } else if ("Đang chế biến".equals(trangThai)) {
                dangCheBien++;
            } else if ("Hoàn thành".equals(trangThai)) {
                hoanThanh++;
            }
        }

        lbTatCa.setText(String.valueOf(tatCa));
        lbChoXuLy.setText(String.valueOf(choXuLy));
        lbDangCheBien.setText(String.valueOf(dangCheBien));
        lbHoanThanh.setText(String.valueOf(hoanThanh));
    }

    private void locDuLieu() {
        model.setRowCount(0);

        for (Object[] row : duLieu) {
            String trangThai = String.valueOf(row[5]);

            if ("Tất cả".equals(boLoc)
                    || boLoc.equals(trangThai)) {

                model.addRow(new Object[]{
                        row[0],
                        row[1],
                        row[2],
                        row[3],
                        dinhDangTien((Double) row[4]),
                        row[5]
                });
            }
        }
    }

    private void capNhatBoLoc() {
        ContainerButtonUpdater.update(this, boLoc);
    }

    private String chuyenTrangThai(String s) {
        if (s == null) {
            return "Chờ xử lý";
        }

        String v = s.trim().toUpperCase();

        if (v.equals("HOAN_THANH")
                || v.equals("HOÀN THÀNH")
                || v.equals("DA_HOAN_THANH")
                || v.equals("DA GIAO")) {
            return "Hoàn thành";
        }

        if (v.equals("DANG_CHE_BIEN")
                || v.equals("DANG_CHUAN_BI")
                || v.equals("DANG_LAM")
                || v.equals("DANG_PHUC_VU")
                || v.equals("DANG CHE BIEN")) {
            return "Đang chế biến";
        }

        return "Chờ xử lý";
    }

    private String dinhDangTien(double tien) {
        return new DecimalFormat("#,##0").format(tien) + " đ";
    }

    private String layCaHienTai() {
        int h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);

        if (h < 14) {
            return "06:00 – 14:00";
        }

        if (h < 18) {
            return "14:00 – 18:00";
        }

        return "18:00 – 23:00";
    }

    private static class ContainerButtonUpdater {

        static void update(JPanel root, String selected) {
            for (java.awt.Component c : root.getComponents()) {
                if (c instanceof JPanel) {
                    updatePanel((JPanel) c, selected);
                }
            }
        }

        private static void updatePanel(JPanel panel, String selected) {
            for (java.awt.Component c : panel.getComponents()) {
                if (c instanceof JButton) {
                    JButton b = (JButton) c;
                    boolean active = b.getText().equals(selected);
                    b.setForeground(active ? VANG : CHU_PHU);
                    b.setBackground(active ? new Color(45, 37, 25) : NEN);
                    b.setBorder(BorderFactory.createLineBorder(active ? VANG : VIEN));
                }

                if (c instanceof JPanel) {
                    updatePanel((JPanel) c, selected);
                }
            }
        }
    }
}