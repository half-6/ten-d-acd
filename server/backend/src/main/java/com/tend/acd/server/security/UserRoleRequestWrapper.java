package com.tend.acd.server.security;

import com.tend.acd.server.model.request.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class UserRoleRequestWrapper extends HttpServletRequestWrapper {
    UserEntity loginUser;
    public UserRoleRequestWrapper(HttpServletRequest request, UserEntity loginUser) {
        super(request);
        this.loginUser = loginUser;
    }

    @Override
    public Principal getUserPrincipal() {
        if(loginUser!=null)
        {
            Principal a = () -> loginUser.username;
            return a;
        }
        return null;
    }
}
