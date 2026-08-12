package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.ExportUtil;
import vn.edu.eaut.lab5.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private final KhachHangBUS khBus = new KhachHangBUS();
    private final SanPhamBUS spBus = new SanPhamBUS();
    private final HoaDonBUS hdBus = new HoaDonBUS();
    private final JComboBox<KhachHang> cboKh = new JComboBox<>();
    private final JComboBox<SanPham> cboSp = new JComboBox<>();
    private final JTextField qty = new JTextField("1");
    private final JLabel total = new JLabel("Tổng tiền: 0 VND");
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã SP", "Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
        @Override public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table = new JTable(model);
    private final List<ChiTietHoaDon> items = new ArrayList<>();
    private KhachHang lastCustomer;
    private int lastInvoiceId = 0;

    public HoaDonPanel() {
        setLayout(new BorderLayout(10,10)); setBorder(new EmptyBorder(10,10,10,10)); table.setRowHeight(25);
        add(buildTop(), BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(buildBottom(), BorderLayout.SOUTH); loadCombos();
    }
    private JPanel buildTop(){
        JPanel outer=new JPanel(new BorderLayout(8,8)); JLabel t=new JLabel("Thông tin hóa đơn");t.setFont(t.getFont().deriveFont(Font.BOLD,15f));outer.add(t,BorderLayout.NORTH);
        JPanel p=new JPanel(new GridBagLayout());GridBagConstraints g=base();
        g.gridy=0;g.gridx=0;g.weightx=0;p.add(new JLabel("Khách hàng:"),g);g.gridx=1;g.weightx=.5;p.add(cboKh,g);
        g.gridx=2;g.weightx=0;p.add(new JLabel("Sản phẩm:"),g);g.gridx=3;g.weightx=.5;p.add(cboSp,g);
        g.gridy=1;g.gridx=0;g.weightx=0;p.add(new JLabel("Số lượng:"),g);g.gridx=1;g.weightx=.5;p.add(qty,g);
        JButton add=new JButton("Thêm sản phẩm vào hóa đơn");g.gridx=2;g.weightx=0;p.add(add,g);JButton save=new JButton("Lưu hóa đơn");g.gridx=3;g.weightx=.5;p.add(save,g);
        add.addActionListener(e->addItem());save.addActionListener(e->saveInvoice());outer.add(p,BorderLayout.CENTER);return outer;
    }
    private JPanel buildBottom(){
        JPanel p=new JPanel(new BorderLayout());total.setFont(total.getFont().deriveFont(Font.BOLD,16f));p.add(total,BorderLayout.WEST);
        JPanel b=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,3));JButton remove=new JButton("Xóa dòng");JButton clear=new JButton("Hủy hóa đơn");JButton txt=new JButton("Xuất TXT");JButton csv=new JButton("Xuất CSV");
        for(JButton x:new JButton[]{remove,clear,txt,csv})b.add(x);p.add(b,BorderLayout.EAST);
        remove.addActionListener(e->removeItem());clear.addActionListener(e->clearInvoice());txt.addActionListener(e->export(false));csv.addActionListener(e->export(true));return p;
    }
    private GridBagConstraints base(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(4,5,4,5);g.fill=GridBagConstraints.HORIZONTAL;return g;}
    private void loadCombos(){try{cboKh.removeAllItems();cboSp.removeAllItems();for(KhachHang k:khBus.findAll())cboKh.addItem(k);for(SanPham s:spBus.findAll())cboSp.addItem(s);}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
    private void addItem(){try{SanPham s=(SanPham)cboSp.getSelectedItem();int q=Integer.parseInt(qty.getText());if(s==null)throw new IllegalArgumentException("Chưa chọn sản phẩm");if(q<=0)throw new IllegalArgumentException("Số lượng phải lớn hơn 0");if(q>s.getSoLuong())throw new IllegalArgumentException("Tồn kho không đủ");for(ChiTietHoaDon old:items)if(old.getMaSp()==s.getMaSp())throw new IllegalArgumentException("Sản phẩm này đã có trong hóa đơn");ChiTietHoaDon c=new ChiTietHoaDon(s.getMaSp(),s.getTenSp(),q,s.getDonGia());items.add(c);model.addRow(new Object[]{c.getMaSp(),c.getTenSp(),c.getSoLuong(),c.getDonGia(),c.getThanhTien()});calc();}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
    private void removeItem(){int r=table.getSelectedRow();if(r<0){MessageUtil.error(this,"Hãy chọn dòng cần xóa");return;}int m=table.convertRowIndexToModel(r);items.remove(m);model.removeRow(m);calc();}
    private void calc(){BigDecimal t=BigDecimal.ZERO;for(ChiTietHoaDon c:items)t=t.add(c.getThanhTien());total.setText("Tổng tiền: "+t+" VND");}
    private void saveInvoice(){try{KhachHang k=(KhachHang)cboKh.getSelectedItem();if(k==null)throw new IllegalArgumentException("Hãy chọn khách hàng");if(items.isEmpty())throw new IllegalArgumentException("Hóa đơn chưa có sản phẩm");lastCustomer=k;lastInvoiceId=hdBus.save(k.getMaKh(),items);MessageUtil.info(this,"Đã lưu hóa đơn #"+lastInvoiceId);items.clear();model.setRowCount(0);calc();loadCombos();}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
    private void clearInvoice(){items.clear();model.setRowCount(0);lastInvoiceId=0;calc();}
    private void export(boolean csv){try{if(lastInvoiceId<=0||lastCustomer==null)throw new IllegalArgumentException("Hãy lưu hóa đơn trước khi xuất file");List<ChiTietHoaDon> exportItems=items;if(exportItems.isEmpty())throw new IllegalArgumentException("Sau khi lưu, danh sách chi tiết đã được xóa. Hãy lập hóa đơn mới để xuất file ngay sau khi lưu.");java.nio.file.Path p=csv?ExportUtil.exportCsv(lastInvoiceId,lastCustomer,exportItems):ExportUtil.exportTxt(lastInvoiceId,lastCustomer,exportItems);MessageUtil.info(this,"Đã xuất: "+p.toAbsolutePath());}catch(Exception e){MessageUtil.error(this,e.getMessage());}}
}
