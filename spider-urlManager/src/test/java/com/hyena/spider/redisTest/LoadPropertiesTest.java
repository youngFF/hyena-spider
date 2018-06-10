package com.hyena.spider.redisTest;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadPropertiesTest {


    @Test
    public void loadProperties() throws IOException {
        Properties props = new Properties();

        // :为什么会出现resourceAsStream为null 的情况
        // 解决方法：将conf文件夹标记为resource
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().
                getResourceAsStream("redisConnectionConfiguration.properties");

        System.out.println(resourceAsStream);
        props.load(resourceAsStream);

        String property = props.getProperty("redis.host");

        System.out.println(property);
    }
}
