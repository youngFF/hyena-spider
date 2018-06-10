package com.hyena.spider.redis.factory;

import com.hyena.spider.helper.validate.HyenaValidate;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import com.hyena.spider.redis.connectionUtil.RedisConnection;

import java.util.HashSet;

public class RedisConnectionPool {

    private static int redisConnectionCount = Integer.valueOf(RedisConnectionConfigurer
            .getRedisConnectionProperty("redis.connection.count"));


    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(RedisConnectionPool.class);

    // 采用HashSet来存放连接
    private static HashSet<RedisConnection> connPool = new HashSet<>();


    static{
        logger.info("初始化redis连接对象 ，数量：" + redisConnectionCount);
        initConnectionPool();
    }


    public static void initConnectionPool() {
        for (int i = 0; i < redisConnectionCount; i++) {
            connPool.add(new RedisConnection());
        }
    }

    //TODO:接下来要做的是怎么对上层提供RedisConnection 接口






}




