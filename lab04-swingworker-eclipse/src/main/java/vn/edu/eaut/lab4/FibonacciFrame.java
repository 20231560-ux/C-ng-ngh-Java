package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class FibonacciFrame extends JFrame {
    private JTextField txtN = new JTextField();
    private JButton btnFind = new JButton("Tìm");
    private JLabel lblResult = new JLabel("Kết quả: ");
    private JProgressBar progressBar = new JProgressBar();

    public FibonacciFrame() {
        setTitle("Bài 4 - Fibonacci bằng memoization");
        setSize(650, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel p = new JPanel(new GridLayout(4,1,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        p.add(txtN); p.add(btnFind); p.add(progressBar); p.add(lblResult); add(p);
        btnFind.addActionListener(e -> findFibonacci());
    }

    private BigInteger fibonacci(int n, Map<Integer,BigInteger> memo) {
        if (n <= 1) return BigInteger.valueOf(n);
        if (memo.containsKey(n)) return memo.get(n);
        BigInteger value = fibonacci(n-1,memo).add(fibonacci(n-2,memo));
        memo.put(n,value);
        return value;
    }

    private void findFibonacci() {
        final int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "N phải là số nguyên >= 0");
            return;
        }

        btnFind.setEnabled(false);
        progressBar.setIndeterminate(true);
        lblResult.setText("Đang tính Fibonacci...");
        SwingWorker<BigInteger,Void> worker = new SwingWorker<>() {
            protected BigInteger doInBackground() {
                return fibonacci(n,new HashMap<>());
            }
            protected void done() {
                try { lblResult.setText("Fibonacci(" + n + ") = " + get()); }
                catch (Exception ex) { lblResult.setText("Có lỗi khi tính Fibonacci"); }
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                btnFind.setEnabled(true);
            }
        };
        worker.execute();
    }
}
