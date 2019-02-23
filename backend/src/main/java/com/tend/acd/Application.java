package com.tend.acd;

import Cancer_Img_Recognition.Class1;
import com.mathworks.toolbox.javabuilder.MWException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

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
        srb.setUrlMappings(Arrays.asList("/api/db/*"));
        HashMap<String, String> initParameters = new HashMap<>();
        //initParameters.put("DBConnectionString","java:/comp/env/jdbc/PostgreSQLDB");
        initParameters.put("DBConnectionString", DBConnectionString);
        srb.setInitParameters(initParameters);
        return srb;
    }

    @Bean("imageService")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Class1 getImageService() throws MWException, IOException {
        Util.logger.trace("start init image detection service");
        Class1 imageService = new Class1();
        Object[] result = imageService.Cancer_Img_Recognition(1, "", "0", "BR");
        configEntity.recognitionVersion = result[0].toString();
        Util.logger.trace("image service warming up success =>" + result[0].toString());
        String base64String = Util.getBase64String("init.jpg");
        result = imageService.Cancer_Img_Recognition(1, base64String, "1", "BR");
        Util.logger.trace("image service first call success =>" + result[0].toString());
        Util.logger.trace("init image detection service success");
        return imageService;
    }
}
