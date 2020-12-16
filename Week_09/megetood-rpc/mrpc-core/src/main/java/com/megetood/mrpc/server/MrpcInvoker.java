package com.megetood.mrpc.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.megetood.mrpc.api.MrpcRequest;
import com.megetood.mrpc.api.MrpcResponse;
import com.megetood.mrpc.exception.MrpcException;
import com.megetood.mrpc.util.ContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class MrpcInvoker {

    @Autowired
    private ContextUtil contextUtil;

    public MrpcResponse invoke(MrpcRequest request) {
        MrpcResponse response = new MrpcResponse();
        String serviceClass = request.getServiceClass();

        try {
            Object service = contextUtil.getBean(serviceClass);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams());

            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch ( IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.setException(new MrpcException(e));
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
