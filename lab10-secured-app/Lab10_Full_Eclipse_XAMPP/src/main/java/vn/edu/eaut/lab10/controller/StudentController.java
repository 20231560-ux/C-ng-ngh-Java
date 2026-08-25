package vn.edu.eaut.lab10.controller;
import java.io.*; import jakarta.servlet.*; import jakarta.servlet.annotation.*; import jakarta.servlet.http.*; import vn.edu.eaut.lab10.model.Student; import vn.edu.eaut.lab10.repository.StudentRepository;
@WebServlet("/staff/students") public class StudentController extends HttpServlet {
 private final StudentRepository r=new StudentRepository();
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{String a=q.getParameter("action");if("delete".equals(a)){r.delete(Integer.parseInt(q.getParameter("id")));p.sendRedirect(q.getContextPath()+"/staff/students");return;}if("edit".equals(a))q.setAttribute("edit",r.find(Integer.parseInt(q.getParameter("id"))));q.setAttribute("items",r.findAll());q.getRequestDispatcher("/staff/student.jsp").forward(q,p);}
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.setCharacterEncoding("UTF-8");Student x=new Student();String id=q.getParameter("id");x.setCode(q.getParameter("code"));
x.setFullName(q.getParameter("fullName"));
x.setEmail(q.getParameter("email"));
x.setMajor(q.getParameter("major"));if(id==null||id.isBlank())r.save(x);else{x.setId(Integer.parseInt(id));r.update(x);}p.sendRedirect(q.getContextPath()+"/staff/students");}
}