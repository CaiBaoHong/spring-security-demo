package com.abc.web.wiremock;


import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockServer {

    @Test
    public void mockServer() throws IOException {
        configureFor(9999) ;
        removeAllMappings();
        mockGet("/order/1","order.json");
        mockGet("/user/1","user.json");
    }

    /**
     * 声明模拟Restful服务的响应内容
     * @param url
     * @param mockFile
     * @throws IOException
     */
    private void mockGet(String url, String mockFile) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+mockFile);
        String content = FileUtils.readFileToString(resource.getFile(),"UTF-8");
        System.out.println(content);
        stubFor(get(urlPathEqualTo(url))
                .willReturn(aResponse()
                        .withHeader("content-type","application/json;charset=utf-8")
                        .withBody(content)
                        .withStatus(200)));
    }

}
