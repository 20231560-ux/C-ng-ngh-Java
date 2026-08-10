package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CountdownFrame extends JFrame {
    private JTextField txtSeconds = new JTextField(10);
    private JButton btnStart = new JButton("Bắt đầu");
    private JLabel lblTime = new JLabel("Thời gian còn lại: ");

    public CountdownFrame() {
        setTitle("Bài 1 - Đồng hồ đếm ngược");
        setSize(420, 190);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        lblTime.setFont(new Font("Arial", Font.BOLD, 20));
        JPanel p = new JPanel(new GridLayout(3, 1, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.add(txtSeconds); p.add(btnStart); p.add(lblTime);
        add(p);

        btnStart.addActionListener(e -> startCountdown());
    }

    private void startCountdown() {
        final int seconds;
        try {
            seconds = Integer.parseInt(txtSeconds.getText().trim());
            if (seconds <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên > 0");
            return;
        }

        btnStart.setEnabled(false);
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            protected Void doInBackground() throws Exception {
                for (int i = seconds; i >= 0; i--) {
                    publish(i);
                    Thread.sleep(1000);
                }
                return null;
            }
            protected void process(List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                lblTime.setText("Thời gian còn lại: " + value + " giây");
            }
            protected void done() {
                btnStart.setEnabled(true);
                JOptionPane.showMessageDialog(CountdownFrame.this, "Hoàn thành!");
            }
        };
        worker.execute();
    }
}
