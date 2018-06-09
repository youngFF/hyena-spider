package com.hyena.spider.redisTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;


/**
 * WARNING:要想进行redis测试，首先确保电脑安装redis，并且开启redis服务，确定相应的端口。
 * <p>
 * <p>
 * 向redis中的key “urls”中分别存放2w，20w，200w，2000w,20000w
 * 分别测试它们在数量的url下的存储/batchSave ， 查找/find ，删除/del 所花费的时间
 * <p>
 * 测试方法：修改方法redisSaveTimeConsumingTest中的count值
 * <p>
 * <p>
 * 以下是我经过实际测试，对redis中Set的batch save（批量存储插入） ， single save（单条存储插入） ，
 * delete member（删除成员） ， find（查找成员）操作分别在不同count值下消耗的时间。
 * <p>
 * method\count     2w          20w         200w        2000w
 * <p>
 * batch save       622~660     3300~3400   27270       是个迷～_~!!
 * <p>
 * single save      70左右        70左右     70左右       70左右
 * <p>
 * find             60~70       60~70       60~70       60~70
 * <p>
 * delete           65~75       65~75       65~75       65~75
 * <p>
 * 单位：ms ，我本人电脑配置 内存4GB ， cpu ：Intel® Pentium(R) CPU G3220 @ 3.00GHz × 2  ，64bit
 * <p>
 * 可以看到，随着Set集合成员数量的不断增大，批量插入的时间会显著增加，但是不管Set成员数目如何，Set的single save，find，delete的时间
 * 都是常数时间，所以Set集合的这种性质非常适合做爬虫url manager的底层数据结构。
 * <p>
 * 但是注意：batch save的时候，使用for loop 排队插入。当插入的数量从20w -> 200w的时候，时间从原来的3s，增加到了30s，同样也是增大
 * 了10倍。
 * <p>
 * 所以：换一个想法，把200w个数据项，同时插入到10个Set类型的key中，看看这样的时间消耗情况。如下：
 * <p>
 * <p>
 * single batch save succeed - key : url1 time : 12571
 * single batch save succeed - key : url4 time : 12652
 * single batch save succeed - key : url3 time : 12732
 * single batch save succeed - key : url0 time : 12737
 * single batch save succeed - key : url8 time : 12749
 * single batch save succeed - key : url6 time : 12802
 * single batch save succeed - key : url5 time : 12836
 * single batch save succeed - key : url2 time : 12867
 * single batch save succeed - key : url7 time : 12872
 * single batch save succeed - key : url9 time : 12908
 * <p>
 * 可以看到，利用多线程，每一个线程向一个key中批量，插入20w数据项，需要的时间将近十三秒。共有
 * 10个线程同时向不同的key中插入，所以总的时候也就是13s。
 * <p>
 * 结论：通过利用多线程将200w个数据，分别同时插入10个key中，需要的总时间为12.8s ，比起原来直接向一个key中
 * 批量插入200w条数据所需要27s时间，时间大幅度下降。
 * <p>
 * 所以说，在存储url时，可以根据某些条件，将url存到不同的key下，这样能减少存储时间。如url的网址，http://www.douban.com/a  , http://www.douban.com/b都存到
 * key为douban.com下
 */
public class MyRedisTest {

    private static int threadCount = 10;

    /**
     * multi thread batch save timeConsuming test
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new Worker());
            t.setName("url" + i);
            t.join();
            t.start();
        }
        long end = System.currentTimeMillis();

        System.out.println("multi thread batch save time consuming : " + (end - start));
    }

    //delete key
    @Test
    public void redisDelKey() {
        Jedis jedis = new Jedis("localhost");
        jedis.del("urls");
        System.out.println("succeed delete key : " + "urls");
    }

    // batch save 批量存储
    @Test
    public void redisBatchSaveTimeConsumingTest() {
        int count = 20000000;
        Jedis jedis = new Jedis("localhost");

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            jedis.sadd("urls", "http://www.baidu.com?i=" + String.valueOf(i));
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    // single add
    @Test
    public void singAddTimeConsumingTest() {
        Jedis jedis = new Jedis("localhost");

        long start = System.currentTimeMillis();

        jedis.sadd("urls", "http://www.baidu.com?i=singleAdd");

        System.out.println(System.currentTimeMillis() - start);
    }

    // find
    @Test
    public void redisFindConsumingTest() {
        long start = System.currentTimeMillis();
        Jedis jedis = new Jedis("localhost");
        boolean isMember = jedis.sismember("urls", "http://www.baidu.com?i=5690");
        long end = System.currentTimeMillis();

        System.out.println("isMember: " + isMember + "time :" + (end - start));

    }

    // delete
    @Test
    public void redisDelTimeConsumingTest() {
        long start = System.currentTimeMillis();
        Jedis jedis = new Jedis("localhost");
        Long urls = jedis.srem("urls", "http://www.baidu.com?i=5690");
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    // batch del key
    @Test
    public void batchDelKeyTest() {
        Jedis jedis = new Jedis("localhost");
        for (int i = 0; i < threadCount; i++) {
            jedis.del("url" + i);
        }
    }

    private static class Worker implements Runnable {

        @Override
        public void run() {
            int count = 200000; // 20w
            Jedis jedis = new Jedis("localhost");
            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                jedis.sadd(Thread.currentThread().getName(), String.valueOf(i));
            }
            long end = System.currentTimeMillis();

            System.out.println("single batch save succeed - key : " + Thread.currentThread().getName() + " time : " + (end - start));


        }
    }

}
