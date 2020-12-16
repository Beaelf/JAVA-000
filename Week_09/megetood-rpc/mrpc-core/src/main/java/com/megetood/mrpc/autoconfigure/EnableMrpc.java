package com.megetood.mrpc.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mrpc启动注解，启动类上使用
 *
 * @author Lei Chengdong
 * @date 2020/12/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(MrpcConfiguration.class)
public @interface EnableMrpc {
}
