package vn.edu.eaut.lab10.controller;
import java.io.*; import jakarta.servlet.*; import jakarta.servlet.annotation.*; import jakarta.servlet.http.*; import vn.edu.eaut.lab10.model.User; import vn.edu.eaut.lab10.service.AuthService;
@WebServlet("/auth") public class AuthController extends HttpServlet{
 private final AuthService s=new AuthService();
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{q.setCharacterEncoding("UTF-8");User u=s.login(q.getParameter("email"),q.getParameter("password"));if(u==null){q.setAttribute("error","Email hoặc mật khẩu không đúng / tài khoản bị khóa");q.getRequestDispatcher("/login.jsp").forward(q,p);return;}q.getSession().setAttribute("currentUser",u);responseRedirect(q,p,"/dashboard.jsp");}
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws IOException{if("logout".equals(q.getParameter("action"))){HttpSession s=q.getSession(false);if(s!=null)s.invalidate();}responseRedirect(q,p,"/login.jsp");}
 private void responseRedirect(HttpServletRequest q,HttpServletResponse p,String x)throws IOException{p.sendRedirect(q.getContextPath()+x);}
}