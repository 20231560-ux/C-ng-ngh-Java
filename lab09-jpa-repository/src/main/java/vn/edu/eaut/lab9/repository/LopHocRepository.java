package vn.edu.eaut.lab9.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import vn.edu.eaut.lab9.model.LopHoc;

import java.util.List;

public class LopHocRepository extends BaseRepository<LopHoc, Integer> {

    public LopHocRepository() {
        super(LopHoc.class);
    }

    @Override
    public List<LopHoc> findAll() {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT DISTINCT l FROM LopHoc l LEFT JOIN FETCH l.danhSachSinhVien ORDER BY l.id";
            return em.createQuery(jpql, LopHoc.class).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existsByMaLop(String maLop, Integer excludeId) {
        EntityManager em = createEntityManager();
        try {
            String jpql = "SELECT COUNT(l) FROM LopHoc l WHERE l.maLop = :ma" +
                    (excludeId != null ? " AND l.id <> :id" : "");
            TypedQuery<Long> query = em.createQuery(jpql, Long.class).setParameter("ma", maLop);
            if (excludeId != null) query.setParameter("id", excludeId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}