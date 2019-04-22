package com.tend.acd.server.repository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Module Name: JwtRepository
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@SuppressWarnings("unused")
@Component
public class JwtRepository {

    @Value("${jwt.key}")
    private String JWTKey;

    @Value("${jwt.issuer}")
    private String JWTIssuer;

    @Value("${jwt.subject}")
    private String JWTSubject;

    @Value("${jwt.expire.min}")
    private int JWTExpireMin;

    private final static String CLAIM_KEY = "user";

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(JWTKey);
    }
    private Date getExpireDate(Date now){
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MINUTE, JWTExpireMin);
        return c.getTime();
    }

    public String encode(UserEntity user) throws JsonProcessingException {
        if(JWTExpireMin > 0)
        {
            Date now = new Date();
            Date expired = getExpireDate(now);
            return JWT.create()
                .withIssuer(JWTIssuer)
                .withClaim(CLAIM_KEY, Util.toJson(user))
                .withExpiresAt(expired)
                .withSubject(JWTSubject)
                .withIssuedAt(now)
                .sign(getAlgorithm());
        }
        else
        {
            return JWT.create()
                .withIssuer(JWTIssuer)
                .withClaim(CLAIM_KEY,Util.toJson(user))
                .sign(getAlgorithm());
        }
    }

    public UserEntity decode(String jwtToken) throws IOException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
            .withIssuer(JWTIssuer)
            .build();
        DecodedJWT jwt = verifier.verify(jwtToken);
        String jwtString = jwt.getClaim(CLAIM_KEY).asString();
        return Util.fromJson(jwtString, UserEntity.class);
    }
    public DecodedJWT verify(String jwtToken) {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(JWTIssuer)
                .build();
        return verifier.verify(jwtToken);
    }
}
