<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>비밀번호 변경</title>
</head>
<body>
	<h1>비밀번호 변경</h1>
	<c:if test="${not empty passwordError}">
		<p style="color:red;">${passwordError}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/member/password.do" method="post">
		<div>
			<label>현재 비밀번호</label>
			<input type="password" name="currentPassword" required>
		</div>
		<div>
			<label>새 비밀번호</label>
			<input type="password" name="newPassword" minlength="8" required>
		</div>
		<div>
			<label>새 비밀번호 확인</label>
			<input type="password" name="confirmPassword" minlength="8" required>
		</div>
		<button type="submit">비밀번호 변경</button>
		<a href="${pageContext.request.contextPath}/member/mypage.do">취소</a>
	</form>
</body>
</html>
