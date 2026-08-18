package vn.edu.eaut.lab7.repository;
import java.util.*; import vn.edu.eaut.lab7.model.Sach;
public class SachRepository {
 private static final List<Sach> data=new ArrayList<>();
 private static int next=3;
 static {
  data.add(new Sach(1,"S001","Lập trình Java","Nguyễn Văn A","NXB Giáo dục",2024));
  data.add(new Sach(2,"S002","Công nghệ Java","Trần Văn B","NXB Khoa học",2025));
 }
 public List<Sach> findAll(){ return data; }
 public Sach findById(int id){ for(Sach x:data) if(x.getId()==id)return x; return null; }
 public void add(Sach x){ x.setId(next++); data.add(x); }
 public void update(Sach x){ Sach o=findById(x.getId()); if(o!=null){o.setMaSach(x.getMaSach());o.setTenSach(x.getTenSach());o.setTacGia(x.getTacGia());o.setNhaXuatBan(x.getNhaXuatBan());o.setNamXuatBan(x.getNamXuatBan());} }
 public void delete(int id){ data.removeIf(x->x.getId()==id); }
 public List<Sach> search(String keyword){
  if(keyword==null||keyword.trim().isEmpty()) return new ArrayList<>(data);
  String k=keyword.trim().toLowerCase(); List<Sach> result=new ArrayList<>();
  for(Sach x:data) if(x.getTenSach().toLowerCase().contains(k)||x.getTacGia().toLowerCase().contains(k)) result.add(x);
  return result;
 }
}