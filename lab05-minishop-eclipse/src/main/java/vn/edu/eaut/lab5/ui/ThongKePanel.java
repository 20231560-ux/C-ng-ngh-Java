package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.ThongKeBUS;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKePanel extends JPanel {
    private final JTextField from=new JTextField(LocalDate.now().withDayOfMonth(1).toString());
    private final JTextField to=new JTextField(LocalDate.now().toString());
    private final JLabel result=new JLabel("Doanh thu: 0 VND");
    private final ThongKeBUS bus=new ThongKeBUS();
    public ThongKePanel(){
        setLayout(new BorderLayout(10,10));setBorder(new EmptyBorder(10,10,10,10));
        JLabel title=new JLabel("Thống kê doanh thu");title.setFont(title.getFont().deriveFont(Font.BOLD,15f));
        JPanel form=new JPanel(new GridBagLayout());GridBagConstraints g=base();
        g.gridy=0;g.gridx=0;g.weightx=0;form.add(new JLabel("Từ ngày (yyyy-MM-dd):"),g);g.gridx=1;g.weightx=.5;form.add(from,g);
        g.gridx=2;g.weightx=0;form.add(new JLabel("Đến ngày:"),g);g.gridx=3;g.weightx=.5;form.add(to,g);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT,8,3));JButton calc=new JButton("Tính doanh thu"),max=new JButton("Hóa đơn cao nhất"),best=new JButton("Sản phẩm bán chạy");buttons.add(calc);buttons.add(max);buttons.add(best);
        JPanel top=new JPanel(new BorderLayout(8,8));top.add(title,BorderLayout.NORTH);top.add(form,BorderLayout.CENTER);top.add(buttons,BorderLayout.SOUTH);add(top,BorderLayout.NORTH);
        result.setBorder(new EmptyBorder(20,20,20,20));result.setFont(result.getFont().deriveFont(Font.BOLD,20f));add(result,BorderLayout.CENTER);
        calc.addActionListener(e->doRevenue());max.addActionListener(e->worker(1));best.addActionListener(e->worker(2));
    }
    private GridBagConstraints base(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(4,5,4,5);g.fill=GridBagConstraints.HORIZONTAL;return g;}
    private void doRevenue(){try{LocalDate a=LocalDate.parse(from.getText().trim()),b=LocalDate.parse(to.getText().trim());if(a.isAfter(b))throw new IllegalArgumentException("Từ ngày phải nhỏ hơn hoặc bằng đến ngày");new SwingWorker<BigDecimal,Void>(){protected BigDecimal doInBackground()throws Exception{return bus.tinhDoanhThu(a,b);}protected void done(){try{result.setText("Doanh thu: "+get()+" VND");}catch(Exception e){result.setText("Lỗi: "+e.getMessage());}}}.execute();}catch(Exception e){result.setText("Ngày không hợp lệ: "+e.getMessage());}}
    private void worker(int type){new SwingWorker<String,Void>(){protected String doInBackground()throws Exception{return type==1?bus.hoaDonCaoNhat():bus.sanPhamBanChay();}protected void done(){try{result.setText(get());}catch(Exception e){result.setText("Lỗi: "+e.getMessage());}}}.execute();}
}
