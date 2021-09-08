package com.github.spartatech.testutils.logback;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This extension allows use of annotation @LogbackRunLevel annotation to change
 * log level on given logger.
 *
 * @author Daniel Conde Diehl

 * History:
 *      Sep 06, 2021 - Daniel Conde Diehl
 */
public class LogbackLevelChangerExtension implements InvocationInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LogbackLevelChangerExtension.class);

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        log.error("Processing LogbackLevelChangerExtension");

        LogbackRunLevel[] levelsList = new LogbackRunLevel[0];
        final ContainerLogbackRunLevel levels = invocationContext.getExecutable().getAnnotation(ContainerLogbackRunLevel.class);
        if (levels != null) {
            log.trace("Found ContainerLogbackRunLevel");
            levelsList = levels.value();
        } else {
            final LogbackRunLevel level = invocationContext.getExecutable().getAnnotation(LogbackRunLevel.class);
            if (level != null) {
                log.trace("Found LogbackRunLevel single annotation");
                levelsList = new LogbackRunLevel[]{level};
            }
        }
        final Map<ch.qos.logback.classic.Logger, Level> originalLevels = new HashMap<>();
        try {
            for (LogbackRunLevel annLevel : levelsList) {
                final Logger log = getLogger(annLevel);
                if (log instanceof ch.qos.logback.classic.Logger) {
                    final ch.qos.logback.classic.Logger logBackLog = (ch.qos.logback.classic.Logger) log;
                    originalLevels.put(logBackLog, logBackLog.getLevel());
                    logBackLog.setLevel(annLevel.newLevel().getLevel());
                }
            }
            //call real testing method
            invocation.proceed();
        } finally {
            originalLevels.forEach(ch.qos.logback.classic.Logger::setLevel);
        }
    }

    /**
     * Retrieves the logger based upon the annotation.
     * First tries to get by Class, if class is not informed,
     * then gets by String.
     * If none is informed, then throw an exception.
     *
     * @param annotationLevel annotation level to discover logger
     * @return Logger found
     * @throws IllegalArgumentException in case annotation does not have loggerName and loggerClass
     */
    private Logger getLogger(LogbackRunLevel annotationLevel) throws IllegalArgumentException {
        if (annotationLevel.loggerClass() != LogbackRunLevel.NOOP.class) {
            return LoggerFactory.getLogger(annotationLevel.loggerClass());
        } else if (!"".equals(annotationLevel.loggerName())) {
            return LoggerFactory.getLogger(annotationLevel.loggerName());
        } else {
            throw new IllegalArgumentException("Neither loggerClass nor loggerName was provided on LogbackRunLevel.");
        }
    }
}
