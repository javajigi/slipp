<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span6">
				<form class="login" action="/signin/facebook" method="POST">
					<p>
						<input class="btn btn-primary btn-large pull-right" type="submit"
							value="페이스북 계정으로 로그인" />
					</p>
				</form>
			</div>
			<div class="span6">
				<form class="login" action="/signin/twitter" method="POST">
					<p>
						<input class="btn btn-info btn-large pull-left" type="submit"
							value="트위터 계정으로 로그인" />
					</p>
				</form>
			</div>
		</div>
	</div>
</body>
</html>