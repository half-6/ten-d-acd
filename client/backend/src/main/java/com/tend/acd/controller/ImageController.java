package com.tend.acd.controller;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.model.response.ResponseBaseEntity;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.service.ImageRecognitionService;
import com.tend.acd.service.RecordService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Cyokin
 * on 3/25/2016.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageRecognitionService imageRecognitionService;

    @Autowired
    RecordService recordService;

    @PostMapping(value="/recognition")
    public ResponseBaseEntity<ResponseImageRecognitionEntity> recognition(@RequestBody String input)
            throws Exception {
        return new ResponseBaseEntity<>(imageRecognitionService.recognize(new JSONObject(input),true));
    }

    @PostMapping(value="/import")
    public ResponseBaseEntity<ResponseImageRecognitionEntity> batch(@RequestBody String input)
            throws Exception {
        return new ResponseBaseEntity<>(imageRecognitionService.recognize(new JSONObject(input),false));
    }

    @PostMapping(value = "/save")
    public ResponseBaseEntity<Boolean> save(@RequestBody String input)
        throws Exception {
        return new ResponseBaseEntity<>(recordService.save(new JSONObject(input)));
    }
    @GetMapping(value = "/download",produces="application/zip")
    public void download(String input, HttpServletResponse response)
            throws Exception {
        recordService.download(new JSONObject(input),response);
    }
}
