package com.hyena.spider.scheduler;


import com.hyena.spider.configuration.HyenaFrameworkConfiguration;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 怎么去写这个HyenaScheduler???
 */
public class HyenaScheduler {


    // 最大线程数
    private static final int THREAD_MAX_SIZE = HyenaFrameworkConfiguration.getMaxThreadCount();
    // logger
    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaScheduler.class);
    private static ArrayList<String> seeds = new ArrayList<>();

    static {
        //进行redis服务检查
        configHyenaSpiderFrameWork();
        //读取种子列表
        readSeeds();
        //装配线程池
        configThreadPool();
    }

    //使用线程池
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, THREAD_MAX_SIZE, 5000,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

    /**
     * 装配hyena-spider框架：
     * 1.进行redis检查
     */
    public static void configHyenaSpiderFrameWork() {
        //进行redis检查：判断redis服务是否启动
        HyenaFrameworkConfiguration.checkHyenaConfig();


    }

    /**
     * 利用产生的种子，装配线程池
     */
    private static void configThreadPool() {

    }

    public static void readSeeds() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("seeds");
        BufferedReader seedsReader = new BufferedReader(new InputStreamReader(inputStream));


        String seed = null;


        try {
            while ((seed = seedsReader.readLine()) != null) {
                logger.info("种子: " + seed);
                seeds.add(seed);
            }
        } catch (IOException e) {
            logger.warn("读取种子列表过程中出现问题！！");
        }



    }

    /**
     * 框架的执行入口
     */
    public void runSpider() {

    }

    public static ArrayList<String> getSeeds() {
        return seeds;
    }

}
