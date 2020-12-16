package com.megetood.mrpc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 异常
 *
 * @author Lei Chengdong
 * @date 2020/12/16
 */
public class MrpcException extends RuntimeException{
    private static final Logger logger = LoggerFactory.getLogger(MrpcException.class);

    private final transient Object[] parameters;

    private String code;

    /**
     * 构造器
     *
     * @param code       异常code
     * @param parameters parameters
     */
    public MrpcException(String code, Object... parameters) {
        super(code, null, true, false);
        this.parameters = parameters;
        this.code = code;
    }

    public MrpcException(String code, Throwable cause, Object... parameters) {
        super(code, cause, true, false);
        this.parameters = parameters;
        this.code = code;
    }

    public MrpcException(String code, Throwable cause) {
        super(code, cause, true, false);
        this.code = code;
        this.parameters = new Object[]{};
    }


    public MrpcException(Throwable cause, Object... parameters) {
        super(null, cause, true, false);
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getCode() {
        return code;
    }

    public String getTrace() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps;
        try {
            ps = new PrintStream(baos, false, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            logger.error("Error get trace, unsupported encoding.", e);
            return null;
        }
        this.printStackTrace(ps);
        ps.flush();
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("message", super.getMessage());
        return map;
    }

}
