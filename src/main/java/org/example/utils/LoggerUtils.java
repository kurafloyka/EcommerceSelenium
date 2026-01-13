package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Loglama işlemleri için utility sınıfı
 */
public class LoggerUtils {
    private static final Logger logger = LogManager.getLogger(LoggerUtils.class);

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }
}

