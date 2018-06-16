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

public class HyenaUrlManager {

    private static final int VISITED_SIZE = 1024 * 1024; // 1M
    private static final int TODO_SIZE = 1024 * 1024; // 1M
    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaUrlManager.class);
    // redis中的visited的key
    private static String[] visitedKeys = RedisNamespaceDesignator.provideVisitedKeys();

    // redis中的todo的key
    /**
     * WARNING：我觉的redis中的todo队列是设计上的一个冗余：原因是，没有必要，把url从namespace中拿出来
     * 先放到redis中的todo队列，然后再从redis中的todo队列移动到内存中的todo队列。
     * 用老百姓的话说就是：折腾来，折腾去干啥啊？？？有病啊？？有钱啊？？
     */
    private static String[] todoKeys = RedisNamespaceDesignator.provideTodoKeys();

    // 内存中的visited队列 ,由于对visitedInMemory大部分的操作是查询操作，故使用HashSet效率较高
    private static HashSet<String> visitedInMemory = new HashSet<>(VISITED_SIZE);

    // 内存中的todo队列，由于todo队列就是获取url，也就是从todoInMemory移除首元素。
    // 所以使用LinkedList效率较高
    private static LinkedList<String> todoInMemory = new LinkedList<>();


    /**
     * 在这个方法不进行url格式检查，应该交给上层接口，这个确保传过来的就是形如：http://www.baidu.com
     * 这样的url ，注意：这个方法有一个弊端就是如果循环调用putUrl的时候，每一次都要获取一个连接
     * 我推荐使用重载的putUrl方法，
     *
     * @param url
     */
    public static void putUrl(String url) {
        if (!visitedInMemory.contains(url)) {
            RedisConnection connection = RedisConnectionPool.getConnection();


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
                ;
            } finally {
                connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
            }
        }

    }


    /**
     * 这个方法是针对循环调用的情景，只需要重用同一个connection
     *
     * @param url
     * @param connection
     */
    public static void putUrl(String url, RedisConnection connection) {
        if (!visitedInMemory.contains(url)) {

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
                ;

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
                flushToVisited();
            }

            return url;
        } else {
            // todoInMemory队列为空，则从redis中的todo队列中选取url，进行填充
            // 指定填充因子，fullFactor
            fullTodoInMemory(0.75);

            //采用递归，可能会出现问题---
            return getUrl();
        }
    }


    private static void flushToVisited() {
        for (String url : visitedInMemory) {
            // 确定散列数
            int modSize = visitedKeys.length;
            // 取得随机数
            int random = (int) (Math.random() * 10);

            int index = random % modSize;
            // 取得连接
            RedisConnection connection = RedisConnectionPool.getConnection();
            Jedis jedisClient = connection.getJedisClient();
            jedisClient.sadd(visitedKeys[index], url);
            logger.info("flush " + url + " to " + visitedKeys[index]);
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);

        }
        // 清空visitedInMemory队列
        visitedInMemory.clear();
    }

    private static void fullTodoInMemory(double loadFactor) {

        // 缩短循环次数，不一定非得到limit，如果从redis中的namespace取得null对象的次数超过
        // 了30，(30这个数字是我凭感觉想出来的，我也不知道为什么选择它，就感觉这个数中不溜。)
        // 并且todoInMemory大小不为空，则停止fullTodoInMemory
        int nullTimes = 0;

        int limit = (int) (loadFactor * TODO_SIZE);

        RedisConnection redisConnection = RedisConnectionPool.getConnection();
        for (int i = 0; i < limit; i++) {

            // 确定散列数
            int modSize = todoKeys.length;
            // 取得随机数
            int random = (int) (Math.random() * 10);

            int index = random % modSize;

            Object[] hostNames = RedisNamespaceDesignator.getHostNamespace().toArray();

            // 因为jedis.spop的时间复杂度为o(1)，所以使用这个方法 ；这个方法是随机移除Set中的某个元素
            String url = redisConnection.getJedisClient().spop((String) hostNames[index]);

            if (url != null) {
                todoInMemory.add(url);
            } else {
                /**
                 * if (++nullTime)相当于：
                 * nullTimes ++ ;
                 * if ( nullTimes > 30 ...
                 *
                 * ....................
                 *
                 * if(nullTimes ++)相当于：
                 *
                 * if (nullTimes > 30 ...
                 * nullTimes ++ ;
                 */
                if (++nullTimes > 30 && !todoInMemory.isEmpty()) {
                    break;
                }
                //如果返回的url为null，说明这个key即，namespace为空，则删除这个key
                redisConnection.getJedisClient().del((String) hostNames[index]);
                //删除内存中hostNamespace中的host
                RedisNamespaceDesignator.getHostNamespace().remove(hostNames[index]) ;
                //会自动条用hostNames[index].toString，不需要强制转型到String
                logger.info("删除redis命名空间: " + hostNames[index]);
            }
            logger.info("取得url : " + url + " 到内存的todo队列中");
        }
        //归还RedisConnection
        redisConnection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
    }
}
