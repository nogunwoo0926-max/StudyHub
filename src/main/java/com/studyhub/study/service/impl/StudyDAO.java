package com.studyhub.study.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.studyhub.study.service.StudyMemberVO;
import com.studyhub.study.service.StudySearchVO;
import com.studyhub.study.service.StudyVO;

@Repository
public class StudyDAO {
    private final SqlSessionTemplate sqlSession;
    public StudyDAO(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    // 스터디 목록 조회
    public List<StudyVO> selectStudyList() {
        return sqlSession.selectList("studyMapper.selectStudyList");
    }

    // 스터디 검색 및 상태 필터 조회
    public List<StudyVO> selectStudyList(StudySearchVO searchVO) {
        return sqlSession.selectList("studyMapper.selectStudyListBySearch", searchVO);
    }

    // 스터디 검색 결과 개수 조회
    public int selectStudyCount(StudySearchVO searchVO) {
        return sqlSession.selectOne("studyMapper.selectStudyCount", searchVO);
    }
    
    // 스터디 등록
    public int insertStudy(StudyVO studyVO) {
        return sqlSession.insert("studyMapper.insertStudy", studyVO);
    }
    
    // 스터디 상세 조회
    public StudyVO selectStudyDetail(Long studyId) {
    	return sqlSession.selectOne("studyMapper.selectStudyDetail",studyId);
    }
    
    // 스터디 내용 수정
    public int updateStudy(StudyVO studyVO) {
        return sqlSession.update("studyMapper.updateStudy",studyVO);
    }

    // 스터디장이 모집 마감
    public int closeStudy(Long studyId) {
        return sqlSession.update("studyMapper.closeStudy", studyId);
    }

    // 스터디장이 모집 재개
    public int openStudy(Long studyId) {
        return sqlSession.update("studyMapper.openStudy", studyId);
    }
    
    // 스터디 삭제
    public int deleteStudy(StudyVO studyVO) {
        return sqlSession.update("studyMapper.deleteStudy", studyVO);
    }

    // 스터디 취소 시 기존 신청 이력도 취소 상태로 보존
    public int cancelStudyMembers(Long studyId) {
        return sqlSession.update("studyMapper.cancelStudyMembers", studyId);
    }
    
    // 스터디 신청(스터디 모집 공고에 멤버를 추가하는 것)
    public int insertStudyMember(StudyMemberVO studyMemberVO) {
        return sqlSession.insert("studyMapper.insertStudyMember", studyMemberVO);
    }
    
    // 스터디 신청자 목록 조회
    public List<StudyMemberVO> selectStudyMemberList(Long studyId) {
        return sqlSession.selectList("studyMapper.selectStudyMemberList", studyId);
    }
    
    // 스터디 신청 승인
    public int approveStudyMember(Long studyMemberId) {
        return sqlSession.update("studyMapper.approveStudyMember",studyMemberId);
    }
    
    // 스터디 신청 반려
    public int rejectStudyMember(Long studyMemberId) {
        return sqlSession.update("studyMapper.rejectStudyMember",studyMemberId);
    }
    
    // 스터디 인원 증가
    public int increaseCurrentMember(Long studyId) {
        return sqlSession.update("studyMapper.increaseCurrentMember",studyId);
    }

    // 모집 정원 도달 시 모집 마감
    public int closeStudyIfFull(Long studyId) {
        return sqlSession.update("studyMapper.closeStudyIfFull", studyId);
    }
    
    // 스터디 신청자 정보 검증
    public StudyMemberVO selectStudyMember(Long studyMemberId) {
        return sqlSession.selectOne("studyMapper.selectStudyMember",studyMemberId);
    }
    
    // 사용자의 과거 스터디 신청 기록 확인용
    public StudyMemberVO selectMemberApplication(StudyMemberVO studyMemberVO) {
        return sqlSession.selectOne( "studyMapper.selectMemberApplication", studyMemberVO);
    }
    
    // 스터디 재신청
    public int reapplyStudy(StudyMemberVO studyMemberVO) {
        return sqlSession.update( "studyMapper.reapplyStudy", studyMemberVO);
    }
    
    // 스터디 신청 취소
    public int cancelStudyApplication(StudyMemberVO studyMemberVO) {
        return sqlSession.update( "studyMapper.cancelStudyApplication", studyMemberVO);
    }

    // 승인된 회원의 스터디 탈퇴
    public int leaveStudyApplication(StudyMemberVO studyMemberVO) {
        return sqlSession.update("studyMapper.leaveStudyApplication", studyMemberVO);
    }

    // 스터디 현재 인원 감소
    public int decreaseCurrentMember(Long studyId) {
        return sqlSession.update("studyMapper.decreaseCurrentMember", studyId);
    }

    // 내가 신청한 스터디 목록 조회
    public List<StudyMemberVO> selectMyApplicationList(String memberId) {
        return sqlSession.selectList("studyMapper.selectMyApplicationList", memberId);
    }

    // 내가 모집 중인 스터디 목록 조회
    public List<StudyVO> selectMyStudyList(String leaderId) {
        return sqlSession.selectList("studyMapper.selectMyStudyList", leaderId);
    }

   // 내가 만든 스터디 전체 이력 조회
   public List<StudyVO> selectMyStudyHistoryList(String leaderId) {
       return sqlSession.selectList("studyMapper.selectMyStudyHistoryList", leaderId);
   }
    
}
