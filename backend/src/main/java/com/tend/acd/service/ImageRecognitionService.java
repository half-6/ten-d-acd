package com.tend.acd.service;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.ImageRecognitionRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class ImageRecognitionService {

  @Autowired
  ImageRecognitionRepository imageRecognitionRepository;

  public List<ResponseImageRecognitionEntity> recognize(String base64Image)
      throws IOException, MWException {
    File img = Util.saveBase64Image(base64Image);
    return imageRecognitionRepository.recognition(img.getAbsolutePath());
  }
}
