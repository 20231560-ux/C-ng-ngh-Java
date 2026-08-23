<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lab 9 - JPA Repository Transaction</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header>
    <h1>Lab 9 - Tích hợp JPA: Entity, Repository, Transaction</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/sinh-vien">Sinh viên</a>
        <a href="${pageContext.request.contextPath}/lop-hoc">Lớp học</a>
        <a href="${pageContext.request.contextPath}/diem">Điểm</a>
    </nav>
</header>
<div class="container">
    <div class="card">
        <h2>Chào mừng đến với ứng dụng quản lý Sinh viên - JPA</h2>
        <p>Ứng dụng minh họa 3 module dữ liệu thật dùng JPA: <b>Sinh viên</b>, <b>Lớp học</b> và <b>Điểm</b>,
            có Entity, Repository, Transaction (begin/commit/rollback) và JPQL tìm kiếm / phân trang.</p>
        <p>Chọn một module ở thanh điều hướng phía trên để bắt đầu.</p>
    </div>
</div>
</body>
</html>
