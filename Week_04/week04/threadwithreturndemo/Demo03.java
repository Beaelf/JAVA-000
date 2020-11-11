package com.megetood.geek.week04.threadwithreturndemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * use {@link  CompletableFuture}
 *
 * @author Lei Chengdong
 * @date 2020/11/10
 */
public class Demo03 {
    public static void main(String[] args) {
        try {
            String s = CompletableFuture.supplyAsync(() -> "supply a apple").get();
            System.out.println("1:"+s);

            String join = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                return "supply a apple";
            }).thenApply(a -> {
                System.out.println(Thread.currentThread().getName());
                return a.substring(9);
            }).get();
            System.out.println("2:"+join);

            String joinAsync = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                return "supply a apple";
            }).thenApply(a -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return a.substring(9);
            }).join();
            System.out.println("3:"+joinAsync);

            String joinAsync2 = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                return "supply a apple";
            }).thenApplyAsync(a -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return a.substring(9);
            }).get();
            System.out.println("4:"+joinAsync2);

            String joinAsync3 = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "supply a apple";
            }).applyToEitherAsync(CompletableFuture.supplyAsync( () -> {
                System.out.println(Thread.currentThread().getName());
                return "supply a bananer";
            }),c->c).join();
            System.out.println("5:"+joinAsync3);

            String joinAsync4 = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "supply a apple";
            }).thenCombineAsync(CompletableFuture.supplyAsync( () -> {
                System.out.println(Thread.currentThread().getName());
                return "supply a bananer";
            }),(a,b)->a+b).join();
            System.out.println("6:"+joinAsync4);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
