package com.megetood.mmq.core;

public class MmqConsumer<T> {

    private final MmqBroker broker;

    private Mmq mmq;

    public MmqConsumer(MmqBroker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.mmq = this.broker.findKmq(topic);
        if (null == mmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public MmqMessage<T> poll(long timeout) {
        return mmq.poll(timeout);
    }

}
