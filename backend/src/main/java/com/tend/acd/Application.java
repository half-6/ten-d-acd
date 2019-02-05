package com.tend.acd;

import Image_Recognition.Class1;
import com.mathworks.toolbox.javabuilder.MWException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class Application {

  @Value("${postgreSQL.connection}")
  String DBConnectionString;

	public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    Properties properties = new Properties();
    properties.setProperty("spring.resources.static-locations","file:///" + Util.getAppStaticPath() + ",classpath:public");
    app.setDefaultProperties(properties);
    app.run(args);
	}
  @Bean
	ServletRegistrationBean getServletRegistration(){
    ServletRegistrationBean srb = new ServletRegistrationBean();
    srb.setServlet(new LinkFuture.DB.Servlet.GenericDBServlet());
    srb.setUrlMappings(Arrays.asList("/api/db/*"));
    HashMap<String,String> initParameters = new HashMap<>();
    //initParameters.put("DBConnectionString","java:/comp/env/jdbc/PostgreSQLDB");
    initParameters.put("DBConnectionString",DBConnectionString);
    srb.setInitParameters(initParameters);
    return srb;
  }
  @Bean("imageService")
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public Class1 getImageService() throws MWException {
    Class1 imageService =  new Class1();
    Util.logger.trace("init image service success");
    return imageService;
  }
}
