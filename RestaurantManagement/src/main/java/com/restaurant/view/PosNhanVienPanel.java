package com.restaurant.view;

import com.restaurant.dao.NguoiDungDAO;
import com.restaurant.model.NguoiDung;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PosNhanVienPanel extends JPanel {

    private final NguoiDung nguoiDangNhap;
    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final List<NguoiDung> dsNhanVien = new ArrayList<>();

    private String locVaiTro = "Tất cả";
    private DefaultTableModel tableModel;
    private JTable table;
    private GiaoPos.OTextPos txtTimKiem;
    private JPanel pnlRoleTabs;

    public PosNhanVienPanel(NguoiDung nguoiDangNhap) {
        this.nguoiDangNhap = nguoiDangNhap;
        setOpaque(false);
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(16, 20, 18, 20));

        dungGiaoDien();
        napDuLieu();
    }

    private void dungGiaoDien() {
        // TOP: Toolbar
        JPanel pnlTop = new JPanel(new BorderLayout(12, 10));
        pnlTop.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN SỰ & PHÂN QUYỀN HỆ THỐNG");
        lblTitle.setFont(GiaoPos.f(18, Font.BOLD));
        lblTitle.setForeground(GiaoPos.DONG_DAM);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlBar = new JPanel(new BorderLayout(12, 0));
        pnlBar.setOpaque(false);

        pnlRoleTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlRoleTabs.setOpaque(false);
        pnlBar.add(pnlRoleTabs, BorderLayout.WEST);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlActions.setOpaque(false);

        txtTimKiem = new GiaoPos.OTextPos("Tìm tên, tài khoản, SĐT…", true);
        txtTimKiem.setPreferredSize(new Dimension(240, 42));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { locVaHienThi(); }
        });
        pnlActions.add(txtTimKiem);

        GiaoPos.NutPos btnThem = new GiaoPos.NutPos("+  THÊM NHÂN VIÊN", GiaoPos.NutPos.STYLE_DONG);
        btnThem.setFont(GiaoPos.f(13, Font.BOLD));
        btnThem.setPreferredSize(new Dimension(170, 42));
        btnThem.addActionListener(e -> moDialogThemNhanVien());
        pnlActions.add(btnThem);

        GiaoPos.NutPos btnLamMoi = new GiaoPos.NutPos("🔄", GiaoPos.NutPos.STYLE_TRANG);
        btnLamMoi.setPreferredSize(new Dimension(46, 42));
        btnLamMoi.addActionListener(e -> napDuLieu());
        pnlActions.add(btnLamMoi);

        pnlBar.add(pnlActions, BorderLayout.EAST);
        pnlTop.add(pnlBar, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // CENTER: Bảng nhân viên
        String[] cols = {"Mã NV", "Tên đăng nhập", "Họ và tên", "Vai trò", "Số điện thoại", "Ca làm", "Lương cơ bản", "Trạng thái"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);

        table.getColumnModel().getColumn(7).setCellRenderer((t, v, s, f, r, c) -> {
            JLabel lbl = new JLabel(v != null ? v.toString() : "", SwingConstants.CENTER);
            lbl.setFont(GiaoPos.f(12, Font.BOLD));
            lbl.setOpaque(true);
            if ("Đang làm việc".equals(v)) {
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
                    if (r >= 0) moDialogSuaNhanVien(r);
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(GiaoPos.THE_VIEN, 1));
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);

        // BOTTOM: Thao tác
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setOpaque(false);

        GiaoPos.NutPos btnSua = new GiaoPos.NutPos("✏️ Sửa thông tin", GiaoPos.NutPos.STYLE_TRANG);
        btnSua.setPreferredSize(new Dimension(140, 42));
        btnSua.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) moDialogSuaNhanVien(r);
            else JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
        });

        GiaoPos.NutPos btnDoiMK = new GiaoPos.NutPos("🔑 Đổi mật khẩu", GiaoPos.NutPos.STYLE_TRANG);
        btnDoiMK.setPreferredSize(new Dimension(140, 42));
        btnDoiMK.addActionListener(e -> xuLyDoiMatKhau());

        GiaoPos.NutPos btnKhoa = new GiaoPos.NutPos("🔒 Khóa / Mở tài khoản", GiaoPos.NutPos.STYLE_DONG_PHU);
        btnKhoa.setPreferredSize(new Dimension(180, 42));
        btnKhoa.addActionListener(e -> xuLyKhoaMo());

        GiaoPos.NutPos btnXoa = new GiaoPos.NutPos("🗑️ Xóa nhân viên", GiaoPos.NutPos.STYLE_DO);
        btnXoa.setPreferredSize(new Dimension(140, 42));
        btnXoa.addActionListener(e -> xuLyXoaNhanVien());

        pnlBot.add(btnSua);
        pnlBot.add(btnDoiMK);
        pnlBot.add(btnKhoa);
        pnlBot.add(btnXoa);
        add(pnlBot, BorderLayout.SOUTH);
    }

    public void napDuLieu() {
        dsNhanVien.clear();
        dsNhanVien.addAll(nguoiDungDAO.layDanhSachNhanVien());
        veRoleTabs();
        locVaHienThi();
    }

    private void veRoleTabs() {
        pnlRoleTabs.removeAll();
        String[] roles = {"Tất cả", "QUAN_TRI", "QUAN_LY", "THU_NGAN", "PHUC_VU", "BEP"};
        String[] labels = {"Tất cả", "Quản trị", "Quản lý", "Thu ngân", "Phục vụ", "Bếp"};

        for (int i = 0; i < roles.length; i++) {
            final String r = roles[i];
            GiaoPos.NutPos btn = new GiaoPos.NutPos(labels[i], GiaoPos.NutPos.STYLE_TAB);
            btn.setFont(GiaoPos.f(11, Font.BOLD));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 36));
            btn.setActive(r.equals(locVaiTro));
            btn.addActionListener(e -> {
                locVaiTro = r;
                veRoleTabs();
                locVaHienThi();
            });
            pnlRoleTabs.add(btn);
        }
        pnlRoleTabs.revalidate();
        pnlRoleTabs.repaint();
    }

    private void locVaHienThi() {
        tableModel.setRowCount(0);
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        for (NguoiDung nd : dsNhanVien) {
            if (!locVaiTro.equals("Tất cả") && !locVaiTro.equalsIgnoreCase(nd.getVaiTro())) {
                continue;
            }
            if (!tuKhoa.isEmpty()
                    && !nd.getHoTen().toLowerCase().contains(tuKhoa)
                    && !nd.getTenDangNhap().toLowerCase().contains(tuKhoa)
                    && (nd.getSoDienThoai() == null || !nd.getSoDienThoai().contains(tuKhoa))) {
                continue;
            }
            tableModel.addRow(new Object[]{
                    "NV" + String.format("%03d", nd.getMaNguoiDung()),
                    nd.getTenDangNhap(),
                    nd.getHoTen(),
                    hienVaiTro(nd.getVaiTro()),
                    nd.getSoDienThoai() != null ? nd.getSoDienThoai() : "—",
                    nd.getCaLam() != null ? nd.getCaLam() : "Full-time",
                    GiaoPos.formatTien(nd.getLuong()),
                    nd.isTrangThai() ? "Đang làm việc" : "Đã khóa"
            });
        }
    }

    private String hienVaiTro(String vt) {
        if (vt == null) return "Nhân viên";
        if (vt.contains("QUAN_TRI")) return "Quản trị viên";
        if (vt.contains("QUAN_LY")) return "Quản lý";
        if (vt.contains("THU_NGAN")) return "Thu ngân";
        if (vt.contains("PHUC_VU")) return "Phục vụ";
        if (vt.contains("BEP")) return "Đầu bếp";
        return vt;
    }

    private NguoiDung layNVDangChon(int r) {
        if (r < 0 || r >= tableModel.getRowCount()) return null;
        String tk = (String) tableModel.getValueAt(r, 1);
        for (NguoiDung nd : dsNhanVien) {
            if (nd.getTenDangNhap().equals(tk)) return nd;
        }
        return null;
    }

    private void moDialogThemNhanVien() {
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 10));
        form.setPreferredSize(new Dimension(420, 280));

        JTextField txtTK = new JTextField("");
        JPasswordField txtMK = new JPasswordField("123456");
        JTextField txtTen = new JTextField("");
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"PHUC_VU", "THU_NGAN", "BEP", "QUAN_LY", "QUAN_TRI"});
        JTextField txtSDT = new JTextField("");
        JTextField txtCa = new JTextField("Sáng");
        JTextField txtLuong = new JTextField("6000000");

        form.add(new JLabel("Tên đăng nhập:")); form.add(txtTK);
        form.add(new JLabel("Mật khẩu:")); form.add(txtMK);
        form.add(new JLabel("Họ và tên:")); form.add(txtTen);
        form.add(new JLabel("Vai trò / Vị trí:")); form.add(cbVaiTro);
        form.add(new JLabel("Số điện thoại:")); form.add(txtSDT);
        form.add(new JLabel("Ca làm việc:")); form.add(txtCa);
        form.add(new JLabel("Lương (VNĐ):")); form.add(txtLuong);

        int r = JOptionPane.showConfirmDialog(this, form, "THÊM NHÂN VIÊN MỚI", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String tk = txtTK.getText().trim();
            String ten = txtTen.getText().trim();
            String mk = new String(txtMK.getPassword()).trim();

            if (tk.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập và Họ tên là bắt buộc!");
                return;
            }
            if (nguoiDungDAO.tonTaiTenDangNhap(tk)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập \"" + tk + "\" đã tồn tại!");
                return;
            }

            NguoiDung nd = new NguoiDung();
            nd.setTenDangNhap(tk);
            nd.setMatKhau(mk.isEmpty() ? "123456" : mk);
            nd.setHoTen(ten);
            nd.setVaiTro((String) cbVaiTro.getSelectedItem());
            nd.setSoDienThoai(txtSDT.getText().trim());
            nd.setCaLam(txtCa.getText().trim());
            try {
                nd.setLuong(Double.parseDouble(txtLuong.getText().replaceAll("[^0-9]", "")));
            } catch (Exception ignore) {
                nd.setLuong(6000000);
            }
            nd.setTrangThai(true);

            boolean ok = nguoiDungDAO.themNhanVien(nd);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        }
    }

    private void moDialogSuaNhanVien(int rowIndex) {
        NguoiDung nd = layNVDangChon(rowIndex);
        if (nd == null) return;

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setPreferredSize(new Dimension(420, 250));

        JTextField txtTen = new JTextField(nd.getHoTen());
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"PHUC_VU", "THU_NGAN", "BEP", "QUAN_LY", "QUAN_TRI"});
        cbVaiTro.setSelectedItem(nd.getVaiTro());
        JTextField txtSDT = new JTextField(nd.getSoDienThoai() != null ? nd.getSoDienThoai() : "");
        JTextField txtEmail = new JTextField(nd.getEmail() != null ? nd.getEmail() : "");
        JTextField txtCa = new JTextField(nd.getCaLam() != null ? nd.getCaLam() : "Sáng");
        JTextField txtLuong = new JTextField(String.valueOf((long) nd.getLuong()));

        form.add(new JLabel("Họ và tên:")); form.add(txtTen);
        form.add(new JLabel("Vai trò / Chức vụ:")); form.add(cbVaiTro);
        form.add(new JLabel("Số điện thoại:")); form.add(txtSDT);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Ca làm việc:")); form.add(txtCa);
        form.add(new JLabel("Lương (VNĐ):")); form.add(txtLuong);

        int r = JOptionPane.showConfirmDialog(this, form, "SỬA NHÂN VIÊN — " + nd.getHoTen(), JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            nd.setHoTen(txtTen.getText().trim());
            nd.setVaiTro((String) cbVaiTro.getSelectedItem());
            nd.setSoDienThoai(txtSDT.getText().trim());
            nd.setEmail(txtEmail.getText().trim());
            nd.setCaLam(txtCa.getText().trim());
            try {
                nd.setLuong(Double.parseDouble(txtLuong.getText().replaceAll("[^0-9]", "")));
            } catch (Exception ignore) {}

            boolean ok = nguoiDungDAO.suaNhanVien(nd);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại!");
            }
        }
    }

    private void xuLyDoiMatKhau() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần đổi mật khẩu!");
            return;
        }
        NguoiDung nd = layNVDangChon(r);
        if (nd == null) return;

        String mkMoi = JOptionPane.showInputDialog(this,
                "Nhập mật khẩu mới cho \"" + nd.getHoTen() + "\" (" + nd.getTenDangNhap() + "):",
                "123456");
        if (mkMoi != null && !mkMoi.trim().isEmpty()) {
            boolean ok = nguoiDungDAO.doiMatKhau(nd.getMaNguoiDung(), mkMoi.trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã đổi mật khẩu thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!");
            }
        }
    }

    private void xuLyKhoaMo() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return;
        }
        NguoiDung nd = layNVDangChon(r);
        if (nd == null) return;

        boolean ttMoi = !nd.isTrangThai();
        boolean ok = nguoiDungDAO.doiTrangThai(nd.getMaNguoiDung(), ttMoi);
        if (ok) {
            nd.setTrangThai(ttMoi);
            locVaHienThi();
            JOptionPane.showMessageDialog(this, (ttMoi ? "Đã mở khóa tài khoản!" : "Đã khóa tài khoản!"));
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại!");
        }
    }

    private void xuLyXoaNhanVien() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        NguoiDung nd = layNVDangChon(r);
        if (nd == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên \"" + nd.getHoTen() + "\" (" + nd.getTenDangNhap() + ")?\n"
                        + "Lưu ý: Nếu nhân viên đã có lịch sử đơn hàng, tài khoản sẽ chuyển sang trạng thái Khóa.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = nguoiDungDAO.xoaNhanVien(nd.getMaNguoiDung());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã xóa (hoặc khóa tài khoản) thành công!");
                napDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên!");
            }
        }
    }
}
