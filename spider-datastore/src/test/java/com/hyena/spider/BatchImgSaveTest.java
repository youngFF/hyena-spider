package com.hyena.spider;

import com.hyena.spider.filesystem.FileRepository;

import java.net.MalformedURLException;
import java.net.URL;

public class BatchImgSaveTest {

    public static void main(String[] args) throws MalformedURLException {
        FileRepository fileRepository = new FileRepository();
        fileRepository.innerBatchSave(new URL("http://img1.mm131.me/pic/2486/5.jpg"));
    }

}
