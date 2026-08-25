package vn.edu.eaut.lab10.repository;

import jakarta.persistence.EntityManager;
import vn.edu.eaut.lab10.config.JPAUtil;
import vn.edu.eaut.lab10.model.User;

public class UserRepository {
    public User findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}
