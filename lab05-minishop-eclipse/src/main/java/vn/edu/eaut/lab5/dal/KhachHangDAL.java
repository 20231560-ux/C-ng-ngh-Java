package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.KhachHang;
import java.sql.*;
import java.util.*;

public class KhachHangDAL {
    private KhachHang map(ResultSet r)throws SQLException{
        return new KhachHang(r.getInt("ma_kh"),r.getString("ten_kh"),r.getString("sdt"),r.getString("dia_chi"));
    }
    public List<KhachHang> findAll()throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM khach_hang ORDER BY ma_kh");ResultSet r=p.executeQuery()){
            List<KhachHang> l=new ArrayList<>();while(r.next())l.add(map(r));return l;
        }
    }
    public List<KhachHang> search(String q)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement(
            "SELECT * FROM khach_hang WHERE ten_kh LIKE ? OR sdt LIKE ? OR dia_chi LIKE ? ORDER BY ma_kh")){
            String x="%"+(q==null?"":q)+"%";p.setString(1,x);p.setString(2,x);p.setString(3,x);
            try(ResultSet r=p.executeQuery()){List<KhachHang> l=new ArrayList<>();while(r.next())l.add(map(r));return l;}
        }
    }
    public boolean insert(KhachHang k)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO khach_hang(ten_kh,sdt,dia_chi) VALUES(?,?,?)")){
            p.setString(1,k.getTenKh());p.setString(2,k.getSdt());p.setString(3,k.getDiaChi());return p.executeUpdate()>0;
        }
    }
    public boolean update(KhachHang k)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("UPDATE khach_hang SET ten_kh=?,sdt=?,dia_chi=? WHERE ma_kh=?")){
            p.setString(1,k.getTenKh());p.setString(2,k.getSdt());p.setString(3,k.getDiaChi());p.setInt(4,k.getMaKh());return p.executeUpdate()>0;
        }
    }
    public boolean delete(int id)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM khach_hang WHERE ma_kh=?")){
            p.setInt(1,id);return p.executeUpdate()>0;
        }
    }
}
