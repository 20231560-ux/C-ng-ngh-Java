package com.restaurant;

import com.restaurant.dao.NguoiDungDAO;
import com.restaurant.model.NguoiDung;
import com.restaurant.view.QuanLyNhaHangFrame;

public class LoginFlowTest {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        try {
            System.out.println("[TEST] Step 1: Querying admin user from MySQL...");
            NguoiDungDAO dao = new NguoiDungDAO();
            NguoiDung admin = dao.timTheoTenDangNhap("admin");
            if (admin == null) {
                System.out.println("[TEST] Admin not found by 'admin', using stub admin...");
                admin = new NguoiDung();
                admin.setTenDangNhap("admin");
                admin.setHoTen("Nguyễn Văn Admin");
                admin.setVaiTro("Quản trị");
            }
            System.out.println("[TEST] Admin user: " + admin.getHoTen() + " (" + admin.getVaiTro() + ")");

            System.out.println("[TEST] Step 2: Instantiating QuanLyNhaHangFrame(admin)...");
            QuanLyNhaHangFrame frame = new QuanLyNhaHangFrame(admin);
            System.out.println("[TEST] Step 3: Frame instantiated successfully!");

            System.out.println("[TEST] Step 4: Testing visible and layout...");
            frame.setVisible(true);
            frame.validate();
            System.out.println("[TEST] Step 5: SUCCESS! No exception thrown during frame creation.");
            
            Thread.sleep(1500);
            frame.dispose();
            System.out.println("[TEST] Step 6: Test completed successfully.");
            System.exit(0);
        } catch (Throwable t) {
            System.err.println("[TEST ERROR] EXCEPTION CAUGHT IN LOGIN FLOW:");
            t.printStackTrace();
            System.exit(1);
        }
    }
}
