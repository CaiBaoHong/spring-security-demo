package com.abc.security.core.validate.code;

/**
 * 短信验证码发送接口
 */
public interface SmsCodeSender {

    void send(String mobile, String code);


}
