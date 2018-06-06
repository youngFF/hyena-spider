package com.hyena.spider.logger.test;


import org.junit.Test;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 取得用户主目录属性
 *
 * 结论：可以使用两种方法获得当前用户的主目录：
 *
 * #使用机器系统的环境变量
 * 1.System.getEnv("USER")
 *
 * #使用java进程启动时的属性
 * 2.System.getProperty("user.home")
 */
public class HomeUserPropertyGetTest {


    /**
     * 获取系统环境变量，也就是linux系统的环境变量
     */
    @Test
    public void systemEnvPropertyGetTest() {
        Properties properties = System.getProperties();
        Map<String, String> env = System.getenv();
        Set<Map.Entry<String, String>> entries = env.entrySet();
        for (Map.Entry entry : entries) {
            System.out.println(entry.getKey() + "  " + entry.getValue());

        }
    }

    /**
     * 获取java的环境变量，也就是java的启动属性-D
     */
    @Test
    public void javaPropertyGetTest() {
        Properties properties = System.getProperties();
        System.out.println(properties);
    }
}
