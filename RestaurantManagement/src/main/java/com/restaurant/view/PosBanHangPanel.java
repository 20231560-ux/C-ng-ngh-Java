package com.restaurant.view;

import com.restaurant.dao.BanAnDAO;
import com.restaurant.dao.ChiTietDonHangDAO;
import com.restaurant.dao.DonHangDAO;
import com.restaurant.dao.MonAnDAO;
import com.restaurant.model.BanAn;
import com.restaurant.model.ChiTietDonHang;
import com.restaurant.model.DonHang;
import com.restaurant.model.MonAn;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

public class PosBanHangPanel extends JPanel {

    private final NguoiDung nhanVienHienTai;
    private final BanAnDAO banAnDAO = new BanAnDAO();
    private final MonAnDAO monAnDAO = new MonAnDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final ChiTietDonHangDAO chiTietDonHangDAO = new ChiTietDonHangDAO();

    // Dữ liệu bộ nhớ
    private final List<BanAn> dsBan = new ArrayList<>();
    private final List<MonAn> dsMon = new ArrayList<>();
    private final List<ChiTietDonHang> gioHang = new ArrayList<>();

    private BanAn banDangChon = null;
    private DonHang donHangHienTai = null;
    private String danhMucChon = "Tất cả";
    private String khuVucChon = "Tất cả";

    // UI Components
    private JPanel pnlDanhSachBan;
    private JPanel pnlKhuVucTabs;
    private JPanel pnlDanhMucTabs;
    private JPanel pnlLuoiMon;
    private GiaoPos.OTextPos txtTimMon;
    private DefaultTableModel cartTableModel;
    private JTable cartTable;

    // Billing summary labels
    private JLabel lblBanTitle;
    private JLabel lblMaDonTitle;
    private JLabel lblTamTinh;
    private JLabel lblVat;
    private JLabel lblGiamGia;
    private JLabel lblTongCong;
    private GiaoPos.NutPos btnThanhToan;

    public PosBanHangPanel(NguoiDung nhanVien) {
        this.nhanVienHienTai = nhanVien;
        setOpaque(false);
        setLayout(new BorderLayout(14, 0));
        setBorder(new EmptyBorder(12, 14, 14, 14));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // CỘT TRÁI: Sơ đồ bàn thu nhỏ / chuyển bàn nhanh (rộng 320px)
        add(taoCotBan(), BorderLayout.WEST);

        // CỘT GIỮA: Danh mục + Lưới món ăn thực đơn
        add(taoCotThucDon(), BorderLayout.CENTER);

        // CỘT PHẢI: Giỏ hàng / Chi tiết hóa đơn / Thanh toán (rộng 430px)
        add(taoCotHoaDon(), BorderLayout.EAST);
    }

    // ==========================================
    // 1. KHU VỰC BÀN NHÀ HÀNG
    // ==========================================
    private JPanel taoCotBan() {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout(0, 10));
        pnl.setPreferredSize(new Dimension(330, 100));

        // Header cột bàn
        JPanel pnlHead = new JPanel(new BorderLayout(0, 8));
        pnlHead.setOpaque(false);

        JLabel lblHead = new JLabel("SƠ ĐỒ BÀN NHÀ HÀNG");
        lblHead.setFont(GiaoPos.f(14, Font.BOLD));
        lblHead.setForeground(GiaoPos.DONG_DAM);
        pnlHead.add(lblHead, BorderLayout.NORTH);

        // Tabs khu vực
        pnlKhuVucTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlKhuVucTabs.setOpaque(false);
        pnlHead.add(pnlKhuVucTabs, BorderLayout.CENTER);

        pnl.add(pnlHead, BorderLayout.NORTH);

        // Danh sách bàn cuộn dạng lưới 2 cột
        pnlDanhSachBan = new JPanel(new GridLayout(0, 2, 8, 8));
        pnlDanhSachBan.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(pnlDanhSachBan, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(wrapper);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        pnl.add(sp, BorderLayout.CENTER);

        // Footer thao tác bàn: Đổi bàn, Gộp bàn, Làm mới
        JPanel pnlTableActions = new JPanel(new GridLayout(1, 3, 6, 0));
        pnlTableActions.setOpaque(false);
        pnlTableActions.setPreferredSize(new Dimension(100, 42));

        GiaoPos.NutPos btnChuyen = new GiaoPos.NutPos("Đổi bàn", GiaoPos.NutPos.STYLE_TRANG);
        btnChuyen.setFont(GiaoPos.f(12, Font.BOLD));
        btnChuyen.addActionListener(e -> xuLyChuyenBan());

        GiaoPos.NutPos btnGop = new GiaoPos.NutPos("Gộp bàn", GiaoPos.NutPos.STYLE_TRANG);
        btnGop.setFont(GiaoPos.f(12, Font.BOLD));
        btnGop.addActionListener(e -> xuLyGopBan());

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("Làm mới", GiaoPos.NutPos.STYLE_DONG_PHU);
        btnLamMoi.setFont(GiaoPos.f(12, Font.BOLD));
        btnLamMoi.addActionListener(e -> napDuLieuBan());

        pnlTableActions.add(btnChuyen);
        pnlTableActions.add(btnGop);
        pnlTableActions.add(btnLamMoi);
        pnl.add(pnlTableActions, BorderLayout.SOUTH);

        return pnl;
    }

    // ==========================================
    // 2. KHU VỰC DANH SÁCH MÓN ĂN
    // ==========================================
    private JPanel taoCotThucDon() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setOpaque(false);

        // Header thực đơn: Tabs nhóm món + Tìm kiếm
        JPanel pnlTop = new JPanel(new BorderLayout(12, 8));
        pnlTop.setOpaque(false);

        txtTimMon = new GiaoPos.OTextPos("Tìm nhanh món ăn theo tên hoặc mã…", true);
        txtTimMon.setPreferredSize(new Dimension(300, 44));
        txtTimMon.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { veLuoiMon(); }
        });
        pnlTop.add(txtTimMon, BorderLayout.NORTH);

        pnlDanhMucTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlDanhMucTabs.setOpaque(false);
        pnlTop.add(pnlDanhMucTabs, BorderLayout.SOUTH);

        pnl.add(pnlTop, BorderLayout.NORTH);

        // Lưới món ăn cảm ứng dạng Card
        pnlLuoiMon = new JPanel(new GridLayout(0, 3, 12, 12));
        pnlLuoiMon.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(pnlLuoiMon, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(wrapper);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(22);
        pnl.add(sp, BorderLayout.CENTER);

        return pnl;
    }

    // ==========================================
    // 3. KHU VỰC HÓA ĐƠN & THANH TOÁN
    // ==========================================
    private JPanel taoCotHoaDon() {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout(0, 10));
        pnl.setPreferredSize(new Dimension(430, 100));

        // Header hóa đơn
        JPanel pnlHead = new JPanel(new BorderLayout());
        pnlHead.setOpaque(false);

        lblBanTitle = new JLabel("CHƯA CHỌN BÀN");
        lblBanTitle.setFont(GiaoPos.f(18, Font.BOLD));
        lblBanTitle.setForeground(GiaoPos.DONG_DAM);

        lblMaDonTitle = new JLabel("Chọn bàn để bắt đầu gọi món");
        lblMaDonTitle.setFont(GiaoPos.f(12, Font.PLAIN));
        lblMaDonTitle.setForeground(GiaoPos.CHU_MO);

        JPanel pnlTitles = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitles.setOpaque(false);
        pnlTitles.add(lblBanTitle);
        pnlTitles.add(lblMaDonTitle);
        pnlHead.add(pnlTitles, BorderLayout.WEST);

        GiaoPos.NutPos btnXoaHet = new GiaoPos.NutPos("Xóa hết", GiaoPos.NutPos.STYLE_TRANG);
        btnXoaHet.setFont(GiaoPos.f(11, Font.BOLD));
        btnXoaHet.setPreferredSize(new Dimension(80, 34));
        btnXoaHet.addActionListener(e -> xuLyXoaHetMon());
        pnlHead.add(btnXoaHet, BorderLayout.EAST);

        pnl.add(pnlHead, BorderLayout.NORTH);

        // Bảng giỏ hàng món đã chọn
        String[] cols = {"Món ăn", "SL", "Đơn giá", "Thành tiền", ""};
        cartTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(40);
        cartTable.setFont(GiaoPos.f(13, Font.PLAIN));
        cartTable.getTableHeader().setFont(GiaoPos.f(12, Font.BOLD));
        cartTable.getTableHeader().setBackground(GiaoPos.DONG_NHAT);
        cartTable.getTableHeader().setForeground(GiaoPos.DONG_DAM);
        cartTable.setSelectionBackground(GiaoPos.DONG_NHAT);
        cartTable.setSelectionForeground(GiaoPos.CHU_CHINH);

        // Căn chỉnh cột
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(45);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(85);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(35);

        // Renderer nút xóa nhanh trên bảng
        cartTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("✕");
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setForeground(GiaoPos.BAN_CAN_THANH_TOAN);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            return btn;
        });

        cartTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int r = cartTable.rowAtPoint(e.getPoint());
                int c = cartTable.columnAtPoint(e.getPoint());
                if (r >= 0 && r < gioHang.size()) {
                    if (c == 4) {
                        xoaMonKhoiGio(r);
                    } else if (c == 1) {
                        suaSoLuongBangPopup(r);
                    }
                }
            }
        });

        JScrollPane spCart = new JScrollPane(cartTable);
        spCart.setBorder(BorderFactory.createLineBorder(GiaoPos.THE_VIEN, 1));
        spCart.getViewport().setBackground(Color.WHITE);
        pnl.add(spCart, BorderLayout.CENTER);

        // Panel Tổng tiền & Thanh toán (Footer)
        JPanel pnlDuoi = new JPanel(new BorderLayout(0, 10));
        pnlDuoi.setOpaque(false);

        JPanel pnlTong = new JPanel(new GridLayout(4, 2, 8, 4));
        pnlTong.setOpaque(false);
        pnlTong.setBorder(new EmptyBorder(8, 6, 8, 6));

        lblTamTinh = new JLabel("0 đ", SwingConstants.RIGHT);
        lblVat = new JLabel("0 đ", SwingConstants.RIGHT);
        lblGiamGia = new JLabel("0 đ", SwingConstants.RIGHT);
        lblTongCong = new JLabel("0 đ", SwingConstants.RIGHT);

        lblTamTinh.setFont(GiaoPos.f(13, Font.BOLD));
        lblVat.setFont(GiaoPos.f(12, Font.PLAIN));
        lblVat.setForeground(GiaoPos.CHU_PHU);
        lblGiamGia.setFont(GiaoPos.f(12, Font.PLAIN));
        lblGiamGia.setForeground(GiaoPos.BAN_CAN_THANH_TOAN);
        lblTongCong.setFont(GiaoPos.f(24, Font.BOLD));
        lblTongCong.setForeground(GiaoPos.DONG_CHINH);

        pnlTong.add(taoLabel("Tạm tính:")); pnlTong.add(lblTamTinh);
        pnlTong.add(taoLabel("Thuế VAT (8%):")); pnlTong.add(lblVat);
        pnlTong.add(taoLabel("Giảm giá:")); pnlTong.add(lblGiamGia);
        pnlTong.add(taoLabelBold("TỔNG CỘNG:")); pnlTong.add(lblTongCong);
        pnlDuoi.add(pnlTong, BorderLayout.NORTH);

        // Hàng nút Gửi bếp & In bill
        JPanel pnlActionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        pnlActionRow.setOpaque(false);
        pnlActionRow.setPreferredSize(new Dimension(100, 42));

        GiaoPos.NutPos btnGuiBep = new GiaoPos.NutPos("🍳 Gửi bếp / Lưu", GiaoPos.NutPos.STYLE_CAM);
        btnGuiBep.setFont(GiaoPos.f(13, Font.BOLD));
        btnGuiBep.addActionListener(e -> xuLyGuiBep());

        GiaoPos.NutPos btnInTam = new GiaoPos.NutPos("📄 In tạm tính", GiaoPos.NutPos.STYLE_TRANG);
        btnInTam.setFont(GiaoPos.f(13, Font.BOLD));
        btnInTam.addActionListener(e -> xuLyInTamTinh());

        pnlActionRow.add(btnGuiBep);
        pnlActionRow.add(btnInTam);
        pnlDuoi.add(pnlActionRow, BorderLayout.CENTER);

        // Nút THANH TOÁN siêu to cảm ứng
        btnThanhToan = new GiaoPos.NutPos("💳  THANH TOÁN (F9)", GiaoPos.NutPos.STYLE_DONG);
        btnThanhToan.setFont(GiaoPos.f(18, Font.BOLD));
        btnThanhToan.setPreferredSize(new Dimension(100, 56));
        btnThanhToan.addActionListener(e -> xuLyMoThanhToan());
        pnlDuoi.add(btnThanhToan, BorderLayout.SOUTH);

        pnl.add(pnlDuoi, BorderLayout.SOUTH);

        return pnl;
    }

    private JLabel taoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(GiaoPos.f(12, Font.PLAIN));
        l.setForeground(GiaoPos.CHU_PHU);
        return l;
    }

    private JLabel taoLabelBold(String text) {
        JLabel l = new JLabel(text);
        l.setFont(GiaoPos.f(14, Font.BOLD));
        l.setForeground(GiaoPos.DONG_DAM);
        return l;
    }

    // ==========================================
    // LOGIC DỮ LIỆU & RENDER GIAO DIỆN
    // ==========================================
    public void napDuLieu() {
        napDuLieuBan();
        napDuLieuMon();
    }

    private void napDuLieuBan() {
        dsBan.clear();
        dsBan.addAll(banAnDAO.layTatCa());
        veKhuVucTabs();
        veDanhSachBan();
    }

    private void napDuLieuMon() {
        dsMon.clear();
        dsMon.addAll(monAnDAO.layTatCa());
        veDanhMucTabs();
        veLuoiMon();
    }

    private void veKhuVucTabs() {
        pnlKhuVucTabs.removeAll();
        LinkedHashSet<String> khuVucs = new LinkedHashSet<>();
        khuVucs.add("Tất cả");
        for (BanAn b : dsBan) {
            if (b.getKhuVuc() != null && !b.getKhuVuc().trim().isEmpty()) {
                khuVucs.add(b.getKhuVuc().trim());
            }
        }

        for (String kv : khuVucs) {
            GiaoPos.NutPos btn = new GiaoPos.NutPos(kv, GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 32));
            btn.setActive(kv.equals(khuVucChon));
            btn.addActionListener(e -> {
                khuVucChon = kv;
                veKhuVucTabs();
                veDanhSachBan();
            });
            pnlKhuVucTabs.add(btn);
        }
        pnlKhuVucTabs.revalidate();
        pnlKhuVucTabs.repaint();
    }

    private void veDanhSachBan() {
        pnlDanhSachBan.removeAll();
        for (BanAn b : dsBan) {
            if (!khuVucChon.equals("Tất cả") && !khuVucChon.equalsIgnoreCase(b.getKhuVuc())) {
                continue;
            }
            pnlDanhSachBan.add(taoTheBan(b));
        }
        pnlDanhSachBan.revalidate();
        pnlDanhSachBan.repaint();
    }

    private JComponent taoTheBan(BanAn b) {
        boolean dangChon = banDangChon != null && banDangChon.getMaBan() == b.getMaBan();
        String st = b.getTrangThai() != null ? b.getTrangThai().toUpperCase() : "TRONG";

        Color mText, mBg;
        String tenHienThi;

        if (st.contains("PHUC_VU")) {
            mText = GiaoPos.BAN_PHUC_VU;
            mBg = GiaoPos.BAN_PHUC_VU_NEN;
            tenHienThi = "ĐANG NGỒI";
        } else if (st.contains("DAT")) {
            mText = GiaoPos.BAN_DAT_TRUOC;
            mBg = GiaoPos.BAN_DAT_NEN;
            tenHienThi = "ĐÃ ĐẶT";
        } else if (st.contains("DON") || st.contains("THANH_TOAN")) {
            mText = GiaoPos.BAN_CAN_THANH_TOAN;
            mBg = GiaoPos.BAN_CAN_NEN;
            tenHienThi = "CẦN DỌN";
        } else {
            mText = GiaoPos.BAN_TRONG;
            mBg = GiaoPos.BAN_TRONG_NEN;
            tenHienThi = "TRỐNG";
        }

        JPanel card = new JPanel(new BorderLayout(4, 4)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(dangChon ? GiaoPos.DONG_NHAT : mBg);
                g2.fillRoundRect(1, 1, w - 2, h - 2, 12, 12);
                g2.setColor(dangChon ? GiaoPos.DONG_CHINH : GiaoPos.THE_VIEN);
                g2.setStroke(new BasicStroke(dangChon ? 2.2f : 1.1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(135, 78));
        card.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel lblTen = new JLabel(b.getTenBan());
        lblTen.setFont(GiaoPos.f(14, Font.BOLD));
        lblTen.setForeground(GiaoPos.CHU_CHINH);

        JLabel lblStatus = new JLabel(tenHienThi);
        lblStatus.setFont(GiaoPos.f(10, Font.BOLD));
        lblStatus.setForeground(mText);

        JLabel lblCho = new JLabel(b.getSoCho() + " chỗ  •  " + (b.getKhuVuc() == null ? "" : b.getKhuVuc()));
        lblCho.setFont(GiaoPos.f(10, Font.PLAIN));
        lblCho.setForeground(GiaoPos.CHU_MO);

        card.add(lblTen, BorderLayout.NORTH);
        card.add(lblStatus, BorderLayout.CENTER);
        card.add(lblCho, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                chonBan(b);
            }
        });

        return card;
    }

    private void chonBan(BanAn b) {
        this.banDangChon = b;
        lblBanTitle.setText(b.getTenBan().toUpperCase());

        // Kiểm tra xem bàn có đơn hàng đang phục vụ không
        donHangHienTai = donHangDAO.layDonHangDangPhucVuCuaBan(b.getMaBan());
        gioHang.clear();

        if (donHangHienTai != null) {
            lblMaDonTitle.setText("Đơn hàng: " + donHangHienTai.getMaDon() + "  •  Đang phục vụ");
            List<ChiTietDonHang> items = chiTietDonHangDAO.layChiTietTheoDonHang(donHangHienTai.getMaDonHang());
            gioHang.addAll(items);
        } else {
            lblMaDonTitle.setText("Bàn " + (b.getTrangThai().equalsIgnoreCase("TRONG") ? "trống" : b.getTrangThai()) + " • Chưa có đơn");
        }

        capNhatBangGioHang();
        veDanhSachBan();
    }

    private void veDanhMucTabs() {
        pnlDanhMucTabs.removeAll();
        LinkedHashSet<String> dms = new LinkedHashSet<>();
        dms.add("Tất cả");
        for (MonAn m : dsMon) {
            if (m.getDanhMuc() != null) dms.add(m.getDanhMuc().trim());
        }

        for (String dm : dms) {
            GiaoPos.NutPos btn = new GiaoPos.NutPos(dm, GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(12, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 16, 36));
            btn.setActive(dm.equals(danhMucChon));
            btn.addActionListener(e -> {
                danhMucChon = dm;
                veDanhMucTabs();
                veLuoiMon();
            });
            pnlDanhMucTabs.add(btn);
        }
        pnlDanhMucTabs.revalidate();
        pnlDanhMucTabs.repaint();
    }

    private void veLuoiMon() {
        pnlLuoiMon.removeAll();
        String tuKhoa = txtTimMon.getText().trim().toLowerCase();

        for (MonAn m : dsMon) {
            if (!danhMucChon.equals("Tất cả") && !danhMucChon.equalsIgnoreCase(m.getDanhMuc())) {
                continue;
            }
            if (!tuKhoa.isEmpty() && !m.getTenMon().toLowerCase().contains(tuKhoa)
                    && (m.getMaMonAn() == null || !m.getMaMonAn().toLowerCase().contains(tuKhoa))) {
                continue;
            }
            pnlLuoiMon.add(taoTheMon(m));
        }
        pnlLuoiMon.revalidate();
        pnlLuoiMon.repaint();
    }

    private JComponent taoTheMon(MonAn m) {
        GiaoPos.ThePos the = new GiaoPos.ThePos(new BorderLayout(0, 6));
        the.setPreferredSize(new Dimension(170, 110));
        the.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTen = new JLabel("<html><b>" + m.getTenMon() + "</b></html>");
        lblTen.setFont(GiaoPos.f(13, Font.BOLD));
        lblTen.setForeground(GiaoPos.CHU_CHINH);

        JLabel lblGia = new JLabel(GiaoPos.formatTien(m.getGia()));
        lblGia.setFont(GiaoPos.f(15, Font.BOLD));
        lblGia.setForeground(GiaoPos.DONG_CHINH);

        JLabel lblNhom = new JLabel((m.getDanhMuc() != null ? m.getDanhMuc() : "")
                + " • " + (m.getDonVi() != null ? m.getDonVi() : "Phần"));
        lblNhom.setFont(GiaoPos.f(11, Font.PLAIN));
        lblNhom.setForeground(GiaoPos.CHU_MO);

        the.add(lblTen, BorderLayout.NORTH);
        the.add(lblGia, BorderLayout.CENTER);
        the.add(lblNhom, BorderLayout.SOUTH);

        the.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                themMonVaoGio(m);
            }
        });

        return the;
    }

    // ==========================================
    // NGHIỆP VỤ THAO TÁC GIỎ HÀNG & HÓA ĐƠN
    // ==========================================
    private void themMonVaoGio(MonAn m) {
        if (banDangChon == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một Bàn phục vụ ở cột bên trái trước khi chọn món!",
                    "Chưa chọn bàn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tự động khởi tạo đơn hàng nếu bàn chưa có đơn
        if (donHangHienTai == null) {
            DonHang dhMoi = new DonHang();
            dhMoi.setMaBan(banDangChon.getMaBan());
            dhMoi.setTenBan(banDangChon.getTenBan());
            if (nhanVienHienTai != null) {
                dhMoi.setMaNguoiDung(nhanVienHienTai.getMaNguoiDung());
            }
            dhMoi.setTrangThai("DANG_PHUC_VU");
            boolean ok = donHangDAO.taoDonHang(dhMoi);
            if (ok) {
                donHangHienTai = dhMoi;
                banDangChon.setTrangThai("DANG_PHUC_VU");
                lblMaDonTitle.setText("Đơn hàng: " + dhMoi.getMaDon() + "  •  Đang phục vụ");
                veDanhSachBan();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể khởi tạo đơn hàng trên CSDL!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Ghi nhận món vào CSDL qua ChiTietDonHangDAO
        chiTietDonHangDAO.themHoacTangMon(donHangHienTai.getMaDonHang(), m.getMaMon(), 1, m.getGia(), "");

        // Tải lại chi tiết đơn hàng
        gioHang.clear();
        gioHang.addAll(chiTietDonHangDAO.layChiTietTheoDonHang(donHangHienTai.getMaDonHang()));
        capNhatBangGioHang();
    }

    private void capNhatBangGioHang() {
        cartTableModel.setRowCount(0);
        double tamTinh = 0;

        for (ChiTietDonHang item : gioHang) {
            tamTinh += item.getThanhTien();
            cartTableModel.addRow(new Object[]{
                    item.getTenMon(),
                    item.getSoLuong(),
                    GiaoPos.formatTien(item.getDonGia()),
                    GiaoPos.formatTien(item.getThanhTien()),
                    "✕"
            });
        }

        double vat = Math.round(tamTinh * 0.08);
        double giam = donHangHienTai != null ? donHangHienTai.getTienGiam() : 0;
        double tong = Math.max(0, tamTinh + vat - giam);

        lblTamTinh.setText(GiaoPos.formatTien(tamTinh));
        lblVat.setText(GiaoPos.formatTien(vat));
        lblGiamGia.setText(GiaoPos.formatTien(giam));
        lblTongCong.setText(GiaoPos.formatTien(tong));

        if (donHangHienTai != null) {
            donHangHienTai.setTienTamTinh(tamTinh);
            donHangHienTai.setTienThue(vat);
            donHangHienTai.setTongTien(tong);
            donHangDAO.capNhatTienDonHang(donHangHienTai.getMaDonHang(), tamTinh, giam, vat, tong);
        }
    }

    private void suaSoLuongBangPopup(int rowIndex) {
        ChiTietDonHang ct = gioHang.get(rowIndex);
        String nhap = JOptionPane.showInputDialog(this,
                "Nhập số lượng mới cho \"" + ct.getTenMon() + "\":",
                ct.getSoLuong());
        if (nhap != null && !nhap.trim().isEmpty()) {
            try {
                int slMoi = Integer.parseInt(nhap.trim());
                if (slMoi <= 0) {
                    xoaMonKhoiGio(rowIndex);
                } else {
                    chiTietDonHangDAO.capNhatSoLuong(ct.getMaChiTiet(), donHangHienTai.getMaDonHang(), slMoi);
                    gioHang.clear();
                    gioHang.addAll(chiTietDonHangDAO.layChiTietTheoDonHang(donHangHienTai.getMaDonHang()));
                    capNhatBangGioHang();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    private void xoaMonKhoiGio(int rowIndex) {
        ChiTietDonHang ct = gioHang.get(rowIndex);
        int r = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món \"" + ct.getTenMon() + "\" khỏi hóa đơn?",
                "Xác nhận xóa món", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            chiTietDonHangDAO.xoaMonKhoiDon(ct.getMaChiTiet(), donHangHienTai.getMaDonHang());
            gioHang.clear();
            gioHang.addAll(chiTietDonHangDAO.layChiTietTheoDonHang(donHangHienTai.getMaDonHang()));
            capNhatBangGioHang();
        }
    }

    private void xuLyXoaHetMon() {
        if (donHangHienTai == null || gioHang.isEmpty()) return;
        int r = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa TOÀN BỘ món trong đơn hàng " + donHangHienTai.getMaDon() + "?",
                "Xác nhận xóa hết", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            chiTietDonHangDAO.xoaToanBoMon(donHangHienTai.getMaDonHang());
            gioHang.clear();
            capNhatBangGioHang();
        }
    }

    private void xuLyGuiBep() {
        if (donHangHienTai == null || gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn hàng đang trống, chưa có món để gửi bếp!");
            return;
        }

        List<String> monList = new ArrayList<>();
        for (ChiTietDonHang ct : gioHang) {
            monList.add(ct.getTenMon() + " x" + ct.getSoLuong());
        }
        NhaBepPanel.themPhieuTuPos(banDangChon.getTenBan(), monList);

        JOptionPane.showMessageDialog(this,
                "Đã gửi " + gioHang.size() + " món xuống bộ phận Bếp KDS chế biến cho " + banDangChon.getTenBan() + "!",
                "Thông báo gửi bếp", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xuLyInTamTinh() {
        if (donHangHienTai == null || gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn hàng đang trống, không thể in tạm tính!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         NOVA RESTAURANT POS\n");
        sb.append("            PHIẾU TẠM TÍNH\n");
        sb.append("========================================\n");
        sb.append("Bàn: ").append(banDangChon.getTenBan()).append("\n");
        sb.append("Mã đơn: ").append(donHangHienTai.getMaDon()).append("\n");
        sb.append("----------------------------------------\n");
        for (ChiTietDonHang ct : gioHang) {
            sb.append(String.format("%-20s x%-2d %12s\n", ct.getTenMon(), ct.getSoLuong(), GiaoPos.formatTien(ct.getThanhTien())));
        }
        sb.append("----------------------------------------\n");
        sb.append("Tạm tính:  ").append(lblTamTinh.getText()).append("\n");
        sb.append("Thuế VAT:  ").append(lblVat.getText()).append("\n");
        sb.append("TỔNG TIỀN: ").append(lblTongCong.getText()).append("\n");
        sb.append("========================================\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Phiếu tạm tính", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xuLyMoThanhToan() {
        if (donHangHienTai == null || gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn và thêm món trước khi thanh toán!");
            return;
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        ThanhToanDialog dialog = new ThanhToanDialog(parentWindow, donHangHienTai, () -> {
            // Sau khi thanh toán thành công: làm mới giao diện
            gioHang.clear();
            donHangHienTai = null;
            if (banDangChon != null) {
                banDangChon.setTrangThai("TRONG");
            }
            capNhatBangGioHang();
            napDuLieuBan();
        });
        dialog.setVisible(true);
    }

    private void xuLyChuyenBan() {
        if (banDangChon == null || donHangHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn đang có khách ngồi để chuyển bàn!");
            return;
        }

        List<BanAn> dsBanTrong = new ArrayList<>();
        for (BanAn b : dsBan) {
            if (b.getMaBan() != banDangChon.getMaBan() && "TRONG".equalsIgnoreCase(b.getTrangThai())) {
                dsBanTrong.add(b);
            }
        }

        if (dsBanTrong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiện tại không có bàn trống nào để chuyển!");
            return;
        }

        JComboBox<BanAn> cbBanDich = new JComboBox<>(dsBanTrong.toArray(new BanAn[0]));
        int r = JOptionPane.showConfirmDialog(this, cbBanDich,
                "Chọn bàn trống muốn chuyển tới (từ " + banDangChon.getTenBan() + "):",
                JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            BanAn banDich = (BanAn) cbBanDich.getSelectedItem();
            if (banDich != null) {
                boolean ok = banAnDAO.chuyenBan(banDangChon.getMaBan(), banDich.getMaBan());
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Đã chuyển thành công từ " + banDangChon.getTenBan() + " sang " + banDich.getTenBan() + "!");
                    napDuLieuBan();
                    chonBan(banDich);
                } else {
                    JOptionPane.showMessageDialog(this, "Chuyển bàn thất bại!");
                }
            }
        }
    }

    private void xuLyGopBan() {
        if (banDangChon == null || donHangHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn đang có đơn để gộp bàn!");
            return;
        }

        List<BanAn> dsBanKhac = new ArrayList<>();
        for (BanAn b : dsBan) {
            if (b.getMaBan() != banDangChon.getMaBan() && "DANG_PHUC_VU".equalsIgnoreCase(b.getTrangThai())) {
                dsBanKhac.add(b);
            }
        }

        if (dsBanKhac.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có bàn đang phục vụ nào khác để gộp!");
            return;
        }

        JComboBox<BanAn> cbBanDich = new JComboBox<>(dsBanKhac.toArray(new BanAn[0]));
        int r = JOptionPane.showConfirmDialog(this, cbBanDich,
                "Gộp " + banDangChon.getTenBan() + " vào bàn đích nào?",
                JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            BanAn banDich = (BanAn) cbBanDich.getSelectedItem();
            if (banDich != null) {
                boolean ok = banAnDAO.gopBan(banDangChon.getMaBan(), banDich.getMaBan());
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Đã gộp thành công " + banDangChon.getTenBan() + " vào " + banDich.getTenBan() + "!");
                    napDuLieuBan();
                    chonBan(banDich);
                } else {
                    JOptionPane.showMessageDialog(this, "Gộp bàn thất bại!");
                }
            }
        }
    }
}
