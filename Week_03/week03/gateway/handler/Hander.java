package com.megetood.geek.week03.gateway.handler;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求处理接口
 *
 * @author Lei Chengdong
 * @date 2020/11/4
 */
public interface Hander {
    void handle(FullHttpRequest request);
}
