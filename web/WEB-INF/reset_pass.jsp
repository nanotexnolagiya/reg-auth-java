<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Вход</title>
    <%@include file="/WEB-INF/include/header.jsp"%>
</head>
<body>
<form action="/reset_password" method="POST">
    <div class="form-group">
        <input type="text" class="form-control" name="email" required placeholder="Email" />
    </div>
    <c:if test="${not empty requestScope.errors}">
        <ul class="nav navbar alert alert-danger">
            <c:forEach items="${requestScope.errors}" var="error">
                <li><c:out value="${error}" /></li>
            </c:forEach>
        </ul>
    </c:if>
    <div class="form-group">
        <input type="submit" class="btn btn-primary btn-block" name="submit" value="Сбрось" />
    </div>
</form>

</body>
</html>
