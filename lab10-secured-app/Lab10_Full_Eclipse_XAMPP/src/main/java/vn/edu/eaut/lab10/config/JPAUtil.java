package vn.edu.eaut.lab10.config;
import jakarta.persistence.*;
public class JPAUtil {
 private static final EntityManagerFactory emf=Persistence.createEntityManagerFactory("lab10PU");
 public static EntityManagerFactory getEntityManagerFactory(){return emf;}
 public static EntityManager getEntityManager(){return emf.createEntityManager();}
}