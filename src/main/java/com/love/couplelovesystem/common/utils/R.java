package com.love.couplelovesystem.common.utils;

import com.love.couplelovesystem.common.constant.CommonConstants;

/**
 * 统一响应结果
 */
public class R {

    private int code;
    private String msg;
    private Object data;

    public R() {}

    private R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public static R ok() {
        return new R(CommonConstants.CODE_SUCCESS, "success", null);
    }

    public static R ok(Object data) {
        return new R(CommonConstants.CODE_SUCCESS, "success", data);
    }

    public static R ok(String msg, Object data) {
        return new R(CommonConstants.CODE_SUCCESS, msg, data);
    }

    public static R error(String msg) {
        return new R(CommonConstants.CODE_ERROR, msg, null);
    }

    public static R error(int code, String msg) {
        return new R(code, msg, null);
    }

    public static R unauthorized() {
        return new R(CommonConstants.CODE_UNAUTHORIZED, "请先登录", null);
    }
}
