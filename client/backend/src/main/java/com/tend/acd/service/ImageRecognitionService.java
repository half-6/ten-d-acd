package com.tend.acd.service;

import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.repository.DBRepository;
import com.tend.acd.repository.ImageRecognitionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

  public ResponseImageRecognitionEntity recognize(JSONObject input,boolean saveHistory)
          throws Exception {
    String base64Image = Util.getBase64FromImage(input.getString("roi_image_src"));
    String cancerType = input.getString("cancer_type");
    String roi_coordinates = input.getString("roi_coordinates");
    String roi_corners = input.getString("roi_corners");
    ResponseImageRecognitionEntity output =  imageRecognitionRepository.recognition(base64Image,cancerType,roi_coordinates,roi_corners);
    input.put("prediction",output.prediction);
    input.put("probability",output.probability / 100);
    input.put("processing_time",output.processingTime);
    input.put("detection_result",Util.toJson(output));
    if(saveHistory){
      new Thread(() -> {
        try {
          dbRepository.saveHistory(input);
        } catch (Exception e) {
          Util.logger.error("saveHistory",e);
        }
      }).run();
    }
    return output;
  }
}
