<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>회원 탈퇴</title>
</head>
<body>
	<h1>회원 탈퇴</h1>
	<p>회원 탈퇴 후에도 기존 스터디와 신청 이력은 보존됩니다.</p>
	<c:if test="${not empty withdrawError}">
		<p style="color:red;">${withdrawError}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/member/withdraw.do" method="post">
		<div>
			<label>현재 비밀번호</label>
			<input type="password" name="password" required>
		</div>
		<button type="submit" onclick="return confirm('정말 회원 탈퇴하시겠습니까?');">회원 탈퇴</button>
		<a href="${pageContext.request.contextPath}/member/mypage.do">취소</a>
	</form>
</body>
</html>
