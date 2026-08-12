package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.util.MessageUtil;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private final JTextField txtUser=new JTextField();
    private final JPasswordField txtPass=new JPasswordField();

    public LoginFrame(){
        setTitle("MiniShop - Dang nhap");setSize(420,260);setDefaultCloseOperation(EXIT_ON_CLOSE);setLocationRelativeTo(null);
        JPanel p=new JPanel(new GridBagLayout());GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(8,8,8,8);g.fill=GridBagConstraints.HORIZONTAL;
        g.gridx=0;g.gridy=0;g.gridwidth=2;JLabel title=new JLabel("MINISHOP - DANG NHAP",SwingConstants.CENTER);title.setFont(new Font("Arial",Font.BOLD,22));p.add(title,g);
        g.gridwidth=1;g.gridy++;p.add(new JLabel("Username:"),g);g.gridx=1;p.add(txtUser,g);
        g.gridx=0;g.gridy++;p.add(new JLabel("Password:"),g);g.gridx=1;p.add(txtPass,g);
        g.gridx=0;g.gridy++;g.gridwidth=2;JButton btn=new JButton("Dang nhap");p.add(btn,g);
        g.gridy++;p.add(new JLabel("Demo: admin/123 | nhanvien/123 | ketoan/123",SwingConstants.CENTER),g);
        add(p);btn.addActionListener(e->login());
    }
    private void login(){
        String u=txtUser.getText().trim(),pw=new String(txtPass.getPassword());
        String sql="SELECT ho_ten,vai_tro FROM tai_khoan WHERE username=? AND password=?";
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,u);ps.setString(2,pw);
            try(ResultSet r=ps.executeQuery()){
                if(r.next()){dispose();new MainFrame(u,r.getString(1),r.getString(2)).setVisible(true);}
                else MessageUtil.error(this,"Sai tai khoan hoac mat khau");
            }
        }catch(SQLException e){MessageUtil.error(this,e.getMessage());}
    }
}
