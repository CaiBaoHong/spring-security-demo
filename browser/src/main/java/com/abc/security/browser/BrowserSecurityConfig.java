package com.abc.security.browser;

import com.abc.security.core.authentication.AbstractChannelSecurityConfig;
import com.abc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.abc.security.core.properties.SecurityConstants;
import com.abc.security.core.properties.SecurityProperties;
import com.abc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();
        */

        /*
        http    .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //在/authentication/require映射的控制器中做登录跳转处理，区分ajax请求引发的登录和请求页面引发的登录
                .loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/form")
                .successHandler(browserAuthenticationSuccessHandler)
                .failureHandler(browserAuthenticationFailureHandler)

                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)//记住我功能用token从数据库拿到username之后，用这个UserDetailsService登录

                .and()
                .authorizeRequests()//开启访问控制
                .antMatchers("/authentication/require",
                        "/code/*",
                        //使用者配置的登录页，也需要放行
                        securityProperties.getBrowser().getLoginPage()).permitAll()
                .anyRequest()//任何请求
                .authenticated()//都需要鉴权（身份认证）
                .and()
                .csrf().disable()
                .apply(smsCodeAuthenticationSecurityConfig)
        ;
        */

        //公共的配置
        applyPasswordAuthenticationConfig(http);

        http
                //验证码配置
                .apply(validateCodeSecurityConfig)
                .and()

                //短信登录配置
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()

                //浏览器特有配置：记住我
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)//记住我功能用token从数据库拿到username之后，用这个UserDetailsService登录
                .and()

                //浏览器特有配置
                .authorizeRequests()//开启访问控制
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        securityProperties.getBrowser().getLoginPage()
                ).permitAll()
                .anyRequest()//任何请求
                .authenticated()//都需要鉴权（身份认证）
                .and()

                .csrf().disable()

        ;
    }

}
