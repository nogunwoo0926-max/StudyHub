package com.studyhub.member.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyhub.notification.service.NotificationService;
import com.studyhub.notification.service.NotificationVO;
import com.studyhub.member.service.MemberService;
import com.studyhub.member.service.MemberVO;
import com.studyhub.study.service.StudyMemberVO;
import com.studyhub.study.service.StudyService;
import com.studyhub.study.service.StudyVO;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final StudyService studyService;
    private final NotificationService notificationService;

    public MemberController(MemberService memberService, StudyService studyService, NotificationService notificationService) {
        this.memberService = memberService;
        this.studyService = studyService;
        this.notificationService = notificationService;
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
    public String join(MemberVO memberVO, Model model) {
        int result = memberService.insertMember(memberVO);

        if (result == -1) {
            model.addAttribute("joinError", "이미 사용 중인 이메일입니다.");
            return "member/join";
        }

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
    public String mypage(HttpSession session, Model model) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        List<StudyMemberVO> applicationList = studyService.selectMyApplicationList(loginMember.getMemberId());
        List<StudyVO> myStudyList = studyService.selectMyStudyList(loginMember.getMemberId());
        List<StudyVO> myStudyHistoryList = studyService.selectMyStudyHistoryList(loginMember.getMemberId());
        List<NotificationVO> notificationList = notificationService.selectNotificationList(loginMember.getMemberId());
        model.addAttribute("applicationList", applicationList);
        model.addAttribute("myStudyList", myStudyList);
        model.addAttribute("myStudyHistoryList", myStudyHistoryList);
        model.addAttribute("notificationList", notificationList);
        return "member/mypage";
    }

    // 회원정보 수정 화면
    @GetMapping("/member/edit.do")
    public String editForm(HttpSession session, Model model) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        model.addAttribute("member", loginMember);
        return "member/edit";
    }

    // 회원정보 수정
    @PostMapping("/member/edit.do")
    public String editMember(MemberVO memberVO, HttpSession session, Model model) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");

        // 회원 ID는 클라이언트 입력값이 아니라 세션 정보 사용
        memberVO.setMemberId(loginMember.getMemberId());
        int result = memberService.updateMember(memberVO);

        if (result == -1) {
            model.addAttribute("member", memberVO);
            model.addAttribute("editError", "이미 사용 중인 이메일입니다.");
            return "member/edit";
        }

        loginMember.setName(memberVO.getName());
        loginMember.setEmail(memberVO.getEmail());
        session.setAttribute("loginMember", loginMember);

        return "redirect:/member/mypage.do";
    }

    // 비밀번호 변경 화면
    @GetMapping("/member/password.do")
    public String passwordForm() {
        return "member/password";
    }

    // 비밀번호 변경
    @PostMapping("/member/password.do")
    public String changePassword(String currentPassword, String newPassword, String confirmPassword, HttpSession session, Model model) {
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordError", "새 비밀번호가 일치하지 않습니다.");
            return "member/password";
        }

        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        int result = memberService.updatePassword(loginMember.getMemberId(), currentPassword, newPassword);

        if (result == -1) {
            model.addAttribute("passwordError", "현재 비밀번호가 올바르지 않습니다.");
            return "member/password";
        }

        if (result == -2) {
            model.addAttribute("passwordError", "새 비밀번호는 8자 이상이어야 합니다.");
            return "member/password";
        }

        return "redirect:/member/mypage.do";
    }

    // 회원 탈퇴 화면
    @GetMapping("/member/withdraw.do")
    public String withdrawForm() {
        return "member/withdraw";
    }

    // 회원 탈퇴
    @PostMapping("/member/withdraw.do")
    public String withdrawMember(String password, HttpSession session, Model model) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        int result = memberService.withdrawMember(loginMember.getMemberId(), password);

        if (result == -1) {
            model.addAttribute("withdrawError", "비밀번호가 올바르지 않습니다.");
            return "member/withdraw";
        }

        session.invalidate();
        return "redirect:/main.do";
    }
}
