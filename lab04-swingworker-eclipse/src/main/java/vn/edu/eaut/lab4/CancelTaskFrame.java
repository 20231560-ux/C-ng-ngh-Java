package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class CancelTaskFrame extends JFrame {
    private JButton btnStart = new JButton("Bắt đầu tải");
    private JButton btnCancel = new JButton("Hủy");
    private JProgressBar progress = new JProgressBar(0,100);
    private JLabel status = new JLabel("Chưa chạy");
    private SwingWorker<Void,Void> worker;

    public CancelTaskFrame() {
        setTitle("Bài 6 - Hủy tác vụ");
        setSize(520,220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        progress.setStringPainted(true);
        btnCancel.setEnabled(false);

        JPanel p = new JPanel(new GridLayout(4,1,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        p.add(btnStart); p.add(btnCancel); p.add(progress); p.add(status); add(p);
        btnStart.addActionListener(e -> start());
        btnCancel.addActionListener(e -> cancel());
    }

    private void start() {
        btnStart.setEnabled(false); btnCancel.setEnabled(true); progress.setValue(0);
        status.setText("Đang chạy...");
        worker = new SwingWorker<>() {
            protected Void doInBackground() throws Exception {
                for (int i=0;i<=100;i++) {
                    if (isCancelled()) return null;
                    setProgress(i);
                    Thread.sleep(100);
                }
                return null;
            }
            protected void done() {
                if (isCancelled()) status.setText("Đã hủy tác vụ");
                else { progress.setValue(100); status.setText("Hoàn thành"); }
                btnStart.setEnabled(true); btnCancel.setEnabled(false);
            }
        };
        worker.addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName()))
                progress.setValue((int)e.getNewValue());
        });
        worker.execute();
    }

    private void cancel() {
        if (worker != null) worker.cancel(true);
    }
}
