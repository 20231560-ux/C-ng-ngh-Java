package vn.edu.eaut.lab5.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(String username,String hoTen,String role){
        setTitle("MiniShop - "+hoTen+" ["+role+"]");setSize(1150,720);setDefaultCloseOperation(EXIT_ON_CLOSE);setLocationRelativeTo(null);
        JTabbedPane tabs=new JTabbedPane();
        if(role.equals("ADMIN")||role.equals("NHANVIEN"))tabs.addTab("San pham",new SanPhamPanel(role));
        if(role.equals("ADMIN")||role.equals("NHANVIEN"))tabs.addTab("Khach hang",new KhachHangPanel());
        if(role.equals("ADMIN")||role.equals("NHANVIEN"))tabs.addTab("Hoa don",new HoaDonPanel());
        if(role.equals("ADMIN")||role.equals("KETOAN"))tabs.addTab("Thong ke",new ThongKePanel());
        if(role.equals("ADMIN"))tabs.addTab("Tai khoan",new JLabel("Quan tri tai khoan: ADMIN co toan quyen."));
        add(tabs,BorderLayout.CENTER);
        JPanel top=new JPanel(new BorderLayout());top.add(new JLabel("  Xin chao: "+hoTen+" | Vai tro: "+role),BorderLayout.WEST);
        JButton logout=new JButton("Dang xuat");top.add(logout,BorderLayout.EAST);add(top,BorderLayout.NORTH);
        logout.addActionListener(e->{dispose();new LoginFrame().setVisible(true);});
    }
}
