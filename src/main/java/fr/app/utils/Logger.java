package fr.app.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    private static void log(String level, String message) {
        System.out.printf("[%s] [%s] %s%n", FORMATTER.format(LocalDateTime.now()), level, message);
    }
}

