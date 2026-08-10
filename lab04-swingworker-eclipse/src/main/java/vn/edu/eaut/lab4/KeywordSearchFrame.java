package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class KeywordSearchFrame extends JFrame {
    private File file;
    private JTextField txtKeyword = new JTextField();
    private JButton btnChoose = new JButton("Chọn file .txt");
    private JButton btnSearch = new JButton("Tìm");
    private JTextArea area = new JTextArea();
    private JLabel result = new JLabel("Chưa tìm kiếm");

    public KeywordSearchFrame() {
        setTitle("Bài 7 - Tìm từ khóa trong file");
        setSize(800,520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        area.setEditable(false);

        JPanel top = new JPanel(new GridLayout(2,2,8,8));
        top.add(btnChoose); top.add(new JLabel("Từ khóa:")); top.add(txtKeyword); top.add(btnSearch);
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        root.add(top,BorderLayout.NORTH); root.add(new JScrollPane(area),BorderLayout.CENTER); root.add(result,BorderLayout.SOUTH);
        add(root);

        btnChoose.addActionListener(e -> choose());
        btnSearch.addActionListener(e -> search());
    }

    private void choose() {
        JFileChooser c=new JFileChooser();
        if(c.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) file=c.getSelectedFile();
    }

    private void search() {
        if(file==null){JOptionPane.showMessageDialog(this,"Hãy chọn file trước");return;}
        String key=txtKeyword.getText().trim().toLowerCase();
        if(key.isEmpty()){JOptionPane.showMessageDialog(this,"Hãy nhập từ khóa");return;}
        btnSearch.setEnabled(false); area.setText(""); result.setText("Đang tìm...");
        SwingWorker<Integer,String> worker=new SwingWorker<>() {
            protected Integer doInBackground() throws Exception {
                int count=0, lineNo=0;
                try(BufferedReader r=Files.newBufferedReader(file.toPath(),StandardCharsets.UTF_8)){
                    String line;
                    while((line=r.readLine())!=null){
                        lineNo++;
                        if(line.toLowerCase().contains(key)){
                            count++;
                            publish("Dòng "+lineNo+": "+line);
                        }
                    }
                }
                return count;
            }
            protected void process(java.util.List<String> chunks) {
                for(String s:chunks) area.append(s+"\n");
            }
            protected void done() {
                try{result.setText("Tìm thấy "+get()+" dòng chứa từ khóa");}
                catch(Exception ex){result.setText("Lỗi đọc file");}
                btnSearch.setEnabled(true);
            }
        };
        worker.execute();
    }
}
