package vn.edu.eaut.lab9.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Lớp dùng chung để lấy EntityManagerFactory cho toàn bộ ứng dụng.
 * EntityManagerFactory chỉ nên khởi tạo MỘT LẦN vì chi phí tạo rất lớn.
 */
public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("lab09PU");

    private JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
