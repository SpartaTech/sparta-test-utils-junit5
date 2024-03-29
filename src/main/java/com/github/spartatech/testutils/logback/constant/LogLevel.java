package com.github.spartatech.testutils.logback.constant;

import ch.qos.logback.classic.Level;

/**
 * Log Level to be used while annotating a test method with LogbackRunLevel.
 */
public enum LogLevel {
    /** Log OFF */
    OFF(Level.OFF),
    /** Log Level ERROR */
    ERROR(Level.ERROR),
    /** Log Level WARN */
    WARN(Level.WARN),
    /** Log Level INFO */
    INFO(Level.INFO),
    /** Log Level DEBUG */
    DEBUG(Level.DEBUG),
    /** Log Level TRACE */
    TRACE(Level.TRACE),
    /** Log ALL */
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
