package com.tend.acd.server.controller;

import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.model.response.ResponseBaseEntity;
import com.tend.acd.server.repository.JwtRepository;
import com.tend.acd.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtRepository jwtRepository;

    private final static String KEY_COOKIE="LF_AUTH";

    @PostMapping("login")
    public ResponseBaseEntity<String> login(@RequestBody UserEntity input, HttpServletResponse response, HttpServletRequest request) throws Exception {
        UserEntity loginUser = userService.login(input);
        Cookie auth = new Cookie(KEY_COOKIE,loginUser.token);
        auth.setMaxAge(1000000);
        response.addCookie(auth);
        return new ResponseBaseEntity<>(loginUser.token);
    }

    @PostMapping("info")
    public ResponseBaseEntity<UserEntity> info(@CookieValue(KEY_COOKIE) String token) throws IOException {
        UserEntity mocker = jwtRepository.decode(token);
        return new ResponseBaseEntity<>(mocker);
    }

    @PostMapping("logout")
    public ResponseBaseEntity<String> logout(HttpServletResponse response) {
        response.addCookie(new Cookie(KEY_COOKIE,null ));
        return new ResponseBaseEntity<>("success");
    }
}
