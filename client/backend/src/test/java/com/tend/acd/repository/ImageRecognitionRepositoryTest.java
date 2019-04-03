package com.tend.acd.repository;

import LinkFuture.DB.Utility;
import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.ApplicationTests;
import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

/**
 * Module Name: ImageRecognitionRepositoryTest Project Name: com.feifanuniv.search.api Created by
 * Cyokin on 1/15/2019
 */
public class ImageRecognitionRepositoryTest extends ApplicationTests {

  @Autowired
  ImageRecognitionRepository imageRecognitionRepository;

  @Test
  public void recognition() throws IOException, MWException, InvocationTargetException, IllegalAccessException {
    String base64String = Util.getBase64String("B39.jpg");
    ResponseImageRecognitionEntity test = imageRecognitionRepository.recognition(base64String,"TH");
    Util.logger.trace("recognizing " + Utility.toJson(test));
  }
  @Test
  public void testJarPerformance() throws IOException, MWException, InvocationTargetException, IllegalAccessException {
    System.out.println("start time");
    Integer i = 5;
    long total = 0;
    while (i>0)
    {
      long startTime = System.nanoTime();
      this.recognition();
      long endTime = System.nanoTime();
      long duration = (endTime - startTime) / 1000000 / 1000;  //divide by 1000000 to get milliseconds.
      total = total + duration;
      System.out.println(duration);
      i--;
    }
    System.out.println("total:" + total);
  }
}