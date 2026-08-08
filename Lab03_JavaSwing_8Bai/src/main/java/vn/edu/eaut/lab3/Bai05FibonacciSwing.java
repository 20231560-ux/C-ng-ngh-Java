package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai05FibonacciSwing extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField n=new JTextField(10);JTextArea out=new JTextArea(8,35);
 public Bai05FibonacciSwing(){setTitle("Bài 5 - Fibonacci");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new BorderLayout());JPanel p=new JPanel();p.add(new JLabel("Nhập n:"));p.add(n);JButton b=new JButton("Hiển thị");p.add(b);b.addActionListener(e->showF());out.setEditable(false);add(p,BorderLayout.NORTH);add(new JScrollPane(out),BorderLayout.CENTER);pack();setLocationRelativeTo(null);}
 void showF(){try{int n1=Integer.parseInt(n.getText());if(n1<=0||n1>92){JOptionPane.showMessageDialog(this,"n phải từ 1 đến 92!");return;}long a=0,b=1;StringBuilder s=new StringBuilder();for(int i=0;i<n1;i++){if(i>0)s.append(" ");s.append(a);long t=a+b;a=b;b=t;}out.setText(s.toString());}catch(NumberFormatException e){JOptionPane.showMessageDialog(this,"n phải là số nguyên dương!");}}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai05FibonacciSwing().setVisible(true));}}
