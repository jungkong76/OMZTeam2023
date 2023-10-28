package com.class302.omzteam.config;

import com.class302.omzteam.login.service.CustomLoginSuccessHandler;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log
@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login/joinForm", "/login/loginForm").permitAll()
                .anyRequest().authenticated() // 다른 요청은 인증 필요
                .and()
                .formLogin()
                .loginPage("/login/loginForm") // 로그인 페이지 URL 지정
                .failureUrl("/login/loginForm?error=true")
                .successHandler(new CustomLoginSuccessHandler())

                .loginProcessingUrl("/login/loginForm")


                .permitAll()

                .and()
                .logout()
                .logoutSuccessUrl("/login/loginForm") // 로그아웃 시 이동할 URL
                .permitAll();

        // 사용자 인증을 위한 UserDetailsService 설정
        http.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


}

