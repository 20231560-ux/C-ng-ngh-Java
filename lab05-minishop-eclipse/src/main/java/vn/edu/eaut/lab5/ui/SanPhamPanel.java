package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.DanhMucBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class SanPhamPanel extends JPanel {
    private final SanPhamBUS bus = new SanPhamBUS();
    private final DanhMucBUS dmBus = new DanhMucBUS();
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtPrice = new JTextField();
    private final JTextField txtQty = new JTextField();
    private final JTextField txtSearch = new JTextField();
    private final JTextField txtMinP = new JTextField();
    private final JTextField txtMaxP = new JTextField();
    private final JTextField txtMinQ = new JTextField();
    private final JTextField txtMaxQ = new JTextField();
    private final JComboBox<Item> cboDm = new JComboBox<>();
    private final JComboBox<Item> cboDmSearch = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã", "Tên sản phẩm", "Đơn giá", "Tồn kho", "Danh mục"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private int page = 0;

    public SanPhamPanel(String role) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        txtId.setEditable(false);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(section("Thông tin sản phẩm"), BorderLayout.NORTH);
        top.add(buildInfoForm(), BorderLayout.CENTER);

        JPanel searchBox = new JPanel(new BorderLayout(8, 8));
        searchBox.add(section("Tìm kiếm nâng cao"), BorderLayout.NORTH);
        searchBox.add(buildSearchForm(), BorderLayout.CENTER);
        top.add(searchBox, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildButtons(role), BorderLayout.SOUTH);

        loadCategories();
        load();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fill();
        });
    }

    private JLabel section(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 15f));
        l.setBorder(new EmptyBorder(3, 3, 3, 3));
        return l;
    }

    private JPanel buildInfoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = base();
        addPair(p, g, 0, 0, "Mã sản phẩm:", txtId, "Tên sản phẩm:", txtName);
        addPair(p, g, 0, 1, "Đơn giá:", txtPrice, "Số lượng:", txtQty);
        addPair(p, g, 0, 2, "Danh mục:", cboDm, null, null);
        return p;
    }

    private JPanel buildSearchForm() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = base();
        addPair(p, g, 0, 0, "Tên sản phẩm:", txtSearch, "Giá từ:", txtMinP);
        addPair(p, g, 0, 1, "Giá đến:", txtMaxP, "SL từ:", txtMinQ);
        addPair(p, g, 0, 2, "SL đến:", txtMaxQ, "Danh mục:", cboDmSearch);
        return p;
    }

    private JPanel buildButtons(String role) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        JButton add = new JButton("Thêm");
        JButton edit = new JButton("Sửa");
        JButton del = new JButton("Xóa");
        JButton clear = new JButton("Làm mới");
        JButton search = new JButton("Tìm kiếm");
        JButton prev = new JButton("Trước");
        JButton next = new JButton("Sau");
        JButton cat = new JButton("Quản lý danh mục");
        for (JButton b : new JButton[]{add, edit, del, clear, search, prev, next, cat}) p.add(b);

        add.addActionListener(e -> save(false));
        edit.addActionListener(e -> save(true));
        del.addActionListener(e -> delete());
        clear.addActionListener(e -> clearForm());
        search.addActionListener(e -> { page = 0; load(); });
        prev.addActionListener(e -> { if (page > 0) { page--; load(); } });
        next.addActionListener(e -> { page++; load(); });
        cat.addActionListener(e -> categoryDialog());
        if ("NHANVIEN".equals(role)) del.setEnabled(false);
        return p;
    }

    private GridBagConstraints base() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 5, 3, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weighty = 0;
        return g;
    }

    private void addPair(JPanel p, GridBagConstraints g, int x, int y,
                         String label1, JComponent field1, String label2, JComponent field2) {
        g.gridy = y; g.gridx = x; g.weightx = 0;
        p.add(new JLabel(label1), g);
        g.gridx = x + 1; g.weightx = 0.5;
        p.add(field1, g);
        if (label2 != null) {
            g.gridx = x + 2; g.weightx = 0;
            p.add(new JLabel(label2), g);
            g.gridx = x + 3; g.weightx = 0.5;
            p.add(field2, g);
        }
    }

    private void loadCategories() {
        cboDm.removeAllItems();
        cboDmSearch.removeAllItems();
        Item all = new Item(0, "-- Tất cả / Không chọn --");
        cboDm.addItem(new Item(0, "-- Không chọn --"));
        cboDmSearch.addItem(all);
        try {
            for (Object[] x : dmBus.findAll()) {
                Item item = new Item((Integer) x[0], (String) x[1]);
                cboDm.addItem(item);
                cboDmSearch.addItem(new Item((Integer) x[0], (String) x[1]));
            }
        } catch (Exception e) { MessageUtil.error(this, e.getMessage()); }
    }

    private void load() {
        try {
            BigDecimal min = bd(txtMinP.getText()), max = bd(txtMaxP.getText());
            Integer minQ = num(txtMinQ.getText()), maxQ = num(txtMaxQ.getText());
            Item item = (Item) cboDmSearch.getSelectedItem();
            int dm = item == null ? 0 : item.id;
            List<SanPham> list = bus.search(txtSearch.getText(), min, max, minQ, maxQ, dm, page);
            model.setRowCount(0);
            for (SanPham s : list) model.addRow(new Object[]{s.getMaSp(), s.getTenSp(), s.getDonGia(), s.getSoLuong(), s.getTenDm()});
        } catch (Exception e) { MessageUtil.error(this, "Lỗi tìm kiếm: " + e.getMessage()); }
    }

    private BigDecimal bd(String s) { return s == null || s.isBlank() ? null : new BigDecimal(s.trim()); }
    private Integer num(String s) { return s == null || s.isBlank() ? null : Integer.valueOf(s.trim()); }

    private void fill() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        int mr = table.convertRowIndexToModel(r);
        txtId.setText(model.getValueAt(mr, 0).toString());
        txtName.setText(model.getValueAt(mr, 1).toString());
        txtPrice.setText(model.getValueAt(mr, 2).toString());
        txtQty.setText(model.getValueAt(mr, 3).toString());
    }

    private void save(boolean edit) {
        try {
            SanPham s = new SanPham();
            if (edit) {
                if (txtId.getText().isBlank()) throw new IllegalArgumentException("Hãy chọn sản phẩm cần sửa");
                s.setMaSp(Integer.parseInt(txtId.getText()));
            }
            s.setTenSp(txtName.getText());
            s.setDonGia(new BigDecimal(txtPrice.getText()));
            s.setSoLuong(Integer.parseInt(txtQty.getText()));
            Item i = (Item) cboDm.getSelectedItem();
            s.setMaDm(i == null ? 0 : i.id);
            bus.save(s);
            MessageUtil.info(this, edit ? "Sửa sản phẩm thành công" : "Thêm sản phẩm thành công");
            clearForm(); load();
        } catch (Exception e) { MessageUtil.error(this, e.getMessage()); }
    }

    private void delete() {
        try {
            if (txtId.getText().isBlank()) throw new IllegalArgumentException("Hãy chọn sản phẩm cần xóa");
            if (MessageUtil.confirm(this, "Bạn có chắc muốn xóa sản phẩm này?")) {
                bus.delete(Integer.parseInt(txtId.getText())); clearForm(); load();
            }
        } catch (Exception e) { MessageUtil.error(this, "Không xóa được: " + e.getMessage()); }
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPrice.setText(""); txtQty.setText("");
        txtSearch.setText(""); txtMinP.setText(""); txtMaxP.setText(""); txtMinQ.setText(""); txtMaxQ.setText("");
        if (cboDm.getItemCount() > 0) cboDm.setSelectedIndex(0);
        if (cboDmSearch.getItemCount() > 0) cboDmSearch.setSelectedIndex(0);
        table.clearSelection(); page = 0; loadCategories();
    }

    private void categoryDialog() {
        JTextField n = new JTextField();
        if (JOptionPane.showConfirmDialog(this, n, "Tên danh mục mới", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { dmBus.insert(n.getText()); loadCategories(); load(); }
            catch (Exception e) { MessageUtil.error(this, e.getMessage()); }
        }
    }

    private static class Item {
        int id; String name;
        Item(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}
