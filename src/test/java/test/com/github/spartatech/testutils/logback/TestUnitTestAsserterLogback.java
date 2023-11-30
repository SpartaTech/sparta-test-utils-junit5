package test.com.github.spartatech.testutils.logback;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.logback.UnitTestAsserterLogback;
import com.github.spartatech.testutils.logback.constant.ExpectValue;

import ch.qos.logback.classic.Level;

/**
 * Unit tests for The Logback asserter.
 *
 * @author Daniel Conde Diehl
 * History:
 * Dec 29, 2016 - Daniel Conde Diehl
 * Aug 03, 2020 - Daniel Conde Diehl - Porting to Junit5 Jupiter
 * Nov 30, 2023 - Daniel Conde Diehl - adding tests for custom validator
 */
public class TestUnitTestAsserterLogback {

    public static final Logger LOGGER = LoggerFactory.getLogger(TestUnitTestAsserterLogback.class);

    @Test
    public void testLogByClass() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);

        LOGGER.info(message);

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testCustomAsserterBefore() {
        final String message1 = "test message 1 {}";
        final String message2 = "test message 2 {}";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message1, "A");
        spyAppender.addExpectation(Level.ERROR, message2, "B");

        LOGGER.info(message1, "A");
        LOGGER.error(message2, "B");

        spyAppender.assertLogExpectations(msgs -> {
            assertEquals(2, msgs.stream()
                    .map(UnitTestAsserterLogback.LogEntryItem::getMessage)
                    .filter(msg -> msg.startsWith("test message"))
                    .count());

            final var errors = msgs.stream()
                    .filter(msg -> msg.getLevel() == Level.ERROR)
                    .toList();
            assertEquals(1, errors.size());
            assertEquals(message2, errors.get(0).getMessage());
        });

        spyAppender.assertLogExpectations(false);
    }


    @Test
    public void testLogByClassLevelMismatch() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.DEBUG, message);

        LOGGER.info(message);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassNoMessages() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));

    }

    @Test
    public void testLogByClassExtraMessages() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);

        LOGGER.info(message);
        LOGGER.info("other message");

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));

    }

    @Test
    public void testLogByClassDifferentMessage() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);

        LOGGER.info("other message");

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));

    }

    @Test
    public void testLogByClassNotInOrderMessages() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, "other message");
        spyAppender.addExpectation(Level.INFO, message);

        LOGGER.info(message);
        LOGGER.info("other message");

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassInOrderMessages() {
        final String message = "test message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        spyAppender.addExpectation(Level.INFO, "other message");

        LOGGER.info(message);
        LOGGER.info("other message");

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testLogByNameMessages() {
        final String message = "test message";
        final String loggerName = "log-mock";


        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(loggerName);
        spyAppender.addExpectation(Level.INFO, message);
        spyAppender.addExpectation(Level.INFO, "other message");

        final Logger logger = LoggerFactory.getLogger(loggerName);

        logger.info(message);
        logger.info("other message");

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testLogByClassWithCorrectParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);

        LOGGER.info(message, param1, param2);

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testLogByClassWithNullParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, null, param2);

        LOGGER.info(message, param1, param2);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassWithIncorrectLessParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);

        LOGGER.info(message, param1);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassWithIncorrectMoreParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);

        LOGGER.info(message, param1, param2, "bla");

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassWithParametersWrongOrder() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);

        LOGGER.info(message, param2, param1);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    @Test
    public void testLogByClassWithParametersAny() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, ExpectValue.ANY);

        LOGGER.info(message, param1, param2);

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testLogByClassWithParametersNull() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, null);

        LOGGER.info(message, param1, null);

        spyAppender.assertLogExpectations(false);
    }

    @Test
    public void testLogByClassWithParametersNullIncorrect() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);

        LOGGER.info(message, param1, null);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(false));
    }

    /* ********* Test method with ignore == true    ************** */

    @Test
    public void testLogByClassNoMessagesIgnoreExtra() {
        final String message = "teste message";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(true));

    }

    @Test
    public void testLogByClassMessagesMatchOrderedIgnoreExtra() {
        final String message1 = "teste message1";
        final String message2 = "teste message2";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message1);
        spyAppender.addExpectation(Level.WARN, message2);

        LOGGER.info(message1);
        LOGGER.warn(message2);

        spyAppender.assertLogExpectations(true);
    }

    @Test
    public void testLogByClassMessagesMatchNotOrderedIgnoreExtra() {
        final String message1 = "teste message1";
        final String message2 = "teste message2";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message1);
        spyAppender.addExpectation(Level.WARN, message2);

        LOGGER.warn(message2);
        LOGGER.info(message1);

        spyAppender.assertLogExpectations(true);
    }

    @Test
    public void testLogByClassMessagesWithExtraIgnoreExtra() {
        final String message1 = "teste message1";
        final String message2 = "teste message2";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message1);
        spyAppender.addExpectation(Level.WARN, message2);

        LOGGER.warn(message2);
        LOGGER.info(message1);
        LOGGER.error("Extra message");

        spyAppender.assertLogExpectations(true);
    }

    @Test
    public void testLogByClassMessagesWrongLevelIgnoreExtra() {
        final String message1 = "teste message1";
        final String message2 = "teste message2";

        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message1);
        spyAppender.addExpectation(Level.WARN, message2);

        LOGGER.debug(message2);
        LOGGER.info(message1);
        LOGGER.error("Extra message");

        assertThrows(AssertionFailedError.class, () -> spyAppender.assertLogExpectations(true));
    }

}
