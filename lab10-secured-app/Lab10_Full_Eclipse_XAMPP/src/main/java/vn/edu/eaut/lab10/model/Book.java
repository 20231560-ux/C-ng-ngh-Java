package vn.edu.eaut.lab10.model;
import jakarta.persistence.*;
@Entity @Table(name="books")
public class Book {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false,unique=true,length=30) private String code;
 @Column(nullable=false,length=150) private String title;
 @Column(length=100) private String author;
  private int quantity;
 public Book(){}
 public Integer getId(){return id;} public void setId(Integer v){id=v;}
 public String getCode(){return code;} public void setCode(String v){code=v;}
 public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public String getAuthor(){return author;} public void setAuthor(String v){author=v;}
 public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}
