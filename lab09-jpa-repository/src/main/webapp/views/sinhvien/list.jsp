<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách sinh viên</title>
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

    <c:if test="${not empty sessionScope.thongBao}">
        <div class="alert alert-success">${sessionScope.thongBao}</div>
        <c:remove var="thongBao" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.loi}">
        <div class="alert alert-error">${sessionScope.loi}</div>
        <c:remove var="loi" scope="session"/>
    </c:if>

    <div class="card">
        <form class="search-bar" action="${pageContext.request.contextPath}/sinh-vien" method="get">
            <input type="text" name="keyword" placeholder="Tìm theo tên, mã sinh viên, lớp..." value="${keyword}">
            <button class="btn btn-primary" type="submit">Tìm kiếm</button>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/sinh-vien?action=new">+ Thêm sinh viên</a>
        </form>

        <table>
            <thead>
            <tr>
                <th>Mã SV</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>Ngày sinh</th>
                <th>Lớp</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="sv" items="${dsSinhVien}">
                <tr>
                    <td>${sv.maSinhVien}</td>
                    <td>${sv.hoTen}</td>
                    <td>${sv.email}</td>
                    <td>${sv.ngaySinh}</td>
                    <td>${sv.lopHoc != null ? sv.lopHoc.tenLop : '(chưa xếp lớp)'}</td>
                    <td>
                        <a class="btn btn-edit" href="${pageContext.request.contextPath}/sinh-vien?action=edit&id=${sv.id}">Sửa</a>
                        <a class="btn btn-delete" href="${pageContext.request.contextPath}/sinh-vien?action=delete&id=${sv.id}"
                           onclick="return confirm('Xóa sinh viên này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty dsSinhVien}">
                <tr><td colspan="6">Không có dữ liệu.</td></tr>
            </c:if>
            </tbody>
        </table>

        <div class="pagination">
            <c:forEach begin="0" end="${totalPages - 1}" var="p">
                <c:choose>
                    <c:when test="${p == page}">
                        <span class="active">${p + 1}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/sinh-vien?keyword=${keyword}&page=${p}">${p + 1}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
