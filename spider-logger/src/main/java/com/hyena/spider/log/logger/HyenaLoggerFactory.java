package com.hyena.spider.log.logger;


import com.hyena.spider.log.loggerprovider.HyenaLoggerProvider;


/**
 * 对外提供Logger，为了避免与第三方日志框架直接依赖，选择用HyenaLoggerProvider提供logger。
 */
public class HyenaLoggerFactory {

    private static HyenaLoggerProvider hyenaLoggerProvider = new HyenaLoggerProvider() ;


    public static HyenaLogger getLogger(String loggerName) {
        return new HyenaLogger(hyenaLoggerProvider.provideLogger(loggerName));
    }


    public static HyenaLogger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static HyenaLogger getRootLogger() {
        return new HyenaLogger(hyenaLoggerProvider.provideRootLogger());
    }

}
