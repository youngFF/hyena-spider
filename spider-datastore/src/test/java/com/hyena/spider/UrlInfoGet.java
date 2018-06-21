package com.hyena.spider;

import com.hyena.spider.filesystem.FileRepository;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class UrlInfoGet {




    @Test
    public void urlInfoGetTest() {
        try {
            URL url = new URL("http://statics.itc.cn/web/static/images/pic/preload_2_1.png");
            System.out.println("get file :" + url.getFile());
            System.out.println("get host : " + url.getHost());
            System.out.println("get path : " + url.getPath());
            System.out.println("get query: " + url.getQuery());
            System.out.println("get ref : " + url.getRef());
            System.out.println("user info : " +url.getUserInfo());
        } catch (Exception e) {

        }
    }


    @Test
    public void makeFileWithDirs() {
        try {
            String host = "http://statics.itc.cn/web/static/images/pic/preload_2_1.png";
            URL url = new URL(host);
            String path = url.getPath() ;

            int pivot = path.lastIndexOf("/");
            System.out.println(pivot);
            System.out.println(path.substring(0, pivot));
            System.out.println(path.substring(pivot+1));



        } catch (Exception e) {

        }
    }
}
