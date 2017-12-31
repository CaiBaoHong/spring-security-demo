package com.abc.security.core.properties;

/**
 * 短信验证码配置
 */
public class SmsCodeProperties {

    private int length = 6;//验证码字符数
    private int expireIn = 180;//验证码过期期限(秒)
    private String filterUrls;//需要做图形验证码校验的url

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getFilterUrls() {
        return filterUrls;
    }

    public void setFilterUrls(String filterUrls) {
        this.filterUrls = filterUrls;
    }
}
