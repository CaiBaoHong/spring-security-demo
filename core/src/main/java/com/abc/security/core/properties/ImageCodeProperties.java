package com.abc.security.core.properties;

public class ImageCodeProperties extends SmsCodeProperties{

    private int width = 100;    //图片宽度
    private int height = 30;    //图片高度
    private int length = 4;     //验证码字符数
    private int expireIn = 180; //验证码过期期限(秒)
    private int fontSize = 20;  //字体大小
    private int hard = 1;       //验证码难度
    private String filterUrls;//需要做图形验证码校验的url

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int getExpireIn() {
        return expireIn;
    }

    @Override
    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    @Override
    public String getFilterUrls() {
        return filterUrls;
    }

    @Override
    public void setFilterUrls(String filterUrls) {
        this.filterUrls = filterUrls;
    }
}
