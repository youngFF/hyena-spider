package com.hyena.spider.filter;

import com.hyena.spider.configuration.HyenaFrameworkConfiguration;
import com.hyena.spider.redis.configure.RedisConnectionConfigurer;

public class UrlFilter {

    private static final String SINGLE_SITE = HyenaFrameworkConfiguration.getHyenaConfig("hyena.single.site");

    /**
     * 将url格式化 ，比如所有的url都是http,https开头，
     *
     * 其次：
     * 比如：对于http://www.baidu.com和http://www.baidu.com/ 其实都是同一个网址
     * 不对后者进行处理，因为解析一次后，得到url和前面的网站相同，但是前面网址解析出来
     * 的url已经放到visited队列中了，所以后者解析出来的url不会放到visited队列中！！！！
     *
     * @return 规范化的url ，实在不行返回null
     */
    public static String formatUrl(String url) {
        // 利用布尔短路特性，将SINGLE_SITE != null 写在前面，因为大多数情况下
        // 都是null的情况多 ，这样的好处就是能较少语句的执行
        if ( SINGLE_SITE != null && !SINGLE_SITE.equals("")){
            // 单一站点
            return url.contains(SINGLE_SITE) ? url : null ;
        }else{
            //过滤掉不是http和https开头的
            if(url.startsWith("https") || url.startsWith("http")){
                return url ;
            }
        }
        return null ;
    }




}
