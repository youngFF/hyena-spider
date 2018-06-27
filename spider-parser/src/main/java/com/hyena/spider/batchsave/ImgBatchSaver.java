package com.hyena.spider.batchsave;


import com.hyena.spider.extrator.ImgExtractor;
import com.hyena.spider.log.logger.HyenaLogMessager;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.jsoup.helper.HttpConnection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 这个类没用上
 */
public class ImgBatchSaver {

    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(ImgBatchSaver.class);


    // 主目录格式 ： /home/{user}
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String REPOSITORY = "hyena-repository";
    // 存放资源的主目录 ：  /home/{user}/hyena-repository
    private static final String REPOSITORY_HOME = USER_HOME + "/" + REPOSITORY;

    public void batchSave(URL url , HttpConnection connection) {
        /**
         * 图片的地址常常是这样的: http://${host}/path/${num}.jpg
         * 我们首先找到最后一个点的位置，注意：要考虑到异常，
         */
        int dotIndex = url.getPath().lastIndexOf(".");

        //如果图片不是这样命名的直接返回
        if (dotIndex == -1) {
            return ;
        }

        String baseImgSrc = url.getHost() + url.getPath().substring(0,dotIndex -1 ) ;

        String suffix = "." +  url.getPath().substring(dotIndex + 1, url.getPath().length());

        //不设置循环结束
        for (int i = 0; ; i++) {
            String imgSrc = baseImgSrc +  + i + suffix;
            System.out.println(imgSrc);
        }

    }



}
