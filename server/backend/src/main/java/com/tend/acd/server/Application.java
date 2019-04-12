package com.tend.acd.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import java.util.Collections;

@SpringBootApplication
@EnableCaching
@SuppressWarnings("Duplicates")
public class Application {
    @Value("${postgreSQL.connection}")
    String DBConnectionString;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    @Bean
    ServletRegistrationBean getServletRegistration() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new LinkFuture.DB.Servlet.GenericDBServlet());
        srb.setUrlMappings(Collections.singletonList("/api/db/*"));
        Util.logger.trace("init DB generic db =>" + DBConnectionString);
        return srb;
    }
}
