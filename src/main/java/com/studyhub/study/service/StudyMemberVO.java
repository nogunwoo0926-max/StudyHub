package com.studyhub.study.service;

import java.util.Date;

public class StudyMemberVO {

    private Long studyMemberId;
    private Long studyId;
    private String memberId;
    private String status;
    private Date applyDate;

    public Long getStudyMemberId() {
        return studyMemberId;
    }

    public void setStudyMemberId(Long studyMemberId) {
        this.studyMemberId = studyMemberId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
}