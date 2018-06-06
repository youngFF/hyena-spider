package com.hyena.spider.logger.test;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.junit.Test;

public class MyOwnHyenaLoggerFactoryTest {

    private HyenaLogger logger = HyenaLoggerFactory.getLogger(MyOwnHyenaLoggerFactoryTest.class);



    @Test
    public void ownHyenaLoggerFactoryTest() {
        logger.info("what do you mean");
    }
}
