package com.abc.security.core.validate.code;

import com.abc.security.core.properties.SecurityConstants;
import com.abc.security.core.properties.SecurityProperties;
import com.abc.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    private Map<String,ValidateCodeType> urlMap = new HashMap<>();

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getFilterUrls(),ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getFilterUrls(),ValidateCodeType.SMS);

        logger.info("验证码过滤规则：{}",urlMap.keySet());

    }

    protected void addUrlToMap(String urlString,ValidateCodeType type){
        if (StringUtils.isNotBlank(urlString)){
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url,type);
            }
        }
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.info("检查验证码，url：{} {}",request.getRequestURI(),request.getMethod());
        ValidateCodeType type = getValidateCodeType(request);
        if (type==null){
            logger.warn("无法确认验证码类型，url：{}",request.getRequestURI());
        }
        if (type!=null){
            logger.info("校验请求（{}）中的验证码，验证码类型：{}",request.getRequestURI(),type);
            try {
                ValidateCodeProcessor processor = validateCodeProcessorHolder.findValidateCodeProcessor(type);
                processor.validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }







    private ValidateCodeType getValidateCodeType(HttpServletRequest request){
        ValidateCodeType result = null;
        boolean isGetMethod = StringUtils.equalsIgnoreCase(request.getMethod(),"get");
        if (!isGetMethod){
            Set<String> urlPatterns = urlMap.keySet();
            for (String urlPattern : urlPatterns) {
                if (pathMatcher.match(urlPattern,request.getRequestURI())){
                    //匹配上了，获取对应的验证码类型
                    result = urlMap.get(urlPattern);
                }
            }
        }
        return result;
    }




}
