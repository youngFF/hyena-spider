package com.hyena.spider;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpClientConnectionTest {


    @Test
    public void test() throws IOException {

        // connector
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod("https://www.baidu.com/baidu?wd=403&tn=monline_4_dg&ie=utf-8");
        int status = httpClient.executeMethod(method);

        System.out.println("status : " + status);


        // downloader
        InputStream stream = method.getResponseBodyAsStream();

        System.out.println("charset: " + ((GetMethod) method).getRequestCharSet());
        System.out.println("path: " + method.getPath());
        System.out.println("url: " + method.getURI());
        // parser
        Document doc = Jsoup.parse(stream, "utf-8", method.getURI().toString());

        System.out.println(doc);
    }
}
