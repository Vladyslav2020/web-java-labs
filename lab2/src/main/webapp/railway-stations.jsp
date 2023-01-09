<%@ page import="com.kpi.lab2.models.entities.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>Railway Stations</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/user.jsp" class="btn mt-2 btn-primary link-primary">Back</a>
    <h1>Railway Stations</h1>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Name</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${railwayStations}" var="railwayStation">
            <tr>
                <th scope="row">
                    <c:choose>
                        <c:when test="${user.isAdmin == true}">
                            <a class="btn btn-success"
                               href="${pageContext.request.contextPath}/railway-stations/${railwayStation.id}"
                               role="button">${railwayStation.id}</a>
                        </c:when>
                        <c:otherwise>
                            ${railwayStation.id}
                        </c:otherwise>
                    </c:choose>
                </th>
                <td>${railwayStation.name}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${user.isAdmin == true}">
        <a href="${pageContext.request.contextPath}/manage-railway-station.jsp" class="btn btn-primary link-primary">
            Create a new Railway Station
        </a>
    </c:if>
</div>
</body>
</html>
