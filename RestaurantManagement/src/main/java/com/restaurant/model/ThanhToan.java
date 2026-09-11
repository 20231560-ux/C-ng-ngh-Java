package com.restaurant.model;

import java.sql.Timestamp;

public class ThanhToan {
    private int maThanhToan;
    private int maDonHang;
    private String phuongThuc; // 'TIEN_MAT', 'CHUYEN_KHOAN', 'THE_NGAN_HANG', 'VI_DIEN_TU'
    private double soTen;
    private String trangThai; // 'DA_THANH_TOAN', 'CHO_XU_LY', 'HOAN_TIEN', 'THAT_BAI'
    private String maGiaoDich;
    private Timestamp thoiGianThanhToan;

    public ThanhToan() {
    }

    public ThanhToan(int maDonHang, String phuongThuc, double soTen, String trangThai, String maGiaoDich) {
        this.maDonHang = maDonHang;
        this.phuongThuc = phuongThuc;
        this.soTen = soTen;
        this.trangThai = trangThai;
        this.maGiaoDich = maGiaoDich;
    }

    public int getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(int maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public double getSoTien() {
        return soTen;
    }

    public void setSoTien(double soTen) {
        this.soTen = soTen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public Timestamp getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    public void setThoiGianThanhToan(Timestamp thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }
}
