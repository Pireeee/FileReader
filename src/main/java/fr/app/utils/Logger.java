package fr.app.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", message, null);
    }

    public static void warn(String message) {
        log("WARN", message, null);
    }

    public static void error(String message) {
        log("ERROR", message, null);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message, throwable);
    }

    public static void debug(String message) {
        log("DEBUG", message, null);
    }

    private static void log(String level, String message, Throwable throwable) {
        String timestamp = FORMATTER.format(LocalDateTime.now());
        System.out.printf("[%s] [%5s] %s%n", timestamp, level, message);

        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            System.out.printf("[%s] [%5s] Exception: %s%n", timestamp, level, sw);
        }
    }
}
