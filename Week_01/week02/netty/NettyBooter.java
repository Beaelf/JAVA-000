package com.megetood.geek.week02.netty;


import com.megetood.geek.week02.netty.websocket.WSServer;

public class NettyBooter {

	public static void main(String[] args) {
		WSServer.getInstance().start();
	}

}
