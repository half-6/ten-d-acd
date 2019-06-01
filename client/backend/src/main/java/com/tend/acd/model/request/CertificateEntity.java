package com.tend.acd.model.request;

import LinkFuture.DB.Config;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateEntity {

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.DefaultTimeFormat)
    public Date startDate;

    @JsonProperty("expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Config.DefaultTimeFormat)
    public Date expiredDate;

    @JsonProperty("hospital_id")
    public UUID hospitalId;
}
