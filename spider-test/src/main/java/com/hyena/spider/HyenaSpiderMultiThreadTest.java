package com.hyena.spider;

import com.hyena.spider.scheduler.HyenaScheduler;

public class HyenaSpiderMultiThreadTest {


    public static void main(String[] args) {
        HyenaScheduler scheduler = new HyenaScheduler();
        scheduler.runSpiderWithMultiThread();
    }

}
