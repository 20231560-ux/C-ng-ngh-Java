package vn.edu.eaut.lab5.util;
import vn.edu.eaut.lab5.model.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class ExportUtil {
    public static Path exportTxt(int maHd,KhachHang kh,List<ChiTietHoaDon> list) throws Exception{
        Path p=Paths.get("HoaDon_"+maHd+".txt");
        StringBuilder s=new StringBuilder();
        s.append("MINISHOP - HOA DON #").append(maHd).append("\n");
        s.append("Ngay lap: ").append(LocalDate.now()).append("\n");
        s.append("Khach hang: ").append(kh.getTenKh()).append(" - ").append(kh.getSdt()).append("\n\n");
        s.append("San pham | So luong | Don gia | Thanh tien\n");
        for(ChiTietHoaDon c:list)s.append(c.getTenSp()).append(" | ").append(c.getSoLuong()).append(" | ")
            .append(c.getDonGia()).append(" | ").append(c.getThanhTien()).append("\n");
        s.append("\nTong tien: ").append(list.stream().map(ChiTietHoaDon::getThanhTien).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add)).append(" VND\n");
        Files.writeString(p,s.toString(),StandardCharsets.UTF_8);return p;
    }
    public static Path exportCsv(int maHd,KhachHang kh,List<ChiTietHoaDon> list)throws Exception{
        Path p=Paths.get("HoaDon_"+maHd+".csv");
        StringBuilder s=new StringBuilder("MaHD,NgayLap,KhachHang,SDT,SanPham,SoLuong,DonGia,ThanhTien\n");
        for(ChiTietHoaDon c:list)s.append(maHd).append(",").append(LocalDate.now()).append(",")
            .append(csv(kh.getTenKh())).append(",").append(kh.getSdt()).append(",").append(csv(c.getTenSp())).append(",")
            .append(c.getSoLuong()).append(",").append(c.getDonGia()).append(",").append(c.getThanhTien()).append("\n");
        Files.writeString(p,s.toString(),StandardCharsets.UTF_8);return p;
    }
    private static String csv(String s){return "\""+s.replace("\"","\"\"")+"\"";}
}
