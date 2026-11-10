<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudyHub 회원 목록</title>
</head>
<body>
	<h1>회원 목록</h1>
	<table border="1">
		<thead>
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>이메일</th>
				<th>권한</th>
				<th>가입일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="member" items="${memberList}">
			<fmt:formatDate value="${member.joinDate}" pattern="yyyy년-MM월-dd일 HH시:mm분:ss초" var="joinDate"/>
				<tr>
					<td>${member.memberId}</td>
					<td>${member.name}</td>
					<td>${member.email}</td>
					<td>${member.role}</td>
					<td>${joinDate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>