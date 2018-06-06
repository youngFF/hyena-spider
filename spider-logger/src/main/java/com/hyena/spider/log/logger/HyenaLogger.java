package com.hyena.spider.log.logger;

import org.apache.log4j.Logger;


/**
 * 使用自己的logger，避免与特定框架的logger绑定
 */
public class HyenaLogger  implements HyenaLogMessager {


    //这里直接依赖第三方的logger进行日志输出
    private Logger logger ;

    public HyenaLogger(Logger logger) {
        this.logger = logger ;
    }


    @Override
    public void debug(Object message) {
        logger.debug(message);
    }

    @Override
    public void debug(Object message, Throwable t) {
        logger.debug(message, t);
    }

    @Override
    public void info(Object message) {
        logger.info(message);
    }

    @Override
    public void info(Object message, Throwable t) {
        logger.info(message, t);
    }

    @Override
    public void warn(Object message) {
        logger.warn(message);
    }

    @Override
    public void warn(Object message, Throwable t) {
        logger.warn(message, t);
    }

    @Override
    public void error(Object message) {
        logger.error(message);
    }

    @Override
    public void error(Object message, Throwable t) {
        logger.error(message, t);
    }

    @Override
    public void fatal(Object message) {
        logger.fatal(message);
    }

    @Override
    public void fatal(Object message, Throwable t) {
        logger.fatal(message, t);
    }
}
