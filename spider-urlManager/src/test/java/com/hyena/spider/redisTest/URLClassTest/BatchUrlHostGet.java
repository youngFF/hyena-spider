package com.hyena.spider.redisTest.URLClassTest;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class BatchUrlHostGet {




    @Test
    public void batchURLHostGet() throws MalformedURLException {
        String[] urls = {"http://www.baidu.com/a/bc", "http://www.douban.com/category"
                , "http://www.netease.com/p.html"};


        for (String s : urls) {

            System.out.println(new URL(s).getHost());
        }
    }
}
