package com.github.spartatech.testutils.logback;

import com.github.spartatech.testutils.logback.constant.LogLevel;

import java.lang.annotation.*;

/**
 * This annotation allows you to change log level of a method execution during test to
 * cover scenarios where you have conditional logging.
 *
 * @author Daniel Conde Diehl
 *
 * History:
 *    Sep 06, 2021 - Daniel Conde Diehl
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(ContainerLogbackRunLevel.class)
public @interface LogbackRunLevel {
    /**
     * Logger name used to change log level.
     * Either loggerName or LoggerClass can be used.
     * LoggerName will take precedence if both were provided
     *
     * @return String loggerName
     */
    String loggerName() default "";

    /**
     * Logger class, used to change log level.
     * Either loggerName or LoggerClass can be used.
     * LoggerName will take precedence if both were provided
     *
     * @return Class loggerClass
     */
    Class<?> loggerClass() default NOOP.class;

    /**
     * New level to be applied for the logger.
     *
     * @return LogLevel new level to be applied
     */
    LogLevel newLevel();

    /**
     * Marker class, just used to establish null value for loggerClass option.
     */
    static class NOOP { }
}
