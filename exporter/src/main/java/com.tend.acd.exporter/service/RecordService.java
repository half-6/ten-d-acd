package com.tend.acd.exporter.service;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.exporter.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class RecordService {
  @Resource(name = "dbHelper")
  GenericDBHelper dbHelper;

  @Value("${image.path}")
  String FromImagePath;


  @Value("${image.path}")
  String ImportFile;

  public void export() throws Exception {
    Path outputFilePath = getOutputFilePath();
    if(!Files.exists(outputFilePath))
    {
      Files.createFile(outputFilePath);
    }
    Util.logger.warn("********** export to {} **********", outputFilePath);

    Util.logger.warn("image path {}", FromImagePath);
    writeToFile(outputFilePath,"hospital","hospital_pkey");
    writeToFile(outputFilePath,"record","record_pkey");
    writeToFile(outputFilePath,"roi_image","roi_image_pkey");
  }
  public void load() throws Exception{
    Path outputFilePath = Paths.get(ImportFile);
    if(!Files.exists(outputFilePath)){
      throw new IllegalArgumentException("Specific import file is not exists, " + ImportFile);
    }
    try(BufferedReader reader = new BufferedReader(new FileReader(outputFilePath.toFile()))){
      String line =reader.readLine();
      while (line!=null)
      {
         if(!line.isEmpty())
         {
           List<Object> output =  dbHelper.insert(line);
           Util.logger.trace("reading ... " + output);
         }
         line = reader.readLine();

      }
    }
  }
  private void writeToFile(Path filePath,String tableName,String conflictKey) throws Exception {
    Util.logger.warn("********** exporting {} table **********", tableName);
    int limit = 10;
    int offset = 0;
    boolean hasValue = true;
    BufferedWriter out = new BufferedWriter(new FileWriter(filePath.toString(), true));
    while (hasValue)
    {
      JSONObject query = new JSONObject();
      query.put("$limit",limit);
      query.put("$offset",offset);
      JSONObject results = dbHelper.selectToJson(tableName,query);
      if(results!=null)
      {
        JSONArray oriImage = results.getJSONArray("data");
        JSONArray page = results.getJSONArray("page");
        Util.logger.trace("reading {}",page.get(0).toString());
        oriImage = oriImage.getJSONArray(0);
        for(int i=0;i<oriImage.length();i++)
        {
          JSONObject item = oriImage.getJSONObject(i);
          String sql = dbHelper.getUpsertTSQL(tableName,item,conflictKey);
          out.write(sql);
          out.write(";");
          out.newLine();
          if(tableName == "roi_image"){
            String roiImageName = item.getString("roi_image") + ".png";
            String originalImageName = item.getString("original_image") + ".png";
            copyImage(roiImageName);
            copyImage(originalImageName);
          }
        }
      }
      else
      {
        hasValue = false;
      }
      offset = offset + limit;
    }
    out.close();
  }
  private void copyImage(String imageName) throws IOException {
    Path fromImagePath = getFromImagePath(imageName);
    if(Files.exists(fromImagePath))
    {
      Path toImagePath = getOutputImagePath(imageName);
      Files.copy(fromImagePath,toImagePath, StandardCopyOption.REPLACE_EXISTING);
      Util.logger.trace("save {} image success",toImagePath);
    }
    else
    {
      Util.logger.warn("missing {} image, skip",fromImagePath);
    }
  }

  private Path getOutputFilePath(){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    String date = simpleDateFormat.format(new Date());
    return Paths.get(Util.getAppPath(),date +".sql");
  }
  private Path getFromImagePath(String imageName){
    return Paths.get(FromImagePath,imageName);
  }
  private Path getOutputImagePath(String imageName) throws IOException {
    Path filePath = Paths.get(Util.getAppPath(),"static","uploads",imageName);
    if(!Files.exists(filePath))
    {
      Util.logger.trace("create directories {}",filePath);
      Files.createDirectories(filePath);
    }
    return filePath;
  }

}
