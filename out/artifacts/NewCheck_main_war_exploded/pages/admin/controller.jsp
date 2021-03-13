<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Admin controller</title>
</head>
<body>
<td>${String.format('%s %s %s %s',"Hello, ", user.getFirstName(), "! Your role is ", user.getUserType())}</td>
<br/>
<td>${"Choose DB to work"}</td>
<br/>
<form action="/pages/admin/users_editor.jsp" >
    <p><input type="submit" value=" Users "></p>
</form>
<form action="/pages/admin/product_editor.jsp" >
    <p><input type="submit" value=" Production "></p>
</form>
</body>
</html>
