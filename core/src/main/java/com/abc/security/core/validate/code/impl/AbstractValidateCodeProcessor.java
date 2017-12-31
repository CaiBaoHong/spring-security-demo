package com.abc.security.core.validate.code.impl;

import com.abc.security.core.validate.code.ValidateCode;
import com.abc.security.core.validate.code.ValidateCodeGenerator;
import com.abc.security.core.validate.code.ValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
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

    @Override
    public void validate(ServletWebRequest request) throws Exception {

    }
}
