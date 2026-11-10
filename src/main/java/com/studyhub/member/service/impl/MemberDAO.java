package com.studyhub.member.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.studyhub.member.service.MemberVO;

@Repository
public class MemberDAO {

    private final SqlSessionTemplate sqlSession;

    public MemberDAO(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    // 전체 멤버 조회
    public List<MemberVO> selectMemberList() {
        return sqlSession.selectList(
                "memberMapper.selectMemberList"
        );
    }
    
    // 신규 멤버 가입
    public int insertMember(MemberVO memberVO) {
    	return sqlSession.insert(
    			"memberMapper.insertMember",
    			memberVO
    	);
    }
    
    // 멤버 로그인
    public MemberVO selectLoginMember(MemberVO memberVO) {
        return sqlSession.selectOne(
                "memberMapper.selectLoginMember",
                memberVO
        );
    }

    // 회원정보 수정
    public int updateMember(MemberVO memberVO) {
        return sqlSession.update("memberMapper.updateMember", memberVO);
    }

    // 이메일 중복 확인
    public int countByEmail(String email) {
        return sqlSession.selectOne("memberMapper.countByEmail", email);
    }

    // 회원 본인을 제외한 이메일 중복 확인
    public int countByEmailExceptMemberId(MemberVO memberVO) {
        return sqlSession.selectOne("memberMapper.countByEmailExceptMemberId", memberVO);
    }

    // 아이디로 회원 조회
    public MemberVO selectMemberById(String memberId) {
        return sqlSession.selectOne("memberMapper.selectMemberById", memberId);
    }

    // 비밀번호 변경
    public int updatePassword(MemberVO memberVO) {
        return sqlSession.update("memberMapper.updatePassword", memberVO);
    }

    // 회원 탈퇴
    public int withdrawMember(String memberId) {
        return sqlSession.update("memberMapper.withdrawMember", memberId);
    }

}
