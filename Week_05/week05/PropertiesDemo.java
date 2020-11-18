package com.megetood.geek.week05;

import com.megetood.geek.week05.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * todo
 *
 * @author Lei Chengdong
 * @date 2020/11/18
 */
public class PropertiesDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);

        String location = "/META-INF/user.properties";
        Resource resource = new ClassPathResource(location);
        EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
        int beanNums = reader.loadBeanDefinitions(encodedResource);

        System.out.println("already load beandefinition nums:"+beanNums);

        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
    }
}
