package com.abc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring Security自定义配置
 */
@ConfigurationProperties(prefix = "custom.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();
    private ValidateCodeProperties code = new ValidateCodeProperties();

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
