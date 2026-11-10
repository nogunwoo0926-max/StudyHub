package com.studyhub.notification.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studyhub.notification.service.NotificationService;
import com.studyhub.notification.service.NotificationVO;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationServiceImpl(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    // 알림 등록
    @Override
    public int insertNotification(NotificationVO notificationVO) {
        return notificationDAO.insertNotification(notificationVO);
    }

    // 회원 알림 목록 조회
    @Override
    public List<NotificationVO> selectNotificationList(String receiverId) {
        return notificationDAO.selectNotificationList(receiverId);
    }

    // 알림 읽음 처리
    @Override
    public int markAsRead(NotificationVO notificationVO) {
        return notificationDAO.markAsRead(notificationVO);
    }
}
