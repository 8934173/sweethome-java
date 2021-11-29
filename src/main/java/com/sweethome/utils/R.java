package com.sweethome.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果封装
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R () {
        put("code", 200);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常");
    }

    public static R error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static R error(Integer code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
