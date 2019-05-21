package com.tend.acd.server.controller;

import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.model.response.ResponseBaseEntity;
import com.tend.acd.server.repository.JwtRepository;
import com.tend.acd.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtRepository jwtRepository;

    private final static String KEY_COOKIE="LF_AUTH";

    @PostMapping("login")
    public ResponseBaseEntity<String> login(@RequestBody UserEntity input, HttpServletResponse response) throws Exception {
        UserEntity loginUser = userService.login(input);
        return new ResponseBaseEntity<>(loginUser.token);
    }

    @PostMapping("info")
    public ResponseBaseEntity<UserEntity> info(HttpServletRequest request) {
        UserEntity mocker = (UserEntity)request.getUserPrincipal();// jwtRepository.decode(token);
        return new ResponseBaseEntity<>(mocker);
    }

    @PostMapping("logout")
    public ResponseBaseEntity<String> logout(HttpServletResponse response) {
        response.addCookie(new Cookie(KEY_COOKIE,null ));
        return new ResponseBaseEntity<>("success");
    }
}
