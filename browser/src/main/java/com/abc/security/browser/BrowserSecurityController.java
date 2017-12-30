package com.abc.security.browser;

import com.abc.security.browser.support.SimpleResponse;
import com.abc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 区分引发登录跳转的请求。如果是页面请求，则跳转到登录页面；如果是非页面请求（ajax），则返回401
 */
@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //工具类：请求缓存
    private RequestCache requestCache = new HttpSessionRequestCache();

    //工具类：请求跳转
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * 当需要身份认证的时候，跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //尝试从请求缓存中获取引发跳转的请求
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if (savedRequest!=null){
            //target就是引发跳转的请求的url
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是：{}",targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl,".html")){
                //跳转到使用者配置的登录页面
                redirectStrategy.sendRedirect(request,response,securityProperties.getBrowser().getLoginPage());
            }
        }
        return new SimpleResponse("user_unauthorized");

    }

}
