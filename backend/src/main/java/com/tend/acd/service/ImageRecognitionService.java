package com.tend.acd.service;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.ImageRecognitionRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class ImageRecognitionService {

  @Autowired
  ImageRecognitionRepository imageRecognitionRepository;

  @Value("${storage.dir}")
  private String storageDir;

  @Autowired
  ServletContext context;

  public ResponseImageRecognitionEntity recognize(MultipartFile image)
      throws IOException, MWException {
    String realPathtoUploads = context.getRealPath(storageDir);
    if(! new File(realPathtoUploads).exists())
    {
      new File(realPathtoUploads).mkdir();
    }
    Path imgPath = Paths.get(realPathtoUploads,image.getOriginalFilename());
    image.transferTo(imgPath);
    Util.logger.trace("Saved file to " + imgPath);
    imageRecognitionRepository.recognition(imgPath.toString());
    return null;
  }

}
