package com.hyena.spider;

import com.hyena.spider.scheduler.HyenaScheduler;

/**
 * 感觉用线程池方法 比 多线程 时间快   more  Faster
 */
public class HyenaSpiderThreadPoolTest {

    public static void main(String[] args) {
        HyenaScheduler scheduler = new HyenaScheduler();
        scheduler.runSpiderWithThreadPool();
    }
}
