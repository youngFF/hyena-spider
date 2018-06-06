package com.hyena.spider.logger.test;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LoggerFormatTest {

    private static final Logger logger = Logger.getRootLogger() ;

    @Test
    /**
     * 测试log4j配置输出的格式是否正确
     */
    public void loggerFormatTest() {
        logger.info("this is an info message");
        logger.debug("this is a debug message");
    }
}
