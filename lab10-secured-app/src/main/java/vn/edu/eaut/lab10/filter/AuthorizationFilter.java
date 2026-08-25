package vn.edu.eaut.lab10.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import vn.edu.eaut.lab10.model.Role;
import vn.edu.eaut.lab10.model.User;

@WebFilter(urlPatterns = {"/admin/*", "/staff/*"})
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        User user = (User) request.getSession().getAttribute("currentUser");
        String path = request.getRequestURI();

        if (path.contains("/admin/") && user.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/error/403.jsp");
            return;
        }

        if (path.contains("/staff/")
                && !(user.getRole() == Role.ADMIN || user.getRole() == Role.STAFF)) {
            response.sendRedirect(request.getContextPath() + "/error/403.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
