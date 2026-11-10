<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>스터디 신청자 관리</title>
</head>
<body>
	<h1>${study.title}신청자 관리</h1>
	<table border="1">
		<thead>
			<tr>
				<th>회원 ID</th>
				<th>신청일</th>
				<th>상태</th>
				<th>관리</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="applicant" items="${applicantList}">
				<tr>
					<td>${applicant.memberId}</td>
					<td><fmt:formatDate value="${applicant.applyDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></td>
					<td>${applicant.status}</td>
					<td><c:if test="${applicant.status eq 'WAIT'}">
							<form action="${pageContext.request.contextPath}/study/approve.do" method="post" style="display: inline;">
								<input type="hidden" name="studyMemberId" value="${applicant.studyMemberId}"> <input type="hidden" name="studyId" value="${study.studyId}">
								<button type="submit">승인</button>
							</form>
							<form action="${pageContext.request.contextPath}/study/reject.do" method="post" style="display: inline;">
								<input type="hidden" name="studyMemberId" value="${applicant.studyMemberId}"> 
								<input type="hidden" name="studyId" value="${study.studyId}">
								<button type="submit">거절</button>
							</form>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<a href="${pageContext.request.contextPath}/study/detail.do?studyId=${study.studyId}"> 스터디 상세로 </a>
</body>
</html>
