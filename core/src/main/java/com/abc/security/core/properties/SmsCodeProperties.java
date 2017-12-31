package com.abc.security.core.properties;

/**
 * 短信验证码配置
 */
public class SmsCodeProperties {

    private int charNum = 6;//验证码字符数
    private int expireIn = 180;//验证码过期期限(秒)
    private String filterUrls;//需要做图形验证码校验的url

    public String getFilterUrls() {
        return filterUrls;
    }

    public void setFilterUrls(String filterUrls) {
        this.filterUrls = filterUrls;
    }

    public int getCharNum() {
        return charNum;
    }

    public void setCharNum(int charNum) {
        this.charNum = charNum;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}
