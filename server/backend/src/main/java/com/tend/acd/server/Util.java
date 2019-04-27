package com.tend.acd.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Cyokin
 * on 4/22/2016.
 */
public class Util {
    public final static org.slf4j.Logger logger = LoggerFactory.getLogger("com.tend.acd.backend");
    public static String toJson(Object user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.writeValueAsString(user);
    }
    public static <T> T fromJson(String jsonString, Class<T> parametrizedClass,Class<?>... parameterClasses) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if(parameterClasses.length ==0)
        {
            return mapper.readValue(jsonString,parametrizedClass);
        }
        else
        {
            ArrayList<Class<?>> subClass = new ArrayList<>();
            subClass.add(parametrizedClass);
            subClass.addAll(Arrays.asList(parameterClasses));
            JavaType parameterType=null;
            for (int i = subClass.size()-1;i>=0;i--)
            {
                if(parameterType==null)
                {
                    parameterType = mapper.getTypeFactory().constructParametricType(subClass.get(i-1),subClass.get(i));
                    i--;
                }
                else
                {
                    parameterType = mapper.getTypeFactory().constructParametricType(subClass.get(i) ,parameterType);
                }
            }
            return mapper.readValue(jsonString,parameterType);
        }
    }
    public static String getAppPath(){
        ApplicationHome home = new ApplicationHome(Application.class);
        return home.getDir().getAbsolutePath();
    }

    static Path getAppStaticPath(){
        return Paths.get(getAppPath(),"static");
    }
}
