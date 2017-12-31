package com.abc.security.core.validate.code;

import com.abc.security.core.properties.SecurityProperties;
import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.font.RandomFontFactory;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.util.Random;

public class ImageCodeGenerator implements ValidateCodeGenerator {

    private SecurityProperties securityProperties;

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public ImageCode generate(ServletWebRequest request) {
        Integer width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                securityProperties.getCode().getImage().getWidth());
        Integer height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                securityProperties.getCode().getImage().getHeight());
        Integer length = ServletRequestUtils.getIntParameter(request.getRequest(), "charNum",
                securityProperties.getCode().getImage().getCharNum());
        Integer fontSize = ServletRequestUtils.getIntParameter(request.getRequest(), "fontSize",
                securityProperties.getCode().getImage().getFontSize());
        Integer expireIn = ServletRequestUtils.getIntParameter(request.getRequest(), "expireIn",
                securityProperties.getCode().getImage().getExpireIn());
        Integer hard = ServletRequestUtils.getIntParameter(request.getRequest(), "hard",
                securityProperties.getCode().getImage().getHard());
        return generateImageCode(width,height,length,fontSize,hard,expireIn);
    }

    private ImageCode generateImageCode(int width,int height,int length,int fontSize, int hard,int expireIn) {

        ConfigurableCaptchaService captchaService = new ConfigurableCaptchaService();
        //word
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setMaxLength(length);// 字数
        wordFactory.setMinLength(length);
        //font
        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMinSize(fontSize);//字体大小
        fontFactory.setMaxSize(fontSize);
        //color
        ColorFactory colorFactory = new MultiColorFactory();

        captchaService.setWidth(width);//宽
        captchaService.setHeight(height);//高
        captchaService.setWordFactory(wordFactory);
        captchaService.setFontFactory(fontFactory);
        captchaService.setColorFactory(colorFactory);
        captchaService.setFilterFactory(getFilterFactory(colorFactory, hard));//难度

        Captcha captcha = captchaService.getCaptcha();
        return new ImageCode(captcha.getImage(), captcha.getChallenge(), expireIn);

    }

    /**
     * 自定义的颜色工厂，可产出多种颜色
     */
    class MultiColorFactory implements ColorFactory {
        @Override
        public Color getColor(int index) {
            Random random = new Random();
            //RGB红绿蓝三个色位
            int[] c = new int[3];
            int i = random.nextInt(c.length);
            for (int j = 0; j < c.length; j++) {
                if (j == i) {
                    c[j] = random.nextInt(71);
                } else {
                    c[j] = random.nextInt(256);
                }
            }
            return new Color(c[0], c[1], c[2]);
        }

    }

    /**
     * 根据指定的难度获取一个过滤器
     *
     * @param colorFactory 颜色工厂
     * @param hard         难度，数字越大难道越高
     * @return
     */
    private FilterFactory getFilterFactory(ColorFactory colorFactory, int hard) {
        switch (hard) {
            case 1:
                return new WobbleRippleFilterFactory();//1
            case 2:
                return new DoubleRippleFilterFactory();//2
            case 3:
                return new CurvesRippleFilterFactory(colorFactory);//3
            case 4:
                return new DiffuseRippleFilterFactory();//4
            case 5:
                return new MarbleRippleFilterFactory();//5
            default:
                return new CurvesRippleFilterFactory(colorFactory);//3
        }
    }

}
