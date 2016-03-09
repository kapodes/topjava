<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<h2>Meal list</h2>
<ul>
    <c:forEach items="${mealList}" var="meal">
        <li style="color:
        <c:if test="${meal.exceed}">
                red
        </c:if>
        <c:if test="${!meal.exceed}">
                green
        </c:if>
                ;">
            <h2>
                <c:out value="${meal}"/>
            </h2>
        </li>

    </c:forEach>
</ul>
</body>
</html>
