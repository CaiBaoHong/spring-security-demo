package com.abc.code;

import com.abc.security.core.properties.SecurityProperties;
import com.abc.security.core.validate.code.image.ImageCode;
import com.abc.security.core.validate.code.ValidateCodeGenerator;
import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.color.SingleColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.font.RandomFontFactory;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 模拟一个用户自定义的图片验证码生成器，用于测试是否支持图片验证码生成逻辑可配置
 */
//@Component("imageCodeGenerator")
public class CustomImageCodeGenerator implements ValidateCodeGenerator {

    //@Autowired
    private SecurityProperties securityProperties;

    @Override
    public ImageCode generate(ServletWebRequest request) {
        Integer width = 300;
        Integer height = 80;
        Integer length = ServletRequestUtils.getIntParameter(request.getRequest(), "charNum",
                securityProperties.getCode().getImage().getLength());
        Integer fontSize = ServletRequestUtils.getIntParameter(request.getRequest(), "fontSize",
                securityProperties.getCode().getImage().getFontSize());
        Integer expireIn = ServletRequestUtils.getIntParameter(request.getRequest(), "expireIn",
                securityProperties.getCode().getImage().getExpireIn());
        Integer hard = ServletRequestUtils.getIntParameter(request.getRequest(), "hard",
                securityProperties.getCode().getImage().getHard());
        return generateImageCode(width, height, length, fontSize, hard, expireIn);
    }

    private ImageCode generateImageCode(int width, int height, int length, int fontSize, int hard, int expireIn) {

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
        ColorFactory colorFactory = new SingleColorFactory();

        captchaService.setWidth(width);//宽
        captchaService.setHeight(height);//高
        captchaService.setWordFactory(wordFactory);
        captchaService.setFontFactory(fontFactory);
        captchaService.setColorFactory(colorFactory);
        captchaService.setFilterFactory(new CurvesRippleFilterFactory(colorFactory));//难度

        Captcha captcha = captchaService.getCaptcha();
        return new ImageCode(captcha.getImage(), captcha.getChallenge(), expireIn);

    }


}
