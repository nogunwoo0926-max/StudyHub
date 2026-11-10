package com.studyhub.study.service;

import java.util.List;

public interface StudyService {
	
	// 스터디 목록 조회
    List<StudyVO> selectStudyList();
    
    // 스터디 등록
    int insertStudy(StudyVO studyVO);
    
    // 스터디 상세 조회
    StudyVO selectStudyDetail(Long studyId);
    
    // 스터디 수정
    int updateStudy(StudyVO studyVO);
    
    // 스터디 삭제
    int deleteStudy(Long studyId);
    
    // 스터디 신청(스터디 모집 공고에 멤버를 추가하는 것)
    String applyStudy(StudyMemberVO studyMemberVO);
    
    // 스터디 신청자 목록 조회
    List<StudyMemberVO> selectStudyMemberList(Long studyId);

    // 스터디 신청 승인
    int approveStudyMember(Long studyMemberId, String loginMemberId);

    // 스터디 신청 반려
    int rejectStudyMember(Long studyMemberId, String loginMemberId);
    
    // 스터디 신청 취소
    int cancelStudyApplication(StudyMemberVO studyMemberVO);

    // 스터디 신청 이력 확인
    StudyMemberVO selectMemberApplication(StudyMemberVO studyMemberVO);
}