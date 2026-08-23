package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;

/**
 * Bài 7: Entity Điểm - mỗi điểm gắn với 1 SinhVien và 1 MonHoc.
 */
@Entity
@Table(name = "diem")
public class Diem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mon_hoc_id", nullable = false)
    private MonHoc monHoc;

    @Column(name = "diem_so")
    private Double diemSo;

    public Diem() {
    }

    public Diem(SinhVien sinhVien, MonHoc monHoc, Double diemSo) {
        this.sinhVien = sinhVien;
        this.monHoc = monHoc;
        this.diemSo = diemSo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public SinhVien getSinhVien() { return sinhVien; }
    public void setSinhVien(SinhVien sinhVien) { this.sinhVien = sinhVien; }

    public MonHoc getMonHoc() { return monHoc; }
    public void setMonHoc(MonHoc monHoc) { this.monHoc = monHoc; }

    public Double getDiemSo() { return diemSo; }
    public void setDiemSo(Double diemSo) { this.diemSo = diemSo; }

    /** Xếp loại đơn giản theo thang điểm 10. */
    public String getXepLoai() {
        if (diemSo == null) return "";
        if (diemSo >= 8.5) return "Giỏi";
        if (diemSo >= 7.0) return "Khá";
        if (diemSo >= 5.5) return "Trung bình";
        if (diemSo >= 4.0) return "Yếu";
        return "Kém";
    }
}
