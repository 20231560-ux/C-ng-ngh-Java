package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.SanPham;
import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class SanPhamDAL {
    private SanPham map(ResultSet rs) throws SQLException {
        SanPham s=new SanPham();
        s.setMaSp(rs.getInt("ma_sp")); s.setTenSp(rs.getString("ten_sp"));
        s.setDonGia(rs.getBigDecimal("don_gia")); s.setSoLuong(rs.getInt("so_luong"));
        s.setMaDm(rs.getInt("ma_dm")); s.setTenDm(rs.getString("ten_dm"));
        return s;
    }
    public List<SanPham> findAll() throws SQLException {
        String sql="SELECT sp.*,dm.ten_dm FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm=dm.ma_dm ORDER BY sp.ma_sp";
        return query(sql);
    }
    public List<SanPham> search(String name, BigDecimal minPrice, BigDecimal maxPrice,
                                Integer minQty, Integer maxQty, Integer maDm, int offset) throws SQLException {
        StringBuilder sql=new StringBuilder(
            "SELECT sp.*,dm.ten_dm FROM san_pham sp LEFT JOIN danh_muc dm ON sp.ma_dm=dm.ma_dm WHERE 1=1");
        List<Object> p=new ArrayList<>();
        if(name!=null&&!name.isBlank()){sql.append(" AND sp.ten_sp LIKE ?");p.add("%"+name+"%");}
        if(minPrice!=null){sql.append(" AND sp.don_gia>=?");p.add(minPrice);}
        if(maxPrice!=null){sql.append(" AND sp.don_gia<=?");p.add(maxPrice);}
        if(minQty!=null){sql.append(" AND sp.so_luong>=?");p.add(minQty);}
        if(maxQty!=null){sql.append(" AND sp.so_luong<=?");p.add(maxQty);}
        if(maDm!=null&&maDm>0){sql.append(" AND sp.ma_dm=?");p.add(maDm);}
        sql.append(" ORDER BY sp.ma_sp LIMIT 10 OFFSET ?");
        p.add(offset);
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement(sql.toString())){
            for(int i=0;i<p.size();i++)ps.setObject(i+1,p.get(i));
            try(ResultSet rs=ps.executeQuery()){List<SanPham> l=new ArrayList<>();while(rs.next())l.add(map(rs));return l;}
        }
    }
    private List<SanPham> query(String sql)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            List<SanPham> l=new ArrayList<>();while(rs.next())l.add(map(rs));return l;
        }
    }
    public boolean insert(SanPham s)throws SQLException{
        String sql="INSERT INTO san_pham(ten_sp,don_gia,so_luong,ma_dm) VALUES(?,?,?,?)";
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,s.getTenSp());ps.setBigDecimal(2,s.getDonGia());ps.setInt(3,s.getSoLuong());
            if(s.getMaDm()>0)ps.setInt(4,s.getMaDm());else ps.setNull(4,Types.INTEGER);return ps.executeUpdate()>0;
        }
    }
    public boolean update(SanPham s)throws SQLException{
        String sql="UPDATE san_pham SET ten_sp=?,don_gia=?,so_luong=?,ma_dm=? WHERE ma_sp=?";
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,s.getTenSp());ps.setBigDecimal(2,s.getDonGia());ps.setInt(3,s.getSoLuong());
            if(s.getMaDm()>0)ps.setInt(4,s.getMaDm());else ps.setNull(4,Types.INTEGER);ps.setInt(5,s.getMaSp());
            return ps.executeUpdate()>0;
        }
    }
    public boolean delete(int id)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement ps=c.prepareStatement("DELETE FROM san_pham WHERE ma_sp=?")){
            ps.setInt(1,id);return ps.executeUpdate()>0;
        }
    }
    public List<SanPham> findAllForCombo()throws SQLException{return findAll();}
}
