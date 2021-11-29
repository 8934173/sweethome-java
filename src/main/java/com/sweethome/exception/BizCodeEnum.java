package com.sweethome.exception;


/**
 * 状态码
 */
public enum BizCodeEnum {
    VALID_EXCEPTION(1000, "参数格式校验失败"),
    UNKNOWN_EXCEPTION(1001, "未知异常"),
    SAVE_EXCEPTION(1003, "保存失败"),
    LOGIN_FAIL(401, "登录失败，请重新登录"),
    NOT_LOGIN(401, "未登录"),
    CAPTCHA_EXCEPTION(1002, "验证码不正确");

    private final int code;

    private final String msg;

    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
