package com.abc.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class TimeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("TimeInterceptor preHandle...");

        HandlerMethod han = (HandlerMethod) handler;
        System.out.println("TimeInterceptor handler.bean: "+han.getBean().getClass().getName());
        System.out.println("TimeInterceptor handler.method: "+han.getMethod().getName());

        request.setAttribute("startTime",new Date().getTime());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("TimeInterceptor postHandle...");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("TimeInterceptor postHandle... 耗时："+(new Date().getTime()-start));

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("TimeInterceptor afterCompletion...");
        Long start = (Long) request.getAttribute("startTime");
        System.out.println("TimeInterceptor afterCompletion... 耗时："+(new Date().getTime()-start));
        System.out.println("TimeInterceptor afterCompletion... exception："+ex);
    }
}
