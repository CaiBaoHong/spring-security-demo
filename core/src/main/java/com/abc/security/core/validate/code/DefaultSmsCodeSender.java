package com.abc.security.core.validate.code;

/**
 * 短信验证码发送接口的默认实现
 * @see {@link SmsCodeSender}
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("模拟短信发送，手机号："+mobile+"，验证码："+code);
    }


}
