package com.megetood.geek.week02;


import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * todo
 *
 * @author Lei Chengdong
 * @date 2020/10/27
 */
public class HttpClientDemo {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8080");
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println(JSON.toJSONString(httpResponse,true));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
