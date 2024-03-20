package test.com.github.spartatech.testutils.logback;

import ch.qos.logback.classic.Level;
import com.github.spartatech.testutils.logback.LogbackLevelChangerExtension;
import com.github.spartatech.testutils.logback.LogbackRunLevel;
import com.github.spartatech.testutils.logback.UnitTestAsserterLogback;
import com.github.spartatech.testutils.logback.constant.LogLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for LogbackLevelChangerExtension.
 *
 * @author Daniel Conde Diehl
 *
 * History:
 *    Sep 06, 2021 - Daniel Conde Diehl
 *
 */
@ExtendWith(LogbackLevelChangerExtension.class)
public class TestLogbackLevelChangerExtension {

    @LogbackRunLevel(newLevel = LogLevel.TRACE, loggerName = "TEST_LOG_NAME")
    @LogbackRunLevel(newLevel = LogLevel.ERROR, loggerName = "TEST_LOG_2")
    @Test
    public void testChangeLogToTrace() {
        UnitTestAsserterLogback asserter1 = new UnitTestAsserterLogback("TEST_LOG_NAME");
        UnitTestAsserterLogback asserter2 = new UnitTestAsserterLogback("TEST_LOG_2");

        asserter1.addExpectation(Level.DEBUG, "should get this log 1");
        asserter1.addExpectation(Level.TRACE, "should get this log 2");
        asserter2.addExpectation(Level.ERROR, "should get this log 3");

        new TestLog().test();

        asserter1.assertLogExpectations(false);
        asserter2.assertLogExpectations(false);
    }

    @LogbackRunLevel(newLevel = LogLevel.WARN, loggerClass = MockClass.class)
    @Test
    public void testChangeByClass() {
        final UnitTestAsserterLogback asserter = new UnitTestAsserterLogback(MockClass.class);

        asserter.addExpectation(Level.WARN, "Should show");

        new MockClass().testLog();

        asserter.assertLogExpectations(false);
    }

    @LogbackRunLevel(newLevel = LogLevel.WARN, loggerClass = MockClass.class)
    @ParameterizedTest
    @CsvSource({
            "true",
            "false"
    })
    void parameterizedTest(boolean showLog) {
        final UnitTestAsserterLogback asserter = new UnitTestAsserterLogback(MockClass.class);

        if (showLog) {
            asserter.addExpectation(Level.WARN, "Should show");
            new MockClass().testLog();
        }

        asserter.assertLogExpectations(false);
    }


    /**
     * Testing inner class to simulate method with logs.
     */
    private static class TestLog {
        private static final Logger log1 = LoggerFactory.getLogger("TEST_LOG_NAME");
        private static final Logger log2 = LoggerFactory.getLogger("TEST_LOG_2");

        public void test() {
            log1.debug("should get this log 1");
            log1.trace("should get this log 2");
            log2.warn("should not get this log");
            log2.error("should get this log 3");
        }
    }
}
