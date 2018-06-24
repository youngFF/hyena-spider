package com.hyena.spider;

import com.hyena.spider.parser.DefaultParser;
import com.hyena.spider.parser.HyenaParser;

public class HyenaWorker implements Runnable {



    private  HyenaParser parser ;


    public HyenaWorker(HyenaParser parser) {
        this.parser = parser ;
    }


    public HyenaWorker() {
        this.parser = new DefaultParser() ;
    }

    public HyenaParser getParser() {
        return parser;
    }

    @Override
    public void run() {
        parser.parse();
    }
}
