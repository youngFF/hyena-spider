package com.hyena.spider.connector.factory;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

public class HttpConnectionFactory {

    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HttpConnectionFactory.class);


    public static Connection getConnection() {
        // 使用HyenaUrlManager获取url
        String url = HyenaUrlManager.getUrl();
        logger.info("解析url :  " + url);
        //HttpConnection.connect(url) 会抛出运行时异常
        return HttpConnection.connect(url);
    }


}
