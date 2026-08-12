package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class HoaDonDAL {
    public int insertHoaDon(int maKh,List<ChiTietHoaDon> list)throws SQLException{
        if(list==null||list.isEmpty())throw new IllegalArgumentException("Hoa don chua co san pham");
        String h="INSERT INTO hoa_don(ngay_lap,ma_kh,tong_tien) VALUES(?,?,?)";
        String d="INSERT INTO chi_tiet_hoa_don(ma_hd,ma_sp,so_luong,don_gia,thanh_tien) VALUES(?,?,?,?,?)";
        String stock="UPDATE san_pham SET so_luong=so_luong-? WHERE ma_sp=? AND so_luong>=?";
        BigDecimal total=BigDecimal.ZERO;
        for(ChiTietHoaDon ct:list) total=total.add(ct.getThanhTien());
        try(Connection c=DBHelper.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement(h,Statement.RETURN_GENERATED_KEYS)){
                p.setDate(1,Date.valueOf(LocalDate.now()));p.setInt(2,maKh);p.setBigDecimal(3,total);p.executeUpdate();
                int id;try(ResultSet r=p.getGeneratedKeys()){if(!r.next())throw new SQLException("Khong lay duoc ma hoa don");id=r.getInt(1);}
                try(PreparedStatement ps=c.prepareStatement(stock);PreparedStatement pd=c.prepareStatement(d)){
                    for(ChiTietHoaDon ct:list){
                        ps.setInt(1,ct.getSoLuong());ps.setInt(2,ct.getMaSp());ps.setInt(3,ct.getSoLuong());
                        if(ps.executeUpdate()!=1)throw new SQLException("Ton kho khong du cho: "+ct.getTenSp());
                        pd.setInt(1,id);pd.setInt(2,ct.getMaSp());pd.setInt(3,ct.getSoLuong());pd.setBigDecimal(4,ct.getDonGia());pd.setBigDecimal(5,ct.getThanhTien());pd.addBatch();
                    }
                    pd.executeBatch();
                }
                c.commit();return id;
            }catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw e;}
            finally{c.setAutoCommit(true);}
        }
    }
    public List<HoaDon> findAll()throws SQLException{
        String sql="SELECT hd.*,kh.ten_kh FROM hoa_don hd JOIN khach_hang kh ON hd.ma_kh=kh.ma_kh ORDER BY hd.ma_hd DESC";
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            List<HoaDon> l=new ArrayList<>();while(r.next()){HoaDon h=new HoaDon();h.setMaHd(r.getInt("ma_hd"));h.setMaKh(r.getInt("ma_kh"));h.setNgayLap(r.getDate("ngay_lap").toLocalDate());h.setTongTien(r.getBigDecimal("tong_tien"));h.setTenKh(r.getString("ten_kh"));l.add(h);}return l;
        }
    }
}
