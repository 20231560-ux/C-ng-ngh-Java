package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCsvFrame extends JFrame {
    private JTextField txtCode=new JTextField(),txtName=new JTextField(),txtPrice=new JTextField();
    private JButton add=new JButton("Thêm"),edit=new JButton("Sửa"),del=new JButton("Xóa"),open=new JButton("Đọc CSV"),save=new JButton("Lưu CSV");
    private JTable table=new JTable(new DefaultTableModel(new Object[]{"Mã SP","Tên SP","Đơn giá"},0));
    private JLabel status=new JLabel("Sẵn sàng");

    public ProductCsvFrame(){
        setTitle("Bài 10 - Quản lý sản phẩm bằng CSV");
        setSize(850,520);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);setLocationRelativeTo(null);

        JPanel form=new JPanel(new GridLayout(2,3,6,6));
        form.add(new JLabel("Mã SP"));form.add(new JLabel("Tên SP"));form.add(new JLabel("Đơn giá"));
        form.add(txtCode);form.add(txtName);form.add(txtPrice);
        JPanel buttons=new JPanel();buttons.add(add);buttons.add(edit);buttons.add(del);buttons.add(open);buttons.add(save);
        JPanel north=new JPanel(new BorderLayout());north.add(form,BorderLayout.CENTER);north.add(buttons,BorderLayout.SOUTH);
        add(north,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);

        add.addActionListener(e->addRow());edit.addActionListener(e->editRow());del.addActionListener(e->deleteRow());
        open.addActionListener(e->readCsv());save.addActionListener(e->saveCsv());
        table.getSelectionModel().addListSelectionListener(e->fillForm());
    }

    private DefaultTableModel model(){return (DefaultTableModel)table.getModel();}
    private void addRow(){
        if(txtCode.getText().isBlank()||txtName.getText().isBlank()||txtPrice.getText().isBlank()){JOptionPane.showMessageDialog(this,"Nhập đủ thông tin");return;}
        try{Double.parseDouble(txtPrice.getText().trim());}catch(Exception e){JOptionPane.showMessageDialog(this,"Đơn giá phải là số");return;}
        model().addRow(new Object[]{txtCode.getText().trim(),txtName.getText().trim(),txtPrice.getText().trim()});clear();
    }
    private void editRow(){
        int r=table.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Chọn dòng cần sửa");return;}
        model().setValueAt(txtCode.getText().trim(),r,0);model().setValueAt(txtName.getText().trim(),r,1);model().setValueAt(txtPrice.getText().trim(),r,2);
    }
    private void deleteRow(){
        int r=table.getSelectedRow();if(r>=0)model().removeRow(r);else JOptionPane.showMessageDialog(this,"Chọn dòng cần xóa");
    }
    private void fillForm(){
        int r=table.getSelectedRow();if(r>=0){txtCode.setText(""+model().getValueAt(r,0));txtName.setText(""+model().getValueAt(r,1));txtPrice.setText(""+model().getValueAt(r,2));}
    }
    private void clear(){txtCode.setText("");txtName.setText("");txtPrice.setText("");}

    private void saveCsv(){
        JFileChooser c=new JFileChooser();
        if(c.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        File f=c.getSelectedFile();
        status.setText("Đang lưu...");
        SwingWorker<Void,Void> w=new SwingWorker<>(){
            protected Void doInBackground() throws Exception{
                try(PrintWriter out=new PrintWriter(Files.newBufferedWriter(f.toPath(),StandardCharsets.UTF_8))){
                    out.println("MaSP,TenSP,DonGia");
                    for(int i=0;i<model().getRowCount();i++)
                        out.println(model().getValueAt(i,0)+","+model().getValueAt(i,1)+","+model().getValueAt(i,2));
                }return null;
            }
            protected void done(){status.setText("Đã lưu CSV: "+f.getAbsolutePath());}
        };w.execute();
    }

    private void readCsv(){
        JFileChooser c=new JFileChooser();
        if(c.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        File f=c.getSelectedFile();status.setText("Đang đọc...");
        SwingWorker<List<Object[]>,Void> w=new SwingWorker<>(){
            protected List<Object[]> doInBackground() throws Exception{
                List<Object[]> rows=new ArrayList<>();
                try(BufferedReader r=Files.newBufferedReader(f.toPath(),StandardCharsets.UTF_8)){
                    String line;boolean first=true;
                    while((line=r.readLine())!=null){
                        if(first){first=false;continue;}
                        String[] a=line.split(",",-1);if(a.length>=3)rows.add(new Object[]{a[0],a[1],a[2]});
                    }
                }return rows;
            }
            protected void done(){
                try{model().setRowCount(0);for(Object[] r:get())model().addRow(r);status.setText("Đọc CSV hoàn tất");}
                catch(Exception e){status.setText("Lỗi đọc CSV");}
            }
        };w.execute();
    }
}
