package com.tend.acd.server;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.server.repository.JwtRepository;
import com.tend.acd.server.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@SpringBootApplication
@EnableCaching
@SuppressWarnings("Duplicates")
public class Application {

    @Value("${spring.datasource.url}?user=${spring.datasource.username}&password=${spring.datasource.password}")
    String DBConnectionString;

    @Autowired
    JwtRepository jwtRepository;

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        Properties properties = new Properties();
        properties.setProperty("spring.resources.static-locations", "file:///" + Util.getAppStaticPath() + ",classpath:public");
        app.setDefaultProperties(properties);
        app.run(args);

    }

    @Bean
    ServletRegistrationBean getServletRegistration() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new LinkFuture.DB.Servlet.GenericDBServlet());
        srb.setUrlMappings(Collections.singletonList("/api/db/*"));
        HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("DBConnectionString", DBConnectionString);
        initParameters.put("enableDelete", "false");
        //initParameters.put("enableInsert", "false");
        //initParameters.put("enableUpdate", "false");
        Util.logger.trace("init DB API =>" + DBConnectionString);
        srb.setInitParameters(initParameters);
        return srb;
    }

    @Bean
    public GenericDBHelper dbHelper() throws SQLException, NamingException, IOException, ClassNotFoundException {
        Util.logger.trace("init GenericDBHelper");
        return new GenericDBHelper(this.dataSource.getConnection());
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SecurityFilter(jwtRepository));
        registration.addUrlPatterns("/api/*");
        return registration;
    }
}
