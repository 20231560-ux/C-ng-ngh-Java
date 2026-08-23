<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Form sinh viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Lab 9 - Quản lý Sinh viên (JPA)</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/sinh-vien">Sinh viên</a>
        <a href="${pageContext.request.contextPath}/lop-hoc">Lớp học</a>
        <a href="${pageContext.request.contextPath}/diem">Điểm</a>
    </nav>
</header>
<div class="container">
    <div class="card">
        <h2>${sinhVien != null && sinhVien.id != null ? 'Sửa sinh viên' : 'Thêm sinh viên'}</h2>

        <c:if test="${not empty loi}">
            <div class="alert alert-error">${loi}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/sinh-vien" method="post">
            <c:if test="${sinhVien != null && sinhVien.id != null}">
                <input type="hidden" name="id" value="${sinhVien.id}">
            </c:if>

            <div class="form-group">
                <label>Mã sinh viên</label>
                <input type="text" name="maSinhVien" value="${sinhVien.maSinhVien}" required>
            </div>
            <div class="form-group">
                <label>Họ tên</label>
                <input type="text" name="hoTen" value="${sinhVien.hoTen}" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" value="${sinhVien.email}">
            </div>
            <div class="form-group">
                <label>Ngày sinh</label>
                <input type="date" name="ngaySinh" value="${sinhVien.ngaySinh}">
            </div>
            <div class="form-group">
                <label>Lớp</label>
                <select name="lopId">
                    <option value="">-- Chọn lớp --</option>
                    <c:forEach var="lop" items="${danhSachLop}">
                        <option value="${lop.id}"
                            ${sinhVien != null && sinhVien.lopHoc != null && sinhVien.lopHoc.id == lop.id ? 'selected' : ''}>
                            ${lop.tenLop}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button class="btn btn-primary" type="submit">Lưu</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/sinh-vien">Hủy</a>
        </form>
    </div>
</div>
</body>
</html>
