package fr.app.utils;

public class SizeFormatter {
    public static String format(long size) {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        char prefix = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", size / Math.pow(1024, exp), prefix);
    }
}
