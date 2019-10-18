package com.tend.acd.cancer;

import LinkFuture.DB.Utility;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Recognition {

    private static String imageRecognitionPath = System.getenv("image.recognition.path");
    private Object recognitionInstance;
    private Method recognitionMethod;
    public String recognitionVersion;

    public Recognition() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        //Path filePath = imageRecognitionPath!=null?Paths.get(imageRecognitionPath):Paths.get(Util.getAppPath(),"lib","Image_Recognition-0.0.6.jar");
        Path filePath = imageRecognitionPath!=null?Paths.get(imageRecognitionPath):Paths.get(Util.getAppPath(),"lib","Image_Recognition.jar");
        Util.logger.trace("loading external Matlab JAR =>" + filePath.toString());
        if(!Files.exists(filePath))
        {
            String errorMessage = "Specific jar not exist," + filePath.toString();
            Util.logger.error(errorMessage);
            throw new Error(errorMessage);
        }
        URL url = filePath.toUri().toURL();
        URLClassLoader jar = new URLClassLoader(
                new URL[] {url},
                this.getClass().getClassLoader()
        );
        Class imageService = Class.forName("Cancer_Img_Recognition.Class1", true, jar);
        recognitionInstance = imageService.newInstance();
        recognitionMethod =  imageService.getDeclaredMethod("Cancer_Img_Recognition",int.class,Object[].class);
        Object[] result = (Object[])recognitionMethod.invoke(recognitionInstance,1, new Object[]{"","0"});
        recognitionVersion = result[0].toString();
        Util.logger.trace("image service warming up success =>" + recognitionVersion);
//        String base64String = Util.getBase64String("init.jpg");
//        recognition(base64String,"TH","[[0,0];[0,10];[10,9];[10,0];[10,10]]", "[[0,0];[0,10];[10,0];[10,10]]");
//        Util.logger.trace("image service first call success");
    }

    public ResponseImageRecognitionEntity recognition(String base64String,String cancerType,String roi_coordinates,String roi_corners) throws InvocationTargetException, IllegalAccessException, IOException {
        Util.logger.trace("recognizing img for " + cancerType);
        //Object[] result = (Object[]) recognitionMethod.invoke(recognitionInstance,1,new Object[]{base64String, "1", "TH"});
        Object[] result = (Object[]) recognitionMethod.invoke(recognitionInstance,1,new Object[]{base64String, "1", "TH",roi_coordinates,roi_corners });
        Util.logger.trace("got result " + result[0].toString());
        JSONArray json = new JSONArray(result[0].toString());
        return Utility.fromJson(json.getJSONObject(0).toString(),ResponseImageRecognitionEntity.class);
    }
}
