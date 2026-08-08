package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai06LoginForm extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField user=new JTextField();JPasswordField pass=new JPasswordField();JComboBox<String> role=new JComboBox<>(new String[]{"Admin","User"});JCheckBox show=new JCheckBox("Hiển thị mật khẩu");
 public Bai06LoginForm(){setTitle("Bài 6 - Form đăng nhập");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new GridLayout(4,2,8,8));add(new JLabel("Tài khoản:"));add(user);add(new JLabel("Mật khẩu:"));add(pass);add(new JLabel("Vai trò:"));add(role);add(show);JButton b=new JButton("Đăng nhập");add(b);show.addActionListener(e->pass.setEchoChar(show.isSelected()?(char)0:'•'));b.addActionListener(e->login());pack();setLocationRelativeTo(null);}
 void login(){String u=user.getText().trim(),p=new String(pass.getPassword()),r=(String)role.getSelectedItem();boolean ok=(u.equals("admin")&&p.equals("123456")&&r.equals("Admin"))||(u.equals("user")&&p.equals("123456")&&r.equals("User"));JOptionPane.showMessageDialog(this,ok?"Đăng nhập thành công!":"Sai tài khoản, mật khẩu hoặc vai trò!");}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai06LoginForm().setVisible(true));}}
