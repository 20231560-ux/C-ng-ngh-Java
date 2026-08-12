package vn.edu.eaut.lab5.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDon {
    private int maHd, maKh;
    private LocalDate ngayLap;
    private BigDecimal tongTien;
    private String tenKh;
    public HoaDon(){}
    public int getMaHd(){return maHd;} public void setMaHd(int v){maHd=v;}
    public int getMaKh(){return maKh;} public void setMaKh(int v){maKh=v;}
    public LocalDate getNgayLap(){return ngayLap;} public void setNgayLap(LocalDate v){ngayLap=v;}
    public BigDecimal getTongTien(){return tongTien;} public void setTongTien(BigDecimal v){tongTien=v;}
    public String getTenKh(){return tenKh;} public void setTenKh(String v){tenKh=v;}
}
