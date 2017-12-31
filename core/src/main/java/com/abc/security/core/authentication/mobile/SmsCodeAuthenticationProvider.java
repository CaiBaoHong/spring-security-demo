package com.abc.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider{

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //用手机号加载用户信息
        String mobile = authentication.getName();
        UserDetails user = userDetailsService.loadUserByUsername(mobile);
        if (user==null){
            throw new InternalAuthenticationServiceException("无法加载手机号（"+mobile+"）对应的用户信息");
        }
        //构造验证成功的AuthenticationToken返回
        SmsCodeAuthenticationToken authResult = new SmsCodeAuthenticationToken(mobile,authentication.getAuthorities());
        authResult.setDetails(authentication.getDetails());
        return authResult;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsCodeAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
