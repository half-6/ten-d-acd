package com.tend.acd.server.security;

import com.tend.acd.server.Util;
import com.tend.acd.server.repository.JwtRepository;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SecurityFilter implements Filter {

    private List<String> WriteList = Arrays.asList(
            "/api/user/login"
            ,"/api/user/logout"
            ,"/api/content/config.js"
            ,"/api/db/public.v_machine_type"
            ,"/api/db/public.v_cancer_type"
            ,"/api/db/public.v_hospital"
            ,"/api/db/public.v_ai_version"
    );
    private JwtRepository jwtRepository;
    public SecurityFilter(JwtRepository jwtRepository){
        this.jwtRepository = jwtRepository;
    }

    private String readToken(HttpServletRequest request){
        String token = request.getHeader("LF_AUTH");
        if(token!=null)
        {
            return token;
        }
        if(request.getCookies()!=null)
        {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("LF_AUTH"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =  (HttpServletRequest)servletRequest;
        HttpServletResponse response =  (HttpServletResponse)servletResponse;
        try {
            String token = readToken(request);
            jwtRepository.verify(token);
            filterChain.doFilter(servletRequest,servletResponse);
        }
        catch (Exception e){
            if(WriteList.contains(request.getRequestURI()))
            {
                filterChain.doFilter(servletRequest,servletResponse);
            }
            else
            {
                Util.logger.error("invalid token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid token");
                //throw new SecurityException("Unauthorized");
            }

        }

    }
}
