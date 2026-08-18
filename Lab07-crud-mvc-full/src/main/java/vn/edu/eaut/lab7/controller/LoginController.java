package vn.edu.eaut.lab7.controller;
import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.*;
@WebServlet("/login") public class LoginController extends HttpServlet{
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException{if("admin".equals(q.getParameter("username"))&&"123456".equals(q.getParameter("password"))){q.getSession().setAttribute("username","admin");p.sendRedirect(q.getContextPath()+"/admin/home.jsp");}else p.sendRedirect(q.getContextPath()+"/login.jsp?error=1");}
}