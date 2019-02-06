package com.tend.acd;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

/**
 * Created by Cyokin
 * on 4/22/2016.
 */
public class Util {
    public final static org.slf4j.Logger logger = LoggerFactory.getLogger("com.tend.acd.backend");

    public static String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();
    }

    public static File saveBase64Image(String base64Image) throws IOException {
        String imageString = base64Image.substring(base64Image.indexOf("base64,") + "base64,".length());
        byte[] imageByte= Base64.decodeBase64(imageString);
        BufferedImage image  =  ImageIO.read(new ByteArrayInputStream(imageByte));
        if(image==null)
        {
            throw new IllegalArgumentException("invalid image");
        }
        String fileName = UUID.randomUUID().toString();
        Path imgPath = getFilePath(fileName + ".png");
        File f = new File(imgPath.toString());
        ImageIO.write(image,"png",f);
        return f;
    }

    private static Path getFilePath(String fileName){
        String realPathToUploads = getAppUploadPath();
        if(! new File(realPathToUploads).exists())
        {
            new File(realPathToUploads).mkdirs();
        }
        return Paths.get(realPathToUploads, fileName);
    }
    public static String getAppUploadPath(){
        return Paths.get(getAppStaticPath().toString(),"uploads").toString();
    }
    public static Path getAppStaticPath(){
        ApplicationHome home = new ApplicationHome(Application.class);
        return Paths.get(home.getDir().getAbsolutePath(),"static");
    }

    public static String stripExtension (String fileName){
        if (fileName == null) return null;
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) return fileName;
        return fileName.substring(0, pos);
    }
}
