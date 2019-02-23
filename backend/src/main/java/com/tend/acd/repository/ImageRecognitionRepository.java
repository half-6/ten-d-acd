package com.tend.acd.repository;

import Cancer_Img_Recognition.Class1;
import LinkFuture.DB.Utility;
import Test_Image_Conversion.Test_Image_ConversionMCRFactory;
import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import java.io.IOException;
import javax.annotation.Resource;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

/**
 * Created by Cyokin
 * on 7/9/2016.
 */
@Repository
public class ImageRecognitionRepository {

    @Resource(name = "imageService")
    Class1 imageService;

    public ResponseImageRecognitionEntity recognition(String base64ImageString,String cancerType) throws MWException, IOException {
        Util.logger.trace("recognizing img for " + cancerType);
        Object[] result =  imageService.Cancer_Img_Recognition(1,base64ImageString,"1",cancerType);
        Util.logger.trace("got result " + result[0].toString());
        //[{"Prediction":"velvet","ProcessingTime":1.1388072999999963,"Probability":0.33457765}]
        JSONArray json = new JSONArray(result[0].toString());
        return Utility.fromJson(json.getJSONObject(0).toString(),ResponseImageRecognitionEntity.class);
    }


    public String testJar(String base64String,String filePath) throws MWException {
        Util.logger.trace("base64String " + base64String);
        Util.logger.trace("filePath " + filePath);
        Test_Image_Conversion.Class1 rec = new Test_Image_Conversion.Class1();
        Object[] result =  rec.Test_Image_Conversion(1,base64String,filePath);
        rec.dispose();
        Util.logger.trace("got result " + result[0].toString());
        return result[0].toString();
    }
}
