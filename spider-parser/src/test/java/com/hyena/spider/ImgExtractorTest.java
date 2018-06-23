package com.hyena.spider;


import com.hyena.spider.extrator.ImgExtractor;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 这个测试主要是要抽取出图片的地址
 */
public class ImgExtractorTest {

    /**
     * 测试列表：
     *  http://www.douban.com
     *  http://www.zhihu.com
     *  http://www.sina.com
     *  http://www.qq.com
     */

    private static String host = "http://www.mm131.com/xinggan/4092.html" ;

    @Test
    public void extractImgSrc() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);

        Elements imgTags = document.getElementsByTag("img");


        for (Element e : imgTags) {
            String src = e.attr("abs:src");
            System.out.println(src);
        }
    }


    // passed !!!!
    @Test
    public void batchSaveImgs() throws IOException {
        Document document = Jsoup.parse(new URL(host), 5000);
        ImgExtractor imgExtractor = new ImgExtractor();
        imgExtractor.extract(document);
    }



    @Test
    public void singlePicDownLoad() throws IOException {
        String url = "http://img1.mm131.me/pic/3142/0.jpg" ;
        HttpConnection connection = (HttpConnection) HttpConnection.connect(url);
        // 必须要带referrer
        connection.referrer("http://www.mm131.com/xinggan/4093.html");
        byte[] imgBytes = connection.execute().bodyAsBytes();

        File img = new File("/home/hyena/1.jpg");
        FileOutputStream fos = new FileOutputStream(img);
        fos.write(imgBytes);
        fos.close();
    }

}
