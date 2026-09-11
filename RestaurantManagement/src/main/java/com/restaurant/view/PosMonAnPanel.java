package com.restaurant.view;

import com.restaurant.dao.MonAnDAO;
import com.restaurant.model.MonAn;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PosMonAnPanel extends JPanel {

    private final NguoiDung nhanVien;
    private final MonAnDAO monAnDAO = new MonAnDAO();
    private final List<MonAn> dsMon = new ArrayList<>();

    private String locDanhMuc = "Tất cả";
    private DefaultTableModel tableModel;
    private JTable table;
    private GiaoPos.OTextPos txtTimKiem;
    private JPanel pnlDanhMucTabs;

    public PosMonAnPanel(NguoiDung nhanVien) {
        this.nhanVien = nhanVien;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(16, 20, 18, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP: Toolbar tìm kiếm, danh mục tabs và nút Thêm món
        JPanel pnlTop = new JPanel(new BorderLayout(12, 10));
        pnlTop.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ THỰC ĐƠN & MÓN ĂN");
        lblTitle.setFont(GiaoPos.f(18, Font.BOLD));
        lblTitle.setForeground(GiaoPos.DONG_DAM);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlBar = new JPanel(new BorderLayout(12, 0));
        pnlBar.setOpaque(false);

        pnlDanhMucTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlDanhMucTabs.setOpaque(false);
        pnlBar.add(pnlDanhMucTabs, BorderLayout.WEST);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlActions.setOpaque(false);

        txtTimKiem = new GiaoPos.OTextPos("Tìm theo tên món ăn…", true);
        txtTimKiem.setPreferredSize(new Dimension(240, 42));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { locVaHienThi(); }
        });
        pnlActions.add(txtTimKiem);

        GiaoPos.NutPos btnThem = new GiaoPos.NutPos("+  THÊM MÓN MỚI", GiaoPos.NutPos.STYLE_DONG);
        btnThem.setFont(GiaoPos.f(13, Font.BOLD));
        btnThem.setPreferredSize(new Dimension(160, 42));
        btnThem.addActionListener(e -> moDialogThemMon());
        pnlActions.add(btnThem);

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄", GiaoPos.NutPos.STYLE_TRANG);
        btnLamMoi.setPreferredSize(new Dimension(46, 42));
        btnLamMoi.addActionListener(e -> napDuLieu());
        pnlActions.add(btnLamMoi);

        pnlBar.add(pnlActions, BorderLayout.EAST);
        pnlTop.add(pnlBar, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Bảng danh sách món ăn
        String[] cols = {"Mã món", "Tên món ăn", "Danh mục", "Giá bán", "Giá vốn", "Đơn vị", "Trạng thái kinh doanh", "Mô tả"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(200);

        // Hiển thị trạng thái có màu sắc
        table.getColumnModel().getColumn(6).setCellRenderer((t, v, s, f, r, c) -> {
            JLabel lbl = new JLabel(v != null ? v.toString() : "", SwingConstants.CENTER);
            lbl.setFont(GiaoPos.f(12, Font.BOLD));
            lbl.setOpaque(true);
            if ("Đang bán".equals(v)) {
                lbl.setForeground(GiaoPos.BAN_TRONG);
                lbl.setBackground(GiaoPos.BAN_TRONG_NEN);
            } else {
                lbl.setForeground(GiaoPos.BAN_CAN_THANH_TOAN);
                lbl.setBackground(GiaoPos.BAN_CAN_NEN);
            }
            return lbl;
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = table.getSelectedRow();
                    if (r >= 0) moDialogSuaMon(r);
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(GiaoPos.THE_VIEN, 1));
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);

        // BOTTOM: Thanh nút thao tác
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setOpaque(false);

        GiaoPos.NutPos btnSua = new GiaoPos.NutPos("✏️ Sửa món", GiaoPos.NutPos.STYLE_TRANG);
        btnSua.setPreferredSize(new Dimension(130, 42));
        btnSua.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) moDialogSuaMon(r);
            else JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa từ bảng!");
        });

        GiaoPos.NutPos btnDoiTT = new GiaoPos.NutPos("🔄 Đổi trạng thái (Bán/Hết)", GiaoPos.NutPos.STYLE_DONG_PHU);
        btnDoiTT.setPreferredSize(new Dimension(200, 42));
        btnDoiTT.addActionListener(e -> xuLyDoiTrangThai());

        GiaoPos.NutPos btnXoa = new GiaoPos.NutPos("🗑️ Xóa món", GiaoPos.NutPos.STYLE_DO);
        btnXoa.setPreferredSize(new Dimension(120, 42));
        btnXoa.addActionListener(e -> xuLyXoaMon());

        pnlBot.add(btnSua);
        pnlBot.add(btnDoiTT);
        pnlBot.add(btnXoa);
        add(pnlBot, BorderLayout.SOUTH);
    }

    public void napDuLieu() {
        dsMon.clear();
        dsMon.addAll(monAnDAO.layTatCa());
        veDanhMucTabs();
        locVaHienThi();
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
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 12, 36));
            btn.setActive(dm.equals(locDanhMuc));
            btn.addActionListener(e -> {
                locDanhMuc = dm;
                veDanhMucTabs();
                locVaHienThi();
            });
            pnlDanhMucTabs.add(btn);
        }
        pnlDanhMucTabs.revalidate();
        pnlDanhMucTabs.repaint();
    }

    private void locVaHienThi() {
        tableModel.setRowCount(0);
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        for (MonAn m : dsMon) {
            if (!locDanhMuc.equals("Tất cả") && !locDanhMuc.equalsIgnoreCase(m.getDanhMuc())) {
                continue;
            }
            if (!tuKhoa.isEmpty() && !m.getTenMon().toLowerCase().contains(tuKhoa)
                    && (m.getMaMonAn() == null || !m.getMaMonAn().toLowerCase().contains(tuKhoa))) {
                continue;
            }
            tableModel.addRow(new Object[]{
                    m.getMaMonAn() != null ? m.getMaMonAn() : "M" + m.getMaMon(),
                    m.getTenMon(),
                    m.getDanhMuc(),
                    GiaoPos.formatTien(m.getGia()),
                    GiaoPos.formatTien(m.getGiaVon()),
                    m.getDonVi() != null ? m.getDonVi() : "Phần",
                    m.isDangBan() ? "Đang bán" : "Ngừng bán",
                    m.getMoTa() != null ? m.getMoTa() : ""
            });
        }
    }

    private MonAn layMonTuRow(int r) {
        if (r < 0 || r >= tableModel.getRowCount()) return null;
        String code = (String) tableModel.getValueAt(r, 0);
        for (MonAn m : dsMon) {
            String mCode = m.getMaMonAn() != null ? m.getMaMonAn() : "M" + m.getMaMon();
            if (mCode.equals(code)) return m;
        }
        return null;
    }

    private void moDialogThemMon() {
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setPreferredSize(new Dimension(420, 240));

        JTextField txtTen = new JTextField("");
        List<String> dms = monAnDAO.layDanhSachDanhMuc();
        if (dms.isEmpty()) { dms.add("Khai vị"); dms.add("Món chính"); dms.add("Đồ uống"); dms.add("Tráng miệng"); }
        JComboBox<String> cbDM = new JComboBox<>(dms.toArray(new String[0]));
        JTextField txtGiaBan = new JTextField("50000");
        JTextField txtGiaVon = new JTextField("25000");
        JTextField txtDonVi = new JTextField("Phần");
        JTextField txtMoTa = new JTextField("");

        form.add(new JLabel("Tên món:")); form.add(txtTen);
        form.add(new JLabel("Danh mục:")); form.add(cbDM);
        form.add(new JLabel("Giá bán (đ):")); form.add(txtGiaBan);
        form.add(new JLabel("Giá vốn (đ):")); form.add(txtGiaVon);
        form.add(new JLabel("Đơn vị tính:")); form.add(txtDonVi);
        form.add(new JLabel("Mô tả:")); form.add(txtMoTa);

        int r = JOptionPane.showConfirmDialog(this, form, "THÊM MÓN ĂN MỚI", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên món không được để trống!");
                return;
            }
            try {
                double giaBan = Double.parseDouble(txtGiaBan.getText().replaceAll("[^0-9]", ""));
                double giaVon = Double.parseDouble(txtGiaVon.getText().replaceAll("[^0-9]", ""));

                MonAn m = new MonAn();
                m.setTenMon(ten);
                m.setDanhMuc((String) cbDM.getSelectedItem());
                m.setGia(giaBan);
                m.setGiaVon(giaVon);
                m.setDonVi(txtDonVi.getText().trim());
                m.setMoTa(txtMoTa.getText().trim());
                m.setDangBan(true);
                m.setMaDanhMuc(cbDM.getSelectedIndex() + 1);

                boolean ok = monAnDAO.insert(m);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Thêm món thành công!");
                    napDuLieu();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm món thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Giá bán và giá vốn phải là số hợp lệ!");
            }
        }
    }

    private void moDialogSuaMon(int rowIndex) {
        MonAn m = layMonTuRow(rowIndex);
        if (m == null) return;

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setPreferredSize(new Dimension(420, 240));

        JTextField txtTen = new JTextField(m.getTenMon());
        List<String> dms = monAnDAO.layDanhSachDanhMuc();
        JComboBox<String> cbDM = new JComboBox<>(dms.toArray(new String[0]));
        cbDM.setSelectedItem(m.getDanhMuc());
        JTextField txtGiaBan = new JTextField(String.valueOf((long) m.getGia()));
        JTextField txtGiaVon = new JTextField(String.valueOf((long) m.getGiaVon()));
        JTextField txtDonVi = new JTextField(m.getDonVi() != null ? m.getDonVi() : "Phần");
        JTextField txtMoTa = new JTextField(m.getMoTa() != null ? m.getMoTa() : "");

        form.add(new JLabel("Tên món:")); form.add(txtTen);
        form.add(new JLabel("Danh mục:")); form.add(cbDM);
        form.add(new JLabel("Giá bán (đ):")); form.add(txtGiaBan);
        form.add(new JLabel("Giá vốn (đ):")); form.add(txtGiaVon);
        form.add(new JLabel("Đơn vị tính:")); form.add(txtDonVi);
        form.add(new JLabel("Mô tả:")); form.add(txtMoTa);

        int r = JOptionPane.showConfirmDialog(this, form, "SỬA THÔNG TIN MÓN — " + m.getTenMon(), JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            try {
                m.setTenMon(txtTen.getText().trim());
                m.setDanhMuc((String) cbDM.getSelectedItem());
                m.setGia(Double.parseDouble(txtGiaBan.getText().replaceAll("[^0-9]", "")));
                m.setGiaVon(Double.parseDouble(txtGiaVon.getText().replaceAll("[^0-9]", "")));
                m.setDonVi(txtDonVi.getText().trim());
                m.setMoTa(txtMoTa.getText().trim());
                m.setMaDanhMuc(cbDM.getSelectedIndex() + 1);

                boolean ok = monAnDAO.update(m);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cập nhật món thành công!");
                    napDuLieu();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Giá bán và giá vốn phải là số hợp lệ!");
            }
        }
    }

    private void xuLyDoiTrangThai() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần đổi trạng thái!");
            return;
        }
        MonAn m = layMonTuRow(r);
        if (m != null) {
            boolean ttMoi = !m.isDangBan();
            boolean ok = monAnDAO.capNhatTrangThai(m.getMaMon(), ttMoi);
            if (ok) {
                m.setDangBan(ttMoi);
                locVaHienThi();
            } else {
                JOptionPane.showMessageDialog(this, "Không đổi được trạng thái món!");
            }
        }
    }

    private void xuLyXoaMon() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }
        MonAn m = layMonTuRow(r);
        if (m == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa món \"" + m.getTenMon() + "\"?",
                "Xác nhận xóa món", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = monAnDAO.delete(m.getMaMon());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã xóa (hoặc chuyển sang ngừng bán) thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Món đang có trong đơn phục vụ, không thể xóa!");
            }
        }
    }
}
