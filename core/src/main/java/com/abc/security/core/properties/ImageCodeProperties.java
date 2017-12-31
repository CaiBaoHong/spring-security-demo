package com.abc.security.core.properties;

public class ImageCodeProperties extends SmsCodeProperties{

    private int width = 100; //图片宽度
    private int height = 30; //图片高度
    private int fontSize = 20; //字体大小
    private int hard = 1; //验证码难度

    public ImageCodeProperties() {
        //默认图片验证码的长度
        setCharNum(4);
    }

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
}
