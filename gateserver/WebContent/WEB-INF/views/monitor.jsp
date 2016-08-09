<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>monitor</title>
</head>
<body>
	Game server list
	<ul>
		<c:forEach items="${servers}" var="server">
		<li>name[${server.value.name}] address[${server.value.ip}:${server.value.port}] type[${server.value.type}] status[${server.value.status}]</li>
		</c:forEach>
	</ul>
</body>
</html>