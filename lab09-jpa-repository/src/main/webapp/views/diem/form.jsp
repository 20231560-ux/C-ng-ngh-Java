<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm điểm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Lab 9 - Quản lý Điểm (JPA)</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/sinh-vien">Sinh viên</a>
        <a href="${pageContext.request.contextPath}/lop-hoc">Lớp học</a>
        <a href="${pageContext.request.contextPath}/diem">Điểm</a>
    </nav>
</header>
<div class="container">
    <div class="card">
        <h2>Thêm điểm</h2>

        <c:if test="${not empty loi}">
            <div class="alert alert-error">${loi}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/diem" method="post">
            <div class="form-group">
                <label>Sinh viên</label>
                <select name="sinhVienId" required>
                    <c:forEach var="sv" items="${dsSinhVien}">
                        <option value="${sv.id}">${sv.hoTen} (${sv.maSinhVien})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Môn học</label>
                <select name="monHocId" required>
                    <c:forEach var="mon" items="${dsMonHoc}">
                        <option value="${mon.id}">${mon.tenMon}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Điểm (0 - 10)</label>
                <input type="number" step="0.1" min="0" max="10" name="diemSo" required>
            </div>
            <button class="btn btn-primary" type="submit">Lưu</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/diem">Hủy</a>
        </form>
    </div>
</div>
</body>
</html>
