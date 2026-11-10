<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>StudyHub 로그인</title>
</head>
<body>
	<h1>로그인</h1>
	<form action="${pageContext.request.contextPath}/member/login.do"
		method="post">
		<div>
			<label>아이디</label> <input type="text" name="memberId">
		</div>
		<div>
			<label>비밀번호</label> <input type="password" name="password">
		</div>
		<button type="submit">로그인</button>
	</form>
	<p>${loginError}</p>
</body>
</html>
