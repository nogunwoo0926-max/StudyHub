<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:formatDate value="${study.startDate}" pattern="yyyy-MM-dd" var="startDate"/>
<fmt:formatDate value="${study.endDate}" pattern="yyyy-MM-dd" var="endDate"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>스터디 수정</title>
</head>
<body>
	<h1>스터디 수정</h1>
	<c:if test="${not empty errorMessage}">
		<p style="color:red;">${errorMessage}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/study/edit.do" method="post">
		<input type="hidden" name="studyId" value="${study.studyId}">
		<div>
			<label>제목</label> 
			<input type="text" name="title" value="${study.title}" required>
		</div>
		<div>
			<label>내용</label>
			<textarea name="content" rows="10" cols="50" required>${study.content}</textarea>
		</div>
		<div>
			<label>최대 인원</label> 
			<input type="number" name="maxMember" value="${study.maxMember}" min="2" max="10" required>
		</div>
		<div>
			<label>시작일</label> 
			<input type="date" name="startDate" value="${startDate}" required>
		</div>
		<div>
			<label>종료일</label> 
			<input type="date" name="endDate" value="${endDate}" required>
		</div>
		<button type="submit">수정 완료</button>
	</form>
	<script>
		const startDate = document.querySelector('input[name="startDate"]');
		const endDate = document.querySelector('input[name="endDate"]');

		endDate.min = startDate.value;

		startDate.addEventListener('change', function() {
			endDate.min = startDate.value;
		});
	</script>
</body>
</html>
