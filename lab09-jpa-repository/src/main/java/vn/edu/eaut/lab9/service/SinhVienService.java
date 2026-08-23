package vn.edu.eaut.lab9.service;

import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.repository.SinhVienRepository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Bài 10: Service chịu trách nhiệm validate dữ liệu (mã trùng, email sai định dạng,
 * trường bắt buộc) trước khi gọi Repository, trả lỗi rõ ràng cho Controller hiển thị.
 */
public class SinhVienService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final SinhVienRepository repository = new SinhVienRepository();

    public List<SinhVien> findAll() {
        return repository.findAll();
    }

    public SinhVien findById(Integer id) {
        return repository.findById(id);
    }

    public List<SinhVien> search(String keyword, int page) {
        return repository.search(keyword, page);
    }

    public long countSearch(String keyword) {
        return repository.countSearch(keyword);
    }

    /** Trả về thông báo lỗi nếu dữ liệu không hợp lệ, null nếu hợp lệ. */
    public String validate(SinhVien sv, boolean isUpdate) {
        if (sv.getMaSinhVien() == null || sv.getMaSinhVien().isBlank()) {
            return "Mã sinh viên không được để trống.";
        }
        if (sv.getHoTen() == null || sv.getHoTen().isBlank()) {
            return "Họ tên không được để trống.";
        }
        if (sv.getEmail() != null && !sv.getEmail().isBlank()
                && !EMAIL_PATTERN.matcher(sv.getEmail()).matches()) {
            return "Email không đúng định dạng.";
        }
        Integer excludeId = isUpdate ? sv.getId() : null;
        if (repository.existsByMaSinhVien(sv.getMaSinhVien(), excludeId)) {
            return "Mã sinh viên [" + sv.getMaSinhVien() + "] đã tồn tại.";
        }
        return null;
    }

    public void save(SinhVien sv) {
        repository.save(sv);
    }

    public void update(SinhVien sv) {
        repository.update(sv);
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
