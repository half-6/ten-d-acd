package com.tend.acd.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by LinkFuture Auto
* Generated on 04/14/2016.
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseImageRecognitionEntity {
    @JsonProperty("prediction")
    public String prediction;

    @JsonProperty("processTime")
    public String processTime;

    @JsonProperty("probability")
    public String probability;

    @JsonProperty("pathology")
    public String pathology;
}
