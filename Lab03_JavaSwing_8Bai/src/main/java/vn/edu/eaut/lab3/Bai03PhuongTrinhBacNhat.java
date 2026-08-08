package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai03PhuongTrinhBacNhat extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField a=new JTextField(),b=new JTextField();JLabel kq=new JLabel("Nghiệm: ");
 public Bai03PhuongTrinhBacNhat(){setTitle("Bài 3 - Giải phương trình ax + b = 0");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new BorderLayout(8,8));JPanel p=new JPanel(new GridLayout(2,2,8,8));p.add(new JLabel("Hệ số a:"));p.add(a);p.add(new JLabel("Hệ số b:"));p.add(b);JButton bt=new JButton("Giải phương trình");bt.addActionListener(e->solve());add(kq,BorderLayout.NORTH);add(p,BorderLayout.CENTER);add(bt,BorderLayout.SOUTH);setSize(420,190);setLocationRelativeTo(null);}
 void solve(){try{double x=Double.parseDouble(a.getText()),y=Double.parseDouble(b.getText());if(Math.abs(x)<1e-9&&Math.abs(y)<1e-9)kq.setText("Nghiệm: vô số nghiệm");else if(Math.abs(x)<1e-9)kq.setText("Nghiệm: vô nghiệm");else kq.setText(String.format("Nghiệm: x = %.4f",-y/x));}catch(NumberFormatException e){JOptionPane.showMessageDialog(this,"Vui lòng nhập a, b là số hợp lệ!");}}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai03PhuongTrinhBacNhat().setVisible(true));}}
