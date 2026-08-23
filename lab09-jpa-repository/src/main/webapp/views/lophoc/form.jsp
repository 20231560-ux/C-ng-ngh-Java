<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Form lớp học</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Lab 9 - Quản lý Lớp học (JPA)</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/sinh-vien">Sinh viên</a>
        <a href="${pageContext.request.contextPath}/lop-hoc">Lớp học</a>
        <a href="${pageContext.request.contextPath}/diem">Điểm</a>
    </nav>
</header>
<div class="container">
    <div class="card">
        <h2>${lopHoc != null && lopHoc.id != null ? 'Sửa lớp học' : 'Thêm lớp học'}</h2>

        <c:if test="${not empty loi}">
            <div class="alert alert-error">${loi}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/lop-hoc" method="post">
            <c:if test="${lopHoc != null && lopHoc.id != null}">
                <input type="hidden" name="id" value="${lopHoc.id}">
            </c:if>
            <div class="form-group">
                <label>Mã lớp</label>
                <input type="text" name="maLop" value="${lopHoc.maLop}" required>
            </div>
            <div class="form-group">
                <label>Tên lớp</label>
                <input type="text" name="tenLop" value="${lopHoc.tenLop}" required>
            </div>
            <div class="form-group">
                <label>Khoa</label>
                <input type="text" name="khoa" value="${lopHoc.khoa}">
            </div>
            <button class="btn btn-primary" type="submit">Lưu</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/lop-hoc">Hủy</a>
        </form>
    </div>
</div>
</body>
</html>
