package com.restaurant.model;

public class NguyenLieu {
    private int maNguyenLieu;
    private String tenNguyenLieu;
    private String nhom;
    private String donVi;
    private double soLuong;
    private double tonToiThieu;
    private double donGia;
    private String nhaCungCap;
    private String ghiChu;

    public NguyenLieu() {
    }

    public NguyenLieu(int maNguyenLieu, String tenNguyenLieu, String nhom, String donVi,
                      double soLuong, double tonToiThieu, double donGia,
                      String nhaCungCap, String ghiChu) {
        this.maNguyenLieu = maNguyenLieu;
        this.tenNguyenLieu = tenNguyenLieu;
        this.nhom = nhom;
        this.donVi = donVi;
        this.soLuong = soLuong;
        this.tonToiThieu = tonToiThieu;
        this.donGia = donGia;
        this.nhaCungCap = nhaCungCap;
        this.ghiChu = ghiChu;
    }

    public int getMaNguyenLieu() {
        return maNguyenLieu;
    }

    public void setMaNguyenLieu(int maNguyenLieu) {
        this.maNguyenLieu = maNguyenLieu;
    }

    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }

    public void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public double getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(double soLuong) {
        this.soLuong = soLuong;
    }

    public double getTonToiThieu() {
        return tonToiThieu;
    }

    public void setTonToiThieu(double tonToiThieu) {
        this.tonToiThieu = tonToiThieu;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        if (soLuong <= 0) {
            return "Hết hàng";
        }

        if (soLuong <= tonToiThieu) {
            return "Sắp hết";
        }

        return "Còn hàng";
    }

    public double getGiaTriTonKho() {
        return soLuong * donGia;
    }
}