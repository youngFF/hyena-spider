package com.hyena.spider.readSeeds;

import com.hyena.spider.scheduler.HyenaScheduler;

/**
 * 这个类是测试HyenaScheduler中爬虫入口方法是否好使
 * 注意：for --> new Thread.start ，和threadPoolExecutor在 @Test方法中不好使,必须在main方法中测试
 */
public class HyenaSchedulerSpiderMethodTest {


    public static void main(String[] args) {
        HyenaScheduler scheduler = new HyenaScheduler();

        scheduler.runSpiderWithMultiThread();
    }
}
