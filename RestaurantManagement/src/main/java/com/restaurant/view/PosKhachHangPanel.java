package com.restaurant.view;

import com.restaurant.dao.KhachHangDAO;
import com.restaurant.model.KhachHang;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PosKhachHangPanel extends JPanel {

    private final NguoiDung nhanVien;
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final List<KhachHang> dsKhachHang = new ArrayList<>();

    private DefaultTableModel tableModel;
    private JTable table;
    private GiaoPos.OTextPos txtTimKiem;

    public PosKhachHangPanel(NguoiDung nhanVien) {
        this.nhanVien = nhanVien;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(16, 20, 18, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP Toolbar
        JPanel pnlTop = new JPanel(new BorderLayout(12, 10));
        pnlTop.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG & THÀNH VIÊN");
        lblTitle.setFont(GiaoPos.f(18, Font.BOLD));
        lblTitle.setForeground(GiaoPos.DONG_DAM);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlBar = new JPanel(new BorderLayout(12, 0));
        pnlBar.setOpaque(false);

        txtTimKiem = new GiaoPos.OTextPos("Tìm khách hàng theo Tên, Số điện thoại…", true);
        txtTimKiem.setPreferredSize(new Dimension(320, 42));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { locVaHienThi(); }
        });
        pnlBar.add(txtTimKiem, BorderLayout.WEST);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlActions.setOpaque(false);

        GiaoPos.NutPos btnThem = new GiaoPos.NutPos("+  THÊM KHÁCH HÀNG", GiaoPos.NutPos.STYLE_DONG);
        btnThem.setFont(GiaoPos.f(13, Font.BOLD));
        btnThem.setPreferredSize(new Dimension(175, 42));
        btnThem.addActionListener(e -> moDialogThemKhachHang());
        pnlActions.add(btnThem);

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄", GiaoPos.NutPos.STYLE_TRANG);
        btnLamMoi.setPreferredSize(new Dimension(46, 42));
        btnLamMoi.addActionListener(e -> napDuLieu());
        pnlActions.add(btnLamMoi);

        pnlBar.add(pnlActions, BorderLayout.EAST);
        pnlTop.add(pnlBar, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Bảng khách hàng
        String[] cols = {"Mã KH", "Họ và tên", "Số điện thoại", "Email", "Địa chỉ", "Điểm tích lũy", "Hạng thẻ", "Ghi chú"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(44);
        table.setFont(GiaoPos.f(13, Font.PLAIN));
        table.getTableHeader().setFont(GiaoPos.f(13, Font.BOLD));
        table.getTableHeader().setBackground(GiaoPos.DONG_NHAT);
        table.getTableHeader().setForeground(GiaoPos.DONG_DAM);
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.setSelectionBackground(GiaoPos.DONG_NHAT);
        table.setSelectionForeground(GiaoPos.CHU_CHINH);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(160);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = table.getSelectedRow();
                    if (r >= 0) moDialogSuaKhachHang(r);
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(GiaoPos.THE_VIEN, 1));
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);

        // BOTTOM: Thao tác sửa, xóa
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setOpaque(false);

        GiaoPos.NutPos btnSua = new GiaoPos.NutPos("✏️ Sửa thông tin", GiaoPos.NutPos.STYLE_TRANG);
        btnSua.setPreferredSize(new Dimension(140, 42));
        btnSua.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) moDialogSuaKhachHang(r);
            else JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
        });

        GiaoPos.NutPos btnXoa = new GiaoPos.NutPos("🗑️ Xóa khách hàng", GiaoPos.NutPos.STYLE_DO);
        btnXoa.setPreferredSize(new Dimension(150, 42));
        btnXoa.addActionListener(e -> xuLyXoaKhachHang());

        pnlBot.add(btnSua);
        pnlBot.add(btnXoa);
        add(pnlBot, BorderLayout.SOUTH);
    }

    public void napDuLieu() {
        dsKhachHang.clear();
        dsKhachHang.addAll(khachHangDAO.findAll());
        locVaHienThi();
    }

    private void locVaHienThi() {
        tableModel.setRowCount(0);
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        for (KhachHang kh : dsKhachHang) {
            if (!tuKhoa.isEmpty()
                    && !kh.getHoTen().toLowerCase().contains(tuKhoa)
                    && (kh.getSoDienThoai() == null || !kh.getSoDienThoai().contains(tuKhoa))
                    && (kh.getEmail() == null || !kh.getEmail().toLowerCase().contains(tuKhoa))) {
                continue;
            }

            int diem = 0; // default
            tableModel.addRow(new Object[]{
                    "KH" + String.format("%04d", kh.getMaKhachHang()),
                    kh.getHoTen(),
                    kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "",
                    kh.getEmail() != null ? kh.getEmail() : "",
                    kh.getDiaChi() != null ? kh.getDiaChi() : "",
                    diem,
                    tinhHangThe(diem),
                    kh.getGhiChu() != null ? kh.getGhiChu() : ""
            });
        }
    }

    private String tinhHangThe(int diem) {
        if (diem >= 1000) return "Kim Cương";
        if (diem >= 500) return "Vàng";
        if (diem >= 200) return "Bạc";
        return "Đồng";
    }

    private KhachHang layKHDangChon(int r) {
        if (r < 0 || r >= tableModel.getRowCount()) return null;
        String maStr = (String) tableModel.getValueAt(r, 0);
        for (KhachHang kh : dsKhachHang) {
            if (("KH" + String.format("%04d", kh.getMaKhachHang())).equals(maStr)) return kh;
        }
        return null;
    }

    private void moDialogThemKhachHang() {
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 10));
        form.setPreferredSize(new Dimension(400, 200));

        JTextField txtTen = new JTextField("");
        JTextField txtSDT = new JTextField("");
        JTextField txtEmail = new JTextField("");
        JTextField txtDiaChi = new JTextField("");
        JTextField txtGhiChu = new JTextField("");

        form.add(new JLabel("Họ và tên:")); form.add(txtTen);
        form.add(new JLabel("Số điện thoại:")); form.add(txtSDT);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Địa chỉ:")); form.add(txtDiaChi);
        form.add(new JLabel("Ghi chú:")); form.add(txtGhiChu);

        int r = JOptionPane.showConfirmDialog(this, form, "THÊM KHÁCH HÀNG MỚI", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Họ tên khách hàng không được để trống!");
                return;
            }

            KhachHang kh = new KhachHang();
            kh.setHoTen(ten);
            kh.setSoDienThoai(sdt);
            kh.setEmail(txtEmail.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setGhiChu(txtGhiChu.getText().trim());

            boolean ok = khachHangDAO.insert(kh);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!");
            }
        }
    }

    private void moDialogSuaKhachHang(int rowIndex) {
        KhachHang kh = layKHDangChon(rowIndex);
        if (kh == null) return;

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 10));
        form.setPreferredSize(new Dimension(400, 200));

        JTextField txtTen = new JTextField(kh.getHoTen());
        JTextField txtSDT = new JTextField(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
        JTextField txtEmail = new JTextField(kh.getEmail() != null ? kh.getEmail() : "");
        JTextField txtDiaChi = new JTextField(kh.getDiaChi() != null ? kh.getDiaChi() : "");
        JTextField txtGhiChu = new JTextField(kh.getGhiChu() != null ? kh.getGhiChu() : "");

        form.add(new JLabel("Họ và tên:")); form.add(txtTen);
        form.add(new JLabel("Số điện thoại:")); form.add(txtSDT);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Địa chỉ:")); form.add(txtDiaChi);
        form.add(new JLabel("Ghi chú:")); form.add(txtGhiChu);

        int r = JOptionPane.showConfirmDialog(this, form, "SỬA KHÁCH HÀNG — " + kh.getHoTen(), JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            kh.setHoTen(txtTen.getText().trim());
            kh.setSoDienThoai(txtSDT.getText().trim());
            kh.setEmail(txtEmail.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setGhiChu(txtGhiChu.getText().trim());

            boolean ok = khachHangDAO.update(kh);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private void xuLyXoaKhachHang() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        KhachHang kh = layKHDangChon(r);
        if (kh == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng \"" + kh.getHoTen() + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = khachHangDAO.delete(kh.getMaKhachHang());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã xóa khách hàng thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng do có đơn hàng liên kết!");
            }
        }
    }
}
