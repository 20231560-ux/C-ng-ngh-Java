package com.restaurant.model;

public class MonAn {

    private int maMon;
    private String maMonAn;
    private int maDanhMuc;
    private String tenMon;
    private String danhMuc;
    private double gia;
    private double giaVon;
    private String donVi;
    private String moTa;
    private String duongDanAnh;
    private int luotBan;
    private boolean dangBan;

    public MonAn() {
    }

    public MonAn(
            int maMon,
            String tenMon,
            String danhMuc,
            double gia,
            String donVi,
            String moTa,
            boolean dangBan
    ) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.danhMuc = danhMuc;
        this.gia = gia;
        this.donVi = donVi;
        this.moTa = moTa;
        this.dangBan = dangBan;
    }

    public MonAn(
            int maMon,
            String maMonAn,
            int maDanhMuc,
            String tenMon,
            String danhMuc,
            double gia,
            double giaVon,
            String donVi,
            String moTa,
            String duongDanAnh,
            int luotBan,
            boolean dangBan
    ) {
        this.maMon = maMon;
        this.maMonAn = maMonAn;
        this.maDanhMuc = maDanhMuc;
        this.tenMon = tenMon;
        this.danhMuc = danhMuc;
        this.gia = gia;
        this.giaVon = giaVon;
        this.donVi = donVi;
        this.moTa = moTa;
        this.duongDanAnh = duongDanAnh;
        this.luotBan = luotBan;
        this.dangBan = dangBan;
    }

    public int getMaMon() {
        return maMon;
    }

    public void setMaMon(int maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isDangBan() {
        return dangBan;
    }

    public void setDangBan(boolean dangBan) {
        this.dangBan = dangBan;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public double getGiaVon() {
        return giaVon;
    }

    public void setGiaVon(double giaVon) {
        this.giaVon = giaVon;
    }

    public double getGiaBan() {
        return gia;
    }

    public void setGiaBan(double giaBan) {
        this.gia = giaBan;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }

    public int getLuotBan() {
        return luotBan;
    }

    public void setLuotBan(int luotBan) {
        this.luotBan = luotBan;
    }

    @Override
    public String toString() {
        return tenMon;
    }
}