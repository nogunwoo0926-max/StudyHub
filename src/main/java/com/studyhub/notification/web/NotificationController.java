package com.studyhub.notification.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyhub.member.service.MemberVO;
import com.studyhub.notification.service.NotificationService;
import com.studyhub.notification.service.NotificationVO;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 알림 읽음 처리
    @PostMapping("/notification/read.do")
    public String markAsRead(Long notificationId, HttpSession session) {
        MemberVO loginMember = (MemberVO) session.getAttribute("loginMember");
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setNotificationId(notificationId);
        notificationVO.setReceiverId(loginMember.getMemberId());

        notificationService.markAsRead(notificationVO);

        return "redirect:/member/mypage.do";
    }
}
