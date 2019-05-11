package com.tend.acd.server.model.response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigEntity {
    public String applicationVersion = getClass().getPackage().getImplementationVersion();
    @Value("${image.url}")
    public String imageUrl;
}
