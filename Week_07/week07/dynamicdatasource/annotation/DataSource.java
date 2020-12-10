package com.megetood.geek.week07.dynamicdatasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切换数据注解 可以用于类或者方法级别 方法级别优先级 > 类级别
 *
 * @author Lei Chengdong
 * @date 2020/12/9
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    /**
     * key值
     * @return
     */
    String value() default "master";
}