package com.restaurant;

import javax.swing.SwingUtilities;

import com.restaurant.view.DangNhapFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            DangNhapFrame frame =
                    new DangNhapFrame();

            frame.setVisible(true);
        });
    }
}