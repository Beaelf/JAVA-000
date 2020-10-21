package com.megetood.geek.week01.hello;

import java.io.*;

/**
 * Hello.xlass类加载器
 *
 * @author Lei Chengdong
 * @date 2020/10/16
 */
public class HelloClassLoader extends ClassLoader {
    /**
     * 类路径
     */
    private String path;

    public HelloClassLoader(String path) {
        this.path = path;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> cls = null;

        byte[] byteArr = getByteArr();

        if (byteArr != null && byteArr.length != 0) {
            // 255-x逻辑
            for (int i = 0; i < byteArr.length; i++) {
                byteArr[i] = (byte) (255 - byteArr[i]);
            }
            return defineClass(name, byteArr, 0, byteArr.length);
        }

        return cls;
    }

    /**
     * 根据文件路径获取文件字节流数组
     *
     * @return 字节流数组
     */
    public byte[] getByteArr() {
        File file = new File(this.path);

        if (file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file);
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, size);
                }

                return outputStream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getPath() {
        return path;
    }
}
