package vn.edu.eaut.lab5.bus;
import vn.edu.eaut.lab5.dal.DanhMucDAL;
import java.sql.SQLException;
import java.util.List;

public class DanhMucBUS {
    private final DanhMucDAL dal=new DanhMucDAL();
    public List<Object[]> findAll()throws SQLException{return dal.findAll();}
    public boolean insert(String n)throws SQLException{if(n==null||n.isBlank())throw new IllegalArgumentException("Ten danh muc rong");return dal.insert(n);}
    public boolean update(int id,String n)throws SQLException{return dal.update(id,n);}
    public boolean delete(int id)throws SQLException{return dal.delete(id);}
}
