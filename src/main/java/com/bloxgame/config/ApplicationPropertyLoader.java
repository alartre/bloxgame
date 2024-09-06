package com.bloxgame.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertyLoader {

    private static final Properties properties = new Properties();

    static {
        try(InputStream inputStream = ApplicationPropertyLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(inputStream == null) {
                throw new FileNotFoundException("Error reading application.properties file");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("application.properties file don't found");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
