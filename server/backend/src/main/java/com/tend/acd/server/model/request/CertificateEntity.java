package com.tend.acd.server.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class CertificateEntity {

    @JsonProperty("start_Time")
    public Date startTime;

    @JsonProperty("expired_time")
    public Date expiredTime;

    @JsonProperty("hospital_id")
    public UUID hospitalId;
}
