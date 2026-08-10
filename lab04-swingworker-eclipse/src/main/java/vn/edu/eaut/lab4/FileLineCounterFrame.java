package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileLineCounterFrame extends JFrame {
    private File selectedFile;
    private JLabel lblFile = new JLabel("Chưa chọn file");
    private JLabel lblResult = new JLabel("Số dòng: ");
    private JButton btnChoose = new JButton("Chọn file");
    private JButton btnCount = new JButton("Đếm dòng");
    private JProgressBar progressBar = new JProgressBar(0,100);

    public FileLineCounterFrame() {
        setTitle("Bài 5 - Đọc file và đếm số dòng");
        setSize(750,260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        progressBar.setStringPainted(true);

        JPanel p = new JPanel(new GridLayout(5,1,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        p.add(btnChoose); p.add(lblFile); p.add(btnCount); p.add(progressBar); p.add(lblResult); add(p);
        btnChoose.addActionListener(e -> chooseFile());
        btnCount.addActionListener(e -> countLines());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
        }
    }

    private void countLines() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file trước");
            return;
        }
        btnCount.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Đang đọc file...");
        SwingWorker<Long,Void> worker = new SwingWorker<>() {
            protected Long doInBackground() throws Exception {
                long totalBytes=Files.size(selectedFile.toPath()), readBytes=0, lines=0;
                try (BufferedReader r=Files.newBufferedReader(selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line=r.readLine())!=null) {
                        lines++;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + 1;
                        int progress = totalBytes==0 ? 100 : (int)Math.min(100, readBytes*100/totalBytes);
                        setProgress(progress);
                    }
                }
                return lines;
            }
            protected void done() {
                try { lblResult.setText("Số dòng: " + get()); }
                catch (Exception ex) { lblResult.setText("Lỗi khi đọc file"); }
                progressBar.setValue(100); btnCount.setEnabled(true);
            }
        };
        worker.addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName()))
                progressBar.setValue((int)e.getNewValue());
        });
        worker.execute();
    }
}
