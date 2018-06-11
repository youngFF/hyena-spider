package com.hyena.spider.redis.RedisUtil;

import com.hyena.spider.helper.validate.HyenaValidate;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class RedisPropsLoader {

    // 要读取属性文件中的字段名称 , 可以修改使之获取你想要的redis连接配置
    private static final String[] redisTargetParams = {"redis.host", "redis.port", "redis.password",
            "redis.timeout", "redis.connection.count",
            "redis.visited.name", "redis.todo.name"};
    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(RedisPropsLoader.class);
    // redis连接属性文件对象
    private static Properties redisProps;
    // 存放redis属性的数据结构，也是直接的对外接口
    private static HashMap<String, String> redisParamsMap = new HashMap<String, String>();

    static {
        logger.info("加载reids属性文件...");
        redisProps = new Properties();
        // 找的是classpath下的资源
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("redisConnectionConfiguration.properties");
        // 这条代码的作用是给用户提醒，在fillRedisParamterMap方法中还是需要对InputStream进行判空检查
        HyenaValidate.notNull(in);

        // 由于HyenaValidate.notNull抛出RuntimeException，所以fillRedisParameter方法
        // 中还需要对InputStream进行判空检查
        fillRedisParameterMap(redisProps, in);
    }


    private RedisPropsLoader() {

    }


    public static void fillRedisParameterMap(Properties props, InputStream in) {
        logger.info("填充redis属性表");
        //
        if (in != null) {
            try {
                redisProps.load(in);
            } catch (IOException e) {

            }

            for (int i = 0; i < redisTargetParams.length; i++) {
                logger.info("找到redis连接属性 : " + redisTargetParams[i]);
                redisParamsMap.put(redisTargetParams[i], redisProps.getProperty(redisTargetParams[i]));
            }
        }
    }

    //取得redis的连接属性
    public static String getRedisConnProperty(String propertyName) {
        return redisParamsMap.get(propertyName);
    }

    //设置redis的连接属性
    public static void setRedisConnProperty(String redisProperty, String redisConnValue) {
        redisParamsMap.put(redisProperty, redisConnValue);
    }


    public static String getProperty(String property) {
        return redisParamsMap.get(property);
    }


}
