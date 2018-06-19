package com.hyena.spider.connector;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

public class JsoupHttpConnection {




    @Test
    public void generateConnectionObject() {
        String host = "http://www.zhihu.com";
        Connection httpConnection = HttpConnection.connect(host);
        Connection.Response response = null ;
        Document document = null ;
        try {
            response = httpConnection.execute();
            document = response.parse();
        } catch (IOException e) {
        }

        Elements aTags = document.getElementsByTag("a");
        for (Element tag : aTags) {
            System.out.println(tag.attr("abs:href"));
        }
    }


    @Test
    public void jitTest() throws ClassNotFoundException {
        // 一定会初始化
        Class.forName("java.lang.Compiler" , true , ClassLoader.getSystemClassLoader());
        System.out.println(System.getProperty("java.vm.info"));
    }
}
