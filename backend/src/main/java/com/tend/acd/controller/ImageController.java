package com.tend.acd.controller;

import com.mathworks.toolbox.javabuilder.MWException;
import com.tend.acd.model.response.ResponseBaseEntity;
import com.tend.acd.model.response.ResponseImageRecognitionEntity;
import com.tend.acd.service.ImageRecognitionService;
import com.tend.acd.service.RecordService;
import java.io.IOException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Cyokin
 * on 3/25/2016.
 */
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageRecognitionService imageRecognitionService;

    @Autowired
    RecordService recordService;

    @PostMapping(value="/recognition")
    public ResponseBaseEntity<ResponseImageRecognitionEntity> create(@RequestParam(value = "image") String image)
        throws IOException, MWException {
        return new ResponseBaseEntity<>(imageRecognitionService.recognize(image));
    }

    @PostMapping(value = "/save")
    public ResponseBaseEntity<Boolean> save(@RequestBody String input)
        throws Exception {
        return new ResponseBaseEntity<>(recordService.save(new JSONObject(input)));
    }
}
