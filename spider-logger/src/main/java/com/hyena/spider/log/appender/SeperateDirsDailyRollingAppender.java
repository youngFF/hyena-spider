package com.hyena.spider.log.appender;

import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.apache.log4j.DailyRollingFileAppender;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 这个类重写了DailyRollingFileAppender，并在log4j.properties使用
 *
 * SeperateDirsDailyRollingAppender的主要功能是：为hyena-spider每天产生的日志创建一个目录，
 * 目录名字格式为yyyyMMdd 。
 * 假如：2018-06-07产生一个日志，则目录结构为
 * USER_HOME/LOG_DIR/DATE/DATE.log  即 文件名称为 USER_HOME/LOG_DIR/20180607/20180607.log
 *
 * @author hyena
 */
public class SeperateDirsDailyRollingAppender extends DailyRollingFileAppender {

    // 使用这样方法获取当前系统用户的主目录，能够避免项目与特定的操作系统紧耦合。这样做能使
    // 项目具有更大的灵活性
    private static final String USER_HOME = System.getProperty("user.home") ;

    // 日志主目录
    private static final String LOG_DIR = "hyena-spider-log";

    // logger
    private static final HyenaLogger logger = HyenaLoggerFactory.getLogger(SeperateDirsDailyRollingAppender.class);


    @Override
    /**
     * 根据爬虫项目的实际需要，设置自己的日志目录结构：
     * 每一天都会创建一个文件夹，然后在文件夹中生成当天的日志文件
     *
     * log4j.properties 中的key：log4j.appender.[appender].File = <fileName>其实就是
     * 调用了这个方法setFile。
     *
     * 这里，不在log4j中配置file，因为在log4j中不不能获得型如yyyyMMdd.log 格式的文件。也就是说，
     * 在log4.propeties中不会使用日期来产生日志文件名.
     */
    public void setFile(String fileName) {

        // 我们拥有使用log4j.properties中的属性，但是为了项目今后的扩展，将接口留出来，方便以后扩展
        String val = executeFileName(fileName);


        Date date = new Date() ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatedDate = sdf.format(date);

        fileNotExistMake(new File(USER_HOME + "/" + LOG_DIR + "/"+ formatedDate));

        this.fileName = USER_HOME + "/" + LOG_DIR + "/" + formatedDate + "/" + formatedDate+".log" ;

    }


    private  String executeFileName(String fileName) {
        return null ;
    }

    private static void fileNotExistMake(File dir) {
        if (!dir.exists()) {
            logger.warn("文件不存在 ：" + dir.getAbsolutePath() );
            logger.info("创建文件 ： " + dir.getAbsolutePath());
            dir.mkdir();
            logger.info("文件创建成功 ： " + dir.getAbsolutePath() );
        }
    }

    private  static void createLogHomeDir() {
        // 组成log的主目录路径
        String logHome = USER_HOME + "/" + LOG_DIR;
        File dir = new File(logHome);
        fileNotExistMake(dir);
    }





    static {
        // 当载入类时，进行目录初始化
        createLogHomeDir();
    }
}
