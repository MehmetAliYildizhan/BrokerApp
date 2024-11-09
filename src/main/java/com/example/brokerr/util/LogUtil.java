package com.example.brokerr.util;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void writeLog(String type, String code, String description) {
        String date = LocalDate.now().format(DATE_FORMAT);
        String fileName = "Log" + date + ".txt";

        String logEntry = String.format("%s | %-7s | %-6s | %-100s",
                LocalDateTime.now().format(DATE_TIME_FORMAT),
                padRight(type, 7),
                padRight(code, 6),
                padRight(description, 100));

        writeToFile(fileName, logEntry);
    }

    public static void writeServiceLog(String serviceName, String type, Long timer, String message) {
        String date = LocalDate.now().format(DATE_FORMAT);
        String fileName = "ServiceLog" + date + ".txt";

        String logEntry;
        if ("response".equals(type) && timer != null) {
            logEntry = String.format("%s | %-15s | %-10s | %-6d ms | %-100s",
                    LocalDateTime.now().format(DATE_TIME_FORMAT),
                    padRight(serviceName, 15),
                    padRight(type, 10),
                    timer,
                    padRight(message, 100));
        } else {
            logEntry = String.format("%s | %-15s | %-10s | %-100s",
                    LocalDateTime.now().format(DATE_TIME_FORMAT),
                    padRight(serviceName, 15),
                    padRight(type, 10),
                    padRight(message, 100));
        }

        writeToFile(fileName, logEntry);
    }

    private static void writeToFile(String fileName, String logEntry) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sağdan boşluk ekleyerek sabit uzunlukta string oluşturan yardımcı metod
    private static String padRight(String input, int length) {
        if (input == null) {
            input = "";
        }
        return String.format("%-" + length + "s", input);
    }
}