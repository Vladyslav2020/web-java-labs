<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.kpi.lab2.models.entities.User" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
          crossorigin="anonymous"/>
    <title>User's main page</title>
</head>
<body>
<div class="container">
    <h1>Hello ${user.name}</h1>

    <c:choose>
        <c:when test="${user.isAdmin == true}">
            <a href="${pageContext.request.contextPath}/railway-stations" class="btn btn-primary link-primary">Manage Railway Stations</a>
            <a href="${pageContext.request.contextPath}/railway-routes" class="btn btn-primary link-primary">Manage Railway Routes</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/railway-stations" class="btn btn-primary link-primary">View Railway Stations</a>
            <a href="${pageContext.request.contextPath}/railway-routes" class="btn btn-primary link-primary">Search Railway Routes</a>
            <a href="${pageContext.request.contextPath}/tickets" class="btn btn-primary link-primary">My Tickets</a>
        </c:otherwise>
    </c:choose>
    <form action="${pageContext.request.contextPath}/logout" class="mt-2" method="post">
        <button type="submit" class="btn btn-primary">Logout</button>
    </form>
</div>

</body>
</html>