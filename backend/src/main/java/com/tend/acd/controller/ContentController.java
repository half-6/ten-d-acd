package com.tend.acd.controller;

import LinkFuture.DB.Utility;
import com.tend.acd.model.response.ConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Cyokin
 * on 3/25/2016.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    ConfigEntity configEntity;

    @GetMapping(value = "/config.js",produces="text/javascript")
    public @ResponseBody String getConfig(String input, HttpServletResponse response)
            throws Exception {
        return "window.tendConfig=" + Utility.toJson(configEntity);
    }
}
