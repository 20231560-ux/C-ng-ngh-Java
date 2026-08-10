package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class ProgressDemoFrame extends JFrame {
    private JButton btnLoad = new JButton("Tải dữ liệu");
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JLabel lblStatus = new JLabel("Chưa tải dữ liệu");

    public ProgressDemoFrame() {
        setTitle("Bài 2 - Mô phỏng tải dữ liệu");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        progressBar.setStringPainted(true);

        JPanel p = new JPanel(new GridLayout(3, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.add(btnLoad); p.add(progressBar); p.add(lblStatus); add(p);
        btnLoad.addActionListener(e -> loadData());
    }

    private void loadData() {
        btnLoad.setEnabled(false);
        progressBar.setValue(0);
        lblStatus.setText("Đang tải dữ liệu...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i += 10) {
                    setProgress(i);
                    Thread.sleep(1000);
                }
                return null;
            }
            protected void done() {
                progressBar.setValue(100);
                lblStatus.setText("Tải dữ liệu hoàn tất");
                btnLoad.setEnabled(true);
            }
        };
        worker.addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName()))
                progressBar.setValue((int)e.getNewValue());
        });
        worker.execute();
    }
}
