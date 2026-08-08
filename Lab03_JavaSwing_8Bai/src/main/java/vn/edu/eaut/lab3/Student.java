package vn.edu.eaut.lab3;
public class Student{
 private String maSV,hoTen;private double diemTB;
 public Student(String m,String h,double d){maSV=m;hoTen=h;diemTB=d;}
 public String getMaSV(){return maSV;}public String getHoTen(){return hoTen;}public double getDiemTB(){return diemTB;}
 public String xepLoai(){if(diemTB>=8.5)return"Giỏi";if(diemTB>=7)return"Khá";if(diemTB>=5)return"Trung bình";return"Yếu";}
}
