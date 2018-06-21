package com.hyena.spider.datastore;

import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;

public class UrlDataStore {


    public void urlStore(String url) {
        HyenaUrlManager.putUrl(url);
    }

    public void urlStore(String url, RedisConnection connection) {
        HyenaUrlManager.putUrl(url , connection);
    }

}
