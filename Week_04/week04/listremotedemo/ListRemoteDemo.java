package com.megetood.geek.week04.listremotedemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程处理集合远程调用
 * 一个用户对象集合，每个用户都要远程调用查询订单信息
 *
 * @author Lei Chengdong
 * @date 2020/11/8
 */
public class ListRemoteDemo {
    public static void main(String[] args) {
        ListRemoteDemo listRemoteDemo = new ListRemoteDemo();
//        listRemoteDemo.listAllUserOrder();  // 150s 不使用多线程
//        listRemoteDemo.listAllUserOrder1(); // 3s 多线程（50个线程）/不适用线程池
//        listRemoteDemo.listAllUserOrder2(); // 51s 多线程（3个）/使用线程池 原子类实现
//        listRemoteDemo.listAllUserOrder3(); // 51 多线程（3个）/使用线程池 countdownlatch实现
    }

    // 不使用多线程
    public List<UserOrder> listAllUserOrder() {
        // 本机数据库查询
        List<UserOrder> res = getUserInfo();

        TimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        res.forEach(c -> {
            // 远程调用查询用户订单信息
            String orderInfo = remoteService(c.getUserId());
            c.setOrderInfo(orderInfo);
        });

        timeCounter.end();
        System.out.println("end");
        System.out.println("0耗时: " + timeCounter.get());

        return res;
    }

    // 多线程+CountDownLatch
    public List<UserOrder> listAllUserOrder1() {
        // 本机数据库查询
        List<UserOrder> res = getUserInfo();

        CountDownLatch countDownLatch = new CountDownLatch(res.size());

        TimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        res.forEach(c -> {
            new Thread(() -> {
                try {
                    // 远程调用查询用户订单信息
                    String orderInfo = remoteService(c.getUserId());
                    c.setOrderInfo(orderInfo);
                } finally {
                    countDownLatch.countDown();
                }

            }).start();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeCounter.end();
        System.out.println("end");
        System.out.println("1耗时: " + timeCounter.get());

        return res;
    }

    //线程池+AtomicInteger
    public List<UserOrder> listAllUserOrder2() {
        // 本机数据库查询
        List<UserOrder> res = getUserInfo();

        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        TimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        res.forEach(c -> {
            executorService.execute(() -> {
                try {
                    // 远程调用查询用户订单信息
                    String orderInfo = remoteService(c.getUserId());
                    c.setOrderInfo(orderInfo);
                } finally {
                    counter.incrementAndGet();
                }
            });
        });
        executorService.shutdown();

        while (true) {
            if (counter.get() == res.size()) {
                break;
            }
        }
        timeCounter.end();
        System.out.println("2耗时: " + timeCounter.get());

        return res;
    }

    // 线程池+CountDownLatch
    public List<UserOrder> listAllUserOrder3() {
        // 本机数据库查询
        List<UserOrder> res = getUserInfo();

        CountDownLatch countDownLatch = new CountDownLatch(res.size());
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        TimeCounter timeCounter = new TimeCounter();
        timeCounter.start();
        res.forEach(c -> {
            executorService.execute(() -> {
                try {
                    // 远程调用查询用户订单信息
                    String orderInfo = remoteService(c.getUserId());
                    c.setOrderInfo(orderInfo);
                } finally {
                    countDownLatch.countDown();
                }
            });
        });
        executorService.shutdown();

        try {
            countDownLatch.await();
            System.out.println(countDownLatch.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeCounter.end();
        System.out.println("3耗时: " + timeCounter.get());
        return res;
    }

    private static String remoteService(String userId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "order" + userId;
    }

    private static List<UserOrder> getUserInfo() {
        List<UserOrder> res = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            UserOrder u = new UserOrder();
            u.setUserId(i + "");
            res.add(u);
        }
        return res;
    }

    private void check(List<UserOrder> res) {
        for (int i = 1; i < res.size(); i++) {
            String l = res.get(i - 1).getOrderInfo();
            String r = res.get(i).getOrderInfo();
            Integer ls = Integer.valueOf(l.substring(5));
            Integer rs = Integer.valueOf(r.substring(5));
            if (ls > rs) {
                throw new RuntimeException("error");
            }
        }
        System.out.println("right");
    }
}

