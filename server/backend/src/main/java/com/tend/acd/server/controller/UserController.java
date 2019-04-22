package com.tend.acd.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.model.response.ResponseBaseEntity;
import com.tend.acd.server.repository.JwtRepository;
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
    JwtRepository jwtRepository;

    private final static String KEY_COOKIE="LF_AUTH";

    @PostMapping("login")
    public ResponseBaseEntity<String> login(@RequestBody UserEntity input, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        input.token = jwtRepository.encode(input);
        Cookie auth = new Cookie(KEY_COOKIE,input.token);
        auth.setMaxAge(1000000);
        response.addCookie(auth);
        return new ResponseBaseEntity<>(input.token);
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
