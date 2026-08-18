package vn.edu.eaut.lab7.repository;
import java.util.*; import vn.edu.eaut.lab7.model.SinhVien;
public class SinhVienRepository {
 private static final List<SinhVien> data=new ArrayList<>();
 private static int next=3;
 static {
  data.add(new SinhVien(1,"20240001","Nguyễn Văn An","an@gmail.com","DCCNTT15.10.1"));
  data.add(new SinhVien(2,"20240002","Trần Thị Bình","binh@gmail.com","DCCNTT15.10.2"));
 }
 public List<SinhVien> findAll(){ return data; }
 public SinhVien findById(int id){ for(SinhVien x:data) if(x.getId()==id)return x; return null; }
 public void add(SinhVien x){ x.setId(next++); data.add(x); }
 public void update(SinhVien x){ SinhVien o=findById(x.getId()); if(o!=null){o.setMaSinhVien(x.getMaSinhVien());o.setHoTen(x.getHoTen());o.setEmail(x.getEmail());o.setLop(x.getLop());} }
 public void delete(int id){ data.removeIf(x->x.getId()==id); }
 public List<SinhVien> search(String keyword){
  if(keyword==null||keyword.trim().isEmpty()) return new ArrayList<>(data);
  String k=keyword.trim().toLowerCase(); List<SinhVien> result=new ArrayList<>();
  for(SinhVien x:data) if(x.getHoTen().toLowerCase().contains(k)||x.getLop().toLowerCase().contains(k)) result.add(x);
  return result;
 }
}