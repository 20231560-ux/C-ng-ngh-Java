package vn.edu.eaut.lab3;
import javax.swing.*;import java.awt.*;
@SuppressWarnings("unused")
public class Bai07MayTinhMini extends JFrame{
 private static final long serialVersionUID = 1L;
 JTextField a=new JTextField(),b=new JTextField(),kq=new JTextField();JTextArea history=new JTextArea();
 public Bai07MayTinhMini(){setTitle("Bài 7 - Máy tính mini");setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new BorderLayout(8,8));JPanel p=new JPanel(new GridLayout(3,2));p.add(new JLabel("Số 1:"));p.add(a);p.add(new JLabel("Số 2:"));p.add(b);p.add(new JLabel("Kết quả:"));kq.setEditable(false);p.add(kq);JPanel q=new JPanel();for(String op:new String[]{"+","-","*","/","Clear"}){JButton bt=new JButton(op);bt.addActionListener(e->calc(op));q.add(bt);}history.setEditable(false);add(p,BorderLayout.NORTH);add(q,BorderLayout.CENTER);add(new JScrollPane(history),BorderLayout.SOUTH);setSize(500,350);setLocationRelativeTo(null);}
 void calc(String op){if(op.equals("Clear")){a.setText("");b.setText("");kq.setText("");history.setText("");return;}try{double x=Double.parseDouble(a.getText()),y=Double.parseDouble(b.getText()),r=0;if(op.equals("/")&&y==0){JOptionPane.showMessageDialog(this,"Không thể chia cho 0!");return;}if(op.equals("+"))r=x+y;if(op.equals("-"))r=x-y;if(op.equals("*"))r=x*y;if(op.equals("/"))r=x/y;kq.setText(String.valueOf(r));history.append(x+" "+op+" "+y+" = "+r+"\n");}catch(NumberFormatException e){JOptionPane.showMessageDialog(this,"Vui lòng nhập hai số hợp lệ!");}}
 public static void main(String[]x){SwingUtilities.invokeLater(()->new Bai07MayTinhMini().setVisible(true));}}
