package com.hyena.spider.redisTest.urlManager;

import com.hyena.spider.redis.RedisUtil.RedisNamespaceDesignator;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HyenaUrlManagerTest {

    /**
     * 测试用例：
     *  http://www.baidu.com
     *  http://www.sohu.com
     *  http://www.douban.com
     *  http://www.dangdang.com
     *  http://www.qq.com/
     */
    private static String host = "http://www.qq.com";




    @Test
    public void parseUrl() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);
        Elements ahrefs = document.getElementsByTag("a");

        for (Element element : ahrefs) {
            String href = element.attr("abs:href");
            if (href.startsWith("http") || href.startsWith("https"))
                System.out.println(href);
        }


    }

    /**
     * 注意：要安装redis，进行测试
     * @throws IOException
     */
    @Test
    public void addUrlTest() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);
        Elements ahrefs = document.getElementsByTag("a");
        RedisConnection connection = RedisConnectionPool.getConnection();

        for (Element element : ahrefs) {
            String href = element.attr("abs:href");
            if (href.startsWith("http") || href.startsWith("https"))
                // 放到redis中的应该是进行过正规化处理的url
                System.out.println(connection);
                HyenaUrlManager.putUrl(href ,connection);
        }

        HashSet<String> hostNamespace = RedisNamespaceDesignator.getHostNamespace();


        System.out.println();
        System.out.println("----------------------");

        String host =null ;
        for (Iterator<String> it = hostNamespace.iterator(); it.hasNext(); ) {
            host  = it.next();
            System.out.println("host : " + host);
        }
    }



    @Test
    public void addUrlToRemoteRedisTest() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);
        Elements ahrefs = document.getElementsByTag("a");
        JedisShardInfo info = new JedisShardInfo("47.104.79.16", 56379, 3000);
        info.setPassword("__yanghe@0510");
        Jedis jedis = new Jedis(info);
        for (Element element : ahrefs) {
            String href = element.attr("abs:href");
            if (href.startsWith("http") || href.startsWith("https"))
                // 放到redis中的应该是进行过正规化处理的url
                System.out.println(jedis);
            HyenaUrlManager.putUrl(href ,new RedisConnection(jedis));
        }

        HashSet<String> hostNamespace = RedisNamespaceDesignator.getHostNamespace();


        System.out.println();
        System.out.println("----------------------");

        /**
         * you can iterator hostNamespace here
         */
        for (String s : hostNamespace) {
            System.out.println("host : " + s );
        }
    }

}
