package com.class302.omzteam.login.service;

import com.class302.omzteam.member.model.MemberDto;
import com.class302.omzteam.mybatis.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String resetPassword(Long mem_no, String newPassword, String confirmPassword) {
        MemberDto memberDto = loginDao.getLoginId(mem_no);


        if (memberDto == null) {
            return "입력한 사번이 존재하지 않습니다.";
        }


        if (!newPassword.equals(confirmPassword)) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // 비밀번호 변경
        String encodedPassword = passwordEncoder.encode(newPassword);
        loginDao.resetPassword(mem_no, encodedPassword);

        return null;  // 성공
    }
}
