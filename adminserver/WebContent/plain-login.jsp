<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>

<body>
	<c:url value="/login" var="loginUrl"/>
	<form action="${loginUrl}" method="post">
	    <c:if test="${param.error != null}">
	        <p>Invalid username and password.</p>
	    </c:if>
	    <c:if test="${param.logout != null}">
	        <p>You have been logged out.</p>
	    </c:if>
	    <div>
	        <label for="username">Username</label>
	        <input type="text" id="username" name="username" />
	    </div>
	    <div>
	        <label for="password">Password</label>
	        <input type="password" id="password" name="password" />
	    </div>
	    <div>
	    	Remember Me: <input type="checkbox" name="remember-me" />
	    </div>
	    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	    <button type="submit" class="btn">Log in</button>
	</form>
</body>
</html>