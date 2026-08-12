package vn.edu.eaut.lab5.dal;
import vn.edu.eaut.lab5.config.DBHelper;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKeDAL {
    public BigDecimal doanhThu(LocalDate a,LocalDate b)throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement(
            "SELECT COALESCE(SUM(tong_tien),0) FROM hoa_don WHERE ngay_lap BETWEEN ? AND ?")){
            p.setDate(1,Date.valueOf(a));p.setDate(2,Date.valueOf(b));try(ResultSet r=p.executeQuery()){r.next();return r.getBigDecimal(1);}
        }
    }
    public String hoaDonCaoNhat()throws SQLException{
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement(
            "SELECT ma_hd,ngay_lap,ma_kh,tong_tien FROM hoa_don ORDER BY tong_tien DESC LIMIT 1");ResultSet r=p.executeQuery()){
            return r.next()?("HD #"+r.getInt(1)+" | "+r.getDate(2)+" | "+r.getBigDecimal(4)+" VND"):"Chua co hoa don";
        }
    }
    public String sanPhamBanChay()throws SQLException{
        String sql="SELECT sp.ma_sp,sp.ten_sp,SUM(ct.so_luong) tong_so_luong FROM chi_tiet_hoa_don ct JOIN san_pham sp ON ct.ma_sp=sp.ma_sp GROUP BY sp.ma_sp,sp.ten_sp ORDER BY tong_so_luong DESC LIMIT 1";
        try(Connection c=DBHelper.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            return r.next()?r.getString("ten_sp")+" | "+r.getInt("tong_so_luong")+" san pham":"Chua co du lieu";
        }
    }
}
