package fr.app.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", addThreadInfo(message), null);
    }

    public static void warn(String message) {
        log("WARN", addThreadInfo(message), null);
    }

    public static void error(String message) {
        log("ERROR", addThreadInfo(message), null);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", addThreadInfo(message), throwable);
    }

    public static void debug(String message) {
        log("DEBUG", addThreadInfo(message), null);
    }

    private static String addThreadInfo(String message) {
        String threadName = Thread.currentThread().getName();
        return "[Thread=" + threadName + "] " + message;
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
