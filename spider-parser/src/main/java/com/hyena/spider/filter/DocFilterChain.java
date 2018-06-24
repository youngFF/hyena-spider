package com.hyena.spider.filter;

import com.hyena.spider.extrator.Extractor;
import com.hyena.spider.httpexecutor.HttpExecutor;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class DocFilterChain {



    private ArrayList<Extractor> filters = new ArrayList<>();


    /**
     * Do not IMPLICIT default constructor
     *
     * 这句话的意思是，及时默认的构造方法什么也不做，你也要写上
     */
    public DocFilterChain() {

    }


    /**
     * 注意：由于ArrayList 非线程安全，所以resiterExtractor和removeExtractor
     * 方法会出现并发问题
     * @param extractor
     */
    public void registerExtractor(Extractor extractor) {
        filters.add(extractor);
    }

    public void removeExtractor(Extractor extractor) {
        filters.remove(extractor) ;
    }


    /**
     * 提供给上层接口
     */
    public void doFilter() {


        //提供Document对象
        HttpExecutor executor = new HttpExecutor() ;
        Document doc = executor.execute();

        for (Extractor extractor : filters) {
            extractor.extract(doc);
        }
    }








}
