package com.hyena.spider.redisTest.urlManager;

import com.hyena.spider.redis.url.manager.RedisUrlManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class RedisUrlManagerTest {

    private static String host = "http://jd.com" ;
    /**
     * 注意：要安装redis，进行测试
     * @throws IOException
     */
    @Test
    public void addUrlTest() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);
        Elements ahrefs = document.getElementsByTag("a");

        for (Element element : ahrefs) {
            String href = element.attr("abs:href");
            if (href.startsWith("http") || href.startsWith("https"))
                // 放到redis中的应该是进行过正规化处理的url
                RedisUrlManager.putUrl(href);
        }


    }


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
}
