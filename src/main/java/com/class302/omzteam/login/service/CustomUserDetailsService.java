package com.class302.omzteam.login.service;

import com.class302.omzteam.mybatis.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.class302.omzteam.member.model.MemberDto;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("사용자 아이디를 입력하세요.");
        }

        MemberDto member = loginDao.getLoginId(Long.parseLong(username));
        System.out.println(member);

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        return new CustomUserDetails(
                member.getMem_no().toString(),
                member.getMem_pw(),
                member.is_initial_login,
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}
