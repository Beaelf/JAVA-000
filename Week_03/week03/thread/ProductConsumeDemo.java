package com.megetood.geek.week03.thread;

/**
 * 生产消费
 *
 * @author Lei Chengdong
 * @date 2020/11/1
 */
public class ProductConsumeDemo {
    public static int goods = 0;
    public static final int max = 10;
    public static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    new Consumer().consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"c").start();

        new Thread(() -> {
            synchronized (lock) {
                try {
                    new Productor().product();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"p").start();
    }
}

class Consumer {
    public void consume() throws InterruptedException {
        while (true) {
            System.out.println(Thread.currentThread().getName() + ": " + ProductConsumeDemo.goods);
            if (ProductConsumeDemo.goods <= 0) {
                System.out.println("没有商品可以消费了。。。");
                ProductConsumeDemo.lock.wait();
            } else {
                ProductConsumeDemo.goods--;
            }
            ProductConsumeDemo.lock.notifyAll();
        }
    }
}

class Productor {
    public void product() throws InterruptedException {
        while (true) {
            System.out.println(Thread.currentThread().getName() + ": " + ProductConsumeDemo.goods);
            if (ProductConsumeDemo.goods >= ProductConsumeDemo.max) {
                System.out.println("生产达到阈值，停止生产。。。");
                ProductConsumeDemo.lock.wait();
            } else {
                ProductConsumeDemo.goods++;
            }
            ProductConsumeDemo.lock.notifyAll();
        }
    }
}
