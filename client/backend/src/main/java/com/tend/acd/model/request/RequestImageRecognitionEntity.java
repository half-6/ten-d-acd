package com.tend.acd.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;


/**
 * Module Name: ${FILE_NAME}
 * Project Name: feifanuniv-search-api
 * Created by Cyokin on 5/30/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestImageRecognitionEntity {
    @JsonProperty("image")
    @NotNull
    public MultipartFile image;
}
