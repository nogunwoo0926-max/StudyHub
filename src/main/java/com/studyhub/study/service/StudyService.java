package com.studyhub.study.service;

import java.util.List;

public interface StudyService {
	
	// 스터디 목록 조회
    List<StudyVO> selectStudyList();

    // 스터디 검색 및 상태 필터 조회
    List<StudyVO> selectStudyList(StudySearchVO searchVO);

    // 스터디 검색 결과 개수 조회
    int selectStudyCount(StudySearchVO searchVO);
    
    // 스터디 등록
    int insertStudy(StudyVO studyVO);
    
    // 스터디 상세 조회
    StudyVO selectStudyDetail(Long studyId);
    
    // 스터디 수정
    int updateStudy(StudyVO studyVO);

    // 스터디장이 모집 마감
    int closeStudy(Long studyId, String loginMemberId);

    // 스터디장이 모집 재개
    int openStudy(Long studyId, String loginMemberId);
    
    // 스터디 삭제
    int deleteStudy(StudyVO studyVO);
    
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

    // 승인된 회원의 스터디 탈퇴
    int leaveStudy(Long studyId, String memberId);

    // 스터디 신청 이력 확인
    StudyMemberVO selectMemberApplication(StudyMemberVO studyMemberVO);
    
    // 내가 신청한 스터디 목록 조회
    List<StudyMemberVO> selectMyApplicationList(String memberId);

    // 내가 모집 중인 스터디 목록 조회
    List<StudyVO> selectMyStudyList(String leaderId);

    // 내가 만든 스터디 전체 이력 조회
    List<StudyVO> selectMyStudyHistoryList(String leaderId);

    // 회원 탈퇴에 따른 스터디·신청 데이터 정리
    int cleanupMemberOnWithdraw(String memberId);
}
