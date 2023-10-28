package com.class302.omzteam.controller;


import com.class302.omzteam.approval.model.ApprovalCommand;
import com.class302.omzteam.approval.model.ApprovalDto;
import com.class302.omzteam.approval.model.ApprovalMemberDto;
import com.class302.omzteam.approval.model.ApprovalSubDto;
import com.class302.omzteam.approval.service.ApprovalService;
import com.class302.omzteam.approval.service.ApprovalService2;
import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.mybatis.ApprovalDao;
import com.class302.omzteam.mybatis.MemberDao;
import com.class302.omzteam.organization.model.UserCommand;
import com.class302.omzteam.organization.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    ApprovalDao approvalDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    ApprovalService2 approvalService2;

    @Autowired
    UserService userService;

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }


    //작성 폼으로
    @GetMapping("/writeForm")
    public String getPostForm(Model model) {
        // 폼을 렌더링하기 위한 데이터를 모델에 추가
        model.addAttribute("approvalDto", new ApprovalDto());
        return "/approval/writeForm";
    }

    //작성 결과 폼으로
    @PostMapping("/approvalResultForm")
    public String submitPost(ApprovalDto approvalDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ApprovalMemberDto approvalMemberDto = approvalDao.selectTwo(Integer.parseInt(user.getUsername()));
        MemberDto member = memberDao.selectOneMem(user.getUsername());
        approvalDto.setMember_name(member.getMem_name());
        approvalDto.setMember_no(Integer.parseInt(user.getUsername()));
        approvalDto.setDept_no(approvalMemberDto.getDept_no());
        approvalDto.setJob1(approvalMemberDto.getJob());

        approvalDao.insert1(approvalDto);


        return "redirect:/approval/approvalMain";
    }

    //메인으로 이동(게시판)
    @GetMapping("/approvalMain")
    public String boardMain(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin = (User) authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));//맴버 정보가져오기
        List<ApprovalCommand> list = null;
        if (user.getJob_no() == 1) {
            list = approvalService2.ApprovalService();
        } else {
            list = approvalService2.ApprovalListDept(approvalDao.findAll2(user.getDept_no()));
        }
        model.addAttribute("list", list);
        return "/approval/approvalMain";
    }


    @GetMapping("/approvalvalue")
    public void boardvalue(@RequestParam(value = "value")String value,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userin =(User)authentication.getPrincipal();
        UserCommand user = userService.user(Integer.parseInt(userin.getUsername()));//맴버 정보가져오기
        List<ApprovalCommand> list = null;
        if (user.getJob_no() == 1) {
            list = approvalService2.ApprovalListDept(approvalDao.valueAll(value));
        } else {
            list = approvalService2.ApprovalListDept(approvalDao.valueDept(user.getDept_no(),value));
        }
        model.addAttribute("list", list);
    }



    //목록 버튼을 누르면 메인창으로
    @GetMapping("/goMain")
    public String goMain() {
        return "redirect:/approval/approvalMain";
    }

    //글 하나 골라서 읽기
    @GetMapping("/goRead")
    public String goRead(@RequestParam int board_id, Model model) {
        //
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("user: "+user);
        ApprovalMemberDto approvalMemberDto = approvalDao.selectTwo(Integer.parseInt(user.getUsername()));
        System.out.println("approvalMemberDto: "+approvalMemberDto);
        model.addAttribute("memDto", approvalMemberDto);
        System.out.println("??????????????????????" + approvalMemberDto);
        //
        ApprovalDto dto = approvalDao.selectOne(board_id);
        System.out.println(dto);
        model.addAttribute("approvalDto", dto);

        return "/approval/resultForm";
    }

    //작성 폼으로 이동
    @GetMapping("/doWrite")
    public String doWrite() {
        return "redirect:/approval/writeForm";
    }

    //결재 폼 작성
    @GetMapping("/approvalWriteForm")
    public String showApprovalForm(@RequestParam int board_id,Model model) {
    ApprovalDto dto = approvalDao.selectOne(board_id);
    model.addAttribute("approvalDto", dto);
        model.addAttribute("board_id", board_id);
        return "/approval/approvalWriteForm";
    }

//    @PostMapping("/ResultForm")
//    public String processApprovalForm(@ModelAttribute ApprovalDto approvalDto) {
//        approvalDao.updateOne(approvalDto);
//        return "resultForm";
//    }

    @PostMapping("/approvalSuccessForm")
    public void aSF(ApprovalSubDto SubDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        MemberDto member = memberDao.selectOneMem(user.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ApprovalMemberDto approvalMemberDto = approvalDao.selectTwo(Integer.parseInt(user.getUsername()));
        MemberDto member = memberDao.selectOneMem(user.getUsername());

        SubDto.setMember_name2(member.getMem_name());
        SubDto.setMember_no2(Integer.parseInt(user.getUsername()));
        SubDto.setMember_no2(approvalMemberDto.getDept_no());
        SubDto.setJob2(approvalMemberDto.getJob());
        approvalDao.updateOne(SubDto);
    }

    @PostMapping("/approvalFailForm")
    public void aSF2(ApprovalSubDto SubDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        MemberDto member = memberDao.selectOneMem(user.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ApprovalMemberDto approvalMemberDto = approvalDao.selectTwo(Integer.parseInt(user.getUsername()));
        MemberDto member = memberDao.selectOneMem(user.getUsername());

        SubDto.setMember_name2(member.getMem_name());
        SubDto.setMember_no2(Integer.parseInt(user.getUsername()));
        SubDto.setMember_no2(approvalMemberDto.getDept_no());
        SubDto.setJob2(approvalMemberDto.getJob());
        approvalDao.updateTwo(SubDto);
    }

    @GetMapping("/deleteApproval")
    public String delete(@RequestParam int board_id) {
        approvalDao.deleteOne(board_id);
        return "redirect:/approval/approvalMain";
    }

    @GetMapping("/approvalResultForm")
    public String result() {
        return "/approval/approvalResultForm";
    }



}
