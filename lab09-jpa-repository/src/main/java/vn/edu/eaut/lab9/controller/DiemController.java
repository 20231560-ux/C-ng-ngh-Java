package vn.edu.eaut.lab9.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab9.model.Diem;
import vn.edu.eaut.lab9.model.MonHoc;
import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.repository.SinhVienRepository;
import vn.edu.eaut.lab9.service.DiemService;

import java.io.IOException;

@WebServlet("/diem")
public class DiemController extends HttpServlet {

    private final DiemService service = new DiemService();
    private final SinhVienRepository sinhVienRepository = new SinhVienRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            service.delete(Integer.parseInt(request.getParameter("id")));
            request.getSession().setAttribute("thongBao", "Xóa điểm thành công.");
            response.sendRedirect(request.getContextPath() + "/diem");
            return;
        }

        if ("new".equals(action)) {
            request.setAttribute("dsSinhVien", sinhVienRepository.findAll());
            request.setAttribute("dsMonHoc", service.findAllMonHoc());
            request.getRequestDispatcher("/views/diem/form.jsp").forward(request, response);
            return;
        }

        request.setAttribute("dsDiem", service.findAll());
        request.getRequestDispatcher("/views/diem/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        Integer sinhVienId = Integer.parseInt(request.getParameter("sinhVienId"));
        Integer monHocId = Integer.parseInt(request.getParameter("monHocId"));
        Double diemSo = null;
        try {
            diemSo = Double.parseDouble(request.getParameter("diemSo"));
        } catch (NumberFormatException ignored) {
        }

        String loiValidate = service.validate(diemSo);
        if (loiValidate != null) {
            request.setAttribute("loi", loiValidate);
            request.setAttribute("dsSinhVien", sinhVienRepository.findAll());
            request.setAttribute("dsMonHoc", service.findAllMonHoc());
            request.getRequestDispatcher("/views/diem/form.jsp").forward(request, response);
            return;
        }

        SinhVien sv = new SinhVien();
        sv.setId(sinhVienId);
        MonHoc mon = new MonHoc();
        mon.setId(monHocId);

        try {
            service.save(new Diem(sv, mon, diemSo));
            request.getSession().setAttribute("thongBao", "Thêm điểm thành công.");
        } catch (RuntimeException ex) {
            request.setAttribute("loi", "Có lỗi xảy ra: " + ex.getMessage());
            request.setAttribute("dsSinhVien", sinhVienRepository.findAll());
            request.setAttribute("dsMonHoc", service.findAllMonHoc());
            request.getRequestDispatcher("/views/diem/form.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/diem");
    }
}
