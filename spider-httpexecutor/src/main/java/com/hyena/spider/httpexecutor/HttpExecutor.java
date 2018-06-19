package com.hyena.spider.httpexecutor;

import com.hyena.spider.connector.factory.HttpConnectionFactory;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.jsoup.Connection;

import java.io.IOException;

public class HttpExecutor implements Executor<Connection.Response> {

    private HyenaLogger logger = HyenaLoggerFactory.getLogger(HttpExecutor.class);

    @Override
    public Connection.Response execute()  {
        Connection.Response response = null ;
        try {
            response = HttpConnectionFactory.getConnection().execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取response对象出错! " );
        }
        return response ;
    }
}
