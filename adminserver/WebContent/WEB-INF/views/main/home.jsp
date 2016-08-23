<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Wego | Dashboard</title>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<!-- base需要放到head中 -->
<base href="<%=basePath%>">

<!-- include charimas css -->
<%@ include file="../common/css.html"%>

<!-- The fav icon -->
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
					<li><a href="#"><i class="fa fa-dashboard"></i> 首页</a></li>
					<li class="active">后台管理</li>
				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">测试用例</h3>
								<div class="box-tools pull-right">
									<button type="button" class="btn btn-box-tool"
										data-widget="collapse">
										<i class="fa fa-minus"></i>
									</button>
									<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									</div>
									<button type="button" class="btn btn-box-tool"
										data-widget="remove">
										<i class="fa fa-times"></i>
									</button>
								</div>
							</div>
							<!-- /.box-header -->
							<div class="box-body">
								<div class="row">
									<div class="col-md-8">
										<h4>JSON API & JSP</h4>
										<h5>GET的中文UrlEncode编码，取决于不同的浏览器，Chrome对URL中出现的中文使用utf-8进行UrlEncode</h5>
										<h5>Spring
											MVC的默认Character是ISO-8859-1[Latin-1],需要使用Spring实现的Servlet
											Filter. PS:详细见web.xml中的Filter</h5>
										<ul>
											<li>test/xml <a href="test/xml">click</a>
												根据accept：application/xml 或 application/json 来区分返回xml还是json</
											<li>
											<li>test/jsp/grape (RESTFUL) <a href="test/jsp/grape">click</a></
											<li>
											<li>test/json/fruit/grape/chinese/葡萄 (RESTFUL) <a
												href="test/json/fruit/grape/chinese/葡萄">click</a></
											<li>
											<li>test/json?name=kiki&chinese_name=陈 (JSON) <a
												href="test/json?name=kiki&chinese_name=陈">click</a></li>
											<li>test/json/form_post ( Form Post )
												<form class="form-horizontal" action="test/json/form_post"
													method="post">
													<input name="username" type="text" class="form-control"
														placeholder="Username" value="测试内容">
													<button name="submit" type="submit" class="btn btn-primary"
														value="submit">Submit</button>
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" />
												</form>
											</li>
											<li>test/json/post_json (Ajax Post Chinese ) <a href="#"
												id="postJson">click</a></li>
										</ul>
										<h4>User Authority</h4>
										<h5>必须是认证用户才能访问User接口</h5>
										<ul>
											<li>用户状态: <sec:authorize access="isAuthenticated()">
													<b style="color: red">已登陆</b>
													<form action="logout" method="POST">
														<input type="submit" value="logout" title="登出" /> <input
															type="hidden" name="${_csrf.parameterName}"
															value="${_csrf.token}" />
													</form>
												</sec:authorize> <sec:authorize access="!isAuthenticated()">
													<b style="color: red">未登陆</b>
												</sec:authorize>
											</li>
											<li>user/query/all (只限Admin权限访问) <a
												href="user/query/all">click</a></li>
											<li>user/whoami <a href="user/whoami">click</a></li>
										</ul>
										<h4>CSV</h4>
										<ul>
											<li>test/downloadCSV <a href="test/downloadCSV">click</a></li>
										</ul>
										<h4>302 Redirect</h4>
										<ul>
											<li>test/download302 <a href="test/download302">click</a></li>
										</ul>
										<h4>An tiny image which read from buffer</h4>
										<ul>
											<li>test/image/tiny <a href="test/image/tiny">click</a></li>
										</ul>
										<h4>Transcation</h4>
										<ul>
											<li>test/transaction/readonly <a
												href="test/transaction/readonly">click</a></li>
											<li>test/transaction/rollback <a
												href="test/transaction/rollback">click</a></li>
											<li>test/transaction/success <a
												href="test/transaction/success">click</a></li>
											<li>test/non-transaction <a href="test/non-transaction">click</a></li>
										</ul>
										<h4>Redis</h4>
										<ul>
											<li>test/cache <a href="test/cache">click</a></li>
										</ul>
										<h4>print log</h4>
										<ul>
											<li>test/log <a href="test/log">click</a></li>
										</ul>
										<h4>Exit Application</h4>
										<ul>
											<li>test/exit <a href="test/exit">click</a></li>
										</ul>
									</div>
								</div>
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
		$('#postJson').click(
				function() {
					var name = $('[name=username]').val()
					post('test/json/post_json', '{"id":25, "name": "' + name
							+ '"}', function(data) {
						if (data['status'])
							alert('数据处理成功:' + data['data'].name);
						else
							alert('数据处理失败:' + data['data']);
					}, function() {
						alert('请求失败，请检查网络环境');
					});
				});
		function post(url, data, success, error) {
			var csrfHeader = $("meta[name='_csrf_header']").attr("content");
			var csrfToken = $("meta[name='_csrf']").attr("content");
			$.ajax({
				type : 'POST',
				url : url,
				data : data,
				success : success,
				error : error,
				headers : {
					'X-CSRF-TOKEN' : csrfToken
				}
			});
		}
	</script>
</body>
</html>
