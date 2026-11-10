<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>스터디 등록</title>
</head>
<body>
	<h1>스터디 등록</h1>
	<c:if test="${not empty errorMessage}">
		<p style="color:red;">${errorMessage}</p>
	</c:if>
	<form action="${pageContext.request.contextPath}/study/write.do" method="post">
		<div>
			<label>제목</label> <input type="text" name="title" required>
		</div>
		<div>
			<label>내용</label>
			<textarea name="content" rows="10" cols="50" required></textarea>
		</div>
		<div>
			<label>최대 인원</label> <input type="number" name="maxMember" min="2" max="10" required>
		</div>
		<div>
			<label>시작일</label> <input type="date" name="startDate" required>
		</div>
		<div>
			<label>종료일</label> <input type="date" name="endDate" required>
		</div>
		<button type="submit">등록</button>
		<button type="reset">초기화</button>
	</form>
	<script>
		const startDate = document.querySelector('input[name="startDate"]');
		const endDate = document.querySelector('input[name="endDate"]');
		const maxMember = document.querySelector('input[name="maxMember"]');

		function getToday() {
			const today = new Date();
			const year = today.getFullYear();
			const month = String(today.getMonth() + 1).padStart(2, '0');
			const day = String(today.getDate()).padStart(2, '0');
			return year + '-' + month + '-' + day;
		}

		const today = getToday();
		startDate.min = today;
		if (!startDate.value) {
			startDate.value = today;
		}
		endDate.min = startDate.value;
		maxMember.max = '10';

		startDate.addEventListener('change', function() {
			endDate.min = startDate.value;
		});
	</script>
</body>
</html>
