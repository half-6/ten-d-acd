package com.tend.acd.repository;

import Image_Recognition.Class1;
import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import org.springframework.stereotype.Repository;

/**
 * Created by Cyokin
 * on 7/9/2016.
 */
@Repository
public class ImageRecognitionRepository {
    public String recognition(String imageUrl) throws MWException {
        Util.logger.trace("recognizing " + imageUrl);
        Class1 rec = new Class1();
        Object[] result =  rec.Image_Recognition(1,imageUrl);
        rec.dispose();
        Util.logger.trace("got result " + result[0].toString());
        return result[0].toString();
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
