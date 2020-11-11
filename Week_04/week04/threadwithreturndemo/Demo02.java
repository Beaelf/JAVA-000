package com.megetood.geek.week04.threadwithreturndemo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * use {@link java.util.concurrent.FutureTask}
 *
 * @author Lei Chengdong
 * @date 2020/11/10
 */
public class Demo02 {
    public static void main(String[] args) {
        FutureTask<String> task = new FutureTask<>(() -> "demo02:user FutureTask");
        new Thread(task).start();
        try {
            System.out.println(task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

