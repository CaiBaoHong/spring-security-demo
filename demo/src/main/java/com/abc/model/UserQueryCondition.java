package com.abc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class UserQueryCondition {

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户年龄起始值")
    private int age;
    @ApiModelProperty(value = "用户年龄结束值")
    private int ageTo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }
}
