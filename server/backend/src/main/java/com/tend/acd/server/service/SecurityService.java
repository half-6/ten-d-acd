package com.tend.acd.server.service;

import com.tend.acd.server.Util;
import com.tend.acd.server.model.request.CertificateEntity;
import com.tend.acd.server.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class SecurityService {

    @Autowired
    SecurityRepository securityRepository;

    public void generateCertificate(CertificateEntity certificateEntity, OutputStream outputStream) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, SignatureException {
        String input = Util.toJson(certificateEntity);
        String encrypt = securityRepository.encrypt(input);
        String sigingure = securityRepository.signature(encrypt);
        try(ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            putToZip(zos,"encrypt",encrypt);
            putToZip(zos,"sign",sigingure);
        }
    }
    private void putToZip(ZipOutputStream zos,String name,String content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }
    public CertificateEntity readCertificate(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchPaddingException {
        String encrypt = null;
        String sign = null;
        try (ZipInputStream zos=new ZipInputStream(inputStream)){
            ZipEntry entry = zos.getNextEntry();
            while (entry != null){
                if(entry.getName().equalsIgnoreCase("encrypt"))
                {
                    encrypt =  readContent(zos);
                }
                if(entry.getName().equalsIgnoreCase("sign"))
                {
                    sign =  readContent(zos);
                }
                entry = zos.getNextEntry();
            }
        }
        if(encrypt!=null && sign!=null){
            boolean verified = securityRepository.verify(encrypt,sign);
            if(verified)
            {
                return Util.fromJson(securityRepository.decrypt(encrypt),CertificateEntity.class);
            }
        }
        return null;
    }
    private String readContent(ZipInputStream zos) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((len = zos.read(buffer,0,1024)) > 0) {
            sb.append(new String(buffer,0,len));
        }
        return sb.toString();
    }


}
