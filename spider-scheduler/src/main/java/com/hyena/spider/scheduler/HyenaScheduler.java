package com.hyena.spider.scheduler;


import com.hyena.spider.configuration.HyenaFrameworkConfiguration;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import com.hyena.spider.work.HyenaWorker;
import redis.clients.jedis.exceptions.JedisConnectionException;

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



    private static final String SINGLE_SITE = HyenaFrameworkConfiguration.getSingleSite() ;
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

        //初始化种子
        initSeeds();

        //装配线程池
        configThreadPool();
    }

    //使用线程池
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, THREAD_MAX_SIZE, 5000,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

    /**
     * 装配hyena-spider框架：
     * 1.进行redis检查
     */
    public static void configHyenaSpiderFrameWork() {
        //进行redis检查：判断redis服务是否启动
        HyenaFrameworkConfiguration.checkHyenaConfig();
        checkRedisAlive();
        logger.info("redis服务已经启动...");

    }

    /**
     * 利用产生的种子，装配线程池
     */
    private static void configThreadPool() {

    }

    public static void initSeeds() {
        HyenaUrlManager manager = new HyenaUrlManager();
        for (String seed : seeds) {
            manager.initSeeds(seed);
        }
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
            seeds.add(SINGLE_SITE);
        } catch (IOException e) {
            logger.warn("读取种子列表过程中出现问题！！");
        }



    }

    /**
     * 框架的执行入口
     */
    public void runSpiderWithThreadPool() {
        for (int i = 0; i < THREAD_MAX_SIZE; i++) {
            threadPoolExecutor.execute(new Task());
        }

        //监控线程池中任务的数量
        new Thread(()->{
            try {
                int task = threadPoolExecutor.getQueue().size();
                //如果线程池中的任务数<10,那么继续添加20个任务
                if (task < 10) {
                    for (int i = 0; i < 20; i++) {
                        threadPoolExecutor.execute(new Task());
                    }
                }
                //睡眠三秒
                Thread.sleep(3000);
            } catch (Exception e) {

            }
        }).start();



    }


    public void runSpiderWithMultiThread() {
        for (int i = 0; i < THREAD_MAX_SIZE; i++) {
            new Thread(()->{
                new Task().run();
            }).start();
        }
    }

    public static ArrayList<String> getSeeds() {
        return seeds;
    }


    public static class Task implements Runnable {
        @Override
        public void run() {
            while (true) {
                new HyenaWorker().run();
            }
        }
    }

    private static void checkRedisAlive() {
        RedisConnection redisConnection = RedisConnectionPool.getConnection();
        testRedisAliveMethod(redisConnection);
    }


    private static void testRedisAliveMethod(RedisConnection redisConnection) {
        //单纯的生成Jedis对象是不会报错的，可以看测试RedisConenction，必须进行实际的redis操作才会
        //报错
        JedisConnectionException e = null;
        /*

       Version 1：
        try {
            redisConnection.getJedisClient().keys("*");
        } catch (JedisConnectionException e1) {
            e = e1 ;
            throw e;

        }
       */

        // 上面的代码，将异常捕获再抛出，没劲!!!!!!!!
        // 运行时异常(RuntimeException)就像人得的内科疾病一样，表面看起来这个人很好，但是其实并不健康
        // 反应到程序上也是一样，运行时异常你看不见也摸不着，你可以选择忽视它，也可以选择处理它（Catch）
        // 但是一旦你对运行时异常选择忽视的话，它就会在你抱着很大期望运行程序的时候给你的心情添堵。

        // 而检查型异常（CheckException），(这个是针对编译器的，也就是说编译器必须对检查型的异常进行检查)，就好比我们平时的
        // 磕碰，缺胳膊短腿，你必须对这种疾病进行处理，不处理的话人就死了！！！！！！！！！！！！这里死了，是说明检查型异常对程序的影响


        // 总的来说：检查型异常就好比“小病小灾”
        //         运行时异常是“大病大灾” ，一旦发现，基本....死了！！！！------程序就吃汉堡了（burger ----> bugger）



        /*
        Version 2 :
            redisConnection.getJedisClient().keys("*");
            因为抛出的是运行时异常，所以不对其进行捕捉，直接有jvm抛出。
            弊端：大多数写代码的时候，我们会忽略运行时异常，这说明在我们的编码中没有这种意识，说明我们的编程手段不够高明。
                 所以，即使是运行时异常，也需要进行捕捉处理。
         */

        /**
         * Version 3 : 对运行时异常进行处理
         */
        try {
            // 用这条语句去测试redis是否运行
            redisConnection.getJedisClient().keys("*");
        } catch (JedisConnectionException e1) {
            //捕获完重新抛出，是为了提醒用户
            logger.fatal("请确定Redis服务是否运行！！！！！");
            // 不改变e1的引用，采用临时引用，将其抛出。 why：试想这样一种情况，当将方法的异常对象的引用抛出的时候，你就对别的方法（也就是直接或间接）
            // 暴露了你当前方法的异常对象的引用。如果其它方法要直接使用异常对象。。。这样想不对，草
            // 因为java是值传递，你将e1抛出，其实在别的方法是一个临时变量，存放e1引用的值，你并没有直接操作异常对象。

            // 下面的思想特别重要： ******请仔细理解，在脑海里面想象你遛狗时的情景：
            //                  当你正常遛狗的时候，狗链在你手中，狗链拴着狗。
            //                  当你只有狗链，狗链没有狗的时候，不好意思，空指针异常(NullPointerException)
            //                  当你没有狗链，让狗“自由飞翔”的时候，不好意思，对象泄露（在c语言中就是内存泄露，因为
            //                  狗不在你的掌控之中，同样有些内存也不在你的掌控之中。）
            //          那么内存泄露怎么办？ 简单，天天看电视，“侧漏不要紧”，就用7度空间，自动吸水（回收无用对象） ----- 引入自动内存管理
            //
            // 究其原因：还是java的“狗链原理” ， 把方法中的异常对象想象成一条狗，如果你直接使用e1引用的话，会存在风险：就是你把你的狗链
            //         拴在了别人家的狗上（你不会是想和小姐姐约炮把-_-!!不存在的），你操纵的就不是原来的异常对象。
            //         所以 e =e1 ,的意思就是我再用一个狗链拴住我的狗，以后我都用e来”遛狗“，这样就不怕再栓错狗了，如果栓错的话，你完全可以
            //         用原来的狗链找回来。


            // 其它看下面这句话的时候，你定要在脑海里想象这种情况！！！！

            // 在这我要引入我对java引用模型的理解：
            //          ”狗与遛狗“模型
            //                      狗链 ------ 引用
            //                      狗   ------ 对象
            //                      遛狗 （让对象活动） ------ 方法调用

            // 以后java中任何关于对象模型，如果想不明白，就把它映射到”狗与遛狗模型“

            // 妈的，实验室就三个人，放假我还在tmd写代码。。。。。Rap ： money in my hand，pussy in my head ！！ yeah
            e = e1 ;
            throw e;
            // 不能在跑出异常的后面打印logger
            // 因为这条语句根本就不能够被执行，这个原则就是编译器优化的一个方面，因为代码根本就不会被执行，所以你写它干啥，你写它干啥，
            // 你写它干啥，我说你！！！
//            logger.info("this is an unreanchable info");
        }finally {
            redisConnection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }
    }
}
