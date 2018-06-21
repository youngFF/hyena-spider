package com.hyena.spider.datastore;

import com.hyena.spider.filesystem.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class ImgDataStore {

    private static final int BASE = 6 ;

    // 给ByteBuffer分配6M大小的缓冲区
    private static final int IMG_BUFFER = 6 * 1024 * 1024 ;

    private ByteBuffer imgBuffer = ByteBuffer.allocate(IMG_BUFFER);

    public void imgStore(String imgSrc) {


        try {
            // 图片的url地址
            URL url = new URL(imgSrc);
            //图片的输入流
            InputStream inputStream = url.openStream();

            FileRepository fileRepository = new FileRepository();

            //将图片存到本地
            fileRepository.imgSave(url,imgBuffer ,inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
