package com.fly.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务运行时异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizRuntimeException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int code=1;
    private String error="业务异常";

    public BizRuntimeException(String msg) {
        super(msg);
    }

    public BizRuntimeException(int code, String error, String msg) {
        super(msg);
        this.code = code;
        this.error = error;
    }

    public BizRuntimeException(int code, String error, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.error = error;
    }
}
