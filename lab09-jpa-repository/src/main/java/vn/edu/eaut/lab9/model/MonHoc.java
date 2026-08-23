package vn.edu.eaut.lab9.model;

import jakarta.persistence.*;

/**
 * Bài 7: Entity Môn học.
 */
@Entity
@Table(name = "mon_hoc")
public class MonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_mon", nullable = false, unique = true, length = 20)
    private String maMon;

    @Column(name = "ten_mon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "so_tin_chi")
    private Integer soTinChi;

    public MonHoc() {
    }

    public MonHoc(String maMon, String tenMon, Integer soTinChi) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public Integer getSoTinChi() { return soTinChi; }
    public void setSoTinChi(Integer soTinChi) { this.soTinChi = soTinChi; }
}
