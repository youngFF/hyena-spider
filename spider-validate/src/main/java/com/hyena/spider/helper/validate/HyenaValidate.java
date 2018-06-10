package com.hyena.spider.helper.validate;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;

public class HyenaValidate {

    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaValidate.class);

    // do not instantiate
    private HyenaValidate() { }


    public static void notNull(Object target) {
        notNull(target, false);
    }

    public static void notNull(Object target, boolean isThrow) {
        if (target == null) {
            logger.warn("Should not be null");
            if (isThrow) {
                throw new NullPointerException();
            }
        }
    }
}
