package com.megetood.mq.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@AllArgsConstructor
@Data
public class MmqMessage<T> {

    private HashMap<String,Object> headers;

    private T body;

}
