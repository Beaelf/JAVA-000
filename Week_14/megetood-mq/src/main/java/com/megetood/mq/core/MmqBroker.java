package com.megetood.mq.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MmqBroker { // Broker+Connection

    public static final int CAPACITY = 10000;

    private final Map<String, Mmq> kmqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name){
        kmqMap.putIfAbsent(name, new Mmq(name,CAPACITY));
    }

    public Mmq findKmq(String topic) {
        return this.kmqMap.get(topic);
    }

    public MmqProducer createProducer() {
        return new MmqProducer(this);
    }

    public MmqConsumer createConsumer() {
        return new MmqConsumer(this);
    }

}
