package com.tend.acd.server.model.response;

import org.springframework.stereotype.Component;

@Component
public class ConfigEntity {
    public String applicationVersion = getClass().getPackage().getImplementationVersion();
}
