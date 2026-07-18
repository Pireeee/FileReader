package fr.app.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class Logger {

    public enum Level { DEBUG, INFO, WARN, ERROR, OFF }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default INFO: debug logs are skipped entirely (message strings are never built).
    // Override at launch with -Dapp.log.level=DEBUG, or at runtime with setLevel().
    private static volatile Level threshold = initLevel();

    private static Level initLevel() {
        String configured = System.getProperty("app.log.level", "INFO");
        try {
            return Level.valueOf(configured.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }

    public static void setLevel(Level level) {
        threshold = level;
    }

    public static boolean isDebugEnabled() {
        return threshold.compareTo(Level.DEBUG) <= 0;
    }

    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    // Supplier variant for hot paths: the message is only built if debug is enabled.
    public static void debug(Supplier<String> message) {
        if (isDebugEnabled()) {
            log(Level.DEBUG, message.get(), null);
        }
    }

    private static String addThreadInfo(String message) {
        String threadName = Thread.currentThread().getName();
        return "[Thread=" + threadName + "] " + message;
    }

    private static void log(Level level, String message, Throwable throwable) {
        if (level.compareTo(threshold) < 0) {
            return;
        }
        String timestamp = FORMATTER.format(LocalDateTime.now());
        System.out.printf("[%s] [%5s] %s%n", timestamp, level, addThreadInfo(message));

        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            System.out.printf("[%s] [%5s] Exception: %s%n", timestamp, level, sw);
        }
    }
}