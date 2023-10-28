package com.class302.omzteam.organization.service;

import com.class302.omzteam.mybatis.DeptDao;
import com.class302.omzteam.mybatis.JobDao;
import com.class302.omzteam.mybatis.MemberDao;
import com.class302.omzteam.organization.model.Member;
import com.class302.omzteam.organization.model.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {


    @Autowired
    MemberDao memberDao;
    @Autowired
    DeptDao deptDao;
    @Autowired
    JobDao jobDao;


    public UserCommand user(int mem_no){
        Member m = memberDao.memberByno(mem_no);
        UserCommand user = new UserCommand(m.getMem_no(),m.getMem_name(),m.getDept_no(),m.getJob(),
                jobname(m.getJob()),m.getBirth(),m.getEmail(),m.getPhone(),dateisString(m.getHiredate()),
                deptDao.deptByno(m.getDept_no()));
    return user;
    }
    public UserCommand userSet(Member m) {
        UserCommand user = new UserCommand(m.getMem_no(),m.getMem_name(),m.getDept_no(),m.getJob(),
                jobname(m.getJob()),m.getBirth(),m.getEmail(),m.getPhone(),dateisString(m.getHiredate()),
                deptDao.deptByno(m.getDept_no()));

        return user;
    }

    public List<UserCommand> users() {

        List<UserCommand>list = new ArrayList<>();
        List<Member> ms = memberDao.memberAll();
        for(Member m : ms) {
            UserCommand user = new UserCommand(m.getMem_no(),m.getMem_name(),m.getDept_no(),m.getJob(),
                    jobname(m.getJob()),m.getBirth(),m.getEmail(),m.getPhone(),dateisString(m.getHiredate()),
                    deptDao.deptByno(m.getDept_no()));
            list.add(user);
        };
        return list;
    }

    public List<UserCommand> usersSet(List<Member> members) {

        List<UserCommand>list = new ArrayList<>();
        for(Member m : members) {
            UserCommand user = new UserCommand(m.getMem_no(),m.getMem_name(),m.getDept_no(),m.getJob(),
                    jobname(m.getJob()),m.getBirth(),m.getEmail(),m.getPhone(),dateisString(m.getHiredate()),
                    deptDao.deptByno(m.getDept_no()));
            list.add(user);
        };
        return list;
    }

    public String dateisString(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }
    public String jobname(int job){
        return jobDao.jobByno(job);
    }

}
