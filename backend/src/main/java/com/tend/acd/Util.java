package com.tend.acd;


import java.util.Optional;
import org.slf4j.LoggerFactory;

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
}
