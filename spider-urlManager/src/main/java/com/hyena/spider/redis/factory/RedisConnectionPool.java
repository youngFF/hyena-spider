package com.hyena.spider.redis.factory;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import com.hyena.spider.redis.connectionUtil.RedisConnection;

import java.util.ArrayList;

public class RedisConnectionPool {


    private static int loopRound = 3 ;

    private static int currentPoolPosition = 0 ;
    private static int redisConnectionCount = Integer.valueOf(RedisConnectionConfigurer
            .getRedisConnectionProperty("redis.connection.count"));

    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(RedisConnectionPool.class);

    // 采用ArrayList来存放连接
    private static ArrayList<RedisConnection> connPool = new ArrayList<>();


    static{
        logger.info("初始化redis连接对象 ，数量：" + redisConnectionCount);
        initConnectionPool();
    }


    private static void initConnectionPool() {
        for (int i = 0; i < redisConnectionCount; i++) {
            connPool.add(new RedisConnection());
        }
    }

    //TODO:接下来要做的是怎么对上层提供RedisConnection 接口

    public  static RedisConnection getConnection() {
        RedisConnection connection = candidateConnection();
        return connection ;
    }


    /**
     * 采用轮训的方式查找可以使用的RedisConnection对象，最后还找不到则返回一个新的
     * 连接对象
     * @return
     */
    private  static RedisConnection candidateConnection() {
        RedisConnection connection = null ;
        for (int i = 0; i < loopRound; i++) {
            // 保存线程池的轮训指针位置，如果到达线程池limit位置，指针重新回到原始位置
            if (currentPoolPosition == connPool.size()) {
                currentPoolPosition = 0;
            }
            for (; currentPoolPosition < connPool.size(); currentPoolPosition++) {
                if ((connection = connPool.get(currentPoolPosition)).getJedisState() == RedisConnection.JedisState.AVAILABLE) {
                    return connection;
                }
            }
        }

        return new RedisConnection();
    }




}




