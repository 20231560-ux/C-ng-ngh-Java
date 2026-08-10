package vn.edu.eaut.lab4;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class CsvStudentFrame extends JFrame {
    private JButton btnOpen = new JButton("Đọc CSV");
    private JTable table = new JTable(new DefaultTableModel(new Object[]{"Mã SV","Họ tên","Điểm"},0));
    private JLabel stat = new JLabel("Chưa có dữ liệu");

    public CsvStudentFrame() {
        setTitle("Bài 8 - Đọc CSV điểm sinh viên");
        setSize(650,420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel top=new JPanel(new BorderLayout());
        top.add(btnOpen,BorderLayout.WEST); top.add(stat,BorderLayout.CENTER);
        add(top,BorderLayout.NORTH); add(new JScrollPane(table),BorderLayout.CENTER);
        btnOpen.addActionListener(e->load());
    }

    private void load() {
        JFileChooser c=new JFileChooser();
        if(c.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        File f=c.getSelectedFile(); btnOpen.setEnabled(false);
        SwingWorker<List<String[]>,Void> w=new SwingWorker<>() {
            protected List<String[]> doInBackground() throws Exception {
                List<String[]> rows=new ArrayList<>();
                try(BufferedReader r=Files.newBufferedReader(f.toPath(),StandardCharsets.UTF_8)){
                    String line; boolean first=true;
                    while((line=r.readLine())!=null){
                        if(first){first=false;continue;}
                        String[] a=line.split(",");
                        if(a.length>=3) rows.add(new String[]{a[0].trim(),a[1].trim(),a[2].trim()});
                    }
                }
                return rows;
            }
            protected void done(){
                try{
                    List<String[]> rows=get();
                    DefaultTableModel m=(DefaultTableModel)table.getModel();m.setRowCount(0);
                    double sum=-0; String best=""; double max=-1;
                    for(String[] r:rows){
                        m.addRow(r); double d=Double.parseDouble(r[2]); sum+=d;
                        if(d>max){max=d;best=r[1];}
                    }
                    stat.setText(String.format("Số SV: %d | Điểm TB: %.2f | Cao nhất: %s (%.2f)",rows.size(),rows.isEmpty()?0:sum/rows.size(),best,max));
                }catch(Exception ex){JOptionPane.showMessageDialog(CsvStudentFrame.this,"Lỗi đọc CSV: "+ex.getMessage());}
                btnOpen.setEnabled(true);
            }
        };w.execute();
    }
}
