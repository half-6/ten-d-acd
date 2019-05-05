package com.tend.acd;


import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Cyokin
 * on 4/22/2016.
 */
public class Util {
    public final static org.slf4j.Logger logger = LoggerFactory.getLogger("com.tend.acd.backend");

    @SuppressWarnings("unused")
    public static String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();
    }

    @SuppressWarnings("unused")
    public static void readRGB(BufferedImage image)
    {
        //get RGB
        int[] ARGB = image.getRGB(0,0,image.getWidth(),image.getHeight(),null,0,image.getWidth());
        int[] R = new int[ARGB.length];
        int[] G = new int[ARGB.length];
        int[] B = new int[ARGB.length];
        for(int i=0;i<ARGB.length;i++)
        {
            Color c = new Color(ARGB[i]);
            R[i] = c.getRed();
            G[i] = c.getGreen();
            B[i] = c.getBlue();
        }
    }
    public static String getBase64FromImage(String base64Image)
    {
        if(base64Image.contains("base64,"))
        {
            return base64Image.substring(base64Image.indexOf("base64,") + "base64,".length());
        }
        return base64Image;
    }

    public static File saveBase64Image(String base64Image) throws IOException {
        base64Image = getBase64FromImage(base64Image);
        byte[] imageByte= Base64.getDecoder().decode(base64Image);
        BufferedImage image  =  ImageIO.read(new ByteArrayInputStream(imageByte));
        if(image==null)
        {
            throw new IllegalArgumentException("invalid image");
        }
        String fileName = UUID.randomUUID().toString();
        Path imgPath = getFilePath(fileName + ".png");
        File f = new File(imgPath.toString());
        ImageIO.write(image,"png",f);
        Util.logger.trace("saved image to " + f.getAbsolutePath());
        return f;
    }

    public static String getBase64String(String resourceFilePath) throws IOException {
        byte [] bytes  = IOUtils.toByteArray( Util.class.getClassLoader().getResourceAsStream(resourceFilePath));
        logger.trace("got resource file " + resourceFilePath + " size is " + bytes.length);
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }

    public static String getBase64String(Path file) throws IOException {
        byte [] bytes  = Files.readAllBytes(file);
        logger.trace("got resource file " + file + " size is " + bytes.length);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Path getFilePath(String fileName){
        String realPathToUploads = getAppUploadPath();
        if(! new File(realPathToUploads).exists())
        {
            //noinspection ResultOfMethodCallIgnored
            new File(realPathToUploads).mkdirs();
        }
        return Paths.get(realPathToUploads, fileName);
    }
    private static String getAppUploadPath(){
        return Paths.get(getAppStaticPath().toString(),"uploads").toString();
    }
    static Path getAppStaticPath(){
        return Paths.get(getAppPath(),"static");
    }
    public static String getAppPath(){
        ApplicationHome home = new ApplicationHome(Application.class);
        return home.getDir().getAbsolutePath();
    }

    public static String stripExtension (String fileName){
        if (fileName == null) return null;
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) return fileName;
        return fileName.substring(0, pos);
    }

    public static String fileNameFormat(String input){
        return input.replaceAll("[^a-zA-Z0-9_\\-\\S+]", "");
    }
}
