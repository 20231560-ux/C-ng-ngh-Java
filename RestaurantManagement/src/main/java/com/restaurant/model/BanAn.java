package com.restaurant.model;

public class BanAn {

    private int maBan;
    private String maBanSo;
    private String tenBan;
    private int soCho;
    private String trangThai;
    private String khuVuc;
    private String ghiChu;

    public BanAn() {
    }

    public BanAn(
            int maBan,
            String tenBan,
            int soCho,
            String trangThai,
            String khuVuc
    ) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.soCho = soCho;
        this.trangThai = trangThai;
        this.khuVuc = khuVuc;
    }

    public BanAn(
            int maBan,
            String maBanSo,
            String tenBan,
            int soCho,
            String trangThai,
            String khuVuc,
            String ghiChu
    ) {
        this.maBan = maBan;
        this.maBanSo = maBanSo;
        this.tenBan = tenBan;
        this.soCho = soCho;
        this.trangThai = trangThai;
        this.khuVuc = khuVuc;
        this.ghiChu = ghiChu;
    }

    public int getMaBan() {
        return maBan;
    }

    public void setMaBan(int maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) {
        this.soCho = soCho;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    public String getMaBanSo() {
        return maBanSo;
    }

    public void setMaBanSo(String maBanSo) {
        this.maBanSo = maBanSo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return (tenBan != null ? tenBan : "Bàn " + maBan) + (khuVuc != null ? " (" + khuVuc + ")" : "");
    }
}