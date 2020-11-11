package com.megetood.geek.week04.threadwithreturndemo;

import java.util.concurrent.CountDownLatch;

/**
 * user atomic
 *
 * @author Lei Chengdong
 * @date 2020/11/11
 */
public class Demo00 {
    static String res;
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                res = getStr();
            } finally {
                countDownLatch.countDown();
            }

        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(res);
    }

    public static String getStr() {
        return "demo01";
    }
}
