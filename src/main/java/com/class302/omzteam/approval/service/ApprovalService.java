package com.class302.omzteam.approval.service;

import com.class302.omzteam.mybatis.ApprovalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {
    private final ApprovalDao approvalDao;

    @Autowired
    public ApprovalService(ApprovalDao approvalDao) {
        this.approvalDao = approvalDao;
    }

}
