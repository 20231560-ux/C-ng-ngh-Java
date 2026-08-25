package vn.edu.eaut.lab10.repository;
import java.util.List; import jakarta.persistence.*; import vn.edu.eaut.lab10.config.JPAUtil; import vn.edu.eaut.lab10.model.Student;
public class StudentRepository {
 public List<Student> findAll(){EntityManager e=JPAUtil.getEntityManager();try{return e.createQuery("from Student order by id",Student.class).getResultList();}finally{e.close();}}
 public Student find(Integer id){EntityManager e=JPAUtil.getEntityManager();try{return e.find(Student.class,id);}finally{e.close();}}
 public void save(Student x){tx(em()->{em.persist(x);});}
 public void update(Student x){tx(em()->{em.merge(x);});}
 public void delete(Integer id){tx(em()->{Student x=em.find(Student.class,id);if(x!=null)em.remove(x);});}
 private EntityManager em(){return JPAUtil.getEntityManager();}
 private interface Work{void run(EntityManager e);}
 private void tx(Work w){EntityManager e=em();EntityTransaction t=e.getTransaction();try{t.begin();w.run(e);t.commit();}catch(RuntimeException ex){if(t.isActive())t.rollback();throw ex;}finally{e.close();}}
}