package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class PrimeSumFrame extends JFrame {
    private JTextField txtN = new JTextField();
    private JButton btnCalculate = new JButton("Tính");
    private JLabel lblResult = new JLabel("Kết quả: ");
    private JProgressBar progressBar = new JProgressBar(0,100);

    public PrimeSumFrame() {
        setTitle("Bài 3 - Tổng các số nguyên tố nhỏ hơn N");
        setSize(550, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        progressBar.setStringPainted(true);

        JPanel p = new JPanel(new GridLayout(4,1,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        p.add(txtN); p.add(btnCalculate); p.add(progressBar); p.add(lblResult); add(p);
        btnCalculate.addActionListener(e -> calculatePrimeSum());
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i=3; i <= Math.sqrt(n); i+=2)
            if (n % i == 0) return false;
        return true;
    }

    private void calculatePrimeSum() {
        final int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n <= 2) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "N phải là số nguyên > 2");
            return;
        }

        btnCalculate.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Đang tính...");
        SwingWorker<Long, Void> worker = new SwingWorker<>() {
            protected Long doInBackground() {
                long sum=0;
                for (int i=2; i<n; i++) {
                    if (isPrime(i)) sum += i;
                    setProgress((int)((i*100.0)/n));
                }
                return sum;
            }
            protected void done() {
                try { lblResult.setText("Tổng các số nguyên tố < " + n + " = " + get()); }
                catch (Exception ex) { lblResult.setText("Có lỗi khi tính toán"); }
                progressBar.setValue(100);
                btnCalculate.setEnabled(true);
            }
        };
        worker.addPropertyChangeListener(e -> {
            if ("progress".equals(e.getPropertyName()))
                progressBar.setValue((int)e.getNewValue());
        });
        worker.execute();
    }
}
