package com.tend.acd.repository;

import Image_Recognition.Class1;
import LinkFuture.DB.Utility;
import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Created by Cyokin
 * on 7/9/2016.
 */
@Repository
public class ImageRecognitionRepository {
    public List<ResponseImageRecognitionEntity> recognition(String imageUrl) throws MWException, IOException {
        Util.logger.trace("recognizing " + imageUrl);
        Class1 rec = new Class1();
        Object[] result =  rec.Image_Recognition(1,imageUrl);
        rec.dispose();
        Util.logger.trace("got result " + result[0].toString());
        return Utility.fromJson(result[0].toString(),List.class,ResponseImageRecognitionEntity.class);
    }


    public String testJar(Integer input) throws MWException {
        Util.logger.trace("makesqr " + input);
        makesqr.Class1 rec = new makesqr.Class1();
        Object[] result =  rec.makesqr(1,input);
        rec.dispose();
        Util.logger.trace("got result " + result[0].toString());
        return result[0].toString();
    }
}
