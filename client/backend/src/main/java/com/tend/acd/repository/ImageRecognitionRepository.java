package com.tend.acd.repository;

import LinkFuture.DB.Utility;
import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.cancer.Recognition;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Cyokin
 * on 7/9/2016.
 */
@Repository
public class ImageRecognitionRepository {

    @Resource(name = "imageService")
    Recognition imageService;

    public ResponseImageRecognitionEntity recognition(String base64ImageString,String cancerType) throws IOException, InvocationTargetException, IllegalAccessException {
        return imageService.recognition(base64ImageString,cancerType);
    }
}
