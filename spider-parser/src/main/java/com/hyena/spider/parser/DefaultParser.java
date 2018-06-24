package com.hyena.spider.parser;

import com.hyena.spider.extrator.Extractor;
import com.hyena.spider.filter.DocFilterChain;


/**
 * 提供给worker的接口
 */
public class DefaultParser implements HyenaParser {


    // 上层使用DefaultParser，并将parser中注册Extractor
    private DocFilterChain chain = new DocFilterChain() ;


    @Override
    public void parse() {
        chain.doFilter();
    }


    public void setExtractor(Extractor extractor) {
        chain.registerExtractor(extractor);
    }
}
