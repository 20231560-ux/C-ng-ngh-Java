package vn.edu.eaut.lab5.bus;
import vn.edu.eaut.lab5.dal.ThongKeDAL;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.SQLException;

public class ThongKeBUS {
    private final ThongKeDAL dal=new ThongKeDAL();
    public BigDecimal tinhDoanhThu(LocalDate a,LocalDate b)throws SQLException{return dal.doanhThu(a,b);}
    public String hoaDonCaoNhat()throws SQLException{return dal.hoaDonCaoNhat();}
    public String sanPhamBanChay()throws SQLException{return dal.sanPhamBanChay();}
}
