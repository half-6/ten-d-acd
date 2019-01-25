package com.tend.acd.service;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.Util;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
      input.remove("original_image");
      input.remove("roi_image");
      Object id = dbHelper.insert("record",input);
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
        item.put("original_image",findNewId(originalImageList,item.getString("original_image")));
        item.remove("prediction");
        Object roiImageId = dbHelper.insert("roi_image",item);
        Util.logger.trace("save roi success," + roiImageId);
      }
      Util.logger.trace("save all record success," + id);
      return true;
    }
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
