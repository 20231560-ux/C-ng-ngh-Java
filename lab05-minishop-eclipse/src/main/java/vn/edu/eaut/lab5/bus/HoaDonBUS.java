package vn.edu.eaut.lab5.bus;
import vn.edu.eaut.lab5.dal.HoaDonDAL;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import java.sql.SQLException;
import java.util.List;

public class HoaDonBUS {
    private final HoaDonDAL dal=new HoaDonDAL();
    public int save(int maKh,List<ChiTietHoaDon> list)throws SQLException{
        if(maKh<=0)throw new IllegalArgumentException("Hay chon khach hang");
        for(ChiTietHoaDon c:list)if(c.getSoLuong()<=0)throw new IllegalArgumentException("So luong phai lon hon 0");
        return dal.insertHoaDon(maKh,list);
    }
    public java.util.List<vn.edu.eaut.lab5.model.HoaDon> findAll()throws SQLException{return dal.findAll();}
}
