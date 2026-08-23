package vn.edu.eaut.lab9.service;

import vn.edu.eaut.lab9.model.LopHoc;
import vn.edu.eaut.lab9.repository.LopHocRepository;
import vn.edu.eaut.lab9.repository.SinhVienRepository;

import java.util.List;

public class LopHocService {

    private final LopHocRepository repository = new LopHocRepository();
    private final SinhVienRepository sinhVienRepository = new SinhVienRepository();

    public List<LopHoc> findAll() {
        return repository.findAll();
    }

    public LopHoc findById(Integer id) {
        return repository.findById(id);
    }

    public String validate(LopHoc lop, boolean isUpdate) {
        if (lop.getMaLop() == null || lop.getMaLop().isBlank()) {
            return "Mã lớp không được để trống.";
        }
        if (lop.getTenLop() == null || lop.getTenLop().isBlank()) {
            return "Tên lớp không được để trống.";
        }
        Integer excludeId = isUpdate ? lop.getId() : null;
        if (repository.existsByMaLop(lop.getMaLop(), excludeId)) {
            return "Mã lớp [" + lop.getMaLop() + "] đã tồn tại.";
        }
        return null;
    }

    public void save(LopHoc lop) {
        repository.save(lop);
    }

    public void update(LopHoc lop) {
        repository.update(lop);
    }

    /** Chỉ cho xóa lớp khi không còn sinh viên nào thuộc lớp đó. */
    public String delete(Integer id) {
        if (!sinhVienRepository.findByLop(id).isEmpty()) {
            return "Không thể xóa lớp vì vẫn còn sinh viên thuộc lớp này.";
        }
        repository.delete(id);
        return null;
    }
}
