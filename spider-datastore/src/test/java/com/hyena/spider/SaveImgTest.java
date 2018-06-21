package com.hyena.spider;

import com.hyena.spider.datastore.ImgDataStore;
import com.hyena.spider.filesystem.FileRepository;
import org.junit.Test;

public class SaveImgTest {

    @Test
    public void saveImg() {

        String url = "http://statics.itc.cn/web/static/images/pic/preload_2_1.png";

        ImgDataStore store = new ImgDataStore();

        store.imgStore(url);

    }



}
