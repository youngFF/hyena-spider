package com.hyena.spider.redis.connectionUtil;

import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import redis.clients.jedis.JedisShardInfo;

public class RedisConnectionTypeCreator {


    private static RedisConnectionConfigurer connectionConfigurer = new RedisConnectionConfigurer();

    private static String host =  connectionConfigurer.getHost();

    private static int port = connectionConfigurer.getPort();

    private static String password = RedisConnectionConfigurer.getRedisConnectionProperty("redis.password");

    private static JedisShardInfo infoBall = new JedisShardInfo(host, port);

    static {
        infoBall.setPassword(password);
    }

    public static RedisConnection getRedisConnection(String connectionType) {
        if (connectionType.equals("default")) {
            return new RedisConnection();
        } else {
            return new RedisConnection(infoBall);
        }

    }

}
