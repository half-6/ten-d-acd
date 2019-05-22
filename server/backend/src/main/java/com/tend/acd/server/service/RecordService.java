package com.tend.acd.server.service;

import com.aliyun.oss.model.OSSObject;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.repository.OSSRepository;
import com.tend.acd.server.repository.RoiImageRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Module Name: ImageRecognitionService Project Name: com.tend.acd Created by Cyokin on 1/18/2019
 */
@Service
public class RecordService {

  @Autowired
  RoiImageRepository roiImageRepository;
  @Autowired
  OSSRepository ossRepository;

  public void download(JSONObject input, UserEntity loginUser,HttpServletResponse response) throws Exception {
    response.setHeader("Content-Disposition", "attachment; filename=\"tend-d-acd.zip\"");
    JSONArray oriImage = roiImageRepository.findArray(input);
    if(oriImage != null && oriImage.length()>0 )
    {
      StringWriter sw = new StringWriter();
      HashSet<String> entryExists = new HashSet<>();
      try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
        try(CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                .withHeader("ID","External ID", "ORIGINAL Image ID", "ROI Image ID", "Prediction", "Pathology", "Probability","Processing Time","Cancer Type","Machine Type","Coordinate","Date Registered")))
        {
          for(int i=0;i<oriImage.length();i++)
          {
            JSONObject item = oriImage.getJSONObject(i);
            String imageId = item.get("roi_image").toString();
            String outputImageName = buildImageNameInZip(item,loginUser);
            if(!entryExists.contains(outputImageName))
            {
              putImageToZip(zos,imageId,"roi/" + outputImageName);
              entryExists.add(outputImageName);
            }

            String originalImageId = item.get("original_image").toString();
            String originalImageName = originalImageId + ".png";
            if(!entryExists.contains(originalImageName)) {
              putImageToZip(zos, originalImageId, "original/" + originalImageName);
              entryExists.add(originalImageName);
            }

            csvPrinter.printRecord(
                    item.get("roi_image_id").toString()
                    ,item.get("record_external_id").toString()
                    ,item.get("original_image").toString()
                    ,item.get("roi_image").toString()
                    ,item.has("prediction")?item.getString("prediction"):null
                    ,item.has("pathology")?item.getString("pathology"):null
                    ,item.has("probability")?item.getDouble("probability"):null
                    ,item.has("processing_time")?item.getDouble("processing_time"):null
                    ,item.has("cancer_type_name")?item.getString("cancer_type_name"):null
                    ,item.has("machine_type_name")?item.getString("machine_type_name"):null
                    ,item.has("coordinate")?item.getJSONObject("coordinate"):null
                    ,item.has("date_registered")?item.getString("date_registered"):null
            );
          }
          ZipEntry entry = new ZipEntry("record.csv");
          zos.putNextEntry(entry);
          zos.write(sw.toString().getBytes());
          zos.closeEntry();
        }
      }
    }
    else
    {
      response.sendError(404,"No results found");
    }
  }
  private void putImageToZip(ZipOutputStream zos,String imageName,String zipImageName) throws IOException {
    try {
      OSSObject image = ossRepository.read(imageName);
      ZipEntry entry = new ZipEntry(zipImageName);
      zos.putNextEntry(entry);
      zos.write(IOUtils.toByteArray(image.getObjectContent()));// Files.readAllBytes(originalFilePath));
      zos.closeEntry();
    }
    catch (Exception  e)
    {
       Util.logger.warn("Specific image not found in OSS");
    }
  }

  private String buildImageNameInZip(JSONObject item,UserEntity loginUser){
    return Util.fileNameFormat(
            item.getString("cancer_type_name")
    + "_"  + item.getString("machine_type_name")
    + "_"  + (item.has("pathology")?item.getString("pathology"):"")
    + "_"  + item.getString("prediction")
    //+ "_"  + (item.has("probability")?item.getDouble("probability"):"")
    //+ "_"  + (item.has("processing_time")?item.getDouble("processing_time"):"")
    + "_"  + item.get("roi_image").toString()
    + "_"  + loginUser.userId
    + ".png");
  }

}
