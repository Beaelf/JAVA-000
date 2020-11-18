package com.megetood.geek.week05;

import com.megetood.geek.week05.domain.User;
import com.megetood.geek.week05.enums.City;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * annotation define bean demo
 *
 * @author Lei Chengdong
 * @date 2020/11/18
 */
public class AnnotationDemo {
    @Bean
    public User user(){
        User user = new User();
        user.setId(1L);
        user.setCity(City.BEIJING);
        user.setName("megetood");
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDemo.class);
        applicationContext.refresh();


        applicationContext.close();
    }
}
