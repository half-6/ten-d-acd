package com.tend.acd.exporter.respsitory;

import com.tend.acd.exporter.ApplicationTests;
import com.tend.acd.exporter.Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OSSRepositoryTest extends ApplicationTests {

    @Autowired
    OSSRepository ossRepository;

    @Test
    public void uploadToRoi() {
        String filePath = OSSRepository.class.getClassLoader().getResource("B39.jpg").getPath();
        ossRepository.upload(new File(filePath));

    }
    @Test
    public void test() throws IOException {
        Files.walk(Paths.get("D:\\codes\\qyotech\\ten-d-acd\\exporter\\target\\201905101235\\static\\uploads"))
                .filter(i->i.endsWith("jpg") || i.endsWith("png") || i.endsWith("bmp"))
                .forEach(i->{
                    Util.logger.trace("uploading images {}", i);
                    //ossRepository.upload(i.toFile());
                });

    }
}