package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;import java.util.Arrays;
@SuppressWarnings("unused")
public class Bai04TamGiacSwing extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField a=new JTextField(),b=new JTextField(),c=new JTextField();JLabel kq=new JLabel("Kết quả: ");
 public Bai04TamGiacSwing(){setTitle("Bài 4 - Kiểm tra tam giác");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new BorderLayout(8,8));JPanel p=new JPanel(new GridLayout(3,2,8,8));p.add(new JLabel("Cạnh a:"));p.add(a);p.add(new JLabel("Cạnh b:"));p.add(b);p.add(new JLabel("Cạnh c:"));p.add(c);JButton bt=new JButton("Kiểm tra");bt.addActionListener(e->check());add(kq,BorderLayout.NORTH);add(p,BorderLayout.CENTER);add(bt,BorderLayout.SOUTH);setSize(420,230);setLocationRelativeTo(null);}
 void check(){try{kq.setText("Kết quả: "+type(Double.parseDouble(a.getText()),Double.parseDouble(b.getText()),Double.parseDouble(c.getText())));}catch(NumberFormatException e){JOptionPane.showMessageDialog(this,"Ba cạnh phải là số hợp lệ!");}}
 String type(double a,double b,double c){double E=1e-9;if(a<=0||b<=0||c<=0||a+b<=c||a+c<=b||b+c<=a)return"Không phải tam giác";boolean deu=Math.abs(a-b)<E&&Math.abs(b-c)<E,can=Math.abs(a-b)<E||Math.abs(a-c)<E||Math.abs(b-c)<E;double[]d={a,b,c};Arrays.sort(d);boolean vuong=Math.abs(d[0]*d[0]+d[1]*d[1]-d[2]*d[2])<E;if(deu)return"Tam giác đều";if(vuong&&can)return"Tam giác vuông cân";if(vuong)return"Tam giác vuông";if(can)return"Tam giác cân";return"Tam giác thường";}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai04TamGiacSwing().setVisible(true));}}
