package com.tend.acd.server.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Module Name: ErrorController Project Name: ten-d-acd Created by Cyokin on 1/28/2019
 */
@SuppressWarnings("unused")
@Controller
public class MyErrorController implements ErrorController {
	
  @RequestMapping("/error")
  public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {
    if(response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED)
    {
      ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
      modelAndView.addObject("code",response.getStatus());
      modelAndView.addObject("message","UNAUTHORIZED");
      modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
      return modelAndView;
    }
    if(response.getStatus() == HttpServletResponse.SC_NOT_FOUND)
    {
      ModelAndView modelAndView = new ModelAndView("index.html");
      modelAndView.setStatus(HttpStatus.OK);
      return modelAndView;
    }
    else
    {
      ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
      modelAndView.addObject("code",response.getStatus());
      modelAndView.addObject("message","UNKNOWN ERROR");
      modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      return modelAndView;
    }
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
