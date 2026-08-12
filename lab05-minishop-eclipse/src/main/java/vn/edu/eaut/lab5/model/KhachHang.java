package vn.edu.eaut.lab5.model;

public class KhachHang {
    private int maKh;
    private String tenKh, sdt, diaChi;
    public KhachHang(){}
    public KhachHang(int maKh,String tenKh,String sdt,String diaChi){
        this.maKh=maKh;this.tenKh=tenKh;this.sdt=sdt;this.diaChi=diaChi;
    }
    public int getMaKh(){return maKh;} public void setMaKh(int v){maKh=v;}
    public String getTenKh(){return tenKh;} public void setTenKh(String v){tenKh=v;}
    public String getSdt(){return sdt;} public void setSdt(String v){sdt=v;}
    public String getDiaChi(){return diaChi;} public void setDiaChi(String v){diaChi=v;}
    @Override public String toString(){return tenKh+" - "+sdt;}
}
