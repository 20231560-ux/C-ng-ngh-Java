package vn.edu.eaut.lab7.model;
public class SinhVien {
 private int id; private String maSinhVien,hoTen,email,lop;
 public SinhVien(){}
 public SinhVien(int id,String maSinhVien,String hoTen,String email,String lop){this.id=id;this.maSinhVien=maSinhVien;this.hoTen=hoTen;this.email=email;this.lop=lop;}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getMaSinhVien(){return maSinhVien;} public void setMaSinhVien(String v){maSinhVien=v;}
 public String getHoTen(){return hoTen;} public void setHoTen(String v){hoTen=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getLop(){return lop;} public void setLop(String v){lop=v;}
}