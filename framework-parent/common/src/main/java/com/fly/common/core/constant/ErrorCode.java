package com.fly.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS_CODE(0, "Success"),
    FAIL_CODE(1, "Fail"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_AUTHORIZATION(401, "NotAuthorization"),
    NOT_FOUND_REQUEST(404, "Not Found Request Path"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    LOGIN_FIRST(999, "Please Log In First"),

    RUNTIME_EXCEPTION(1000, "Server RunTimeException"),
    NULL_POINTER_EXCEPTION(1001, "Server NullPointerException"),
    CLASS_CAST_EXCEPTION(1002, "Server ClassCastException"),
    IO_EXCEPTION(1003, "Server IOException"),
    NO_SUCH_METHOD_EXCEPTION(1004, "Server NoSuchMethodException"),
    INDEX_OUT_OF_BOUNDS_EXCEPTION(1005, "Server IndexOutOfBoundsException"),
    CONNECT_EXCEPTION(1006, "Server ConnectException"),
    ERROR_MEDIA_TYPE(1007, "Content-type Error"),
    EMPTY_REQUEST_BOYD(1008, "Params Body Can Not Emply"),
    ERROR_REQUEST_BOYD(1009, "Params Body Error"),
    ERROR_VERSION(2000, "Version Error"),
    ERROR_FORMAT_PARAMETER(2001, "Parameter Format Error");


    private Integer code;
    private String msg;


}
