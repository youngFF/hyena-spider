package com.hyena.spider.extrator;

import com.hyena.spider.filter.UrlFilter;
import com.hyena.spider.httpexecutor.HttpExecutor;
import com.hyena.spider.log.logger.HyenaLogger;
import com.hyena.spider.log.logger.HyenaLoggerFactory;
import com.hyena.spider.redis.connectionUtil.RedisConnection;
import com.hyena.spider.redis.factory.RedisConnectionPool;
import com.hyena.spider.redis.url.manager.HyenaUrlManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UrlExtractor extends AbstractExtractor {

    private HyenaLogger logger = HyenaLoggerFactory.getLogger(UrlExtractor.class);

    // 便于子类继承
    protected static final String TARGET_TAG = "a";


    @Override
    protected void extractInner(Document document) {
        Elements aTags = document.getElementsByTag(TARGET_TAG);
        forLoopTags(aTags);
    }

    private  void forLoopTags(Elements tags) {
        // acquire redis Connection
        RedisConnection connection = RedisConnectionPool.getConnection();
        String url = null ;
        for (Element element : tags) {
            //存到redis当中
            url = element.attr("abs:href");

            //因为formatUrl会为null，所以需要对它进行再次判断
            String formatUrl = UrlFilter.formatUrl(url);

            logger.info("解析出url : " + formatUrl);

            if (formatUrl != null) {
                HyenaUrlManager.putUrl(formatUrl , connection);
            }
        }
    }

}
