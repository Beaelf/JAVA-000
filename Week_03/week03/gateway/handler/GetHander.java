package com.megetood.geek.week03.gateway.handler;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 处理get请求
 *
 * @author Lei Chengdong
 * @date 2020/11/4
 */
public class GetHander implements Hander{
    @Override
    public void handle(FullHttpRequest request) {
        System.out.println("handle get request");
    }
}
