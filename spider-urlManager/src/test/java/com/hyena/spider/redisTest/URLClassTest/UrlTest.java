package com.hyena.spider.redisTest.URLClassTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlTest {


    @Test
    public void urlTest() throws IOException {
        URL url = new URL("http://www.baidu.com/a/c/v");

        System.out.println("authority: " + url.getAuthority());
        System.out.println("content : " + url.getContent());
        System.out.println("default port : " + url.getDefaultPort());
        System.out.println("get file :" + url.getFile());
        System.out.println("host : " + url.getHost());
        System.out.println("path : " + url.getPath());
        System.out.println("protocol : " + url.getProtocol());
        System.out.println("query : " + url.getQuery());
        System.out.println("ref : " + url.getRef());
        System.out.println("user info :  " + url.getUserInfo());
    }
}
