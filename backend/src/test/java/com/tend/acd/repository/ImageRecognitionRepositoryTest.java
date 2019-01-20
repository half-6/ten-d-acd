package com.tend.acd.repository;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.ApplicationTests;
import java.io.File;
import java.io.IOException;
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
  public void recognition() throws IOException, MWException {
    File testImage = ResourceUtils.getFile(this.getClass().getResource("/Cat.jpg"));
    imageRecognitionRepository.recognition(testImage.getAbsolutePath());
  }
  @Test
  public void testJar() throws MWException {
    int randomNum = ThreadLocalRandom.current().nextInt(5, 10 + 1);
    imageRecognitionRepository.testJar(randomNum);
  }
  @Test
  public void testJarPerformance() throws IOException, MWException {
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