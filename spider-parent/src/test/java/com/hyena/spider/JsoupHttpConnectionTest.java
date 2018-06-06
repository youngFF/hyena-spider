package com.hyena.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class JsoupHttpConnectionTest {



    @Test
    public void connect() throws IOException {
        // 创建connection
        Connection httpConnection = HttpConnection.connect("http://www.baidu.com");
        httpConnection.header("Connection" , "keepAlive") ;
        // 执行连接，并获得response对象
        // execute方法会抛出各种异常
        // 执行触发器
        long start = System.currentTimeMillis();

        Connection.Response response = httpConnection.execute();

        Map<String, String> headers = response.headers();
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry entry : entries ) {
            System.out.println(entry.getKey()+ " "  +entry.getValue());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start );
        System.out.println("statusCode: " + response.statusCode());
        System.out.println("cookies: " + response.cookies());
        System.out.println("charset: " + response.charset());
        System.out.println("afterSetCharset: " + response.charset("utf-8").charset());
        System.out.println("contentType: "+response.contentType());
     //   System.out.println(response.parse());
        System.out.println(response);
        // 解析器进行解析
        System.out.println(response.parse());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

  //      System.out.println(Jsoup.parse(new URL("https://www.baidu.com/baidu?wd=403&tn=monline_4_dg&ie=utf-8"),5000));
    }
}
