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
    public boolean isUserInRole(String role) {
        if (loginUser == null) {
            return super.isUserInRole(role);
        }
        return loginUser.roles.contains(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return loginUser;
    }
}
