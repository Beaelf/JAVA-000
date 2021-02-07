package com.megetood.mmq.core;

import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Mmq {

    public Mmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue(capacity);
    }

    private String topic;

    private int capacity;

    private LinkedBlockingQueue<com.megetood.mmq.core.MmqMessage> queue;

    public boolean send(MmqMessage message) {
        return queue.offer(message);
    }

    public MmqMessage poll() {
        return queue.poll();
    }

    @SneakyThrows
    public MmqMessage poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

}
