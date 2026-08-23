package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bài 6: Entity Lớp học - quan hệ 1-nhiều với SinhVien
 * Một LopHoc có nhiều SinhVien.
 */
@Entity
@Table(name = "lop_hoc")
public class LopHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_lop", nullable = false, unique = true, length = 20)
    private String maLop;

    @Column(name = "ten_lop", nullable = false, length = 100)
    private String tenLop;

    @Column(name = "khoa", length = 100)
    private String khoa;

    // mappedBy = tên field "lopHoc" bên SinhVien -> đây là phía "1" (không giữ FK)
    @OneToMany(mappedBy = "lopHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SinhVien> danhSachSinhVien = new ArrayList<>();

    public LopHoc() {
    }

    public LopHoc(String maLop, String tenLop, String khoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoa = khoa;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }

    public List<SinhVien> getDanhSachSinhVien() { return danhSachSinhVien; }
    public void setDanhSachSinhVien(List<SinhVien> danhSachSinhVien) { this.danhSachSinhVien = danhSachSinhVien; }
}
