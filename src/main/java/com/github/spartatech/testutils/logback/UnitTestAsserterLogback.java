package com.github.spartatech.testutils.logback;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.opentest4j.AssertionFailedError;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.logback.constant.ExpectValue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;


/** 
 * 
 * Logback utility to spy logs calls
 * The way it works is:
 * - Instantiate a new {@link UnitTestAsserterLogback} giving the logger to be spied.
 * - Declare all your expectations using addExpectation
 * - call method to be tested
 * - call {@code UnitTestAsserterLogback.assertLogExpectations()}
 * 
 * @author Daniel Conde Diehl
 * History: 
 *    Dec 27, 2016 - Daniel Conde Diehl
 *    Aug 03, 2020 - Daniel Conde Diehl - Porting to Junit5 Jupiter
 *    Nov 30, 2023 - Daniel Conde Diehl - Removing deprecated method, adding lambda expectation
 *  
 */
public class UnitTestAsserterLogback  {

    private final LinkedList<LogEntryItem> expectations = new LinkedList<>();
    private final LinkedList<ILoggingEvent> events = new LinkedList<>();
    
    private final UnitTestAsserterLogbackAppender appender;
    
    
    /**
     * Constructor receiving the logger as a String.
     * @param logger name as a String
     */
    public UnitTestAsserterLogback(String logger) {
        appender = new UnitTestAsserterLogbackAppender(logger, events);
        attachAppenderToLogback();
    }
    
    /**
     * Constructor receiving the logger as a class.
     * @param clazz Class that will be used as a logger name
     */
    public UnitTestAsserterLogback(Class<?> clazz) {
        appender = new UnitTestAsserterLogbackAppender(clazz, events);
        attachAppenderToLogback();
    }
    
    /**
     * Adds a new expectation to the logger. 
     * 
     * @param level expected for the log entry
     * @param logMessage message expected for the log entry
     * @param params list of parameters for the log entry.
     */
    public void addExpectation(Level level, String logMessage, Object...params) {
        expectations.add(new LogEntryItem(level, logMessage, params));
    }

    
    /**
     * Replay expectations to check if all logs happened.
     * Analyzes in order and all logs supposed to be there 
     * 
     * @param ignoreExtraMessages false - if any message other than ones expected happens it fail, also check in order, 
     * 							  true - ensure that messages that were expected happens, allows extra messages and does not check 
     * @throws AssertionError Throws an assertion error when the asserts fail
     */
	public void assertLogExpectations(boolean ignoreExtraMessages) throws AssertionError {
    	if (!ignoreExtraMessages) {
            if (events.size() != expectations.size()) {
                throw new AssertionFailedError("Invalid number of messages", String.valueOf(expectations.size()), String.valueOf(events.size()));
            }
            
            for (ILoggingEvent event : events) {
                final LogEntryItem entry = expectations.remove();

                if (!entry.getMessage().equals(event.getMessage())) {
                    throw new AssertionFailedError("Message mismatch", entry.getMessage(), event.getMessage());
                }
                
                compareEntries(event, entry);
            }
    	} else {
    		for (LogEntryItem entry : expectations) {
    			boolean foundMatch = false;
    			for(ILoggingEvent event : events) {
    				if (entry.getMessage().equals(event.getMessage())) {
    					try {
    						compareEntries(event, entry);
    						foundMatch = true;
    						break;
    					} catch (AssertionFailedError e) {
    						//Error happened not a match
    					}
    				}
    			}
    			if (!foundMatch) {
    				throw new AssertionFailedError("Message ["+entry + "] not found");
    			}
            }
    	}
    }

    /**
     * Custom asserter, delegate the validation to all messages to a provided supplier.
     * Can be used for more complex validations, also can be used in conjunction with
     * normal asserter.
     *
     * @param customEvaluator supplier that will receive all the messages to evaluate
     * @throws AssertionError in case a validation fails
     */
    public void assertLogExpectations(Consumer<List<LogEntryItem>> customEvaluator) throws AssertionError {
        final List<LogEntryItem> messages = events.stream()
                .map(event -> new LogEntryItem(event.getLevel(), event.getMessage(), event.getArgumentArray()))
                .toList();

        customEvaluator.accept(messages);
    }

    /**
     * Compares an expected entry with a logging event, checking if the level, and param match.
     * 
     * @param event Log event that happened
     * @param entry expected entry
     */
    private void compareEntries (ILoggingEvent event, LogEntryItem entry) {
        if (entry.getLevel() != event.getLevel()) {
            throw new AssertionFailedError("LogLevel mismatch", entry.getLevel().toString(), event.getLevel().toString());
        }
        
        int expectedSize = entry.getParams() == null ? 0 : entry.getParams().length;
        int actualSize = event.getArgumentArray() == null ? 0 : event.getArgumentArray().length;
        if (expectedSize != actualSize) {
            throw new AssertionFailedError("Incorrect number of params", 
                                        entry.getParams()== null ? "0" : String.valueOf(entry.getParams().length) , 
                                        event.getArgumentArray() == null ? "0" : String.valueOf(event.getArgumentArray().length));
        }

        for (int i = 0; i < entry.getParams().length; i++) {
            Object expectedParam = entry.getParams()[i];
            Object actualParam = event.getArgumentArray()[i];

            if (ExpectValue.ANY == expectedParam) {
                continue;
            }

            if (expectedParam == null && actualParam != null) {
                throw new AssertionFailedError("Param [" + i + "] mismatch", "null", actualParam.toString());
            } else if (expectedParam != null && !expectedParam.equals(actualParam)) {
                throw new AssertionFailedError("Param [" + i + "] mismatch", expectedParam.toString(), actualParam == null ? "NULL" : actualParam.toString());
            }
        }
    }
    
    /**
     * Attached the log to the logback. 
     */
    private void attachAppenderToLogback() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(appender);
    }
    
    /** 
     * 
     * Internal VO to carry log entries for asserting values.
     * 
     * @author Daniel Conde Diehl
     * History: 
     *    Jan 15, 2017 - Daniel Conde Diehl
     *  
     */
    public static class LogEntryItem {
        private final Level level;
        private final String message;
        private final Object[] params;

        /**
         * Constructor with all values.
         *
         * @param level   log level for the message
         * @param message text message
         * @param params  params used in the log
         */
        public LogEntryItem(Level level, String message, Object[] params) {
            this.level = level;
            this.message = message;
            this.params = params;
        }
        
        /**
         * @return the level
         */
        public Level getLevel() {
            return level;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }
        /**
         * @return the params
         */
        public Object[] getParams() {
            return params;
        }

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
            return "[level=" + level +
                    ", message=" + message +
                    ", params=" + Arrays.toString(params) +
                    "]";
		}
    }
}
