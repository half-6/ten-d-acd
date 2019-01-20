package com.tend.acd.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Date;

/**
* Created by LinkFuture Auto
* Generated on 04/14/2016.
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("response")
public class ResponseBaseEntity<T> {
    public ResponseBaseEntity(){

    }
    public ResponseBaseEntity(T data)
    {
        this.data = data;
    }
    @JsonProperty("code")
    public Integer code = 200;

    @JsonProperty("message")
    public String message = "success";

    @JsonProperty("timestamp")
    private Date timestamp = new Date();

    @JsonProperty("data")
    public T data;
}
