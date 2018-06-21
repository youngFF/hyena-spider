package com.hyena.spider.extrator;

import org.jsoup.nodes.Document;

/**
 * Extractor：所有抽取器都应该实现的接口
 * 主要功能：抽取页面的元素
 */
public interface Extractor {


    /**
     * 执行页面抽取逻辑
     */
    void extract(Document document) ;
}
