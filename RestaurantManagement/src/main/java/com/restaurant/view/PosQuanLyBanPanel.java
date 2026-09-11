package com.restaurant.view;

import com.restaurant.dao.BanAnDAO;
import com.restaurant.model.BanAn;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PosQuanLyBanPanel extends JPanel {

    private final NguoiDung nhanVien;
    private final BanAnDAO banAnDAO = new BanAnDAO();
    private final List<BanAn> dsBan = new ArrayList<>();
    private final Consumer<BanAn> onChonBanDeGoiMon;

    private String locKhuVuc = "Tất cả";
    private String locTrangThai = "Tất cả";

    private JPanel pnlLocKhuVuc;
    private JPanel pnlLocTrangThai;
    private JPanel pnlGridBan;
    private GiaoPos.OTextPos txtTimKiem;
    private JLabel lblStatTong;
    private JLabel lblStatTrong;
    private JLabel lblStatPhucVu;
    private JLabel lblStatDatTruoc;

    public PosQuanLyBanPanel(NguoiDung nhanVien, Consumer<BanAn> onChonBanDeGoiMon) {
        this.nhanVien = nhanVien;
        this.onChonBanDeGoiMon = onChonBanDeGoiMon;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(16, 20, 18, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP: Thống kê số lượng bàn + Bộ lọc + Tìm kiếm + Nút Thêm bàn
        JPanel pnlTop = new JPanel(new BorderLayout(0, 14));
        pnlTop.setOpaque(false);

        // 4 Thẻ thống kê bàn
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 14, 0));
        pnlStats.setOpaque(false);
        pnlStats.setPreferredSize(new Dimension(100, 80));

        lblStatTong = new JLabel("0", SwingConstants.CENTER);
        lblStatTrong = new JLabel("0", SwingConstants.CENTER);
        lblStatPhucVu = new JLabel("0", SwingConstants.CENTER);
        lblStatDatTruoc = new JLabel("0", SwingConstants.CENTER);

        pnlStats.add(taoTheStat("TỔNG SỐ BÀN", lblStatTong, GiaoPos.DONG_CHINH, GiaoPos.DONG_NHAT));
        pnlStats.add(taoTheStat("BÀN TRỐNG", lblStatTrong, GiaoPos.BAN_TRONG, GiaoPos.BAN_TRONG_NEN));
        pnlStats.add(taoTheStat("ĐANG PHỤC VỤ", lblStatPhucVu, GiaoPos.BAN_PHUC_VU, GiaoPos.BAN_PHUC_VU_NEN));
        pnlStats.add(taoTheStat("ĐÃ ĐẶT TRƯỚC", lblStatDatTruoc, GiaoPos.BAN_DAT_TRUOC, GiaoPos.BAN_DAT_NEN));
        pnlTop.add(pnlStats, BorderLayout.NORTH);

        // Thanh công cụ: Lọc khu vực + Trạng thái + Tìm kiếm + Thêm bàn
        JPanel pnlToolbar = new JPanel(new BorderLayout(12, 0));
        pnlToolbar.setOpaque(false);

        JPanel pnlFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlFilters.setOpaque(false);

        pnlLocKhuVuc = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlLocKhuVuc.setOpaque(false);
        pnlFilters.add(pnlLocKhuVuc);

        pnlLocTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlLocTrangThai.setOpaque(false);
        pnlFilters.add(pnlLocTrangThai);

        pnlToolbar.add(pnlFilters, BorderLayout.WEST);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlActions.setOpaque(false);

        txtTimKiem = new GiaoPos.OTextPos("Tìm tên bàn, khu vực…", true);
        txtTimKiem.setPreferredSize(new Dimension(220, 42));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { veGridBan(); }
        });
        pnlActions.add(txtTimKiem);

        GiaoPos.NutPos btnThemBan = new GiaoPos.NutPos("+  THÊM BÀN MỚI", GiaoPos.NutPos.STYLE_DONG);
        btnThemBan.setFont(GiaoPos.f(13, Font.BOLD));
        btnThemBan.setPreferredSize(new Dimension(160, 42));
        btnThemBan.addActionListener(e -> moDialogThemBan());
        pnlActions.add(btnThemBan);

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄", GiaoPos.NutPos.STYLE_TRANG);
        btnLamMoi.setPreferredSize(new Dimension(46, 42));
        btnLamMoi.addActionListener(e -> napDuLieu());
        pnlActions.add(btnLamMoi);

        pnlToolbar.add(pnlActions, BorderLayout.EAST);
        pnlTop.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Lưới các bàn
        pnlGridBan = new JPanel(new GridLayout(0, 5, 14, 14));
        pnlGridBan.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(pnlGridBan, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(wrapper);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(22);
        add(sp, BorderLayout.CENTER);
    }

    private JPanel taoTheStat(String tieuDe, JLabel lblVal, Color cText, Color cBg) {
        GiaoPos.ThePos pnl = new GiaoPos.ThePos(new BorderLayout());
        pnl.setTheBackground(cBg);
        pnl.setBorderColor(new Color(cText.getRed(), cText.getGreen(), cText.getBlue(), 80));

        JLabel lt = new JLabel(tieuDe, SwingConstants.CENTER);
        lt.setFont(GiaoPos.f(11, Font.BOLD));
        lt.setForeground(cText);

        lblVal.setFont(GiaoPos.f(26, Font.BOLD));
        lblVal.setForeground(cText);

        pnl.add(lt, BorderLayout.NORTH);
        pnl.add(lblVal, BorderLayout.CENTER);
        return pnl;
    }

    public void napDuLieu() {
        dsBan.clear();
        dsBan.addAll(banAnDAO.layTatCa());
        capNhatThongKe();
        veLocKhuVuc();
        veLocTrangThai();
        veGridBan();
    }

    private void capNhatThongKe() {
        int tong = dsBan.size();
        int trong = 0, phucVu = 0, dat = 0;
        for (BanAn b : dsBan) {
            String st = b.getTrangThai() != null ? b.getTrangThai().toUpperCase() : "TRONG";
            if (st.contains("PHUC_VU")) phucVu++;
            else if (st.contains("DAT")) dat++;
            else trong++;
        }
        lblStatTong.setText(String.valueOf(tong));
        lblStatTrong.setText(String.valueOf(trong));
        lblStatPhucVu.setText(String.valueOf(phucVu));
        lblStatDatTruoc.setText(String.valueOf(dat));
    }

    private void veLocKhuVuc() {
        pnlLocKhuVuc.removeAll();
        LinkedHashSet<String> kvs = new LinkedHashSet<>();
        kvs.add("Tất cả");
        for (BanAn b : dsBan) {
            if (b.getKhuVuc() != null && !b.getKhuVuc().trim().isEmpty()) {
                kvs.add(b.getKhuVuc().trim());
            }
        }
        for (String kv : kvs) {
            GiaoPos.NutPos btn = new GiaoPos.NutPos(kv, GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 36));
            btn.setActive(kv.equals(locKhuVuc));
            btn.addActionListener(e -> {
                locKhuVuc = kv;
                veLocKhuVuc();
                veGridBan();
            });
            pnlLocKhuVuc.add(btn);
        }
        pnlLocKhuVuc.revalidate();
        pnlLocKhuVuc.repaint();
    }

    private void veLocTrangThai() {
        pnlLocTrangThai.removeAll();
        String[] tts = {"Tất cả", "Trống", "Đang phục vụ", "Đã đặt", "Cần dọn"};
        for (String tt : tts) {
            GiaoPos.NutPos btn = new GiaoPos.NutPos(tt, GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 8, 36));
            btn.setActive(tt.equals(locTrangThai));
            btn.addActionListener(e -> {
                locTrangThai = tt;
                veLocTrangThai();
                veGridBan();
            });
            pnlLocTrangThai.add(btn);
        }
        pnlLocTrangThai.revalidate();
        pnlLocTrangThai.repaint();
    }

    private void veGridBan() {
        pnlGridBan.removeAll();
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        for (BanAn b : dsBan) {
            if (!locKhuVuc.equals("Tất cả") && !locKhuVuc.equalsIgnoreCase(b.getKhuVuc())) {
                continue;
            }
            String st = b.getTrangThai() != null ? b.getTrangThai().toUpperCase() : "TRONG";
            if (locTrangThai.equals("Trống") && !st.equals("TRONG")) continue;
            if (locTrangThai.equals("Đang phục vụ") && !st.contains("PHUC_VU")) continue;
            if (locTrangThai.equals("Đã đặt") && !st.contains("DAT")) continue;
            if (locTrangThai.equals("Cần dọn") && !st.contains("DON")) continue;

            if (!tuKhoa.isEmpty() && !b.getTenBan().toLowerCase().contains(tuKhoa)
                    && (b.getKhuVuc() == null || !b.getKhuVuc().toLowerCase().contains(tuKhoa))) {
                continue;
            }
            pnlGridBan.add(taoTheBanLietKe(b));
        }
        pnlGridBan.revalidate();
        pnlGridBan.repaint();
    }

    private JComponent taoTheBanLietKe(BanAn b) {
        GiaoPos.ThePos the = new GiaoPos.ThePos(new BorderLayout(0, 8));
        the.setPreferredSize(new Dimension(200, 160));

        String st = b.getTrangThai() != null ? b.getTrangThai().toUpperCase() : "TRONG";
        Color mCol, mBg;
        String tenStatus;

        if (st.contains("PHUC_VU")) {
            mCol = GiaoPos.BAN_PHUC_VU;
            mBg = GiaoPos.BAN_PHUC_VU_NEN;
            tenStatus = "ĐANG PHỤC VỤ";
        } else if (st.contains("DAT")) {
            mCol = GiaoPos.BAN_DAT_TRUOC;
            mBg = GiaoPos.BAN_DAT_NEN;
            tenStatus = "ĐÃ ĐẶT TRƯỚC";
        } else if (st.contains("DON")) {
            mCol = GiaoPos.BAN_CAN_THANH_TOAN;
            mBg = GiaoPos.BAN_CAN_NEN;
            tenStatus = "CẦN DỌN";
        } else {
            mCol = GiaoPos.BAN_TRONG;
            mBg = GiaoPos.BAN_TRONG_NEN;
            tenStatus = "TRỐNG";
        }

        the.setTheBackground(mBg);
        the.setBorderColor(new Color(mCol.getRed(), mCol.getGreen(), mCol.getBlue(), 120));

        // Header thẻ bàn
        JPanel pnlHead = new JPanel(new BorderLayout());
        pnlHead.setOpaque(false);

        JLabel lblTen = new JLabel(b.getTenBan());
        lblTen.setFont(GiaoPos.f(17, Font.BOLD));
        lblTen.setForeground(GiaoPos.CHU_CHINH);
        pnlHead.add(lblTen, BorderLayout.WEST);

        JLabel lblSt = new JLabel(tenStatus);
        lblSt.setFont(GiaoPos.f(10, Font.BOLD));
        lblSt.setForeground(mCol);
        pnlHead.add(lblSt, BorderLayout.EAST);
        the.add(pnlHead, BorderLayout.NORTH);

        // Center thông tin
        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlCenter.setOpaque(false);

        JLabel lblInfo1 = new JLabel("Khu vực: " + (b.getKhuVuc() != null ? b.getKhuVuc() : "Tầng 1"));
        lblInfo1.setFont(GiaoPos.f(12, Font.PLAIN));
        lblInfo1.setForeground(GiaoPos.CHU_PHU);

        JLabel lblInfo2 = new JLabel("Sức chứa: " + b.getSoCho() + " chỗ ngồi");
        lblInfo2.setFont(GiaoPos.f(12, Font.PLAIN));
        lblInfo2.setForeground(GiaoPos.CHU_PHU);

        pnlCenter.add(lblInfo1);
        pnlCenter.add(lblInfo2);
        the.add(pnlCenter, BorderLayout.CENTER);

        // Nút hành động nhanh trên thẻ bàn
        JPanel pnlActions = new JPanel(new GridLayout(1, 3, 4, 0));
        pnlActions.setOpaque(false);
        pnlActions.setPreferredSize(new Dimension(100, 36));

        GiaoPos.NutPos btnGoiMon = new GiaoPos.NutPos("Gọi món", GiaoPos.NutPos.STYLE_DONG);
        btnGoiMon.setFont(GiaoPos.f(11, Font.BOLD));
        btnGoiMon.addActionListener(e -> {
            if (onChonBanDeGoiMon != null) {
                onChonBanDeGoiMon.accept(b);
            }
        });

        GiaoPos.NutPos btnSua = new GiaoPos.NutPos("Sửa", GiaoPos.NutPos.STYLE_TRANG);
        btnSua.setFont(GiaoPos.f(11, Font.BOLD));
        btnSua.addActionListener(e -> moDialogSuaBan(b));

        GiaoPos.NutPos btnXoa = new GiaoPos.NutPos("Xóa", GiaoPos.NutPos.STYLE_DO);
        btnXoa.setFont(GiaoPos.f(11, Font.BOLD));
        btnXoa.addActionListener(e -> xuLyXoaBan(b));

        pnlActions.add(btnGoiMon);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        the.add(pnlActions, BorderLayout.SOUTH);

        return the;
    }

    private void moDialogThemBan() {
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 12));
        form.setPreferredSize(new Dimension(360, 200));

        JTextField txtTen = new JTextField("Bàn " + (dsBan.size() + 1));
        JTextField txtKhu = new JTextField("Tầng 1");
        JSpinner spSoCho = new JSpinner(new SpinnerNumberModel(4, 1, 50, 1));
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"TRONG", "DAT_TRUOC", "DANG_PHUC_VU", "CAN_DON"});
        JTextField txtGhiChu = new JTextField("");

        form.add(new JLabel("Tên bàn:")); form.add(txtTen);
        form.add(new JLabel("Khu vực:")); form.add(txtKhu);
        form.add(new JLabel("Sức chứa:")); form.add(spSoCho);
        form.add(new JLabel("Trạng thái:")); form.add(cbTrangThai);
        form.add(new JLabel("Ghi chú:")); form.add(txtGhiChu);

        int r = JOptionPane.showConfirmDialog(this, form, "THÊM BÀN MỚI", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên bàn không được để trống!");
                return;
            }
            BanAn b = new BanAn();
            b.setTenBan(ten);
            b.setKhuVuc(txtKhu.getText().trim());
            b.setSoCho((Integer) spSoCho.getValue());
            b.setTrangThai((String) cbTrangThai.getSelectedItem());
            b.setGhiChu(txtGhiChu.getText().trim());

            boolean ok = banAnDAO.themBan(b);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm bàn thất bại!");
            }
        }
    }

    private void moDialogSuaBan(BanAn b) {
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 12));
        form.setPreferredSize(new Dimension(360, 200));

        JTextField txtTen = new JTextField(b.getTenBan());
        JTextField txtKhu = new JTextField(b.getKhuVuc() != null ? b.getKhuVuc() : "");
        JSpinner spSoCho = new JSpinner(new SpinnerNumberModel(b.getSoCho() > 0 ? b.getSoCho() : 4, 1, 50, 1));
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"TRONG", "DANG_PHUC_VU", "DAT_TRUOC", "CAN_DON"});
        cbTrangThai.setSelectedItem(b.getTrangThai() != null ? b.getTrangThai().toUpperCase() : "TRONG");
        JTextField txtGhiChu = new JTextField(b.getGhiChu() != null ? b.getGhiChu() : "");

        form.add(new JLabel("Tên bàn:")); form.add(txtTen);
        form.add(new JLabel("Khu vực:")); form.add(txtKhu);
        form.add(new JLabel("Sức chứa:")); form.add(spSoCho);
        form.add(new JLabel("Trạng thái:")); form.add(cbTrangThai);
        form.add(new JLabel("Ghi chú:")); form.add(txtGhiChu);

        int r = JOptionPane.showConfirmDialog(this, form, "SỬA BÀN " + b.getTenBan(), JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            b.setTenBan(txtTen.getText().trim());
            b.setKhuVuc(txtKhu.getText().trim());
            b.setSoCho((Integer) spSoCho.getValue());
            b.setTrangThai((String) cbTrangThai.getSelectedItem());
            b.setGhiChu(txtGhiChu.getText().trim());

            boolean ok = banAnDAO.suaBan(b);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin bàn thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật bàn thất bại!");
            }
        }
    }

    private void xuLyXoaBan(BanAn b) {
        int r = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + b.getTenBan() + "\"?\nLưu ý: Không thể xóa bàn đang có khách ngồi!",
                "Xác nhận xóa bàn", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            boolean ok = banAnDAO.xoaBan(b.getMaBan());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã xóa bàn thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa bàn! Bàn đang có đơn phục vụ hoặc ràng buộc dữ liệu.");
            }
        }
    }
}
