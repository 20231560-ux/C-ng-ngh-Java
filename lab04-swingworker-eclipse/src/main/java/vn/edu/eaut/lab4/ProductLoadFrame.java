package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductLoadFrame extends JFrame {

    private JButton btnLoad = new JButton("Tải sản phẩm");
    private JProgressBar progress = new JProgressBar(0, 100);
    private JLabel status = new JLabel("Chưa tải");

    private JTable table = new JTable(
            new DefaultTableModel(
                    new Object[]{"Mã SP", "Tên SP", "Đơn giá"}, 0
            )
    );

    private String[][] data = {
            {"SP01", "Bàn phím", "250000"},
            {"SP02", "Chuột", "150000"},
            {"SP03", "Màn hình", "2500000"}
    };

    public ProductLoadFrame() {

        setTitle("Bài 9 - Mô phỏng tải danh sách sản phẩm");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        progress.setStringPainted(true);

        JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));

        top.add(btnLoad);
        top.add(progress);
        top.add(status);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> load());
    }

    private void load() {

        btnLoad.setEnabled(false);
        progress.setValue(0);
        status.setText("Đang tải...");

        SwingWorker<Void, Integer> worker =
                new SwingWorker<Void, Integer>() {

            @Override
            protected Void doInBackground() throws Exception {

                for (int i = 0; i <= 100; i += 10) {

                    setProgress(i);

                    Thread.sleep(150);
                }

                return null;
            }

            @Override
            protected void done() {

                DefaultTableModel model =
                        (DefaultTableModel) table.getModel();

                model.setRowCount(0);

                for (String[] r : data) {
                    model.addRow(r);
                }

                progress.setValue(100);

                status.setText(
                        "Tải hoàn tất - "
                        + data.length
                        + " sản phẩm"
                );

                btnLoad.setEnabled(true);
            }
        };

        worker.addPropertyChangeListener(e -> {

            if ("progress".equals(e.getPropertyName())) {

                progress.setValue(
                        (int) e.getNewValue()
                );
            }
        });

        worker.execute();
    }
}