package com.megetood.geek.week04.threadwithreturndemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * use thread pool and callback interface to implement a thread with return
 *
 * @author Lei Chengdong
 * @date 2020/11/10
 */
public class Demo01 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> res = executorService.submit(() -> "demo01");
        System.out.println(res);
        executorService.shutdown();
    }
}
