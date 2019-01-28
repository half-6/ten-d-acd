package com.tend.acd.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Module Name: ErrorController Project Name: ten-d-acd Created by Cyokin on 1/28/2019
 */
@Controller
public class MyErrorController implements ErrorController {
	
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {
	//let vue handle error 
    return "index.html";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
