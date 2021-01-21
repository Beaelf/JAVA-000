package com.megetood.mq.core;

public class MmqConsumer<T> {

    private final MmqBroker broker;

    private Mmq kmq;

    public MmqConsumer(MmqBroker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public MmqMessage<T> poll(long timeout) {
        return kmq.poll(timeout);
    }

}
