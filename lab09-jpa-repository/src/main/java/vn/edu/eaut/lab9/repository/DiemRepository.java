package vn.edu.eaut.lab9.repository;

import jakarta.persistence.EntityManager;
import vn.edu.eaut.lab9.model.Diem;

import java.util.List;

public class DiemRepository extends BaseRepository<Diem, Integer> {

    public DiemRepository() {
        super(Diem.class);
    }

    @Override
    public List<Diem> findAll() {
        EntityManager em = createEntityManager();
        try {
            // JOIN FETCH để lấy luôn SinhVien và MonHoc, tránh lỗi LazyInitializationException khi hiển thị JSP
            String jpql = "SELECT d FROM Diem d JOIN FETCH d.sinhVien JOIN FETCH d.monHoc ORDER BY d.id DESC";
            return em.createQuery(jpql, Diem.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Diem> findBySinhVien(Integer sinhVienId) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT d FROM Diem d JOIN FETCH d.monHoc WHERE d.sinhVien.id = :svId";
            return em.createQuery(jpql, Diem.class)
                    .setParameter("svId", sinhVienId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
