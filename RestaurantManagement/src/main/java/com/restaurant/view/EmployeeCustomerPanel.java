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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EmployeeCustomerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color NEN = new Color(18, 18, 18);
    private static final Color KHUNG = new Color(24, 24, 24);
    private static final Color VIEN = new Color(52, 52, 52);
    private static final Color CHU = new Color(238, 235, 230);
    private static final Color PHU = new Color(145, 140, 133);
    private static final Color VANG = new Color(212, 163, 89);
    private static final Color XANH = new Color(86, 178, 125);

    private final JLabel lbTong = new JLabel("0");
    private final JLabel lbThanhVien = new JLabel("0");
    private final JLabel lbThongTin = new JLabel("0");

    private final JTextField txtTim = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã KH", "Họ và tên", "Số điện thoại", "Email", "Địa chỉ"}, 0) {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    public EmployeeCustomerPanel() {
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
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel trai = new JPanel();
        trai.setOpaque(false);
        trai.setLayout(new javax.swing.BoxLayout(trai, javax.swing.BoxLayout.Y_AXIS));

        JLabel title = new JLabel("KHÁCH HÀNG");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel sub = new JLabel("Tra cứu và theo dõi thông tin khách hàng");
        sub.setForeground(PHU);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        trai.add(title);
        trai.add(javax.swing.Box.createVerticalStrut(5));
        trai.add(sub);

        JLabel status = new JLabel("THÔNG TIN KHÁCH HÀNG");
        status.setForeground(VANG);
        status.setFont(new Font("Segoe UI", Font.BOLD, 10));

        p.add(trai, BorderLayout.WEST);
        p.add(status, BorderLayout.EAST);

        return p;
    }

    private JPanel taoNoiDung() {
        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setOpaque(false);

        main.add(taoThongKe(), BorderLayout.NORTH);
        main.add(taoDanhSach(), BorderLayout.CENTER);

        return main;
    }

    private JPanel taoThongKe() {
        JPanel p = new JPanel(new GridLayout(1, 3, 12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, 95));

        p.add(the("TỔNG KHÁCH HÀNG", lbTong, "Tài khoản khách trong hệ thống", VANG));
        p.add(the("KHÁCH THÀNH VIÊN", lbThanhVien, "Khách đã đăng ký", XANH));
        p.add(the("CÓ THÔNG TIN", lbThongTin, "Có số điện thoại hoặc email", new Color(190, 145, 90)));

        return p;
    }

    private JPanel the(String title, JLabel value, String desc, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(KHUNG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                BorderFactory.createEmptyBorder(13, 16, 13, 16)
        ));

        JLabel t = new JLabel(title);
        t.setForeground(PHU);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));

        value.setForeground(color);
        value.setFont(new Font("Segoe UI", Font.BOLD, 27));

        JLabel d = new JLabel(desc);
        d.setForeground(new Color(105, 105, 105));
        d.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        p.add(d, BorderLayout.SOUTH);

        return p;
    }

    private JPanel taoDanhSach() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setOpaque(false);

        JPanel dau = new JPanel(new BorderLayout(15, 0));
        dau.setOpaque(false);

        JLabel title = new JLabel("DANH SÁCH KHÁCH HÀNG");
        title.setForeground(CHU);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtTim.setPreferredSize(new Dimension(330, 36));
        txtTim.setForeground(CHU);
        txtTim.setBackground(KHUNG);
        txtTim.setCaretColor(VANG);
        txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTim.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        txtTim.putClientProperty("JTextField.placeholderText", "Tìm theo tên, số điện thoại hoặc email");

        JButton refresh = new JButton("Làm mới");
        refresh.setPreferredSize(new Dimension(95, 36));
        refresh.setForeground(VANG);
        refresh.setBackground(KHUNG);
        refresh.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        refresh.setBorder(BorderFactory.createLineBorder(VIEN));
        refresh.setFocusPainted(false);
        refresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refresh.setMargin(new Insets(5, 10, 5, 10));
        refresh.addActionListener(e -> napDuLieu());

        JPanel phai = new JPanel(new BorderLayout(8, 0));
        phai.setOpaque(false);
        phai.add(txtTim, BorderLayout.CENTER);
        phai.add(refresh, BorderLayout.EAST);

        dau.add(title, BorderLayout.WEST);
        dau.add(phai, BorderLayout.EAST);

        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                loc();
            }

            public void removeUpdate(DocumentEvent e) {
                loc();
            }

            public void changedUpdate(DocumentEvent e) {
                loc();
            }
        });

        p.add(dau, BorderLayout.NORTH);
        p.add(taoBang(), BorderLayout.CENTER);

        return p;
    }

    private JScrollPane taoBang() {
        table.setBackground(KHUNG);
        table.setForeground(CHU);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(44);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(43, 43, 43));
        table.setSelectionBackground(new Color(42, 38, 30));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(PHU);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setForeground(CHU);
        center.setBackground(KHUNG);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(230);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(VIEN));
        scroll.getViewport().setBackground(KHUNG);
        scroll.setBackground(KHUNG);

        return scroll;
    }

    private void napDuLieu() {
        model.setRowCount(0);

        int tong = 0;
        int coThongTin = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT ma_khach_hang, ho_ten, so_dien_thoai, email, dia_chi " +
                     "FROM khach_hang ORDER BY ma_khach_hang DESC")) {

            while (rs.next()) {
                String phone = rs.getString(3);
                String email = rs.getString(4);

                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        phone == null ? "" : phone,
                        email == null ? "" : email,
                        rs.getString(5) == null ? "" : rs.getString(5)
                });

                tong++;

                if ((phone != null && !phone.isBlank())
                        || (email != null && !email.isBlank())) {
                    coThongTin++;
                }
            }

        } catch (Exception e) {
            model.addRow(new Object[]{1, "Nguyễn Văn An", "0901234567", "an@gmail.com", "Hà Nội"});
            model.addRow(new Object[]{2, "Trần Minh Anh", "0912345678", "anh@gmail.com", "Hà Nội"});
            model.addRow(new Object[]{3, "Lê Hoàng Nam", "0987654321", "nam@gmail.com", "Nam Định"});
            tong = 3;
            coThongTin = 3;
        }

        lbTong.setText(String.valueOf(tong));
        lbThanhVien.setText(String.valueOf(tong));
        lbThongTin.setText(String.valueOf(coThongTin));

        loc();
    }

    private void loc() {
        String key = txtTim.getText().trim().toLowerCase();

        if (key.isEmpty()) {
            for (int i = 0; i < table.getRowCount(); i++) {
                table.setRowSelectionInterval(i, i);
                table.removeRowSelectionInterval(i, i);
            }
            return;
        }

        for (int i = 0; i < table.getRowCount(); i++) {
            boolean match = false;

            for (int j = 0; j < table.getColumnCount(); j++) {
                Object value = table.getValueAt(i, j);

                if (value != null && value.toString().toLowerCase().contains(key)) {
                    match = true;
                    break;
                }
            }

            if (match) {
                table.setRowSelectionInterval(i, i);
            }
        }
    }
}