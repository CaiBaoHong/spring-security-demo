package com.abc.security.core.validate.code;

import com.abc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实现图片验证码生成逻辑可配置
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 如果使用者自己注入了名为imageCodeGenerator的Bean，则不会注入core模块的默认实现的图片验证码生成逻辑。
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator gen = new ImageCodeGenerator();
        gen.setSecurityProperties(securityProperties);
        return gen;
    }

    /**
     * 让使用者可以自己定义短信验证码发送的逻辑
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender(){
        return new DefaultSmsCodeSender();
    }

}
