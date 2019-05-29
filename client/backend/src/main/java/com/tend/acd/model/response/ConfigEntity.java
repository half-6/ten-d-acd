package com.tend.acd.model.response;

import com.tend.acd.model.request.CertificateEntity;
import org.springframework.stereotype.Component;

@Component
public class ConfigEntity {
    public String applicationVersion = getClass().getPackage().getImplementationVersion();
    public String recognitionVersion;
    public CertificateEntity certificateEntity;
}
