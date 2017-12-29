package com.abc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * 切片（类）
 * 切片包含：切入点 + 增强
 */
//@Aspect
//@Component
public class TimeAspect {

    /**
     * 切入点类型:
     *  @Before() //前置
     *  @After() //后置
     *  @AfterThrowing //抛出异常
     *  @Around() //环绕
     */
    @Around("execution(* com.abc.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        //函数内的逻辑，就是增强
        System.out.println("Time Aspect start...");

        //通过ProceeingJoinPoint对象可以拿到切入方法的参数
        Object[] args = pjp.getArgs();
        System.out.println("Time Aspect, args: "+ Arrays.toString(args));

        long start = new Date().getTime();
        Object result = pjp.proceed();
        System.out.println("Time Aspect, 耗时："+(new Date().getTime()-start));

        System.out.println("Time Aspect end...");

        return result;

    }


}
