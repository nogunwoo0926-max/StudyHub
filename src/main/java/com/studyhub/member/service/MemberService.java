package com.studyhub.member.service;

import java.util.List;

public interface MemberService {

	// 전체 멤버 조회
    List<MemberVO> selectMemberList();
    
    // 멤버 가입
    int insertMember(MemberVO memberVO);
    
    // 멤버 로그인
    MemberVO login(MemberVO memberVO);

    // 회원정보 수정
    int updateMember(MemberVO memberVO);

    // 비밀번호 변경
    int updatePassword(String memberId, String currentPassword, String newPassword);

    // 회원 탈퇴
    int withdrawMember(String memberId, String password);
}
