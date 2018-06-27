package com.hyena.spider.imgthreadpooldownload;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImgThreadPoolDownLoader {


    private static ThreadPoolExecutor imgExecutor = new ThreadPoolExecutor(50, 100, 5000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>());


    public synchronized  static void addImgDownloadTask(Runnable task) {
        imgExecutor.execute(task);
    }

}
