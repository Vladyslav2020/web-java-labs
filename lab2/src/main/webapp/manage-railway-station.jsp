<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Manage Railway Stations</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/railway-stations" class="btn mt-2 btn-primary link-primary">Back</a>
    <c:choose>
        <c:when test="${railwayStation.id != null}">
            <h1>Update a Railway Station</h1>
            <form action="${pageContext.request.contextPath}/railway-stations/${railwayStation.id}" method="post">
                <div class="mb-3">
                    <label for="id" class="form-label">ID</label>
                    <input type="number" name="start-station" class="form-control" disabled id="id" value="${railwayStation.id}"/>
                </div>
                <div class="mb-3">
                    <label for="name-update" class="form-label">Start station</label>
                    <input type="text" name="name" class="form-control" value="${railwayStation.name}" id="name-update"/>
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
            <form action="${pageContext.request.contextPath}/railway-stations/delete/${railwayStation.id}" method="post">
                <button type="submit" class="btn btn-danger mt-2">Delete</button>
            </form>
        </c:when>
        <c:otherwise>
            <h1>Create a Railway Station</h1>
            <form action="${pageContext.request.contextPath}/railway-stations" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" id="name"/>
                </div>
                <button type="submit" class="btn btn-primary">Create</button>
            </form>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>