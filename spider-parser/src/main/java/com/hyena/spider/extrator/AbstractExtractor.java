package com.hyena.spider.extrator;

import com.hyena.spider.httpexecutor.HttpExecutor;
import org.jsoup.nodes.Document;

public abstract class AbstractExtractor implements Extractor {


    /**
     * 采用模板方法，每个extract的处理逻辑都是一样的
     * 1.生成Document对象
     * 2.抽取想要的页面元素
     * 3.将想要的页面元素存储
     */
    @Override
    public void extract(Document document) {
        extractInner(document);
    }

    /**
     * 进行真正的抽取逻辑处理
     * 抽取相应的资源，并且将资源本地化
     */
    protected abstract void extractInner(Document document) ;
}
