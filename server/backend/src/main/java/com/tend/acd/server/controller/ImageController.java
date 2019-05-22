package com.tend.acd.server.controller;

import com.aliyun.oss.model.OSSObject;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.UserEntity;
import com.tend.acd.server.repository.OSSRepository;
import com.tend.acd.server.service.RecordService;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Cyokin
 * on 3/25/2016.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    OSSRepository ossRepository;

    @Autowired
    RecordService recordService;

    @GetMapping(value = "/download",produces="application/zip")
    public void download(String input, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        UserEntity loginUser =  (UserEntity)request.getUserPrincipal();
        recordService.download(new JSONObject(input),loginUser,response);
    }


    @GetMapping(value="/{id}")
    public void get(@PathVariable String id,HttpServletResponse response) throws IOException {
        try {
            OSSObject image = ossRepository.read(id);
            InputStream in = image.getObjectContent();
            response.setContentType(image.getObjectMetadata().getContentType());
            response.setContentLengthLong(image.getObjectMetadata().getContentLength());
            IOUtils.copy(in,response.getOutputStream());
        }
        catch (Exception e){
            Util.logger.trace("Invalid image key");
            throw new IllegalArgumentException("Invalid image key");
        }
    }

}
