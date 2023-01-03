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
    <title>Railway Routes</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/user.jsp" class="btn mt-2 btn-primary link-primary">Back</a>
    <form class="mt-2 d-flex" action="${pageContext.request.contextPath}/railway-routes/search" method="post">
        <input class="form-control me-2" type="search" name="start-station" placeholder="Start Station"
               aria-label="Search">
        <input class="form-control me-2" type="search" name="finish-station" placeholder="Finish Station"
               aria-label="Search">
        <button class="btn btn-outline-success" type="submit">Search</button>
    </form>
    <h1>Railway Routes</h1>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Start Station</th>
            <th scope="col">Finish Station</th>
            <th scope="col">Start Time</th>
            <th scope="col">End Time</th>
            <th scope="col">Duration</th>
            <th scope="col">Train</th>
            <th scope="col">Available tickets</th>
            <c:if test="${user.isAdmin == false}">
                <th scope="col">
                    Buy a ticket
                </th>
            </c:if>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${railwayRoutes}" var="railwayRoute">
            <tr>
                <th scope="row">
                    <c:choose>
                        <c:when test="${user.isAdmin == true}">
                            <a class="btn btn-success"
                               href="${pageContext.request.contextPath}/railway-routes/${railwayRoute.id}"
                               role="button">${railwayRoute.id}</a>
                        </c:when>
                        <c:otherwise>
                            ${railwayRoute.id}
                        </c:otherwise>
                    </c:choose>
                </th>
                <td>${railwayRoute.startStation.name}</td>
                <td>${railwayRoute.finishStation.name}</td>
                <td>${railwayRoute.startTime}</td>
                <td>${railwayRoute.endTime}</td>
                <td>${railwayRoute.duration}</td>
                <td>${railwayRoute.train.number}</td>
                <td>${railwayRoute.availableTickets}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/tickets" method="get">
                        <input type="hidden" name="railway-route-id" value="${railwayRoute.id}"/>
                        <button type="submit" class="btn btn-primary mt-2">Buy</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${user.isAdmin == true}">
        <a href="${pageContext.request.contextPath}/manage-railway-route.jsp" class="btn btn-primary link-primary">Create
            a new Railway Route</a>
    </c:if>
</div>
</body>
</html>
