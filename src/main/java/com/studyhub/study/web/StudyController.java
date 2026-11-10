package com.studyhub.study.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyhub.member.service.MemberVO;
import com.studyhub.study.service.StudyMemberVO;
import com.studyhub.study.service.StudyService;
import com.studyhub.study.service.StudyVO;

@Controller
public class StudyController {
    private final StudyService studyService;
    
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    // 스터디 목록 조회
    @GetMapping("/study/list.do")
    public String studyList(Model model) {
        List<StudyVO> studyList = studyService.selectStudyList();
        model.addAttribute("studyList", studyList);
        return "study/list";
    }
    
    // 스터디 등록
    @GetMapping("/study/write.do")
    public String writeForm() {
        return "study/write";
    }
    @PostMapping("/study/write.do")
    public String writeStudy(StudyVO studyVO, HttpSession session) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        studyVO.setLeaderId(loginMember.getMemberId());  // 세션에 로그인한 멤버의 아이디를 가져와 스터디 등록자 아이디로 설정
        studyService.insertStudy(studyVO);
        return "redirect:/study/list.do";
    }
    
    // 스터디 상세 조회
    @GetMapping("/study/detail.do")
    public String studyDetail(Long studyId, HttpSession session, Model model) {

        // 스터디 상세 정보
        StudyVO study = studyService.selectStudyDetail(studyId);
        model.addAttribute("study", study);
        // 현재 로그인 사용자
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        // 로그인 상태라면
        if (loginMember != null) {
            StudyMemberVO application = new StudyMemberVO();

            application.setStudyId(studyId);
            application.setMemberId(loginMember.getMemberId());
            StudyMemberVO myApplication = studyService.selectMemberApplication(application);
            model.addAttribute("myApplication", myApplication);
        }
        return "study/detail";
    }
    
    // 스터디 수정
    @GetMapping("/study/edit.do")
    public String editForm(Long studyId, HttpSession session, Model model) {
        StudyVO study = studyService.selectStudyDetail(studyId);
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");

        if (!study.getLeaderId().equals(loginMember.getMemberId())) {
            return "redirect:/study/detail.do?studyId=" + studyId;
        }
        model.addAttribute("study", study);
        return "study/edit";
    }
    
    @PostMapping("/study/edit.do")
    public String editStudy( StudyVO studyVO, HttpSession session) {
        StudyVO originalStudy = studyService.selectStudyDetail(studyVO.getStudyId());
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        
        if (!originalStudy.getLeaderId().equals(loginMember.getMemberId())) {
            return "redirect:/study/detail.do?studyId=" + studyVO.getStudyId();
        }
        studyService.updateStudy(studyVO);
        return "redirect:/study/detail.do?studyId=" + studyVO.getStudyId();
    }
    
    // 스터디 삭제
    @PostMapping("/study/delete.do")
    public String deleteStudy(Long studyId, HttpSession session) {
        StudyVO study = studyService.selectStudyDetail(studyId);
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");

        // 작성자가 아니면 삭제 불가
        if (!study.getLeaderId().equals(loginMember.getMemberId())) {
            return "redirect:/study/detail.do?studyId=" + studyId;
        }
        studyService.deleteStudy(studyId);
        return "redirect:/study/list.do";
    }
    
    // 스터디 신청(스터디 모집 공고에 멤버를 추가하는 것)
    @PostMapping("/study/apply.do")
    public String applyStudy(Long studyId, HttpSession session, Model model) {

        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        StudyMemberVO studyMemberVO = new StudyMemberVO();

        studyMemberVO.setStudyId(studyId);
        studyMemberVO.setMemberId(loginMember.getMemberId());

        String result = studyService.applyStudy(studyMemberVO);

        if ("WAIT".equals(result)) {
            model.addAttribute("message", "이미 신청 중인 스터디입니다.");
        }

        if ("APPROVED".equals(result)) {
            model.addAttribute("message", "이미 참여 중인 스터디입니다.");
        }

        if ("REAPPLIED".equals(result)) {
            model.addAttribute("message", "재신청이 완료되었습니다.");
        }

        if ("APPLIED".equals(result)) {
            model.addAttribute("message","스터디 신청이 완료되었습니다.");
        }

        if ("FULL".equals(result)) {
            model.addAttribute("message", "모집 인원이 마감되었습니다.");
        }
        
        if ("CLOSED".equals(result)) {
            model.addAttribute("message", "모집이 종료된 스터디입니다.");
        }

        // 스터디 정보 다시 조회
        StudyVO study = studyService.selectStudyDetail(studyId);
        model.addAttribute("study", study);

        // 현재 사용자의 신청 상태도 다시 조회
        StudyMemberVO myApplication = studyService.selectMemberApplication(studyMemberVO);
        model.addAttribute("myApplication", myApplication);
        
        return "study/detail";
    }
    
    // 스터디 신청자 관리 화면
    @GetMapping("/study/applicants.do")
    public String applicantList(Long studyId, HttpSession session, Model model) {
    	
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        StudyVO study = studyService.selectStudyDetail(studyId);

        // 스터디장만 접근 가능
        if (!study.getLeaderId().equals(loginMember.getMemberId())) {
            return "redirect:/study/detail.do?studyId=" + studyId;
        }
        List<StudyMemberVO> applicantList = studyService.selectStudyMemberList(studyId);
        model.addAttribute("study", study);
        model.addAttribute("applicantList", applicantList);
        return "study/applicants";
    }
    
    // 스터디 신청 승인
    @PostMapping("/study/approve.do")
    public String approveStudyMember(Long studyMemberId, Long studyId, HttpSession session) {

        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        
        studyService.approveStudyMember(studyMemberId, loginMember.getMemberId());
        return "redirect:/study/applicants.do?studyId=" + studyId;
    }
    
    // 스터디 신청 반려
    @PostMapping("/study/reject.do")
    public String rejectStudyMember(Long studyMemberId, Long studyId, HttpSession session) {

        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");

        studyService.rejectStudyMember(studyMemberId,loginMember.getMemberId());
        return "redirect:/study/applicants.do?studyId=" + studyId;
    }
    
    // 스터디 신청 취소
    @PostMapping("/study/cancelApply.do")
    public String cancelStudyApplication(Long studyId, HttpSession session) {

        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        StudyMemberVO studyMemberVO = new StudyMemberVO();

        studyMemberVO.setStudyId(studyId);
        studyMemberVO.setMemberId(loginMember.getMemberId());

        studyService.cancelStudyApplication(studyMemberVO);

        return "redirect:/study/detail.do?studyId=" + studyId;
    }
}