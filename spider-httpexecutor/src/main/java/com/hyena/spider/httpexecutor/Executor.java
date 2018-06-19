package com.hyena.spider.httpexecutor;


import java.util.concurrent.Callable;

/**
 * 任务执行接口，这个任务是所有任务的抽象
 */
public interface Executor<V>  {

    V execute();
}
