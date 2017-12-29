package com.abc.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mmvc;

    @Before
    public void setup() {
        mmvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenQuerySuccess() throws Exception {
        String resBody = mmvc.perform(get("/user")
                .param("username", "Tom")
                .param("age", "3")
                .param("ageTo", "18")
                .param("page", "3")
                .param("size", "20")
                .param("sort", "age,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenQuerySuccess, response: " + resBody);
    }

    @Test
    public void whenGetInfoSuccess() throws Exception {
        String resBody = mmvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenGetInfoSuccess, response: " + resBody);
    }

    @Test
    public void whenGetInfoFail() throws Exception {
        String resBody = mmvc.perform(get("/user/abc")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenGetInfoFail, response: " + resBody);
    }

    @Test
    public void whenCreateSuccess() throws Exception{
        long now = new Date().getTime();
        String content = "{\"username\":\"cai\",\"password\":\"222\",\"birthday\":"+now+"}";
        String resBody = mmvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenCreateSuccess, response: " + resBody);
    }

    @Test
    public void whenUpdateSuccess() throws Exception{
        long now = new Date().getTime();
        String content = "{\"username\":\"cai\",\"password\":\"222\",\"birthday\":"+now+"}";
        String resBody = mmvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenUpdateSuccess, response: " + resBody);
    }

    @Test
    public void whenUpdateBitthdayInvalid() throws Exception{
        long oneYearLater = LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String content = "{\"username\":\"cai\",\"password\":\"222\",\"birthday\":"+oneYearLater+"}";
        String resBody = mmvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenUpdateFail, response: " + resBody);
    }

    @Test
    public void whenDeleteSuccess() throws Exception{
        mmvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUploadSuccess() throws Exception{
        String resBody = mmvc.perform(fileUpload("/file")
                .file(new MockMultipartFile("file",
                        "test.txt",
                        "multipart/form-data",
                        "hello choi".getBytes("UTF-8"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("whenUploadSuccess, response: " + resBody);
    }

}
