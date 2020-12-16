package com.megetood.mrpc.demo.provider;

import com.megetood.mrpc.autoconfigure.EnableMrpc;
import com.megetood.mrpc.api.MrpcRequest;
import com.megetood.mrpc.api.MrpcResponse;
import com.megetood.mrpc.server.MrpcInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableMrpc
public class MrpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MrpcServerApplication.class, args);
	}

	@Autowired
	MrpcInvoker invoker;

	@PostMapping("/")
	public MrpcResponse invoke(@RequestBody MrpcRequest request) {
		return invoker.invoke(request);
	}

//	@Bean
//	public MrpcInvoker createInvoker(@Autowired MrpcResolver resolver){
//		return new MrpcInvoker(resolver);
//	}
//
//	@Bean
//	public MrpcResolver createResolver(){
//		return new DemoResolver();
//	}

	// 能否去掉name
	//
//	@Bean(name = "com.megetood.mrpc.demo.api.UserService")
//	public UserService createUserService(){
//		return new UserServiceImpl();
//	}
//
//	@Bean(name = "com.megetood.mrpc.demo.api.OrderService")
//	public OrderService createOrderService(){
//		return new OrderServiceImpl();
//	}

}
