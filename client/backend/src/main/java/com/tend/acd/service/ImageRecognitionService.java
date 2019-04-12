package com.tend.acd.service;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.DBRepository;
import com.tend.acd.repository.ImageRecognitionRepository;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class ImageRecognitionService {

  @Autowired
  ImageRecognitionRepository imageRecognitionRepository;

  @Autowired
  DBRepository dbRepository;

  public ResponseImageRecognitionEntity recognize(JSONObject input)
          throws Exception {
    String base64Image = Util.getBase64FromImage(input.getString("roi_image_src"));
    String cancerType = input.getString("cancer_type");
    ResponseImageRecognitionEntity output =  imageRecognitionRepository.recognition(base64Image,cancerType);
    input.put("prediction",output.prediction);
    input.put("probability",output.probability);
    input.put("processing_time",output.processingTime);
    dbRepository.saveHistory(input);
    return output;
  }
}
