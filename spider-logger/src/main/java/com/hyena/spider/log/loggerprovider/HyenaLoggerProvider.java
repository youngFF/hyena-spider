package com.hyena.spider.log.loggerprovider;

import org.apache.log4j.Logger;


/**
 * 直接与第三方框架交互的类，这个类向上层提供具体的Logger factory
 */
public class HyenaLoggerProvider {


    private static final String DEFAULT_FACTORY = "org.apache.log4j.Logger";


    private static Object logFactory ;




    public static Logger provideRootLogger() {
        return Logger.getRootLogger() ;
    }

    public static Logger provideLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }



}
