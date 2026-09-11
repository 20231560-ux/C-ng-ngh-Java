package com.restaurant.dao;

import com.restaurant.model.NguyenLieu;
import java.util.ArrayList;
import java.util.List;

public class NguyenLieuDAO {

    private final List<NguyenLieu> danhSach = new ArrayList<>();

    public NguyenLieuDAO() {
        danhSach.add(new NguyenLieu(1, "Thịt bò", "Thịt", "Kg", 25, 5, 250000, "Nhà cung cấp A", ""));
        danhSach.add(new NguyenLieu(2, "Thịt gà", "Thịt", "Kg", 18, 5, 95000, "Nhà cung cấp A", ""));
        danhSach.add(new NguyenLieu(3, "Cá hồi", "Hải sản", "Kg", 8, 3, 320000, "Nhà cung cấp B", ""));
        danhSach.add(new NguyenLieu(4, "Tôm", "Hải sản", "Kg", 4, 5, 180000, "Nhà cung cấp B", ""));
        danhSach.add(new NguyenLieu(5, "Rau xà lách", "Rau củ", "Kg", 3, 5, 45000, "Nhà cung cấp C", ""));
        danhSach.add(new NguyenLieu(6, "Cà chua", "Rau củ", "Kg", 12, 4, 35000, "Nhà cung cấp C", ""));
        danhSach.add(new NguyenLieu(7, "Khoai tây", "Rau củ", "Kg", 15, 5, 30000, "Nhà cung cấp C", ""));
        danhSach.add(new NguyenLieu(8, "Phô mai", "Sữa", "Kg", 2, 3, 220000, "Nhà cung cấp D", ""));
        danhSach.add(new NguyenLieu(9, "Sữa tươi", "Sữa", "Lít", 20, 5, 32000, "Nhà cung cấp D", ""));
        danhSach.add(new NguyenLieu(10, "Dầu ăn", "Gia vị", "Lít", 10, 3, 45000, "Nhà cung cấp E", ""));
        danhSach.add(new NguyenLieu(11, "Nước mắm", "Gia vị", "Lít", 8, 2, 55000, "Nhà cung cấp E", ""));
        danhSach.add(new NguyenLieu(12, "Đường", "Gia vị", "Kg", 7, 2, 25000, "Nhà cung cấp E", ""));
        danhSach.add(new NguyenLieu(13, "Muối", "Gia vị", "Kg", 6, 2, 15000, "Nhà cung cấp E", ""));
        danhSach.add(new NguyenLieu(14, "Tiêu", "Gia vị", "Kg", 1, 2, 180000, "Nhà cung cấp E", ""));
        danhSach.add(new NguyenLieu(15, "Bột mì", "Bột", "Kg", 10, 3, 28000, "Nhà cung cấp F", ""));
    }

    public List<NguyenLieu> findAll() {
        return new ArrayList<>(danhSach);
    }

    public void insert(NguyenLieu nguyenLieu) {
        int maMoi = 1;

        for (NguyenLieu item : danhSach) {
            if (item.getMaNguyenLieu() >= maMoi) {
                maMoi = item.getMaNguyenLieu() + 1;
            }
        }

        nguyenLieu.setMaNguyenLieu(maMoi);
        danhSach.add(nguyenLieu);
    }

    public void update(NguyenLieu nguyenLieu) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getMaNguyenLieu() == nguyenLieu.getMaNguyenLieu()) {
                danhSach.set(i, nguyenLieu);
                return;
            }
        }
    }

    public void delete(int maNguyenLieu) {
        danhSach.removeIf(item -> item.getMaNguyenLieu() == maNguyenLieu);
    }
}