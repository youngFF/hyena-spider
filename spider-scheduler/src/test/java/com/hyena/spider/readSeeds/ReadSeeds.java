package com.hyena.spider.readSeeds;

import com.hyena.spider.scheduler.HyenaScheduler;
import org.junit.Test;

import java.util.ArrayList;

public class ReadSeeds {


    @Test
    public void readSeeds() {
        HyenaScheduler.readSeeds();
        ArrayList<String> seeds = HyenaScheduler.getSeeds();

        for (String seed : seeds) {
            //后面加点，是为了看看有没有空行
            System.out.println(seed + " .....");
        }
    }

}
