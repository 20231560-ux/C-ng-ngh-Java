package vn.edu.eaut.lab9.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab9.model.LopHoc;
import vn.edu.eaut.lab9.service.LopHocService;

import java.io.IOException;

@WebServlet("/lop-hoc")
public class LopHocController extends HttpServlet {

    private final LopHocService service = new LopHocService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String loi = service.delete(Integer.parseInt(request.getParameter("id")));
            if (loi != null) {
                request.getSession().setAttribute("loi", loi);
            } else {
                request.getSession().setAttribute("thongBao", "Xóa lớp học thành công.");
            }
            response.sendRedirect(request.getContextPath() + "/lop-hoc");
            return;
        }

        if ("edit".equals(action)) {
            request.setAttribute("lopHoc", service.findById(Integer.parseInt(request.getParameter("id"))));
            request.getRequestDispatcher("/views/lophoc/form.jsp").forward(request, response);
            return;
        }

        if ("new".equals(action)) {
            request.getRequestDispatcher("/views/lophoc/form.jsp").forward(request, response);
            return;
        }

        request.setAttribute("dsLopHoc", service.findAll());
        request.getRequestDispatcher("/views/lophoc/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");
        boolean isUpdate = idParam != null && !idParam.isBlank();

        LopHoc lop = isUpdate ? service.findById(Integer.parseInt(idParam)) : new LopHoc();
        lop.setMaLop(request.getParameter("maLop"));
        lop.setTenLop(request.getParameter("tenLop"));
        lop.setKhoa(request.getParameter("khoa"));

        String loiValidate = service.validate(lop, isUpdate);
        if (loiValidate != null) {
            request.setAttribute("loi", loiValidate);
            request.setAttribute("lopHoc", lop);
            request.getRequestDispatcher("/views/lophoc/form.jsp").forward(request, response);
            return;
        }

        if (isUpdate) {
            service.update(lop);
            request.getSession().setAttribute("thongBao", "Cập nhật lớp học thành công.");
        } else {
            service.save(lop);
            request.getSession().setAttribute("thongBao", "Thêm lớp học thành công.");
        }

        response.sendRedirect(request.getContextPath() + "/lop-hoc");
    }
}
