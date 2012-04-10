<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@include file="/WEB-INF/jsp/include/tags.jspf"%>
<html>
<head>
<link href="${url:resource('/stylesheets/boards.css')}" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span10">
				<form class="form-horizontal">
					<fieldset>
						<legend>SLiPP Q&A 질문하기</legend>
						<div class="control-group">
							<label class="control-label" for="title">제목</label>
							<div class="controls">
								<input class="input-xlarge focused span7" id="focusedInput"
									type="text">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">내용</label>
							<div class="controls">
								<textarea class="input-xlarge span7" id="textarea" name="contents"  cols="75" rows="15"></textarea>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">태그</label>
							<div class="controls">
								<input class="input-xlarge focused span7" id="focusedInput"
									type="text">
							</div>
						</div>
						
						<div class="form-actions">
							<button type="submit" class="btn btn-primary">질문하기</button>
							<button class="btn"><a href="/qna">목록보기</a></button>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="span2">
				<div class="tags">
					<ul>
						<li>java</li>
						<li>java1</li>
						<li>java2</li>
						<li>java3</li>
						<li>java4</li>
						<li>java5</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>