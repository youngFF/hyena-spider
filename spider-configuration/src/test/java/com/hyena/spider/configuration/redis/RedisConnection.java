package com.hyena.spider.configuration.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConnection {



    @Test
    public void redisConnectionTest() {
        Jedis jedis = new Jedis();
        jedis.keys("*");
    }

    @Test
    public  void shouldCatchCheckException() {
        Properties props = new Properties();
        InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream("spider-global.propertie");

        try {
            // do not propagate -- 不传播异常
            props.load(resource);
        } catch (IOException | NullPointerException e) {
            //不应该捕获这个异常，而是要抛出运行时异常
//            e.printStackTrace();
//            System.out.println("can run here??");
            throw new RuntimeException("请检查 " + " 的路径是否正确");
        }

    }
}
