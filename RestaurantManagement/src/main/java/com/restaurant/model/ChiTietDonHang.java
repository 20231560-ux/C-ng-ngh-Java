package com.restaurant.model;

public class ChiTietDonHang {
    private int maChiTiet;
    private int maDonHang;
    private int maMon;
    private int soLuong;
    private double donGia;
    private String ghiChu;
    private String trangThaiMon; // 'CHO_CUNG_UNG', 'DANG_CHE_BIEN', 'HOAN_THANH', 'DA_PHUC_VU'

    // Helper display fields
    private String tenMon;
    private String donViTinh;
    private String danhMuc;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int maDonHang, int maMon, int soLuong, double donGia, String ghiChu) {
        this.maDonHang = maDonHang;
        this.maMon = maMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
        this.trangThaiMon = "CHO_CUNG_UNG";
    }

    public int getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(int maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaMon() {
        return maMon;
    }

    public void setMaMon(int maMon) {
        this.maMon = maMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThaiMon() {
        return trangThaiMon;
    }

    public void setTrangThaiMon(String trangThaiMon) {
        this.trangThaiMon = trangThaiMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }
}
