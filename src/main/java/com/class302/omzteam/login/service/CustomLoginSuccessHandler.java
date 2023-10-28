package com.class302.omzteam.login.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


            if (userDetails.isInitialLogin()) {
                request.getSession().setAttribute("showChangePasswordPopup", true);
                getRedirectStrategy().sendRedirect(request, response, "/");
                return;
            } else {
                getRedirectStrategy().sendRedirect(request, response, "/"); // main 페이지로 리디렉트
                return;
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}


