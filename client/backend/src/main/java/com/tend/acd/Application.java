package com.tend.acd;

import com.tend.acd.cancer.Recognition;
import com.tend.acd.model.response.ConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings("Duplicates")
@SpringBootApplication
@EnableCaching
public class Application {

    @Value("${postgreSQL.connection}")
    String DBConnectionString;

    @Autowired
    ConfigEntity configEntity;

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
        Util.logger.trace("init DB connection =>" + DBConnectionString);
        srb.setInitParameters(initParameters);
        return srb;
    }

    @Bean("imageService")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Recognition getImageService() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Recognition recognition = new Recognition();
        configEntity.recognitionVersion = recognition.recognitionVersion;
        return recognition;
    }
}
