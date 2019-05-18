package com.tend.acd.server.service;

import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.repository.JwtRepository;
import com.tend.acd.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    JwtRepository jwtRepository;

    @Autowired
    UserRepository userRepository;

    public UserEntity login(UserEntity userEntity) throws Exception {
        UserEntity loginUser = userRepository.login(userEntity.username,userEntity.password);
        if(loginUser!=null){
            loginUser.token = jwtRepository.encode(loginUser);
            return loginUser;
        }
        throw new SecurityException("Invalid user name or password");
    }
}
