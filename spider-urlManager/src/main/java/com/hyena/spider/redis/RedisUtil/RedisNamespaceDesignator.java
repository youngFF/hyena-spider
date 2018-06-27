package com.hyena.spider.redis.RedisUtil;


import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import com.hyena.spider.redis.connectionUtil.RedisConnection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 管理redis中，visited，todo，namespace，的名字，
 * 也就是key的名称。
 * <p>
 * redis中的相应的key对应的value(类型为Set)初始化工作交给，spider-configuration模块
 * 完成
 * <p>
 * 管理redis当中的url的namespace及相应下面的url数量，例如：
 * <p>
 * http://www.baidu.com  10000
 * http://www.
 */
public class RedisNamespaceDesignator {

    private static String visitedKey = RedisConnectionConfigurer.
            getRedisConnectionProperty("redis.visited.name");

    private static String namespaceKey = RedisConnectionConfigurer.getRedisConnectionProperty("redis.namespace");
    /**
     * 记录以解析的namespace，如果hostNamespace的数量为1，说明这是一个定向爬虫，爬去特定的站点。
     * 这里面存放的都是网站的地址： 且不带www. ，比如www.douban.com，在hostNamespace存放的就是douban.com
     */
    private static HashSet<String> hostNamespace = new HashSet<String>();

    static {
        // 这里就不使用连接池中的连接了
        RedisConnection redisConnection = new RedisConnection();
        // host namespace in redis
        Set<String> sites = redisConnection.getJedisClient().smembers(RedisConnectionConfigurer.
                getRedisConnectionProperty("redis.namespace"));

        for (String key : sites) {
            hostNamespace.add(key);
        }

    }

    /**
     * 提供visited命名空间
     *
     * @return
     */
    public static String provideVisitedKey() {
        return visitedKey;
    }




    /**
     * 提供url命令空间，即namespace
     * @return
     */
    public static HashSet<String> getHostNamespace() {
        return hostNamespace ;
    }


    public static String getNamespaceKey() {
        return namespaceKey ;
    }
    /**
     * add操作需要同步
     * @param host
     */
    public synchronized  static void updateHostNamespace(String host) {
        hostNamespace.add(host);
    }
}
