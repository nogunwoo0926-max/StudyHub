<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:formatDate value="${study.startDate}" pattern="yyyy-MM-dd" var="startDate"/>
<fmt:formatDate value="${study.endDate}" pattern="yyyy-MM-dd" var="endDate"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스터디 수정</title>
</head>
<body>
	<h1>스터디 수정</h1>
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
			<input type="number" name="maxMember" value="${study.maxMember}" min="2" required>
		</div>
		<div>
			<label>시작일</label> 
			<input type="date" name="startDate" value="${startDate}">
		</div>
		<div>
			<label>종료일</label> 
			<input type="date" name="endDate" value="${endDate}">
		</div>
		<button type="submit">수정 완료</button>
	</form>
</body>
</html>