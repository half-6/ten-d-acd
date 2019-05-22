package com.tend.acd.server.security;

import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.repository.JwtRepository;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
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
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization!=null && authorization.startsWith("Bearer"))
        {
            return authorization.substring("Bearer".length()).trim();
        }
        return request.getParameter("t");
//        if(request.getCookies()!=null)
//        {
//            return Arrays.stream(request.getCookies())
//                    .filter(c -> c.getName().equals("LF_AUTH"))
//                    .findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);
//        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =  (HttpServletRequest)servletRequest;
        HttpServletResponse response =  (HttpServletResponse)servletResponse;
        try {
            String token = readToken(request);
            UserEntity user = jwtRepository.decode(token);
            filterChain.doFilter(new UserRoleRequestWrapper(request,user),servletResponse);
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
            }

        }

    }
}
