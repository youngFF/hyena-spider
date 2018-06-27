package com.hyena.spider.configuration;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HyenaFrameworkConfiguration {

    // logger
    private final static HyenaLogger logger = HyenaLoggerFactory.getLogger(HyenaFrameworkConfiguration.class);

    // the location of configuration file
    private static final String file = "spider-global.properties";
    // parser filter tags
    private static  String filterTags  ;



    // max thread count
    private static  int maxThreadCount  ;


    private static final int DEFAULT_MAX_THREAD_COUNT = 20 ;

    // base filter tags
    private static final String BASE_FILTER_TAGS = "a,img";



    private static void checkProperties() {
        filterTags = getProperty(file).getProperty("hyena.parse.filter");
        if (filterTags == null) {
            //因为我目前只对这两个元素感兴趣啊，哈哈哈哈哈哈哈哈
            filterTags = BASE_FILTER_TAGS;
            logger.warn("请设置hyena.parse.filter字段的值，hyena-spider将采用默认的filterTags: " + BASE_FILTER_TAGS);
        } else {
            filterTags = BASE_FILTER_TAGS + "," + filterTags;
            logger.info("元素解析器为: " +  filterTags);
        }

        try {
            maxThreadCount = Integer.parseInt(getProperty(file).getProperty("hyena.maxThreadCount")) ;
        } catch (NumberFormatException e) {
            maxThreadCount = DEFAULT_MAX_THREAD_COUNT ;
            logger.warn("请检查文件: " + file + " 中字段: hyena.maxThreadCount的值" + "  hyena-spider将采用默认的值：" + DEFAULT_MAX_THREAD_COUNT);
        }

    }


    static {
        //不要在外部主动调用，而是当类载入的时候就进行hyena-frame条件检查
        checkHyenaConfig();
        maxThreadCount = Integer.valueOf(getProperty(file).getProperty("hyena.maxThreadCount"));
    }

    /**
     * hyena-spider使用checkHyenaConfig对框配置进行检查。
     * 主要检查的方面有：
     *      redis服务是否启动
     *      spider-global.properties中的字段是否被正确设置。
     *
     *      一下三个方法是提供给上层的三个接口
     *
     * 必须调用checkProperties方法之后，才能使用filterTags
     */
    public static void checkHyenaConfig() {
        checkProperties();
    }

    public static String getFilterTags() {
        return filterTags ;
    }


    public static String getHyenaConfig(String config) {
        return getProperty(file).getProperty(config);
    }

    public static String getSingleSite() {
        return getProperty(file).getProperty("hyena.single.site");
    }

    public static int getMaxThreadCount() {

        //静态int属性如果没有设置的话为0
        return maxThreadCount != 0 ? maxThreadCount : DEFAULT_MAX_THREAD_COUNT;
    }
   // ..................................................... //








    private static Properties getProperty(String path)  {
        Properties props = new Properties();
        InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

        try {
            // do not propagate -- 不传播异常
            props.load(resource);
        } catch (IOException | NullPointerException e) {
            //不应该捕获这个异常，而是要抛出运行时异常。因为你即使打印这个异常，也不会对程序的调试有
            //　任何帮助
//            e.printStackTrace();

            throw new RuntimeException("请检查 " + path + " 的路径是否正确");
        }
        return props ;
    }



}
