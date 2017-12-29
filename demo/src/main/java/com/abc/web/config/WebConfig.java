package com.abc.web.config;

import com.abc.web.filter.TimeFilter;
import com.abc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;


    //    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean();
        reg.setFilter(new TimeFilter());
        reg.addUrlPatterns("/*");
        return reg;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(timeInterceptor);
//    }


    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(new ThreadPoolTaskExecutor());
    }
}
