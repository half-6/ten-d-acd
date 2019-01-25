package com.tend.acd.service;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.Application;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.ImageRecognitionRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

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

  public List<ResponseImageRecognitionEntity> recognize(String base64Image)
      throws IOException, MWException {
    String imageString = base64Image.substring(base64Image.indexOf("base64,") + "base64,".length());
    byte[] imageByte= Base64.decodeBase64(imageString);
    BufferedImage image  =  ImageIO.read(new ByteArrayInputStream(imageByte));
    if(image==null)
    {
      throw new IllegalArgumentException("invalid image");
    }
    Path imgPath = getFilePath(".png");
    File f = new File(imgPath.toString());
    ImageIO.read(new ByteArrayInputStream(imageByte));
    ImageIO.write(image,"png",f);
    return imageRecognitionRepository.recognition(imgPath.toString());
  }

  private Path getFilePath(String fileExtension){
    ApplicationHome home = new ApplicationHome(Application.class);
    String realPathtoUploads = Paths.get(home.getDir().getAbsolutePath(),storageDir).toString();// context.getRealPath(storageDir);
    if(! new File(realPathtoUploads).exists())
    {
      new File(realPathtoUploads).mkdir();
    }
    return Paths.get(realPathtoUploads, UUID.randomUUID() + fileExtension);
  }
}
