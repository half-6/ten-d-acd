package com.tend.acd.server.repository;

import LinkFuture.DB.DBHelper.DBRepository;
import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.server.model.request.UserEntity;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Module Name: JwtRepository
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@SuppressWarnings("unused")
@Repository
public class UserRepository extends DBRepository<UserEntity,Integer> {

    @Resource
    GenericDBHelper dbHelper;

    public UserRepository() {
        super("public.user");
    }

    public UserEntity login(String username,String password) throws Exception {
        JSONObject where = new JSONObject();
        where.put("username",username);
        where.put("status","active");
        UserEntity user = this.findOne(where);
        if(BCrypt.checkpw(password,user.password))
        {
            return user;
        }
        return null;
    }

}
