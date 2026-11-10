<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>StudyHub 회원가입</title>
</head>
<body>
	<h1>회원가입</h1>
	<c:if test="${not empty joinError}">
		<p style="color:red;">${joinError}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/member/join.do"
		method="post">
		<div>
			<label>아이디</label> <input type="text" name="memberId">
		</div>
		<div>
			<label>비밀번호</label> <input type="password" name="password">
		</div>
		<div>
			<label>이름</label> <input type="text" name="name">
		</div>
		<div>
			<label>이메일</label> <input type="email" name="email">
		</div>
		<button type="submit">가입</button>
		<button type="reset">초기화</button>
	</form>
</body>
</html>
