package com.studyhub.member.service;

import java.util.List;

public interface MemberService {

	// 전체 멤버 조회
    List<MemberVO> selectMemberList();
    
    // 멤버 가입
    int insertMember(MemberVO memberVO);
    
    // 멤버 로그인
    MemberVO login(MemberVO memberVO);
}