package test.com.github.spartatech.testutils.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MockClass {
    private static final Logger log = LoggerFactory.getLogger(MockClass.class);
    
    public void testLog() {
        log.warn("Should show");
        log.debug("Should NOT show");
    }
    
}
