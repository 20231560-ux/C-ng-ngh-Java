package vn.edu.eaut.lab10.model;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false,unique=true,length=100) private String email;
 @Column(nullable=false) private String password;
 @Column(name="full_name",nullable=false,length=100) private String fullName;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Role role;
 private boolean active=true;
 public User(){}
 public Integer getId(){return id;} public void setId(Integer v){id=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}