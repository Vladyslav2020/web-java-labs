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
    <title>Buy a ticket</title>
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/railway-routes" class="btn mt-2 btn-primary link-primary">Back</a>
    <h1>Buy a ticket</h1>
    <form action="${pageContext.request.contextPath}/tickets" method="post">
        <div class="mb-3">
            <label for="railway-route-id" class="form-label">Railway Route ID</label>
            <input type="number" name="railway-route-id" class="form-control" readonly="readonly" id="railway-route-id" value="${railwayRouteId}"/>
        </div>
        <div class="mb-3">
            <label for="seat-number" class="form-label">Choose a seat number: ${availableSeats}</label>
            <input type="number" name="seat-number" class="form-control" id="seat-number"/>
        </div>
        <button type="submit" class="btn btn-primary">Buy</button>
    </form>
</div>

</body>
</html>