package com.hyena.spider.logger.test;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetFormatDateTest {


    @Test
    public void foramtDateTest() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        System.out.println(format);
    }
}
