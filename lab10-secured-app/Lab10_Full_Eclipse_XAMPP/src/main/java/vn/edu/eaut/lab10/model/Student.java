package vn.edu.eaut.lab10.model;
import jakarta.persistence.*;
@Entity @Table(name="students")
public class Student {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false,unique=true,length=30) private String code;
 @Column(nullable=false,length=100) private String fullName;
 @Column(length=100) private String email;
 @Column(length=100) private String major;
 public Student(){}
 public Integer getId(){return id;} public void setId(Integer v){id=v;}
 public String getCode(){return code;} public void setCode(String v){code=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getMajor(){return major;} public void setMajor(String v){major=v;}
}
