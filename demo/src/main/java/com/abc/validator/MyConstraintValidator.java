package com.abc.validator;

import com.abc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验注解的校验逻辑
 * 注意：这里MyConstraintValidator不用写@Component注解
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object>{

    @Autowired
    private HelloService helloService;

    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("My constraint validator init...");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        helloService.greeting("cai");
        System.out.println("My constraint validator value: "+value);
        return true;
    }

}
