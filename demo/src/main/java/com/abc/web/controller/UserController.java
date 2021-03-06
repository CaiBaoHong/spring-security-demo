package com.abc.web.controller;

import com.abc.exception.UserNotExistException;
import com.abc.model.User;
import com.abc.model.UserQueryCondition;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    //获取认证用户信息
    @GetMapping("/me")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails /*Authentication authentication*/){
        return userDetails;
    }

    @ApiOperation(value = "查询用户")
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

    @ApiOperation(value = "查询用户详情")
    @JsonView(User.UserDetailView.class)
    @GetMapping("/{id:\\d+}")
    public User getInfo(@ApiParam(value = "用户id")
                            @PathVariable String id){
//        throw new RuntimeException("用户不存在");
//        throw new UserNotExistException("1");
        return new User("tom");
    }


    @ApiOperation(value = "创建用户")
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

    @ApiOperation(value = "更新用户")
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

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id){
        System.out.println("Delete User, id: "+id);
    }

}
