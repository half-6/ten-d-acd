package com.tend.acd.exporter.service;

import LinkFuture.DB.DBHelper.GenericDBHelper;
import com.tend.acd.exporter.Util;
import com.tend.acd.exporter.respsitory.OSSRepository;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  @Value("${app.image.dir}")
  String AppImageDir;


  @Value("${backup.dir}")
  String BackupDir;

  @Autowired
  OSSRepository ossRepository;

  // import
  public void load() throws Exception{
    Path exportDir = Paths.get(BackupDir);
    if(!Files.exists(exportDir)){
      throw new IllegalArgumentException("Specific import directory is not exists, " + exportDir);
    }
    Util.logger.warn("********** import from {}  **********", exportDir);
    //generate sql
    Path outputFilePath = Paths.get(exportDir.toString(),"tend.sql");
    Util.logger.trace("import data =>{}", outputFilePath);
    try(BufferedReader reader = new BufferedReader(new FileReader(outputFilePath.toFile()))){
      String line =reader.readLine();
      while (line!=null)
      {
        if(!line.isEmpty())
        {
          List<Object> output =  dbHelper.insert(line);
          Util.logger.trace("imported id:{} ", output);
        }
        line = reader.readLine();
      }
    }
    //upload images to OSS
    Path fromImageDir = Paths.get(exportDir.toString(),"static","uploads");
    Util.logger.trace("import images {}=>OSS cloud", fromImageDir);
    Files.walk(fromImageDir)
            .filter(i->{
              String filePath = i.toString();
              return filePath.endsWith("jpg") || filePath.endsWith("png") || filePath.endsWith("bmp");
            })
            .forEach(i->{
              Util.logger.trace("uploading images {}", i);
              ossRepository.upload(i.toFile());
            });

    Util.logger.warn("********** import success  **********");
  }


  //export
  public void export() throws Exception {
    Path exportDir = getExportDir();
    if(!Files.exists(exportDir))
    {
      Files.createDirectories(exportDir);
    }
    File fromImageDir = new File(AppImageDir);
    if(!fromImageDir.exists())
    {
      Util.logger.error("target image dir not exists ", AppImageDir);
    }
    Util.logger.warn("********** export to {} **********", exportDir);
    //generate sql
    Path outputFilePath = Paths.get(exportDir.toString(),"tend.sql");
    Util.logger.trace("export data =>{}", outputFilePath);
    writeToFile(outputFilePath,"hospital","hospital_pkey");
    writeToFile(outputFilePath,"roi_history","roi_history_pkey");
    writeToFile(outputFilePath,"record","record_pkey");
    writeToFile(outputFilePath,"roi_image","roi_image_pkey");
    //copy images
    File toImageDir = Paths.get(exportDir.toString(),"static","uploads").toFile();
    Util.logger.trace("export images {}=>{}", fromImageDir,toImageDir);
    FileUtils.copyDirectory(fromImageDir,toImageDir);
    Util.logger.warn("********** export success  **********");
  }

  private void writeToFile(Path filePath,String tableName,String conflictKey) throws Exception {
    Util.logger.warn("exporting {} table", tableName);
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
          String sql = dbHelper.getUpsertTSQL(tableName,item,conflictKey,false);
          out.write(sql);
          out.write(";");
          out.newLine();
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

  private Path getExportDir(){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    String date = simpleDateFormat.format(new Date());
    return Paths.get(Util.getAppPath(),date);
  }
}
