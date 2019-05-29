package com.tend.acd.service;

import com.tend.acd.ApplicationTests;
import com.tend.acd.model.request.CertificateEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityServiceTest extends ApplicationTests {

    @Autowired
    SecurityService securityService;

    @Test
    public void readCertificate() throws Exception {
        CertificateEntity certificate = securityService.readCertificate();
    }
}