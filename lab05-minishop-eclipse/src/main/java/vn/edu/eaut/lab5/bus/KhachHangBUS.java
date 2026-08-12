package vn.edu.eaut.lab5.bus;
import vn.edu.eaut.lab5.dal.KhachHangDAL;
import vn.edu.eaut.lab5.model.KhachHang;
import java.sql.SQLException;
import java.util.List;

public class KhachHangBUS {
    private final KhachHangDAL dal=new KhachHangDAL();
    public List<KhachHang> findAll()throws SQLException{return dal.findAll();}
    public List<KhachHang> search(String q)throws SQLException{return dal.search(q);}
    public boolean save(KhachHang k)throws SQLException{validate(k);return k.getMaKh()==0?dal.insert(k):dal.update(k);}
    public boolean delete(int id)throws SQLException{return dal.delete(id);}
    private void validate(KhachHang k){
        if(k.getTenKh()==null||k.getTenKh().isBlank())throw new IllegalArgumentException("Ten khach hang khong duoc rong");
        if(k.getSdt()==null||!k.getSdt().matches("\\d{1,10}"))throw new IllegalArgumentException("So dien thoai chi gom so va toi da 10 ky tu");
    }
}
