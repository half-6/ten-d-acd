package com.tend.acd.exporter;


import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

/**
 * Created by Cyokin
 * on 4/22/2016.
 */
public class Util {
    public final static org.slf4j.Logger logger = LoggerFactory.getLogger("com.tend.acd.backend");
    public static String getAppPath(){
        ApplicationHome home = new ApplicationHome(Application.class);
        return home.getDir().getAbsolutePath();
    }
}
