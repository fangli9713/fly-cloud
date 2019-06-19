package com.fly.common.core.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fly.common.core.constant.ErrorCode;
import com.fly.common.core.convert.DataResult;
import com.fly.common.util.LocaleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.ConnectException;

/**
 * 全局异常
 *
 * @author guoyong1
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Resource
    private LocaleUtil localeUtil;

    /**
     * 运行时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public DataResult<String> runtimeExceptionHandler(RuntimeException ex) {
        return result(ErrorCode.RUNTIME_EXCEPTION.getCode(), ex);
    }

    /**
     * 空指针异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public DataResult<String> nullPointerExceptionHandler(NullPointerException ex) {
        return result(ErrorCode.NULL_POINTER_EXCEPTION.getCode(), ex);
    }

    /**
     * 类型转换异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public DataResult<String> classCastExceptionHandler(ClassCastException ex) {
        return result(ErrorCode.CLASS_CAST_EXCEPTION.getCode(), ex);
    }

    /**
     * IO异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public DataResult<String> ioExceptionHandler(IOException ex) {
        return result(ErrorCode.IO_EXCEPTION.getCode(), ex);
    }

    /**
     * 未知方法异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public DataResult<String> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return result(ErrorCode.NO_SUCH_METHOD_EXCEPTION.getCode(), ex);
    }

    /**
     * 数组越界异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public DataResult<String> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return result(ErrorCode.INDEX_OUT_OF_BOUNDS_EXCEPTION.getCode(), ex);
    }

    /**
     * 网络异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public DataResult<String> connectException(ConnectException ex) {
        return result(ErrorCode.CONNECT_EXCEPTION.getCode(), ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public DataResult<String> requestNotReadable(HttpMessageNotReadableException ex) {
        return result(ErrorCode.BAD_REQUEST.getCode(), ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public DataResult<String> requestTypeMismatch(TypeMismatchException ex) {
        return result(ErrorCode.BAD_REQUEST.getCode(), ex);
    }

    /**
     * 400错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public DataResult<String> requestMissingServletRequest(MissingServletRequestParameterException ex) {
        return result(ErrorCode.BAD_REQUEST.getCode(), ex);
    }

    @ExceptionHandler({ServletException.class})
    @ResponseBody
    public DataResult<String> http404(ServletException ex) {
        return result(ErrorCode.NOT_FOUND_REQUEST.getCode(), ex);
    }

    /**
     * 405错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public DataResult<String> request405(HttpRequestMethodNotSupportedException ex) {
        return result(ErrorCode.METHOD_NOT_ALLOWED.getCode(), ex);
    }

    /**
     * 406错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public DataResult<String> request406(HttpMediaTypeNotAcceptableException ex) {
        return result(ErrorCode.NOT_ACCEPTABLE.getCode(), ex);
    }

    /**
     * 500错误
     *
     * @param runtimeException
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public DataResult<String> server500(RuntimeException runtimeException) {
        return result(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), runtimeException);
    }

    /**
     * BizRuntimeException 业务异常
     *
     * @param bizRuntimeException
     * @return
     */
    @ExceptionHandler({BizRuntimeException.class})
    @ResponseBody
    public DataResult<String> appWebException(BizRuntimeException bizRuntimeException) {
        return result(bizRuntimeException.getCode(), bizRuntimeException);
    }


    @ExceptionHandler({JsonMappingException.class})
    @ResponseBody
    public DataResult<String> jsonMappingException(JsonMappingException jsonMappingException) {
        return result(ErrorCode.ERROR_FORMAT_PARAMETER.getCode(), jsonMappingException);
    }

    /**
     * 结果集
     *
     * @param errCode
     * @param e
     * @return
     */
    private DataResult<String> result(int errCode, BizRuntimeException e) {
        log.error(e.getMessage(), e);
        String localValue = localeUtil.getMessage(String.valueOf(errCode));
        if (!StringUtils.isEmpty(localValue)) {
            return new DataResult<String>().fail(errCode, localValue, e.getMessage());
        } else {
            return new DataResult<String>().fail(errCode, e.getError(), e.getMessage());
        }
    }

    private DataResult<String> result(int errCode, Exception e) {
        log.error(e.getMessage(), e);
        return new DataResult<String>().fail(errCode, localeUtil.getMessage(String.valueOf(errCode)), e.getMessage());
    }
}
