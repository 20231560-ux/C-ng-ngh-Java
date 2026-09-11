package com.restaurant.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerRealtimeTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Savoré - Manager Realtime");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1250, 760);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            ManagerRealtimePanel panel = new ManagerRealtimePanel();

            frame.add(panel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}