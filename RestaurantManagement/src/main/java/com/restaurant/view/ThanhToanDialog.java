package com.restaurant.view;

import com.restaurant.dao.KhuyenMaiDAO;
import com.restaurant.dao.ThanhToanDAO;
import com.restaurant.model.DonHang;
import com.restaurant.model.KhachHang;
import com.restaurant.model.KhuyenMai;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ThanhToanDialog extends JDialog {

    private final DonHang donHang;
    private final Runnable onThanhToanThanhCong;
    private final ThanhToanDAO thanhToanDAO = new ThanhToanDAO();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    private boolean isTienMat = true;
    private double tongTienGoc;
    private double tienGiam = 0;
    private double tongTienCanThu;

    private JTextField txtTienKhachDua;
    private JLabel lblTienThua;
    private JLabel lblTongTienHienThi;
    private JComboBox<KhuyenMaiItem> cbKhuyenMai;
    private JPanel panelChuyenKhoan;
    private JPanel panelTienMat;
    private GiaoPos.NutPos btnTabTienMat;
    private GiaoPos.NutPos btnTabChuyenKhoan;

    public ThanhToanDialog(Window parent, DonHang donHang, Runnable onThanhToanThanhCong) {
        super(parent, "THANH TOÁN HÓA ĐƠN — " + donHang.getMaDon(), ModalityType.APPLICATION_MODAL);
        this.donHang = donHang;
        this.onThanhToanThanhCong = onThanhToanThanhCong;
        this.tongTienGoc = donHang.getTongTien() > 0 ? donHang.getTongTien() : donHang.getTienTamTinh();
        this.tienGiam = donHang.getTienGiam();
        this.tongTienCanThu = Math.max(0, tongTienGoc - tienGiam);

        setSize(780, 680);
        setLocationRelativeTo(parent);
        setResizable(false);
        dungGiaoDien();
    }

    private void dungGiaoDien() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(GiaoPos.NEN_TRANG_NGA);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));
        setContentPane(root);

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("THANH TOÁN ĐƠN HÀNG");
        lblTitle.setFont(GiaoPos.f(22, Font.BOLD));
        lblTitle.setForeground(GiaoPos.DONG_DAM);

        JLabel lblSub = new JLabel("Bàn: " + donHang.getTenBan() + "  •  Mã đơn: " + donHang.getMaDon());
        lblSub.setFont(GiaoPos.f(14, Font.PLAIN));
        lblSub.setForeground(GiaoPos.CHU_PHU);

        JPanel pnlTitles = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTitles.setOpaque(false);
        pnlTitles.add(lblTitle);
        pnlTitles.add(lblSub);
        pnlHeader.add(pnlTitles, BorderLayout.WEST);

        // Tổng tiền cần thu nổi bật ở header
        JPanel pnlAmount = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlAmount.setOpaque(false);
        JLabel lblLblTong = new JLabel("TỔNG TIỀN PHẢI THU", SwingConstants.RIGHT);
        lblLblTong.setFont(GiaoPos.f(11, Font.BOLD));
        lblLblTong.setForeground(GiaoPos.CHU_MO);

        lblTongTienHienThi = new JLabel(GiaoPos.formatTien(tongTienCanThu), SwingConstants.RIGHT);
        lblTongTienHienThi.setFont(GiaoPos.f(26, Font.BOLD));
        lblTongTienHienThi.setForeground(GiaoPos.DONG_CHINH);

        pnlAmount.add(lblLblTong);
        pnlAmount.add(lblTongTienHienThi);
        pnlHeader.add(pnlAmount, BorderLayout.EAST);

        root.add(pnlHeader, BorderLayout.NORTH);

        // Center Panel: Tabs phương thức + Khuyến mãi + Form tính tiền
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setOpaque(false);

        // Thanh chọn phương thức (Tabs)
        JPanel pnlTabs = new JPanel(new GridLayout(1, 2, 12, 0));
        pnlTabs.setOpaque(false);
        pnlTabs.setPreferredSize(new Dimension(100, 48));

        btnTabTienMat = new GiaoPos.NutPos("💵  TIỀN MẶT", GiaoPos.NutPos.STYLE_TAB);
        btnTabChuyenKhoan = new GiaoPos.NutPos("📱  CHUYỂN KHOẢN (VIETQR)", GiaoPos.NutPos.STYLE_TAB);
        btnTabTienMat.setActive(true);

        btnTabTienMat.addActionListener(e -> doiPhuongThuc(true));
        btnTabChuyenKhoan.addActionListener(e -> doiPhuongThuc(false));

        pnlTabs.add(btnTabTienMat);
        pnlTabs.add(btnTabChuyenKhoan);
        center.add(pnlTabs, BorderLayout.NORTH);

        // Panel thông tin khuyến mãi & nội dung phương thức
        JPanel pnlBody = new JPanel(new BorderLayout(0, 12));
        pnlBody.setOpaque(false);

        // Khuyến mãi selection
        GiaoPos.ThePos pnlKM = new GiaoPos.ThePos(new BorderLayout(12, 0));
        pnlKM.setPreferredSize(new Dimension(100, 56));
        pnlKM.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblKM = new JLabel("Mã khuyến mãi / Giảm giá:");
        lblKM.setFont(GiaoPos.f(13, Font.BOLD));
        lblKM.setForeground(GiaoPos.CHU_CHINH);
        pnlKM.add(lblKM, BorderLayout.WEST);

        cbKhuyenMai = new JComboBox<>();
        cbKhuyenMai.setFont(GiaoPos.f(13, Font.PLAIN));
        napKhuyenMai();
        cbKhuyenMai.addActionListener(e -> xuLyChonKhuyenMai());
        pnlKM.add(cbKhuyenMai, BorderLayout.CENTER);

        pnlBody.add(pnlKM, BorderLayout.NORTH);

        // Card phương thức
        CardLayout cardLayout = new CardLayout();
        JPanel pnlCardMethod = new JPanel(cardLayout);
        pnlCardMethod.setOpaque(false);

        panelTienMat = taoPanelTienMat();
        panelChuyenKhoan = taoPanelChuyenKhoan();

        pnlCardMethod.add(panelTienMat, "TIEN_MAT");
        pnlCardMethod.add(panelChuyenKhoan, "CHUYEN_KHOAN");
        pnlBody.add(pnlCardMethod, BorderLayout.CENTER);

        center.add(pnlBody, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // Footer: Nút Hủy & Xác nhận thanh toán
        JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 14, 0));
        pnlFooter.setOpaque(false);
        pnlFooter.setPreferredSize(new Dimension(100, 52));

        GiaoPos.NutPos btnHuy = new GiaoPos.NutPos("ĐÓNG / QUAY LẠI", GiaoPos.NutPos.STYLE_TRANG);
        btnHuy.addActionListener(e -> dispose());

        GiaoPos.NutPos btnXacNhan = new GiaoPos.NutPos("✔  XÁC NHẬN THANH TOÁN & IN BILL", GiaoPos.NutPos.STYLE_DONG);
        btnXacNhan.setFont(GiaoPos.f(16, Font.BOLD));
        btnXacNhan.addActionListener(e -> thucHienThanhToan());

        pnlFooter.add(btnHuy);
        pnlFooter.add(btnXacNhan);
        root.add(pnlFooter, BorderLayout.SOUTH);
    }

    private void doiPhuongThuc(boolean tienMat) {
        isTienMat = tienMat;
        btnTabTienMat.setActive(tienMat);
        btnTabChuyenKhoan.setActive(!tienMat);
        CardLayout cl = (CardLayout) ((JPanel) panelTienMat.getParent()).getLayout();
        cl.show(panelTienMat.getParent(), tienMat ? "TIEN_MAT" : "CHUYEN_KHOAN");
    }

    private JPanel taoPanelTienMat() {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout(0, 12));

        JPanel pnlInputs = new JPanel(new GridLayout(2, 2, 14, 12));
        pnlInputs.setOpaque(false);

        JLabel lblDua = new JLabel("Tiền khách đưa (VNĐ):");
        lblDua.setFont(GiaoPos.f(13, Font.BOLD));
        lblDua.setForeground(GiaoPos.CHU_CHINH);

        txtTienKhachDua = new JTextField(String.valueOf((long) tongTienCanThu));
        txtTienKhachDua.setFont(GiaoPos.f(18, Font.BOLD));
        txtTienKhachDua.setForeground(GiaoPos.DONG_DAM);
        txtTienKhachDua.setHorizontalAlignment(JTextField.RIGHT);
        txtTienKhachDua.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoPos.DONG_CHINH, 2),
                new EmptyBorder(6, 12, 6, 12)
        ));

        txtTienKhachDua.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { tinhTienThua(); }
        });

        JLabel lblThuaTxt = new JLabel("Tiền thừa trả khách:");
        lblThuaTxt.setFont(GiaoPos.f(13, Font.BOLD));
        lblThuaTxt.setForeground(GiaoPos.CHU_CHINH);

        lblTienThua = new JLabel("0 đ", SwingConstants.RIGHT);
        lblTienThua.setFont(GiaoPos.f(22, Font.BOLD));
        lblTienThua.setForeground(GiaoPos.BAN_TRONG);

        pnlInputs.add(lblDua);
        pnlInputs.add(txtTienKhachDua);
        pnlInputs.add(lblThuaTxt);
        pnlInputs.add(lblTienThua);
        pnl.add(pnlInputs, BorderLayout.NORTH);

        // Các phím tiền nhanh cảm ứng lớn
        JPanel pnlQuick = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlQuick.setOpaque(false);
        pnlQuick.setBorder(new EmptyBorder(14, 0, 0, 0));

        double[] quickAmounts = {tongTienCanThu, 50000, 100000, 200000, 300000, 500000, 1000000, 2000000};
        String[] quickLabels = {"ĐỦ TIỀN", "50.000", "100.000", "200.000", "300.000", "500.000", "1.000.000", "2.000.000"};

        for (int i = 0; i < quickAmounts.length; i++) {
            final double amt = quickAmounts[i];
            GiaoPos.NutPos btn = new GiaoPos.NutPos(quickLabels[i],
                    i == 0 ? GiaoPos.NutPos.STYLE_DONG_PHU : GiaoPos.NutPos.STYLE_TRANG);
            btn.setFont(GiaoPos.f(13, Font.BOLD));
            btn.addActionListener(e -> {
                txtTienKhachDua.setText(String.valueOf((long) amt));
                tinhTienThua();
            });
            pnlQuick.add(btn);
        }

        pnl.add(pnlQuick, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoPanelChuyenKhoan() {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout(18, 0));

        // QR Code Canvas giả lập VietQR
        JPanel qrCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Nền QR trắng
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(8, 8, w - 16, h - 16, 12, 12);
                g2.setColor(GiaoPos.THE_VIEN);
                g2.drawRoundRect(8, 8, w - 16, h - 16, 12, 12);

                // Mô phỏng hoa văn mã QR
                g2.setColor(new Color(0x1B, 0x2A, 0x4A));
                int size = Math.min(w, h) - 40;
                int ox = (w - size) / 2;
                int oy = (h - size) / 2;

                // 3 ô vuông định vị góc
                veGocQR(g2, ox + 6, oy + 6, 38);
                veGocQR(g2, ox + size - 44, oy + 6, 38);
                veGocQR(g2, ox + 6, oy + size - 44, 38);

                // Các pixel ngẫu nhiên mô phỏng mã QR
                java.util.Random rnd = new java.util.Random(donHang.getMaDon().hashCode());
                int step = 8;
                for (int x = ox + 48; x < ox + size - 48; x += step) {
                    for (int y = oy + 6; y < oy + size - 6; y += step) {
                        if (rnd.nextBoolean()) {
                            g2.fillRect(x, y, step - 1, step - 1);
                        }
                    }
                }
                for (int x = ox + 6; x < ox + size - 6; x += step) {
                    for (int y = oy + 48; y < oy + size - 48; y += step) {
                        if (rnd.nextBoolean()) {
                            g2.fillRect(x, y, step - 1, step - 1);
                        }
                    }
                }

                // Logo ở giữa
                int midX = ox + size / 2;
                int midY = oy + size / 2;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(midX - 22, midY - 14, 44, 28, 8, 8);
                g2.setColor(GiaoPos.DONG_CHINH);
                g2.drawRoundRect(midX - 22, midY - 14, 44, 28, 8, 8);
                g2.setFont(GiaoPos.f(10, Font.BOLD));
                g2.drawString("VietQR", midX - 18, midY + 4);

                g2.dispose();
            }

            private void veGocQR(Graphics2D g2, int x, int y, int s) {
                g2.fillRect(x, y, s, s);
                g2.setColor(Color.WHITE);
                g2.fillRect(x + 5, y + 5, s - 10, s - 10);
                g2.setColor(new Color(0x1B, 0x2A, 0x4A));
                g2.fillRect(x + 9, y + 9, s - 18, s - 18);
            }
        };
        qrCanvas.setPreferredSize(new Dimension(220, 220));
        pnl.add(qrCanvas, BorderLayout.WEST);

        // Thông tin tài khoản ngân hàng
        JPanel pnlBankInfo = new JPanel(new GridLayout(6, 1, 0, 4));
        pnlBankInfo.setOpaque(false);
        pnlBankInfo.setBorder(new EmptyBorder(8, 6, 8, 6));

        pnlBankInfo.add(taoDongInfo("Ngân hàng:", "MB BANK (Ngân hàng Quân Đội)"));
        pnlBankInfo.add(taoDongInfo("Số tài khoản:", "9999.8888.6666"));
        pnlBankInfo.add(taoDongInfo("Chủ tài khoản:", "NOVA RESTAURANT"));
        pnlBankInfo.add(taoDongInfo("Số tiền:", GiaoPos.formatTien(tongTienCanThu)));
        pnlBankInfo.add(taoDongInfo("Nội dung CK:", donHang.getMaDon() + " " + donHang.getTenBan()));

        JLabel lblGuide = new JLabel("<html><i>Mở ứng dụng ngân hàng hoặc ví điện tử bất kỳ (MoMo, ZaloPay, ViettelPay...) để quét mã thanh toán.</i></html>");
        lblGuide.setFont(GiaoPos.f(12, Font.PLAIN));
        lblGuide.setForeground(GiaoPos.CHU_PHU);
        pnlBankInfo.add(lblGuide);

        pnl.add(pnlBankInfo, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoDongInfo(String k, String v) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        JLabel lk = new JLabel(k);
        lk.setFont(GiaoPos.f(13, Font.BOLD));
        lk.setForeground(GiaoPos.CHU_PHU);
        lk.setPreferredSize(new Dimension(100, 20));

        JLabel lv = new JLabel(v);
        lv.setFont(GiaoPos.f(13, Font.BOLD));
        lv.setForeground(GiaoPos.DONG_DAM);

        p.add(lk, BorderLayout.WEST);
        p.add(lv, BorderLayout.CENTER);
        return p;
    }

    private void napKhuyenMai() {
        cbKhuyenMai.removeAllItems();
        cbKhuyenMai.addItem(new KhuyenMaiItem(null, "— Không áp dụng giảm giá —"));

        List<KhuyenMai> ds = khuyenMaiDAO.findAll();
        for (KhuyenMai km : ds) {
            cbKhuyenMai.addItem(new KhuyenMaiItem(km, km.getMaCode() + " - " + km.getTenKhuyenMai()
                    + " (Giảm " + GiaoPos.formatTien(km.getGiaTriGiam()) + ")"));
        }
    }

    private void xuLyChonKhuyenMai() {
        KhuyenMaiItem item = (KhuyenMaiItem) cbKhuyenMai.getSelectedItem();
        if (item != null && item.km != null) {
            tienGiam = item.km.getGiaTriGiam();
        } else {
            tienGiam = 0;
        }
        tongTienCanThu = Math.max(0, tongTienGoc - tienGiam);
        lblTongTienHienThi.setText(GiaoPos.formatTien(tongTienCanThu));
        tinhTienThua();
    }

    private void tinhTienThua() {
        try {
            String text = txtTienKhachDua.getText().replaceAll("[^0-9]", "");
            double dua = text.isEmpty() ? 0 : Double.parseDouble(text);
            double thua = dua - tongTienCanThu;
            if (thua >= 0) {
                lblTienThua.setText(GiaoPos.formatTien(thua));
                lblTienThua.setForeground(GiaoPos.BAN_TRONG);
            } else {
                lblTienThua.setText("Thiếu " + GiaoPos.formatTien(Math.abs(thua)));
                lblTienThua.setForeground(GiaoPos.BAN_CAN_THANH_TOAN);
            }
        } catch (Exception ignore) {
            lblTienThua.setText("0 đ");
        }
    }

    private void thucHienThanhToan() {
        String phuongThuc = isTienMat ? "TIEN_MAT" : "CHUYEN_KHOAN";
        String maGD = isTienMat ? "" : ("QR" + System.currentTimeMillis() % 1000000);

        if (isTienMat) {
            String text = txtTienKhachDua.getText().replaceAll("[^0-9]", "");
            double dua = text.isEmpty() ? 0 : Double.parseDouble(text);
            if (dua < tongTienCanThu) {
                JOptionPane.showMessageDialog(this,
                        "Số tiền khách đưa chưa đủ! Cần thêm " + GiaoPos.formatTien(tongTienCanThu - dua),
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtTienKhachDua.requestFocus();
                return;
            }
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán đơn " + donHang.getMaDon() + " bàn " + donHang.getTenBan()
                        + "\nSố tiền: " + GiaoPos.formatTien(tongTienCanThu)
                        + "\nPhương thức: " + (isTienMat ? "Tiền mặt" : "Chuyển khoản QR"),
                "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (xacNhan != JOptionPane.YES_OPTION) return;

        boolean thanhCong = thanhToanDAO.thucHienThanhToan(
                donHang.getMaDonHang(),
                donHang.getMaBan(),
                phuongThuc,
                tongTienCanThu,
                maGD,
                donHang.getMaKhachHang()
        );

        if (thanhCong) {
            hienThiHoaDonThanhCong();
            dispose();
            if (onThanhToanThanhCong != null) {
                onThanhToanThanhCong.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại! Vui lòng kiểm tra lại kết nối CSDL.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiHoaDonThanhCong() {
        String receipt = """
                ========================================
                         NOVA RESTAURANT POS
                     HÓA ĐƠN THANH TOÁN DỊCH VỤ
                ========================================
                Mã đơn: %s
                Bàn: %s
                Phương thức: %s
                Tạm tính: %s
                Giảm giá: %s
                ----------------------------------------
                TỔNG TIỀN: %s
                ----------------------------------------
                Cảm ơn Quý khách & Hẹn gặp lại!
                ========================================
                """.formatted(
                donHang.getMaDon(),
                donHang.getTenBan(),
                isTienMat ? "Tiền mặt" : "Chuyển khoản QR",
                GiaoPos.formatTien(tongTienGoc),
                GiaoPos.formatTien(tienGiam),
                GiaoPos.formatTien(tongTienCanThu)
        );

        JTextArea ta = new JTextArea(receipt);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(new Color(0xFFFFF8));

        JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                "Thanh toán thành công — Hóa đơn", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class KhuyenMaiItem {
        KhuyenMai km;
        String text;
        KhuyenMaiItem(KhuyenMai km, String text) {
            this.km = km;
            this.text = text;
        }
        @Override public String toString() { return text; }
    }
}
