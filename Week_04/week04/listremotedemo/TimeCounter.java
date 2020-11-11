package com.megetood.geek.week04.listremotedemo;

/**
 * 计时器
 *
 * @author Lei Chengdong
 * @date 2020/11/8
 */
public class TimeCounter {
    private Long start;
    private Long end;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
    }

    public Long get() {
        return end - start;
    }
}
