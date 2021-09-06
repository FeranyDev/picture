package com.example.picture.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.picture.util.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = com.example.picture.exception.BizException.class)
    @ResponseBody
    public ResultResponse bizExceptionHandler(HttpServletRequest req, com.example.picture.exception.BizException e) {
        logger.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultResponse.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse nullPointerExceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.BODY_NOT_MATCH);
    }


    /**
     * 处理空数据的异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResultResponse bindExceptionHandler(HttpServletRequest req, BindException e) {
        logger.error("发生空字段异常！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.DATA_NULL);
    }


    /**
     * 空文件上传异常
     */
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseBody
    public ResultResponse missingServletRequestPartExceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("所需的请求部分为空！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.DATA_NULL);
    }


    /**
     * 超出大小异常
     */
    @ExceptionHandler(value = SizeLimitExceededException.class)
    @ResponseBody
    public ResultResponse sizeLimitExceededExceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("上传文件过大！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.DATA_NULL);
    }

    /**
     * 未登录异常处理
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ResultResponse notLoginExceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未登录！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.UNAUTHORIZED);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultResponse.error(com.example.picture.exception.ExceptionEnum.INTERNAL_SERVER_ERROR);
    }


}