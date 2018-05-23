package com.batman.bysj.common.exception;

/**
 * 业务逻辑异常
 * Created by chuangyu.liang on 2016/12/27.
 *
 * @author chuanyu.liang
 * @version 3.0.0
 */
public class BusinessException extends RuntimeException {
    /**
     * 创建业务逻辑异常
     *
     * @param message 异常的描述信息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 创建业务逻辑异常
     *
     * @param message 异常的描述信息
     * @param cause   内部异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
