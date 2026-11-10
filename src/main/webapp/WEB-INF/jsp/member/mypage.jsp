<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/studyhub.css">
<title>마이페이지</title>
</head>
<body>
	<h1>마이페이지</h1>
	<p>${sessionScope.loginMember.name}님의 페이지입니다.</p>
	<a href="${pageContext.request.contextPath}/member/edit.do">회원정보 수정</a><br>
	<a href="${pageContext.request.contextPath}/member/password.do">비밀번호 변경</a><br>
	<a href="${pageContext.request.contextPath}/member/withdraw.do">회원 탈퇴</a>

	<h2>내 신청 스터디</h2>

	<c:choose>
		<c:when test="${empty applicationList}">
			<p>신청한 스터디가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table border="1">
				<thead>
					<tr>
						<th>스터디 제목</th>
						<th>신청 상태</th>
						<th>신청일</th>
						<th>스터디 상태</th>
						<th>취소 정보</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="application" items="${applicationList}">
						<tr>
							<td><a href="${pageContext.request.contextPath}/study/detail.do?studyId=${application.studyId}">${application.title}</a></td>
							<td>
								<c:choose>
									<c:when test="${application.status eq 'WAIT'}">승인 대기 중</c:when>
									<c:when test="${application.status eq 'APPROVED'}">참여 중</c:when>
									<c:when test="${application.status eq 'REJECTED'}">거절됨</c:when>
									<c:when test="${application.status eq 'CANCELLED'}">신청 취소</c:when>
									<c:otherwise>${application.status}</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatDate value="${application.applyDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></td>
							<td>
								<c:choose>
									<c:when test="${application.studyStatus eq 'OPEN'}">모집 중</c:when>
									<c:when test="${application.studyStatus eq 'CLOSED'}">모집 마감</c:when>
									<c:when test="${application.studyStatus eq 'CANCELLED'}">스터디 취소</c:when>
									<c:otherwise>${application.studyStatus}</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:if test="${application.studyStatus eq 'CANCELLED'}">
									<p>사유: ${application.cancelReason}</p>
									<p>일시: <fmt:formatDate value="${application.cancelDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></p>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>내가 모집 중인 스터디</h2>

	<c:choose>
		<c:when test="${empty myStudyList}">
			<p>모집 중인 스터디가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table border="1">
				<thead>
					<tr>
						<th>스터디 제목</th>
						<th>모집 인원</th>
						<th>시작일</th>
						<th>종료일</th>
						<th>상태</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="study" items="${myStudyList}">
						<tr>
							<td><a href="${pageContext.request.contextPath}/study/detail.do?studyId=${study.studyId}">${study.title}</a></td>
							<td>${study.currentMember} / ${study.maxMember}</td>
							<td><fmt:formatDate value="${study.startDate}" pattern="yyyy년 MM월 dd일" /></td>
							<td><fmt:formatDate value="${study.endDate}" pattern="yyyy년 MM월 dd일" /></td>
							<td>모집 중</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>내가 만든 스터디 전체 이력</h2>

	<c:choose>
		<c:when test="${empty myStudyHistoryList}">
			<p>등록한 스터디가 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table border="1">
				<thead>
					<tr>
						<th>스터디 제목</th>
						<th>모집 인원</th>
						<th>상태</th>
						<th>등록일</th>
						<th>취소 정보</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="study" items="${myStudyHistoryList}">
						<tr>
							<td><a href="${pageContext.request.contextPath}/study/detail.do?studyId=${study.studyId}">${study.title}</a></td>
							<td>${study.currentMember} / ${study.maxMember}</td>
							<td>
								<c:choose>
									<c:when test="${study.status eq 'OPEN'}">모집 중</c:when>
									<c:when test="${study.status eq 'CLOSED'}">모집 마감</c:when>
									<c:when test="${study.status eq 'CANCELLED'}">스터디 취소</c:when>
									<c:otherwise>${study.status}</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatDate value="${study.regDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></td>
							<td>
								<c:if test="${study.status eq 'CANCELLED'}">
									<p>사유: ${study.cancelReason}</p>
									<p>일시: <fmt:formatDate value="${study.cancelDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></p>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

	<h2>알림</h2>

	<c:choose>
		<c:when test="${empty notificationList}">
			<p>새로운 알림이 없습니다.</p>
		</c:when>
		<c:otherwise>
			<table border="1">
				<thead>
					<tr>
						<th>알림 내용</th>
						<th>발생 일시</th>
						<th>상태</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="notification" items="${notificationList}">
						<tr>
							<td>
								<c:choose>
									<c:when test="${not empty notification.studyId}">
										<a href="${pageContext.request.contextPath}/study/detail.do?studyId=${notification.studyId}">${notification.message}</a>
									</c:when>
									<c:otherwise>${notification.message}</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatDate value="${notification.regDate}" pattern="yyyy년 MM월 dd일 HH:mm" /></td>
							<td>${notification.readYn eq 'Y' ? '읽음' : '읽지 않음'}</td>
							<td>
								<c:if test="${notification.readYn eq 'N'}">
									<form action="${pageContext.request.contextPath}/notification/read.do" method="post">
										<input type="hidden" name="notificationId" value="${notification.notificationId}">
										<button type="submit">읽음</button>
									</form>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

	<br>
	<a href="${pageContext.request.contextPath}/main.do">메인 페이지로</a>
</body>
</html>
