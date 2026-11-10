<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>회원정보 수정</title>
</head>
<body>
	<h1>회원정보 수정</h1>
	<c:if test="${not empty editError}">
		<p style="color:red;">${editError}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/member/edit.do" method="post">
		<div>
			<label>아이디</label>
			<input type="text" value="${member.memberId}" readonly>
		</div>
		<div>
			<label>이름</label>
			<input type="text" name="name" value="${member.name}" required>
		</div>
		<div>
			<label>이메일</label>
			<input type="email" name="email" value="${member.email}" required>
		</div>
		<button type="submit">수정 완료</button>
		<a href="${pageContext.request.contextPath}/member/mypage.do">취소</a>
	</form>
</body>
</html>
