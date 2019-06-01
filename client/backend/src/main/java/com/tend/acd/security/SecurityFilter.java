package com.tend.acd.security;

import com.tend.acd.Util;
import com.tend.acd.model.response.ConfigEntity;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SecurityFilter implements Filter {
    private List<String> WriteList = Arrays.asList(
            "/api/content/config.js"
            ,"/api/db/public.v_machine_type"
            ,"/api/db/public.v_cancer_type"
            ,"/api/db/public.v_hospital"
            ,"/api/db/public.v_ai_version"
    );

    private ConfigEntity configEntity;
    public SecurityFilter(ConfigEntity configEntity){
        this.configEntity = configEntity;
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =  (HttpServletRequest)servletRequest;
        HttpServletResponse response =  (HttpServletResponse)servletResponse;
        Date now = new Date();
        if(WriteList.contains(request.getRequestURI()) || configEntity.certificateEntity.expiredDate.after(now))
        {
            filterChain.doFilter(servletRequest,servletResponse);
        }
        else {
            Util.logger.error("invalid certificate");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid certificate");
        }

    }
}
