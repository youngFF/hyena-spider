package com.hyena.spider.redisTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;


/**
 * WARNING:要想进行redis测试，首先确保电脑安装redis，并且开启redis服务，确定相应的端口。
 * <p>
 * <p>
 * 向reids中的key “urls”中分别存放2w，20w，200w，2000w,20000w
 * 分别测试它们在数量的url下的存储/batchSave ， 查找/find ，删除/del 所花费的时间
 * <p>
 * 测试方法：修改方法redisSaveTimeConsumingTest中的count值
 * <p>
 * <p>
 * 以下是我经过实际测试，对redis中Set的batch save（批量存储插入） ， single save（单条存储插入） ，
 * delete memeber（删除成员） ， find（查找成员）操作分别在不同count值下消耗的时间。
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
 */
public class MyRedisTest {


    //delete key
    @Test
    public void redisDelKey() {
        Jedis jedis = new Jedis("localhost");
        jedis.del("urls");
        System.out.println("successed delete key : " + "urls");
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
    public void singAddTimeCosumingTest() {
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
    public void redisDelTimeCosumingTest() {
        long start = System.currentTimeMillis();
        Jedis jedis = new Jedis("localhost");
        Long urls = jedis.srem("urls", "http://www.baidu.com?i=5690");
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }


    public static class Worker implements Runnable {

        @Override
        public void run() {
            int count = 200000; // 20w
            Jedis jedis = new Jedis("localhost");
            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                jedis.sadd(Thread.currentThread().getName(), String.valueOf(i));
            }
            long end = System.currentTimeMillis();

            System.out.println("batch save succeed : " + Thread.currentThread().getName() + " : " + (end - start));


        }
    }

    @Test
    public void multiBatchSaveTimeCosumingTest() {

        int threadCount = 10 ;

        new Thread(new Worker()).start();
    }


}
