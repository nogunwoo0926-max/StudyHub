package com.studyhub.member.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyhub.member.service.MemberService;
import com.studyhub.member.service.MemberVO;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 멤버 조회
    @GetMapping("/member/list.do")
    public String memberList(Model model) {
        List<MemberVO> memberList =
                memberService.selectMemberList();
        model.addAttribute("memberList", memberList);
        return "member/list";
    }
    
    // 멤버 가입
    @GetMapping("/member/join.do")
    public String joinForm() {
        return "member/join";
    }
    @PostMapping("/member/join.do")
    public String join(MemberVO memberVO) {
        memberService.insertMember(memberVO);
        return "redirect:/member/login.do";
    }
    
    // 멤버 로그인 기능
    @GetMapping("/member/login.do")
    public String loginForm() {
        return "member/login";
    }
    @PostMapping("/member/login.do")
    public String login(MemberVO memberVO,
                        HttpSession session,
                        Model model) {
        MemberVO loginMember =
                memberService.login(memberVO);
        if (loginMember == null) {
            model.addAttribute(
                    "loginError",
                    "아이디 또는 비밀번호가 올바르지 않습니다."
            );
            return "member/login";
        }
        session.setAttribute(
                "loginMember",
                loginMember
        );
        return "redirect:/main.do";
    }
    
    // 멤버 로그아웃
    @GetMapping("/member/logout.do")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main.do";
    }
    
    // 마이페이지
    @GetMapping("/member/mypage.do")
    public String mypage() {
        return "member/mypage";
    }
}