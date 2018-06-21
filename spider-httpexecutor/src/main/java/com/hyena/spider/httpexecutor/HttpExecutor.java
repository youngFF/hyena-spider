package com.hyena.spider.httpexecutor;

import com.hyena.spider.connector.factory.HttpConnectionFactory;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HttpExecutor implements Executor<Document> {

    private HyenaLogger logger = HyenaLoggerFactory.getLogger(HttpExecutor.class);

    /**
     * 注意返回值可能为null
     * @return
     */
    @Override
    public Document execute()  {
        Connection.Response response = null;
        Document document = null ;
        try {
            //采用对象配置的链式法则，可以方便对象的配置。
            response = HttpConnectionFactory.getConnection().method(Connection.Method.GET).execute();
            return response.parse() ;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取document对象出错! " );
        }
        return document ;
     }
}
