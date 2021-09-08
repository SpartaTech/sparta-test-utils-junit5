package com.github.spartatech.testutils.logback;

import java.lang.annotation.*;

/**
 * Annotation that allows repeatable Logback runlevel annotations in a method.
 *
 * @author Daniel Conde Diehl
 *
 * History:
 *    Sep 06, 2021 - Daniel Conde Diehl
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ContainerLogbackRunLevel {
    /**
     * List of logBackRunLevel to allow repeatable annotation.
     *
     * @return Array of {@code LogbackRunLevel}
     */
    LogbackRunLevel[] value();
}
