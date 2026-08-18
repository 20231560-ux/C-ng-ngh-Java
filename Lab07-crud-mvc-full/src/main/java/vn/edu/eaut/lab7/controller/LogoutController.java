package vn.edu.eaut.lab7.controller;
import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.*;
@WebServlet("/logout") public class LogoutController extends HttpServlet{protected void doGet(HttpServletRequest q,HttpServletResponse p)throws IOException{HttpSession s=q.getSession(false);if(s!=null)s.invalidate();p.sendRedirect(q.getContextPath()+"/");}}