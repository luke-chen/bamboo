<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>AdminLTE 2 | Dashboard</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />

	<!-- The base path of href -->
	<base href="<%=basePath%>">

	<!-- include charimas css -->
	<%@ include file="../common/css.html"%>
	
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		<!-- include topbar -->
		<jsp:include page="../common/topbar.jsp" />

		<!-- include left menu -->
		<jsp:include page="../common/leftmenu.jsp" />

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<h1>
					Backstage Framework Dashboard <small>AdminLTE 2 + SpringMVC
						+ SpringSecurity + MyBatis + LogBack</small>
				</h1>
				<ol class="breadcrumb">
					<li><a href="#"><i class="fa fa-dashboard"></i> 用户管理</a></li>
					<li class="active">后台管理</li>
				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">用户列表</h3>
              <div><small>需要添加新用户点击: <a href="#" data-toggle="modal" data-target="#newUser">新增用户</a></small></div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <table id="user" class="table table-bordered table-hover">
                <thead>
                <tr>
                  <th>用户名</th>
				<th>用户权限</th>
				<th>创建时间</th>
				<th>更新密码</th>
  				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<th>删除该用户</th>
				</sec:authorize>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
			<tr>
				<td>${user.username}</td>
				<td>${user.authority}</td>
				<td>${user.create_date}</td>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<td>
						<a href="#" data-toggle="modal" data-target="#changePwd" onclick="selectUser('${user.username}')">更新密码</a>
					</td>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
					<c:choose>
						<c:when test="${user.username == username}">
				  			<td>
				  				<a href="#" data-toggle="modal" data-target="#changePwd" onclick="selectUser('${user.username}')">更新密码</a>
				  			</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
				</sec:authorize>
				<c:choose>
					<c:when test="${user.username != username}">
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<td>
								<a href="#" data-toggle="modal" data-target="#deleteUser" onclick="selectUser('${user.username}')">删&nbsp;&nbsp;&nbsp;&nbsp;除</a>
							</td>
				  		</sec:authorize>
						<sec:authorize access="hasRole('ROLE_ROLE')">
							<td></td>
						</sec:authorize>
					</c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>
				</c:choose>
			</tr>
			</c:forEach>
		</tbody>
		</table>
	</div>
	</div>


<!-- 新建用户 -->
<div class="modal fade" id="newUser" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建用户</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>用户名</th>
			  		<th>密码</th>
			  		<th>权限</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
			  		<td><input id="username" type="text" class="form-control" placeholder="输入用户名"/></td>
			  		<td><input id="password" type="password" class="form-control" placeholder="输入密码"/></td>
			  		<td>
						<select id="authority" class="form-control">
			  				<option value="ROLE_USER">用户</option>
			  				<option value="ROLE_ADMIN">管理员</option>
			  			</select>
			  		</td>
				</tr>
			</tbody>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewUser" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<!-- 修改密码 -->
<div class="modal fade" id="changePwd" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">修改密码</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>新密码</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
					<td><input id="c_password" type="password" class="form-control" placeholder="输入新密码"/></td>
				</tr>
			</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<button id="updatePwd" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
	</div>
	</div>
</div>

<!-- 删除用户 -->
<div class="modal fade" id="deleteUser" tabindex="-1" role="dialog">
	<div class="modal-dialog" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">删除用户</h4>
		</div>
		<div class="modal-body">
      		<button id="delUserConfirm" class="btn btn-primary" type="button">确定</button>
      		<button data-dismiss="modal" class="btn btn-primary" type="button">取消</button>
		</div>
	</div>
	</div>
</div>

		</section>
	</div> <!-- End of Main content  -->
	<!-- include footer -->
	<%@ include file="../common/footer.html"%>
</div> <!-- End of  Content Wrappe  -->

<!-- include js -->
<%@ include file="../common/js.html"%>
<script type="text/javascript">
$(document).ready(function(){
	$('#user').DataTable({
	    "paging": true,
	    "lengthChange": true,
	    "searching": false,
	    "ordering": true,
	    "info": true,
	    "autoWidth": false
  	});
});

var selectedUsername = '';

function selectUser(username) {
	selectedUsername = username;
}

$('#addNewUser').click(function(event) {
	post('user/add',
		'username='+$("#username").val()+'&password='+$("#password").val()+'&authority='+$("#authority").val(),
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>user';
			}
			else {
				alert('添加用户失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delUserConfirm').click(function(event) {
	post('user/delete',
		'username='+selectedUsername,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>user';
			}
			else {
				alert('删除用户失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#updatePwd').click(function(event) {
	var url = 'user/changePassword';
	post('user/changePassword',
			'username='+selectedUsername+'&password='+$("#c_password").val(),
			function (data) {
				if(data['status']) {
					alert('密码更新成功');
				}
				else {
					alert('密码更新失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
});

function post(url, data, success, error) {
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: 'POST', url: url, data: data, success: success, error: error,
		headers: {'X-CSRF-TOKEN': csrfToken}
	});
}
</script>
</body>
</html>
