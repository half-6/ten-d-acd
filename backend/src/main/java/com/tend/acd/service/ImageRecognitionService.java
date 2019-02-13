package com.tend.acd.service;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.ImageRecognitionRepository;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class ImageRecognitionService {

  @Autowired
  ImageRecognitionRepository imageRecognitionRepository;

  public ResponseImageRecognitionEntity recognize(String base64Image)
      throws IOException, MWException {
    base64Image = Util.getBase64FromImage(base64Image);
    ResponseImageRecognitionEntity output =  imageRecognitionRepository.recognition(base64Image);
    return output;
  }
}
