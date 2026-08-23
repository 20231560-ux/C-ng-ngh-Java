package vn.edu.eaut.lab9.listener;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.edu.eaut.lab9.config.JPAUtil;
import vn.edu.eaut.lab9.model.Diem;
import vn.edu.eaut.lab9.model.LopHoc;
import vn.edu.eaut.lab9.model.MonHoc;
import vn.edu.eaut.lab9.model.SinhVien;

import java.util.List;

@WebListener
public class DataSeedListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Long soLuongLop = em.createQuery("SELECT COUNT(l) FROM LopHoc l", Long.class)
                    .getSingleResult();

            if (soLuongLop == null || soLuongLop == 0) {
                tx.begin();
                LopHoc lop1 = new LopHoc("CNTT01", "Công nghệ thông tin K17.1", "CNTT");
                LopHoc lop2 = new LopHoc("CNTT02", "Công nghệ thông tin K17.2", "CNTT");
                em.persist(lop1);
                em.persist(lop2);

                SinhVien sv1 = new SinhVien("SV001", "Nguyễn Văn An", "an.nv@example.com", lop1);
                SinhVien sv2 = new SinhVien("SV002", "Trần Thị Bình", "binh.tt@example.com", lop1);
                SinhVien sv3 = new SinhVien("SV003", "Lê Văn Cường", "cuong.lv@example.com", lop2);
                em.persist(sv1);
                em.persist(sv2);
                em.persist(sv3);

                MonHoc mon1 = new MonHoc("IT3242", "Công nghệ Java", 3);
                MonHoc mon2 = new MonHoc("IT3101", "Cấu trúc dữ liệu và giải thuật", 3);
                em.persist(mon1);
                em.persist(mon2);
                tx.commit();
            }

            Long soLuongDiem = em.createQuery("SELECT COUNT(d) FROM Diem d", Long.class)
                    .getSingleResult();
            if (soLuongDiem == null || soLuongDiem == 0) {
                List<SinhVien> danhSachSV = em.createQuery("SELECT s FROM SinhVien s", SinhVien.class).getResultList();
                List<MonHoc> danhSachMon = em.createQuery("SELECT m FROM MonHoc m", MonHoc.class).getResultList();

                if (!danhSachSV.isEmpty() && !danhSachMon.isEmpty()) {
                    tx.begin();
                    double[] diemMau = {8.5, 7.0, 6.5, 9.0, 5.5, 4.0};
                    int i = 0;
                    for (SinhVien sv : danhSachSV) {
                        for (MonHoc mon : danhSachMon) {
                            em.persist(new Diem(sv, mon, diemMau[i % diemMau.length]));
                            i++;
                        }
                    }
                    tx.commit();
                }
            }
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}