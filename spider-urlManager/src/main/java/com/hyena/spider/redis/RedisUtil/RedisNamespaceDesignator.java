package com.hyena.spider.redis.RedisUtil;


import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import com.hyena.spider.redis.connectionUtil.RedisConnection;

import java.util.HashMap;

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

    private static String[] visitedKeys = RedisConnectionConfigurer.
            getRedisConnectionProperty("redis.visited.name").split(",");

    private static String[] todoKeys = RedisConnectionConfigurer.
            getRedisConnectionProperty("redis.todo.name").split(",");


    /**
     * 记录Redis中namespace的key和其对应的Set中元素的数量
     */
    private static HashMap<String, Integer> hostNamespace = new HashMap<>();


    static {
        // 这里就不使用连接池中的连接了
        RedisConnection redisConnection = new RedisConnection();
        //TODO:从redis中读取namespace以及对应的url数量。
    }

    /**
     * 提供visited命名空间
     *
     * @return
     */
    public static String[] provideVisitedKeys() {
        return visitedKeys;
    }


    public static String[] provideTodoKeys() {
        return todoKeys;
    }


    public static void updateHostNamespace(String host) {
        Integer old = hostNamespace.get(host);
        if (old == null) {
            hostNamespace.put(host, 1);
        } else {
            hostNamespace.put(host, 1 + old);
        }
    }

    public static HashMap<String , Integer> getHostNamespace() {
        return hostNamespace ;
    }
}
