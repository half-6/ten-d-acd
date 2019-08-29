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
    @JsonProperty("Prediction")
    public String prediction;

    @JsonProperty("ProcessingTime")
    public double processingTime;

    @JsonProperty("Probability")
    public double probability;

    @JsonProperty("Shape_Axises")
    public double[][] shapeAxises;

    @JsonProperty("Shape_Ratio")
    public double shapeRatio;

    @JsonProperty("Margin_Levels")
    public double[][] marginLevels;

    @JsonProperty("Margin_Ratio")
    public double marginRatio;

    @JsonProperty("Echos")
    public double[] echos;

    @JsonProperty("isUni")
    public String isUni;

    @JsonProperty("Calcification_Coods")
    public double[][] calcificationCoods;

    @JsonProperty("Calcification_index")
    public double calcificationIndex;
}
