package ru.informtb.patterns.singleton;

public interface Logger {

    void logError(String message);

    void logInfo(String message);

    void logWarning(String message);

    void getLogHistory();
}
