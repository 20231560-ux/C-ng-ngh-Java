package vn.edu.eaut.lab10.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.eaut.lab10.model.User;
import vn.edu.eaut.lab10.service.AuthService;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AuthService authService = new AuthService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        // Đăng xuất
        if ("logout".equals(action)) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        // Nếu truy cập /auth trực tiếp
        response.sendRedirect(
                request.getContextPath() + "/login.jsp"
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.trim().isEmpty()
                || password.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Vui lòng nhập email và mật khẩu."
            );

            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(request, response);

            return;
        }

        User user = authService.login(email, password);

        if (user == null) {

            request.setAttribute(
                    "error",
                    "Email hoặc mật khẩu không đúng."
            );

            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(request, response);

            return;
        }

        // Lưu user vào session
        request.getSession().setAttribute(
                "currentUser",
                user
        );

        // Vào Dashboard
        response.sendRedirect(
                request.getContextPath()
                + "/dashboard.jsp"
        );
    }
}