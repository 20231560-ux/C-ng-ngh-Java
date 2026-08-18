package vn.edu.eaut.lab7.repository;
import java.util.*; import vn.edu.eaut.lab7.model.SanPham;
public class SanPhamRepository {
 private static final List<SanPham> data=new ArrayList<>();
 private static int next=3;
 static {
  data.add(new SanPham(1,"SP001","Laptop Dell","Laptop học tập",15000000,5));
  data.add(new SanPham(2,"SP002","Chuột Logitech","Chuột không dây",450000,20));
 }
 public List<SanPham> findAll(){ return data; }
 public SanPham findById(int id){ for(SanPham x:data) if(x.getId()==id)return x; return null; }
 public void add(SanPham x){ x.setId(next++); data.add(x); }
 public void update(SanPham x){ SanPham o=findById(x.getId()); if(o!=null){o.setMa(x.getMa());o.setTen(x.getTen());o.setMoTa(x.getMoTa());o.setGia(x.getGia());o.setSoLuong(x.getSoLuong());} }
 public void delete(int id){ data.removeIf(x->x.getId()==id); }
 public List<SanPham> search(String keyword){
  if(keyword==null||keyword.trim().isEmpty()) return new ArrayList<>(data);
  String k=keyword.trim().toLowerCase(); List<SanPham> result=new ArrayList<>();
  for(SanPham x:data) if(x.getTen().toLowerCase().contains(k)||x.getMa().toLowerCase().contains(k)) result.add(x);
  return result;
 }
}