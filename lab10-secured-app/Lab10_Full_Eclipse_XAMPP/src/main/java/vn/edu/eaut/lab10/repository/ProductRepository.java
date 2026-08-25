package vn.edu.eaut.lab10.repository;
import java.util.List; import jakarta.persistence.*; import vn.edu.eaut.lab10.config.JPAUtil; import vn.edu.eaut.lab10.model.Product;
public class ProductRepository {
 public List<Product> findAll(){EntityManager e=JPAUtil.getEntityManager();try{return e.createQuery("from Product order by id",Product.class).getResultList();}finally{e.close();}}
 public Product find(Integer id){EntityManager e=JPAUtil.getEntityManager();try{return e.find(Product.class,id);}finally{e.close();}}
 public void save(Product x){tx(em()->{em.persist(x);});}
 public void update(Product x){tx(em()->{em.merge(x);});}
 public void delete(Integer id){tx(em()->{Product x=em.find(Product.class,id);if(x!=null)em.remove(x);});}
 private EntityManager em(){return JPAUtil.getEntityManager();}
 private interface Work{void run(EntityManager e);}
 private void tx(Work w){EntityManager e=em();EntityTransaction t=e.getTransaction();try{t.begin();w.run(e);t.commit();}catch(RuntimeException ex){if(t.isActive())t.rollback();throw ex;}finally{e.close();}}
}