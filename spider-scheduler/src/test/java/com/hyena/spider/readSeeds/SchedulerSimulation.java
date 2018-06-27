package com.hyena.spider.readSeeds;

import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import com.hyena.spider.work.HyenaWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SchedulerSimulation {



    /**
     * 用这个方法模拟爬取单个站点的情况
     * @throws IOException
     */
    @Test
    public void simulateSpiderScheduler() throws IOException {
        // single site
//        String seed = "http://www.douban.com";
//        ArrayList<String> urls = extractUrl(seed);
//
//
//        RedisConnection connection = new RedisConnection();
//
//        for (String url : urls) {
//            HyenaUrlManager.putUrl(url, connection);
//        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20));
        //装配线程池
        for (int i = 0; i < 5; i++) {
            HyenaWorker worker = new HyenaWorker();
            System.out.println("添加worker ：　" + i);
            executor.execute(worker);
        }


       /* new Thread(()->{
            //等待的任务数
            int size = executor.getQueue().size();

            while (true) {
                //如果等待的任务数量小于10的话，那么添加40个
                if (size < 10) {
                    for (int i = 0; i < 40; i++) {
                        executor.execute(new HyenaWorker());
                    }
                    System.out.println("添加40个任务");
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



        }).start();
*/
//        connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
    }






    public ArrayList<String> extractUrl(String seed) throws IOException {
        Document document = Jsoup.parse(new URL(seed), 5000);

        ArrayList<String> seeds = new ArrayList<>();

        Elements elements = document.getElementsByTag("a");


        RedisConnection connection = RedisConnectionPool.getConnection();
        String href = null ;
        for (Element url : elements) {
            href = url.attr("abs:href");

            if (href != null && !href.equals("")) {
                HyenaUrlManager.putUrl(href , connection);
            }
        }

        return seeds ;
    }


    @Test
    public void firstParseSinleSite() throws IOException {
        extractUrl("http://www.jd.com");
    }


    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                try {
                    test();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    // passed
    @Test
    public static void test() throws ClassNotFoundException {
        Class.forName("com.hyena.spider.scheduler.HyenaScheduler");
        while (true) {
            new HyenaWorker().run();
        }
    }
}
