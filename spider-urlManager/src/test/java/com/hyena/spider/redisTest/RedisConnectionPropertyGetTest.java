package com.hyena.spider.redisTest;

import com.hyena.spider.redis.configure.RedisConnectionConfigurer;
import org.junit.Test;

public class RedisConnectionPropertyGetTest {


    @Test
    public void redisConnectionPropertyGetTest() {
        RedisConnectionConfigurer configurer = new RedisConnectionConfigurer();

        String localhost = configurer.getHost();
        System.out.println(localhost);

        String redisConnectionCount = RedisConnectionConfigurer.getRedisConnectionProperty("redis.connection.count");
        System.out.println(redisConnectionCount);
    }



}
