package com.tend.acd;

import Thyroid_Img_Recognition.Class1;
import com.mathworks.toolbox.javabuilder.MWException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ResourceUtils;

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
  public Class1 getImageService() throws MWException, IOException {
    Class1 imageService =  new Class1();
    Object[] result =  imageService.Thyroid_Img_Recognition(1,"","0");
    Util.logger.trace("image service warming up success =>" + result[0].toString());
    String base64String = Util.getBase64String("/init.jpg");
    result = imageService.Thyroid_Img_Recognition(1,base64String,"1");
    Util.logger.trace("image service first call success =>" + result[0].toString());
    Util.logger.trace("init image service success");
    return imageService;
  }
}
