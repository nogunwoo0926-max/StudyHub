<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스터디 등록</title>
</head>
<body>
	<h1>스터디 등록</h1>
	<form action="${pageContext.request.contextPath}/study/write.do" method="post">
		<div>
			<label>제목</label> <input type="text" name="title" required>
		</div>
		<div>
			<label>내용</label>
			<textarea name="content" rows="10" cols="50" required></textarea>
		</div>
		<div>
			<label>최대 인원</label> <input type="number" name="maxMember" min="2" required>
		</div>
		<div>
			<label>시작일</label> <input type="date" name="startDate">
		</div>
		<div>
			<label>종료일</label> <input type="date" name="endDate">
		</div>
		<button type="submit">등록</button>
		<button type="reset">초기화</button>
	</form>
</body>
</html>