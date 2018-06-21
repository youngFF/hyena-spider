package com.hyena.spider;


import com.hyena.spider.extrator.ImgExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 这个测试主要是要抽取出图片的地址
 */
public class ImgExtractorTest {


    private static String host = "http://www.mm131.com" ;

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

}
