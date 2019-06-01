package com.tend.acd.server.service;

import com.tend.acd.server.ApplicationTests;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.CertificateEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SecurityServiceTest extends ApplicationTests {

    @Autowired
    SecurityService securityService;

    @Test
    public void generateCertificate() throws NoSuchPaddingException, SignatureException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        CertificateEntity certificateEntity = new CertificateEntity();
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH,1);

        certificateEntity.startDate = now;
        certificateEntity.expiredDate = c.getTime();
        certificateEntity.hospitalId = UUID.randomUUID();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
        securityService.generateCertificate(certificateEntity,outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CertificateEntity decryptEntity = securityService.readCertificate(inputStream);

        Assert.assertEquals(certificateEntity.startDate,decryptEntity.startDate);
        Assert.assertEquals(certificateEntity.expiredDate,decryptEntity.expiredDate);
        Assert.assertEquals(certificateEntity.hospitalId,decryptEntity.hospitalId);
    }

    @Test
    public void exportCertificate() throws NoSuchPaddingException, SignatureException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        CertificateEntity certificateEntity = new CertificateEntity();
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        //c.add(Calendar.MONTH,1);
        c.add(Calendar.HOUR,1);

        certificateEntity.startDate = now;
        certificateEntity.expiredDate = c.getTime();
        certificateEntity.hospitalId = UUID.fromString("82b988ee-c2ab-4960-8ac2-c073f426ee96");

        File file = Paths.get(Util.getAppPath(),"hospital.cert").toFile();
        FileOutputStream outputStream = new FileOutputStream (file);
        securityService.generateCertificate(certificateEntity,outputStream);
    }
}