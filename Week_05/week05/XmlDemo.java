package com.megetood.geek.week05;

import com.megetood.geek.week05.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * xml 配置Bean
 *
 * @author Lei Chengdong
 * @date 2020/11/18
 */
public class XmlDemo {
    /*
    写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。
     */
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:/META-INF/spring-bean.xml");
        int first = beanFactory.getBeanDefinitionCount();
        User bean = beanFactory.getBean(User.class);
        System.out.println(first);
        System.out.println(bean);
    }
}
