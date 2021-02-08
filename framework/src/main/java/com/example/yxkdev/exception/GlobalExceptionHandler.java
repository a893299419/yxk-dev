package com.example.yxkdev.exception;

import com.example.yxkdev.pojo.GeneralResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理自定义的业务异常
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public GeneralResult<Object> restErrorHandler(HttpServletRequest request, BizException e) {
        String err = "requestURI:" + request.getRequestURI() + ",errorCode:" + e.getErrorCode()
                + ",errorMsg:" + e.getErrorMsg();
        log.error(err, e);
        return GeneralResult.genErrorResult(e.getMessage(), e.getErrorCode(), e.getTraceId());
    }

    // 处理接口参数数据格式错误异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public GeneralResult<Object> errorHandler(HttpServletRequest request,
                                              MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        String err = null;
        e.getBindingResult().getAllErrors()
                .forEach(error -> message.append(error.getDefaultMessage()).append(";"));
        String des = message.toString();
        if (!StringUtils.isEmpty(des)) {
            err = des.substring(0, des.length() - 1);
        }
        log.error(err + ",requestURI:" + request.getRequestURI(), e);
        return GeneralResult.genErrorResult(CommonErrorEnum.BODY_NOT_MATCH.getErrorMsg(),
                CommonErrorEnum.BODY_NOT_MATCH.getErrorCode(), MDC.get("traceId"));
    }

    // 处理其他异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public GeneralResult<Object> errorHandler(HttpServletRequest request, Exception e) {
        log.error("internal server error,requestURI:" + request.getRequestURI(), e);
        return GeneralResult.genErrorResult(CommonErrorEnum.INTERNAL_SERVER_ERROR.getErrorMsg(),
                CommonErrorEnum.INTERNAL_SERVER_ERROR.getErrorCode(), MDC.get("traceId"));
    }

}
