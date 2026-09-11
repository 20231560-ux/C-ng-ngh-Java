package com.restaurant.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.model.DonHang;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class CustomerCartFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Color CREAM = new Color(248, 244, 235);
    private final Color WHITE = new Color(255, 253, 248);
    private final Color DARK = new Color(47, 39, 33);
    private final Color GOLD = new Color(178, 135, 75);
    private final Color GOLD_DARK = new Color(139, 98, 48);
    private final Color TEXT_LIGHT = new Color(125, 107, 87);
    private final Color BORDER = new Color(226, 215, 197);
    private String maBan = "B08";
    private final List<MonGioHang> gioHang = new ArrayList<>();
    private JPanel danhSachPanel;
    private JLabel lblTamTinh;
    private JLabel lblPhiDichVu;
    private JLabel lblTongCong;
    private JLabel lblSoMon;
    private JTextArea txtGhiChu;

    public CustomerCartFrame() {
        this("B08");
    }

    public CustomerCartFrame(String maBan) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan.trim();
        khoiTao();
    }

    public CustomerCartFrame(String maBan, List<MonGioHang> gioHangChuyen) {
        this.maBan = maBan == null || maBan.trim().isEmpty() ? "B08" : maBan.trim();
        if (gioHangChuyen != null) gioHang.addAll(gioHangChuyen);
        khoiTao();
    }

    private void khoiTao() {
        setTitle("Savoré - Giỏ hàng");
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
        root.add(taoBottom(), BorderLayout.SOUTH);
        capNhatTongTien();
    }

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(13, 18, 13, 18)
        ));
        JButton back = new JButton("QUAY LẠI");
        back.setForeground(DARK);
        back.setFont(new Font("Segoe UI", Font.BOLD, 10));
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 10));
        back.setContentAreaFilled(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> dispose());
        JLabel title = new JLabel("GIỎ HÀNG", SwingConstants.CENTER);
        title.setForeground(DARK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel ban = new JLabel("BÀN " + maBan, SwingConstants.RIGHT);
        ban.setForeground(TEXT_LIGHT);
        ban.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        ban.setPreferredSize(new Dimension(55, 25));
        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(ban, BorderLayout.EAST);
        return header;
    }

    private JPanel taoNoiDung() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CREAM);
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(14, 22, 18, 22));
        JPanel intro = new JPanel(new BorderLayout());
        intro.setOpaque(false);
        intro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        intro.setAlignmentX(LEFT_ALIGNMENT);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Món đã chọn");
        title.setForeground(DARK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblSoMon = new JLabel();
        lblSoMon.setForeground(TEXT_LIGHT);
        lblSoMon.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(lblSoMon);
        intro.add(textPanel, BorderLayout.WEST);
        body.add(intro);
        body.add(Box.createVerticalStrut(10));
        danhSachPanel = new JPanel();
        danhSachPanel.setOpaque(false);
        danhSachPanel.setLayout(new BoxLayout(danhSachPanel, BoxLayout.Y_AXIS));
        danhSachPanel.setAlignmentX(LEFT_ALIGNMENT);
        hienThiGioHang();
        JScrollPane scroll = new JScrollPane(danhSachPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        body.add(scroll);
        body.add(Box.createVerticalStrut(10));
        JLabel noteTitle = new JLabel("GHI CHÚ CHO ĐƠN");
        noteTitle.setForeground(DARK);
        noteTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        noteTitle.setAlignmentX(LEFT_ALIGNMENT);
        body.add(noteTitle);
        body.add(Box.createVerticalStrut(5));
        txtGhiChu = new JTextArea();
        txtGhiChu.setRows(3);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setForeground(DARK);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtGhiChu.setBackground(WHITE);
        txtGhiChu.setBorder(BorderFactory.createEmptyBorder(9, 11, 9, 11));
        JScrollPane noteScroll = new JScrollPane(txtGhiChu);
        noteScroll.setAlignmentX(LEFT_ALIGNMENT);
        noteScroll.setPreferredSize(new Dimension(386, 62));
        noteScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        noteScroll.setMinimumSize(new Dimension(360, 62));
        noteScroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        noteScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        noteScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        body.add(noteScroll);
        content.add(body, BorderLayout.CENTER);
        return content;
    }

    private void hienThiGioHang() {
        danhSachPanel.removeAll();
        if (gioHang.isEmpty()) {
            JLabel empty = new JLabel("Chưa có món nào trong giỏ");
            empty.setForeground(TEXT_LIGHT);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            empty.setAlignmentX(LEFT_ALIGNMENT);
            danhSachPanel.add(Box.createVerticalStrut(18));
            danhSachPanel.add(empty);
        } else {
            for (int i = 0; i < gioHang.size(); i++) {
                MonGioHang mon = gioHang.get(i);
                JPanel card = taoCardMon(mon, i);
                card.setAlignmentX(LEFT_ALIGNMENT);
                danhSachPanel.add(card);
                if (i < gioHang.size() - 1) danhSachPanel.add(Box.createVerticalStrut(10));
            }
        }
        danhSachPanel.revalidate();
        danhSachPanel.repaint();
        capNhatSoMon();
    }

    private JPanel taoCardMon(MonGioHang mon, int index) {
        RoundedPanel card = new RoundedPanel(18, WHITE);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(386, 108));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        card.setMinimumSize(new Dimension(360, 108));
        FoodImagePanel image = new FoodImagePanel(mon.getHinh());
        image.setPreferredSize(new Dimension(82, 82));
        JPanel center = new JPanel(new BorderLayout(0, 3));
        center.setOpaque(false);
        JLabel category = new JLabel(mon.getDanhMuc());
        category.setForeground(GOLD_DARK);
        category.setFont(new Font("Segoe UI", Font.BOLD, 8));
        JLabel name = new JLabel(mon.getTen());
        name.setForeground(DARK);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel price = new JLabel(dinhDangTien(mon.getGia()));
        price.setForeground(GOLD_DARK);
        price.setFont(new Font("Segoe UI", Font.BOLD, 11));
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(category);
        info.add(Box.createVerticalStrut(4));
        info.add(name);
        info.add(Box.createVerticalStrut(5));
        info.add(price);
        center.add(info, BorderLayout.CENTER);
        JPanel quantityPanel = new JPanel(new GridLayout(1, 3));
        quantityPanel.setOpaque(false);
        quantityPanel.setPreferredSize(new Dimension(88, 30));
        JButton minus = new JButton("-");
        minus.setFocusPainted(false);
        minus.setBorderPainted(false);
        minus.setBackground(new Color(238, 230, 216));
        minus.setForeground(DARK);
        minus.setFont(new Font("Segoe UI", Font.BOLD, 15));
        minus.addActionListener(e -> {
            if (mon.getSoLuong() > 1) {
                mon.setSoLuong(mon.getSoLuong() - 1);
                hienThiGioHang();
                capNhatTongTien();
            }
        });
        JLabel quantity = new JLabel(String.valueOf(mon.getSoLuong()), SwingConstants.CENTER);
        quantity.setForeground(DARK);
        quantity.setFont(new Font("Segoe UI", Font.BOLD, 11));
        JButton plus = new JButton("+");
        plus.setFocusPainted(false);
        plus.setBorderPainted(false);
        plus.setBackground(GOLD);
        plus.setForeground(Color.WHITE);
        plus.setFont(new Font("Segoe UI", Font.BOLD, 15));
        plus.addActionListener(e -> {
            if (mon.getSoLuong() < 99) {
                mon.setSoLuong(mon.getSoLuong() + 1);
                hienThiGioHang();
                capNhatTongTien();
            }
        });
        quantityPanel.add(minus);
        quantityPanel.add(quantity);
        quantityPanel.add(plus);
        JButton delete = new JButton("XÓA");
        delete.setForeground(TEXT_LIGHT);
        delete.setFont(new Font("Segoe UI", Font.BOLD, 8));
        delete.setFocusPainted(false);
        delete.setBorderPainted(false);
        delete.setContentAreaFilled(false);
        delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        delete.addActionListener(e -> {
            gioHang.remove(index);
            hienThiGioHang();
            capNhatTongTien();
        });
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(quantityPanel);
        right.add(Box.createVerticalStrut(4));
        right.add(delete);
        card.add(image, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel taoBottom() {
        JPanel bottom = new JPanel();
        bottom.setBackground(WHITE);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(10, 22, 14, 22)
        ));
        JPanel subtotal = taoDongTien("TẠM TÍNH", lblTamTinh = new JLabel());
        JPanel service = taoDongTien("PHÍ DỊCH VỤ", lblPhiDichVu = new JLabel());
        JPanel line = new JPanel();
        line.setBackground(BORDER);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(386, 1));
        JPanel total = taoDongTong();
        JButton order = new JButton("GỬI GỌI MÓN");
        order.setForeground(Color.WHITE);
        order.setBackground(GOLD);
        order.setFont(new Font("Segoe UI", Font.BOLD, 13));
        order.setFocusPainted(false);
        order.setBorderPainted(false);
        order.setPreferredSize(new Dimension(386, 50));
        order.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        order.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        order.addActionListener(e -> guiDon());
        bottom.add(subtotal);
        bottom.add(Box.createVerticalStrut(3));
        bottom.add(service);
        bottom.add(Box.createVerticalStrut(7));
        bottom.add(line);
        bottom.add(Box.createVerticalStrut(7));
        bottom.add(total);
        bottom.add(Box.createVerticalStrut(9));
        bottom.add(order);
        return bottom;
    }

    private JPanel taoDongTien(String title, JLabel value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel label = new JLabel(title);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        value.setForeground(DARK);
        value.setFont(new Font("Segoe UI", Font.BOLD, 10));
        panel.add(label, BorderLayout.WEST);
        panel.add(value, BorderLayout.EAST);
        return panel;
    }

    private JPanel taoDongTong() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel label = new JLabel("TỔNG CỘNG");
        label.setForeground(DARK);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTongCong = new JLabel();
        lblTongCong.setForeground(GOLD_DARK);
        lblTongCong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label, BorderLayout.WEST);
        panel.add(lblTongCong, BorderLayout.EAST);
        return panel;
    }

    private void capNhatSoMon() {
        int soMon = 0;
        for (MonGioHang mon : gioHang) soMon += mon.getSoLuong();
        if (lblSoMon != null) lblSoMon.setText(soMon + " món đã chọn");
    }

    private void capNhatTongTien() {
        int tamTinh = 0;
        for (MonGioHang mon : gioHang) tamTinh += mon.getGia() * mon.getSoLuong();
        int phiDichVu = tamTinh * 10 / 100;
        int tong = tamTinh + phiDichVu;
        if (lblTamTinh != null) lblTamTinh.setText(dinhDangTien(tamTinh));
        if (lblPhiDichVu != null) lblPhiDichVu.setText(dinhDangTien(phiDichVu));
        if (lblTongCong != null) lblTongCong.setText(dinhDangTien(tong));
        capNhatSoMon();
    }

    private void guiDon() {
        if (gioHang.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống.", "Savoré", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DonHangDAO dao = new DonHangDAO();
        int maBanId = dao.timMaBanTheoSo(maBan);

        if (maBanId <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy bàn " + maBan, "Savoré", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int tamTinh = 0;
        for (MonGioHang mon : gioHang) {
            tamTinh += mon.getGia() * mon.getSoLuong();
        }

        int phiDichVu = tamTinh * 10 / 100;
        int tongTien = tamTinh + phiDichVu;

        DonHang donHang = new DonHang();
        donHang.setMaBan(maBanId);
        donHang.setTrangThai("CHO_XU_LY");
        donHang.setTienTamTinh(tamTinh);
        donHang.setTienGiam(0);
        donHang.setTienThue(phiDichVu);
        donHang.setTongTien(tongTien);
        donHang.setGhiChu(txtGhiChu == null ? "" : txtGhiChu.getText().trim());

        boolean thanhCong = dao.taoDonHangVaChiTiet(donHang, gioHang);

        if (!thanhCong) {
            javax.swing.JOptionPane.showMessageDialog(this, "Không thể gửi gọi món. Vui lòng kiểm tra kết nối CSDL và mã món.", "Savoré", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Đã gửi gọi món thành công cho bàn " + maBan + ".\nMã đơn: " + donHang.getMaDon(), "Savoré", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        gioHang.clear();
        capNhatGiaoDienSauKhiGui();
    }

    private void capNhatGiaoDienSauKhiGui() {
        if (danhSachPanel != null) {
            danhSachPanel.removeAll();
            JLabel empty = new JLabel("Chưa có món trong giỏ", SwingConstants.CENTER);
            empty.setForeground(TEXT_LIGHT);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            danhSachPanel.add(empty);
            danhSachPanel.revalidate();
            danhSachPanel.repaint();
        }
        capNhatTongTien();
    }

    private String dinhDangTien(int tien) {
        return String.format("%,dđ", tien).replace(",", ".");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerCartFrame("B08").setVisible(true));
    }

    public static class MonGioHang {
        private final int maMon;
        private final String ten;
        private final int gia;
        private int soLuong;
        private final String danhMuc;
        private final int hinh;

        public MonGioHang(int maMon, String ten, int gia, int soLuong, String danhMuc, int hinh) {
            this.maMon = maMon;
            this.ten = ten;
            this.gia = gia;
            this.soLuong = soLuong;
            this.danhMuc = danhMuc;
            this.hinh = hinh;
        }

        public int getMaMon() { return maMon; }
        public String getTen() { return ten; }
        public int getGia() { return gia; }
        public int getSoLuong() { return soLuong; }
        public String getDanhMuc() { return danhMuc; }
        public int getHinh() { return hinh; }
        public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
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
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            Color start;
            Color end;
            if (type == 1) {
                start = new Color(86, 108, 91);
                end = new Color(182, 170, 119);
            } else if (type == 2) {
                start = new Color(82, 58, 51);
                end = new Color(174, 123, 88);
            } else {
                start = new Color(91, 65, 44);
                end = new Color(194, 146, 78);
            }
            g2.setPaint(new java.awt.GradientPaint(0, 0, start, w, h, end));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 18, 18);
            int cx = w / 2;
            int cy = h / 2;
            g2.setColor(new Color(255, 249, 229, 225));
            g2.fillOval(cx - 30, cy - 30, 60, 60);
            g2.setColor(new Color(255, 255, 255, 230));
            g2.fillOval(cx - 24, cy - 24, 48, 48);
            if (type == 1) {
                g2.setColor(new Color(225, 163, 92));
                g2.fillRoundRect(cx - 11, cy - 18, 22, 36, 5, 5);
                g2.setColor(new Color(75, 103, 85));
                g2.fillRect(cx - 10, cy + 7, 20, 10);
            } else if (type == 2) {
                g2.setColor(new Color(101, 63, 48));
                g2.fillRoundRect(cx - 18, cy - 16, 36, 32, 5, 5);
                g2.setColor(new Color(224, 176, 127));
                g2.fillRect(cx - 14, cy - 11, 28, 7);
                g2.setColor(new Color(245, 221, 188));
                g2.fillRect(cx - 14, cy + 1, 28, 7);
            } else {
                g2.setColor(new Color(130, 71, 35));
                g2.fillOval(cx - 18, cy - 17, 36, 34);
                g2.setColor(new Color(211, 126, 50));
                g2.fillOval(cx - 14, cy - 13, 28, 26);
                g2.setColor(new Color(229, 171, 78));
                g2.fillOval(cx - 6, cy - 6, 12, 12);
            }
            g2.dispose();
        }
    }
}
