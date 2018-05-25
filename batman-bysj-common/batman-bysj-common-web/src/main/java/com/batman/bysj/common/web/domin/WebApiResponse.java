package com.batman.bysj.common.web.domin;

/**
 * @author victor.qin
 * @date 2018/5/25 12:22
 */
public class WebApiResponse<T> {
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = 1;

    private int code;
    private String error;
    private T data;

    public static <T> WebApiResponse<T> success(T data) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setCode(SUCCESS_CODE);
        response.setData(data);
        return response;
    }

    public static <T> WebApiResponse<T> error(String message, int code) {
        return WebApiResponse.error(message, ERROR_CODE);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
