package com.hyena.spider.readSeeds;

import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import com.hyena.spider.work.HyenaWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

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

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 3000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(50));
        //装配线程池
        for (int i = 0; i < 50; i++) {
            System.out.println("添加worker ：　" + i);
            executor.execute(new HyenaWorker());
        }


        new Thread(()->{
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

//        connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
    }






    public ArrayList<String> extractUrl(String seed) throws IOException {
        Document document = Jsoup.parse(new URL(seed), 5000);

        ArrayList<String> seeds = new ArrayList<>();

        Elements elements = document.getElementsByTag("a");


        String href = null ;
        for (Element url : elements) {
            href = url.attr("abs:href");

            if (href != null && !href.equals("")) {
                seeds.add(href);
            }
        }

        return seeds ;
    }



    // passed
    @Test
    public void test() {
        new HyenaWorker().run();
    }
}
