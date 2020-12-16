package com.megetood.mrpc.demo.consumer;

import com.megetood.mrpc.client.Mrpc;
import com.megetood.mrpc.demo.api.Order;
import com.megetood.mrpc.demo.api.OrderService;
import com.megetood.mrpc.demo.api.User;
import com.megetood.mrpc.demo.api.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MrpcClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById

		UserService userService = Mrpc.create(UserService.class, "http://localhost:8080/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());

//		OrderService orderService = Mrpc.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));

		// 新加一个OrderService

        //  SpringApplication.run(MrpcClientApplication.class, args);
	}

}
