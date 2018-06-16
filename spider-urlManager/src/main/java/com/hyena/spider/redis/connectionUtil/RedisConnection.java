package com.hyena.spider.redis.connectionUtil;

import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import redis.clients.jedis.Jedis;

public class RedisConnection {


    // jedis connection
    private Jedis jedisClient  ;

    // jedis connection state
    // 当在程序代码中使用完连接的时候，要把RedisConnection的jedisConnState的值改为AVILABLE
    private JedisState jedisConnState ;


    // 采用默认的主机和端口号 ,本爬虫框架采用默认构造方法构造redis client对象
    public RedisConnection() {
        this.jedisClient = new Jedis();
        this.jedisConnState = JedisState.AVAILABLE ;
    }

    // TODO
    /*public RedisConnection(String host, String password) {
        this.jedisClient = new Jedis(host,)
    }*/

    public RedisConnection(Jedis jedis) {
        this.jedisClient = jedis ;
        this.jedisConnState = JedisState.AVAILABLE ;
    }

    // 使用redisConnectionConfiguration.properties里面的属性配置redis client对象
    public RedisConnection(RedisConnectionConfigurer configurer) {
        String host = configurer.getHost() ;
        int port = configurer.getPort() ;
        int timeout = configurer.getTimeout() ;

        this.jedisClient = new Jedis(host, port, timeout);
        this.jedisConnState = JedisState.AVAILABLE ;
    }


    // 返回redis client 服务类，就是jedis对象
    public Jedis getJedisClient() {
        return this.jedisClient ;
    }

    // 返回jedis对象的连接状态
    public JedisState getJedisState() {
        return this.jedisConnState ;
    }


    public void setJedisConnState(JedisState jedisConnState) {
        this.jedisConnState = jedisConnState;
    }

    /**
     * 表示jedis链接在连接池中的状态，有两种状态：
     *
     * WORKING 表示这个连接正在工作，
     *
     * AVAILABLE: 标示这个连接现在可用
     */
    public static enum JedisState {


        WORKING ,

        AVAILABLE


    }


}

