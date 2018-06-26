package com.hyena.spider.readSeeds;

import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import org.junit.Test;

public class GetUrlTest {


    @Test
    public void getUrl() {
        String url = HyenaUrlManager.getUrl();
        System.out.println(url);
    }
}
