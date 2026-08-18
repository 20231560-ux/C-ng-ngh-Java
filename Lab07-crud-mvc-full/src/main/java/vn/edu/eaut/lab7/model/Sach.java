package vn.edu.eaut.lab7.model;
public class Sach {
 private int id; private String maSach,tenSach,tacGia,nhaXuatBan; private int namXuatBan;
 public Sach(){}
 public Sach(int id,String maSach,String tenSach,String tacGia,String nhaXuatBan,int namXuatBan){this.id=id;this.maSach=maSach;this.tenSach=tenSach;this.tacGia=tacGia;this.nhaXuatBan=nhaXuatBan;this.namXuatBan=namXuatBan;}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getMaSach(){return maSach;} public void setMaSach(String v){maSach=v;}
 public String getTenSach(){return tenSach;} public void setTenSach(String v){tenSach=v;}
 public String getTacGia(){return tacGia;} public void setTacGia(String v){tacGia=v;}
 public String getNhaXuatBan(){return nhaXuatBan;} public void setNhaXuatBan(String v){nhaXuatBan=v;}
 public int getNamXuatBan(){return namXuatBan;} public void setNamXuatBan(int v){namXuatBan=v;}
}