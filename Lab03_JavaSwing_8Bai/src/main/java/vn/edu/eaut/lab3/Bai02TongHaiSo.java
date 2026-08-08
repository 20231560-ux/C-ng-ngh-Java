package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai02TongHaiSo extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField a=new JTextField(),b=new JTextField();JLabel kq=new JLabel("Kết quả: ");
 public Bai02TongHaiSo(){setTitle("Bài 2 - Tính tổng hai số");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new GridLayout(4,2,8,8));add(new JLabel("Số thứ nhất:"));add(a);add(new JLabel("Số thứ hai:"));add(b);JButton t=new JButton("Tính tổng"),r=new JButton("Làm mới");add(t);add(r);add(kq);add(new JLabel());t.addActionListener(e->sum());r.addActionListener(e->{a.setText("");b.setText("");kq.setText("Kết quả: ");a.requestFocus();});setSize(380,190);setLocationRelativeTo(null);}
 void sum(){try{kq.setText("Kết quả: "+(Double.parseDouble(a.getText())+Double.parseDouble(b.getText())));}catch(NumberFormatException e){JOptionPane.showMessageDialog(this,"Dữ liệu nhập phải là số hợp lệ!");}}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai02TongHaiSo().setVisible(true));}}
