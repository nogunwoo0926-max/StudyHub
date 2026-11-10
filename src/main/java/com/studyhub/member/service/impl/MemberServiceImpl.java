package com.studyhub.member.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyhub.member.service.MemberService;
import com.studyhub.member.service.MemberVO;
import com.studyhub.study.service.StudyService;

@Service
public class MemberServiceImpl implements MemberService {

	private final MemberDAO memberDAO;
	private final StudyService studyService;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public MemberServiceImpl(MemberDAO memberDAO, StudyService studyService) {
		this.memberDAO = memberDAO;
		this.studyService = studyService;
	}

	// 전체 멤버 조회
	@Override
	public List<MemberVO> selectMemberList() {
		return memberDAO.selectMemberList();
	}
	
	// 멤버 가입
	@Override
	public int insertMember(MemberVO memberVO) {
	    if (memberDAO.countByEmail(memberVO.getEmail()) > 0) {
	        return -1;
	    }

	    memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
	    return memberDAO.insertMember(memberVO);
	}
	
	// 멤버 로그인
	@Override
	public MemberVO login(MemberVO memberVO) {
	    MemberVO loginMember = memberDAO.selectMemberById(memberVO.getMemberId());

	    if (loginMember == null) {
	        return null;
	    }

	    String storedPassword = loginMember.getPassword();

	    if (isBcryptPassword(storedPassword)) {
	        return passwordEncoder.matches(memberVO.getPassword(), storedPassword) ? loginMember : null;
	    }

	    if (!storedPassword.equals(memberVO.getPassword())) {
	        return null;
	    }

	    MemberVO updateVO = new MemberVO();
	    updateVO.setMemberId(loginMember.getMemberId());
	    updateVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
	    memberDAO.updatePassword(updateVO);
	    loginMember.setPassword(updateVO.getPassword());

	    return loginMember;
	}

	// 회원정보 수정
	@Override
	public int updateMember(MemberVO memberVO) {
	    if (memberDAO.countByEmailExceptMemberId(memberVO) > 0) {
	        return -1;
	    }

	    return memberDAO.updateMember(memberVO);
	}

	// 비밀번호 변경
	@Override
	public int updatePassword(String memberId, String currentPassword, String newPassword) {
	    if (newPassword == null || newPassword.length() < 8) {
	        return -2;
	    }

	    if (currentPassword == null) {
	        return -1;
	    }

	    MemberVO member = memberDAO.selectMemberById(memberId);

	    if (member == null || !matchesPassword(currentPassword, member.getPassword())) {
	        return -1;
	    }

	    MemberVO updateVO = new MemberVO();
	    updateVO.setMemberId(memberId);
	    updateVO.setPassword(passwordEncoder.encode(newPassword));

	    return memberDAO.updatePassword(updateVO);
	}

	// 회원 탈퇴
	@Override
	@Transactional
	public int withdrawMember(String memberId, String password) {
	    MemberVO member = memberDAO.selectMemberById(memberId);

	    if (member == null || !matchesPassword(password, member.getPassword())) {
	        return -1;
	    }

	    studyService.cleanupMemberOnWithdraw(memberId);

	    return memberDAO.withdrawMember(memberId);
	}

	private boolean matchesPassword(String rawPassword, String storedPassword) {
	    if (isBcryptPassword(storedPassword)) {
	        return passwordEncoder.matches(rawPassword, storedPassword);
	    }

	    return storedPassword.equals(rawPassword);
	}

	private boolean isBcryptPassword(String password) {
	    return password != null && password.startsWith("$2");
	}
}
