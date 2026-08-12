package vn.edu.eaut.lab5.model;
import java.math.BigDecimal;

public class SanPham {
    private int maSp, soLuong, maDm;
    private String tenSp, tenDm;
    private BigDecimal donGia;

    public SanPham() {}
    public SanPham(int maSp, String tenSp, BigDecimal donGia, int soLuong, int maDm, String tenDm) {
        this.maSp=maSp; this.tenSp=tenSp; this.donGia=donGia;
        this.soLuong=soLuong; this.maDm=maDm; this.tenDm=tenDm;
    }
    public int getMaSp(){return maSp;} public void setMaSp(int v){maSp=v;}
    public String getTenSp(){return tenSp;} public void setTenSp(String v){tenSp=v;}
    public BigDecimal getDonGia(){return donGia;} public void setDonGia(BigDecimal v){donGia=v;}
    public int getSoLuong(){return soLuong;} public void setSoLuong(int v){soLuong=v;}
    public int getMaDm(){return maDm;} public void setMaDm(int v){maDm=v;}
    public String getTenDm(){return tenDm;} public void setTenDm(String v){tenDm=v;}
    @Override public String toString(){return tenSp;}
}
