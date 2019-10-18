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
    public String shapeRatio;

    @JsonProperty("Margin_Levels")
    public double[][] marginLevels;

    @JsonProperty("Smoothness_Ratio")
    public double smoothnessRatio;

    @JsonProperty("Irregularity_Ratio")
    public double irregularityRatio;

    @JsonProperty("Echos")
    public double[] echos;

    @JsonProperty("Echo_Label")
    public String echoLabel;

    @JsonProperty("Calcification_Coods")
    public double[][] calcificationCoods;

    @JsonProperty("Calcification_index")
    public double calcificationIndex;
}
