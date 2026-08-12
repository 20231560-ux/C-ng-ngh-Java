package vn.edu.eaut.lab5.bus;
import vn.edu.eaut.lab5.dal.SanPhamDAL;
import vn.edu.eaut.lab5.model.SanPham;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamBUS {
    private final SanPhamDAL dal=new SanPhamDAL();
    public List<SanPham> findAll()throws SQLException{return dal.findAll();}
    public List<SanPham> search(String n,BigDecimal min,BigDecimal max,Integer minQ,Integer maxQ,Integer dm,int page)throws SQLException{
        return dal.search(n,min,max,minQ,maxQ,dm,page*10);
    }
    public boolean save(SanPham s)throws SQLException{validate(s);return s.getMaSp()==0?dal.insert(s):dal.update(s);}
    public boolean delete(int id)throws SQLException{if(id<=0)throw new IllegalArgumentException("Ma san pham khong hop le");return dal.delete(id);}
    private void validate(SanPham s){
        if(s.getTenSp()==null||s.getTenSp().isBlank())throw new IllegalArgumentException("Ten san pham khong duoc rong");
        if(s.getDonGia()==null||s.getDonGia().compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("Don gia phai lon hon 0");
        if(s.getSoLuong()<0)throw new IllegalArgumentException("So luong khong duoc am");
    }
}
