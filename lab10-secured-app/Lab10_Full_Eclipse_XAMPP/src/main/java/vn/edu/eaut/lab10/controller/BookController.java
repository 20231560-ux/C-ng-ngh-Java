package vn.edu.eaut.lab10.controller;
import java.io.*; import jakarta.servlet.*; import jakarta.servlet.annotation.*; import jakarta.servlet.http.*; import vn.edu.eaut.lab10.model.Book; import vn.edu.eaut.lab10.repository.BookRepository;
@WebServlet("/staff/books") public class BookController extends HttpServlet {
 private final BookRepository r=new BookRepository();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{String a=q.getParameter("action");if("delete".equals(a)){r.delete(Integer.parseInt(q.getParameter("id")));p.sendRedirect(q.getContextPath()+"/staff/books");return;}if("edit".equals(a))q.setAttribute("edit",r.find(Integer.parseInt(q.getParameter("id"))));q.setAttribute("items",r.findAll());q.getRequestDispatcher("/staff/book.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.setCharacterEncoding("UTF-8");Book x=new Book();String id=q.getParameter("id");x.setCode(q.getParameter("code"));
x.setTitle(q.getParameter("title"));
x.setAuthor(q.getParameter("author"));
x.setQuantity(Integer.parseInt(q.getParameter("quantity"))); if(id==null||id.isBlank())r.save(x);else{x.setId(Integer.parseInt(id));r.update(x);}p.sendRedirect(q.getContextPath()+"/staff/books");}
}