package com.hyena.spider.redisTest;

import com.hyena.spider.redis.connectionUtil.RedisConnection;
import org.junit.Test;

public class EnumInstanceEqualTest {


    /**
     * 枚举类是单例模式
     */
    @Test
    public void enumInstanceEqualTest() {
        RedisConnection.JedisState jedisState = RedisConnection.JedisState.AVAILABLE ;
        RedisConnection.JedisState jedisState1 = RedisConnection.JedisState.AVAILABLE;

        // Enum type cant instantiate
//        RedisConnection.JedisState jedisState2 = new RedisConnection.JedisState();
        System.out.println(jedisState.equals(jedisState1));

    }
}
