package com.abc.security.browser;

import com.abc.security.browser.authentication.BrowserAuthenticationFailureHandler;
import com.abc.security.browser.authentication.BrowserAuthenticationSuccessHandler;
import com.abc.security.core.properties.SecurityProperties;
import com.abc.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private BrowserAuthenticationSuccessHandler browserAuthenticationSuccessHandler;
    @Autowired
    private BrowserAuthenticationFailureHandler browserAuthenticationFailureHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private ValidateCodeFilter validateCodeFilter;

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

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
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
        ;
    }

}
