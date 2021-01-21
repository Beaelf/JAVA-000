package com.megetood.mq.core;

public class MmqProducer {

    private MmqBroker broker;

    public MmqProducer(MmqBroker broker) {
        this.broker = broker;
    }

    public boolean send(String topic, MmqMessage message) {
        Mmq kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        return kmq.send(message);
    }
}
