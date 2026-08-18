package vn.edu.eaut.lab7.model;
public class SanPham {
 private int id; private String ma,ten,moTa; private double gia; private int soLuong;
 public SanPham(){}
 public SanPham(int id,String ma,String ten,String moTa,double gia,int soLuong){this.id=id;this.ma=ma;this.ten=ten;this.moTa=moTa;this.gia=gia;this.soLuong=soLuong;}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getMa(){return ma;} public void setMa(String v){ma=v;}
 public String getTen(){return ten;} public void setTen(String v){ten=v;}
 public String getMoTa(){return moTa;} public void setMoTa(String v){moTa=v;}
 public double getGia(){return gia;} public void setGia(double v){gia=v;}
 public int getSoLuong(){return soLuong;} public void setSoLuong(int v){soLuong=v;}
}