package com.abc.security.core.validate.code.impl;

import com.abc.security.core.validate.code.*;
import com.abc.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 利用spring的依赖查找注入验证码生成器
     */
    @Autowired
    private Map<String,ValidateCodeGenerator> validateCodeGenerators;


    @Override
    public void create(ServletWebRequest request) throws Exception {
        //生成验证码
        C validateCode = generate(request);
        //保存验证码
        save(request,validateCode);
        //发送验证码
        send(request,validateCode);
    }

    /**
     * 生成验证码
     * @param request
     * @return
     */
    private C generate(ServletWebRequest request){
        //从请求中获取验证码类型信息，如：/code/sms-->sms，或/code/image-->image
        String type = getProcessorType(request);
        String key = type+"CodeGenerator";
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(key);
        return (C)validateCodeGenerator.generate(request);
    }

    /**
     * 保存验证码
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        String key = SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase();
        sessionStrategy.setAttribute(request,key,validateCode);
    }

    /**
     * 发送验证码
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/code/");
    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @return
     */
    private ValidateCodeType getValidateCodeType() {
        // 如： smsCodeProcessor --> sms
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        // 如： sms -->  SMS -- ValidateCodeType.SMS
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 构建验证码放入session时的key
     *
     * @return
     */
    private String getSessionKey() {
        return SESSION_KEY_PREFIX + getValidateCodeType().toString().toUpperCase();
    }

    @Override
    public void validate(ServletWebRequest request) {
        //从请求中获取验证码类型
        ValidateCodeType type = getValidateCodeType();
        String sessionKey = getSessionKey();

        C codeInSession = (C) sessionStrategy.getAttribute(request,sessionKey);

        String codeInRequest = null;
        try {
            String paramName = type.getParamNameOnValidate();
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), paramName);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确");
        }

        sessionStrategy.removeAttribute(request, sessionKey);

    }
}
