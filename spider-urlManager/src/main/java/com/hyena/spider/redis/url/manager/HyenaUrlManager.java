package com.hyena.spider.redis.url.manager;


import com.hyena.spider.configuration.HyenaFrameworkConfiguration;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.RedisUtil.RedisNamespaceDesignator;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

/**
 * 将redis中的todo队列删除，因为我觉得这是一个冗余的设计
 */
public class HyenaUrlManager {


    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaUrlManager.class);
    // redis中的visited的key
    private static String visitedKey = RedisNamespaceDesignator.provideVisitedKey();

    // redis中的命名空间
    private static String namespaceKey = RedisNamespaceDesignator.getNamespaceKey();


    private static final String SINGLE_SITE = HyenaFrameworkConfiguration.getSingleSite();
    /**
     * WARNING：我觉的redis中的todo队列是设计上的一个冗余：原因是，没有必要，把url从namespace中拿出来
     * 先放到redis中的todo队列，然后再从redis中的todo队列移动到内存中的todo队列。
     * 用老百姓的话说就是：折腾来，折腾去干啥啊？？？有病啊？？有钱啊？？
     */


    /**
     * 在这个方法不进行url格式检查，应该交给上层接口，这个确保传过来的就是形如：http://www.baidu.com
     * 这样的url ，注意：这个方法有一个弊端就是如果循环调用putUrl的时候，每一次都要获取一个连接
     * 我推荐使用重载的putUrl方法，
     *
     * @param url
     */
    public static void putUrl(String url) {

        RedisConnection connection = RedisConnectionPool.getConnection();

        // 只要有一个redis visited队列包含这个连接，我们就将这个url废弃

        if (connection.getJedisClient().sismember(visitedKey, url)) {
            logger.info(url + " 存在visited队列中，将被废弃");
            return;
        }


        // 将url添加到 namespace中。
        try {
            //这个url是标准化的url ，形如：http://www.baidu.com
            URL url1 = new URL(url);
            //找到这个url的hostOwner
            String hostOwner = getHostOwner(url1.getHost());

            System.out.println(hostOwner);

            try {
                connection.getJedisClient().sadd(hostOwner, url);
                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(hostOwner);
                connection.getJedisClient().sadd(namespaceKey, hostOwner);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + hostOwner);
                // 添加到命名空间中
            } catch (Exception e) {
                // do nothing
            }

        } catch (MalformedURLException e) {
            //利用重定向技术，将异常打印到日志文件中
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            e.printStackTrace(pw);
            logger.error(writer.toString());
        } finally {
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }


    }


    /**
     * 这个方法是针对循环调用的情景，只需要重用同一个connection
     *
     * @param url
     * @param connection
     */
    public static void putUrl(String url, RedisConnection connection) {


        // 只要有一个redis visited队列包含这个连接，我们就将这个url废弃

        if (connection.getJedisClient().sismember(visitedKey, url)) {
            logger.info(url + " 存在visited队列中，将被废弃");
            return;
        }


        // 将url添加到 namespace中。
        try {
            //这个url是标准化的url ，形如：http://www.baidu.com
            URL url1 = new URL(url);
            //找到这个url的hostOwner
            String hostOwner = getHostOwner(url1.getHost());


            try {
                connection.getJedisClient().sadd(hostOwner, url);
                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(hostOwner);
                connection.getJedisClient().sadd(namespaceKey, hostOwner);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + hostOwner);
                // 添加到命名空间中
            } catch (Exception e) {
                // do nothing
            }


        } catch (MalformedURLException e) {
            //利用重定向技术，将异常打印到日志文件中
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            e.printStackTrace(pw);
            logger.error(writer.toString());
        } finally {
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }

    }


    /**
     * 初始化种子的时候，不判断url是否在hyena-visted中
     * @param url
     * @param connection
     */
    public static void putInitSeedsUrl(String url, RedisConnection connection) {



        // 将url添加到 namespace中。
        try {
            //这个url是标准化的url ，形如：http://www.baidu.com
            URL url1 = new URL(url);
            //找到这个url的hostOwner
            String hostOwner = getHostOwner(url1.getHost());


            try {
                connection.getJedisClient().sadd(hostOwner, url);
                // 更新host namespace
                RedisNamespaceDesignator.updateHostNamespace(hostOwner);
                connection.getJedisClient().sadd(namespaceKey, hostOwner);
                //注意这个logger必须在这里，因为只有上条语句成功执行之后，才可以打印日志
                logger.info(url + "   added to namespace --->" + hostOwner);
                // 添加到命名空间中
            } catch (Exception e) {
                // do nothing
            }


        } catch (MalformedURLException e) {
            //利用重定向技术，将异常打印到日志文件中
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            e.printStackTrace(pw);
            logger.error(writer.toString());
        } finally {
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }

    }

    /**
     * TODO : 怎么获取url是一个问题??????????
     * 这个方法耗费了我很长的时间。。。。。。。。。。。。。。-_-!!!
     *
     * @return
     */
    public static String getUrl() {

        RedisConnection connection = RedisConnectionPool.getConnection();

        RedisConnection namespaceConnection = RedisConnectionPool.getConnection() ;



        String url = null;
        try {
            String single = new URL(SINGLE_SITE).getHost();

            String randomNamespaceKey = namespaceConnection.getJedisClient().srandmember(namespaceKey);
            //如果hyena全局设置了single site 参数，那么就返回这个namespace下的一个url，否则
            //返回一个随机的namespace下的url

            //TODO ,取得url，注意这个url可能会为空
            url = connection.getJedisClient().spop( single != null && !single.equals("")?
            single : randomNamespaceKey);

            if (url == null) {
                url = nullUrlStrategy(connection, randomNamespaceKey);
            }

            //将url放到redis visited中
            connection.getJedisClient().sadd(visitedKey, url);
            logger.info(url + " -------> " + visitedKey );

        } catch (Exception e) {
            // do nothing
        } finally {
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
            namespaceConnection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }
        return url;

    }

    public static String nullUrlStrategy(RedisConnection connection , String randomKey) {
        String url = null ;
        while (true) {
            url = connection.getJedisClient().spop(randomKey);
            if (url != null) {
                break ;
            }
        }
        return url ;
    }
    public void initSeeds(String... seeds) {
        for (String seed : seeds) {
            logger.info("初始化种子 : " + seed );
            extractUrl(seed);
        }
    }


    private void extractUrl(String url) {
        RedisConnection connection = RedisConnectionPool.getConnection();

        try {
            Document document = Jsoup.parse(new URL(url), 5000);
            Elements aTags = document.getElementsByTag("a");
            String href = null ;
            for (Element aTag : aTags) {
                  href = aTag.attr("abs:href");

                if (href != null && !href.equals("")) {
                    HyenaUrlManager.putInitSeedsUrl(href , connection);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            connection.setJedisConnState(RedisConnection.JedisState.AVAILABLE);
        }
    }

    //如果这个命名空间为空的话，进行初次解析


    /**
     * 找到这个url的hostOwner,如果找不到，那么自己就是一个host
     *
     * @param url
     * @return
     */
    private static String getHostOwner(String url) {
        HashSet<String> hostNamespace = RedisNamespaceDesignator.getHostNamespace();

        for (String host : hostNamespace) {
            if (!host.equals("") && url.contains(host)) {
                return host;
            }
        }

        return url;
    }
}
