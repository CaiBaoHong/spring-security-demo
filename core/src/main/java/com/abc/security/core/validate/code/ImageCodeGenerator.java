package com.abc.security.core.validate.code;

import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.font.RandomFontFactory;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.Random;

public class ImageCodeGenerator {

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
     * 随机获取一个过滤器
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

    /**
     * 创建一个图片验证码
     *
     * @param request
     * @return
     */
    public ImageCode generate(HttpServletRequest request) {

        ConfigurableCaptchaService captchaService = new ConfigurableCaptchaService();

        //word
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setMaxLength(4);// 字数
        wordFactory.setMinLength(4);

        //font
        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMinSize(20);//字体大小
        fontFactory.setMaxSize(20);

        //color
        ColorFactory colorFactory = new MultiColorFactory();

        captchaService.setWidth(100);//宽
        captchaService.setHeight(30);//高
        captchaService.setWordFactory(wordFactory);
        captchaService.setFontFactory(fontFactory);
        captchaService.setColorFactory(colorFactory);
        captchaService.setFilterFactory(getFilterFactory(colorFactory, 1));//难度


        /*
        ConfigurableCaptchaService captchaService = new ConfigurableCaptchaService();
        captchaService.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        captchaService.setFilterFactory(new CurvesRippleFilterFactory(captchaService.getColorFactory()));
        */

        Captcha captcha = captchaService.getCaptcha();
        return new ImageCode(captcha.getImage(), captcha.getChallenge(), 60);

    }


}
