<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách điểm</title>
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
    <c:if test="${not empty sessionScope.thongBao}">
        <div class="alert alert-success">${sessionScope.thongBao}</div>
        <c:remove var="thongBao" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.loi}">
        <div class="alert alert-error">${sessionScope.loi}</div>
        <c:remove var="loi" scope="session"/>
    </c:if>

    <div class="card">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/diem?action=new">+ Thêm điểm</a>
        <br><br>
        <table>
            <thead>
            <tr><th>Sinh viên</th><th>Môn học</th><th>Điểm</th><th>Xếp loại</th><th>Hành động</th></tr>
            </thead>
            <tbody>
            <c:forEach var="d" items="${dsDiem}">
                <tr>
                    <td>${d.sinhVien.hoTen} (${d.sinhVien.maSinhVien})</td>
                    <td>${d.monHoc.tenMon}</td>
                    <td>${d.diemSo}</td>
                    <td>
                        <c:choose>
                            <c:when test="${d.xepLoai == 'Giỏi'}"><span class="badge badge-gioi">${d.xepLoai}</span></c:when>
                            <c:when test="${d.xepLoai == 'Khá'}"><span class="badge badge-kha">${d.xepLoai}</span></c:when>
                            <c:when test="${d.xepLoai == 'Trung bình'}"><span class="badge badge-tb">${d.xepLoai}</span></c:when>
                            <c:otherwise><span class="badge badge-yeu">${d.xepLoai}</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a class="btn btn-delete" href="${pageContext.request.contextPath}/diem?action=delete&id=${d.id}"
                           onclick="return confirm('Xóa điểm này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty dsDiem}">
                <tr><td colspan="5">Không có dữ liệu.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
