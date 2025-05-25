package ru.informtb.patterns.singleton;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService implements Logger {
    private final int LOG_SIZE = 10;
    private static final LoggerService INSTANCE = new LoggerService();

    private LoggerService() {
    }

    public static LoggerService getInstance() {
        return INSTANCE;
    }

    @Override
    public void logError(String message) {
        String logText = String.format("%s - {ERROR}: %s\n", timeMetrics(), message);
        System.out.printf(logText);
        if (!checkingFull()) {
            writeToFile(logText);
        }

    }

    @Override
    public void logInfo(String message) {
        String logText = String.format("%s - {INFO}: %s\n", timeMetrics(), message);
        System.out.printf(logText);
        if (!checkingFull()) {
            writeToFile(logText);
        }
    }

    @Override
    public void logWarning(String message) {
        String logText = String.format("%s - {WARNING}: %s\n", timeMetrics(), message);
        System.out.printf(logText);
        if (!checkingFull()) {
            writeToFile(logText);
        }

    }

    @Override
    public void getLogHistory() {
        StringBuffer stringBuffer = new StringBuffer();
        File file = new File("C:\\Users\\User\\Documents\\logSingleton.log");

        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(stringBuffer);
    }

    private boolean checkingFull() {
        System.out.println("Лог полностью заполнен. Освободите память");
        return LOG_SIZE <= getSizeInLogFile();

    }

    private int getSizeInLogFile() {
        int size = 0;
        File file = new File("C:\\Users\\User\\Documents\\logSingleton.log");

        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bf.readLine()) != null) {
                size++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return size;
    }

    private String timeMetrics() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void writeToFile(String message) {
        File file = new File("C:\\Users\\User\\Documents\\logSingleton.log");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            bufferedWriter.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
