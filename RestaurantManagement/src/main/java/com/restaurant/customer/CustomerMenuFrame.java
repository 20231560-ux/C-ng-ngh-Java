package com.restaurant.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerMenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Color CREAM = new Color(248, 244, 235);
    private final Color WHITE = new Color(255, 253, 248);
    private final Color DARK = new Color(47, 39, 33);
    private final Color GOLD = new Color(178, 135, 75);
    private final Color GOLD_DARK = new Color(139, 98, 48);
    private final Color TEXT_LIGHT = new Color(125, 107, 87);
    private final Color BORDER = new Color(226, 215, 197);
    private String maBan = "B08";
    private int soLuongGioHang = 0;
    private final List<CustomerCartFrame.MonGioHang> gioHang = new ArrayList<>();
    private JPanel danhSachMon;
    private JTextField txtTimKiem;
    private JButton lblGioHang;
    private String danhMucHienTai = "TẤT CẢ";
    private final List<MonAn> danhSach = new ArrayList<>();

    public CustomerMenuFrame() {
        this("B08");
    }

    public CustomerMenuFrame(String maBan) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan.trim();
        taoDuLieu();
        khoiTao();
    }

    private void taoDuLieu() {
        danhSach.add(new MonAn(1, "Bò lúc lắc Savoré", "189.000đ", "4.9", "Bò mềm thơm kết hợp rau củ và sốt đặc biệt.", "MÓN CHÍNH", 0));
        danhSach.add(new MonAn(2, "Mỳ Ý sốt bò", "129.000đ", "4.8", "Mỳ Ý kết hợp sốt bò đậm đà và phô mai.", "MÓN CHÍNH", 1));
        danhSach.add(new MonAn(3, "Salad cá hồi", "149.000đ", "4.9", "Cá hồi áp chảo dùng cùng rau xanh và sốt.", "KHAI VỊ", 2));
        danhSach.add(new MonAn(4, "Gà nướng thảo mộc", "159.000đ", "4.8", "Thịt gà nướng vàng thơm cùng thảo mộc.", "MÓN CHÍNH", 3));
        danhSach.add(new MonAn(5, "Trà đào Savoré", "59.000đ", "4.7", "Trà đào thanh mát, phù hợp dùng cùng món chính.", "ĐỒ UỐNG", 4));
        danhSach.add(new MonAn(6, "Tiramisu", "79.000đ", "4.9", "Tiramisu mềm mịn với vị cà phê và cacao.", "TRÁNG MIỆNG", 5));
    }

    private void khoiTao() {
        setTitle("Savoré - Thực đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430, 820);
        setMinimumSize(new Dimension(430, 820));
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);
        setContentPane(root);
        root.add(taoHeader(), BorderLayout.NORTH);
        root.add(taoNoiDung(), BorderLayout.CENTER);
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(15, 22, 13, 22)
        ));
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("SAVORÉ");
        logo.setForeground(DARK);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel ban = new JLabel("BÀN " + maBan);
        ban.setForeground(TEXT_LIGHT);
        ban.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        logoPanel.add(logo);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(ban);
        JButton gioHangButton = new JButton("GIỎ  " + soLuongGioHang);
        gioHangButton.setForeground(DARK);
        gioHangButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gioHangButton.setFocusPainted(false);
        gioHangButton.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));
        gioHangButton.setContentAreaFilled(false);
        gioHangButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gioHangButton.addActionListener(e -> moGioHang());
        lblGioHang = gioHangButton;
        header.add(logoPanel, BorderLayout.WEST);
        header.add(gioHangButton, BorderLayout.EAST);
        return header;
    }

    private JPanel taoNoiDung() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CREAM);
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(12, 22, 6, 22));
        JLabel title = new JLabel("Bạn muốn dùng món gì?");
        title.setForeground(DARK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setAlignmentX(LEFT_ALIGNMENT);
        top.add(title);
        top.add(Box.createVerticalStrut(10));
        JPanel search = taoTimKiem();
        search.setAlignmentX(LEFT_ALIGNMENT);
        top.add(search);
        top.add(Box.createVerticalStrut(10));
        JPanel categories = taoDanhMuc();
        categories.setAlignmentX(LEFT_ALIGNMENT);
        top.add(categories);
        content.add(top, BorderLayout.NORTH);
        danhSachMon = new JPanel();
        danhSachMon.setOpaque(false);
        danhSachMon.setLayout(new BoxLayout(danhSachMon, BoxLayout.Y_AXIS));
        danhSachMon.setBorder(new EmptyBorder(8, 22, 25, 22));
        hienThiMon();
        JScrollPane scroll = new JScrollPane(danhSachMon);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        content.add(scroll, BorderLayout.CENTER);
        return content;
    }

    private JPanel taoTimKiem() {
        RoundedPanel panel = new RoundedPanel(15, WHITE);
        panel.setLayout(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(386, 48));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));
        JLabel icon = new JLabel("TÌM");
        icon.setForeground(GOLD_DARK);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 9));
        txtTimKiem = new JTextField();
        txtTimKiem.setBorder(null);
        txtTimKiem.setOpaque(false);
        txtTimKiem.setForeground(DARK);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hienThiMon(); }
            public void removeUpdate(DocumentEvent e) { hienThiMon(); }
            public void changedUpdate(DocumentEvent e) { hienThiMon(); }
        });
        panel.add(icon, BorderLayout.WEST);
        panel.add(txtTimKiem, BorderLayout.CENTER);
        return panel;
    }

    private JPanel taoDanhMuc() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 6, 6));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(386, 58));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        String[] names = {"TẤT CẢ", "KHAI VỊ", "MÓN CHÍNH", "ĐỒ UỐNG", "TRÁNG MIỆNG"};
        for (String name : names) {
            panel.add(taoNutDanhMuc(name));
        }
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        panel.add(empty);
        return panel;
    }

    private JButton taoNutDanhMuc(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 9));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (text.equals(danhMucHienTai)) {
            button.setBackground(GOLD);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(237, 230, 218));
            button.setForeground(TEXT_LIGHT);
        }
        button.addActionListener(e -> {
            danhMucHienTai = text;
            hienThiMon();
        });
        return button;
    }

    private void hienThiMon() {
        if (danhSachMon == null) return;
        danhSachMon.removeAll();
        String tuKhoa = txtTimKiem == null ? "" : txtTimKiem.getText().trim().toLowerCase();
        for (MonAn mon : danhSach) {
            boolean dungDanhMuc = "TẤT CẢ".equals(danhMucHienTai) || mon.danhMuc.equals(danhMucHienTai);
            boolean dungTimKiem = tuKhoa.isEmpty() || mon.ten.toLowerCase().contains(tuKhoa) || mon.moTa.toLowerCase().contains(tuKhoa);
            if (dungDanhMuc && dungTimKiem) {
                JPanel card = taoCardMon(mon);
                card.setAlignmentX(LEFT_ALIGNMENT);
                danhSachMon.add(card);
                danhSachMon.add(Box.createVerticalStrut(12));
            }
        }
        if (danhSachMon.getComponentCount() == 0) {
            JLabel empty = new JLabel("Không tìm thấy món phù hợp");
            empty.setForeground(TEXT_LIGHT);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            empty.setAlignmentX(CENTER_ALIGNMENT);
            danhSachMon.add(Box.createVerticalStrut(30));
            danhSachMon.add(empty);
        }
        danhSachMon.revalidate();
        danhSachMon.repaint();
    }

    private JPanel taoCardMon(MonAn mon) {
        RoundedPanel card = new RoundedPanel(20, WHITE);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(386, 142));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 142));
        card.setMinimumSize(new Dimension(360, 142));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        FoodImagePanel image = new FoodImagePanel(mon.hinh);
        image.setPreferredSize(new Dimension(122, 122));
        JPanel right = new JPanel(new BorderLayout(0, 3));
        right.setOpaque(false);
        JPanel upper = new JPanel(new BorderLayout());
        upper.setOpaque(false);
        JLabel category = new JLabel(mon.danhMuc);
        category.setForeground(GOLD_DARK);
        category.setFont(new Font("Segoe UI", Font.BOLD, 8));
        JLabel rating = new JLabel("★ " + mon.rating);
        rating.setForeground(new Color(163, 119, 59));
        rating.setFont(new Font("Segoe UI", Font.BOLD, 9));
        upper.add(category, BorderLayout.WEST);
        upper.add(rating, BorderLayout.EAST);
        JLabel name = new JLabel(mon.ten);
        name.setForeground(DARK);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel description = new JLabel("<html><div style='width:170px;'>" + mon.moTa + "</div></html>");
        description.setForeground(TEXT_LIGHT);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        JLabel price = new JLabel(mon.gia);
        price.setForeground(DARK);
        price.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JButton add = new JButton("+");
        add.setPreferredSize(new Dimension(34, 34));
        add.setFocusPainted(false);
        add.setBorderPainted(false);
        add.setForeground(Color.WHITE);
        add.setBackground(GOLD);
        add.setFont(new Font("Segoe UI", Font.BOLD, 19));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.addActionListener(e -> themVaoGio(mon));
        bottom.add(price, BorderLayout.WEST);
        bottom.add(add, BorderLayout.EAST);
        right.add(upper, BorderLayout.NORTH);
        right.add(name, BorderLayout.CENTER);
        JPanel lower = new JPanel(new BorderLayout());
        lower.setOpaque(false);
        lower.add(description, BorderLayout.NORTH);
        lower.add(bottom, BorderLayout.SOUTH);
        right.add(lower, BorderLayout.SOUTH);
        card.add(image, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        MouseAdapter mouse = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(251, 247, 239));
                card.repaint();
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(WHITE);
                card.repaint();
            }
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() != add) moChiTiet(mon);
            }
        };
        card.addMouseListener(mouse);
        image.addMouseListener(mouse);
        name.addMouseListener(mouse);
        description.addMouseListener(mouse);
        return card;
    }

    private void themVaoGio(MonAn mon) {
        for (CustomerCartFrame.MonGioHang item : gioHang) {
            if (item.getMaMon() == mon.maMon) {
                item.setSoLuong(item.getSoLuong() + 1);
                soLuongGioHang++;
                capNhatGioHang();
                return;
            }
        }
        gioHang.add(new CustomerCartFrame.MonGioHang(mon.maMon, mon.ten, parseGia(mon.gia), 1, mon.danhMuc, mon.hinh));
        soLuongGioHang++;
        capNhatGioHang();
    }

    private void capNhatGioHang() {
        if (lblGioHang != null) lblGioHang.setText("GIỎ  " + soLuongGioHang);
    }

    private int parseGia(String gia) {
        if (gia == null || gia.trim().isEmpty()) return 0;
        return Integer.parseInt(gia.replace(".", "").replace("đ", "").trim());
    }

    private void moChiTiet(MonAn mon) {
        System.out.println("Chọn món: " + mon.ten);
    }

    private void moGioHang() {
        CustomerCartFrame cartFrame = new CustomerCartFrame(maBan, gioHang);
        cartFrame.setLocationRelativeTo(this);
        cartFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                soLuongGioHang = 0;
                for (CustomerCartFrame.MonGioHang item : gioHang) soLuongGioHang += item.getSoLuong();
                capNhatGioHang();
            }
        });
        cartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerMenuFrame("B08").setVisible(true));
    }

    private static class MonAn {
        int maMon;
        String ten;
        String gia;
        String rating;
        String moTa;
        String danhMuc;
        int hinh;
        MonAn(int maMon, String ten, String gia, String rating, String moTa, String danhMuc, int hinh) {
            this.maMon = maMon;
            this.ten = ten;
            this.gia = gia;
            this.rating = rating;
            this.moTa = moTa;
            this.danhMuc = danhMuc;
            this.hinh = hinh;
        }
    }

    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int radius;
        private final Color background;
        RoundedPanel(int radius, Color background) {
            this.radius = radius;
            this.background = background;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class FoodImagePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int type;
        FoodImagePanel(int type) {
            this.type = type;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gradient;
            if (type == 0) gradient = new GradientPaint(0, 0, new Color(91, 65, 44), w, h, new Color(194, 146, 78));
            else if (type == 1) gradient = new GradientPaint(0, 0, new Color(115, 66, 41), w, h, new Color(210, 147, 83));
            else if (type == 2) gradient = new GradientPaint(0, 0, new Color(81, 103, 66), w, h, new Color(178, 177, 111));
            else if (type == 3) gradient = new GradientPaint(0, 0, new Color(114, 67, 42), w, h, new Color(201, 133, 65));
            else if (type == 4) gradient = new GradientPaint(0, 0, new Color(86, 111, 102), w, h, new Color(184, 157, 112));
            else gradient = new GradientPaint(0, 0, new Color(82, 58, 51), w, h, new Color(178, 126, 87));
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h, 18, 18);
            int cx = w / 2;
            int cy = h / 2;
            g2.setColor(new Color(255, 249, 229, 235));
            g2.fillOval(cx - 43, cy - 43, 86, 86);
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillOval(cx - 33, cy - 33, 66, 66);
            if (type == 0 || type == 3) {
                g2.setColor(new Color(130, 71, 35));
                g2.fillOval(cx - 25, cy - 23, 50, 46);
                g2.setColor(new Color(211, 126, 50));
                g2.fillOval(cx - 18, cy - 16, 36, 32);
                g2.setColor(new Color(229, 171, 78));
                g2.fillOval(cx - 8, cy - 7, 16, 14);
            } else if (type == 1) {
                g2.setColor(new Color(206, 148, 83));
                g2.fillOval(cx - 27, cy - 20, 54, 40);
                g2.setColor(new Color(116, 63, 38));
                g2.fillOval(cx - 17, cy - 14, 34, 28);
            } else if (type == 2) {
                g2.setColor(new Color(235, 165, 108));
                g2.fillOval(cx - 27, cy - 19, 54, 38);
                g2.setColor(new Color(222, 115, 76));
                g2.fillOval(cx - 18, cy - 14, 36, 28);
                g2.setColor(new Color(81, 115, 65));
                g2.fillOval(cx - 39, cy - 7, 18, 13);
                g2.fillOval(cx + 21, cy - 11, 19, 14);
            } else if (type == 4) {
                g2.setColor(new Color(226, 165, 93));
                g2.fillRoundRect(cx - 17, cy - 25, 34, 50, 9, 9);
                g2.setColor(new Color(244, 213, 142));
                g2.fillOval(cx - 14, cy - 19, 28, 23);
                g2.setColor(new Color(75, 103, 85));
                g2.fillRect(cx - 15, cy + 9, 30, 14);
            } else {
                g2.setColor(new Color(96, 60, 47));
                g2.fillRoundRect(cx - 29, cy - 25, 58, 50, 10, 10);
                g2.setColor(new Color(219, 173, 126));
                g2.fillRect(cx - 23, cy - 19, 46, 11);
                g2.setColor(new Color(245, 221, 188));
                g2.fillRect(cx - 23, cy - 2, 46, 11);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
