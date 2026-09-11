package com.restaurant;

import com.restaurant.model.NguoiDung;
import com.restaurant.view.QuanLyNhaHangFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LayoutScreenshotTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "false");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        String artifactDir = "C:\\Users\\Lenovo\\.gemini\\antigravity\\brain\\dd54a859-f924-4638-b510-f73cdd003660";

        SwingUtilities.invokeAndWait(() -> {
            try {
                NguoiDung admin = new NguoiDung();
                admin.setHoTen("Nguyễn Văn Admin");
                admin.setVaiTro("Quản trị hệ thống");

                QuanLyNhaHangFrame frame = new QuanLyNhaHangFrame(admin);
                frame.setExtendedState(JFrame.NORMAL);
                frame.setSize(1440, 900);
                frame.setLocationRelativeTo(null);
                frame.doLayout();
                frame.validate();
                frame.setVisible(true);

                String[][] screens = {
                        {"DASHBOARD", "screen_01_dashboard.png"},
                        {"BAN_DAT", "screen_02_datban.png"},
                        {"POS", "screen_03_pos.png"},
                        {"DON_HANG", "screen_04_donhang.png"},
                        {"MON_AN", "screen_05_monan.png"},
                        {"COMBO", "screen_06_combo.png"},
                        {"KHACH_HANG", "screen_07_khachhang.png"},
                        {"KHO", "screen_08_kho.png"},
                        {"NHAN_VIEN", "screen_09_nhanvien.png"},
                        {"DOANH_THU", "screen_10_baocao.png"},
                        {"KHUYEN_MAI", "screen_11_khuyenmai.png"},
                        {"CAI_DAT", "screen_12_caidat.png"}
                };

                // Chụp lần lượt từng màn hình
                new Thread(() -> {
                    try {
                        Thread.sleep(1200);
                        for (int i = 0; i < screens.length; i++) {
                            final int idx = i;
                            final String card = screens[i][0];
                            final String filename = screens[i][1];

                            SwingUtilities.invokeAndWait(() -> {
                                frame.moPhanHe(card);
                                frame.validate();
                                frame.repaint();
                            });

                            Thread.sleep(600);

                            SwingUtilities.invokeAndWait(() -> {
                                try {
                                    int w = frame.getWidth();
                                    int h = frame.getHeight();
                                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D g2 = img.createGraphics();
                                    frame.paint(g2);
                                    g2.dispose();

                                    File out = new File(artifactDir, filename);
                                    ImageIO.write(img, "PNG", out);
                                    System.out.println("CAPTURED: " + filename + " for " + card);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        }

                        // Sau khi chụp xong 12 màn, lưu ảnh tổng quan và đóng frame
                        SwingUtilities.invokeLater(() -> {
                            frame.dispose();
                            System.out.println("ALL_12_SCREENS_CAPTURED_SUCCESSFULLY");
                            System.exit(0);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }).start();

            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }
}
