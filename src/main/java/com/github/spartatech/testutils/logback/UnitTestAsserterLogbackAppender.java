package com.github.spartatech.testutils.logback;

import java.util.LinkedList;
import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;


/** 
 * 
 * Logback Appender that receives log calls and adds to event list. Used to tap into the logs and asserts then.
 * 
 * @author Daniel Conde Diehl
 * 
 * History: 
 *    Dec 27, 2016 - Daniel Conde Diehl
 *  
 */
class UnitTestAsserterLogbackAppender implements Appender<ILoggingEvent> {

    /**
     * Link for the Events where we are going to write.
     */
    private LinkedList<ILoggingEvent> events;
    
    /**
     * Log being spied
     */
    private String logger;
    
    /**
     * Constructor receiving the logger as a String.
     * @param logger name as a String
     * @param eventList list of events to write to
     */
    public UnitTestAsserterLogbackAppender(String logger, LinkedList<ILoggingEvent> eventList) {
        this.logger = logger;
        events = eventList;
    }
    
    /**
     * Constructor receiving the logger as a class.
     * @param clazz Class that will be used as a logger name
     * @param eventList list of events to write to
     */
    public UnitTestAsserterLogbackAppender(Class<?> clazz, LinkedList<ILoggingEvent> eventList) {
        this(clazz.getName(), eventList);
    }
    
    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        if (!event.getLoggerName().equals(logger)) return;
        events.add(event);
    }
       
    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public void setContext(Context context) {
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void addStatus(Status status) {
    }

    @Override
    public void addInfo(String msg) {
    }

    @Override
    public void addInfo(String msg, Throwable ex) {
    }

    @Override
    public void addWarn(String msg) {
    }

    @Override
    public void addWarn(String msg, Throwable ex) {
    }

    @Override
    public void addError(String msg) {
    }

    @Override
    public void addError(String msg, Throwable ex) {
    }

    @Override
    public void addFilter(Filter<ILoggingEvent> newFilter) {
    }

    @Override
    public void clearAllFilters() {
    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        return null;
    }

    @Override
    public FilterReply getFilterChainDecision(ILoggingEvent event) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

};
