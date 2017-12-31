package com.abc.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateCodeFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private AuthenticationFailureHandler authenticationFailureHandler;

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/authentication/form",request.getRequestURI())
                && StringUtils.endsWithIgnoreCase("POST",request.getMethod())){
            try {
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * 校验 验证码 是否正确
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        String sessionKey = ValidateCodeController.SESSION_KEY;

        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,sessionKey);

        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }


}
