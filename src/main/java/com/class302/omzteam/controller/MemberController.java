package com.class302.omzteam.controller;


import com.class302.omzteam.mybatis.DeptDao;
import com.class302.omzteam.mybatis.MemberDao;
import com.class302.omzteam.organization.model.Dept;
import com.class302.omzteam.organization.model.Member;
import com.class302.omzteam.organization.model.UserCommand;
import com.class302.omzteam.organization.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberDao memberDao;

    @Autowired
    UserService userService;

    @Autowired
    DeptDao deptDao;

    @GetMapping({"/test"})
    public void test(HttpSession session, Model model) {
        List<UserCommand> users = userService.users();
        model.addAttribute("users",users);
    }

    @GetMapping({"/depttable"})
    public void depttest(Model model) {
        List<Dept> depts = deptDao.deptAll();
        List<Member> members = memberDao.memberAll();
        model.addAttribute("depts",depts);
        model.addAttribute("members",members);
    }

    @GetMapping({"/input"})
    @ResponseBody
    public UserCommand ax(@RequestParam int mem_no){
        Member member = memberDao.memberByno(mem_no);
        return userService.userSet(member);
    }


}