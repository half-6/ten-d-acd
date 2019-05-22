package com.tend.acd.server.repository;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public class OSSRepository {

    @Value("${oss.bucketName.image}")
    String imageBucketName;

    private OSSClient ossClient;

    public OSSRepository(@Value("${oss.endpoint}")
                                 String endpoint,
                         @Value("${oss.accessKeyId}")
                                 String accessKeyId,
                         @Value("${oss.accessKeySecret}")
                                 String accessKeySecret
    ) {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public void upload( File file) {
        String id = FilenameUtils.removeExtension(file.getName());
        ossClient.putObject(imageBucketName, id, file);
    }
    public OSSObject read(String id){
        return ossClient.getObject(imageBucketName,id);
    }
}
