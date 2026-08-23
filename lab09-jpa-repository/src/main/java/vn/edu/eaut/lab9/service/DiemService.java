package vn.edu.eaut.lab9.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.eaut.lab9.config.JPAUtil;
import vn.edu.eaut.lab9.model.Diem;
import vn.edu.eaut.lab9.model.MonHoc;
import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.repository.DiemRepository;
import vn.edu.eaut.lab9.repository.MonHocRepository;

import java.util.List;

public class DiemService {

    private final DiemRepository diemRepository = new DiemRepository();
    private final MonHocRepository monHocRepository = new MonHocRepository();

    public List<Diem> findAll() {
        return diemRepository.findAll();
    }

    public List<MonHoc> findAllMonHoc() {
        return monHocRepository.findAll();
    }

    public String validate(Double diemSo) {
        if (diemSo == null) return "Điểm không được để trống.";
        if (diemSo < 0 || diemSo > 10) return "Điểm phải nằm trong khoảng 0 - 10.";
        return null;
    }

    public void save(Diem diem) {
        diemRepository.save(diem);
    }

    public void delete(Integer id) {
        diemRepository.delete(id);
    }

    /**
     * Bài 11: Thêm sinh viên mới VÀ tạo điểm mặc định (điểm = 0) cho tất cả môn học
     * hiện có, trong CÙNG MỘT transaction. Nếu bất kỳ thao tác nào lỗi thì rollback
     * toàn bộ (sinh viên cũng không được lưu).
     */
    public void themSinhVienVaDiemMacDinh(SinhVien sinhVien) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1) Lưu sinh viên
            em.persist(sinhVien);

            // 2) Lấy danh sách môn học hiện có và tạo điểm mặc định = 0 cho từng môn
            List<MonHoc> danhSachMon = em.createQuery(
                    "SELECT m FROM MonHoc m", MonHoc.class).getResultList();

            for (MonHoc mon : danhSachMon) {
                Diem diem = new Diem(sinhVien, mon, 0.0);
                em.persist(diem);
            }

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex; // ném lại để Controller/Servlet hiển thị lỗi cho người dùng
        } finally {
            em.close();
        }
    }
}
