package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai01HelloSwing extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField name=new JTextField(20);
 public Bai01HelloSwing(){setTitle("Bài 1 - Chào người dùng");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new FlowLayout());add(new JLabel("Nhập tên:"));add(name);JButton b=new JButton("Hiển thị lời chào");add(b);b.addActionListener(e->hello());pack();setLocationRelativeTo(null);}
 void hello(){String s=name.getText().trim();if(s.isEmpty())JOptionPane.showMessageDialog(this,"Vui lòng nhập tên!");else JOptionPane.showMessageDialog(this,"Xin chào, "+s+"!");}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai01HelloSwing().setVisible(true));}}
