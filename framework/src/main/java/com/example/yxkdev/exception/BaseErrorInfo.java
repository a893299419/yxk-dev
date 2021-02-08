package com.example.yxkdev.exception;

public interface BaseErrorInfo {

    /**
     * 获取错误码
     * @return
     */
    String getErrorCode();

    /**
     * 获取错误信息
     * @return
     */
    String getErrorMsg();
}
