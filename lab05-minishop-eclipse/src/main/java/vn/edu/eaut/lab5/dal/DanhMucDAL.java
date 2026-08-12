package vn.edu.eaut.lab5.dal;
import vn.edu.eaut.lab5.config.DBHelper;
import java.sql.*;
import java.util.*;

public class DanhMucDAL {
    public List<Object[]> findAll()throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("SELECT ma_dm,ten_dm FROM danh_muc ORDER BY ma_dm");ResultSet r=p.executeQuery()){
            List<Object[]> l=new ArrayList<>();while(r.next())l.add(new Object[]{r.getInt(1),r.getString(2)});return l;
        }
    }
    public boolean insert(String name)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO danh_muc(ten_dm) VALUES(?)")){p.setString(1,name);return p.executeUpdate()>0;}
    }
    public boolean update(int id,String name)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("UPDATE danh_muc SET ten_dm=? WHERE ma_dm=?")){p.setString(1,name);p.setInt(2,id);return p.executeUpdate()>0;}
    }
    public boolean delete(int id)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM danh_muc WHERE ma_dm=?")){p.setInt(1,id);return p.executeUpdate()>0;}
    }
}
