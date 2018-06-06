package com.hyena.spider.logger.test;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.junit.Test;

public class LoggerFormatTest {

    private static final HyenaLogger logger = HyenaLoggerFactory.getRootLogger() ;

    @Test
    /**
     * 测试log4j配置输出的格式是否正确
     */
    public void loggerFormatTest() {
        logger.info("this is an info message");
        logger.debug("this is a debug message");
    }
}
