package vn.edu.eaut.lab10.repository;
import java.util.*; import jakarta.persistence.*; import vn.edu.eaut.lab10.config.JPAUtil; import vn.edu.eaut.lab10.model.User;
public class UserRepository {
 public User findByEmail(String x){EntityManager e=JPAUtil.getEntityManager();try{return e.createQuery("from User u where u.email=:x",User.class).setParameter("x",x).getResultStream().findFirst().orElse(null);}finally{e.close();}}
 public List<User> findAll(){EntityManager e=JPAUtil.getEntityManager();try{return e.createQuery("from User order by id",User.class).getResultList();}finally{e.close();}}
 public User find(Integer id){EntityManager e=JPAUtil.getEntityManager();try{return e.find(User.class,id);}finally{e.close();}}
 public void save(User x){tx(e->e.persist(x));} public void update(User x){tx(e->e.merge(x));} public void delete(Integer id){tx(e->{User x=e.find(User.class,id);if(x!=null)e.remove(x);});}
 private interface Work{void run(EntityManager e);} private void tx(Work w){EntityManager e=JPAUtil.getEntityManager();EntityTransaction t=e.getTransaction();try{t.begin();w.run(e);t.commit();}catch(RuntimeException ex){if(t.isActive())t.rollback();throw ex;}finally{e.close();}}
}