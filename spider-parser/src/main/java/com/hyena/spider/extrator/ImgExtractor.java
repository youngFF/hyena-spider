package com.hyena.spider.extrator;

import com.hyena.spider.datastore.ImgDataStore;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgExtractor extends AbstractExtractor {

    private static final String[] IMG_SUFFIX = {"png", "jpg", "jpeg", "gif"};


    @Override
    protected void extractInner(Document document) {
        Elements imgTags = document.getElementsByTag("img");
        forLoopImgTags(imgTags);
    }

    private void forLoopImgTags(Elements imgTags) {
        for (Element img : imgTags) {
            String imgSrc = img.attr("abs:src");

            // 感觉这段代码封装的很好
            if (acceptImg(imgSrc, IMG_SUFFIX)) {
                // 设计自己的图片文件系统坐标，将图片按照自定义的路线放到相应的位置
                ImgDataStore store = new ImgDataStore();
                store.imgStore(imgSrc);
            }
        }
    }


    private boolean acceptImg(String imgSrc, String[] srcFormats) {
        boolean accept = false;

        for (String srcFormat : srcFormats) {
            accept = accept || imgSrc.endsWith(srcFormat);
        }

        return accept;


    }


}
