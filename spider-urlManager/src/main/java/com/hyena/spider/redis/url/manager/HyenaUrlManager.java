package com.hyena.spider.redis.url.manager;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.RedisUtil.RedisNamespaceDesignator;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import redis.clients.jedis.Jedis;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * 将redis中的todo队列删除，因为我觉得这是一个冗余的设计
 */
public class HyenaUrlManager {


    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaUrlManager.class);
    // redis中的visited的key
    private static String visitedKey = RedisNamespaceDesignator.provideVisitedKey();

    // redis中的todo的key
    /**
     * WARNING：我觉的redis中的todo队列是设计上的一个冗余：原因是，没有必要，把url从namespace中拿出来
     * 先放到redis中的todo队列，然后再从redis中的todo队列移动到内存中的todo队列。
     * 用老百姓的话说就是：折腾来，折腾去干啥啊？？？有病啊？？有钱啊？？
     */





    /**
     * 在这个方法不进行url格式检查，应该交给上层接口，这个确保传过来的就是形如：http://www.baidu.com
     * 这样的url ，注意：这个方法有一个弊端就是如果循环调用putUrl的时候，每一次都要获取一个连接
     * 我推荐使用重载的putUrl方法，
     *
     * @param url
     */
    public static void putUrl(String url) {

            RedisConnection connection = RedisConnectionPool.getConnection();

            // 只要有一个redis visited队列包含这个连接，我们就将这个url废弃

                if (connection.getJedisClient().sismember(visitedKey, url)) {
                    logger.info(url + " 存在visited队列中，将被废弃");
                    return;
                }


            // 将url添加到 namespace中。
            try {
                //这个url是标准化的url ，形如：http://www.baidu.com
                URL url1 = new URL(url);
                //找到这个url的hostOwner
                String hostOwner = getHostOwner(url1.getHost());


                connection.getJedisClient().sadd(hostOwner, url);

                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(hostOwner);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + hostOwner);
            } catch (MalformedURLException e) {
                //利用重定向技术，将异常打印到日志文件中
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                e.printStackTrace(pw);
                logger.error(writer.toString());
            } finally {
                connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
            }


    }


    /**
     * 这个方法是针对循环调用的情景，只需要重用同一个connection
     *
     * @param url
     * @param connection
     */
    public  static void putUrl(String url, RedisConnection connection) {
        if (!visitedInMemory.contains(url)) {

            // 只要有一个redis visited队列包含这个连接，我们就将这个url废弃
            for (String visitedKey : visitedKey) {
                if (connection.getJedisClient().sismember(visitedKey, url)) {
                    logger.info(url + " 存在visited队列中，将被废弃");
                    return;
                }
            }


            // 将url添加到 namespace中。
            try {
                //这个url是标准化的url ，形如：http://www.baidu.com
                URL url1 = new URL(url);
                String host = url1.getHost();
                connection.getJedisClient().sadd(host, url);

                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(host);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + host);
            } catch (MalformedURLException e) {
                //利用重定向技术，将异常打印到日志文件中
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                e.printStackTrace(pw);
                logger.error(writer.toString());


            } finally {
                // 向RedisConnectionPool归还RedisConnection
                connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
            }
        }

    }

    /**
     * TODO : 怎么获取url是一个问题??????????
     * 这个方法耗费了我很长的时间。。。。。。。。。。。。。。-_-!!!
     *
     * @return
     */
    public static String getUrl() {
        // 如果todoInMemory队列不为空，则返回队列首的url
        if (todoInMemory.size() != 0) {
            String url = todoInMemory.removeFirst();
            logger.info("取得url ： " + url);
            // 如果visited队列已经满了，则将visited队列中的url散列到v1，v2，v3中：采用随机数散列
            if (visitedInMemory.size() == VISITED_SIZE) {
                flushToRedisVisited();
            }
            //添加到visitedInMemory
            visitedInMemory.add(url);

            return url;
        } else {
            // todoInMemory队列为空，则从redis中的todo队列中选取url，进行填充
            // 指定填充因子，fullFactor
            fullTodoInMemory(0.75);

            //采用递归，可能会出现问题---
            return getUrl();
        }
    }


    /**
     * 找到这个url的hostOwner,如果找不到，那么自己就是一个host
     * @param url
     * @return
     */
    public static String getHostOwner(String url) {
        HashSet<String> hostNamespace = RedisNamespaceDesignator.getHostNamespace();

        for (String host : hostNamespace) {
            if (url.contains(host)) {
                return host ;
            }
        }

        return url ;
    }
}
