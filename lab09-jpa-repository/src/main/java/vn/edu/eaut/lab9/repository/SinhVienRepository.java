package vn.edu.eaut.lab9.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import vn.edu.eaut.lab9.model.SinhVien;

import java.util.List;

/**
 * Bài 3 + Bài 4: CRUD và JPQL cho SinhVien.
 * Bài 9: tìm kiếm + phân trang bằng setFirstResult / setMaxResults.
 */
public class SinhVienRepository extends BaseRepository<SinhVien, Integer> {

    public static final int PAGE_SIZE = 5;

    public SinhVienRepository() {
        super(SinhVien.class);
    }

    @Override
    public List<SinhVien> findAll() {
        EntityManager em = createEntityManager();
        try {
            // LEFT JOIN FETCH để lấy luôn LopHoc (kể cả khi lopHoc = null), tránh LazyInitializationException khi hiển thị JSP
            return em.createQuery(
                            "SELECT s FROM SinhVien s LEFT JOIN FETCH s.lopHoc ORDER BY s.id DESC",
                            SinhVien.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Bài 4 + Bài 9: tìm theo tên/lớp, có phân trang. page bắt đầu từ 0. */
    public List<SinhVien> search(String keyword, int page) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT s FROM SinhVien s " +
                    "LEFT JOIN FETCH s.lopHoc " +
                    "WHERE LOWER(s.hoTen) LIKE :kw " +
                    "OR LOWER(s.maSinhVien) LIKE :kw " +
                    "OR (s.lopHoc IS NOT NULL AND LOWER(s.lopHoc.tenLop) LIKE :kw) " +
                    "ORDER BY s.id DESC";
            TypedQuery<SinhVien> query = em.createQuery(jpql, SinhVien.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%");
            query.setFirstResult(page * PAGE_SIZE);
            query.setMaxResults(PAGE_SIZE);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countSearch(String keyword) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT COUNT(s) FROM SinhVien s " +
                    "WHERE LOWER(s.hoTen) LIKE :kw " +
                    "OR LOWER(s.maSinhVien) LIKE :kw " +
                    "OR (s.lopHoc IS NOT NULL AND LOWER(s.lopHoc.tenLop) LIKE :kw)";
            return em.createQuery(jpql, Long.class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /** Bài 10: kiểm tra trùng mã sinh viên trước khi thêm/sửa. */
    public boolean existsByMaSinhVien(String maSinhVien, Integer excludeId) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT COUNT(s) FROM SinhVien s WHERE s.maSinhVien = :ma " +
                    (excludeId != null ? "AND s.id <> :id" : "");
            TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                    .setParameter("ma", maSinhVien);
            if (excludeId != null) query.setParameter("id", excludeId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public List<SinhVien> findByLop(Integer lopId) {
        EntityManager em = createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM SinhVien s WHERE s.lopHoc.id = :lopId ORDER BY s.hoTen",
                            SinhVien.class)
                    .setParameter("lopId", lopId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
