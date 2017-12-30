package com.abc.security.core.validate.code;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成图形验证码的控制器
 */
@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = new ImageCodeGenerator().generate(request);
        //存入session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        setResponseImageCodeHeaders(response);
        ImageIO.write(imageCode.getImage(), "PNG", response.getOutputStream());
    }

    /**
     * 设置图片验证码相关的响应头
     *
     * @param response
     */
    private void setResponseImageCodeHeaders(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setContentType("image/png");
        long time = System.currentTimeMillis();
        response.setDateHeader("Expires", 0);
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }

}
