package com.tend.acd.controller;

import com.tend.acd.Util;
import com.tend.acd.model.response.ResponseBaseEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Module Name: ${FILE_NAME}
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@SuppressWarnings("unused")
@RestControllerAdvice
public class RestErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBaseEntity processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> errorMessage = new ArrayList<>();
        for (FieldError fieldError: fieldErrors) {
            errorMessage.add(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        ResponseBaseEntity output = new ResponseBaseEntity();
        output.code = HttpStatus.BAD_REQUEST.value();
        output.message = String.join(",",errorMessage);
        Util.logger.error(output.message);
        return output;
    }
    @ExceptionHandler({IllegalArgumentException.class,
        MissingServletRequestParameterException.class,
        MissingPathVariableException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseBaseEntity processNotFoundError(Exception ex)
    {
        ResponseBaseEntity output = new ResponseBaseEntity();
        output.code = HttpStatus.NOT_FOUND.value();
        output.message = ex.getMessage();
        Util.logger.trace(output.message);
        return output;
    }
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseBaseEntity processUnKnowError(Exception ex)
    {
        ResponseBaseEntity output = new ResponseBaseEntity();
        output.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        output.message = "Unknown error,Please try later";
        Util.logger.error(ex.getClass().getName() + "=>" + ex.getMessage());
        return output;
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseBaseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ResponseBaseEntity output = new ResponseBaseEntity();
        output.code = HttpStatus.METHOD_NOT_ALLOWED.value();
        output.message = "Request method not supported";
        Util.logger.warn(output.message);
        return output;
    }
}
