package com.studyhub.study.service.impl;

import java.util.List;
import java.util.Calendar;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.studyhub.notification.service.NotificationService;
import com.studyhub.notification.service.NotificationVO;
import com.studyhub.study.service.StudyMemberVO;
import com.studyhub.study.service.StudySearchVO;
import com.studyhub.study.service.StudyService;
import com.studyhub.study.service.StudyVO;

@Service
public class StudyServiceImpl implements StudyService {
    private final StudyDAO studyDAO;
    private final NotificationService notificationService;

    public StudyServiceImpl(StudyDAO studyDAO, NotificationService notificationService) {
        this.studyDAO = studyDAO;
        this.notificationService = notificationService;
    }
    
    // 스터디 목록 조회
    @Override
    public List<StudyVO> selectStudyList() {
        return studyDAO.selectStudyList();
    }

    // 스터디 검색 및 상태 필터 조회
    @Override
    public List<StudyVO> selectStudyList(StudySearchVO searchVO) {
        return studyDAO.selectStudyList(searchVO);
    }

    // 스터디 검색 결과 개수 조회
    @Override
    public int selectStudyCount(StudySearchVO searchVO) {
        return studyDAO.selectStudyCount(searchVO);
    }
    
    // 스터디 등록
    @Override
    public int insertStudy(StudyVO studyVO) {
        if (studyVO.getStartDate() == null) {
            studyVO.setStartDate(getToday());
        }

        validateStudy(studyVO);

        if (studyVO.getStartDate().before(getToday())) {
            throw new IllegalArgumentException("스터디 시작일은 오늘 이전으로 설정할 수 없습니다.");
        }

        return studyDAO.insertStudy(studyVO);
    }
    
    // 스터디 상세 조회
    @Override
    public StudyVO selectStudyDetail(Long studyId) {
        return studyDAO.selectStudyDetail(studyId);
    }
    
    // 스터디 수정
    @Override
    public int updateStudy(StudyVO studyVO) {
        validateStudy(studyVO);

        StudyVO originalStudy = studyDAO.selectStudyDetail(studyVO.getStudyId());

        if (originalStudy == null) {
            throw new IllegalArgumentException("존재하지 않는 스터디입니다.");
        }

        if (studyVO.getMaxMember() < originalStudy.getCurrentMember()) {
            throw new IllegalArgumentException("현재 참여 인원보다 모집 정원을 작게 설정할 수 없습니다.");
        }

        return studyDAO.updateStudy(studyVO);
    }

    // 스터디장이 모집 마감
    @Override
    public int closeStudy(Long studyId, String loginMemberId) {
        StudyVO study = studyDAO.selectStudyDetail(studyId);

        if (study == null || !study.getLeaderId().equals(loginMemberId)) {
            return 0;
        }

        return studyDAO.closeStudy(studyId);
    }

    // 스터디장이 모집 재개
    @Override
    public int openStudy(Long studyId, String loginMemberId) {
        StudyVO study = studyDAO.selectStudyDetail(studyId);

        if (study == null || !study.getLeaderId().equals(loginMemberId)) {
            return 0;
        }

        if (!"CLOSED".equals(study.getStatus()) || study.getCurrentMember() >= study.getMaxMember()) {
            return 0;
        }

        return studyDAO.openStudy(studyId);
    }

    // 스터디 등록 및 수정 공통 검증
    private void validateStudy(StudyVO studyVO) {
        if (studyVO.getTitle() == null || studyVO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 제목을 입력해주세요.");
        }

        if (studyVO.getContent() == null || studyVO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("스터디 내용을 입력해주세요.");
        }

        if (studyVO.getMaxMember() < 2 || studyVO.getMaxMember() > 10) {
            throw new IllegalArgumentException("모집 정원은 2명 이상 10명 이하로 설정해주세요.");
        }

        if (studyVO.getStartDate() == null || studyVO.getEndDate() == null) {
            throw new IllegalArgumentException("시작일과 종료일을 모두 입력해주세요.");
        }

        if (studyVO.getStartDate().after(studyVO.getEndDate())) {
            throw new IllegalArgumentException("종료일은 시작일보다 빠를 수 없습니다.");
        }
    }

    // 오늘 날짜를 시간 없이 반환
    private Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 스터디 삭제
    @Override
    @Transactional
    public int deleteStudy(StudyVO studyVO) {
        Long studyId = studyVO.getStudyId();
	    StudyVO study = studyDAO.selectStudyDetail(studyId);
	    List<StudyMemberVO> studyMemberList = studyDAO.selectStudyMemberList(studyId);

	    for (StudyMemberVO studyMember : studyMemberList) {
	        if ("WAIT".equals(studyMember.getStatus()) || "APPROVED".equals(studyMember.getStatus())) {
	            NotificationVO notificationVO = new NotificationVO();
	            notificationVO.setReceiverId(studyMember.getMemberId());
	            notificationVO.setStudyId(studyId);
	            notificationVO.setType("STUDY_CANCELLED");
	            notificationVO.setMessage("신청한 스터디가 취소되었습니다: " + study.getTitle());
	            notificationService.insertNotification(notificationVO);
	        }
	    }

        // 신청 기록을 삭제하지 않고 CANCELLED 상태로 보존
        studyDAO.cancelStudyMembers(studyId);

        // 스터디 상태, 취소 사유, 취소 일시 저장
        return studyDAO.deleteStudy(studyVO);
    }
	
	// 스터디 신청(스터디 모집 공고에 멤버를 추가하는 것)
	@Override
	@Transactional
	public String applyStudy(StudyMemberVO studyMemberVO) {
		
		// 스터디 정보 조회
	    StudyVO study = studyDAO.selectStudyDetail(studyMemberVO.getStudyId());

	    // 존재하지 않는 스터디
	    if (study == null) {
	        return "NOT_FOUND";
	    }

	    // 자기 스터디 신청 방지
	    if (study.getLeaderId().equals(studyMemberVO.getMemberId())) {
	        return "OWN_STUDY";
	    }
	    
	    // 모집 마감
	    if (!"OPEN".equals(study.getStatus())) {
	        return "CLOSED";
	    }

	    // 정원 초과
	    if (study.getCurrentMember() >= study.getMaxMember()) {
	        return "FULL";
	    }

	    // 기존 신청 기록 조회
	    StudyMemberVO existing = studyDAO.selectMemberApplication(studyMemberVO);

	    // 처음 신청하는 회원
	    if (existing == null) {
	        int result = studyDAO.insertStudyMember(studyMemberVO);

	        if (result > 0) {
            notifyStudyLeaderOfApplication(study);
	            return "APPLIED";
	        }

	        return "ERROR";
	    }

	    // 이미 신청 대기 중
	    if ("WAIT".equals(existing.getStatus())) {
	        return "WAIT";
	    }

	    // 이미 승인된 회원
	    if ("APPROVED".equals(existing.getStatus())) {
	        return "APPROVED";
	    }

	    // 거절 또는 취소된 회원 → 재신청
	    if ("REJECTED".equals(existing.getStatus()) || "CANCELLED".equals(existing.getStatus())) {
	        int result = studyDAO.reapplyStudy(studyMemberVO);

	        if (result > 0) {
            notifyStudyLeaderOfApplication(study);
	            return "REAPPLIED";
	        }

	        return "ERROR";
	    }
	    return "ERROR";
	}

	// 새로운 신청 또는 재신청 발생 시 스터디장에게 알림
	private void notifyStudyLeaderOfApplication(StudyVO study) {
	    NotificationVO notificationVO = new NotificationVO();
	    notificationVO.setReceiverId(study.getLeaderId());
	    notificationVO.setStudyId(study.getStudyId());
	    notificationVO.setType("NEW_APPLICATION");
	    notificationVO.setMessage("새로운 스터디 신청이 있습니다: " + study.getTitle());
	    notificationService.insertNotification(notificationVO);
	}

	// 스터디 신청자 목록 조회
	@Override
	public List<StudyMemberVO> selectStudyMemberList(Long studyId) {
	    return studyDAO.selectStudyMemberList(studyId);
	}
	
	// 내가 신청한 스터디 목록 조회
	@Override
	public List<StudyMemberVO> selectMyApplicationList(String memberId) {
	    return studyDAO.selectMyApplicationList(memberId);
	}

	// 내가 모집 중인 스터디 목록 조회
	@Override
	public List<StudyVO> selectMyStudyList(String leaderId) {
	    return studyDAO.selectMyStudyList(leaderId);
	}

	// 내가 만든 스터디 전체 이력 조회
	@Override
	public List<StudyVO> selectMyStudyHistoryList(String leaderId) {
	    return studyDAO.selectMyStudyHistoryList(leaderId);
	}

	// 회원 탈퇴에 따른 스터디·신청 데이터 정리
	@Override
	@Transactional
	public int cleanupMemberOnWithdraw(String memberId) {
	    List<StudyVO> leaderStudyList = studyDAO.selectMyStudyHistoryList(memberId);

	    for (StudyVO study : leaderStudyList) {
	        if ("CANCELLED".equals(study.getStatus())) {
	            continue;
	        }

	        List<StudyMemberVO> studyMemberList = studyDAO.selectStudyMemberList(study.getStudyId());

	        for (StudyMemberVO studyMember : studyMemberList) {
	            if ("WAIT".equals(studyMember.getStatus()) || "APPROVED".equals(studyMember.getStatus())) {
	                NotificationVO notificationVO = new NotificationVO();
                notificationVO.setReceiverId(studyMember.getMemberId());
                notificationVO.setStudyId(study.getStudyId());
                notificationVO.setType("STUDY_CANCELLED");
                notificationVO.setMessage("스터디장이 회원 탈퇴하여 스터디가 취소되었습니다: " + study.getTitle());
                notificationService.insertNotification(notificationVO);
            }
        }

        studyDAO.cancelStudyMembers(study.getStudyId());
        study.setCancelReason("스터디장 회원 탈퇴");
        studyDAO.deleteStudy(study);
    }

    List<StudyMemberVO> applicationList = studyDAO.selectMyApplicationList(memberId);

    for (StudyMemberVO application : applicationList) {
        if (!"WAIT".equals(application.getStatus()) && !"APPROVED".equals(application.getStatus())) {
            continue;
        }

        StudyVO study = studyDAO.selectStudyDetail(application.getStudyId());

        if (study == null) {
            continue;
        }

        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setReceiverId(study.getLeaderId());
        notificationVO.setStudyId(study.getStudyId());

        if ("WAIT".equals(application.getStatus())) {
            notificationVO.setType("APPLICATION_CANCELLED");
            notificationVO.setMessage("신청 회원의 탈퇴로 스터디 신청이 취소되었습니다: " + study.getTitle());
            notificationService.insertNotification(notificationVO);
            studyDAO.cancelStudyApplication(createStudyMemberVO(study.getStudyId(), memberId));
        } else {
            notificationVO.setType("MEMBER_LEFT");
            notificationVO.setMessage("참여 회원의 탈퇴로 스터디에서 탈퇴 처리되었습니다: " + study.getTitle());
            notificationService.insertNotification(notificationVO);

            StudyMemberVO studyMemberVO = createStudyMemberVO(study.getStudyId(), memberId);
            int leaveResult = studyDAO.leaveStudyApplication(studyMemberVO);

            if (leaveResult > 0) {
                int decreaseResult = studyDAO.decreaseCurrentMember(study.getStudyId());

                if (decreaseResult == 0) {
                    throw new IllegalStateException("탈퇴 회원의 스터디 인원을 감소시키지 못했습니다.");
                }
            }
        }
    }

    return 1;
}

	private StudyMemberVO createStudyMemberVO(Long studyId, String memberId) {
	    StudyMemberVO studyMemberVO = new StudyMemberVO();
	    studyMemberVO.setStudyId(studyId);
	    studyMemberVO.setMemberId(memberId);
	    return studyMemberVO;
	}

	// 스터디 신청 승인
	@Override
	@Transactional
	public int approveStudyMember(Long studyMemberId, String loginMemberId) {

	    StudyMemberVO studyMember = studyDAO.selectStudyMember(studyMemberId);
	    StudyVO study = studyDAO.selectStudyDetail(studyMember.getStudyId());

	    // 현재 로그인한 사용자가 스터디장인지 확인
	    if (!study.getLeaderId().equals(loginMemberId)) {
	        return 0;
	    }
	    // 이미 정원이 가득 찬 경우
	    if (study.getCurrentMember() >= study.getMaxMember()) {
	        return 0;
	    }
	    int result = studyDAO.approveStudyMember(studyMemberId);

	    // WAIT → APPROVED 변경에 성공한 경우만 인원 증가
	    if (result > 0) {
	        studyDAO.increaseCurrentMember(studyMember.getStudyId());
	        studyDAO.closeStudyIfFull(studyMember.getStudyId());

	        NotificationVO notificationVO = new NotificationVO();
	        notificationVO.setReceiverId(studyMember.getMemberId());
	        notificationVO.setStudyId(studyMember.getStudyId());
	        notificationVO.setType("APPROVED");
	        notificationVO.setMessage("스터디 신청이 승인되었습니다: " + study.getTitle());
	        notificationService.insertNotification(notificationVO);
	    }
	    return result;
	}

	// 스터디 신청 반려
	@Override
	@Transactional
	public int rejectStudyMember(Long studyMemberId, String loginMemberId) {

	    StudyMemberVO studyMember = studyDAO.selectStudyMember(studyMemberId);
	    StudyVO study = studyDAO.selectStudyDetail(studyMember.getStudyId());

	    if (!study.getLeaderId().equals(loginMemberId)) {
	        return 0;
	    }
	    int result = studyDAO.rejectStudyMember(studyMemberId);

	    if (result > 0) {
	        NotificationVO notificationVO = new NotificationVO();
	        notificationVO.setReceiverId(studyMember.getMemberId());
	        notificationVO.setStudyId(studyMember.getStudyId());
	        notificationVO.setType("REJECTED");
	        notificationVO.setMessage("스터디 신청이 거절되었습니다: " + study.getTitle());
	        notificationService.insertNotification(notificationVO);
	    }

	    return result;
	}
	
	// 스터디 신청 취소
	@Override
	@Transactional
	public int cancelStudyApplication(StudyMemberVO studyMemberVO) {
		
	    StudyMemberVO existing = studyDAO.selectMemberApplication(studyMemberVO);

	    if (existing == null) {
	        return 0;
	    }
	    
	    // 본인의 WAIT 신청만 취소 가능
	    if (!"WAIT".equals(existing.getStatus())) {
	        return 0;
	    }
	    int result = studyDAO.cancelStudyApplication(studyMemberVO);

	    if (result > 0) {
	        StudyVO study = studyDAO.selectStudyDetail(studyMemberVO.getStudyId());
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setReceiverId(study.getLeaderId());
        notificationVO.setStudyId(study.getStudyId());
        notificationVO.setType("APPLICATION_CANCELLED");
        notificationVO.setMessage("스터디 신청이 취소되었습니다: " + study.getTitle());
        notificationService.insertNotification(notificationVO);
    }

	    return result;
	}

	// 승인된 회원의 스터디 탈퇴
	@Override
	@Transactional
	public int leaveStudy(Long studyId, String memberId) {
	    StudyMemberVO studyMemberVO = new StudyMemberVO();
	    studyMemberVO.setStudyId(studyId);
	    studyMemberVO.setMemberId(memberId);

	    StudyMemberVO existing = studyDAO.selectMemberApplication(studyMemberVO);

	    // 신청 이력이 없거나 승인된 회원이 아니면 탈퇴 불가
	    if (existing == null || !"APPROVED".equals(existing.getStatus())) {
	        return 0;
	    }

	    StudyVO study = studyDAO.selectStudyDetail(studyId);

	    if (study == null) {
	        return 0;
	    }

	    int leaveResult = studyDAO.leaveStudyApplication(studyMemberVO);

	    if (leaveResult > 0) {
	        int decreaseResult = studyDAO.decreaseCurrentMember(studyId);

	        if (decreaseResult == 0) {
	            throw new IllegalStateException("스터디 현재 인원을 감소시키지 못했습니다.");
	        }

	        NotificationVO notificationVO = new NotificationVO();
	        notificationVO.setReceiverId(study.getLeaderId());
	        notificationVO.setStudyId(studyId);
	        notificationVO.setType("MEMBER_LEFT");
	        notificationVO.setMessage("스터디 참여 회원이 탈퇴했습니다: " + study.getTitle());
	        notificationService.insertNotification(notificationVO);

	    }

	    return leaveResult;
	}
	
	// 스터디 신청 이력 조회
	@Override
	public StudyMemberVO selectMemberApplication(StudyMemberVO studyMemberVO) {
		return studyDAO.selectMemberApplication(studyMemberVO);
	}
}
