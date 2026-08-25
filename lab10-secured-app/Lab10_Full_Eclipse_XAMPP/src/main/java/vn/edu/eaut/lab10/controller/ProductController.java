package vn.edu.eaut.lab10.controller;
import java.io.*; import jakarta.servlet.*; import jakarta.servlet.annotation.*; import jakarta.servlet.http.*; import vn.edu.eaut.lab10.model.Product; import vn.edu.eaut.lab10.repository.ProductRepository;
@WebServlet("/staff/products") public class ProductController extends HttpServlet {
 private final ProductRepository r=new ProductRepository();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{String a=q.getParameter("action");if("delete".equals(a)){r.delete(Integer.parseInt(q.getParameter("id")));p.sendRedirect(q.getContextPath()+"/staff/products");return;}if("edit".equals(a))q.setAttribute("edit",r.find(Integer.parseInt(q.getParameter("id"))));q.setAttribute("items",r.findAll());q.getRequestDispatcher("/staff/product.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.setCharacterEncoding("UTF-8");Product x=new Product();String id=q.getParameter("id");x.setCode(q.getParameter("code"));
x.setName(q.getParameter("name"));
x.setPrice(Double.parseDouble(q.getParameter("price")));
x.setQuantity(Integer.parseInt(q.getParameter("quantity"))); if(id==null||id.isBlank())r.save(x);else{x.setId(Integer.parseInt(id));r.update(x);}p.sendRedirect(q.getContextPath()+"/staff/products");}
}