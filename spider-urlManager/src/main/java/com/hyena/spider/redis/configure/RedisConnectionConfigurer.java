package com.hyena.spider.redis.configure;

import com.hyena.spider.redis.RedisUtil.RedisPropsLoader;

public class RedisConnectionConfigurer {


    // DEFAULT HOST , PORT , TIMEOUT OF REDIS CONNECTION
    private static final String DEFAULT_HOST = "127.0.0.1";

    private static final int DEFAULT_PORT = 6379;

    private static final int DEFAULT_TIMEOUT = 3000; // 3s



    public RedisConnectionConfigurer() {

    }


    // 如果属性表中的timeout，host，port值为null ， 则使用默认值
    public int getTimeout() {
        return RedisPropsLoader.getRedisConnProperty("redis.timeout") !=null ?
              Integer.valueOf(RedisPropsLoader.getRedisConnProperty("redis.timeout")) : DEFAULT_TIMEOUT ;
    }


    /**
     * 如果override为true，那么方法将返回你指定的值，而不是Configurer中默认的值
     * @param overrideProperty
     * @param ownTimeout
     * @return
     */
    public int getDefaultTimeout(boolean overrideProperty, int ownTimeout) {

        return overrideProperty ? ownTimeout : DEFAULT_TIMEOUT;
    }

    public String getHost() {
        return RedisPropsLoader.getRedisConnProperty("redis.host") !=null
                ? RedisPropsLoader.getRedisConnProperty("redis.host") : DEFAULT_HOST;
    }

    public String getDefaultHost(boolean overrideProperty , String ownHost) {
        return overrideProperty ? ownHost : DEFAULT_HOST ;
    }


    public int getPort() {
        return RedisPropsLoader.getRedisConnProperty("redis.port") !=null
                ? Integer.valueOf(RedisPropsLoader.getRedisConnProperty("redis.host")) : DEFAULT_PORT;
    }

    public int getDefaultPort(boolean overrideProperty , int ownPort) {
        return overrideProperty ? ownPort : DEFAULT_PORT ;
    }


}
