<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="vn.edu.eaut.lab10.model.User" %>
<%
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
</head>
<body>
<h1>Dashboard</h1>
<h3>Xin chào: <%= user.getFullName() %></h3>
<p>Email: <%= user.getEmail() %></p>
<p>Role: <%= user.getRole() %></p>
<hr>
<a href="<%=request.getContextPath()%>/admin/index.jsp">Quản trị ADMIN</a><br><br>
<a href="<%=request.getContextPath()%>/staff/index.jsp">Nghiệp vụ STAFF</a><br><br>
<a href="<%=request.getContextPath()%>/user/index.jsp">Trang USER</a><br><br>
<a href="<%=request.getContextPath()%>/auth?action=logout">Đăng xuất</a>
</body>
</html>
