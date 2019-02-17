package com.tend.acd.service;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class RecordService {
  @Value("${postgreSQL.connection}")
  String DBConnectionString;

  public Boolean save(JSONObject input) throws Exception {
    try(GenericDBHelper dbHelper = new GenericDBHelper(DBConnectionString))
    {
      JSONArray originalImageList =  input.getJSONArray("original_image");
      JSONArray oriImage = input.getJSONArray("roi_image");
      Object id = dbHelper.insert("record",input);
      Util.logger.trace("save record success," + id);
      for(int i=0;i<originalImageList.length();i++)
      {
         JSONObject item = originalImageList.getJSONObject(i);
         File img = Util.saveBase64Image(item.getString("src"));
         item.put("new_id",Util.stripExtension(img.getName()));
      }
      for(int i=0;i<oriImage.length();i++)
      {
        JSONObject item = oriImage.getJSONObject(i);
        File img = Util.saveBase64Image(item.getString("src"));
        item.put("record_id",id.toString());
        item.put("roi_image",Util.stripExtension(img.getName()));
        item.put("original_image",findNewId(originalImageList,item.getString("original_image_id")));
        Object roiImageId = dbHelper.insert("roi_image",item);
        Util.logger.trace("save roi image success," + roiImageId);
      }
      Util.logger.trace("save all success," + id);
      return true;
    }
  }
  public void download(JSONObject input, HttpServletResponse response) throws Exception {
    response.setHeader("Content-Disposition", "attachment; filename=\"tend-d-acd.zip\"");
    try(GenericDBHelper dbHelper = new GenericDBHelper(DBConnectionString))
    {
        JSONObject results = dbHelper.selectToJson("v_roi_image",input);
        JSONArray oriImage = results.getJSONArray("data");
        if(oriImage.length()>0 )
        {
          oriImage = oriImage.getJSONArray(0);
          try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for(int i=0;i<oriImage.length();i++)
            {
              JSONObject item = oriImage.getJSONObject(i);
              String imageId = item.get("roi_image").toString();
              String imageName = imageId + ".png";
              Path filePath = Util.getFilePath(imageName);
              if(Files.exists(filePath))
              {
                ZipEntry entry = new ZipEntry(buildImageNameInZip(item));
                zos.putNextEntry(entry);
                zos.write(Files.readAllBytes(Util.getFilePath(imageName)));
                zos.closeEntry();
              }
            }
          }
        }
    }
  }
  private String buildImageNameInZip(JSONObject item){
    return Util.fileNameFormat(item.getString("cancer_type_name")
    + "_"  + item.getString("machine_type_name")
    + "_"  + (item.has("pathology")?item.getString("pathology"):"")
    + "_"  + item.getString("prediction")
    + "_"  + item.get("roi_image").toString() + ".png");
  }
  private String findNewId(JSONArray originalImageList,String oldId){
    for(int i=0;i<originalImageList.length();i++)
    {
      JSONObject item = originalImageList.getJSONObject(i);
      if(item.getString("id").equalsIgnoreCase(oldId))
      {
         return item.getString("new_id");
      }
    }
    return null;
  }
}
