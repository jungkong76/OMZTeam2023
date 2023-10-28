package com.class302.omzteam.controller;

import com.class302.omzteam.dept_board.model.Dept_board;
import com.class302.omzteam.login.service.ResetPasswordService;
import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.mybatis.Dept_boardDao;
import com.class302.omzteam.mybatis.LoginDao;
import com.class302.omzteam.login.service.CustomUserDetails;
import com.class302.omzteam.organization.model.UserCommand;
import com.class302.omzteam.organization.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    LoginDao loginDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    Dept_boardDao deptBoardDao;

    @Autowired
    ResetPasswordService resetPassword;


    @GetMapping("/login/joinForm")
    public String registerForm(Model model) {

        Long nextMno = loginDao.getNextNum();
        MemberDto memberDto = new MemberDto();
        memberDto.setMem_no(nextMno);

        model.addAttribute("memberDto", memberDto);
        return "login/joinForm";
    }

    @PostMapping("/login/joinForm")
    public String registerMember(@ModelAttribute MemberDto memberDto) throws Exception {

        String encryptedPassword = passwordEncoder.encode(memberDto.getMem_pw());
        memberDto.setMem_pw(encryptedPassword);

        loginDao.register(memberDto);

        return "redirect:/";
    }

    @GetMapping("/login/loginForm")
    public String loginForm() {
        return "login/loginForm";
    }

    @GetMapping("/")
    public String main(Model model, HttpSession session, HttpServletRequest request) {
        // 현재 로그인한 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 이름 또는 기타 정보 가져오기
        String username = authentication.getName();

        UserCommand user = userService.user(Integer.parseInt(username));
        List<Dept_board> list1 = deptBoardDao.dbpard1(user.getDept_no());
        List<Dept_board> list2 = deptBoardDao.dbpard0(user.getDept_no());
        for(Dept_board d : list1){
            String date = d.getUpdatedate().substring(0,11);
            d.setUpdatedate(date);
        }
        for(Dept_board d : list2){
            String date = d.getUpdatedate().substring(0,11);
            d.setUpdatedate(date);
        }

        Long mem_no = Long.parseLong(username);
        Integer dept_no = loginDao.getDeptno(mem_no);

        session.setAttribute("deptno", dept_no);

        Integer sessionDeptNo = (Integer) session.getAttribute("deptno");
        System.out.println("sessionDeptNo==================="+sessionDeptNo);


        Boolean showChangePasswordPopup = (Boolean) session.getAttribute("showChangePasswordPopup");
        if (showChangePasswordPopup == null || !showChangePasswordPopup) {
            showChangePasswordPopup = false;
        }

        model.addAttribute("showChangePasswordPopup", showChangePasswordPopup);
        model.addAttribute("username", username);
        model.addAttribute("list1",list1);
        model.addAttribute("list2",list2);
//        model.addAttribute("deptno", dept_no);

        return "index";
    }

    @GetMapping("/login/changePassword")
    public String showChangePasswordForm(Model model, HttpSession session) {

        boolean showChangePasswordPopup = session.getAttribute("showChangePasswordPopup") != null && (boolean) session.getAttribute("showChangePasswordPopup");

        model.addAttribute("showChangePasswordPopup", showChangePasswordPopup);
        return "login/changePassword";
    }

    @PostMapping("/login/changePassword")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestParam String newPassword,
                                              @RequestParam String confirmPassword,
                                              Authentication authentication, HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("errorMessage", "로그인되어 있지 않습니다.");
            return response;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        if (!newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("errorMessage", "비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return response;
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        MemberDto memberToUpdate = new MemberDto();
        memberToUpdate.setMem_no(Long.parseLong(username));
        memberToUpdate.setMem_pw(encryptedPassword);
        memberToUpdate.set_initial_login(false);

        loginDao.updatePassword(memberToUpdate.getMem_no(),
                memberToUpdate.getMem_pw(),
                memberToUpdate.is_initial_login());

        session.setAttribute("showChangePasswordPopup", false);

        response.put("success", true);
        response.put("redirectUrl", "/");  // 클라이언트에게 리다이렉트할 URL 전송

        return response;
    }

    @GetMapping("/login/resetPassword")
    public String resetGet(){

        return "/login/resetPassword";
    }

    @PostMapping("/login/resetPassword")
    public String resetPost(
            @RequestParam String mem_no,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        String errorMessage = resetPassword.resetPassword(Long.valueOf(mem_no), newPassword, confirmPassword);

        if (errorMessage == null) {
            return "/login/closeWindow";
        } else {
            model.addAttribute("errorMessage", errorMessage);
            return "/login/resetPassword";
        }
    }

    @PostMapping("/checkMemNoValidity")
    @ResponseBody
    public Map<String, Boolean> checkMemNoValidity(@RequestParam String mem_no) {
        Map<String, Boolean> response = new HashMap<>();
        // DB에서 사번 유효성 검사 수행
        MemberDto memberDto = loginDao.getLoginId(Long.valueOf(mem_no));
        response.put("isValid", memberDto != null);
        return response;
    }

    @GetMapping("/getSessionDeptno")
    public ResponseEntity<Integer> getDeptno(HttpSession session) {
        Integer deptno = (Integer) session.getAttribute("deptno");
        System.out.println("deptno==============" + deptno);
        return ResponseEntity.ok(deptno);
    }

}