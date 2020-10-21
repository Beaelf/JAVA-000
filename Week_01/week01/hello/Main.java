package com.megetood.geek.week01.hello;

import com.megetood.geek.week01.hello.HelloClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Lei Chengdong
 * @date 2020/10/16
 */
public class Main {
    /*
        自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内
        容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。
     */
    public static void main(String[] args) {
        String path = "D:\\worksapce\\ideaspace\\enhance" +
                "\\megetood-geek\\src\\main\\java\\com\\megetood\\geek\\week01\\hello\\Hello.xlass";
        String className = "Hello";

        HelloClassLoader helloClassLoader = new HelloClassLoader(path);

        try {
            Class<?> cls = helloClassLoader.findClass(className);
            if (cls == null) {
                throw new ClassNotFoundException("not found class " + className);
            }

            cls.getMethod("hello").invoke(cls.newInstance());
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
