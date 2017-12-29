package com.abc.web.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

//@Component
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Time filter init...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Time filter start------");
        long start = new Date().getTime();
        chain.doFilter(request,response);
        long time = new Date().getTime() - start;
        System.out.println("Time filter finish------, time: "+time+" ms");
    }

    @Override
    public void destroy() {
        System.out.println("Time filter destroy...");
    }

}
