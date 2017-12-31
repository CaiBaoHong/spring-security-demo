package com.abc.security.core.validate.code;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 定义验证码发送和验证的接口
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建验证码
     * @param request
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     * @param request
     */
    void validate(ServletWebRequest request);

}
