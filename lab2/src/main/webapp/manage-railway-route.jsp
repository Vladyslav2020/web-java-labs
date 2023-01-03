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
    <title>Manage Railway Routes</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/railway-routes" class="btn mt-2 btn-primary link-primary">Back</a>
    <c:choose>
        <c:when test="${railwayRoute.id != null}">
            <h1>Update a Railway Route</h1>
            <form action="${pageContext.request.contextPath}/railway-routes/${railwayRoute.id}" method="post">
                <div class="mb-3">
                    <label for="id" class="form-label">ID</label>
                    <input type="number" name="start-station" class="form-control" disabled id="id" value="${railwayRoute.id}"/>
                </div>
                <div class="mb-3">
                    <label for="start-station-update" class="form-label">Start station</label>
                    <input type="text" name="start-station" class="form-control" id="start-station-update" value="${railwayRoute.startStation.name}"/>
                </div>
                <div class="mb-3">
                    <label for="finish-station-update" class="form-label">Finish station</label>
                    <input type="text" name="finish-station" class="form-control" id="finish-station-update" value="${railwayRoute.finishStation.name}"/>
                </div>
                <div class="mb-3">
                    <label for="train-number-update" class="form-label">Train number</label>
                    <input type="number" name="train-number" class="form-control" id="train-number-update" value="${railwayRoute.train.number}"/>
                </div>
                <div class="mb-3">
                    <label for="start-time-update" class="form-label">Start time</label>
                    <input type="datetime-local" name="start-time" class="form-control" id="start-time-update" value="${railwayRoute.startTime}"/>
                </div>
                <div class="mb-3">
                    <label for="end-time-update" class="form-label">End time</label>
                    <input type="datetime-local" name="end-time" class="form-control" id="end-time-update" value="${railwayRoute.endTime}"/>
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
            <form action="${pageContext.request.contextPath}/railway-routes/delete/${railwayRoute.id}" method="post">
                <button type="submit" class="btn btn-danger mt-2">Delete</button>
            </form>
        </c:when>
        <c:otherwise>
            <h1>Create a Railway Route</h1>
            <form action="${pageContext.request.contextPath}/railway-routes" method="post">
                <div class="mb-3">
                    <label for="start-station" class="form-label">Start station</label>
                    <input type="text" name="start-station" class="form-control" id="start-station"/>
                </div>
                <div class="mb-3">
                    <label for="finish-station" class="form-label">Finish station</label>
                    <input type="text" name="finish-station" class="form-control" id="finish-station"/>
                </div>
                <div class="mb-3">
                    <label for="train-number" class="form-label">Train number</label>
                    <input type="number" name="train-number" class="form-control" id="train-number"/>
                </div>
                <div class="mb-3">
                    <label for="start-time" class="form-label">Start time</label>
                    <input type="datetime-local" name="start-time" class="form-control" id="start-time"/>
                </div>
                <div class="mb-3">
                    <label for="end-time" class="form-label">End time</label>
                    <input type="datetime-local" name="end-time" class="form-control" id="end-time"/>
                </div>
                <button type="submit" class="btn btn-primary">Create</button>
            </form>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>