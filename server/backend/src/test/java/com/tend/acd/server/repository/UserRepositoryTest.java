package com.tend.acd.server.repository;

import LinkFuture.DB.DBHelper.DBHelper;
import LinkFuture.DB.DBHelper.Model.CommandTypeInfo;
import com.tend.acd.server.ApplicationTests;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.UserEntity;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest extends ApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DataSource  dataSource;

    @Test
    public void login() throws Exception {
        UserEntity user = userRepository.login("admin","tend-qazwsx123");
        assertEquals(user.username,"admin");
        Util.logger.trace(Util.toJson(user));
    }
    @Test
    public void loginFailed() throws Exception {
        UserEntity user = userRepository.login("admin","tend-qazwsx1231");
        assertEquals(user,null);
    }
    @Test
    public void createPassword() throws Exception {
        String password = BCrypt.hashpw("tend-qazwsx123",BCrypt.gensalt());
        Util.logger.trace(password);
    }
    @Test
    public void insertObject() throws Exception {
        String password = BCrypt.hashpw("cyokin-qazwsx123",BCrypt.gensalt());
        JSONObject obj = new JSONObject();
        obj.put("username","cyokin");
        obj.put("password",password);
        obj.put("display_name","cyokin");
        obj.put("roles", Arrays.asList("ABC"));
        int userId = userRepository.insert(obj);
        Util.logger.trace(Integer.toString(userId));
        assertTrue(userId>0);
    }
    @Test
    public void insertT() throws Exception {
        String password = BCrypt.hashpw("cyokin-qazwsx123",BCrypt.gensalt());
        UserEntity user = new UserEntity();
        user.username = "cyokin";
        user.password = password;
        user.displayName = "cyokin";
        user.roles = Arrays.asList("ABC");
        int userId = userRepository.insert(user);
        Util.logger.trace(Integer.toString(userId));
        assertTrue(userId>0);
    }
    @Test
    public void updateObject() throws Exception {
        String displayName = "cyokin zhang";
        JSONObject obj = new JSONObject();
        obj.put("display_name",displayName);
        JSONObject where = new JSONObject();
        where.put("username","cyokin");
        int success = userRepository.update(obj,where);
        Util.logger.trace(Integer.toString(success));
        assertTrue(success==1);
        assertEquals(userRepository.findOne(where).displayName,displayName);
    }
    @Test
    public void updateT() throws Exception {
        UserEntity user = new UserEntity();
        user.displayName = "cyokin zhang";
        UserEntity where = new UserEntity();
        where.username = "cyokin";
        int success = userRepository.update(user,where);
        Util.logger.trace(Integer.toString(success));
        assertTrue(success==1);
        assertEquals(userRepository.findOne(where).displayName,user.displayName);
    }
    @Test
    public void deleteObject() throws Exception {
        JSONObject where = new JSONObject();
        where.put("username","cyokin");
        int success = userRepository.delete(where);
        Util.logger.trace(Integer.toString(success));
        assertTrue(success==1);
        assertEquals(userRepository.findOne(where),null);
    }
    @Test
    public void deleteT() throws Exception {
        UserEntity where = new UserEntity();
        where.username = "cyokin";
        int success = userRepository.delete(where);
        Util.logger.trace(Integer.toString(success));
        assertTrue(success==1);
        assertEquals(userRepository.findOne(where),null);
    }

    @Test
    public void allObject() throws Exception {
        insertObject();
        updateObject();
        deleteObject();
    }
    @Test
    public void allT() throws Exception {
        insertT();
        updateT();
        deleteT();
    }
}