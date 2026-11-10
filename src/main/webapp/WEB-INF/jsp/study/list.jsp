<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudyHub 스터디 목록</title>
</head>
<body>
	<h1>스터디 목록</h1>
	<table border="1">
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>스터디장</th>
				<th>인원</th>
				<th>상태</th>
				<th>시작일</th>
				<th>종료일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="study" items="${studyList}">
			<fmt:formatDate value="${study.startDate}" pattern="yyyy년-MM월-dd일" var="startDate"/>
			<fmt:formatDate value="${study.endDate}" pattern="yyyy년-MM월-dd일" var="endDate"/>
				<tr>
					<td>${study.studyId}</td>
					<td>
						<a href="${pageContext.request.contextPath}/study/detail.do?studyId=${study.studyId}"> ${study.title}</a>
					</td>
					<td>${study.leaderId}</td>
					<td>${study.currentMember} / ${study.maxMember}</td>
					<td>${study.status}</td>
					<td>${startDate}</td>
					<td>${endDate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${not empty sessionScope.loginMember}">
	    <a href="${pageContext.request.contextPath}/study/write.do"> 스터디 등록 </a> 
	</c:if>
	<br>
	<a href="${pageContext.request.contextPath}/main.do"> 메인 페이지로 </a>
</body>
</html>