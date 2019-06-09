package com.tend.acd;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.cancer.Recognition;
import com.tend.acd.model.response.ConfigEntity;
import com.tend.acd.security.SecurityFilter;
import com.tend.acd.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings("Duplicates")
@SpringBootApplication
@EnableCaching
public class Application {

    @Value("${spring.datasource.url}?user=${spring.datasource.username}&password=${spring.datasource.password}")
    String DBConnectionString;

    @Autowired
    ConfigEntity configEntity;

    @Autowired
    DataSource dataSource;

    @Autowired
    SecurityService securityService;

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
        //initParameters.put("DBConnectionString","java:/comp/env/jdbc/PostgreSQLDB");
        initParameters.put("DBConnectionString", DBConnectionString);
        Util.logger.trace("register generic db servlet =>" + DBConnectionString);
        srb.setInitParameters(initParameters);
        return srb;
    }

    @Bean
    public GenericDBHelper dbHelper() throws SQLException, NamingException, IOException, ClassNotFoundException {
        Util.logger.trace("init GenericDBHelper");
        return new GenericDBHelper(this.dataSource.getConnection());
    }

    @Bean("imageService")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Recognition getImageService() {
        try{
            Recognition recognition = new Recognition();
            configEntity.recognitionVersion = recognition.recognitionVersion;
            configEntity.certificateEntity = securityService.readCertificate();
            return recognition;
        }
        catch (Exception e){
            String errMsg = "load image recognition jar failed with " + e.getMessage();
            Util.logger.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }

    @Bean
    public FilterRegistrationBean getSecurityRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SecurityFilter(configEntity));
        registration.addUrlPatterns("/api/*");
        Util.logger.trace("register security servlet");
        return registration;
    }
}
