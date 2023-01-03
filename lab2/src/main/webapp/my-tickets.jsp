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
    <title>My tickets</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/user.jsp" class="btn mt-2 btn-primary link-primary">Back</a>
    <h1>My tickets</h1>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Route ID</th>
            <th scope="col">Seat Number</th>
            <th scope="col">Start Time</th>
            <th scope="col">End Time</th>
            <th scope="col">Train</th>
            <th scope="col">Delete ticket</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tickets}" var="ticket">
            <tr>
                <th scope="row">${ticket.id}</th>
                <td>${ticket.route.id}</td>
                <td>${ticket.seatNumber}</td>
                <td>${ticket.route.startTime}</td>
                <td>${ticket.route.endTime}</td>
                <td>${ticket.route.train.number}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/tickets/delete/${ticket.id}" method="post">
                        <button type="submit" class="btn btn-danger mt-2">Delete</button>
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
