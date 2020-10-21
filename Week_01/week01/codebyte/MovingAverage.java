package com.megetood.geek.week01.codebyte;

/**
 * @author Lei Chengdong
 * @date 2020/10/18
 */
public class MovingAverage {
    private int count = 0;
    private double sum = 0.0D;

    public void submit(double val) {
        this.count++;
        this.sum += val;
    }

    public double getAvg() {
        if (0 == count) {
            return sum;
        }
        return this.sum / this.count;
    }
}
