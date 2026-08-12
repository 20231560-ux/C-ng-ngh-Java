package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.util.MessageUtil;
import vn.edu.eaut.lab5.util.PhoneDocumentFilter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private final KhachHangBUS bus = new KhachHangBUS();
    private final JTextField id = new JTextField(), name = new JTextField(), phone = new JTextField(), address = new JTextField(), search = new JTextField();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã", "Tên khách hàng", "Số điện thoại", "Địa chỉ"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10)); setBorder(new EmptyBorder(10, 10, 10, 10));
        id.setEditable(false);
        ((AbstractDocument) phone.getDocument()).setDocumentFilter(new PhoneDocumentFilter());
        table.setRowHeight(25); table.setAutoCreateRowSorter(true);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(title("Thông tin khách hàng"), BorderLayout.NORTH);
        top.add(info(), BorderLayout.CENTER);
        JPanel searchBox = new JPanel(new BorderLayout(8, 8));
        searchBox.add(title("Tìm kiếm"), BorderLayout.NORTH);
        JPanel sf = new JPanel(new GridBagLayout()); GridBagConstraints g = base();
        pair(sf, g, 0, 0, "Tìm theo tên / SĐT / địa chỉ:", search);
        searchBox.add(sf, BorderLayout.CENTER); top.add(searchBox, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(buttons(), BorderLayout.SOUTH);
        load();
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) fill(); });
    }

    private JLabel title(String s) { JLabel l = new JLabel(s); l.setFont(l.getFont().deriveFont(Font.BOLD, 15f)); return l; }
    private JPanel info() {
        JPanel p = new JPanel(new GridBagLayout()); GridBagConstraints g = base();
        pair4(p,g,0,0,"Mã khách hàng:",id,"Tên khách hàng:",name);
        pair4(p,g,0,1,"Số điện thoại:",phone,"Địa chỉ:",address);
        return p;
    }
    private JPanel buttons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,8,3));
        JButton add=new JButton("Thêm"), edit=new JButton("Sửa"), del=new JButton("Xóa"), find=new JButton("Tìm kiếm"), clear=new JButton("Làm mới");
        for(JButton b:new JButton[]{add,edit,del,find,clear})p.add(b);
        add.addActionListener(e->save(false)); edit.addActionListener(e->save(true)); del.addActionListener(e->delete());
        find.addActionListener(e->load()); clear.addActionListener(e->{name.setText("");phone.setText("");address.setText("");search.setText("");id.setText("");load();});
        return p;
    }
    private GridBagConstraints base(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(3,5,3,5);g.fill=GridBagConstraints.HORIZONTAL;return g;}
    private void pair(JPanel p,GridBagConstraints g,int x,int y,String label,JComponent field){g.gridy=y;g.gridx=x;g.weightx=0;p.add(new JLabel(label),g);g.gridx=x+1;g.weightx=1;p.add(field,g);}
    private void pair4(JPanel p,GridBagConstraints g,int x,int y,String l1,JComponent f1,String l2,JComponent f2){g.gridy=y;g.gridx=x;g.weightx=0;p.add(new JLabel(l1),g);g.gridx=x+1;g.weightx=.5;p.add(f1,g);g.gridx=x+2;g.weightx=0;p.add(new JLabel(l2),g);g.gridx=x+3;g.weightx=.5;p.add(f2,g);}

    private void load(){try{List<KhachHang> l=search.getText().isBlank()?bus.findAll():bus.search(search.getText());model.setRowCount(0);for(KhachHang k:l)model.addRow(new Object[]{k.getMaKh(),k.getTenKh(),k.getSdt(),k.getDiaChi()});}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
    private void fill(){int r=table.getSelectedRow();if(r<0)return;int m=table.convertRowIndexToModel(r);id.setText(model.getValueAt(m,0).toString());name.setText(model.getValueAt(m,1).toString());phone.setText(model.getValueAt(m,2).toString());address.setText(model.getValueAt(m,3).toString());}
    private void save(boolean edit){try{KhachHang k=new KhachHang();if(edit){if(id.getText().isBlank())throw new IllegalArgumentException("Hãy chọn khách hàng cần sửa");k.setMaKh(Integer.parseInt(id.getText()));}k.setTenKh(name.getText());k.setSdt(phone.getText());k.setDiaChi(address.getText());bus.save(k);MessageUtil.info(this,edit?"Sửa khách hàng thành công":"Thêm khách hàng thành công");clearFields();load();}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
    private void delete(){try{if(id.getText().isBlank())throw new IllegalArgumentException("Hãy chọn khách hàng cần xóa");if(MessageUtil.confirm(this,"Bạn có chắc muốn xóa khách hàng này?")){bus.delete(Integer.parseInt(id.getText()));clearFields();load();}}catch(Exception e){MessageUtil.error(this,"Không xóa được: "+e.getMessage());}}
    private void clearFields(){id.setText("");name.setText("");phone.setText("");address.setText("");table.clearSelection();}
}
