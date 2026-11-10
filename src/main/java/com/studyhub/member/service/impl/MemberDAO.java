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

}