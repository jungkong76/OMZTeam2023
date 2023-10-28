package com.class302.omzteam.login.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final boolean isInitialLogin;

    public CustomUserDetails(String username, String password, boolean isInitialLogin, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.isInitialLogin = isInitialLogin;
    }

    public boolean isInitialLogin() {
        return isInitialLogin;
    }
}



