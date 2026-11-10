package com.studyhub.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studyhub.member.service.MemberService;
import com.studyhub.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	private final MemberDAO memberDAO;

	public MemberServiceImpl(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}

	// 전체 멤버 조회
	@Override
	public List<MemberVO> selectMemberList() {
		return memberDAO.selectMemberList();
	}
	
	// 멤버 가입
	@Override
	public int insertMember(MemberVO memberVO) {
	    return memberDAO.insertMember(memberVO);
	}
	
	// 멤버 로그인
	@Override
	public MemberVO login(MemberVO memberVO) {
	    return memberDAO.selectLoginMember(memberVO);
	}
}