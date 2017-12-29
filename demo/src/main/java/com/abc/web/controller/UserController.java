package com.abc.web.controller;

import com.abc.exception.UserNotExistException;
import com.abc.model.User;
import com.abc.model.UserQueryCondition;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.management.RuntimeMBeanException;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> query(UserQueryCondition condition,
                            @PageableDefault(page = 1,size = 15,sort = "username,asc") Pageable pageable){
        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
        System.out.println("分页参数：pageNumber: "+pageable.getPageNumber());
        System.out.println("分页参数：pageSize: "+pageable.getPageSize());
        System.out.println("分页参数：sort: "+pageable.getSort());
        return Arrays.asList(
                new User("张三"),
                new User("李四"),
                new User("王五"));
    }

    @JsonView(User.UserDetailView.class)
    @GetMapping("/{id:\\d+}")
    public User getInfo(@PathVariable String id){
//        throw new RuntimeException("用户不存在");
//        throw new UserNotExistException("1");
        return new User("tom");
    }


    @PostMapping
    public User create(@RequestBody @Valid User user){
//        if (bind.hasErrors()){
//            bind.getAllErrors().stream()
//                    .forEach(err-> System.out.println("Create User, Invalid Param:"+err.getDefaultMessage()));
//        }

        System.out.println("create user: "+ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@RequestBody @Valid User user, BindingResult bind){
        if (bind.hasErrors()){
            bind.getAllErrors().stream()
                    .forEach(err-> {
                        FieldError ferr = (FieldError)err;
                        System.out.println("Field: "+ferr.getField()+" Error Msg: "+ferr.getDefaultMessage());
                    });
        }

        System.out.println("create user: "+ReflectionToStringBuilder.toString(user,ToStringStyle.MULTI_LINE_STYLE));
        user.setId("1");
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id){
        System.out.println("Delete User, id: "+id);
    }

}
