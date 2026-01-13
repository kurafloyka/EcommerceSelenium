package org.example.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration dosyasından değerleri okumak için utility sınıfı
 */
public class ConfigReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream inputStream = ConfigReader.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            if (inputStream == null) {
                throw new RuntimeException("Config dosyası bulunamadı: config.properties");
            }
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Config dosyası yüklenemedi", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    public static String getECommerceUrl() {
        return getProperty("ecommerce.url");
    }

    public static String getECommerceEmail() {
        return getProperty("ecommerce.email");
    }

    public static String getECommercePassword() {
        return getProperty("ecommerce.password");
    }
}

