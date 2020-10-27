package com.megetood.geek.week02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http server 01
 *
 * @author Lei Chengdong
 * @date 2020/10/26
 */
public class HttpServer03 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> {
                    service(socket);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println();
            printWriter.write("hello nio");
            printWriter.close();
            socket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
