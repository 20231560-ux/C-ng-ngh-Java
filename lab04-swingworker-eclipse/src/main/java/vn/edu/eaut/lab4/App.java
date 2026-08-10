package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame menu = new JFrame("LAB 4 - Java SwingWorker");
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setSize(560, 520);
            menu.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(10, 1, 8, 8));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

            String[] names = {
                "Bài 1 - Đồng hồ đếm ngược",
                "Bài 2 - Mô phỏng tải dữ liệu",
                "Bài 3 - Tổng số nguyên tố < N",
                "Bài 4 - Fibonacci bằng memoization",
                "Bài 5 - Đếm số dòng file",
                "Bài 6 - Hủy tác vụ tải dữ liệu",
                "Bài 7 - Tìm từ khóa trong file",
                "Bài 8 - Đọc CSV điểm sinh viên",
                "Bài 9 - Tải danh sách sản phẩm",
                "Bài 10 - Quản lý sản phẩm CSV"
            };

            JButton[] buttons = new JButton[names.length];
            for (int i = 0; i < names.length; i++) {
                final int index = i;
                buttons[i] = new JButton(names[i]);
                panel.add(buttons[i]);
                buttons[i].addActionListener(e -> open(index));
            }

            menu.add(panel);
            menu.setVisible(true);
        });
    }

    private static void open(int index) {
        switch (index) {
            case 0 -> new CountdownFrame().setVisible(true);
            case 1 -> new ProgressDemoFrame().setVisible(true);
            case 2 -> new PrimeSumFrame().setVisible(true);
            case 3 -> new FibonacciFrame().setVisible(true);
            case 4 -> new FileLineCounterFrame().setVisible(true);
            case 5 -> new CancelTaskFrame().setVisible(true);
            case 6 -> new KeywordSearchFrame().setVisible(true);
            case 7 -> new CsvStudentFrame().setVisible(true);
            case 8 -> new ProductLoadFrame().setVisible(true);
            case 9 -> new ProductCsvFrame().setVisible(true);
        }
    }
}
