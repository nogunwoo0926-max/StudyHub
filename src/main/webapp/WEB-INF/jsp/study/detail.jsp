<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:formatDate value="${study.startDate}" pattern="yyyy년 MM월 dd일" var="startDate" />
<fmt:formatDate value="${study.endDate}" pattern="yyyy년 MM월 dd일" var="endDate" />
<fmt:formatDate value="${study.cancelDate}"  pattern="yyyy년 MM월 dd일 HH:mm" var="cancelDate" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>${study.title}</title>
</head>
<body>
	<h1>${study.title}</h1>
	<table border="1">
		<tr>
			<th>스터디장</th>
			<td>${study.leaderId}</td>
		</tr>
		<tr>
			<th>모집 인원</th>
			<td>${study.currentMember}/ ${study.maxMember}</td>
		</tr>
		<tr>
			<th>상태</th>
			<td>
				<c:choose>
					<c:when test="${study.status eq 'OPEN'}">모집중</c:when>
					<c:when test="${study.status eq 'CLOSED'}">모집마감</c:when>
					<c:when test="${study.status eq 'CANCELLED'}">스터디 취소</c:when>
					<c:otherwise>${study.status}</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th>시작일</th>
			<td>${startDate}</td>
		</tr>
		<tr>
			<th>종료일</th>
			<td>${endDate}</td>
		</tr>
		<tr>
			<th>내용</th>
			<td>${study.content}</td>
		</tr>
	</table>
	
	<br>
	
	<c:if test="${study.status ne 'CANCELLED' and not empty sessionScope.loginMember and sessionScope.loginMember.memberId eq study.leaderId}">
		<a href="${pageContext.request.contextPath}/study/edit.do?studyId=${study.studyId}">수정 </a>
		<br>
		<c:if test="${study.status eq 'OPEN'}">
			<form action="${pageContext.request.contextPath}/study/close.do" method="post" style="display:inline;">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit" onclick="return confirm('스터디 모집을 마감하시겠습니까?');">모집 마감</button>
			</form>
			<br>
		</c:if>
		<c:if test="${study.status eq 'CLOSED' and study.currentMember lt study.maxMember}">
			<form action="${pageContext.request.contextPath}/study/open.do" method="post" style="display:inline;">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit" onclick="return confirm('스터디 모집을 다시 시작하시겠습니까?');">모집 재개</button>
			</form>
			<br>
		</c:if>
		<form action="${pageContext.request.contextPath}/study/delete.do" method="post">
		    <input type="hidden" name="studyId" value="${study.studyId}">
		    <label for="cancelReason">취소 사유</label>
		    <br>
		    <textarea id="cancelReason" name="cancelReason" rows="5" cols="50" required placeholder="스터디 취소 사유를 입력해주세요."></textarea>
		    <br>
		    <button type="submit" onclick="return confirm('스터디를 취소하시겠습니까?');"> 스터디 취소 </button>
		</form>
	</c:if>
	
	<br>
	
	<c:if test="${study.status ne 'CANCELLED' and not empty sessionScope.loginMember and sessionScope.loginMember.memberId ne study.leaderId}">
		<!-- 신청 기록이 없는 경우 -->
		<c:if test="${empty myApplication}">
			<form action="${pageContext.request.contextPath}/study/apply.do" method="post">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit">스터디 신청</button>
			</form>
		</c:if>

		<!-- 이미 신청했고 승인 대기 중 -->
		<c:if test="${myApplication.status eq 'WAIT'}">
			<span>승인 대기 중</span>
			<form action="${pageContext.request.contextPath}/study/cancelApply.do" method="post" style="display:inline;">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit" onclick="return confirm('신청을 취소하시겠습니까?');"> 신청 취소</button>
			</form>
		</c:if>

		<!-- 거절 또는 신청 취소 후 -->
		<c:if test="${myApplication.status eq 'REJECTED' or myApplication.status eq 'CANCELLED'}">
			<form action="${pageContext.request.contextPath}/study/apply.do" method="post">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit">재신청</button>
			</form>
		</c:if>

		<!-- 승인된 경우 -->
		<c:if test="${myApplication.status eq 'APPROVED'}">
			<span>참여 중</span>
			<form action="${pageContext.request.contextPath}/study/leave.do" method="post" style="display:inline;">
				<input type="hidden" name="studyId" value="${study.studyId}">
				<button type="submit" onclick="return confirm('스터디에서 탈퇴하시겠습니까?');">스터디 탈퇴</button>
			</form>
		</c:if>

	</c:if>

	<c:if test="${study.status eq 'CANCELLED'}">
	    <tr>
	        <th>취소 사유: </th>
	        <td>${study.cancelReason}</td>
	    </tr>
	    <br>
	    <tr>
	        <th>취소 일시: </th>
	        <td>${cancelDate}</td>
	    </tr>
	</c:if>
	
	<br>
	
	<c:if test="${study.status ne 'CANCELLED' and not empty sessionScope.loginMember and sessionScope.loginMember.memberId eq study.leaderId}">
		<a href="${pageContext.request.contextPath}/study/applicants.do?studyId=${study.studyId}"> 신청자 관리 </a>
	</c:if>
	<br>
	<a href="${pageContext.request.contextPath}/study/list.do"> 목록으로 </a>
	<!-- 스터디 신청 시 알림 -->
	<c:if test="${not empty message}">
		<script>
			alert('${message}');
		</script>
	</c:if>
</body>
</html>
