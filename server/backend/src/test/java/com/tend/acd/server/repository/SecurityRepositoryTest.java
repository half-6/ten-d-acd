package com.tend.acd.server.repository;

import com.tend.acd.server.ApplicationTests;
import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.CertificateEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class SecurityRepositoryTest extends ApplicationTests {

    @Autowired
    SecurityRepository securityRepository;
    Path keyDir = Paths.get(Util.getAppPath(),"keys");

    @Test
    public void generateKeyPairToFile() throws IOException, NoSuchAlgorithmException {
        securityRepository.generateKeyPairToFile(keyDir);
    }

    @Test
    public void encryptAndDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        String input = Util.toJson(new CertificateEntity());
        String encryptStr = securityRepository.encrypt(input);
        String decryptStr = securityRepository.decrypt(encryptStr);
        Assert.assertEquals(decryptStr,input);
    }

    @Test
    public void signAndVerify() throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, SignatureException {
        String input = "ABCD";
        String encryptStr = securityRepository.signature(input);
        boolean isVerify = securityRepository.verify(input,encryptStr);
        Assert.assertTrue(isVerify);
    }
}