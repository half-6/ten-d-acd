package com.tend.acd.service;

import com.tend.acd.Util;
import com.tend.acd.model.request.CertificateEntity;
import com.tend.acd.repository.DBRepository;
import com.tend.acd.repository.SecurityRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("Duplicates")
@Service
public class SecurityService {

    @Autowired
    SecurityRepository securityRepository;

    @Autowired
    DBRepository dbRepository;

    public CertificateEntity readCertificate() throws Exception {
        Util.logger.trace("reading certificate");
        FileInputStream inputStream = new FileInputStream(Util.getAppCertPath());
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
                CertificateEntity certificate =  Util.fromJson(securityRepository.decrypt(encrypt),CertificateEntity.class);
                Util.logger.trace("got certificate {}",Util.toJson(certificate));
                JSONObject hospital = dbRepository.findHospital(certificate.hospitalId);
                if(hospital!=null)
                {
                    Date now = new Date();
                    if(certificate.expiredTime.after(now))
                    {
                        Util.logger.trace("The application will expire on {}", certificate.expiredTime);
                    }
                    else
                    {
                        Util.logger.error("Your license has expired on {},Please contact your support representative", certificate.expiredTime);
                    }
                    return certificate;
                }
                else
                {
                    Util.logger.error("Certificate not match");
                }
            }
        }
        throw new RuntimeException("Invalid certificate");
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
