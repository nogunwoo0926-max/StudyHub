package com.studyhub.notification.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.studyhub.notification.service.NotificationVO;

@Repository
public class NotificationDAO {

    private final SqlSessionTemplate sqlSession;

    public NotificationDAO(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    // 알림 등록
    public int insertNotification(NotificationVO notificationVO) {
        return sqlSession.insert("notificationMapper.insertNotification", notificationVO);
    }

    // 회원 알림 목록 조회
    public List<NotificationVO> selectNotificationList(String receiverId) {
        return sqlSession.selectList("notificationMapper.selectNotificationList", receiverId);
    }

    // 알림 읽음 처리
    public int markAsRead(NotificationVO notificationVO) {
        return sqlSession.update("notificationMapper.markAsRead", notificationVO);
    }
}
