package vn.edu.eaut.lab9.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.eaut.lab9.model.LopHoc;
import vn.edu.eaut.lab9.model.SinhVien;
import vn.edu.eaut.lab9.repository.LopHocRepository;
import vn.edu.eaut.lab9.repository.SinhVienRepository;
import vn.edu.eaut.lab9.service.SinhVienService;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/sinh-vien")
public class SinhVienController extends HttpServlet {

    private final SinhVienService service = new SinhVienService();
    private final LopHocRepository lopHocRepository = new LopHocRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                service.delete(Integer.parseInt(request.getParameter("id")));
                request.getSession().setAttribute("thongBao", "Xóa sinh viên thành công.");
            } catch (RuntimeException ex) {
                request.getSession().setAttribute("loi", "Xóa thất bại: " + ex.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/sinh-vien");
            return;
        }

        if ("edit".equals(action)) {
            SinhVien sv = service.findById(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("sinhVien", sv);
            request.setAttribute("danhSachLop", lopHocRepository.findAll());
            request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
            return;
        }

        if ("new".equals(action)) {
            request.setAttribute("danhSachLop", lopHocRepository.findAll());
            request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
            return;
        }

        // Danh sách + tìm kiếm + phân trang (Bài 9)
        String keyword = request.getParameter("keyword");
        int page = 0;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }
        if (keyword == null) keyword = "";

        long total = service.countSearch(keyword);
        int totalPages = (int) Math.ceil(total / (double) SinhVienRepository.PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        request.setAttribute("dsSinhVien", service.search(keyword, page));
        request.setAttribute("keyword", keyword);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/views/sinhvien/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        boolean isUpdate = idParam != null && !idParam.isBlank();

        SinhVien sv = isUpdate ? service.findById(Integer.parseInt(idParam)) : new SinhVien();
        sv.setMaSinhVien(request.getParameter("maSinhVien"));
        sv.setHoTen(request.getParameter("hoTen"));
        sv.setEmail(request.getParameter("email"));

        String ngaySinhStr = request.getParameter("ngaySinh");
        if (ngaySinhStr != null && !ngaySinhStr.isBlank()) {
            sv.setNgaySinh(LocalDate.parse(ngaySinhStr));
        }

        String lopIdStr = request.getParameter("lopId");
        if (lopIdStr != null && !lopIdStr.isBlank()) {
            LopHoc lop = new LopHoc();
            lop.setId(Integer.parseInt(lopIdStr));
            sv.setLopHoc(lop);
        } else {
            sv.setLopHoc(null);
        }

        String loiValidate = service.validate(sv, isUpdate);
        if (loiValidate != null) {
            request.setAttribute("loi", loiValidate);
            request.setAttribute("sinhVien", sv);
            request.setAttribute("danhSachLop", lopHocRepository.findAll());
            request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
            return;
        }

        try {
            if (isUpdate) {
                service.update(sv);
                request.getSession().setAttribute("thongBao", "Cập nhật sinh viên thành công.");
            } else {
                service.save(sv);
                request.getSession().setAttribute("thongBao", "Thêm sinh viên thành công.");
            }
        } catch (RuntimeException ex) {
            request.setAttribute("loi", "Có lỗi xảy ra: " + ex.getMessage());
            request.setAttribute("sinhVien", sv);
            request.setAttribute("danhSachLop", lopHocRepository.findAll());
            request.getRequestDispatcher("/views/sinhvien/form.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/sinh-vien");
    }
}
