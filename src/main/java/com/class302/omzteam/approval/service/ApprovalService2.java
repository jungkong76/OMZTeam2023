package com.class302.omzteam.approval.service;

import com.class302.omzteam.approval.model.ApprovalCommand;
import com.class302.omzteam.approval.model.ApprovalDto;
import com.class302.omzteam.mybatis.ApprovalDao;
import com.class302.omzteam.mybatis.DeptDao;
import com.class302.omzteam.mybatis.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApprovalService2 {



    @Autowired
    ApprovalDao approvalDao;
    @Autowired
    DeptDao deptDao;
    @Autowired
    JobDao jobDao;

    public List<ApprovalCommand> ApprovalService() {
        List<ApprovalDto> list2 = approvalDao.findAll();
        List<ApprovalCommand> list = new ArrayList<>();
        for(ApprovalDto app : list2){
            ApprovalCommand approvalCommand = new ApprovalCommand(app.getBoard_id(),
                                                                app.getPass(),
                                                                deptDao.deptByno(app.getDept_no()),
                                                                app.getMember_name(),
                                                                app.getMember_no(),
                                                                jobDao.jobByno(app.getJob1()),
                                                                app.getMember_name2(),
                                                                app.getMember_no2(),
                                                                jobDao.jobByno(app.getJob2()),
                                                                app.getRegdate(),
                                                                app.getComdate(),
                                                                app.getTitle(),
                                                                app.getContent(),
                                                                app.getComment());
        list.add(approvalCommand);
        }
        return list;
    }
    public List<ApprovalCommand> ApprovalListDept(List<ApprovalDto> dto) {
        List<ApprovalCommand> list = new ArrayList<>();
        for(ApprovalDto app : dto){
            ApprovalCommand approvalCommand = new ApprovalCommand(app.getBoard_id(),
                    app.getPass(),
                    deptDao.deptByno(app.getDept_no()),
                    app.getMember_name(),
                    app.getMember_no(),
                    jobDao.jobByno(app.getJob1()),
                    app.getMember_name2(),
                    app.getMember_no2(),
                    jobDao.jobByno(app.getJob2()),
                    app.getRegdate(),
                    app.getComdate(),
                    app.getTitle(),
                    app.getContent(),
                    app.getComment());
            list.add(approvalCommand);
        }

        return list;
    }

}
