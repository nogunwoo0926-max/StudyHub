package com.studyhub.study.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.studyhub.study.service.StudyMemberVO;
import com.studyhub.study.service.StudyService;
import com.studyhub.study.service.StudyVO;

@Service
public class StudyServiceImpl implements StudyService {
    private final StudyDAO studyDAO;
    public StudyServiceImpl(StudyDAO studyDAO) {
        this.studyDAO = studyDAO;
    }
    
    // 스터디 목록 조회
    @Override
    public List<StudyVO> selectStudyList() {
        return studyDAO.selectStudyList();
    }
    
    // 스터디 등록
    @Override
    public int insertStudy(StudyVO studyVO) {
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
        return studyDAO.updateStudy(studyVO);
    }

    // 스터디 삭제
	@Override
	@Transactional
	public int deleteStudy(Long studyId) {
	    // 신청 기록은 삭제하지 않고 취소 상태로 남긴다.
	    studyDAO.cancelStudyMembers(studyId);
	    return studyDAO.deleteStudy(studyId);
	}
	
	// 스터디 신청(스터디 모집 공고에 멤버를 추가하는 것)
	@Override
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
	        studyDAO.insertStudyMember(studyMemberVO);
	        return "APPLIED";
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
	        studyDAO.reapplyStudy(studyMemberVO);
	        return "REAPPLIED";
	    }
	    return "ERROR";
	}

	// 스터디 신청자 목록 조회
	@Override
	public List<StudyMemberVO> selectStudyMemberList(Long studyId) {
	    return studyDAO.selectStudyMemberList(studyId);
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
	    }
	    return result;
	}

	// 스터디 신청 반려
	@Override
	public int rejectStudyMember(Long studyMemberId, String loginMemberId) {

	    StudyMemberVO studyMember = studyDAO.selectStudyMember(studyMemberId);
	    StudyVO study = studyDAO.selectStudyDetail(studyMember.getStudyId());

	    if (!study.getLeaderId().equals(loginMemberId)) {
	        return 0;
	    }
	    return studyDAO.rejectStudyMember(studyMemberId);
	}
	
	// 스터디 신청 취소
	@Override
	public int cancelStudyApplication(StudyMemberVO studyMemberVO) {
		
	    StudyMemberVO existing = studyDAO.selectMemberApplication(studyMemberVO);

	    if (existing == null) {
	        return 0;
	    }
	    
	    // 본인의 WAIT 신청만 취소 가능
	    if (!"WAIT".equals(existing.getStatus())) {
	        return 0;
	    }
	    return studyDAO.cancelStudyApplication(studyMemberVO);
	}
	
	// 스터디 신청 이력 조회
	@Override
	public StudyMemberVO selectMemberApplication(StudyMemberVO studyMemberVO) {
		return studyDAO.selectMemberApplication(studyMemberVO);
	}
}
