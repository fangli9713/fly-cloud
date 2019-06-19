package com.fly.common.core.convert;

import com.fly.common.core.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataResult<T> {


    private Integer status;
    private String info;
    private String message;
    private T data;

    public DataResult<T> success() {
        this.status = ErrorCode.SUCCESS_CODE.getCode();
        this.info = ErrorCode.SUCCESS_CODE.getMsg();
        this.message = info;
        return this;
    }


    public DataResult<T> success(T data) {
        this.status = ErrorCode.SUCCESS_CODE.getCode();
        this.info = ErrorCode.SUCCESS_CODE.getMsg();
        this.message = info;
        this.data = data;
        return this;
    }


    public DataResult<T> fail(String message) {
        this.status = ErrorCode.FAIL_CODE.getCode();
        this.info = ErrorCode.FAIL_CODE.getMsg();
        this.message = message;
        return this;
    }

    public DataResult<T> fail(int status, String info, String message) {
        this.status = status;
        this.info = info;
        this.message = message;
        return this;
    }
}