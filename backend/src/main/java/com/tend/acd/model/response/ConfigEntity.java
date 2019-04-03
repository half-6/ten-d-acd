package com.tend.acd.model.response;

import org.springframework.stereotype.Component;

@Component
public class ConfigEntity {
    public String applicationVersion = getClass().getPackage().getImplementationVersion();
    public String recognitionVersion;
}
