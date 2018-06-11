package com.hyena.spider.redis.url.manager;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.RedisUtil.RedisNamespaceDesignator;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
public class RedisUrlManager {

    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(RedisUrlManager.class);

    private static final int VISITED_SIZE = 1024 * 1024; // 1M

    private static final int TODO_SIZE = 1024 * 1024; // 1M


    // redis中的visited的key
    private static String[] visitedKeys = RedisNamespaceDesignator.provideVisitedKyes();

    // redis中的todo的key
    private static String[] todoKeys = RedisNamespaceDesignator.provideTodoKeys() ;

    // 内存中的visited队列 ,由于对visitedInMemory大部分的操作是查询操作，故使用HashSet效率较高
    private static HashSet<String> visitedInMemory = new HashSet<>(VISITED_SIZE);

    // 内存中的todo队列，由于todo队列就是获取url，也就是从todoInMemory移除首元素。
    // 所以使用LinkedList效率较高
    private static LinkedList<String> todoInMemory = new LinkedList<>();


    /**
     * 在这个方法不进行url格式检查，应该交给上层接口，这个确保传过来的就是形如：http://www.baidu.com
     * 这样的url
     * @param url
     */
    public static void putUrl(String url) {
        if (! visitedInMemory.contains(url)) {
            RedisConnection connection = RedisConnectionPool.getConnection() ;


            // 只要有一个redis visited队列包含这个连接，我们就将这个url废弃
            for (String visitedKey : visitedKeys) {
                if (connection.getJedisClient().sismember(visitedKey, url)) {
                    logger.info(url + " 存在visited队列中，将被废弃");
                    return;
                }
            }


            // 将url添加到 namespace中。
            try {
                //这个url是标准化的url ，形如：http://www.baidu.com
                URL url1 = new URL(url);
                String host = url1.getHost() ;
                connection.getJedisClient().sadd(host, url);

                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(host);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + host );
            } catch (MalformedURLException e) {
                logger.info("url malformed : " + url ) ;
                e.printStackTrace();
            }finally {
                connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
            }
        }

    }


    /**
     * TODO : 怎么获取url是一个问题??????????
     *
     * @return
     */
    public static String getUrl() {

        return null;
    }

}
