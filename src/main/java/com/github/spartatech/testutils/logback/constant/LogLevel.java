package com.github.spartatech.testutils.logback.constant;

import ch.qos.logback.classic.Level;

/**
 * Log Level to be used while annotating a test method with LogbackRunLevel.
 */
public enum LogLevel {
    OFF(Level.OFF),
    ERROR(Level.ERROR),
    WARN(Level.WARN),
    INFO(Level.INFO),
    DEBUG(Level.DEBUG),
    TRACE(Level.TRACE),
    ALL(Level.ALL);

    private final Level level;

    /**
     * Constructor for LogLevel receiving Logback level.
     * @param level logback level
     */
    LogLevel(Level level) {
        this.level = level;
    }

    /**
     * Gets the level for logback.
     *
     * @return logback level
     */
    public Level getLevel() {
        return level;
    }
}
