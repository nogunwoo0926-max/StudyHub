<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudyHub</title>
</head>
<body>
	<h1>StudyHub</h1>
	<h2>스터디 모집 플랫폼</h2>
	<c:choose>
		<%--로그인한 상태면 --%>
		<c:when test="${not empty sessionScope.loginMember}">
			<p>${sessionScope.loginMember.name}님환영합니다.</p>
			<a href="${pageContext.request.contextPath}/member/logout.do"> 로그아웃 </a>
			<br>
			<a href="${pageContext.request.contextPath}/member/mypage.do"> 마이 페이지로 </a>
		</c:when>
		<c:otherwise>
			<a href="${pageContext.request.contextPath}/member/login.do"> 로그인 </a> <br>
			<a href="${pageContext.request.contextPath}/member/join.do"> 회원가입 </a>
		</c:otherwise>
	</c:choose>
	<br>
	<a href="${pageContext.request.contextPath}/study/list.do"> 스터디 목록 확인 </a>
	<br>
	<a href="${pageContext.request.contextPath}/member/list.do"> 멤버 목록 확인 </a>
</body>
</html>