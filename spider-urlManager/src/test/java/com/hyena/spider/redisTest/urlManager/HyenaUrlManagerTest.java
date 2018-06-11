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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        HashMap<String, Integer> hostNamespace = RedisNamespaceDesignator.getHostNamespace();

        Set<Map.Entry<String, Integer>> entries = hostNamespace.entrySet();
        System.out.println();
        System.out.println("----------------------");

        for (Map.Entry entry : entries) {
            System.out.println("host : " + entry.getKey() + "  url nums : " + entry.getValue());
        }
    }

}
