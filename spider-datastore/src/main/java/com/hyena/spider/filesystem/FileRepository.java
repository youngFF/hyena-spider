package com.hyena.spider.filesystem;


import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * 这个类是存放所有hyena爬取的资源的路径映射类
 */
public class FileRepository {

    // 主目录格式 ： /home/{user}
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String REPOSITORY = "hyena-repository";
    // 存放资源的主目录 ：  /home/{user}/hyena-repository
    private static final String REPOSITORY_HOME = USER_HOME + "/" + REPOSITORY;
    private static HyenaLogger logger = HyenaLoggerFactory.getLogger(FileRepository.class);

    /**
     * 根据url和inputStream，FileRepository会把它放到应该放置的地址
     *
     * @param url         给定的图片的url地址
     */
    public void imgSave(URL url , HttpConnection connection) throws IOException {


        byte[] imgBytes = connection.execute().bodyAsBytes();

        File img = url2File(url);
        //如果文件存在那么直接退出
        if (img.exists()) {
            logger.info("文件已经存在: " + REPOSITORY_HOME +"/"+ url.getHost() + url.getPath());
            return ;
        }

        FileOutputStream fos = new FileOutputStream(img);

        fos.write(imgBytes);
        fos.flush();
        logger.info("成功存放图片 : " + REPOSITORY_HOME +"/"+ url.getHost() + url.getPath());

        // 关闭流 ，不抛出异常，在自己的方法中消化 ,借鉴Spring中处理JDBC的模板方法
        closeStream(fos);

        // 方法尾不需要再次调用imgBuffer.clear，因为在方法开头就已经调用了
        // imgBuffer.clear()
    }


    /**
     * 将url转换为文件系统中的地址
     * 接下来想象怎么映射。。。。。
     *
     * @return
     */
    public File url2File(URL imgSrc) {

        String host = imgSrc.getHost() ;
        String path = imgSrc.getPath();
        int pivot = path.lastIndexOf("/");

        // 存放图片的目录，在存放图片之前，先要创建相应的目录
        String dirs = host + path.substring(0, pivot);


        File dir = new File(REPOSITORY_HOME + "/" + dirs);

        // 创建目录
        dir.mkdirs();

        String fileName = REPOSITORY_HOME + "/" + host + path;

        File imgFile = new File(fileName);

        return imgFile;
    }


    private void closeStream(OutputStream out) {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
