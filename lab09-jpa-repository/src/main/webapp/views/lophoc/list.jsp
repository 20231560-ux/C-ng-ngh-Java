<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách lớp học</title>
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
    <c:if test="${not empty sessionScope.thongBao}">
        <div class="alert alert-success">${sessionScope.thongBao}</div>
        <c:remove var="thongBao" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.loi}">
        <div class="alert alert-error">${sessionScope.loi}</div>
        <c:remove var="loi" scope="session"/>
    </c:if>

    <div class="card">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/lop-hoc?action=new">+ Thêm lớp học</a>
        <br><br>
        <table>
            <thead>
            <tr><th>Mã lớp</th><th>Tên lớp</th><th>Khoa</th><th>Số SV</th><th>Hành động</th></tr>
            </thead>
            <tbody>
            <c:forEach var="lop" items="${dsLopHoc}">
                <tr>
                    <td>${lop.maLop}</td>
                    <td>${lop.tenLop}</td>
                    <td>${lop.khoa}</td>
                    <td>${lop.danhSachSinhVien.size()}</td>
                    <td>
                        <a class="btn btn-edit" href="${pageContext.request.contextPath}/lop-hoc?action=edit&id=${lop.id}">Sửa</a>
                        <a class="btn btn-delete" href="${pageContext.request.contextPath}/lop-hoc?action=delete&id=${lop.id}"
                           onclick="return confirm('Xóa lớp này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty dsLopHoc}">
                <tr><td colspan="5">Không có dữ liệu.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
