package com.love.couplelovesystem.common.exception;

import com.love.couplelovesystem.common.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.error("文件上传超过大小限制", e);
        return R.error("文件大小超过限制（图片最大10MB，音频最大20MB）");
    }

    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error("系统异常", e);
        return R.error("服务器开小差了，请稍后再试");
    }
}
