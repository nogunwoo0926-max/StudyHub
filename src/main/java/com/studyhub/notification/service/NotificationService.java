package com.studyhub.notification.service;

import java.util.List;

public interface NotificationService {

    // 알림 등록
    int insertNotification(NotificationVO notificationVO);

    // 회원 알림 목록 조회
    List<NotificationVO> selectNotificationList(String receiverId);

    // 알림 읽음 처리
    int markAsRead(NotificationVO notificationVO);
}
